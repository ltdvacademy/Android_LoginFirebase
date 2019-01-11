# Android_LoginFirebase
Login using Firebase Auth Android

Sebuah Implementasi Firebase Auth pada Android, Login Email dan Password. (ID)
A Firebase Auth Implementation on Android, Email Login and Password. (EN)

Silahkan Translate, karena menggunakan Bahasa Inggris. (ID)
Please Translate, because using English Language. (EN)

# How to Build

1. Configure Gradl (Module: app) --> for Firebase Auth

```javascript

    implementation 'com.google.firebase:firebase-auth:16.1.0'
    implementation 'com.google.android.gms:play-services-auth:16.0.1'
    
```

2. Configure Gradl (Module: app) --> for Design Library & Constraint Layout

```javascript

    implementation 'com.android.support:design:28.0.0'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
    
```

3. Dont forget to Connect to Firebase & Get google-services.json

4. Added Apply Plugin Google Service in Gradl (Module: app), placement down

```javascript

apply plugin: 'com.google.gms.google-services'
    
```

5. Makesure You have activated the auth service in the firebase console.

![alt text](https://github.com/poncoe/Android_LoginFirebase/blob/master/app/src/main/assets/auth1.png)
![alt text](https://github.com/poncoe/Android_LoginFirebase/blob/master/app/src/main/assets/auth2.png)

6. If successful, email data will appear on the firebase console auth service

![alt text](https://github.com/poncoe/Android_LoginFirebase/blob/master/app/src/main/assets/auth3.png)

# Please ask if you experience difficulties, Yeah!

# Screenshot

![alt text](https://github.com/poncoe/Android_LoginFirebase/blob/master/app/src/main/assets/output.png)
