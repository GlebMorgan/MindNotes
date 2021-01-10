package gleb.mindnotes;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class AlarmService extends IntentService {

    public AlarmService() {
        super(AlarmService.class.getName());
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("AlarmService", "in onHandleEvent");

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().
                getSystemService(Context.NOTIFICATION_SERVICE);
        Intent activityIntent = new Intent(getApplicationContext(), NotesListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent notificationIntent = PendingIntent
                .getActivity(getApplicationContext(),
                             0,
                             activityIntent,
                             PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_book_open_page_variant_black_48dp)
                .setContentIntent(notificationIntent)
                .setContentText("this is my notification")
                .setContentTitle("my notification")
                .setSound(alarmSound)
                .setAutoCancel(true);
        notificationManager.notify(0, builder.build());
    }
}
