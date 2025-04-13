class MiConverter {

  static List<int> intToBytes(int value) {
    List<int> bytes = [];
    for (int i = 3; i >= 0; i--) {
      bytes.add((value >> (i * 8)) & 0xFF);
    }
    return bytes;
  }

  static int bytesToInt(List<int> bytes) {
    if (bytes.length != 4) {
      throw ArgumentError('El arreglo de bytes debe tener una longitud de 4');
    }
    int value = 0;
    for (int i = 0; i < 4; i++) {
      value = (value << 8) | bytes[i];
    }
    return value;
  }
}