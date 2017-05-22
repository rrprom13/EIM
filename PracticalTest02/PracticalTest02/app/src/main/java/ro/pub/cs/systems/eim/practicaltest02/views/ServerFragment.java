package ro.pub.cs.systems.eim.practicaltest02.views;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class ServerFragment extends Fragment {

    private ServerThread serverThread;

    private EditText portEditText;
    private Button connectButton;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String portString = portEditText.getText().toString();
            Log.i(Constants.TAG, portString);
            try {
                int port = Integer.parseInt(portString);
                if (serverThread != null) {
                    serverThread.stopServer();
                }
                serverThread = new ServerThread(portEditText, port);
                serverThread.startServer();
            } catch (NumberFormatException exception) {
                Toast.makeText(getActivity(), "Wrong port number: " + portString, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.server_frame_layout, parent, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        portEditText = (EditText) getActivity().findViewById(R.id.server_port_edit_text);
        connectButton = (Button) getActivity().findViewById(R.id.server_connect_button);

        connectButton.setOnClickListener(buttonClickListener);
    }

    @Override
    public void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }

}
