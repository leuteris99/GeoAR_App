import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:file_picker/file_picker.dart';
import 'package:firebase_storage/firebase_storage.dart';
import 'package:geo_ar_web_ui/models/ArModel.dart';
import 'package:geo_ar_web_ui/models/Hologram.dart';
import 'package:geo_ar_web_ui/models/Place.dart';
import 'package:geo_ar_web_ui/models/MyRoute.dart';

class DatabaseService {
  // collection reference
  final FirebaseFirestore _firebaseFirestore = FirebaseFirestore.instance;
  final FirebaseStorage _firebaseStorage = FirebaseStorage.instance;

  // get collection
  Future getCollection(String collection) async {
    return await _firebaseFirestore.collection(collection).get();
  }

  // get steam of a collection
  Stream<QuerySnapshot> getStreamCollection(String collection) {
    return _firebaseFirestore.collection(collection).snapshots();
  }

  // update a document in the firebase
  Future updateDocument(String documentID, var data) async {
    var sendingData;
    if (data is MyRoute) {
      sendingData = {
        'title': data.title,
        'places': data.places,
      };
    } else if (data is Place) {
      sendingData = {
        'title': data.title,
        'latLng': data.latLng,
        'aoe': data.aoe,
        'hologramReference': data.hologramReference,
      };
    } else if (data is Hologram) {
      sendingData = {
        'title': data.title,
        'imageURL': data.imageURL,
        'description': data.description,
        'question': data.question,
        'answerArray': data.answerArray,
        'webURL': data.webURL,
        'arModelReference': data.arModelReference,
      };
    } else if (data is ArModel) {
      sendingData = {
        'title': data.title,
        'modelURL': data.modelURL,
        'scale': data.scale,
        'distFromAnchor': data.distFromAnchor,
        'animationSpeed': data.animationSpeed,
      };
    } else {
      print("error: updating thedocuments.");
      return;
    }
    return _firebaseFirestore.doc(documentID).update(sendingData);
  }

  // create a new document in the firebase
  Future createDocument(String destinationCollection, var data) async {
    var sendingData;
    if (data is MyRoute) {
      sendingData = {
        'title': data.title,
        'places': data.places,
      };
    } else if (data is Place) {
      sendingData = {
        'title': data.title,
        'latLng': data.latLng,
        'aoe': data.aoe,
        'hologramReference': data.hologramReference,
      };
    } else if (data is Hologram) {
      sendingData = {
        'title': data.title,
        'imageURL': data.imageURL,
        'description': data.description,
        'question': data.question,
        'answerArray': data.answerArray,
        'webURL': data.webURL,
        'arModelReference': data.arModelReference,
      };
    } else if (data is ArModel) {
      sendingData = {
        'title': data.title,
        'modelURL': data.modelURL,
        'scale': data.scale,
        'distFromAnchor': data.distFromAnchor,
        'animationSpeed': data.animationSpeed,
      };
    } else {
      print("error: updating thedocuments.");
      return;
    }
    return _firebaseFirestore
        .collection(destinationCollection)
        .add(sendingData);
  }

  dynamic selectFile() async {
    final result = await FilePicker.platform
        .pickFiles(type: FileType.any, allowMultiple: false);

    return result;
  }

  Future uploadFile(String destinationCollection, dynamic fileResult) async {
    print(fileResult);
    if (fileResult.files.first != null) {
      var fileBytes = fileResult.files.first.bytes;
      var fileName = fileResult.files.first.name;

      // upload file
      await _firebaseStorage
          .ref('$destinationCollection/$fileName')
          .putData(fileBytes);
    }
  }

  Future<List> getStorageList(String path) async {
    ListResult result = await _firebaseStorage.ref(path).listAll();

    List<String> storageList = [];
    result.items.forEach((Reference ref) {
      storageList.add(ref.name);
    });
    return storageList;
  }
}
