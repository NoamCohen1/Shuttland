package com.example.shuttland;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

public class Client {
    String ip = "52.201.126.29";
    int port = 3001;
    PrintWriter mBufferOut;
    public void Connect() {

        Runnable runable = new Runnable(){
            @Override
            public void run(){

                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    Socket socket = new Socket(serverAddr, port);
                    mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
                            , true);
                } catch (IOException e) {
                    Log.e("TCP", "C: Error", e);
                    System.out.println(e.toString());
                }

            }
        };
        Thread thread = new Thread(runable);
        thread.start();
    }
    public void sendMessage(final int temp) {
        Date date=new Date();
//                            String day = String.format("%E", date ).toLowerCase();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);
        int current_time = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {
                    mBufferOut.println(temp);
                    mBufferOut.flush();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
//
//    public void close() {
//        if(this.socket!=null) {
//            try {
//                this.socket.close();
//                this.mBufferOut.close();
//            } catch (IOException e) {
//                Log.e("TCP", "C: Error", e);
//                System.out.println((e.toString()));
//            }
//        }
//    }
}
