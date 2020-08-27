package CustomClasses;

import android.os.Handler;
import android.os.Looper;

public class RunOnUIThread {

    public static void runOnUiThread(Runnable runnable){
        final Handler UIHandler = new Handler(Looper.getMainLooper());
        UIHandler .post(runnable);
    }

}
