# IDCheck.io SDK Sample - MobileSDK v5 - Android #

## Setup ##

You need credentials to acces ARIADNEXT's Nexus hosting the SDK library.
Once retrieved, update the file `app/build.gradle` to set your credentials :

```gradle

repositories {
    mavenLocal()
    // AriadNext External Nexus for SDK dependency
    maven {
        credentials {
            username 'YOUR_USER_NAME'
            password 'YOUR_PASSWORD'
        }
        url "https://repoman.rennes.ariadnext.com/content/repositories/com.ariadnext.idcheckio/"
    }
}

```

## Add your SDK's licence

To be able to use the sample, please :

- Add your license file in the `./app/src/main/assets/` folder of this project
- Name it "**licence.axt**"
- Update the signing configuration with your certificate (the one for which you send the SHA-1 fingerprint to ARIADNEXT)

## Run the project

Import the project in Android Studio, or build the application using this command : 

```shell
./gradlew assembleRelease
```

You are now good to go ! 😎
