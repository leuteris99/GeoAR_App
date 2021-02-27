// Import material components
import 'package:flutter/material.dart';
import 'package:geo_ar_web_ui/services/authService.dart';

class AuthPage extends StatefulWidget {
  AuthPage({Key key}) : super(key: key);

  @override
  _AuthPageState createState() => _AuthPageState();
}

final _formKey = GlobalKey<FormState>();

class _AuthPageState extends State<AuthPage> {
  @override
  Widget build(BuildContext context) {
    TextEditingController emailController = TextEditingController();
    TextEditingController passController = TextEditingController();

    AuthService as = AuthService();

    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Colors.black,
        elevation: 0.0,
        title: Center(
          child: Text("Web UI"),
        ),
      ),
      body: Center(
        child: Form(
          key: _formKey,
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
              AutofillGroup(
                child: Column(
                  children: [
                    Container(
                      //padding: const EdgeInsets.only(left:15.0,right: 15.0,top:0,bottom: 0),
                      padding: EdgeInsets.only(
                          left: 30, right: 30, top: 0, bottom: 0),
                      width: getScreenWidth(context) / 3,
                      // The email field
                      child: TextFormField(
                        autofillHints: [AutofillHints.email],
                        validator: (value) {
                          if (value.isEmpty) {
                            return "Please enter your email.";
                          } else {
                            String tmp = as.validationResult;
                            if (tmp == "No user found for that email." ||
                                tmp ==
                                    "The email address is badly formatted.") {
                              return tmp;
                            }
                          }
                          return null;
                        },
                        controller: emailController,
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
                      // The password field
                      child: TextFormField(
                        autofillHints: [AutofillHints.password],
                        validator: (value) {
                          if (value.isEmpty) {
                            return "Please enter your password.";
                          } else {
                            String tmp = as.validationResult;
                            if (tmp ==
                                "Wrong password provided for that user.") {
                              return tmp;
                            }
                          }
                          return null;
                        },
                        controller: passController,
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
                          hintText: 'Enter secure password',
                        ),
                      ),
                    ),
                  ],
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
                  if (_formKey.currentState.validate()) {
                    await as.signIn(
                      emailController.text,
                      passController.text,
                      context,
                    );
                  }
                },
              ),
            ],
          ),
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
