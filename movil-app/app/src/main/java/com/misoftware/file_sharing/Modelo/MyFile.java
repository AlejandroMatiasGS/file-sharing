package com.misoftware.file_sharing.Modelo;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyFile implements Serializable {
    @SerializedName("Name")
    private String name;
    @SerializedName("Path")
    private String path;
    @SerializedName("Size")
    private long size;

    public MyFile(String name, String path, long size) {
        this.name = name;
        this.path = path;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
