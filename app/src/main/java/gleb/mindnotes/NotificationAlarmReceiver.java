package gleb.mindnotes;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final PendingResult result = goAsync();
        Toast.makeText(context, "!!!", Toast.LENGTH_SHORT).show();
        Log.i("NotificAlarmReceiver", "in onReceive");
        Intent alarmIntent = new Intent(context, AlarmService.class);
        context.startService(alarmIntent);
        result.finish();
    }
}
