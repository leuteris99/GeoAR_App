import 'package:cloud_firestore/cloud_firestore.dart';

class ArModel {
  String _title;
  DocumentReference _reference;
  String _modelURL;
  double _scale;
  double _distFromAnchor;
  num _animationSpeed;

  ArModel(String title, String modelURL,
      {DocumentReference reference,
      double scale = 1.0,
      double distFromAnchor = 0.0,
      num animationSpeed = 10000}) {
    this.title = title;
    this._reference = reference;
    this.modelURL = modelURL;
    this.scale = scale;
    this.distFromAnchor = distFromAnchor;
    this.animationSpeed = animationSpeed;
  }
  String get title => this._title;

  set title(String value) => this._title = value;

  DocumentReference get reference => this._reference;

  set reference(DocumentReference reference) => this._reference = reference;

  get modelURL => this._modelURL;

  set modelURL(value) => this._modelURL = value;

  double get scale => this._scale;

  set scale(double value) => this._scale = value;

  double get distFromAnchor => this._distFromAnchor;

  set distFromAnchor(double value) => this._distFromAnchor = value;

  num get animationSpeed => this._animationSpeed;

  set animationSpeed(num value) => this._animationSpeed = value;

  @override
  String toString() {
    return "ArModel {" +
        "\ntitle: " +
        title +
        "\nreference: " +
        reference.toString() +
        "\nmodelURL: " +
        modelURL +
        "\nscale: " +
        scale.toString() +
        "\ndistFromAnchor: " +
        distFromAnchor.toString() +
        "\nanimationSpeed: " +
        animationSpeed.toString() +
        "\n}";
  }
}
