package no.hiof.joakimj.remmenproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.hiof.joakimj.remmenproject.Database.Database;
import no.hiof.joakimj.remmenproject.Holder.CommentAdapter;
import no.hiof.joakimj.remmenproject.Holder.SearchAdapter;
import no.hiof.joakimj.remmenproject.Modell.Comment;
import no.hiof.joakimj.remmenproject.Modell.Favorites;
import no.hiof.joakimj.remmenproject.Modell.Rating;
import no.hiof.joakimj.remmenproject.Modell.User;

import static java.lang.String.valueOf;


public class MainActivity extends AppCompatActivity implements RatingDialogListener {

    private static final int RC_SIGN_IN = 1;
    int currentFoodIndex = 0;
    public static int counterImg = 0;
    public static int counter = 0;
    public String DATA_URL = "";
    public String url = "";
    public String searchUrl = "";
    boolean firstImage = true;
    boolean firstTimeLoggedIn = false;
    boolean favorited = false;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    public DatabaseReference favDatabaseReference;

    private TextView contentTextView, foodNameTextView, allergiesTextView, commentsTextView, descriptionTextView, txt_foodName, txt_foodId;
    private ImageView imageView, favImage;
    private RecyclerView commentRV;

    static public String userUid, nameUid;
    private String contentText, nameUser, descriptionText, allergiesHolder, foodId, weburl, uploads, foodapi, foodTempHolder, ratingUrl;
    public String foodNameText;
    private Integer allergiesText;
    SearchView searchView = null;
    private Float ratingNumber;

    private FloatingActionButton btnFav, btnRating;

    static public RequestQueue requestQueue;
    RatingBar ratingBar;
    ArrayList<String> foodImages;
    List<Comment> commentList;
    public static User makeUser;

    //    FirebaseDatabase database;
    DatabaseReference ratingTbl;
    Database localDB;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String temp = "";



        /*if(food_id < 0) {
            getSearchData(food_id);
            Picasso.get().load("http://81.166.82.90/uploads/" + food_id).fit().into(imageView);
        }*/

        //layout Views
        foodNameTextView = (TextView) findViewById(R.id.foodNameTextView);
        allergiesTextView = (TextView) findViewById(R.id.allergiesTextView);
        contentTextView = (TextView) findViewById(R.id.contentTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

        //comment recycleView
        commentRV = findViewById(R.id.comment_recycler_view);
        commentRV.setLayoutManager(new LinearLayoutManager(this));



        imageView = findViewById(R.id.imageView);
        favImage = findViewById(R.id.favImage);
        foodImages = new ArrayList<String>();
        makeUser = new User();


        weburl = getString(R.string.url_webpage);
        foodapi = getString(R.string.foodapi);
        uploads = getString(R.string.uploads);

        //btnFav = (FloatingActionButton)findViewById(R.id.btn_fav);
        btnRating = (FloatingActionButton) findViewById(R.id.btn_ratingBar);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        createAuthenticationListener();
        userUid = firebaseAuth.getUid();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        //Local Database
        //ratingTbl = database.getReference("Rating");
        localDB = new Database(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        favDatabaseReference = firebaseDatabase.getReference("favorites");

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        requestQueue = Volley.newRequestQueue(this);


        imageView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, R.string.swipe_bottom, Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                counterImg++;
                counter++;

                DATA_URL = weburl + uploads + counterImg + getString(R.string.jpg);
                url = weburl + foodapi + counter;

                Toast.makeText(MainActivity.this, R.string.swipe_left, Toast.LENGTH_SHORT).show();
                Picasso.get().load(DATA_URL).fit().into(imageView);
                getData();

                Log.i("TAG", "user: " + makeUser.getfNavn() + makeUser.geteNavn());

                displayComment();

            }

            public void onSwipeRight() {
                counterImg--;
                counter--;


                DATA_URL = weburl + uploads + counterImg + getString(R.string.jpg);
                url = weburl + foodapi + counter;

                Toast.makeText(MainActivity.this, R.string.swipe_right, Toast.LENGTH_SHORT).show();
                Picasso.get().load(DATA_URL).fit().into(imageView);
                getData();

                displayComment();
            }

            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, R.string.swipe_top, Toast.LENGTH_SHORT).show();

            }
        });

        if(!firstTimeLoggedIn) {
            getUserData();
        }
    }

    private void getSearchData(String addingValue) {

        String searching_url = "http://81.166.82.90/foodapi.php?food_id=" + addingValue;
        try {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, searching_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //opening the first object in Json
                            JSONObject obj = response.getJSONObject("object");

                            //adding whats in fNavn to a string
                            foodNameText = (obj.getString("foodName"));
                            contentText = (obj.getString("comments"));
                            descriptionText = (obj.getString("description"));
                            allergiesText = (obj.getInt("allergier"));

                            try {
                                ratingNumber = BigDecimal.valueOf(obj.getDouble("rating")).floatValue();
                                Log.i("Rating", "rating: " + (obj.getString("rating")));
                            } catch (JSONException e) {
                                ratingNumber = 0.0f;
                                Log.i("Rating", "Rating didnt fetch, no rating: " + e);
                            }


                            //adding string into TextView
                            foodNameTextView.setText(foodNameText);
                            descriptionTextView.setText(descriptionText);
                            contentTextView.setText(getString(R.string.comment) + "\n" + contentText);
                            ratingBar.setRating(ratingNumber);
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
                                } else if (allergiesText >= 32) {
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
                        contentTextView.setText("No more listings");
                    }
                });
                requestQueue.add(jsonObjectRequest);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("TAG", "ObjectRequest" + e);
            }
    }

    private void startRegistrering() {

        if(firstTimeLoggedIn) {
            Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
            intent.putExtra("key_userUid", userUid);
            intent.putExtra("key_username", nameUser);
            startActivity(intent);
        } else {
            getUserData();
        }
    }


    private void displayComment() {
        String COMMENT_URL = "http://81.166.82.90/comment.php?food_id=" + (counter);

        commentList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, COMMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject comment = array.getJSONObject(i);
                        commentList.add(new Comment(
                                comment.getString("comment"),
                                comment.getString("fNavn"),
                                comment.getInt("rating")
                        ));
                    }
                    CommentAdapter adapter = new CommentAdapter(MainActivity.this, commentList);
                    commentRV.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error in Volley: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
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

        if (firebaseAuthStateListener != null) {
            firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
        }

        if(!(SearchAdapter.tester == null)) {
           getSearchData(SearchAdapter.tester);
           Picasso.get().load("http://81.166.82.90/uploads/"+SearchAdapter.tester+".jpg").fit().into(imageView);
           counter = Integer.parseInt(SearchAdapter.tester);
           counterImg = Integer.parseInt(SearchAdapter.tester);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseAuthStateListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
        }
    }

    private void createAuthenticationListener() {
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    firstTimeLoggedIn = true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                nameUser = user.getDisplayName();
                userUid = user.getUid();
                Toast.makeText(this, getString(R.string.signed_in_as) + user.getDisplayName(), Toast.LENGTH_SHORT).show();

                startRegistrering();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.sign_in_canceled, Toast.LENGTH_SHORT).show();
                finish();
            }
        }


    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "test " + query, Toast.LENGTH_LONG).show();
                //getDataSearch(query);
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("Query", query);
                startActivity(intent);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                valueOf(value),
                comments);

        String i = rating.getRateValue();
        //hardcoded to not bug
        String j = valueOf(makeUser.getId());
        String k = rating.getFoodId();
        String l = rating.getComment();

        ratingUrl = "http://81.166.82.90/set_rating?rating=" + i +
                "&user_id=" + j +
                "&food_id=" + k +
                "&comments=" + l;

        new SendRating().execute();
    }



    public void addToFavorites(View view) {

        if(favorited) {
            favorited = false;
        } else {
            favorited = true;
        }
        if(favorited) {
            favImage.setBackgroundResource(R.drawable.ic_favorite_highlighted_24dp);
        }

        String tempFoodId = foodId;
        String tempFoodName = foodNameText;

        String id = valueOf(makeUser.getId());
        Favorites favorites = new Favorites(tempFoodId,tempFoodName, valueOf(makeUser.getId()));
        favDatabaseReference.child(id).child(tempFoodId).setValue(favorites);
        Log.i("banana", tempFoodId);
        Toast.makeText(getApplicationContext(), "Favorited . . . " + id, Toast.LENGTH_LONG).show();

    }


    public class SendRating extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(ratingUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String value = reader.readLine();
                Log.i("TAG", "Result is: " + value);

            } catch (Exception e) {
                Log.i("TAG", "Something went wrong in sendRating: " + e);
            }
            return null;
        }

    }

    //get Json

    public void getData() {

        try {
            final JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //opening the first object in Json
                        JSONObject obj = response.getJSONObject("object");

                        //adding whats in fNavn to a string
                        foodNameText = (obj.getString("foodName"));
                        contentText = (obj.getString("comments"));
                        descriptionText = (obj.getString("description"));
                        allergiesText = (obj.getInt("allergier"));

                        try {
                            ratingNumber = BigDecimal.valueOf(obj.getDouble("rating")).floatValue();
                            Log.i("Rating", "rating: " + (obj.getString("rating")));
                        } catch (JSONException e) {
                            ratingNumber = 0.0f;
                            Log.i("Rating", "Rating didnt fetch, no rating: " + e);
                        }


                        //adding string into TextView
                        foodNameTextView.setText(foodNameText);
                        descriptionTextView.setText(descriptionText);
                        contentTextView.setText(getString(R.string.comment) + "\n" + contentText);
                        ratingBar.setRating(ratingNumber);
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
                            } else if (allergiesText >= 32) {
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
                    contentTextView.setText("No more listings");
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "ObjectRequest" + e);
        }
    }

    public void getUserData() {

        String set_user_url = "http://81.166.82.90/userapi.php?google_id=" + userUid;

        try {
            final JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, set_user_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //opening the first object in Json
                        JSONObject obj = response.getJSONObject("object");
                        Log.i("TAGUSER", " " + obj.getString("fNavn"));

                        makeUser.setId(Integer.valueOf(obj.getString("user_id")));
                        makeUser.setfNavn(obj.getString("fNavn"));
                        makeUser.seteNavn(obj.getString("eNavn"));
                        makeUser.setAllergi(Integer.valueOf(obj.getString("allergier")));


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("TAG", "JSONExeption" + e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    Log.i("TAG", "VolleyError !! " + error);

                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "ObjectRequest" + e);
        }
    }

}

