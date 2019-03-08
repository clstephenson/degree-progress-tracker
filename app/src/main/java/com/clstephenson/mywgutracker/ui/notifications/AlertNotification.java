package com.clstephenson.mywgutracker.ui.notifications;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.clstephenson.mywgutracker.MyApplication;
import com.clstephenson.mywgutracker.R;

import androidx.core.app.NotificationCompat;

import static android.text.format.DateUtils.FORMAT_ABBREV_ALL;
import static android.text.format.DateUtils.formatDateTime;

/**
 * Helper class for showing and canceling alert
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class AlertNotification {
    public static final int REMINDER_DEFAULT_DAYS_BEFORE = 7;
    private static final String NOTIFICATION_TAG = "Alert";
    public static final int REMINDER_DEFAULT_HOUR_OF_DAY = 8;
    public static final int REMINDER_USE_CURRENT_TIME_OF_DAY = -1;
    /**
     * The unique identifier for this type of notification.
     */
    private static final String TAG = AlertNotification.class.getSimpleName();

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     *
     * @param context
     * @param title heading test to use for the notification
     * @param text main notification text
     * @param delay delay in millis from the current time to schedule the notification
     * @param notificationId identifies the notification in case of updates
     * @param clickIntent Intent to use when the notification is clicked by the user
     */
    public static void scheduleAlert(final Context context, final String title, final String text,
                                     long delay, final int notificationId, PendingIntent clickIntent) {
        Log.d(TAG, "scheduleAlert() called with: title = [" + title + "], text = [" + text + "], delay = [" + delay + "], notificationId = [" + notificationId + "], ");
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_wgu)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setTicker(title)
                .setContentIntent(clickIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, AlertPublisher.class);
        notificationIntent.putExtra(AlertPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(AlertPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = System.currentTimeMillis() + delay;
        Log.d(TAG, "scheduleAlert: scheduling alert for " + formatDateTime(context, futureInMillis, FORMAT_ABBREV_ALL));
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #scheduleAlert(Context, String, String, long, int, PendingIntent)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
