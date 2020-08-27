package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Receiver1 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int resultCode = getResultCode();
        String resultData = getResultData();
        Bundle bundle = getResultExtras(true);

        Toast.makeText(context, "Receiver 1 "+resultCode+"-"+resultData+" Bundle-"+bundle.get("x")+" intent-"+intent.getStringExtra("y"), Toast.LENGTH_LONG).show();

        setResultCode(2);

        setResultData("b");

        bundle.putString("x","200");

        intent.putExtra("y","200");

        //meken kelinma anthima Receiver ekata yanawa
        abortBroadcast();

    }
}
