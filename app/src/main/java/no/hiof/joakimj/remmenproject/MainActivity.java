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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RatingDialogListener {

    private static final int RC_SIGN_IN = 1;
    int currentFoodIndex = 0;
    public static int counterImg = 1;
    public static int counter = 1;
    public String DATA_URL = "";
    public String url = "";
    boolean firstImage = true;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    private TextView foodNameTextView, allergiesTextView, commentsTextView, descriptionTextView;
    private ImageView imageView;

    private String foodNameText, commentsText, descriptionText, allergiesHolder, userUid, foodId, weburl, uploads, foodapi;
    private Integer allergiesText;

    private FloatingActionButton btnFav, btnRating;

    RequestQueue requestQueue;
    RatingBar ratingBar;
    ArrayList<String> foodImages;

//    FirebaseDatabase database;
    DatabaseReference ratingTbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //layout Views
        foodNameTextView = (TextView) findViewById(R.id.foodNameTextView);
        allergiesTextView = (TextView) findViewById(R.id.allergiesTextView);
        commentsTextView = (TextView) findViewById(R.id.commentsTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);


        imageView = findViewById(R.id.imageView);
        foodImages = new ArrayList<String>();

        weburl = getString(R.string.url_webpage);
        foodapi = getString(R.string.foodapi);
        uploads = getString(R.string.uploads);

        btnFav = (FloatingActionButton)findViewById(R.id.btn_fav);
        btnRating = (FloatingActionButton)findViewById(R.id.btn_ratingBar);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        createAuthenticationListener();
        userUid = firebaseAuth.getUid();
      //  ratingTbl = database.getReference("Rating");


        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog(); 
            }
        });

        requestQueue = Volley.newRequestQueue(this);

        imageView.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, R.string.swipe_bottom, Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                counterImg++;
                counter++;

                DATA_URL = weburl + uploads + counterImg + getString(R.string.jpg);
                url = weburl + foodapi + counter;

                Toast.makeText(MainActivity.this,  R.string.swipe_left, Toast.LENGTH_SHORT).show();
                Picasso.get().load(DATA_URL).fit().into(imageView);
                getData();
            }

            public void onSwipeRight() {
                counterImg--;
                counter--;

                DATA_URL = weburl + uploads + counterImg + getString(R.string.jpg);
                url = weburl + foodapi + counter;

                Toast.makeText(MainActivity.this,  R.string.swipe_right, Toast.LENGTH_SHORT).show();
                Picasso.get().load(DATA_URL).fit().into(imageView);
                getData();
            }

            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, R.string.swipe_top, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText(R.string.submit)
                .setNegativeButtonText(R.string.cancel)
                .setNoteDescriptions(Arrays.asList(getString(R.string.very_bad), getString(R.string.not_good),
                        getString(R.string.quite_ok), getString(R.string.good), getString(R.string.really_good)))
                .setDefaultRating(1)
                .setTitle(R.string.rate_this_food)
                .setDescription(R.string.please_select_some_stars_and_give_feedback)
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint(R.string.please_write_your_comment_here)
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(MainActivity.this)
                .show();
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
                Toast.makeText(this, getString(R.string.signed_in_as) + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.sign_in_canceled, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NonNull String comments) {
        //Get rating and upload to database
        final Rating rating = new Rating(userUid,
                foodId,
                String.valueOf(value),
                comments);
        ratingTbl.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userUid).exists()) {
                    //remove old value
                    ratingTbl.child(userUid).removeValue();
                    //update new value
                    ratingTbl.child(userUid).setValue(rating);
                } else {
                    //update new value
                    ratingTbl.child(userUid).setValue(rating);
                }
                Toast.makeText(MainActivity.this, R.string.thank_you_for_evaluating_the_food, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
            case 1 : return getString(R.string.shellfish);
            case 2 : return getString(R.string.lactose_milk);
            case 4 : return getString(R.string.egg);
            case 8 : return getString(R.string.peanutt);
            case 16 : return getString(R.string.gluten_wheat);
            case 32 : return getString(R.string.soy);
            case 64 : return getString(R.string.fish);
            case 128 : return getString(R.string.lupine);
            default : return getString(R.string.none);
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
                        commentsTextView.setText(getString(R.string.comment) + "\n" + commentsText);
                        allergiesHolder = "";

                        foodId = "\"" + counter + "\"";

                        while (allergiesText > 0) {
                            if (allergiesText >= 8192) {
                                allergiesHolder += getString(R.string.meat_mammal);
                                allergiesText -= 8192;
                            } else if (allergiesText >= 4096) {
                                allergiesHolder += getString(R.string.sulfur);
                                allergiesText -= 4096;
                            } else if (allergiesText >= 2048) {
                                allergiesHolder += getString(R.string.molluscs);
                                allergiesText -= 2048;
                            } else if (allergiesText >= 1024) {
                                allergiesHolder += getString(R.string.mustard);
                                allergiesText -= 1024;
                            } else if (allergiesText >= 512) {
                                allergiesHolder += getString(R.string.celery);
                                allergiesText -= 512;
                            } else if (allergiesText >= 256) {
                                allergiesHolder += getString(R.string.nuts);
                                allergiesText -= 256;
                            } else if (allergiesText >= 128) {
                                allergiesHolder += getString(R.string.lupine);
                                allergiesText -= 128;
                            } else if (allergiesText >= 64) {
                                allergiesHolder += getString(R.string.fish);
                                allergiesText -= 64;
                            } if (allergiesText >= 32) {
                                allergiesHolder += getString(R.string.soy);
                                allergiesText -= 32;
                            } else if (allergiesText >= 16) {
                                allergiesHolder += getString(R.string.gluten_wheat);
                                allergiesText -= 16;
                            } else if (allergiesText >= 8) {
                                allergiesHolder += getString(R.string.peanutt);
                                allergiesText -= 8;
                            } else if (allergiesText >= 4) {
                                allergiesHolder += getString(R.string.egg);
                                allergiesText -= 4;
                            } else if (allergiesText >= 2) {
                                allergiesHolder += getString(R.string.lactose_milk);
                                allergiesText -= 2;
                            } else if (allergiesText >= 1) {
                                allergiesHolder += getString(R.string.shellfish);
                                allergiesText -= 1;
                            }
                            allergiesHolder += "\n";
                        }


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
