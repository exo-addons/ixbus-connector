package org.exoplatform.addons.ixbus;

import org.apache.ecs.Document;
import org.apache.ecs.wml.Do;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.exoplatform.addons.ixbus.entity.DocumentEntity;
import org.exoplatform.addons.ixbus.entity.SettingsEntity;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IxbusConnectorService {

  private static final String IXBUS_SERVICE_API = "ixbus";

  private final HttpClient httpClient;

  private String serverUrl ="";
  private           String apiKey    ="";
  protected final Log    LOG       = ExoLogger.getLogger(IxbusConnectorService.class);

  private String currentSessionToken=null;
  private long timeStampCreationSessionToken = 0;
  private long sessionTokenLiveTime = 86400L;
  private Map<String,String> userIdentifiers;
  private static final int     DEFAULT_POOL_CONNECTION = 100;
  private OrganizationService organizationService;


  public IxbusConnectorService(InitParams params, OrganizationService organizationService) {
    if (params.getValueParam("serverUrl") != null) {
      this.serverUrl = params.getValueParam("serverUrl").getValue();
    }
    if (params.getValueParam("apiKey") != null) {
      this.apiKey = params.getValueParam("apiKey").getValue();
    }
    if (serverUrl.isEmpty() || apiKey.isEmpty()) {
      LOG.error("IxbusConnector service is not correctly configured. Check serverUrl and apiKey.");
    }
    userIdentifiers = new HashMap<>();

    this.organizationService = organizationService;

    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setDefaultMaxPerRoute(DEFAULT_POOL_CONNECTION);
    HttpClientBuilder httpClientBuilder = HttpClients.custom()
                                                     .setConnectionManager(connectionManager)
                                                     .setConnectionReuseStrategy(new DefaultConnectionReuseStrategy())
                                                     .setMaxConnPerRoute(DEFAULT_POOL_CONNECTION)
                                                     .setRedirectStrategy(new RedirectStrategy() {
                                                       @Override
                                                       public boolean isRedirected(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
                                                         return false;
                                                       }

                                                       @Override
                                                       public HttpUriRequest getRedirect(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
                                                         return null;
                                                       }
                                                     });
    this.httpClient = httpClientBuilder.build();


  }

  private void createSessionToken() {
    if (currentSessionToken == null || (System.currentTimeMillis() - timeStampCreationSessionToken) > sessionTokenLiveTime*1000) {
      //create new session token
      try {
        timeStampCreationSessionToken = System.currentTimeMillis();
        JSONObject jsonResponse = doPost(serverUrl + "/api/jeton/creer?validiteEnSecondes" + sessionTokenLiveTime,apiKey);
        if (jsonResponse.has("payload")) {
          currentSessionToken = jsonResponse.getString("payload");
          LOG.debug("Session Token created : {}, at timestamp {}", currentSessionToken, timeStampCreationSessionToken);
        }
      } catch (HttpResponseException e) {
        LOG.error("remote_service={} operation={} parameters=\"apiKey:{}, status=ko "
                      + "duration_ms={} error_msg=\"{}, status : {} \"",
                  IXBUS_SERVICE_API,
                  "createSessionToken",
                  apiKey,
                  System.currentTimeMillis() - timeStampCreationSessionToken,
                  e.getReasonPhrase(),
                  e.getStatusCode(), e);
        timeStampCreationSessionToken = 0;
        currentSessionToken=null;
      } catch (IOException e) {
        LOG.error("Error while trying to create a session token", e);
        timeStampCreationSessionToken = 0;
        currentSessionToken=null;
      }
    }
  }

  public String getUserIdentifier(String username) {
    return getUserIdentifier(username,true);
  }

  private String getUserIdentifier(String username, boolean canReplay) {
    String userIdentifier = userIdentifiers.get(username);
    if (userIdentifier == null) {
      User user = null;
      try {
         user = organizationService.getUserHandler().findUserByName(username);
      } catch (Exception e) {
        LOG.error("Unable to find user {}",username,e);
      }

      if (user != null) {
        createSessionToken();
        long startTime = System.currentTimeMillis();
        try {
          JSONObject jsonResponse = doGet(serverUrl + "/api/parapheur/v1/utilisateur?email=" + user.getEmail(), currentSessionToken);
          if (jsonResponse.has("payload") && !jsonResponse.getJSONArray("payload").isEmpty()) {
            userIdentifier = jsonResponse.getJSONArray("payload").getJSONObject(0).getString("identifiant");
            LOG.debug("User identifier found for username={} : idenfier={}", username, userIdentifier);
            userIdentifiers.put(username, userIdentifier);
          } else if (jsonResponse.has("payload")) {
            //empty payload
            LOG.warn("No Ixbus user founded corresponding to user={} with email={}",username,user.getEmail());
          }
        } catch (HttpResponseException e) {
          if (e.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY && canReplay) {
            //we are redirect on authentication page
            //session token is no more valid
            currentSessionToken = null;
            timeStampCreationSessionToken = 0;
            return getUserIdentifier(username, false);
          } else {
            LOG.error("remote_service={} operation={} parameters=\"username:{}, status=ko "
                          + "duration_ms={} error_msg=\"{}, status : {} \"",
                      IXBUS_SERVICE_API,
                      "getUserIdentifier",
                      username,
                      System.currentTimeMillis() - startTime,
                      e.getReasonPhrase(),
                      e.getStatusCode(), e);
          }
        } catch (IOException e) {
          LOG.error("Error while trying to get user identifier", e);
        }
      }

    }
    return userIdentifier;
  }

  private JSONObject doGet(String url,String token) throws IOException {
    HttpGet httpUserRequest = new HttpGet(url);
    httpUserRequest.setHeader("IXBUS_API", token);
    HttpResponse httpResponse = httpClient.execute(httpUserRequest);
    String responseString = new BasicResponseHandler().handleResponse(httpResponse);
    EntityUtils.consume(httpResponse.getEntity());
    return new JSONObject(responseString);
  }

  private JSONObject doPost(String url, String token) throws IOException {
    HttpPost httpSessionRequest = new HttpPost(url);
    httpSessionRequest.setHeader("IXBUS_API", token);
    HttpResponse httpResponse = httpClient.execute(httpSessionRequest);
    String responseString = new BasicResponseHandler().handleResponse(httpResponse);
    EntityUtils.consume(httpResponse.getEntity());
    return new JSONObject(responseString);
  }

  public int getUserFoldersCount(String username) {
    return getUserFoldersCount(username,true);
  }

  private int getUserFoldersCount(String username, boolean canReplay) {
    String userIdentifier = getUserIdentifier(username);
    if (userIdentifier!=null) {
      createSessionToken();
      long startTime = System.currentTimeMillis();
      try {
        JSONObject jsonResponse = doGet(serverUrl + "/api/parapheur/v1/dossier/compteur?idUtilisateur=" + userIdentifier, currentSessionToken);
        if (jsonResponse.has("payload") && jsonResponse.getJSONObject("payload").has("nbDossiers")) {
          int result = jsonResponse.getJSONObject("payload").getInt("nbDossiers");
          LOG.debug("Found {} folders that {} own", result, username);
          return result;
        }
      } catch (HttpResponseException e) {
        if (e.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY && canReplay) {
          //we are redirect on authentication page
          //session token is no more valid
          currentSessionToken = null;
          timeStampCreationSessionToken = 0;
          return getUserFoldersCount(username, false);
        } else {
          LOG.error("remote_service={} operation={} parameters=\"username:{}, status=ko "
                        + "duration_ms={} error_msg=\"{}, status : {} \"",
                    IXBUS_SERVICE_API,
                    "getUserFolderCount",
                    username,
                    System.currentTimeMillis() - startTime,
                    e.getReasonPhrase(),
                    e.getStatusCode(), e);
        }
      } catch (IOException e) {
        LOG.error("Error while getting user folders count", e);
      }
    }
    return 0;
  }

  public List<DocumentEntity> getUserFolders(String username) {
    return getUserFolders(username,true);
  }

  private List<DocumentEntity> getUserFolders(String username, boolean canReplay) {
    List<DocumentEntity> result = new ArrayList<>();
    String userIdentifier = getUserIdentifier(username);
    if (userIdentifier!=null) {
      createSessionToken();
      long startTime = System.currentTimeMillis();
      try {
        JSONObject jsonResponse = doGet(serverUrl + "/api/parapheur/v1/dossier/emis?statutUtilisateur=redacteur&idUtilisateur=" + userIdentifier, currentSessionToken);
        if (jsonResponse.has("payload")) {
          JSONArray folders=jsonResponse.getJSONArray("payload");
          folders.forEach(folder -> {
            if (!((JSONObject)folder).getString("statut").equals("Annule")) {
              result.addFirst(toDocumentEntity((JSONObject) folder));
            }
          });
          LOG.debug("Found {} folders that {} own", result.size(), username);
          return result;
        }
      } catch (HttpResponseException e) {
        if (e.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY && canReplay) {
          //we are redirect on authentication page
          //session token is no more valid
          currentSessionToken = null;
          timeStampCreationSessionToken = 0;
          return getUserFolders(username, false);
        } else {
          LOG.error("remote_service={} operation={} parameters=\"username:{}, status=ko "
                        + "duration_ms={} error_msg=\"{}, status : {} \"",
                    IXBUS_SERVICE_API,
                    "getUserFolders",
                    username,
                    System.currentTimeMillis() - startTime,
                    e.getReasonPhrase(),
                    e.getStatusCode(), e);
        }
      } catch (IOException e) {
        LOG.error("Error while getting user folders", e);
      }
    }
    return result;
  }

  public int getUserActionsCount(String username) {
    //we want to replay only once, in case of the session token has been revoked.
    return getUserActionsCount(username,true);
  }

  private int getUserActionsCount(String username, boolean canReplay) {
    String userIdentifier = getUserIdentifier(username);
    if (userIdentifier!=null) {
      createSessionToken();
      long startTime = System.currentTimeMillis();
      try {
        JSONObject jsonResponse = doGet(serverUrl + "/api/parapheur/v1/dossier/compteur?idUtilisateur=" + userIdentifier, currentSessionToken);
        if (jsonResponse.has("payload") && jsonResponse.getJSONObject("payload").has("nbDossiers")) {
          int result = jsonResponse.getJSONObject("payload").getInt("nbDossiers");
          LOG.debug("Found {} folders that {} must do", result, username);
          return result;
        }
      } catch (HttpResponseException e) {
        if (e.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY && canReplay) {
          //we are redirect on authentication page
          //session token is no more valid
          currentSessionToken = null;
          timeStampCreationSessionToken = 0;
          return getUserActionsCount(username, false);
        } else {
          LOG.error("remote_service={} operation={} parameters=\"username:{}, status=ko "
                        + "duration_ms={} error_msg=\"{}, status : {} \"",
                    IXBUS_SERVICE_API,
                    "getUserActionsCount",
                    username,
                    System.currentTimeMillis() - startTime,
                    e.getReasonPhrase(),
                    e.getStatusCode(), e);
        }
      } catch (IOException e) {
        LOG.error("Error while getting user actions count", e);
      }
    }
    return 0;
  }

  public List<DocumentEntity> getUserActions(String username) {
    return getUserActions(username,true);
  }

  private List<DocumentEntity> getUserActions(String username, boolean canReplay) {
    List<DocumentEntity> result = new ArrayList<>();
    String userIdentifier = getUserIdentifier(username);
    if (userIdentifier!=null) {
      createSessionToken();
      long startTime = System.currentTimeMillis();
      try {
        JSONObject jsonResponse = doGet(serverUrl + "/api/parapheur/v1/dossier?idUtilisateur=" + userIdentifier, currentSessionToken);
        if (jsonResponse.has("payload")) {
          JSONArray folders=jsonResponse.getJSONArray("payload");
          folders.forEach(folder -> result.addFirst(toDocumentEntity((JSONObject) folder)));
          LOG.debug("Found {} folders that {} must do", result.size(), username);
          return result;
        }
      } catch (HttpResponseException e) {
        if (e.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY && canReplay) {
          //we are redirect on authentication page
          //session token is no more valid
          currentSessionToken = null;
          timeStampCreationSessionToken = 0;
          return getUserActions(username, false);
        } else {
          LOG.error("remote_service={} operation={} parameters=\"username:{}, status=ko "
                        + "duration_ms={} error_msg=\"{}, status : {} \"",
                    IXBUS_SERVICE_API,
                    "getUserActions",
                    username,
                    System.currentTimeMillis() - startTime,
                    e.getReasonPhrase(),
                    e.getStatusCode(), e);
        }
      } catch (IOException e) {
        LOG.error("Error while getting user actions", e);
      }
    }
    return result;
  }

  private DocumentEntity toDocumentEntity(JSONObject folder) {
    DocumentEntity document = new DocumentEntity();
    document.setId(folder.getString("identifiant"));
    document.setName(folder.getString("nom"));
    if (folder.has("actionAttendue") && !folder.isNull("actionAttendue")) {
      document.setAction(folder.getString("actionAttendue"));
    }
    document.setCreationDate(folder.getString("dateCreation"));
    document.setStatus(folder.getString("statut"));
    if (folder.has("dateLimite") && !folder.isNull("dateLimite")) {
      document.setDueDate(folder.getString("dateLimite"));
    }
    document.setReferentFirstName(folder.getJSONObject("referent").getString("prenom"));
    document.setReferentLastName(folder.getJSONObject("referent").getString("nom"));
    document.setNature(folder.getJSONObject("nature").getString("nom"));
    return document;
  }

  public SettingsEntity getSettings() {
    SettingsEntity settings = new SettingsEntity();
    settings.setCreateUrl(serverUrl+"/parapheur/preparer");
    return settings;
  }

}
