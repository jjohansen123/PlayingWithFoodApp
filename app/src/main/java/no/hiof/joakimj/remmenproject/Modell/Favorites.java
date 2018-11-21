package no.hiof.joakimj.remmenproject.Modell;

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
}
