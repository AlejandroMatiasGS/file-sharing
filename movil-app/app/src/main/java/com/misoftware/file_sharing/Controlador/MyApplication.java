package com.misoftware.file_sharing.Controlador;

import android.app.Application;

import java.util.logging.SocketHandler;

public class MyApplication extends Application {
    private Host host;

    public Host getHost() {
        return this.host;
    }

    public void setHost(Host h) {
        this.host = h;
    }

}
