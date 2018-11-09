package no.hiof.joakimj.remmenproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    int currentFoodIndex = 0;
    public static int counterImg = 1;
    public static int counter = 1;
    public String DATA_URL = "";
    public String url = "";
    boolean firstImage = true;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    private TextView foodNameTextView;
    private TextView allergiesTextView;
    private TextView commentsTextView;
    private TextView descriptionTextView;
    private ImageView imageView;

    private String foodNameText;
    private Integer allergiesText;
    private String commentsText;
    private String descriptionText;
    private String allergiesHolder;

    RequestQueue requestQueue;
    ArrayList<String> foodImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        foodImages = new ArrayList<String>();

        foodNameTextView = (TextView) findViewById(R.id.foodNameTextView);
        allergiesTextView = (TextView) findViewById(R.id.allergiesTextView);
        commentsTextView = (TextView) findViewById(R.id.commentsTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

        firebaseAuth = FirebaseAuth.getInstance();
        createAuthenticationListener();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        requestQueue = Volley.newRequestQueue(this);

        imageView.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "Swipe Bottom", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                if(firstImage) {};
                DATA_URL = "http://81.166.82.90/uploads/" + counterImg + ".jpg";
                url = "http://81.166.82.90/foodapi.php?food_id=" + counter;

                Toast.makeText(MainActivity.this,  "Swipe Left", Toast.LENGTH_SHORT).show();
                Picasso.get().load(DATA_URL).fit().into(imageView);
                getData();
            }

            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "Swipe Right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "Swipe Top", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(firebaseAuthStateListener != null) {
            firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(firebaseAuthStateListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
        }
    }

    private void createAuthenticationListener() {
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    startActivityForResult(AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }


    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_item:
                AuthUI.getInstance().signOut(this);
                return true;
            case R.id.settings_item:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.favorites_item:
                intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(this, "Signed in as " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    //get Json

    public class foodinfo{
        int id;
        String foodName;
        String comments;
        String oppskrift;
        String description;
        List<String> allergi;
    }

    public List<String> allergiListe(int allergicode) {
        int current_allergi = 65536;
        List<String> output =  new ArrayList<String>();

        while(allergicode > 0) {
            if(current_allergi > allergicode) {
                current_allergi /= 2;
            } else {
                allergicode -= current_allergi;
                output.add(allergiKodeTilNavn(current_allergi));
                current_allergi /= 2;
            }
        }

        return output;
    }

    public String allergiKodeTilNavn(int allergicode) {
        switch (allergicode) {
            case 1 : return "Skalldyr";
            case 2 : return "Laktose";
            case 4 : return "Egg";
            case 8 : return "Pianøtt";
            case 16 : return "Hvete";
            case 32 : return "Soya";
            case 64 : return "Fisk";
            case 128 : return "Lupin";
            default : return "Ingen";
        }
    }

    public void getData() {
        final foodinfo output = new foodinfo();
        try {
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //opening the first object in Json
                        JSONObject obj = response.getJSONObject("object");

                        //adding whats in fNavn to a string
                        foodNameText = (obj.getString("foodName"));
                        commentsText = (obj.getString("comments"));
                        descriptionText = (obj.getString("description"));
                        allergiesText = (obj.getInt("allergier"));

                        //adding string into TextView
                        foodNameTextView.setText(foodNameText);
                        descriptionTextView.setText(descriptionText);
                        commentsTextView.setText("Kommentar: " + "\n" + commentsText);
                        allergiesHolder = "";

                        while (allergiesText > 0) {
                            if (allergiesText >= 8192) {
                                allergiesHolder += "Kjøtt Pattedyr";
                                allergiesText -= 8192;
                            } else if (allergiesText >= 4096) {
                                allergiesHolder += "Svoveldioksid";
                                allergiesText -= 4096;
                            } else if (allergiesText >= 2048) {
                                allergiesHolder += "Bløtdyr";
                                allergiesText -= 2048;
                            } else if (allergiesText >= 1024) {
                                allergiesHolder += "Sennep";
                                allergiesText -= 1024;
                            } else if (allergiesText >= 512) {
                                allergiesHolder += "Selleri";
                                allergiesText -= 512;
                            } else if (allergiesText >= 256) {
                                allergiesHolder += "Nøtter";
                                allergiesText -= 256;
                            } else if (allergiesText >= 128) {
                                allergiesHolder += "Lupin";
                                allergiesText -= 128;
                            } else if (allergiesText >= 64) {
                                allergiesHolder += "Fisk";
                                allergiesText -= 64;
                            } if (allergiesText >= 32) {
                                allergiesHolder += "Soya";
                                allergiesText -= 32;
                            } else if (allergiesText >= 16) {
                                allergiesHolder += "Glutenholdig korn";
                                allergiesText -= 16;
                            } else if (allergiesText >= 8) {
                                allergiesHolder += "Peanøtter";
                                allergiesText -= 8;
                            } else if (allergiesText >= 4) {
                                allergiesHolder += "Egg";
                                allergiesText -= 4;
                            } else if (allergiesText >= 2) {
                                allergiesHolder += "Laktose";
                                allergiesText -= 2;
                            } else if (allergiesText >= 1) {
                                allergiesHolder += "Skalldyr";
                                allergiesText -= 1;
                            }
                            allergiesHolder += "\n";
                        }

                        counterImg++;
                        counter++;
                        allergiesTextView.setText(allergiesHolder);



                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("TAG", "JSONExeption" + e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    Log.i("TAG", "VolleyError" + error);

                    Picasso.get().load(R.drawable.placeholder).into(imageView);
                        foodNameTextView.setText("");
                    allergiesTextView.setText("");
                    descriptionTextView.setText("");
                    commentsTextView.setText("No more listings");
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "ObjectRequest" + e);
        }
    }
}
