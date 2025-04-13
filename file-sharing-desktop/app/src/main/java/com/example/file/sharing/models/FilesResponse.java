package com.example.file.sharing.models;

import java.util.List;

public class FilesResponse {
    private int totalSize;
    private List<MiFile> files;

    public FilesResponse(int totalSize, List<MiFile> files) {
        this.totalSize = totalSize;
        this.files = files;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public List<MiFile> getFiles() {
        return files;
    }
    
    
}
