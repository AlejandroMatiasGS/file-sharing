package com.example.file.sharing.models;

public class MiFile {
    private String name;
    private String path;
    private long size;

    public MiFile(String name, String path, long size) {
        this.name = name;
        this.path = path;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }
}
