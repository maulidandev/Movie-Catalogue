package com.dicoding.picodiploma.moviecatalogueapi.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.dicoding.picodiploma.moviecatalogueapi.BuildConfig;
import com.dicoding.picodiploma.moviecatalogueapi.R;
import com.dicoding.picodiploma.moviecatalogueapi.api.APIService;
import com.dicoding.picodiploma.moviecatalogueapi.api.Constants;
import com.dicoding.picodiploma.moviecatalogueapi.model.APIResponse;
import com.dicoding.picodiploma.moviecatalogueapi.model.Movie;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TYPE_RELEASE_REMINDER = "ReleaseReminder";
    public static final String TYPE_DAILY_REMINDER = "DailyReminder";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    // Siapkan 2 id untuk 2 macam alarm, onetime dna repeating
    private final int ID_RELEASE_REMINDER = 100;
    private final int ID_DAILY_REMINDER = 101;

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel_01";
    private static final CharSequence CHANNEL_NAME = "dicoding channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        if (type.equalsIgnoreCase(TYPE_DAILY_REMINDER)){
            showNotification(context, message, NOTIFICATION_ID);
        }else{
            getReleaseMovie(context);
        }
    }

    private void getReleaseMovie(final Context context) {
        Calendar calendar = Calendar.getInstance();
        String date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        Retrofit retrofit = Constants.getRetrofit();
        APIService service = retrofit.create(APIService.class);
        Call call = service.releaseMovie(BuildConfig.API_KEY, date, date);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    ArrayList<Movie> movies = response.body().getResults();

                    for (int i = 0; i<movies.size(); i++){
                        String message = movies.get(i).getTitle() + context.getResources().getString(R.string.message_release_reminder);

                        showNotification(context, message, (int) movies.get(i).getId());
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showNotification(Context context, String message, int notificationID){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground))
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true);

        /*
            Untuk android Oreo ke atas perlu menambahkan notification channel
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            mBuilder.setChannelId(CHANNEL_ID);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = mBuilder.build();

        if (mNotificationManager != null) {
            mNotificationManager.notify(notificationID, notification);
        }

    }

    public void setReminder(Context context, String type) {
        String message = context.getResources().getString(R.string.app_name) + context.getResources().getString(R.string.message_daily_reminder);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? 7 : 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        /* mengatasi ketika user aktifkan remider di atas jam 7 */
        if(Calendar.getInstance().after(calendar)){
            // Move to tomorrow
            calendar.add(Calendar.DATE, 1);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        String toast = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? context.getResources().getString(R.string.added_daily_reminder_success) : context.getResources().getString(R.string.added_release_reminder_success);
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context, String type) {
        int requestCode = type.equalsIgnoreCase(TYPE_RELEASE_REMINDER) ? ID_RELEASE_REMINDER : ID_DAILY_REMINDER;

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        String toast = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? context.getResources().getString(R.string.canceled_daily_reminder_success) : context.getResources().getString(R.string.canceled_release_reminder_success);
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }
}
