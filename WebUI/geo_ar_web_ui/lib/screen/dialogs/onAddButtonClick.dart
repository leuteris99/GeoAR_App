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
    String currentlySelected = dropDownValue.toString();

    return _OnAddButtonClickState(items, categoryTitle, dropDownValue,
        currentlySelected, inputControllers);
  }
}

class _OnAddButtonClickState extends State<OnAddButtonClick> {
  List _items;
  String _categoryTitle;
  dynamic _fileResult;
  var _dropDownValue;
  String _currentlySelected;
  List _inputControllers;
  final _formKey = GlobalKey<FormState>();

  _OnAddButtonClickState(this._items, this._categoryTitle, this._dropDownValue,
      this._currentlySelected, this._inputControllers);

  @override
  Widget build(BuildContext context) {
    List<Widget> widgetList = [];
    int controllerCount = 0;
    widgetList.add(
      getEditText("Title *", (String value) {
        if (value.isEmpty) {
          return "Please enter a title";
        }
        return null;
      }, controller: _inputControllers[controllerCount++]),
    );

    if (_categoryTitle == "Routes") {
      widgetList.add(Text(
        "Select Places:",
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

      widgetList.add(getEditText("Latitude *", (String value) {
        if (value.isEmpty) {
          return "Please enter Latitude";
        }
        try {
          double tmp = double.parse(value);
          if (tmp > 90 || tmp < -90) {
            return "Error: coordinates are out of range.";
          }
        } catch (e) {
          return "Error: non-numeric charecters found.";
        }
        return null;
      },
          textInputType: TextInputType.number,
          controller: _inputControllers[controllerCount++]));
      widgetList.add(getEditText("Longitude *", (String value) {
        if (value.isEmpty) {
          return "Please enter Longitude";
        }
        try {
          double tmp = double.parse(value);
          if (tmp > 180 || tmp < -180) {
            return "Error: coordinates are out of range.";
          }
        } catch (e) {
          return "Error: non-numeric charecters found.";
        }
        return null;
      },
          textInputType: TextInputType.number,
          controller: _inputControllers[controllerCount++]));
      widgetList.add(Padding(
        padding: const EdgeInsets.only(top: 30),
        child: getEditText("Area of Effect", (String value) {
          if (value.isNotEmpty) {
            int tmp = int.tryParse(value);
            if (tmp <= 0) {
              return "Error: area of effect must be positive number.";
            }
            return tmp != null ? null : "Error: non-numeric charecters found.";
          } else {
            return null;
          }
        },
            textInputType: TextInputType.number,
            controller: _inputControllers[controllerCount++]),
      ));

      widgetList.add(Text(
        "Select Hologram:",
        style: TextStyle(fontSize: 20),
      ));
      widgetList.add(getDropDown());
    } else if (_categoryTitle == "Holograms") {
      widgetList.add(
        getEditText(
          "Image URL",
          (String value) {
            // if (value.isEmpty) {
            //   return "Please enter the Image URL";
            // }
            return null;
          },
          textInputType: TextInputType.url,
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(
        getEditText(
          "Description *",
          (String value) {
            if (value.isEmpty) {
              return "Please enter a Description";
            }
            return null;
          },
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(
        getEditText(
          "Question",
          (String value) {
            if (value.isNotEmpty) {
              if (_inputControllers[4].text == "" ||
                  _inputControllers[5].text == "" ||
                  _inputControllers[4].text == null ||
                  _inputControllers[5].text == null) {
                return "Error: there is no answers for this question";
              }
            }
            return null;
          },
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(
        getEditText(
          "Answer 1",
          (String value) {
            if (value.isNotEmpty) {
              if (_inputControllers[3].text == "" ||
                  _inputControllers[3].text == null) {
                return "Error: there is no question for this answer.";
              }
            }
            return null;
          },
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(
        getEditText(
          "Answer 2",
          (String value) {
            if (value.isNotEmpty) {
              if (_inputControllers[3].text == "" ||
                  _inputControllers[3].text == null) {
                return "Error: there is no question for this answer.";
              }
            }
            return null;
          },
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(
        getEditText(
          "Web URL",
          (String value) {
            // if (value.isEmpty) {
            //   return "Please enter a Description";
            // }
            return null;
          },
          textInputType: TextInputType.url,
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(Text(
        "Select Model:",
        style: TextStyle(fontSize: 20),
      ));
      widgetList.add(getDropDown());
    } else if (_categoryTitle == "Models") {
      widgetList.add(
        getEditText("Scale", (String value) {
          if (value.isNotEmpty || value != "") {
            return double.tryParse(value) != null
                ? null
                : "Error: non-numeric charecters found.";
          }
          return null;
        },
            textInputType: TextInputType.number,
            controller: _inputControllers[controllerCount++]),
      );
      widgetList.add(
        getEditText("Distance from the Center", (String value) {
          if (value.isNotEmpty || value != "") {
            return double.tryParse(value) != null
                ? null
                : "Error: non-numeric charecters found.";
          }
          return null;
        },
            textInputType: TextInputType.number,
            controller: _inputControllers[controllerCount++]),
      );
      widgetList.add(
        getEditText(
          "Animation Speed",
          (String value) {
            if (value.isNotEmpty || value != "") {
              return num.tryParse(value) != null
                  ? null
                  : "Error: non-numeric charecters found.";
            }
            return null;
          },
          textInputType: TextInputType.number,
          controller: _inputControllers[controllerCount++],
        ),
      );
      widgetList.add(Text(
        "Select File:",
        style: TextStyle(fontSize: 20),
      ));
      widgetList.add(getDropDown());
      widgetList.add(
        Text(
          "Selected: " + _currentlySelected,
          style: TextStyle(color: Colors.white),
        ),
      );
      widgetList.add(Padding(
        padding: const EdgeInsets.all(16.0),
        child: Text(
          "OR",
          style: TextStyle(fontSize: 14),
        ),
      ));
      widgetList.add(
        MaterialButton(
          child: Text(
            "Upload",
            style: TextStyle(
              color: Colors.blueGrey[300],
              fontSize: 18,
            ),
          ),
          onPressed: () async {
            var tmp = await DatabaseService().selectFile();
            print("file selected");

            setState(() {
              _fileResult = tmp;
              _currentlySelected = _fileResult.files.first.name;
            });
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
                  if (_formKey.currentState.validate()) {
                    List selectedPlaces = [];
                    for (int i = 0; i < _items.length; i++) {
                      if (_val[i]) {
                        selectedPlaces.add(_items[i].title);
                      }
                    }
                    data = MyRoute(_inputControllers[0].text, selectedPlaces);

                    DatabaseService().createDocument("routes", data);
                  }
                  break;
                case "Places":
                  if (_formKey.currentState.validate()) {
                    Map<String, dynamic> coordinatesMap = new Map();
                    coordinatesMap = {
                      "latitude": double.parse(_inputControllers[1].text),
                      "longitude": double.parse(_inputControllers[2].text),
                    };
                    int aoe = 10;
                    if (_inputControllers[3].text != "" &&
                        _inputControllers[3].text != null) {
                      aoe = int.parse(_inputControllers[3].text);
                    }
                    data = Place(
                      _inputControllers[0].text,
                      coordinatesMap,
                      aoe,
                      _dropDownValue.reference,
                    );

                    DatabaseService().createDocument("marker", data);
                  }
                  break;
                case "Holograms":
                  if (_formKey.currentState.validate()) {
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
                  }
                  break;
                case "Models":
                  if (_formKey.currentState.validate()) {
                    double scale = 10;
                    if (_inputControllers[1].text != "" &&
                        _inputControllers[1].text != null) {
                      scale = double.parse(_inputControllers[1].text);
                    }
                    double distFromAnchor = 10;
                    if (_inputControllers[2].text != "" &&
                        _inputControllers[2].text != null) {
                      distFromAnchor = double.parse(_inputControllers[2].text);
                    }
                    num animationSpeed = 10;
                    if (_inputControllers[3].text != "" &&
                        _inputControllers[3].text != null) {
                      animationSpeed = num.parse(_inputControllers[3].text);
                    }

                    data = ArModel(
                      _inputControllers[0].text,
                      _fileResult != null
                          ? _fileResult.files.first.name
                          : _dropDownValue.toString(),
                      scale: scale,
                      distFromAnchor: distFromAnchor,
                      animationSpeed: animationSpeed,
                    );

                    DatabaseService().createDocument("arModel", data);
                  }
                  break;
              }
              print("checks: " + _val.toString());
              print(data);

              if (_fileResult != null) {
                DatabaseService().uploadFile("ar_models", _fileResult);
              }
              if (_formKey.currentState.validate()) {
                Navigator.of(context).pop("I am saved YEAH!");
              }
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
      body: Center(
        child: Container(
          width: getScreenWidth(context) > 1200
              ? 1000
              : (2 * getScreenWidth(context)) / 3,
          child: Form(
            key: _formKey,
            child: new Column(
              children: widgetList,
            ),
          ),
        ),
      ),
    );
  }

  Widget getEditText(
    String title,
    String validator(String value), {
    TextInputType textInputType = TextInputType.text,
    TextEditingController controller,
  }) {
    return Padding(
      padding: const EdgeInsets.all(10.0),
      child: new TextFormField(
        validator: (value) => validator(value),
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
    return Padding(
      padding: const EdgeInsets.all(10.0),
      child: Container(
        padding: const EdgeInsets.all(10),
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
                _fileResult = null;
                _currentlySelected = _dropDownValue.toString();
              },
            );
          },
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
