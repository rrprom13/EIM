package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;

public class ServerThread extends Thread {

    public boolean isRunning;

    private ServerSocket serverSocket;

    private EditText serverPortEditText;
    private int serverPort;

    public ServerThread(EditText serverPortEditText, int serverPort) {
        this.serverPortEditText = serverPortEditText;
        this.serverPort = serverPort;
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v(Constants.TAG, "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
        }
        Log.v(Constants.TAG, "stopServer() method was invoked");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(serverPort);
            while (isRunning) {
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    CommunicationThread communicationThread = new CommunicationThread(socket);
                    communicationThread.start();
                }
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
        }
    }

}
