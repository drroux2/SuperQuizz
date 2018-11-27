package fr.diginamic.formation.super_quizz.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkChangeReceiver  extends BroadcastReceiver {

    public static final String NETWORK_CHANGE_ACTION = "fr.diginamic.formation.super_quizz.broadcast.NetworkChangeReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e("BROADCAST_RECEIVER", String.valueOf(isOnline(context)));
        if (isOnline(context))
        {
            sendInternalBroadcast(context, "Internet Connected", true);
        }
        else
        {
            sendInternalBroadcast(context, "Internet Not Connected", false);
        }
    }

    /**
     * This method is responsible to send status by internal broadcast
     *
     * @param context
     * @param status
     * */
    private void sendInternalBroadcast(Context context, String status, boolean isOnline)
    {
        try
        {
            Intent intent = new Intent();
            intent.putExtra("status", status);
            intent.putExtra("isOnline", isOnline);
            intent.setAction(NETWORK_CHANGE_ACTION);
            context.sendBroadcast(intent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Check if network available or not
     *
     * @param context
     * */
    public boolean isOnline(Context context)
    {
        boolean isOnline = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isOnline = (netInfo != null && netInfo.isConnected());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return isOnline;
    }
}
