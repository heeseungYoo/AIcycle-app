package com.example.project3_test1.PersonFragments;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
    private KeyguardManager km = null;
    private KeyguardManager.KeyguardLock keyLock = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(intent.ACTION_SCREEN_OFF)) {
            if (km == null)
                km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

            if(keyLock == null)
                keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);

            disableKeyguard();

            Intent i = new Intent(context, LockScreenActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {
            Log.e("onReceive", "SCREEN_OFF");
        }
        else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Log.e("onReceive", "BOOT_COMPLETED");
        }

    }

    public void reenableKeyguard() {
        keyLock.reenableKeyguard();
    }

    public void disableKeyguard() {
        keyLock.disableKeyguard();
    }
}
