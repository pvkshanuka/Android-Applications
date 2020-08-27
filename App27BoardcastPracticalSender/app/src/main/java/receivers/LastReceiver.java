package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class LastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int resultCode = getResultCode();
        String resultData = getResultData();
        Bundle bundle = getResultExtras(true);

        Toast.makeText(context, "LastReceiver.! "+resultCode+"-"+resultData+" Bundle-"+bundle.get("x")+" intent-"+intent.getStringExtra("y"), Toast.LENGTH_LONG).show();

    }
}
