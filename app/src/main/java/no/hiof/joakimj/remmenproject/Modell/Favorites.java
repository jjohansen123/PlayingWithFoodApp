package no.hiof.joakimj.remmenproject.Modell;

import android.graphics.fonts.FontVariationAxis;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Favorites {
    int food_id;
    String food_name;
    int user_id;

    public Favorites() {

    }

    public Favorites(int food_id, int user_id, String food_name) {
        this.food_id = food_id;
        this.food_name = food_name;
    }




    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public static List<Favorites> getData() {
        List<Favorites> data = new ArrayList<>();

        String[] foodId = {"2",
        "3",
        "5"};

        String[] foodName = {"Two",
                "Three",
                "Five"};

        for(int i = 0; i < foodId.length; i++) {
            Favorites current = new Favorites();
            current.setFood_id(i);
            current.setFood_name(foodName[i]);
            Log.i("TestingFrag", "" + current);
            data.add(current);
            Log.i("TestingFrag", "" + data.get(i));
        }
        return data;
    }
}
