package my.edu.uitm.friendmanagement.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import my.edu.uitm.friendmanagement.R;
import my.edu.uitm.friendmanagement.helper.DependencyInjectionHelper;
import my.edu.uitm.friendmanagement.models.Friend;

public class NotificationService extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            FriendManagementService service = DependencyInjectionHelper.getFriendManagementServiceByContext(context);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.YEAR, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            List<String> birthdayFriends = new ArrayList<>();

            service.getFriends().stream()
                    .filter((f) -> {
                        Calendar friendCal = Calendar.getInstance();
                        friendCal.setTime(f.getBirthdate());
                        friendCal.set(Calendar.YEAR, 0);

                        boolean tst = friendCal.getTime().compareTo(cal.getTime()) == 0;

                        return friendCal.getTime().compareTo(cal.getTime()) == 0;
                    })
                    .forEach(f -> birthdayFriends.add(f.getName()));

            if (birthdayFriends.size() > 0) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "NotifChannel")
                        .setSmallIcon(R.drawable.ic_home)
                        .setContentTitle("The following friends have their birthday tomorrow!")
                        .setContentText(String.join(", ", birthdayFriends))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                createNotificationChannel(context);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(1, builder.build());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Channel for notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("NotifChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}