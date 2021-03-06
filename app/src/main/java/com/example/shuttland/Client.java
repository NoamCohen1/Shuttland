package com.example.shuttland;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Client {
    private String ip = "52.201.126.29";
    // String ip = "10.0.2.2"; //emulator ip- for testing
    private int port = 3001;
    private PrintWriter mBufferOut;
    private BufferedReader mBufferIn;
    private Socket socket;
    private Thread connect_thread;

    // connect to Server
    private void Connect() {
        Runnable runable = new Runnable() {
            @Override
            public void run() {

                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket(serverAddr, port);
                    mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
                            , true);
                    mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    Log.e("TCP", "C: Error", e);
                    System.out.println(e.toString());
                }

            }
        };
        connect_thread = new Thread(runable);
        connect_thread.start();
    }

    /**
     * Create the request for the server, according the current information of the user.
     * @param message_type - 1: user overloading reporting , 2: user request for prediction.
     * @param weather - weather in celsius degree.
     * @param y - in case of message type 1- prediction from user.
     * @return Prediction from Server
     */
    String sendMessage(final int message_type, int weather, String y) {
        Connect();
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);
        int current_time = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
        String range_time = findRange(current_time);
        String day_str = convertDay(day);
        if(range_time.equals("invalid time")|| day_str.equals("invalid time")){
            disconnect();
            return "";
        }
        String isBreak = ifBreak(current_time);
        String weather_type = temp_range(weather);
        String temp = message_type + "," + weather_type + "\t" + day_str + "\t" + isBreak + "\t" + range_time;
        if (message_type == 1) {
            temp += "\t" + y;
        }
        final String msg = temp;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // waiting for the connection
                    connect_thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mBufferOut != null) {
                    // send message to the server
                    mBufferOut.println(msg);
                    mBufferOut.flush();
                }

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String s = "";
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> result = es.submit(new Callable<String>() {
            public String call() throws Exception {
                // the other thread
                if (message_type == 2 && mBufferIn != null)
                    return readServerMsg();
                return "";
            }
        });
        try {
            // enter to block until the response from the server arrive.
            s = result.get();
        } catch (Exception e) {
            e.getStackTrace();
        }
        es.shutdown();
        disconnect();
        return s;
    }

    /**
     * Convert from int time to range of hours
     * @param curr_time - current time
     * @return range time
     */
    private String findRange(int curr_time) {
        if (curr_time > 730 && curr_time < 930)
            return "7:30-9:30";
        if (curr_time > 930 && curr_time < 1130)
            return "9:30-11:30";
        if (curr_time > 1130 && curr_time < 1330)
            return "11:30-13:30";
        if (curr_time > 1330 && curr_time < 1530)
            return "13:30-15:30";
        if (curr_time > 1530 && curr_time < 1730)
            return "15:30-17:30";
        if (curr_time > 1730 && curr_time < 2030)
            return "17:30-20:30";
        return "invalid time";
    }


    private String convertDay(int day) {
        if (day == 1) {
            return "sunday";
        }
        if (day == 2) {
            return "monday";
        }
        if (day == 3) {
            return "tuesday";
        }
        if (day == 4) {
            return "wednesday";
        }
        if (day == 5) {
            return "thursday";
        }
        if (day == 6) {
            return "friday";
        }
        return "invalid time";
    }

    /**
     * Check if the current time ib break time
     * @param curr_time - current time
     * @return is break
     */
    private String ifBreak(int curr_time) {
        if ((curr_time > 730 && curr_time < 800) || (curr_time > 830 && curr_time < 900)
                || (curr_time > 930 && curr_time < 1000) || (curr_time > 1130 && curr_time < 1200)
                || (curr_time > 1330 && curr_time < 1400) || (curr_time > 1530 && curr_time < 1600))
            return "yes";

        return "no";
    }

    private String temp_range(int temp) {
        if (temp < 17) {
            return "rainy";
        }
        if (temp < 22) {
            return "overcast";
        }
        if (temp < 29) {
            return "nice";
        }
        return "sunny";
    }

    private void disconnect() {
        if (this.socket != null) {
            try {
                this.socket.close();
                this.mBufferOut.close();
            } catch (IOException e) {
                Log.e("TCP", "C: Error", e);
                System.out.println((e.toString()));
            }
        }
    }

    private String readServerMsg() {
        String data = "";
        if (socket.isConnected()) {
            try {
                data = mBufferIn.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (data == null) {
                data = "";
            }
        }
        return data;
    }
}
