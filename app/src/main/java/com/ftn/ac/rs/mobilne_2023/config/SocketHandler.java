package com.ftn.ac.rs.mobilne_2023.config;

import android.util.Log;

import com.ftn.ac.rs.mobilne_2023.BuildConfig;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketHandler {

    static Socket socket;

    public static void setSocket(){
        try {
            socket = IO.socket("http://" + BuildConfig.IP_ADDR + ":3000");
        }catch (Exception e){
            Log.println(Log.ERROR, "Error while connecting to socket", e.getMessage());
        }
    }

    public static Socket getSocket(){
        return socket;
    }

}
