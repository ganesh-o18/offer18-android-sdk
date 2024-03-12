
[![Android CI with Gradle](https://github.com/ganesh-o18/offer18-android-sdk/actions/workflows/gradle.yml/badge.svg)](https://github.com/ganesh-o18/offer18-android-sdk/actions/workflows/gradle.yml)

# Android SDK
##### (Draft v0.1)
#####  Last Edit 12-March,2024

_Required - Java 1.8, Android API level 16_
#### Dependency
Offer18 Android SDK has single dependency on OkHttp {version} library for http requests.

#### Permission
Make sure your app has permission -
```xml
<uses-permission android:name="android.permission.INTERNET" />
```


#### Initialise SDK
```java
Offer18.init(getApplicationBaseContext());
```
#### Enable `production` mode
```java
Offer18.enableProductionMode();
```
#### Enable `debug` mode
```java
Offer18.enableDebugMode();
```
>SKD can be either in `production` or `debug` mode. By default it is {TDB} mode. Logging is only enabled in `debug` mode.

#### Check  `production / debug` mode
```java
Offer18.isProductModeEnabled(); // returns true if production mode is enabled
Offer18.isDebugModeEnabled(); // returns true if debug mode is enabled
```

#### Get   mode
```java
Offer18.getEnv(); // debug or production
```
#### Exceptions
-  `Offer18ClientNotIntialiseException`
   This execution thrown when SDK is not initialised.
-  `Offer18FormFieldDataTypException`
   This exception thrown when passed argument's data-type mis-matched
-  `Offer18FormFieldRequiredException`
   This exception thrown when called method does not receive sufficient argument(s).
-  `Offer18InvalidCredentialsException`
   This exception thrown when passed credentials are invalid
-  `Offer18SSLVerificationException`
   This exception thrown when sub-domain does not have SSL  
  