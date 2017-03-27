package ro.pub.cs.systems.eim.lab03.phonedialer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PhoneDialerActivity extends AppCompatActivity {

    private static Context context;

    PhoneDialerListener listener = new PhoneDialerListener();
    StringBuilder editTextString = new StringBuilder("");

    private static final int PERMISSION_REQUEST_CALL_PHONE = 1;
    private static final int CONTACTS_MANAGER_REQUEST_CODE = 2;

    private class PhoneDialerListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.answer) {
                if (ContextCompat.checkSelfPermission(PhoneDialerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            PhoneDialerActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_CALL_PHONE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + ((EditText) findViewById(R.id.editText)).getText().toString()));
                    startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.decline) {
                context = getApplicationContext();
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    Method telephonyMethod = tm.getClass().getDeclaredMethod("getITelephony");
                    telephonyMethod.setAccessible(true);
                    Object iTelephony = telephonyMethod.invoke(tm);
                    Method endCallMethod = iTelephony.getClass().getDeclaredMethod("endCall");
                    endCallMethod.invoke(iTelephony);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return;
            }

            if (v.getId() == R.id.contacts) {
                String phoneNumber = ((EditText) findViewById(R.id.editText)).getText().toString();
                System.out.println(phoneNumber);
                if (phoneNumber.length() > 0) {
                    Intent intent = new Intent("ro.pub.cs.systems.eim.lab04.contactsmanager.intent.action.ContactsManagerActivity");
                    intent.putExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY", phoneNumber);
                    startActivityForResult(intent, CONTACTS_MANAGER_REQUEST_CODE);
                } else {
                    Toast.makeText(getApplication(), getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
                }
                return;
            }

            if (v.getId() == R.id.backspace) {
                if (editTextString.length() == 0) {
                    return;
                }
                editTextString.deleteCharAt(editTextString.length() - 1);
            } else {
                if (editTextString.length() == 12) {
                    return;
                }
                switch (v.getId()) {
                    case R.id.button_0:
                        editTextString.append('0');
                        break;
                    case R.id.button_1:
                        editTextString.append('1');
                        break;
                    case R.id.button_2:
                        editTextString.append('2');
                        break;
                    case R.id.button_3:
                        editTextString.append('3');
                        break;
                    case R.id.button_4:
                        editTextString.append('4');
                        break;
                    case R.id.button_5:
                        editTextString.append('5');
                        break;
                    case R.id.button_6:
                        editTextString.append('6');
                        break;
                    case R.id.button_7:
                        editTextString.append('7');
                        break;
                    case R.id.button_8:
                        editTextString.append('8');
                        break;
                    case R.id.button_9:
                        editTextString.append('9');
                        break;
                    case R.id.button_star:
                        editTextString.append('*');
                        break;
                    case R.id.button_hash:
                        editTextString.append('#');
                        break;
                }
            }
            ((EditText) findViewById(R.id.editText)).setText(editTextString);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dialer);

        Button[] buttons = new Button[12];
        ImageButton imageButton = (ImageButton) findViewById(R.id.backspace);
        ImageButton callButton = (ImageButton) findViewById(R.id.answer);
        ImageButton hangButton = (ImageButton) findViewById(R.id.decline);
        ImageButton contactsButton = (ImageButton) findViewById(R.id.contacts);

        buttons[0] = (Button) findViewById(R.id.button_0);
        buttons[1] = (Button) findViewById(R.id.button_1);
        buttons[2] = (Button) findViewById(R.id.button_2);
        buttons[3] = (Button) findViewById(R.id.button_3);
        buttons[4] = (Button) findViewById(R.id.button_4);
        buttons[5] = (Button) findViewById(R.id.button_5);
        buttons[6] = (Button) findViewById(R.id.button_6);
        buttons[7] = (Button) findViewById(R.id.button_7);
        buttons[8] = (Button) findViewById(R.id.button_8);
        buttons[9] = (Button) findViewById(R.id.button_9);
        buttons[10] = (Button) findViewById(R.id.button_star);
        buttons[11] = (Button) findViewById(R.id.button_hash);

        for (int i = 0 ; i < 12 ; i++) {
            buttons[i].setOnClickListener(listener);
        }

        imageButton.setOnClickListener(listener);
        callButton.setOnClickListener(listener);
        hangButton.setOnClickListener(listener);
        contactsButton.setOnClickListener(listener);
    }
}
