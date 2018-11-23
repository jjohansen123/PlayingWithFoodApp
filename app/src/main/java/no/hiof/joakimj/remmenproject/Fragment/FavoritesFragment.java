package no.hiof.joakimj.remmenproject.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import no.hiof.joakimj.remmenproject.Database.Adapter.FavoritedRecyclerAdapter;
import no.hiof.joakimj.remmenproject.Modell.Favorites;
import no.hiof.joakimj.remmenproject.R;



/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {

    //private RecyclerView recyclerView;
    private List<Favorites> favoritesList;

    public final static String FAVORITED_INDEX = "favoritedIndex";
    private static final int DEFAULT_FAVORITED_INDEX = 1;

    private TextView foodIdTextView;
    private TextView foodNameTextView;

    private int foodIndex;

    //private OnFavoritedFragmentInteractionListener listener;

    public FavoritesFragment() {
        //Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout
        favoritesList = Favorites.getData();

        //View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        View fragmentView = inflater.inflate(R.layout.fragment_favorites, container, false);
        foodIdTextView = fragmentView.findViewById(R.id.foodIdTextView);
        foodNameTextView = fragmentView.findViewById(R.id.foodNameTextViewFav);

        foodIndex = savedInstanceState == null? DEFAULT_FAVORITED_INDEX : savedInstanceState.getInt(FAVORITED_INDEX, DEFAULT_FAVORITED_INDEX);
        setDisplayedFavoritedDetail(foodIndex);

        //favoritesList = Favorites.getData();
        //setUpRecyclerView(view);

        return fragmentView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(FAVORITED_INDEX, foodIndex);
    }
/*
    @Override
    public void onStart() {
        super.onStart();
        try {
            listener = (OnFavoritedFragmentInteractionListener).getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        listener = null;
    }

    private void setUpRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.favoritedFragment);
        FavoritedRecyclerAdapter adapter = new FavoritedRecyclerAdapter(getContext(),
                favoritesList, new FavoritedRecyclerAdapter.RecyclerViewClickListener() {

            @Override
            public void onClick(View view, int position) {

            }
        });

    }
*/
    public void setDisplayedFavoritedDetail(int foodIndex) {
        this.foodIndex = foodIndex;
        Log.i("TestingFrag", "index: " + foodIndex);
        favoritesList = Favorites.getData();

        Favorites favorites = favoritesList.get(foodIndex);
        foodIdTextView.setText(favorites.getFood_id());
        Log.i("TestingFrag", "foodid: " + favorites.getFood_id());
        foodNameTextView.setText(favorites.getFood_name());
        Log.i("TestingFrag", "foodName: " + favorites.getFood_name());
    }
/*
    public interface OnFavoritedFragmentInteractionListener {
        void onFavoritedSelected(int id);
    }
*/
}
