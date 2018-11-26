package no.hiof.joakimj.remmenproject.Holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import no.hiof.joakimj.remmenproject.MainActivity;
import no.hiof.joakimj.remmenproject.Modell.Food;
import no.hiof.joakimj.remmenproject.R;
import no.hiof.joakimj.remmenproject.SearchActivity;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    private Context context;
    private List<Food> foodList;
    private ItemClickListener clickListener;

    public static String tester;

    Food food;

    public SearchAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_list_item,null);

        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        food = foodList.get(i);

        searchViewHolder.foodIdTextView.setText(food.getFood_id());
        searchViewHolder.foodNameTextView.setText(food.getFood_name());
        searchViewHolder.allergiesTextView.setText(String.valueOf(food.getAllergies()));
    }

    @Override
    public int getItemCount() {
        return foodList == null ? 0 : foodList.size();
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView foodNameTextView, foodIdTextView, allergiesTextView;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            foodIdTextView = itemView.findViewById(R.id.search_food_id_TV);
            foodNameTextView = itemView.findViewById(R.id.search_food_name_TV);
            allergiesTextView = itemView.findViewById(R.id.search_allergies_TV);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //clickListener.onClick(v,getPosition());
            Toast.makeText(context, "klikking=!" + getAdapterPosition() + " " + food.getFood_name(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(v.getContext(), MainActivity.class);
            intent.putExtra("food_id", food.getFood_id());
            tester = food.getFood_id();
            ((Activity)context).setResult(Activity.RESULT_OK, intent);
            ((Activity) context).finish();

            if(clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }


    public interface ItemClickListener {
        void onClick(View view, int position);
    }
}
