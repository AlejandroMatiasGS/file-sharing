package com.example.file.sharing.controllers;

import java.nio.ByteBuffer;

public class Converter {

    public static byte[] intToBytes(int valor) {
        return ByteBuffer.allocate(4).putInt(valor).array();
    }

    public static int bytesToInt(byte[] bytes) {
        if (bytes.length != 4) {
            throw new IllegalArgumentException("El arreglo debe tener 4 bytes");
        }
        return ByteBuffer.wrap(bytes).getInt();
    }
}
