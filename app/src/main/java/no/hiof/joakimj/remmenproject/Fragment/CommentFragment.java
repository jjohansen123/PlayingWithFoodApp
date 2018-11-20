package no.hiof.joakimj.remmenproject.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import no.hiof.joakimj.remmenproject.MainActivity;
import no.hiof.joakimj.remmenproject.Modell.Comment;
import no.hiof.joakimj.remmenproject.Modell.Helpingcode;
import no.hiof.joakimj.remmenproject.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class CommentFragment extends android.support.v4.app.Fragment {





    public final static String COMMENT_INDEX = "index";
    private static final int DEFAULT_COMMENT_INDEX = 0;

    private TextView fNameTextView;
    private TextView commentTextView;
    private RatingBar ratingBar;
    private List<Comment> commentList = new ArrayList<Comment>();
    private int index = 0;
    private Comment commentholder;
    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        //Inflater
        View fragmentView = inflater.inflate(R.layout.fragment_comment, container, false);

        fNameTextView = fragmentView.findViewById(R.id.firstNameTextView);
        commentTextView = fragmentView.findViewById(R.id.commentsTextView);
        ratingBar = fragmentView.findViewById(R.id.rtbProductRating);

        index = savedInstanceState == null? DEFAULT_COMMENT_INDEX : savedInstanceState.getInt(COMMENT_INDEX, DEFAULT_COMMENT_INDEX);

        setCommentOnView(index);
        index++;
        return fragmentView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(COMMENT_INDEX, index);
    }

    public void setCommentOnView (int index) {
      //  commentList = Helpingcode.getCommentData();
        Log.i("reeeeee8", "exception " + commentList);

         RequestQueue requestQueue;
                //static public Comment temp = new Comment();

            try {

                JSONObject obj = MainActivity.getJsonData();
                Log.i("reeeee2", "exception " + obj);

                fNameTextView.setText((obj.getString("comment")));
                commentTextView.setText((obj.getString("fNavn")));

                ratingBar.setRating(Integer.valueOf((obj.getString("rating"))));

                Log.i("reeeee", "se her: ");

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("reeee", "ObjectRequest" + e);
            }
    }

}