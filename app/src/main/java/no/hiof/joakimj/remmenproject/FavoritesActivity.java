package no.hiof.joakimj.remmenproject;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import no.hiof.joakimj.remmenproject.Fragment.FavoritesFragment;
import no.hiof.joakimj.remmenproject.Holder.MyRecyclerViewHolder;
import no.hiof.joakimj.remmenproject.Modell.Favorites;

public class FavoritesActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    int favCounter = 0;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference favDatabaseReference;
    Favorites favorites;

    Button btnPost;



    FirebaseRecyclerOptions<Favorites> options;
    FirebaseRecyclerAdapter<Favorites,MyRecyclerViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        firebaseDatabase = FirebaseDatabase.getInstance();
        favDatabaseReference = firebaseDatabase.getReference("favorites_foods");

        recyclerView = findViewById(R.id.favoritedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        favDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                displayFavorites();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        displayFavorites();


    }

    @Override
    protected void onStop() {
        if(adapter != null) {
            adapter.stopListening();
        }
        super.onStop();
    }

    private void displayFavorites() {
        options =
                new FirebaseRecyclerOptions.Builder<Favorites>()
                        .setQuery(favDatabaseReference, Favorites.class)
                        .build();

        adapter =
                new FirebaseRecyclerAdapter<Favorites, MyRecyclerViewHolder>(options) {
                    @NonNull
                    @Override
                    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favorites_list_item, viewGroup, false);
                        Log.i("Tag test", "er det noe som skjer?");
                        return new MyRecyclerViewHolder(itemView);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final MyRecyclerViewHolder holder, int position, @NonNull Favorites model) {

                        final String key= getRef(position).getKey();


                        favDatabaseReference.child(key).child("4").addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    Favorites favorites = dataSnapshot.getValue(Favorites.class);
                                    Log.i("tag", "dataSnapshot " + favorites.getFood_id());

                                    holder.food_id_textView.setText(favorites.getFood_id());
                                    holder.food_name_textView.setText(favorites.getFood_name());

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }


                };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}
