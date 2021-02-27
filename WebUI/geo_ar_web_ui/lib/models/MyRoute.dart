import 'package:cloud_firestore/cloud_firestore.dart';

class MyRoute {
  String _title;
  List _places;
  DocumentReference _reference;

  MyRoute(String title, List places, {DocumentReference reference}) {
    this.title = title;
    this._reference = reference;
    this.places = places;
  }
  String get title => this._title;

  set title(String value) => this._title = value;

  DocumentReference get reference => this._reference;

  set reference(DocumentReference value) => this._reference = value;

  get places => this._places;

  set places(value) => this._places = value;

  @override
  String toString() {
    return "Route {" +
        "\ntitle: " +
        title +
        "\nplaces: " +
        places.toString() +
        "\n}";
  }
}
