package com.example.c302_p09_mcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AddMenuItemActivity extends AppCompatActivity {

    private String loginId, apikey, categoryId;
    private EditText etItem, etPrice;
    private Button btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        etItem = findViewById(R.id.etItem);
        etPrice = findViewById(R.id.etPrice);
        btnAdd = findViewById(R.id.btnAdd);

        Intent loginIntent = getIntent();
        loginId = loginIntent.getStringExtra("loginId");
        apikey = loginIntent.getStringExtra("apikey");
        categoryId = loginIntent.getStringExtra("categoryId");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = etItem.getText().toString().trim();
                String etPriceVal = etPrice.getText().toString().trim();

                // Check for any empty fields
                if(item.length() == 0){
                    Toast.makeText(AddMenuItemActivity.this, "Item name cannot be blank.", Toast.LENGTH_SHORT).show();

                }else if(etPriceVal.length() == 0){
                    Toast.makeText(AddMenuItemActivity.this, "Price cannot be blank.", Toast.LENGTH_SHORT).show();

                }else{

                    // If no empty fields, proceed to add
                    Double price = Double.parseDouble(etPriceVal);

                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("loginId", loginId);
                    params.put("apikey", apikey);
                    params.put("categoryId", categoryId);
                    params.put("description", item);
                    params.put("unitPrice", price);

                    client.post("http://10.0.2.2/C302_P09_mCafe/addMenuItem.php", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                String message = response.getString("message");

                                Toast.makeText(AddMenuItemActivity.this, message, Toast.LENGTH_SHORT).show();
                                finish();
                            } catch (JSONException e) {
                                Toast.makeText(AddMenuItemActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

    }


}