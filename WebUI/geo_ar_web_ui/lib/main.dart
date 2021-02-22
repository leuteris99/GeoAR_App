import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Web UI',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
        textTheme: TextTheme(
            bodyText1: TextStyle(
              color: Colors.white,
            ),
            bodyText2: TextStyle(color: Colors.white)),
      ),
      home: MyHomePage(title: 'GeoAR app-Web UI'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Center(child: Text(widget.title)),
        backgroundColor: Colors.black,
      ),
      backgroundColor: Colors.black,
      body: new Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: <Widget>[
          makeList("Routes", context, ["one", "two", "Three"]),
          makeList("Places", context, ["one", "two", "Three"]),
          makeList("Holograms", context, ["one", "two", "Three"]),
          makeList("Models", context, ["one", "two", "Three"]),
        ],
      ),
    );
  }
}

Container makeList(String title, var context, List<String> list) {
  List<Widget> textList = [];
  textList.add(Container(
    height: 20,
  ));
  textList.add(makeItem(title, context));

  for (String item in list) {
    textList.add(makeItem(item, context));
  }
  return Container(
    width: (getScreenWidth(context)) / 5.0,
    child: Card(
      child: new Column(
        children: textList,
      ),
      color: Colors.grey[900],
    ),
  );
}

Widget makeItem(String name, var context) {
  return Container(
    height: 50,
    width: (getScreenWidth(context) / 5.0) - 20,
    margin: const EdgeInsets.all(8.0),
    child: Card(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(25)),
      color: Colors.black,
      child: Align(
        alignment: Alignment(-0.8,0.0),
        child: Text(name),
      ),
    ),
  );
}

double getScreenWidth(var context) {
  return MediaQuery.of(context).size.width;
}
