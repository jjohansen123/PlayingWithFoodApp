package no.hiof.joakimj.remmenproject.Holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import no.hiof.joakimj.remmenproject.Modell.Comment;
import no.hiof.joakimj.remmenproject.Modell.Food;
import no.hiof.joakimj.remmenproject.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_list_item,null);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int i) {
        Comment comment = commentList.get(i);

        commentViewHolder.firstname_text_view.setText(comment.getfName());
        commentViewHolder.comment_text_view.setText(comment.getComment());
        commentViewHolder.rtb_comment.setRating(comment.getRating());
        Log.i("TAG", "test... " + comment.getComment());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView firstname_text_view, comment_text_view;
        RatingBar rtb_comment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            firstname_text_view = itemView.findViewById(R.id.comment_first_name_text_view);
            comment_text_view = itemView.findViewById(R.id.comment_text_view);
            rtb_comment = itemView.findViewById(R.id.rtb_comment_rating);
        }
    }
}


