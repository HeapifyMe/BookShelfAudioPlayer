package pam.bookshelf;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

public class MyApplicationClass extends Application {
    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();

        // TODO Put your application initialization code here.
    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            new SharedPreferencesClass(context).setPosition(1);
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            try{
//                new SharedPreferencesClass(context).setPosition(1);
//
//            }catch (Exception e){}
//            Toast.makeText(MyApplicationClass.this, "portrait", Toast.LENGTH_SHORT).show();
//        }
//    }
}