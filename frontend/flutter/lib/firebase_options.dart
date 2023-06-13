// File generated by FlutterFire CLI.
// ignore_for_file: lines_longer_than_80_chars, avoid_classes_with_only_static_members
import 'package:firebase_core/firebase_core.dart' show FirebaseOptions;
import 'package:flutter/foundation.dart'
    show defaultTargetPlatform, kIsWeb, TargetPlatform;

/// Default [FirebaseOptions] for use with your Firebase apps.
///
/// Example:
/// ```dart
/// import 'firebase_options.dart';
/// // ...
/// await Firebase.initializeApp(
///   options: DefaultFirebaseOptions.currentPlatform,
/// );
/// ```
class DefaultFirebaseOptions {
  static FirebaseOptions get currentPlatform {
    if (kIsWeb) {
      return web;
    }
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        return android;
      case TargetPlatform.iOS:
        return ios;
      case TargetPlatform.macOS:
        return macos;
      case TargetPlatform.windows:
        throw UnsupportedError(
          'DefaultFirebaseOptions have not been configured for windows - '
          'you can reconfigure this by running the FlutterFire CLI again.',
        );
      case TargetPlatform.linux:
        throw UnsupportedError(
          'DefaultFirebaseOptions have not been configured for linux - '
          'you can reconfigure this by running the FlutterFire CLI again.',
        );
      default:
        throw UnsupportedError(
          'DefaultFirebaseOptions are not supported for this platform.',
        );
    }
  }

  static const FirebaseOptions web = FirebaseOptions(
    apiKey: 'AIzaSyDt8VjpN0LKeyjv9XZYeBcd7kFOVvoqO9s',
    appId: '1:716678169981:web:2933af21675114df877814',
    messagingSenderId: '716678169981',
    projectId: 'ngok3fyp-68e66',
    authDomain: 'ngok3fyp-68e66.firebaseapp.com',
    storageBucket: 'ngok3fyp-68e66.appspot.com',
    measurementId: 'G-FQMBR1D43W',
  );

  static const FirebaseOptions android = FirebaseOptions(
    apiKey: 'AIzaSyD0TzKSa9YeYt-Gfg4Y8MQYdqevyAsbiPk',
    appId: '1:716678169981:android:bbcf073c093b8661877814',
    messagingSenderId: '716678169981',
    projectId: 'ngok3fyp-68e66',
    storageBucket: 'ngok3fyp-68e66.appspot.com',
  );

  static const FirebaseOptions ios = FirebaseOptions(
    apiKey: 'GOOGLE_API_KEY',
    appId: 'GOOGLE_APP_ID',
    messagingSenderId: '716678169981',
    projectId: 'ngok3fyp-68e66',
    storageBucket: 'ngok3fyp-68e66.appspot.com',
    iosClientId: 'CLIENT_ID',
    iosBundleId: 'com.example.ngok3fypFrontendFlutter',
  );

  static const FirebaseOptions macos = FirebaseOptions(
    apiKey: 'GOOGLE_API_KEY',
    appId: 'GOOGLE_APP_ID',
    messagingSenderId: '716678169981',
    projectId: 'ngok3fyp-68e66',
    storageBucket: 'ngok3fyp-68e66.appspot.com',
    iosClientId: 'CLIENT_ID',
    iosBundleId: 'com.example.ngok3fypFrontendFlutter',
  );
}