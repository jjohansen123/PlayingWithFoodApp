package no.hiof.joakimj.remmenproject.Modell;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.valueOf;

public class Favorites {
    String food_id;
    String food_name;
    static int length;
    int user_id;

    public Favorites() {
        // Default constructor required for calls to DataSnapshot.getValue(Favorites.class)
    }


    public Favorites(String food_id, String food_name) {
        this.food_id = food_id;
        this.food_name = food_name;
        this.user_id = valueOf(user_id);
    }

    public static int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
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

        Integer[] foodId = {2,
        3,
        5};

        String[] foodName = {"Two",
                "Three",
                "Five"};

        length = foodId.length;

        for(int i = 0; i < foodId.length; i++) {
            Favorites current = new Favorites();
            current.setFood_id(String.valueOf(foodId[i]));
            current.setFood_name(foodName[i]);
            data.add(current);
        }
        return data;
    }
}
