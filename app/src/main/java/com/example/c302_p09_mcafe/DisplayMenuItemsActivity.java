package com.example.c302_p09_mcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DisplayMenuItemsActivity extends AppCompatActivity {

    private ListView lvCatItem;
    private ArrayList<MenuCategoryItem> alCatItem;
    private ArrayAdapter<MenuCategoryItem> aaCatItem;

    // Init variables for login credentials
    private String loginId;
    private String apikey;
    private String categoryId;

    // Set up sharedPreferences
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu_items);

        lvCatItem = findViewById(R.id.listViewMenuCategoryItems);

        alCatItem = new ArrayList<MenuCategoryItem>();

        aaCatItem = new MenuItemAdapter(this, R.layout.menu_item_row, alCatItem);
        lvCatItem.setAdapter(aaCatItem);

        Intent loginIntent = getIntent();

        // Get loginId, apikey, categoryId from the previous Intent
        loginId = loginIntent.getStringExtra("loginId");
        apikey = loginIntent.getStringExtra("apikey");
        categoryId = loginIntent.getStringExtra("categoryId");


        lvCatItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuCategoryItem selected = alCatItem.get(position);

                Intent itemDetailsIntent = new Intent(DisplayMenuItemsActivity.this, DisplayMenuItemDetails.class);
                itemDetailsIntent.putExtra("loginId", loginId);
                itemDetailsIntent.putExtra("apikey", apikey);
                itemDetailsIntent.putExtra("itemId", selected.getId());

                startActivity(itemDetailsIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        alCatItem.clear();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("loginId", loginId);
        params.put("apikey", apikey);

        client.post("http://10.0.2.2/C302_P09_mCafe/getMenuItemsByCategory.php?categoryId=" + categoryId, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject category = (JSONObject) response.get(i);
                        String id = category.getString("menu_item_id");
                        String description = category.getString("menu_item_description");
                        Double unitPrice = category.getDouble("menu_item_unit_price");

                        MenuCategoryItem c = new MenuCategoryItem(id, categoryId, description, unitPrice);
                        alCatItem.add(c);
                    }
                } catch (JSONException e) {

                }
                aaCatItem.notifyDataSetChanged();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submain, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            // TODO: Clear SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // TODO: Redirect back to login screen

            Intent logoutIntent = new Intent(DisplayMenuItemsActivity.this, LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
            return true;
        }else if(id == R.id.menu_addmenuitem){
            Intent addIntent = new Intent(DisplayMenuItemsActivity.this, AddMenuItemActivity.class);
            addIntent.putExtra("loginId", loginId);
            addIntent.putExtra("apikey", apikey);
            addIntent.putExtra("categoryId", categoryId);

            startActivity(addIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}