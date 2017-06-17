/**
 * CTRL + ALT + L aligns code lines
 * Created by mohsen on 4/26/2017.
 * cygnus-x.net
 * hashanp.xyz
 * djxmmx.net
 */
package com.wordpress.persiandudeblog.dailyqoute;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class RunBackgroundTask {

    private static Handler uiThreadHandler;

    private static String quoteServerAddress;

    private static Socket socket = null;

    private static Thread socketThread;

    //private final String TAG = MainActivity.class.getSimpleName();

    //singleton object only instantiates once
    private static RunBackgroundTask singleton = new RunBackgroundTask();
    //private constructor so nobody could make a new object
    private RunBackgroundTask(){}


    public static RunBackgroundTask getInstance(){
        return singleton;
    }

    protected static void startSocketThread(Handler uiThread,String qServerAddress){
        uiThreadHandler = uiThread;
        quoteServerAddress = qServerAddress;

        if(socketThread!=null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            socketThread = new Thread(new RunnableTask());
            socketThread.start();

    }

    private enum State
    {
        Success,
        Error
    }

    //Inner class
    private static class RunnableTask implements Runnable {
        @Override
        public void run() {

            StringBuilder stringBuilder = new StringBuilder();
            String str;
            try {
                int portNumber = 17;
                InetAddress inetAddress = InetAddress.getByName(quoteServerAddress);

                socket = new Socket(inetAddress, portNumber);
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while ((str = br.readLine()) != null) {
                    if (!str.equals("")) {
                        stringBuilder.append(str);
                        stringBuilder.append("\n");
                    }
                }
                br.close();
                sendMessageToUi(stringBuilder.toString(),State.Success);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                Log.wtf("Exception@run@socket", ex.getMessage());
                sendMessageToUi(null,State.Error);
            } finally {
                try {
                     if (socket != null) {
                        socket.close();
                     }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Log.wtf("CLOSING_SOCKET", "NotSuccessful.");
                }
            }

        }
    }

    private static void sendMessageToUi(String quote,State state) {

       Message message = uiThreadHandler.obtainMessage();

        if (state == State.Error) message.obj = "Please Check Your Internet Connection!";
        else message.obj = quote;

        message.sendToTarget();
        //Log.wtf(TAG, "RUN METHOD quoteProperty equals " + quoteMessage);
        //Log.d("Test Log","This is a test.");
        //else quoteView.setText("Failed to Connect!");
    }
}








