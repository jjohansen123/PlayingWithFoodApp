package no.hiof.joakimj.remmenproject;

import android.support.annotation.NonNull;
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
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import no.hiof.joakimj.remmenproject.Fragment.FavoritesFragment;
import no.hiof.joakimj.remmenproject.Holder.MyRecyclerViewHolder;
import no.hiof.joakimj.remmenproject.Modell.Favorites;

public class FavoritesActivity extends AppCompatActivity{

    RecyclerView recyclerView;

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

        btnPost = findViewById(R.id.btn_update);

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

        Log.i("TAG test", "er jeg her?! " + options.toString());
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
                        Log.i("TAG test", "er jeg der?!");
                        final String favoriteId = getRef(position).getKey();
                        favDatabaseReference.child(favoriteId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    
                                    String favId = dataSnapshot.getValue().toString();
                                    String favName = dataSnapshot.getValue().toString();
                                    holder.food_id_textView.setText(.getFood_id());
                                    Log.i("TAG", "food id: " + favId);
                                    holder.food_name_textView.setText(favName);
                                    Log.i("TAG", "food name: " + favName);
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


    public void updatePost(View view) {
        displayFavorites();
    }
}
