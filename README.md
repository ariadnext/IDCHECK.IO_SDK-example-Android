![ARIADNEXT Logo](img/logo.png)

> 💡 For older IDCheck.io Mobile SDK Sample (v5.x.x), please checkout the [sdk_v5](https://github.com/ariadnext/IDCHECK.IO_SDK-example-Android/tree/sdk_v5) branch

# IDCheck.io Mobile SDK Sample

## Getting Started

To get this sample running, please follow the instructions :

 1. Ask our [Customer Success Managers](mailto:csm@ariadnext.com) for credentials to access the *ARIADNEXT* external repository in order to retrieve the **IDCheck.io Mobile SDK** library and integrate it to the project.

 2. You will need to create a keystore in order to [sign](https://developer.android.com/studio/publish/app-signing#opt-out) the final `apk`, this keystore's fingerprint will be declared in the licence and used to check the application's integrity at runtime while activating the SDK. You will have to provide the keystore SHA1 fingerprint to the CSM team when requesting a license.
 Here is the command line that will help you obtain it :
 ```shell
 keytool -list -v -keystore app/<YOUR KEYSTORE FILE>.jks
 ```
 In order to sign your app with the keystore, add it to the **app** folder and update the **app/build.gradle** file with the key/passwords you created in the keystore :
 ```groovy
 android {
    // ...
    signingConfigs {
        release {
            // TODO 2 : add your own keystore.
            storeFile file("<PATH TO YOUR KEYSTORE FILE>")
            storePassword "<KEYSTORE PASSWORD>"
            keyAlias "<KEY>"
            keyPassword "<KEY PASSWORD>"
        }
    }
 }
 ```

 3. With your keystore's SHA1 fingerprint, ask the CSM Team to create a license for you to test the SDK. Add this license file to the project : rename it to **license.axt** and move it to the **app/src/main/assets** folder.

## Sample application

This sample project aims to showcase all possibilities of the **IDCheck.io Mobile SDK** and the associated best practices regarding these features. It also helps you understand how you can easily integrate the SDK, activate it and customise/adapt it to your application and business needs.

The main screen displays a sliding tile to choose between four distinct capture flows :
 - **Online flow** : this flow uses the SDK for capturing an ID document first, and then continues with a Biometric Liveness session capture. It shows what you need to do in order to chain multiple online captures and keep transferring the *OnlineContext* through all these captures.
 - **Simple capture** : this flow has a specific selector to show you how to setup the SDK in order to capture a specific document (from ID to address proof, or even selfie).
 - **Analyze** : this flow uses the `Idcheckio.analyze` API which is useful when you don't want to use the camera feed for live-capturing and analyzing a document, but instead use a static image provided by the user from his phone's gallery.

Select the flow you want to try, and click on *Give it a try* button to start capturing documents with the SDK.
