    package no.hiof.joakimj.remmenproject.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import no.hiof.joakimj.remmenproject.Modell.Comment;
import no.hiof.joakimj.remmenproject.R;

    /**
     * A simple {@link Fragment} subclass.
     */
    public class CommentFragment extends android.support.v4.app.Fragment {

        public final static String COMMENT_INDEX = "index";
        private static final int DEFAULT_COMMENT_INDEX = 1;

        private TextView fNameTextView;
        private TextView commentTextView;
        private RatingBar ratingBar;
        private List<Comment> commentList;
        private int index;

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

            return fragmentView;

        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putInt(COMMENT_INDEX, index);
        }

        public void setCommentOnView (int index) {
            commentList = Comment.getCommentData();

            Comment comment = commentList.get(index);

            fNameTextView.setText(comment.getfName());
            commentTextView.setText(comment.getComment());
            ratingBar.setRating(comment.getRating());
        }

    }
