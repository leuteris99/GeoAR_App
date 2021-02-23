// Import material components
import 'package:flutter/material.dart';
// Import the firebase_core plugin
import 'package:firebase_core/firebase_core.dart';
// Import the firestore plugin
import 'package:cloud_firestore/cloud_firestore.dart';
// Import the firebase authentication plugin
import 'package:firebase_auth/firebase_auth.dart';

// Project Imports
import 'screen/authPage.dart';

// TO DEPLOY: run "powershell -ExecutionPolicy Bypass" to be able to run scripts with powershell

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    new MaterialApp(
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
        home: new MyApp(),
        routes: <String, WidgetBuilder>{
          "/home": (BuildContext context) => new MyHomePage(title: "Web UI"),
        }),
  );
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
          return AuthPage();
        }

        // show something whilst waiting for initialization to complete
        return Text("Loading...");
      },
    );
  }
}

Widget somethingWentWrong() {
  return Text("ERROR: Something went wrong :(");
}

Widget loading() {
  return Scaffold(
    body: Text("Loading..."),
  );
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  FirebaseFirestore _firebaseFirestore = FirebaseFirestore.instance;
  List<String> routes = [];

  @override
  Widget build(BuildContext context) {
    // _firebaseFirestore
    //     .collection("routes")
    //     .get()
    //     .then((QuerySnapshot querySnapshot) {
    //   querySnapshot.docs.forEach((doc) {
    //     routes.add(doc["title"]);
    //   });
    //   print(routes);
    // });
    return startScaffold();
  }

  Widget startScaffold() {
    return Scaffold(
      appBar: AppBar(
        title: Center(child: Text(widget.title)),
        backgroundColor: Colors.black,
      ),
      backgroundColor: Colors.black,
      body: new Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: <Widget>[
          makeList("Routes", context, "routes", _firebaseFirestore),
          makeList("Places", context, "marker", _firebaseFirestore),
          makeList("Holograms", context, "hologram", _firebaseFirestore),
          makeList("Models", context, "arModel", _firebaseFirestore),
        ],
      ),
    );
  }
}

Widget makeList(String title, var context, String collectionName,
    FirebaseFirestore _firebaseFirestore) {
  List<Widget> textList = [];
  textList.add(Container(
    height: 20,
  ));
  textList.add(makeCategoryTitle(title, context));

  return FutureBuilder<QuerySnapshot>(
    future: _firebaseFirestore.collection(collectionName).get(),
    builder:
        (BuildContext builderContext, AsyncSnapshot<QuerySnapshot> snapshot) {
      if (snapshot.hasError) {
        return Text("Something went wrong");
      }

      if (snapshot.connectionState == ConnectionState.done) {
        snapshot.data.docs.forEach((doc) {
          textList.add(makeItem(doc["title"], context));
        });
        textList.add(makeAddButton(context, title));

        return Container(
          width: (getScreenWidth(context)) / 5.0,
          child: Card(
            child: new Column(
              children: textList,
            ),
            color: Colors.grey[900],
          ),
        );
      }

      return Text("loading");
    },
  );
}

Widget makeItem(String name, var context) {
  return Container(
    height: 50,
    width: (getScreenWidth(context) / 5.0) - 20,
    margin: const EdgeInsets.all(8.0),
    child: Card(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(25)),
      color: Colors.black,
      child: InkWell(
        customBorder:
            RoundedRectangleBorder(borderRadius: BorderRadius.circular(25)),
        onTap: () => ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text(name),
          duration: const Duration(milliseconds: 500),
        )),
        child: Align(
          alignment: Alignment(-0.8, 0.0),
          child: Text(name),
        ),
      ),
    ),
  );
}

Widget makeCategoryTitle(String name, var context) {
  return Container(
    height: 100,
    width: (getScreenWidth(context) / 5.0) - 20,
    margin: const EdgeInsets.all(10.0),
    child: Card(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(25)),
      color: Colors.black,
      child: Center(
        child: Text(
          name,
          style: TextStyle(
            fontSize: 20,
          ),
        ),
      ),
    ),
  );
}

Widget makeAddButton(var context, String category) {
  return MaterialButton(
    height: 40,
    minWidth: 60,
    color: Colors.black,
    shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(40),
    ),
    child: Icon(
      Icons.add,
      color: Colors.white,
    ),
    onPressed: () => ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text("add item to " + category),
        duration: Duration(milliseconds: 500),
      ),
    ),
  );
}

double getScreenWidth(var context) {
  return MediaQuery.of(context).size.width;
}

double getScreenHeight(var context) {
  return MediaQuery.of(context).size.height;
}
