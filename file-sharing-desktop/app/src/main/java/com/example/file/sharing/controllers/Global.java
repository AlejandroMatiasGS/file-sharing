package com.example.file.sharing.controllers;

import com.example.file.sharing.models.MiFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.JFrame;

public class Global {
    private static Global instance;
    private List<MiFile> files;
    private Stack<JFrame> frames;
    private String downloadPath;
    
    private Global() { 
        files = new ArrayList<>(); 
        frames = new Stack<>();
    }
    
    public static Global getInstance() {
        if(instance == null) instance = new Global();
        return instance;
    }
    
    public void addFiles(List<MiFile> _files) { files.addAll(_files); }
    
    public void removeFile(MiFile file) { files.remove(file); }
    
    public List<MiFile> getFiles() { return files; }
    
    public void push(JFrame f) {
        frames.push(f);
    }
    
    public void popFrame() {
        if (!frames.isEmpty()) {
            JFrame currentFrame = frames.pop();
            currentFrame.dispose();
            if (!frames.isEmpty()) {
                JFrame previousFrame = frames.peek();
                previousFrame.setVisible(true);
            }
        }
    }

    public void popUntil(Class<?> targetClass) {
        while (!frames.isEmpty() && !frames.peek().getClass().equals(targetClass)) {
            JFrame currentFrame = frames.pop();
            currentFrame.dispose();
        }
        if (!frames.isEmpty()) {
            JFrame target = frames.peek();
            target.setVisible(true);
        }
    }
    
    public void setDownloadPath(String value) {
        this.downloadPath = value;
    }

    public String getDownloadPath() {
        return downloadPath;
    }
    
    
}
