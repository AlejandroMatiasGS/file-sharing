package com.misoftware.file_sharing.Controlador;

import android.app.Application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Host {
    private ServerSocket server;
    private Socket client;

    public Host() {
        this.client = new Socket();
    }

    public Host(String ip, int port) {
        try {
            this.server = new ServerSocket();
            this.server.bind(new InetSocketAddress(ip, port));
            this.server.setSoTimeout(500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean aceptar() {
        try {
            this.client = server.accept();
            this.client.setSoTimeout(7000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean conectar(String ip, int port, int timeout) {
        try {
            this.client.connect(new InetSocketAddress(ip, port), timeout);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean enviar(byte[] data, int offset, int length) {
        try {
            OutputStream out = this.client.getOutputStream();
            out.write(data, offset, length);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int recibir(byte[] buffer) {
        try {
            InputStream in = this.client.getInputStream();
            return in.read(buffer, 0, buffer.length);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void cerrar() {
        try {
            this.client.close();
        } catch (IOException e) { }
    }

    public void cerrarServer() {
        try {
            this.server.close();
        } catch (IOException e) { }
    }

    public void setSoTimeout(int timeout) {
        try {
            this.client.setSoTimeout(timeout);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
