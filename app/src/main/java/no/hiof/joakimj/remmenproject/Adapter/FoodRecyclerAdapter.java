package no.hiof.joakimj.remmenproject.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;


import java.util.List;

import no.hiof.joakimj.remmenproject.Modell.Comment;
import no.hiof.joakimj.remmenproject.R;

public class FoodRecyclerAdapter {
    private LayoutInflater layoutInflater;
    private List<Comment> commentList;
    private RecyclerViewClickListener clickListener;

    public FoodRecyclerAdapter(LayoutInflater layoutInflater, List<Comment> commentList, RecyclerViewClickListener clickListener) {
        this.layoutInflater = layoutInflater;
        this.commentList = commentList;
        this.clickListener = clickListener;
    }

    @NonNull
    //@Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.comment_list_item, parent, false);

        return new CommentViewHolder(itemView, clickListener);
    }

    //@Override
    public void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int position) {
        //retrive data from the position
        Comment currentComment = commentList.get(position);
        //Add new data
        commentViewHolder.setData(currentComment);
    }

    //@Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        RatingBar ratingBar;
        TextView fNameTextView;
        TextView commentTextView;

        RecyclerViewClickListener onCommentClickListener;

        public CommentViewHolder(View itemView, RecyclerViewClickListener onCommentClickListener) {
            super(itemView);

            this.onCommentClickListener = onCommentClickListener;

            ratingBar = itemView.findViewById(R.id.rtbProductRating);
            fNameTextView = itemView.findViewById(R.id.firstNameTextView);
            commentTextView = itemView.findViewById(R.id.commentsTextView);

            itemView.setOnClickListener(this);
        }

        public void setData(Comment comment) {
            ratingBar.setRating(comment.getRating());
            fNameTextView.setText(comment.getfName());
            commentTextView.setText(comment.getComment());
        }

        @Override
        public void onClick(View view) {
            onCommentClickListener.onClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

}