package eimsimulationtest01.systems.cs.pub.ro.simulationtest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SimulationTest01MainActivity extends AppCompatActivity {

    private final static int SECONDARY_ACTIVITY_REQUEST_CODE = 1;
    private final static int SERVICE_STATUS = 2;
    private final static int SERVICE_STARTED = 3;

    ButtonListener buttonListener = new ButtonListener();
    EditText leftEditText;
    EditText rightEditText;
    Button leftPressButton;
    Button rightPressButton;
    Button switchActivity;

    private int serviceStatus = 2;

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int leftNumberOfClicks = 0;
            int rightNumberOfClicks = 0;
            if (v.getId() == R.id.left_press_button) {
                leftNumberOfClicks = Integer.parseInt(leftEditText.getText().toString()) + 1;
                leftEditText.setText(String.valueOf(leftNumberOfClicks));
            }
            if (v.getId() == R.id.right_press_button) {
                rightNumberOfClicks = Integer.parseInt(rightEditText.getText().toString()) + 1;
                rightEditText.setText(String.valueOf(rightNumberOfClicks));
            }
            if (v.getId() == R.id.second_activity_button) {
                Intent intent = new Intent(getApplicationContext(), SimulationTest01SecondaryActivity.class);
                int numberOfClicks = Integer.parseInt(leftEditText.getText().toString()) +
                        Integer.parseInt(rightEditText.getText().toString());
                intent.putExtra("numberOfClicks", numberOfClicks);
                startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
            }

            if (leftNumberOfClicks + rightNumberOfClicks > 10 && serviceStatus == SERVICE_STATUS) {
                Log.i("[Started]", "Service started - " + leftNumberOfClicks + rightNumberOfClicks);
                Intent intent = new Intent(getApplicationContext(), SimulationTest01Service.class);
                intent.putExtra("firstNumber", leftNumberOfClicks);
                intent.putExtra("secondNumber", rightNumberOfClicks);
                getApplicationContext().startService(intent);
                serviceStatus = SERVICE_STARTED;
            }

        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private IntentFilter intentFilter = new IntentFilter();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("[Message]", intent.getStringExtra("message"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_test01_main);

        leftPressButton = (Button) findViewById(R.id.left_press_button);
        rightPressButton = (Button) findViewById(R.id.right_press_button);
        switchActivity = (Button) findViewById(R.id.second_activity_button);

        leftPressButton.setOnClickListener(buttonListener);
        rightPressButton.setOnClickListener(buttonListener);
        switchActivity.setOnClickListener(buttonListener);

        leftEditText = (EditText) findViewById(R.id.left_edit_text);
        rightEditText = (EditText) findViewById(R.id.right_edit_text);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("leftPressButton")) {
                leftEditText.setText(savedInstanceState.getString("leftPressButton"));
            } else {
                leftEditText.setText(String.valueOf(0));
            }
            if (savedInstanceState.containsKey("rightPressButton")) {
                rightEditText.setText(savedInstanceState.getString("rightPressButton"));
            } else {
                rightEditText.setText(String.valueOf(0));
            }
        } else {
            leftEditText.setText(String.valueOf(0));
            rightEditText.setText(String.valueOf(0));
        }

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(Constants.TAG, "onRestart() method was invoked");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Constants.TAG, "onStart() method was invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
        Log.i(Constants.TAG, "onResume() method was invoked");
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
        Log.i(Constants.TAG, "onPause() method was invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(Constants.TAG, "onStop() method was invoked");
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, SimulationTest01Service.class);
        stopService(intent);
        super.onDestroy();
        Log.i(Constants.TAG, "onDestroy() method was invoked");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("leftPressButton", leftEditText.getText().toString());
        savedInstanceState.putString("rightPressButton", rightEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        if (savedInstanceState.containsKey("leftPressButton")) {
            leftEditText.setText(savedInstanceState.getString("leftPressButton"));
        } else {
            leftEditText.setText(String.valueOf(0));
        }
        if (savedInstanceState.containsKey("rightPressButton")) {
            rightEditText.setText(savedInstanceState.getString("rightPressButton"));
        } else {
            rightEditText.setText(String.valueOf(0));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

}
