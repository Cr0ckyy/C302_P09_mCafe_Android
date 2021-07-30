package com.example.c302_p09_mcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DisplayMenuItemDetails extends AppCompatActivity {

    private EditText etItem, etPrice;
    private Button btnUpdate, btnDelete;

    // Init variables for login credentials
    private String loginId;
    private String apikey;
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu_item_details);

        etItem = findViewById(R.id.etItem);
        etPrice = findViewById(R.id.etPrice);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);


        // Get loginId, apikey, categoryId from the previous Intent
        Intent loginIntent = getIntent();
        loginId = loginIntent.getStringExtra("loginId");
        apikey = loginIntent.getStringExtra("apikey");
        itemId = loginIntent.getStringExtra("itemId");

        populateFieldsWithItem();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });

    }

    private void updateItem() {
        String desc = etItem.getText().toString().trim();
        String priceInput = etPrice.getText().toString().trim();
        Double price = Double.parseDouble(priceInput);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("loginId", loginId);
        params.put("apikey", apikey);
        params.put("id", itemId);
        params.put("description", desc);
        params.put("unitPrice", price);

        client.post("http://10.0.2.2/C302_P09_mCafe/updateMenuItemById.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String message = response.getString("message");

                    Toast.makeText(DisplayMenuItemDetails.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {

                }
            }
        });
    }

    private void deleteItem() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("loginId", loginId);
        params.put("apikey", apikey);
        params.put("id", itemId);

        client.post("http://10.0.2.2/C302_P09_mCafe/deleteMenuItemById.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String message = response.getString("message");

                    Toast.makeText(DisplayMenuItemDetails.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {

                }
            }
        });
    }

    private void populateFieldsWithItem(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("loginId", loginId);
        params.put("apikey", apikey);

        client.post("http://10.0.2.2/C302_P09_mCafe/getMenuItemDetails.php?id=" + itemId, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String item = response.getString("menu_item_description");
                    String price = response.getString("menu_item_unit_price");

                    etItem.setText(item);
                    etPrice.setText(price);
                } catch (JSONException e) {

                }
            }
        });
    }
}