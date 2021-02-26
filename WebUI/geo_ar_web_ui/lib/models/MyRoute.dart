class MyRoute {
  String _title;
  List _places;

  MyRoute(String title, List places) {
    this.title = title;
    this.places = places;
  }
  String get title => this._title;

  set title(String value) => this._title = value;

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
