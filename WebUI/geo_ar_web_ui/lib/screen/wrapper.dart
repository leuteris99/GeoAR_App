import 'package:flutter/material.dart';
import 'package:geo_ar_web_ui/models/myUser.dart';
import 'package:geo_ar_web_ui/screen/authPage.dart';
import 'package:geo_ar_web_ui/screen/homePage.dart';
import 'package:provider/provider.dart';

class Wrapper extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final user = Provider.of<MyUser>(context);
    print(user);

    if (user == null) {
      return AuthPage();
    }else{
      return HomePage(title: "GeoAR app - Web UI",);
    }
  }
}
