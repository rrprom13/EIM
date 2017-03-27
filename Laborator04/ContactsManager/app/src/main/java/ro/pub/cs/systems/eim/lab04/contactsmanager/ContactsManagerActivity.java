package ro.pub.cs.systems.eim.lab04.contactsmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {

    private static final String EMPTY_STRING = "";
    private static final int CONTACTS_MANAGER_REQUEST_CODE = 2;

    ButtonListener buttonListener = new ButtonListener();

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.showLayoutButton) {
                String buttonText = ((Button) findViewById(R.id.showLayoutButton)).getText().toString();
                if (buttonText.equals(getString(R.string.show_additional_fields))) {
                    findViewById(R.id.innerDownLayout).setVisibility(View.VISIBLE);
                    ((Button) findViewById(R.id.showLayoutButton)).setText(R.string.hide_additional_fields);
                } else if (buttonText.equals(getString(R.string.hide_additional_fields))) {
                    findViewById(R.id.innerDownLayout).setVisibility(View.GONE);
                    ((Button) findViewById(R.id.showLayoutButton)).setText(R.string.show_additional_fields);
                }
            }
            if (v.getId() == R.id.cancelButton) {
                setResult(Activity.RESULT_CANCELED, new Intent());
            }
            if (v.getId() == R.id.saveButton) {
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
                String phone = ((EditText) findViewById(R.id.phoneEditText)).getText().toString();
                String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
                String address = ((EditText) findViewById(R.id.addressEditText)).getText().toString();
                String jobTitle = ((EditText) findViewById(R.id.jobTitleEditText)).getText().toString();
                String company = ((EditText) findViewById(R.id.companyEditText)).getText().toString();
                String website = ((EditText) findViewById(R.id.websiteEditText)).getText().toString();
                String im = ((EditText) findViewById(R.id.imEditText)).getText().toString();

                if (!name.equals(EMPTY_STRING)) {
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                }
                if (!phone.equals(EMPTY_STRING)) {
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                }
                if (!email.equals(EMPTY_STRING)) {
                    intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                }
                if (!address.equals(EMPTY_STRING)) {
                    intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                }
                if (!jobTitle.equals(EMPTY_STRING)) {
                    intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
                }
                if (!company.equals(EMPTY_STRING)) {
                    intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                }
                ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
                if (!website.equals(EMPTY_STRING)) {
                    ContentValues websiteRow = new ContentValues();
                    websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                    websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                    contactData.add(websiteRow);
                }
                if (!im.equals(EMPTY_STRING)) {
                    ContentValues imRow = new ContentValues();
                    imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                    imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                    contactData.add(imRow);
                }
                intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
        //        startActivity(intent);
                startActivityForResult(intent, CONTACTS_MANAGER_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        Button showLayoutButton = (Button) findViewById(R.id.showLayoutButton);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);

        EditText phoneEditText = (EditText) findViewById(R.id.phoneEditText);

        showLayoutButton.setOnClickListener(buttonListener);
        saveButton.setOnClickListener(buttonListener);
        cancelButton.setOnClickListener(buttonListener);

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneEditText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode) {
            case CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }
}
