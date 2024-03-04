[![Android CI with Gradle](https://github.com/ganesh-o18/offer18-android-sdk/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/ganesh-o18/offer18-android-sdk/actions/workflows/gradle.yml)
```java
Offer18.init();

Map<String, String> args = new HashMap<>();
args.put("offerId", "23234");
Offer18.trackConversion(args);
```