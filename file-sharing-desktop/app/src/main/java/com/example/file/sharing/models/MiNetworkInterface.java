package com.example.file.sharing.models;

public class MiNetworkInterface {
    private String name;
    private String ip;

    public MiNetworkInterface(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }
}
