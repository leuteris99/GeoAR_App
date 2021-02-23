// Import the firebase authentication plugin
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:geo_ar_web_ui/models/myUser.dart';

class AuthService {
  final FirebaseAuth _auth = FirebaseAuth.instance;
  MyUser _user(UserCredential userCredential) {
    return userCredential != null ? MyUser(uid: userCredential.user.uid) : null;
  }

  // sign in with email
  Future signIn(String email, String password, var context) async {
    try {
      UserCredential userCredential = await _auth.signInWithEmailAndPassword(
        email: email,
        password: password,
      );
      return _user(userCredential);
    } on FirebaseAuthException catch (e) {
      if (e.code == 'user-not-found') {
        print('No user found for that email.');
        ScaffoldMessenger.of(context).showSnackBar(
            new SnackBar(content: Text('No user found for that email.')));
      } else if (e.code == 'wrong-password') {
        print('Wrong password provided for that user.');
        ScaffoldMessenger.of(context).showSnackBar(new SnackBar(
            content: Text('Wrong password provided for that user.')));
      } else {
        print(e.message);
        ScaffoldMessenger.of(context)
            .showSnackBar(new SnackBar(content: Text(e.message)));
      }
      return null;
    }
  }

  // sign out
}
