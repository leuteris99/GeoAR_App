import 'package:cloud_firestore/cloud_firestore.dart';

class Place {
  String _title;
  DocumentReference _reference;
  Map<String, dynamic> _latLng;
  int _aoe;
  DocumentReference _hologramReference;

  Place(String title, Map<String, dynamic> coordinates, int aoe,
      DocumentReference hologramReference,
      {DocumentReference reference}) {
    _title = title;
    this._reference = reference;
    _latLng = coordinates;
    _aoe = aoe;
    _hologramReference = hologramReference;
  }

  String get title => this._title;

  set title(String value) => this._title = value;

  DocumentReference get reference => this._reference;

  set reference(DocumentReference value) => this._reference = value;

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
