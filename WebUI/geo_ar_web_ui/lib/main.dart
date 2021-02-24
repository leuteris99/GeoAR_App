// Import material components
import 'package:flutter/material.dart';
// Import the firebase_core plugin
import 'package:firebase_core/firebase_core.dart';
// Import the Provider package
import 'package:provider/provider.dart';
// Import the firestore plugin
// import 'package:cloud_firestore/cloud_firestore.dart';
// Import the firebase authentication plugin
// import 'package:firebase_auth/firebase_auth.dart';

// Project Imports
import 'models/myUser.dart';
import 'package:geo_ar_web_ui/screen/wrapper.dart';
import 'package:geo_ar_web_ui/services/authService.dart';

// TO DEPLOY: run "powershell -ExecutionPolicy Bypass" to be able to run scripts with powershell

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // Create the initialization Future outside of `build`:
  final Future<FirebaseApp> _initialization = Firebase.initializeApp();

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: _initialization,
      builder: (context, snapshot) {
        // check for errors
        if (snapshot.hasError) {
          return somethingWentWrong();
        }

        // Once complete init the firebase connection start the app
        if (snapshot.connectionState == ConnectionState.done) {
          return new StreamProvider<MyUser>.value(
            value: AuthService().user,
            child: new MaterialApp(
              title: 'Web UI',
              theme: ThemeData(
                primarySwatch: Colors.blue,
                textTheme: TextTheme(
                  bodyText1: TextStyle(
                    color: Colors.white,
                  ),
                  bodyText2: TextStyle(
                    color: Colors.white,
                  ),
                ),
              ),
              home: Wrapper(),
              // routes: <String, WidgetBuilder>{
              //   "/home": (BuildContext context) => new HomePage(title: "Web UI"),
              // },
            ),
          );
        }

        // show something whilst waiting for initialization to complete
        return loading();
      },
    );
  }
}

Widget somethingWentWrong() {
  return MaterialApp(
    home: Scaffold(
      body: Text("ERROR: Something went wrong :("),
    ),
  );
}

Widget loading() {
  return MaterialApp(
    home: Scaffold(
      body: Text("Loading..."),
    ),
  );
}
