package org.exoplatform.addons.ixbus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.exoplatform.services.rest.resource.ResourceContainer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ixbus")
public class IxBusRestService implements ResourceContainer {

  private IxbusConnectorService ixbusConnectorService;

  public IxBusRestService(IxbusConnectorService ixbusConnectorService) {
    this.ixbusConnectorService = ixbusConnectorService;
  }

  @GET
  @RolesAllowed("users")
  @Path("/folders")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get current user folders", method = "GET")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response getCurrentUserFolders() {
    return Response.ok(ixbusConnectorService.getCurrentUserFolders()).build();
  }

  @GET
  @RolesAllowed("users")
  @Path("/folders/count")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get current user folders count", method = "GET")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response getCurrentUserFoldersCount() {
    int count = ixbusConnectorService.getCurrentUserFoldersCount();

    return Response.ok("{\"count\":"+count+"}").build();
  }

  @GET
  @RolesAllowed("users")
  @Path("/actions")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get current user actions", method = "GET")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response getCurrentUserActions() {
    return Response.ok(ixbusConnectorService.getCurrentUserActions()).build();
  }

  @GET
  @RolesAllowed("users")
  @Path("/actions/count")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get current user actions count", method = "GET")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response getCurrentUserActionsCount() {
    int count = ixbusConnectorService.getCurrentUserActionsCount();

    return Response.ok("{\"count\":"+count+"}").build();
  }

  @GET
  @RolesAllowed("users")
  @Path("/settings")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get ixbus settings", method = "GET")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response getSettings() {
    return Response.ok(ixbusConnectorService.getSettings()).build();
  }
}
