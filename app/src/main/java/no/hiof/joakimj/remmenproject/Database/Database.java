package no.hiof.joakimj.remmenproject.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "Kissi.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME,null, DB_VER);
    }

    public void addToFavorites(Integer foodId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(foodId) Values('%s');", foodId);
        db.execSQL(query);
    }

    public void removeFromFavorites(Integer foodId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE foodId='%s';", foodId);
        db.execSQL(query);
    }

    public boolean isFavorite(Integer foodId)  {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE foodId='%s'", foodId);
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

}
