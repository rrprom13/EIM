package ro.pub.cs.systems.eim.practicaltest02.views;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientAsyncTask;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class ClientFragment extends Fragment {

    private ClientAsyncTask clientAsyncTask;

    private EditText addressEditText;
    private EditText portEditText;
    private EditText cityEditText;
    private Spinner informationSpinner;
    private Spinner httpMethodSpinner;
    private Button getForecastButton;
    private TextView resultTextView;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String address = addressEditText.getText().toString();
            String portString = portEditText.getText().toString();
            String city = cityEditText.getText().toString();
            String information = informationSpinner.getSelectedItem().toString();
            String httpMethod = httpMethodSpinner.getSelectedItem().toString();

            clientAsyncTask = new ClientAsyncTask(resultTextView);
            clientAsyncTask.execute(address, portString, city, information, httpMethod);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.client_frame_layout, parent, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        addressEditText = (EditText) getActivity().findViewById(R.id.client_address_edit_text);
        portEditText = (EditText) getActivity().findViewById(R.id.client_port_edit_text);
        cityEditText = (EditText) getActivity().findViewById(R.id.client_city_edit_text);
        informationSpinner = (Spinner) getActivity().findViewById(R.id.client_information_spinner);
        httpMethodSpinner = (Spinner) getActivity().findViewById(R.id.client_http_method_spinner);
        getForecastButton = (Button) getActivity().findViewById(R.id.client_get_forecast_button);
        resultTextView = (TextView) getActivity().findViewById(R.id.client_response_text_view);

        getForecastButton.setOnClickListener(buttonClickListener);
    }

}