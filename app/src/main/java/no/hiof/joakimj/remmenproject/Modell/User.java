package no.hiof.joakimj.remmenproject.Modell;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import no.hiof.joakimj.remmenproject.Holder.SearchAdapter;
import no.hiof.joakimj.remmenproject.MainActivity;
import no.hiof.joakimj.remmenproject.SearchActivity;

public class User {
    int id;
    String fNavn;
    String eNavn;
    String google_id;
    int allergi;

    Context context;

    List<User> userList;

    public User() {

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
