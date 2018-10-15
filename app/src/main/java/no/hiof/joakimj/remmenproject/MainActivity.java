package no.hiof.joakimj.remmenproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);

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