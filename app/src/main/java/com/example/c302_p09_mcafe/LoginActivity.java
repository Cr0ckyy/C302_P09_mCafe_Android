
package com.example.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etLoginID, etPassword;
    private Button btnSubmit;

    private String username;
    private String password;

    // Set up sharedPreferences
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGIN_ID = "sharedLoginID";
    public static final String PASSWORD = "sharedPassword";

    private String savedLoginId;
    private String savedPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginID = (EditText)findViewById(R.id.editTextLoginID);
        etPassword = (EditText)findViewById(R.id.editTextPassword);
        btnSubmit = (Button)findViewById(R.id.buttonSubmit);

        loadSharedPref();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etLoginID.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                if (username.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Login failed. Please enter username.", Toast.LENGTH_LONG).show();

                } else if (password.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Login failed. Please enter password.", Toast.LENGTH_LONG).show();

                } else {
                    //proceed to authenticate user
                    OnLogin(v);
                }
            }
        });
    }

    private void OnLogin(View v) {
        // Point X - TODO: call doLogin web service to authenticate user
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);

        client.post("http://10.0.2.2/C302_P09_mCafe/doLogin.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String id = response.getString("id");
                    String apiKey = response.getString("apikey");

                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.putExtra("loginId", id);
                    mainIntent.putExtra("apikey", apiKey);

                    saveSharedPref(username, password);

                    startActivity(mainIntent);

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();

                }
            }

        });

    }

    /* ------------------------------------ METHODS LANGUAGE SHARED PREF - START ------------------------------------ */

    public void saveSharedPref(String username, String password){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_ID, username);
        editor.putString(PASSWORD, password);

        editor.apply();

        //Toast.makeText(this, "Data saved.", Toast.LENGTH_SHORT).show();
    }

    public void loadSharedPref(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        savedLoginId = sharedPreferences.getString(LOGIN_ID, "");
        savedPassword = sharedPreferences.getString(PASSWORD, "");

        etLoginID.setText(savedLoginId);
        etPassword.setText(savedPassword);

        if(!savedLoginId.equals("") && !savedPassword.equals("")){
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("username", savedLoginId);
            params.put("password", savedPassword);

            client.post("http://10.0.2.2/C302_P09_mCafe/doLogin.php", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        String id = response.getString("id");
                        String apiKey = response.getString("apikey");

                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        mainIntent.putExtra("loginId", id);
                        mainIntent.putExtra("apikey", apiKey);

                        startActivity(mainIntent);

                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();

                    }
                }

            });
        }
    }


    /* ------------------------------------ METHODS LANGUAGE SHARED PREF - END ------------------------------------ */






}


