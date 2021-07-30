package com.example.c302_p09_mcafe;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<MenuCategory> adapter;
    private ArrayList<MenuCategory> list;
    private AsyncHttpClient client;

    // Declare loginId and apikey
    private String loginId;
    private String apikey;

    // Set up sharedPreferences
    public static final String SHARED_PREFS = "sharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listViewMenuCategories);
        list = new ArrayList<MenuCategory>();
        adapter = new ArrayAdapter<MenuCategory>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        client = new AsyncHttpClient();

        //TODO: read loginId and apiKey from SharedPreferences
        Intent loginIntent = getIntent();
        loginId = loginIntent.getStringExtra("loginId");
        apikey = loginIntent.getStringExtra("apikey");

        // TODO: if loginId and apikey is empty, go back to LoginActivity
        if (loginId.equals("") || apikey.equals("")) {
            Intent emptyIntent = new Intent(MainActivity.this, LoginActivity.class);
            emptyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(emptyIntent);
        }

        //TODO: Point X - call getMenuCategories.php to populate the list view
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("loginId", loginId);
        params.put("apikey", apikey);

        client.post("http://10.0.2.2/C302_P09_mCafe/getMenuCategories.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject category = (JSONObject) response.get(i);
                        String id = category.getString("menu_item_category_id");
                        String cat = category.getString("menu_item_category_description");

                        Log.e("CAT", id + cat);

                        MenuCategory c = new MenuCategory(id, cat);
                        list.add(c);
                    }
                } catch (JSONException e) {

                }
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MenuCategory selected = list.get(position);

                //TODO: make Intent to DisplayMenuItemsActivity passing the categoryId
                Intent menuItemIntent = new Intent(MainActivity.this, DisplayMenuItemsActivity.class);
                menuItemIntent.putExtra("loginId", loginId);
                menuItemIntent.putExtra("apikey", apikey);
                menuItemIntent.putExtra("categoryId", selected.getCategoryId());

                startActivity(menuItemIntent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

            Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
