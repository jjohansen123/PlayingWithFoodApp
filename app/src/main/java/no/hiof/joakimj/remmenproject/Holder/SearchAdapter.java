package no.hiof.joakimj.remmenproject.Holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import no.hiof.joakimj.remmenproject.Modell.Food;
import no.hiof.joakimj.remmenproject.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    private Context context;
    private List<Food> foodList;

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
        Food food = foodList.get(i);

        searchViewHolder.foodIdTextView.setText(food.getFood_id());
        searchViewHolder.foodNameTextView.setText(food.getFood_name());
        searchViewHolder.allergiesTextView.setText(String.valueOf(food.getAllergies()));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }


    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView foodNameTextView, foodIdTextView, allergiesTextView;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            foodIdTextView = itemView.findViewById(R.id.search_food_id_TV);
            foodNameTextView = itemView.findViewById(R.id.search_food_name_TV);
            allergiesTextView = itemView.findViewById(R.id.search_allergies_TV);
        }
    }
}
