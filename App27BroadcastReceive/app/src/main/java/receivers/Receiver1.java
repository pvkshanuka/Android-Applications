package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Receiver1 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String name = intent.getStringExtra("name");
        Toast.makeText(context, "Receive = "+name, Toast.LENGTH_LONG).show();

    }
}
