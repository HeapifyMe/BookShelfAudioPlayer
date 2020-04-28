package pam.bookshelf;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesClass {
    Context context;

    public SharedPreferencesClass(Context context) {
        this.context = context;
    }

    public void setPosition(int pos) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("pos", pos);
        editor.commit();
    }

    public int getPosition() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Data", MODE_PRIVATE);
        return sharedPreferences.getInt("pos", 0);
    }


    public void setSearch(String search) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Data", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("search", search);
        editor.commit();
    }

    public String getSearch() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Data", MODE_PRIVATE);
        return sharedPreferences.getString("search", "");
    }


}