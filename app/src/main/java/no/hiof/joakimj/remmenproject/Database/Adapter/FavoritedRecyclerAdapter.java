package no.hiof.joakimj.remmenproject.Database.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import no.hiof.joakimj.remmenproject.Modell.Favorites;
import no.hiof.joakimj.remmenproject.R;

public class FavoritedRecyclerAdapter extends RecyclerView.Adapter<FavoritedRecyclerAdapter.FavoritedViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Favorites> favoritesList;
    private RecyclerViewClickListener clickListener;

    public FavoritedRecyclerAdapter(Context context, List<Favorites> favoritesList, RecyclerViewClickListener clickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.favoritesList = favoritesList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FavoritedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView = layoutInflater.inflate(R.layout.fragment_favorites, viewGroup,false);
        return new FavoritedViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritedViewHolder favoritedViewHolder, int position) {
        // Retrieve the data for that position
        Favorites currentFavorite = favoritesList.get(position);
        // Add the data to the view
        favoritedViewHolder.setData(currentFavorite);
    }


    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    class FavoritedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView posterImageView;
        RecyclerViewClickListener onFavoritedClickListener;

        public FavoritedViewHolder(View itemView, RecyclerViewClickListener onFavoritedClickListener) {
            super(itemView);
            this.onFavoritedClickListener = onFavoritedClickListener;

            itemView.setOnClickListener(this);
        }

        public void setData(Favorites favorites) {
            //posterImageView.setImageResource(movie.getImageId());


        }

        @Override
        public void onClick(View view) {
            onFavoritedClickListener.onClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

}