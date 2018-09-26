package no.hiof.joakimj.remmenproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SomethingNew extends AppCompatActivity {

    private String string = "Hei";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_something_new);
    }
}
