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
    private int length;
    private int idmat;
    private String comment;
    private String fName;
    private int rating;

    static RequestQueue requestQueue;


    public int getIdmat() {
        return idmat;
    }

    public void setIdmat(int idmat) {
        this.idmat = idmat;
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public static List<Comment> getCommentData() {
        final List<Comment> commentData = new ArrayList<>();

        /*
        String url = "http://81.166.82.90/comment.php?food_id=0";

        try {

            final JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.i("TEST", "Kommer jeg hit?");
                        //opening the first object in Json
                        JSONObject obj = response.getJSONObject("object");
                        //int tempfoodid = (obj.getInt("food_id"));
                        //int temprating = (obj.getInt("rating"));
                        //String tempcomment = (obj.getString("comment"));
                        //String tempnavn = (obj.getString("fNavn"));

                        int tempfoodid = 0;
                        int temprating = 3;
                        String tempcomment = "Dummy";
                        String tempnavn = "Dummy";

                        Comment temp = new Comment(tempfoodid, tempcomment, tempnavn,temprating);
                        commentData.add(temp);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("TAG", "JSONExeption" + e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("TEST", "Kommer jeg?" + error);
                }
            });
            Log.i("TEST", "Kommer jeg ditt?");
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "ObjectRequest" + e);
        }
        */

        int[] foodId = {
                0,
                0
        };

        int[] ratingen = {
                2,
                3
        };

        String[] comment = {
                "Dummy2",
                "Dummy3"
        };

        String[] navn = {
                "Dummynavn2",
                "Dummynavn3"
        };


        for (int i = 0; i < comment.length; i++) {
            Comment current = new Comment();
            current.setIdmat(foodId[i]);
            current.setfName(navn[i]);
            current.setComment(comment[i]);
            current.setRating(ratingen[i]);
            commentData.add(current);

        }

        Log.i("CURRENT", "current: " + getCommentData());
        return commentData;
    }

}
