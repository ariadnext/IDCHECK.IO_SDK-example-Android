# IDCheck.io SDK Sample - MobileSDK v5 - Android #

## Setup ##

You need credentials to acces ARIADNEXT's Nexus hosting the SDK library.
Once retrieved, update the file `gradle.properties` at the root of the project :

```gradle
# TODO : add here your AriadNext Nexus user name to retrieve SDK dependency
AXTNexusUser = YOUR_USERNAME
AXTNexusPassword = YOUR_PASSWORD
```

## Add your SDK's licence

To be able to use the sample, please :

- Add your license file in the `./app/src/main/assets/` folder of this project
- Name it "**licence.axt**"

## Run the project

Import the project in Android Studio, or build the application using this command : 

```shell
./gradlew assembleDebug
```

You are now good to go ! 😎
