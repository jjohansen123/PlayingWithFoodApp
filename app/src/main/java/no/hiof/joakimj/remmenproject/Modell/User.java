package no.hiof.joakimj.remmenproject.Modell;


public class User {
    int id;
    String fNavn;
    String eNavn;
    String google_id;
    int allergi;


    public User() {

    }

    public User(int id, String fNavn, String eNavn, String google_id, int allergi) {
        this.id = id;
        this.fNavn = fNavn;
        this.eNavn = eNavn;
        this.google_id = google_id;
        this.allergi = allergi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfNavn() {
        return fNavn;
    }

    public void setfNavn(String fNavn) {
        this.fNavn = fNavn;
    }

    public String geteNavn() {
        return eNavn;
    }

    public void seteNavn(String eNavn) {
        this.eNavn = eNavn;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
    }

    public int getAllergi() {
        return allergi;
    }

    public void setAllergi(int allergi) {
        this.allergi = allergi;
    }



}
