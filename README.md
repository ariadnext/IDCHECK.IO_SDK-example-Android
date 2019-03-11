# IDCHECK.IO_SDK-example-Android

To get this sample running, please follow the instructions :

 1. Integrate the *IDCheck.io SDK* library to the project : move the `smartsdk-release.aar` file to the `app/libs` folder.

 2. Add your SDK License to the project : move the `license.axt` file to the `app/src/main/assets` folder

 3. Eventually add your keystore to the build process : <br/>
    Add the folowing to your `app/build.gradle`file :
    ```
    android {
      ...
      signingConfigs {
          debug {
              storeFile file("<path_to_your_keystore>.jks")
              storePassword "<password>"
              keyAlias "<key>"
              keyPassword "<password>"
          }
          release {
              storeFile file("<path_to_your_keystore>.jks")
              storePassword "<password>"
              keyAlias "<key>"
              keyPassword "<password>"
          }
      }
    }
    ```
