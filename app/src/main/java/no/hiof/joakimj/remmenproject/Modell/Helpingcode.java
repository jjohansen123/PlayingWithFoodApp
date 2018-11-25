package no.hiof.joakimj.remmenproject.Modell;

import android.util.Log;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

public class Helpingcode {
   static public RequestQueue requestQueue;
   static public Comment temp = new Comment();
    static String[] test;
    static public List<Comment> commentData = new ArrayList<Comment>();
   static public String[] getCommentData()
    {
        try {
                        commentData.clear();
                        //JSONObject obj = MainActivity.getJsonData();
            Log.i("reeeee2", "exception " );





                        commentData.add(temp);
                        Log.i("reeeee", "se her: ");

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("reeee", "ObjectRequest" + e);
        }
    return test;
    }

}
