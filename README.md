# Citizen Hub Android

## Building

### Requirements

#### GitHub Access

You need a GitHub account, and you must generate a personal access token with `repo` and `write:packages` scope.

Gradle must be able to find the properties `github.username` and `github.token` with the necessary credential information.

For instance, the following could be added to `~/.gradle/gradle.properties`:

```
github.username=<username>
github.toke=<token>
```


#### Data4Life SDK Endpoint Configuration

 A file named `d4lsdk-config.json` must exist in the project root containing the endpoint configuration.
 
 The file should be a valid JSON file, containing a root JSON object with the following properties:
 
 - *clientId*: a `string` representing the client id attributed to the application;
 
 - *clientSecret*: a `string` representing the client secret for the indicated *clientId*;
 
 - *redirectScheme*: a `string` representing the endpoint key identification;
 
 - *environment*: a `string` representing the type of environment used;
 
 - *debug*: a `boolean` indicating if debug is active;
 
 - *platform*: a `string` representing the platform logging in to.
 
 Example:
 
 ```
{
    "id": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
    "secret": "XXXXXXXXXXXXXXXX",
    "redirectScheme": "org.example.XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
    "environment": "STAGING",
    "debug": true,
    "platform": "EXMPL"
}
```

