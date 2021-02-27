import 'package:cloud_firestore/cloud_firestore.dart';

class Hologram {
  String _title;
  DocumentReference _reference;
  String _imageURL;
  String _description;
  String _question;
  List _answerArray;
  String _webURL;
  dynamic _arModelReference;

  Hologram(String title, String description,
      {DocumentReference reference,
      String imageUrl = "",
      String question = "",
      List answerArray,
      String webURL = "",
      dynamic arModelReference}) {
    this._title = title;
    this._reference = reference;
    this._description = description;
    this._imageURL = imageUrl;
    this._question = question;
    this._answerArray = answerArray;
    this._webURL = webURL;
    this._arModelReference = arModelReference;
  }

  String get title => this._title;

  set title(String value) => this._title = value;

  DocumentReference get reference => this._reference;

  set reference(DocumentReference value) => this._reference = value;

  String get imageURL => this._imageURL;

  set imageURL(String value) => this._imageURL = value;

  String get description => this._description;

  set description(String value) => this._description = value;

  String get question => this._question;

  set question(String value) => this._question = value;

  get answerArray => this._answerArray;

  set answerArray(value) => this._answerArray = value;

  String get webURL => this._webURL;

  set webURL(String value) => this._webURL = value;

  get arModelReference => this._arModelReference;

  set arModelReference(value) => this._arModelReference = value;

  @override
  String toString() {
    return "Hologram {" +
        "\n    title: " +
        title.toString() +
        ",\n    imageURl: " +
        imageURL.toString() +
        ",\n    description: " +
        description.toString() +
        ",\n    question: " +
        question.toString() +
        ",\n    answerArray: " +
        answerArray.toString() +
        ",\n    webURL: " +
        webURL.toString() +
        ",\n    arModelReference: " +
        arModelReference.toString() +
        "\n}";
  }
}
