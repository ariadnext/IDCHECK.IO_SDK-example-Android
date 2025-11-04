![ARIADNEXT Logo](img/logo.png)

> 💡 For older IDCheck.io Mobile SDK Sample (v8.x.x), please checkout the [sdk_v8](https://github.com/ariadnext/IDCHECK.IO_SDK-example-Android/tree/sdk_v8) branch

> 💡 For older IDCheck.io Mobile SDK Sample (v7.x.x), please checkout the [sdk_v7](https://github.com/ariadnext/IDCHECK.IO_SDK-example-Android/tree/sdk_v7) branch

> 💡 For older IDCheck.io Mobile SDK Sample (v6.x.x), please checkout the [sdk_v6](https://github.com/ariadnext/IDCHECK.IO_SDK-example-Android/tree/sdk_v6) branch

# IDCheck.io Mobile SDK Sample

## Getting Started

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

 2. With your application bundle id, ask the [Customer Success Managers](mailto:csm@idnow.io) to create an `idToken` to activate the SDK. You can then integrate it in your project using a `buildConfigField` in your **app/build.gradle** file:

    ```groovy
    // TODO 2: Set your own IdCheck.io token
    buildConfigField 'String', 'IDCHECKIO_ID_TOKEN', "\"YOUR_ID_TOKEN\""
    ```

## Sample application

This sample project aims to showcase all possibilities of the **IDCheck.io Mobile SDK** and the associated best practices regarding these features. It also helps you understand how you can easily integrate the SDK, activate it and customise/adapt it to your application and business needs.

The main screen displays two distinct capture flows :

 - **Online flow** : This flow uses the SDK for capturing any type of document based on the parameter given to the sdk.
 - **Onboarding session** : Specific capture session that will handle a complete onboarding.