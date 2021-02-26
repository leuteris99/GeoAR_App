import 'package:cloud_firestore/cloud_firestore.dart';

class Place {
  String _title;
  Map<String,dynamic> _latLng;
  int _aoe;
  DocumentReference _hologramReference;

  Place(String title, Map<String,dynamic> coordinates, int aoe,
      DocumentReference hologramReference) {
    _title = title;
    _latLng = coordinates;
    _aoe = aoe;
    _hologramReference = hologramReference;
  }

  String get title => this._title;

  set title(String value) => this._title = value;

  get latLng => this._latLng;

  set latLng(value) => this._latLng = value;

  get aoe => this._aoe;

  set aoe(value) => this._aoe = value;

  get hologramReference => this._hologramReference;

  set hologramReference(value) => this._hologramReference = value;

  @override
  String toString() {
    return "Place {" +
        "\ntitle: " +
        title +
        "\nlatLng: {\n\tlatitude:" +
        latLng["latitude"].toString() +
        ",\n\tlongitude: " +
        latLng["longitude"].toString() +
        ",\n}\naoe: " +
        aoe.toString() +
        "\nhologramReference: " +
        hologramReference.toString() +
        "\n}";
  }
}
