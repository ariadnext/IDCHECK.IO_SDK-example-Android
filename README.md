# IDCheck.io SDK Sample - MobileSDK v5 - Android #

## Setup ##

To get this sample running, please follow the instructions :

1. Ask our [Customer Success Managers](mailto:csm@ariadnext.com) for credentials to access the *ARIADNEXT* external repository in order to retrieve the **IDCheck.io Mobile SDK** library and integrate it to the project (update the **build.gradle** at the root of the project):

   ```groovy
   // TODO 1: Set your own credentials to access ARIADNEXT external repository in order to access SDK library download
   maven {
       credentials {
           username = "<YOUR USERNAME>"
           password = "<YOUR PASSWORD>"
       }
       url "https://repoman.rennes.ariadnext.com/content/repositories/com.ariadnext.idcheckio/"
   }
   ```

2. With your application bundle id, ask the [Customer Success Managers](mailto:csm@ariadnext.com) to create an `idToken` to activate the SDK. You can then integrate it in your project using a `buildConfigField` in your **app/build.gradle** file:

   ```groovy
   // TODO 2: Set your own IdCheck.io token
   buildConfigField 'String', 'IDCHECKIO_ID_TOKEN', "\"YOUR_ID_TOKEN\""
   ```
   
## Run the project

Import the project in Android Studio, or build the application using this command : 

```shell
./gradlew clean assembleDebug
```

You are now good to go ! 😎
