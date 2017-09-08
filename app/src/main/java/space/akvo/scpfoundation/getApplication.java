package space.akvo.scpfoundation;

import android.app.Application;

/**
 * Created by akvo on 2017/9/6.
 */

public class getApplication extends Application {
    private static getApplication instance;
    public static getApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
    }
}
