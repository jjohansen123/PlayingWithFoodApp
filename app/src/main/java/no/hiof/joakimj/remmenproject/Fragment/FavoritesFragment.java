package no.hiof.joakimj.remmenproject.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import no.hiof.joakimj.remmenproject.Holder.FavoritedRecyclerAdapter;
import no.hiof.joakimj.remmenproject.Modell.Favorites;
import no.hiof.joakimj.remmenproject.R;



/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Favorites> favoritesList;
    private OnFavoritedFragmentInteractionListener listener;

    public FavoritesFragment() {
        //Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        favoritesList = Favorites.getData();
        setUpRecyclerView(view);
        Log.i("Erviher?", "test");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try{
            listener = (OnFavoritedFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement FragmentInteractionListener");
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        listener = null;
    }

    public void setUpRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.favoritedRecyclerView);
        FavoritedRecyclerAdapter adapter = new FavoritedRecyclerAdapter(getContext(), favoritesList, new FavoritedRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getContext(), favoritesList.get(position).getFood_name() + " clicked", Toast.LENGTH_SHORT).show();

                listener.onFavoritedSelected(position);
            }
        });

        recyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3); // (Context context, int spanCount)
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public interface OnFavoritedFragmentInteractionListener {
        void onFavoritedSelected(int id);
    }

}
