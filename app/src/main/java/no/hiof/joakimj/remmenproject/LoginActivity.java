package no.hiof.joakimj.remmenproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private LoginButton facebookLoginBtn;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        isLogout = getIntent().getBooleanExtra("isLogout", false);
        facebookLoginBtn = findViewById(R.id.facebookLoginBtn);
        facebookLoginBtn.setReadPermissions(Arrays.asList("email"));

        facebookLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null && !isLogout && (facebookLoginBtn.getText().equals("Log out")
                        || facebookLoginBtn.getText().equals("Logg av"))) {
                    //save user data to database
                    FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid())
                            .child("uid").setValue(mAuth.getUid());
                        goMainScreen();
                }
                isLogout = false;
            }
        };
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Error logging in", Toast.LENGTH_LONG).show();
                }
                else{
                    saveFacebookUserData();//TODO: test
                }
            }
        });
    }
    public void saveFacebookUserData()
    {
        mAuth = FirebaseAuth.getInstance();
        // Line to let us see why it's not working
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

        // calls the /user/me endpoint to fetch the user data for the given access token.
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(), // TODO - eventually this won't be only our user, but the current user!
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        // get stuff from the JSON object
                        try {
                            String fbName = response.getJSONObject().get("name").toString();
                            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("name").setValue(fbName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onBackPressed() {
        if (mAuth.getCurrentUser() != null && (facebookLoginBtn.getText().equals("Log out") || facebookLoginBtn.getText().equals("Log ut"))) {
            super.onBackPressed();
        }
    }

    public void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
