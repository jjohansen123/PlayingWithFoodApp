package no.hiof.joakimj.remmenproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;


public class MainActivity extends AppCompatActivity {

    //boolean firstImage = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);
        Button logoutBtn = findViewById(R.id.logOutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // All your networking logic
                // should be here

                // https://code.tutsplus.com/tutorials/android-from-scratch-using-rest-apis--cms-27117
            }
        });



        imageView.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "Swipe Bottom", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "Swipe Left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "Swipe Right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "Swipe Top", Toast.LENGTH_SHORT).show();
            }
        });


    }




}
