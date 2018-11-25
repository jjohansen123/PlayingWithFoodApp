package no.hiof.joakimj.remmenproject.Modell;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.valueOf;

public class Favorites {
    String food_id;
    String food_name;
    static int length;
    String user_id;



    public Favorites() {
        // Default constructor required for calls to DataSnapshot.getValue(Favorites.class)
    }


    public Favorites(String food_id, String food_name, String user_id) {
        this.food_id = food_id;
        this.food_name = food_name;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

}
