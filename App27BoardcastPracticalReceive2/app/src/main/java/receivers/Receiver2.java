package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Receiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int resultCode = getResultCode();
        String resultData = getResultData();
        Bundle bundle = getResultExtras(true);

        Toast.makeText(context, "Receiver 2 "+resultCode+"-"+resultData+" Bundle-"+bundle.get("x")+" intent-"+intent.getStringExtra("y"), Toast.LENGTH_LONG).show();

        setResultCode(3);

        setResultData("c");

        bundle.putString("x","300");

        intent.putExtra("y","300");


    }
}
