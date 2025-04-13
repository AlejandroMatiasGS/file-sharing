class MiFile {
  final String name;
  final String path;
  final int size;

  MiFile({required this.name, required this.path, required this.size});

  Map<String, dynamic> toJson() {
    return {
      "name": name,
      "path": path,
      "size": size,
    };
  }
}
