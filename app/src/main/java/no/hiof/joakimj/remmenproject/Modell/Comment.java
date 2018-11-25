package no.hiof.joakimj.remmenproject.Modell;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import no.hiof.joakimj.remmenproject.MainActivity;

public class Comment {
    public String comment;
    public String fName;
    public int rating;

    public Comment() {
        // Default constructor required for calls
    }

    public Comment(String comment, String fName, int rating) {
        this.comment = comment;
        this.fName = fName;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
}
