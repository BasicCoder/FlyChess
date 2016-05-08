package com.example.room;

import android.app.Application;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ApplicationUtil extends Application{
	public static final String ADDRESS = "172.19.48.102";
    public static final int PORT = 8899;
    private int timeout = 60000;
    private Socket socket;
    private OutputStream dos = null;
    private InputStream dis = null;


    public void init() throws IOException, Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //与服务器建立连接
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(ADDRESS, PORT), timeout);
                    socket = new Socket(ADDRESS, PORT);
                    dos = socket.getOutputStream();
                    dis = socket.getInputStream();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public OutputStream getDos() {
        return dos;
    }

    public void setDos(OutputStream dos) {
        this.dos = dos;
    }

    public InputStream getDis() {
        return dis;
    }

    public void setDis(InputStream dis) {
        this.dis = dis;
    }
}
