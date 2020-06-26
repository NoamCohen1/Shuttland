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
    Socket socket;
    public void Connect() {

        Runnable runable = new Runnable(){
            @Override
            public void run(){

                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket(serverAddr, port);
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
    public void sendMessage(final int message_type,String y) {
        Date date=new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);
        int current_time = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
        String range_time=findRange(current_time);
        if(range_time.equals("invalid time"))
            return;
        String isBreak= ifBreak(current_time);
        String temp=message_type+","+day+","+isBreak+","+range_time;
        if(message_type==1) {
            temp += "," + y;
        }
        final String msg=temp;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {
                    mBufferOut.println(msg);
                    mBufferOut.flush();
                }
                disconnect();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public String findRange(int curr_time){
       if(curr_time>730&& curr_time<930)
           return "7:30-9:30";
        if(curr_time>930&& curr_time<1130)
            return "9:30-11:30";
        if(curr_time>1130&& curr_time<1330)
            return "11:30-13:30";
        if(curr_time>1330&& curr_time<1530)
            return "13:30-15:30";
        if(curr_time>1530&& curr_time<1730)
            return "15:30-17:30";
        if(curr_time>1730&& curr_time<2030)
            return "17:30-20:30";
        return "invalid time";
    }

    public String ifBreak (int curr_time){
        if((curr_time>730&& curr_time<800)||(curr_time>830&& curr_time<900)
            ||(curr_time>930&& curr_time<1000) || (curr_time>1130&& curr_time<1200)
            || (curr_time>1330&& curr_time<1400) || (curr_time>1530&& curr_time<1600))
            return "yes";

        return "no";
    }

    public void disconnect() {
        if(this.socket!=null) {
            try {
                this.socket.close();
                this.mBufferOut.close();
            } catch (IOException e) {
                Log.e("TCP", "C: Error", e);
                System.out.println((e.toString()));
            }
        }
    }
}
