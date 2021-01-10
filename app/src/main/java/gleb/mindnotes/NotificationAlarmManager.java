package gleb.mindnotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

class NotificationAlarmManager {

    private static final String TAG = NotificationAlarmManager.class.getSimpleName();
    private Context context;

    public NotificationAlarmManager(Context context) {
        this.context = context;
    }

    void scheduleAlarm(int frequency, int repeatCount) {
        Log.d("NotificationAlarmMgr", "in scheduleAlarm");
        final int multiplier = frequency + 2;
        long millis = 1000;
        for (int repeat = 0; repeat < repeatCount; repeat++) {
            millis *= multiplier;
            Log.d("NotificationAlarmMgr", "in loop");
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent alarmIntent = PendingIntent
                    .getBroadcast(context, repeat,
                            new Intent(context.getApplicationContext(), NotificationAlarmReceiver.class),
                            PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + millis, alarmIntent);
        }
    }
    void cancelAlarm(int repeatCount) {
        for (int repeat = 0; repeat < repeatCount; repeat++) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent alarmIntent = PendingIntent
                    .getBroadcast(context, repeat,
                            new Intent(context.getApplicationContext(), NotificationAlarmReceiver.class),
                            PendingIntent.FLAG_UPDATE_CURRENT);
            if (alarmManager != null) {
                alarmManager.cancel(alarmIntent);
            }
        }
    }
}









