import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:geo_ar_web_ui/models/ArModel.dart';
import 'package:geo_ar_web_ui/models/Hologram.dart';
import 'package:geo_ar_web_ui/models/MyRoute.dart';
import 'package:geo_ar_web_ui/models/Place.dart';
import 'package:geo_ar_web_ui/screen/dialogs/showItemDialog.dart';
import 'package:geo_ar_web_ui/services/authService.dart';
import 'package:geo_ar_web_ui/services/databaseService.dart';

class HomePage extends StatefulWidget {
  HomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  List<MyRoute> routes = [];
  List<Place> places = [];
  List<Hologram> holograms = [];
  List<ArModel> arModels = [];

  @override
  Widget build(BuildContext context) {
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
          makeList("Routes", context, "routes"),
          makeList("Places", context, "marker"),
          makeList("Holograms", context, "hologram"),
          makeList("Models", context, "arModel"),
        ],
      ),
    );
  }

  Widget makeList(String title, var context, String collectionName) {
    List<Widget> textViewList = [];
    bool isInit = true;
    textViewList.add(Container(
      height: 20,
    ));
    textViewList.add(makeCategoryTitle(title, context));

    return StreamBuilder<QuerySnapshot>(
      stream: DatabaseService().getStreamCollection(collectionName),
      builder:
          (BuildContext builderContext, AsyncSnapshot<QuerySnapshot> snapshot) {
        if (snapshot.hasError) {
          return Text("Something went wrong");
        }
        if (snapshot.connectionState == ConnectionState.waiting) {
          return Text("loading");
        }

        if (!isInit) {
          textViewList.removeLast();
        }
        isInit = false;

        snapshot.data.docChanges.forEach((docChange) {
          // add new item when an object gets added and remove an item when a object get deleted.
          if (docChange.type == DocumentChangeType.added) {
            textViewList.insert(docChange.newIndex + 2,
                makeItem(docChange.doc["title"], context));
            if (collectionName == "routes") {
              print(docChange.doc["title"]);

              routes.insert(docChange.newIndex,
                  MyRoute(docChange.doc["title"], docChange.doc["places"]));
            } else if (collectionName == "marker") {
              Map<String, dynamic> latLngMap = {
                "latitude": docChange.doc["latLng"]["latitude"],
                "longitude": docChange.doc["latLng"]["longitude"],
              };
              places.insert(
                docChange.newIndex,
                Place(
                  docChange.doc["title"],
                  latLngMap,
                  docChange.doc["aoe"],
                  docChange.doc["hologramReference"],
                ),
              );
            } else if (collectionName == "hologram") {
              holograms.insert(
                docChange.newIndex,
                Hologram(
                  docChange.doc["title"],
                  docChange.doc["description"],
                  reference: docChange.doc.reference,
                  question: docChange.doc["question"],
                  answerArray: docChange.doc["answerArray"],
                  arModelReference: docChange.doc["arModelReference"],
                  imageUrl: docChange.doc["imageURL"],
                  webURL: docChange.doc["webURL"],
                ),
              );
            } else if (collectionName == "arModel") {
              arModels.insert(
                docChange.newIndex,
                ArModel(
                  docChange.doc["title"],
                  docChange.doc["modelURL"],
                  reference: docChange.doc.reference,
                  scale: docChange.doc["scale"],
                  distFromAnchor: docChange.doc["distFromAnchor"],
                  animationSpeed: docChange.doc["animationSpeed"],
                ),
              );
            }
          } else if (docChange.type == DocumentChangeType.removed) {
            print("doc remove");
            textViewList.removeAt(docChange.oldIndex + 2);
            if (collectionName == "routes") {
              routes.removeAt(docChange.oldIndex);
            } else if (collectionName == "marker") {
              places.removeAt(docChange.oldIndex);
            } else if (collectionName == "hologram") {
              holograms.removeAt(docChange.oldIndex);
            } else if (collectionName == "arModel") {
              arModels.removeAt(docChange.oldIndex);
            }
          }
        });
        textViewList.add(makeAddButton(context, title));

        return Container(
          width: (getScreenWidth(context)) / 5.0,
          child: Card(
            child: new ListView.builder(
              itemCount: textViewList.length,
              itemBuilder: (context, index) {
                return textViewList[index];
              },
            ),
            color: Colors.grey[900],
          ),
        );
      },
    );
  }

  Widget makeItem(String name, var context) {
    return Container(
      height: 55,
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
          child: ListTile(
            title: Text(
              name,
              style: TextStyle(color: Colors.white),
            ),
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
    return CircleAvatar(
      backgroundColor: Colors.black,
      radius: 20,
      child: IconButton(
        icon: Icon(
          Icons.add,
          color: Colors.white,
        ),
        onPressed: () async {
          if (category == "Routes") {
            _execShowItemDialog(context, places, category);
          } else if (category == "Places") {
            _execShowItemDialog(context, holograms, category);
          } else if (category == "Holograms") {
            _execShowItemDialog(context, arModels, category);
          } else if (category == "Models") {
            _execShowItemDialog(context,
                await DatabaseService().getStorageList("ar_models"), category);
          }
        },
      ),
    );
  }

  double getScreenWidth(var context) {
    return MediaQuery.of(context).size.width;
  }

  double getScreenHeight(var context) {
    return MediaQuery.of(context).size.height;
  }

  void _execShowItemDialog(
      var context, List items, String categoryTitle) async {
    String save = await Navigator.of(context).push(
      new MaterialPageRoute<String>(
        builder: (BuildContext builderContext) {
          return new OnAddButtonClick(
            items: items,
            categoryTitle: categoryTitle,
          );
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
}
