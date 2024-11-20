# ixbus-connector

This addon display Ixbus informations in a drawer in eXo

## How to install
Launch this commands :
```
cd ${EXO_HOME}
./addon install exo-ixbus-connector
```

## How to configure

Availables properties are :

- exo.ixbus.api.url : Url of the Ixbus Server
- exo.ixbus.api.key : Api Key of the service user which will make API call. It must be an admin user. To get the API Key, to into the user profile.


## Notifications : 
- Notification job runs once by hour
- property : exo.ixbus.notificationJob.expression
- The job will notify users which have a document in state Visa or Signature, if the date of the step to do is after the date of the last execution of the job (ie. it will only notify documents for which the status change within the last hour)
- There will be no notification for the first execution


## Possible improvments :
- Currently, we use email as a pivot between eXo user and Ixbus users because the addon is used in context where the mail cannot be change by users. In case this addon is used in a different context, where users can change his email, we could make the pivot as username by default (require that username in Ixbus are the same as username in eXo), and possible to change to use email with a property


## Todo
Mail Channel
dans la notif web : ouvrir le drawer
