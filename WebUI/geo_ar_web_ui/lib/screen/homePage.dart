import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:geo_ar_web_ui/screen/dialogs/showItemDialog.dart';
import 'package:geo_ar_web_ui/services/authService.dart';

class HomePage extends StatefulWidget {
  HomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
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
    AuthService _auth = AuthService();
    return Scaffold(
      appBar: AppBar(
        elevation: 0.0,
        title: Center(child: Text(widget.title)),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.logout),
            onPressed: () async {
              await _auth.signOut();
            },
          ),
        ],
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
        onTap: () {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(name),
              duration: const Duration(milliseconds: 500),
            ),
          );
        },
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
    onPressed: () {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("add item to " + category),
          duration: Duration(milliseconds: 500),
        ),
      );
      _execShowItemDialog(context);
    },
  );
}

double getScreenWidth(var context) {
  return MediaQuery.of(context).size.width;
}

double getScreenHeight(var context) {
  return MediaQuery.of(context).size.height;
}

void _execShowItemDialog(var context) async {
  String save = await Navigator.of(context).push(
    new MaterialPageRoute<String>(
      builder: (BuildContext builderContext) {
        return new ShowItemDialog();
      },
      fullscreenDialog: true,
    ),
  );

  if (save != null) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(save),
        duration: Duration(milliseconds: 500),
      ),
    );
  }
}
