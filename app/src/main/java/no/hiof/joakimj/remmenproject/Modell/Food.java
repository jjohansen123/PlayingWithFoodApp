package no.hiof.joakimj.remmenproject.Modell;

public class Food {
    private String food_id;
    private String food_name;
    private String allergies;

    public Food() {
        // Default constructor required for calls
    }

    public Food(String food_id, String food_name, String allergies) {
        this.food_id = food_id;
        this.food_name = food_name;
        this.allergies = allergies;
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

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
}
