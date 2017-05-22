package ro.pub.cs.systems.eim.practicaltest02.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ClientAsyncTask extends AsyncTask<String, String, Void> {

    private TextView resultTextView;

    public ClientAsyncTask(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            Socket socket = new Socket(params[0], Integer.parseInt(params[1]));
            BufferedReader br = Utilities.getReader(socket);
            PrintWriter pw = Utilities.getWriter(socket);

            pw.println(params[2]);
            pw.println(params[3]);
            pw.println(params[4]);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        resultTextView.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        Log.i(Constants.TAG, progress[0]);
        // TODO exercise 6b
        // - append the content to serverMessageTextView
       // String serverMessageString = serverMessageTextView.getText().toString();
    //    serverMessageTextView.setText(serverMessageString + progress[0]);
    }

    @Override
    protected void onPostExecute(Void result) {}
}