package no.hiof.joakimj.remmenproject.Modell;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import no.hiof.joakimj.remmenproject.MainActivity;

public class User {
    int id;
    String fNavn;
    String eNavn;
    String google_id;
    int allergi;

    RequestQueue requestQueue;

    public User() {

    }
    void getUser(String google_id) {
        try {
            final JSONObject object = new JSONObject();

            String userUrl = "http://81.166.82.90/google_id=" + google_id;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, userUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //opening the first object in Json
                        JSONObject obj = response.getJSONObject("object");

                        id = (obj.getInt("user_id"));
                        allergi = (obj.getInt("allergier"));
                        eNavn = (obj.getString("eNavn"));
                        fNavn = (obj.getString("fNavn"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("TAG", "Registrerer bruker");
                        //her registrerer man brukere
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("TAG", "VolleyError in User class: " + error);
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "ObjectRequest in User class: " + e);
        }
    }

    public User(int id, String fNavn, String eNavn, String google_id, int allergi) {
        this.id = id;
        this.fNavn = fNavn;
        this.eNavn = eNavn;
        this.google_id = google_id;
        this.allergi = allergi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfNavn() {
        return fNavn;
    }

    public void setfNavn(String fNavn) {
        this.fNavn = fNavn;
    }

    public String geteNavn() {
        return eNavn;
    }

    public void seteNavn(String eNavn) {
        this.eNavn = eNavn;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
    }

    public int getAllergi() {
        return allergi;
    }

    public void setAllergi(int allergi) {
        this.allergi = allergi;
    }
}
