package edu.uw.uidemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;

/**
 * Created by iguest on 1/26/16.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("RECEIVER", intent.toString());

        SmsMessage[] messages = getMessagesFromIntent(intent);

        for(SmsMessage msg : messages){
            Log.v("RECEIVER", msg.getOriginatingAddress() + ": "+msg.getMessageBody());
            Toast t = Toast.makeText(context, msg.getOriginatingAddress() + ": "+msg.getMessageBody(), Toast.LENGTH_LONG);
            t.show();
        }

        //do other work

    }
}
