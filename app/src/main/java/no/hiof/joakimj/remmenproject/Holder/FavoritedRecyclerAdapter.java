package no.hiof.joakimj.remmenproject.Holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import no.hiof.joakimj.remmenproject.Modell.Favorites;
import no.hiof.joakimj.remmenproject.R;

public class FavoritedRecyclerAdapter extends RecyclerView.Adapter<FavoritedRecyclerAdapter.FavoritedViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Favorites> favoritesList;
    private RecyclerViewClickListener clickListener;

    public FavoritedRecyclerAdapter(Context context, List<Favorites> favoritesList, RecyclerViewClickListener recyclerViewClickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.favoritesList = favoritesList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FavoritedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = layoutInflater.inflate(R.layout.favorites_list_item, viewGroup, false);
        return new FavoritedViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritedRecyclerAdapter.FavoritedViewHolder favoritedViewHolder, int i) {
        Favorites currentFavorite = favoritesList.get(i);
        favoritedViewHolder.setData(currentFavorite);
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    class FavoritedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView foodIdTextView;
        TextView foodNameTextView;

        RecyclerViewClickListener onFavoritedClickListener;

        public FavoritedViewHolder(@NonNull View itemView, RecyclerViewClickListener onFavoritedClickListener) {
            super(itemView);

            this.onFavoritedClickListener = onFavoritedClickListener;

            foodIdTextView = itemView.findViewById(R.id.foodIdTextView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextViewFav);

            itemView.setOnClickListener(this);

        }

        public void setData(Favorites favorites) {
            foodIdTextView.setText(favorites.getFood_id());
            foodNameTextView.setText(favorites.getFood_name());
        }

        @Override
        public void onClick(View v) {
            onFavoritedClickListener.onClick(itemView, getAdapterPosition());
        }


    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }
}
