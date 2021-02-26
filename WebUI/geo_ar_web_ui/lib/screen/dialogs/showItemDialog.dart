import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:geo_ar_web_ui/models/ArModel.dart';
import 'package:geo_ar_web_ui/models/Hologram.dart';
import 'package:geo_ar_web_ui/models/MyRoute.dart';
import 'package:geo_ar_web_ui/models/Place.dart';
import 'package:geo_ar_web_ui/services/databaseService.dart';

List<bool> _val = [];

class OnAddButtonClick extends StatefulWidget {
  final List items;
  final String categoryTitle;
  OnAddButtonClick(
      {Key key, @required this.items, @required this.categoryTitle})
      : super(key: key);
  @override
  _OnAddButtonClickState createState() {
    _val = [];
    List inputControllers = [];

    items.forEach((element) {
      _val.add(false);
    });

// 7 giati toso einai o megistos arithmos atributes apo ta adikeimena pou exo ftiaxei
    for (int i = 0; i < 7; i++) {
      inputControllers.add(TextEditingController());
    }

    var dropDownValue = items[0];

    return _OnAddButtonClickState(
        items, categoryTitle, dropDownValue, inputControllers);
  }
}

class _OnAddButtonClickState extends State<OnAddButtonClick> {
  List _items;
  String _categoryTitle;
  dynamic _fileResult;
  var _dropDownValue;
  List _inputControllers;

  _OnAddButtonClickState(this._items, this._categoryTitle, this._dropDownValue,
      this._inputControllers);

  @override
  Widget build(BuildContext context) {
    List<Widget> widgetList = [];
    int controllerCount = 0;
    widgetList.add(
        getEditText("Title", controller: _inputControllers[controllerCount++]));

    if (_categoryTitle == "Routes") {
      widgetList.add(Text(
        "Select " + _categoryTitle + ":",
        style: TextStyle(fontSize: 20),
      ));

      int i = 0;
      _items.forEach((element) {
        widgetList.add(getCheckListTile(element, i));
        i++;
      });
      i = 0;
    } else if (_categoryTitle == "Places") {
      widgetList.add(Text(
        "Enter Coordinates:",
        style: TextStyle(fontSize: 20),
      ));

      widgetList.add(getEditText("Latitude",
          textInputType: TextInputType.number,
          controller: _inputControllers[controllerCount++]));
      widgetList.add(getEditText("Longitude",
          textInputType: TextInputType.number,
          controller: _inputControllers[controllerCount++]));
      widgetList.add(getEditText("Area of Effect",
          textInputType: TextInputType.number,
          controller: _inputControllers[controllerCount++]));

      widgetList.add(Text(
        "Select " + _categoryTitle + ":",
        style: TextStyle(fontSize: 20),
      ));
      widgetList.add(getDropDown());
    } else if (_categoryTitle == "Holograms") {
      widgetList.add(
        getEditText(
          "Image URL",
          textInputType: TextInputType.url,
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(
        getEditText(
          "Description",
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(
        getEditText(
          "Question",
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(
        getEditText(
          "Answer 1",
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(
        getEditText(
          "Answer 2",
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(
        getEditText(
          "Web URL",
          textInputType: TextInputType.url,
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(Text(
        "Select " + _categoryTitle + ":",
        style: TextStyle(fontSize: 20),
      ));
      widgetList.add(getDropDown());
    } else if (_categoryTitle == "Models") {
      widgetList.add(
        getEditText("Scale",
            textInputType: TextInputType.number,
            controller: _inputControllers[controllerCount++]),
      );
      widgetList.add(
        getEditText("Distance from the Center",
            textInputType: TextInputType.number,
            controller: _inputControllers[controllerCount++]),
      );
      widgetList.add(
        getEditText(
          "Animation Speed",
          textInputType: TextInputType.number,
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(getDropDown());
      widgetList.add(
        MaterialButton(
          child: Text(
            "Upload",
            style: TextStyle(color: Colors.white),
          ),
          onPressed: () async {
            _fileResult = await DatabaseService().selectFile();
            print("file selected");
          },
        ),
      );
    }

    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Colors.black,
        elevation: 0.0,
        title: Text("New " + _categoryTitle),
        actions: [
          new TextButton(
            onPressed: () {
              //TODO: Handle save.
              print("get saved!");
              var data;
              switch (_categoryTitle) {
                case "Routes":
                  List selectedPlaces = [];
                  for (int i = 0; i < _items.length; i++) {
                    if (_val[i]) {
                      selectedPlaces.add(_items[i].title);
                    }
                  }
                  data = MyRoute(_inputControllers[0].text, selectedPlaces);

                  DatabaseService().createDocument("routes", data);
                  break;
                case "Places":
                  Map<String, dynamic> coordinatesMap = new Map();
                  coordinatesMap = {
                    "latitude": double.parse(_inputControllers[1].text),
                    "longitude": double.parse(_inputControllers[2].text),
                  };

                  data = Place(
                    _inputControllers[0].text,
                    coordinatesMap,
                    int.parse(_inputControllers[3].text),
                    _dropDownValue.reference,
                  );

                  DatabaseService().createDocument("marker", data);
                  break;
                case "Holograms":
                  List answerArray = [
                    _inputControllers[4].text,
                    _inputControllers[5].text
                  ];
                  data = Hologram(
                    _inputControllers[0].text,
                    _inputControllers[2].text,
                    imageUrl: _inputControllers[1].text.toString(),
                    question: _inputControllers[3].text,
                    answerArray: answerArray,
                    webURL: _inputControllers[6].text,
                    arModelReference:
                        _dropDownValue.reference is DocumentReference
                            ? _dropDownValue.reference
                            : "",
                  );

                  DatabaseService().createDocument("hologram", data);
                  break;
                case "Models":
                  data = ArModel(
                    _inputControllers[0].text,
                    _dropDownValue.toString(),
                    scale: double.parse(_inputControllers[1].text),
                    distFromAnchor: double.parse(_inputControllers[2].text),
                    animationSpeed: double.parse(_inputControllers[3].text),
                  );

                  DatabaseService().createDocument("arModel", data);
                  break;
              }
              print("checks: " + _val.toString());
              print(data);

              if (_fileResult != null) {
                DatabaseService().uploadFile("ar_models", _fileResult);
              }
              Navigator.of(context).pop("I am saved YEAH!");
            },
            child: Text(
              "SAVE",
              style: Theme.of(context)
                  .textTheme
                  .subtitle1
                  .copyWith(color: Colors.white),
            ),
          ),
        ],
      ),
      body: new Column(
        children: widgetList,
      ),
    );
  }

  Widget getEditText(String title,
      {TextInputType textInputType = TextInputType.text,
      TextEditingController controller}) {
    return new TextField(
      controller: controller,
      keyboardType: textInputType,
      style: TextStyle(
        color: Colors.white,
      ),
      decoration: InputDecoration(
        border: UnderlineInputBorder(),
        fillColor: Colors.grey[900],
        filled: true,
        // hintText: "Enter " + title + "",
        labelText: title,
        labelStyle: TextStyle(
          color: Colors.white,
        ),
        // hintStyle: TextStyle(
        //   color: Colors.grey[200],
        //   fontSize: 10,
        // ),
      ),
    );
  }

  Widget getCheckListTile(var object, int index) {
    return CheckboxListTile(
      value: _val[index],
      onChanged: (v) {
        setState(() {
          _val[index] = !_val[index];
          print("object");
        });
      },
      title: Text(
        object.title,
        style: TextStyle(color: Colors.white),
      ),
    );
  }

  Widget getDropDown() {
    return Container(
      padding: EdgeInsets.all(10),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(10.0),
        color: Colors.grey[900],
      ),
      child: new DropdownButton(
        value: _dropDownValue,
        items: _items.map<DropdownMenuItem<dynamic>>((dynamic value) {
          return DropdownMenuItem<dynamic>(
            value: value,
            child: Text(
              (value is String ? value : value.title),
              style: TextStyle(
                color: Colors.white,
              ),
            ),
          );
        }).toList(),
        onChanged: (value) {
          setState(
            () {
              _dropDownValue = value;
            },
          );
        },
      ),
    );
  }
}
