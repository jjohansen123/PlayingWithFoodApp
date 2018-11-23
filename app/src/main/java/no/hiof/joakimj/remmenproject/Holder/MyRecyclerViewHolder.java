package no.hiof.joakimj.remmenproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.BreakIterator;

import no.hiof.joakimj.remmenproject.R;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView food_name_textView;
    public TextView food_id_textView;

    public MyRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        food_name_textView = (TextView) itemView.findViewById(R.id.foodNameTextViewFav);
        food_id_textView = (TextView) itemView.findViewById(R.id.foodIdTextView);

    }
}
