package no.hiof.joakimj.remmenproject;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import no.hiof.joakimj.remmenproject.Holder.SearchAdapter;
import no.hiof.joakimj.remmenproject.Modell.Food;


public class SearchActivity extends AppCompatActivity {
    
    RecyclerView recyclerView;
    String Query, searchUrl;

    List<Food> foodList;

    RequestQueue requestQueue;
    DatabaseReference searchDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        String query = intent
                .getExtras()
                .getString("Query");

        searchUrl = "http://81.166.82.90/foodsearch.php?foodName="+query;

        requestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.searchRecyclerView); 
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodList = new ArrayList<>();
        
        displaySearchApi(searchUrl);

    }

    private void displaySearchApi(String searchUrl) {



        Log.i("TAG", "kommer jeg hit 1");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, searchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    Log.i("TAG", "kommer jeg hit 2 " + array.length());


                    Log.i("TAG", "kommer jeg hit 12312 " + array.length());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject food = array.getJSONObject(i);

                        foodList.add(new Food(
                                food.getString("food_id").toString(),
                                food.getString("foodName").toString(),
                                food.getString("allergi")
                        ));

                    }
                    Log.i("TAG", "kommer jeg hit 5");
                    SearchAdapter adapter = new SearchAdapter(SearchActivity.this, foodList);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchActivity.this, "Error in Volley: " + error, Toast.LENGTH_SHORT).show();
                    }
        });
        Log.i("TAG", "kommer jeg hit 6");
        Volley.newRequestQueue(this).add(stringRequest);
        Log.i("TAG", "kommer jeg hit 7");
    }
}