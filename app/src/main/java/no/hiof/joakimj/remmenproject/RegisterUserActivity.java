package no.hiof.joakimj.remmenproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterUserActivity extends AppCompatActivity {

    Integer allergiValue = 0;

    private FirebaseAuth firebaseAuth;
    private String firstname, surname, userUrl, fullname;
    private EditText firstnameText, surnameText;
    private CheckBox cB1,cB2, cB3, cB4, cB5, cB6, cB7, cB8, cB9, cB10, cB11, cB12, cB13, cB14, cB15;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        fullname = MainActivity.nameUid;
        String[] parts = fullname.split("\\s+");
        Log.i("Registrer", "Fullname: " + fullname + " " + parts.length);

        if(parts.length == 1) {
            firstname = parts[0];
            surname = "Enter surname";
        } else if(parts.length==2) {
            firstname = parts[0];
            surname = parts[1];
            Log.i("Registrer", "firstname: " + firstname + " lastname: " + surname);
        } else if (parts.length==3) {
            firstname = parts[0];
            String middlename = parts[1];
            surname = parts[2];
            Log.i("Registrer", "firstname: " + firstname + " lastname: " + surname);
        }

        firstnameText = findViewById(R.id.editFName);
        surnameText = findViewById(R.id.editSurname);

        firstnameText.setText(firstname);
        surnameText.setText(surname);




        cB1 = findViewById(R.id.checkBox1);
        cB2 = findViewById(R.id.checkBox2);
        cB3 = findViewById(R.id.checkBox3);
        cB4 = findViewById(R.id.checkBox4);
        cB5 = findViewById(R.id.checkBox5);
        cB6 = findViewById(R.id.checkBox6);
        cB7 = findViewById(R.id.checkBox7);
        cB8 = findViewById(R.id.checkBox8);
        cB9 = findViewById(R.id.checkBox9);
        cB10 = findViewById(R.id.checkBox10);
        cB11 = findViewById(R.id.checkBox11);
        cB12 = findViewById(R.id.checkBox12);
        cB13 = findViewById(R.id.checkBox13);
        cB14 = findViewById(R.id.checkBox14);
        cB15 = findViewById(R.id.checkBox15);
    }



    public void submitResult(View view) {

        if(cB1.isChecked()) {
            allergiValue += 1;
        }
        if(cB2.isChecked()) {
            allergiValue += 2;
        }
        if(cB3.isChecked()) {
            allergiValue += 4;
        }
        if(cB4.isChecked()) {
            allergiValue += 8;
        }
        if(cB5.isChecked()) {
            allergiValue += 16;
        }
        if(cB6.isChecked()) {
            allergiValue += 32;
        }
        if(cB7.isChecked()) {
            allergiValue += 64;
        }
        if(cB8.isChecked()) {
            allergiValue += 128;
        }
        if(cB9.isChecked()) {
            allergiValue += 256;
        }
        if(cB10.isChecked()) {
            allergiValue += 512;
        }
        if(cB11.isChecked()) {
            allergiValue += 1024;
        }
        if(cB12.isChecked()) {
            allergiValue += 2048;
        }
        if(cB13.isChecked()) {
            allergiValue += 4096;
        }
        if(cB14.isChecked()) {
            allergiValue += 8192;
        }
        if(cB15.isChecked()) {
            allergiValue += 16384;
        }

        firstname = firstnameText.getText().toString();
        surname = surnameText.getText().toString();

        userUrl = "http://81.166.82.90/register_user.php?allergier=" + allergiValue
                + "&fNavn=" + firstname
                + "&eNavn=" + surname
                + "&google_id=" + MainActivity.userUid;

         new SendUserToDatabase().execute();
     }

     public class SendUserToDatabase extends AsyncTask<String, String, String> {
         @Override
         protected String doInBackground(String... strings) {
             try {
                 URL url = new URL(userUrl);
                 HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                 httpURLConnection.setRequestMethod("GET");
                 httpURLConnection.connect();

                 BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                 String value = reader.readLine();
                 Log.i("TAG", "Result is: " + value);

             } catch (Exception e){
                 Log.i("TAG", "Something went wrong in sendRating: " + e);
             }
             return null;

         }
     }


}
