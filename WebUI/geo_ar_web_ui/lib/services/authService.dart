// Import the firebase authentication plugin
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:geo_ar_web_ui/models/myUser.dart';

class AuthService {
  final FirebaseAuth _auth = FirebaseAuth.instance;
  String _validationResult;
  MyUser _user(User user) {
    return user != null ? MyUser(uid: user.uid) : null;
  }

  // auth change user stream
  Stream<MyUser> get user {
    return _auth.authStateChanges().map(_user);
  }

  // sign in with email
  Future signIn(String email, String password, var context) async {
    try {
      UserCredential userCredential = await _auth.signInWithEmailAndPassword(
        email: email,
        password: password,
      );
      _validationResult = null;
      return _user(userCredential.user);
    } on FirebaseAuthException catch (e) {
      if (e.code == 'user-not-found') {
        print('No user found for that email.');
        _validationResult = "No user found for that email.";
      } else if (e.code == 'wrong-password') {
        print('Wrong password provided for that user.');
        _validationResult = "Wrong password provided for that user.";
      } else if(e.code == "invalid-email") {
        print(e.message);
        _validationResult = "The email address is badly formatted.";
      }else {
        print(e.code);
        _validationResult = null;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(e.message),
          ),
        );
      }
      return null;
    }
  }

  // sign out
  Future signOut() async {
    try {
      return await _auth.signOut();
    } catch (e) {
      print(e.toString());
      return null;
    }
  }

  String get validationResult {
    return _validationResult;
  }
}
