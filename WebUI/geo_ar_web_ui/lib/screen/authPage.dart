// Import material components
import 'package:flutter/material.dart';
import 'package:geo_ar_web_ui/models/myUser.dart';
import 'package:geo_ar_web_ui/services/auth.dart';

import '../main.dart';

class AuthPage extends StatefulWidget {
  AuthPage({Key key}) : super(key: key);

  @override
  _AuthPageState createState() => _AuthPageState();
}

class _AuthPageState extends State<AuthPage> {
  @override
  Widget build(BuildContext context) {
    final emailControler = TextEditingController();
    final passControler = TextEditingController();

    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Colors.black,
        title: Center(
          child: Text("Web UI"),
        ),
      ),
      body: Center(
        child: Column(
          children: <Widget>[
            Container(
              padding: EdgeInsets.only(
                top: getScreenHeight(context) / 6,
                bottom: 30,
              ),
              child: Text(
                "Log In.",
                style: TextStyle(fontSize: 40),
              ),
            ),
            Container(
              //padding: const EdgeInsets.only(left:15.0,right: 15.0,top:0,bottom: 0),
              padding: EdgeInsets.only(left: 30, right: 30, top: 0, bottom: 0),
              width: getScreenWidth(context) / 3,
              child: TextField(
                controller: emailControler,
                style: TextStyle(
                  color: Colors.white,
                ),
                decoration: InputDecoration(
                  fillColor: Colors.grey[900],
                  filled: true,
                  border: OutlineInputBorder(),
                  labelStyle: TextStyle(
                    color: Colors.grey[400],
                  ),
                  hintStyle: TextStyle(
                    color: Colors.grey[400],
                  ),
                  labelText: 'Email',
                  hintText: 'Enter your email',
                ),
              ),
            ),
            Container(
              width: getScreenWidth(context) / 3,
              padding: const EdgeInsets.only(
                  left: 30.0, right: 30.0, top: 15, bottom: 20),
              //padding: EdgeInsets.symmetric(horizontal: 15),
              child: TextField(
                controller: passControler,
                // textInputAction: TextInputAction.go,
                style: TextStyle(
                  color: Colors.white,
                ),
                obscureText: true,
                decoration: InputDecoration(
                    fillColor: Colors.grey[900],
                    filled: true,
                    border: OutlineInputBorder(),
                    labelStyle: TextStyle(
                      color: Colors.grey[400],
                    ),
                    hintStyle: TextStyle(
                      color: Colors.grey[400],
                    ),
                    labelText: 'Password',
                    hintText: 'Enter secure password'),
              ),
            ),
            MaterialButton(
              // Log In Button
              height: 50,
              minWidth: 80,
              color: Colors.grey[900],
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(40),
              ),
              child: Icon(
                Icons.arrow_forward_rounded,
                color: Colors.white,
              ),
              onPressed: () async {
                AuthService as = AuthService();
                MyUser uc = await as.signIn(
                  emailControler.text,
                  passControler.text,
                  context,
                );
                if (uc != null) {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) =>
                          MyHomePage(title: 'GeoAR app-Web UI'),
                    ),
                  );
                }
              },
            ),
          ],
        ),
      ),
    );
  }
}

double getScreenWidth(var context) {
  return MediaQuery.of(context).size.width;
}

double getScreenHeight(var context) {
  return MediaQuery.of(context).size.height;
}
