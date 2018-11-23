package no.hiof.joakimj.remmenproject;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import no.hiof.joakimj.remmenproject.Fragment.FavoritesFragment;
import no.hiof.joakimj.remmenproject.Holder.MyRecyclerViewHolder;
import no.hiof.joakimj.remmenproject.Modell.Favorites;

public class FavoritesActivity extends AppCompatActivity implements FavoritesFragment.OnFavoritedFragmentInteractionListener{

    public static final String FAVORITED_ID_KEY = "food_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        int id = getIntent().getIntExtra(FAVORITED_ID_KEY, 1);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FavoritesFragment favoritesFragment = (FavoritesFragment) fragmentManager.findFragmentById(R.id.favoritedFragment);

        //favoritesFragment.setDisplayedFavoritedDetail(id);
    }

    @Override
    public void onFavoritedSelected(int id) {

    }
}
