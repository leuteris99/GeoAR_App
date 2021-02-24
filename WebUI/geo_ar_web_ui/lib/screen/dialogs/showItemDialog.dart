import 'package:flutter/material.dart';

class ShowItemDialog extends StatefulWidget {
  @override
  _ShowItemDialogState createState() => _ShowItemDialogState();
}

class _ShowItemDialogState extends State<ShowItemDialog> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Colors.black,
        elevation: 0.0,
        title: Text("Title."),
        actions: [
          new TextButton(
            onPressed: () {
              //TODO: Handle save.
              print("get saved!");
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
      body: Text("Attributes are here."),
    );
  }
}
