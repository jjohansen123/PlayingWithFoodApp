package no.hiof.joakimj.remmenproject.Modell;

import android.text.Layout;
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

import no.hiof.joakimj.remmenproject.Fragment.CommentFragment;
import no.hiof.joakimj.remmenproject.MainActivity;
import no.hiof.joakimj.remmenproject.Modell.Comment;
public class Helpingcode {
   static public RequestQueue requestQueue;
   static public Comment temp = new Comment();
    static String[] test;
    static public List<Comment> commentData = new ArrayList<Comment>();
   static public String[] getCommentData()
    {
        try {
                        commentData.clear();
                        JSONObject obj = MainActivity.getJsonData();
            Log.i("reeeee2", "exception " + obj);



            test[0] =  (obj.getString("comment"));
            test[1] =  (obj.getString("fNavn"));
            test[2] =  (obj.getString("rating"));

                        commentData.add(temp);
                        Log.i("reeeee", "se her: ");

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("reeee", "ObjectRequest" + e);
        }
    return test;
    }

}
