package my.edu.uitm.friendmanagement.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import my.edu.uitm.friendmanagement.R;
import my.edu.uitm.friendmanagement.activities.abstractions.BaseActivity;
import my.edu.uitm.friendmanagement.fragments.FriendOverviewFragment;
import my.edu.uitm.friendmanagement.helper.DependencyInjectionHelper;
import my.edu.uitm.friendmanagement.models.Friend;
import my.edu.uitm.friendmanagement.services.FriendManagementService;
import my.edu.uitm.friendmanagement.services.NotificationService;

public class OverviewActivity extends BaseActivity {

    private FriendManagementService friendManagementService;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        initActivity("Welcome");

        friendManagementService = DependencyInjectionHelper.getFriendManagementServiceByContext(this);
        setupFriendList("");

        // search box setup
        ((EditText)findViewById(R.id.inputSearch)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                setupFriendList(((EditText)findViewById(R.id.inputSearch)).getText().toString().trim());
            }
        });

        // Setup notification alarm schedule
        setupNotificationAlarmSchedule(this);

        // add friend button setup
        findViewById(R.id.buttonAddFriend)
                .setOnClickListener(l -> startActivity(new Intent(this, EditFriendActivity.class)));
    }

    private void setupNotificationAlarmSchedule(Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime() + 1000,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

        // Faking the alarm trigger
        NotificationService notificationService = new NotificationService();
        notificationService.onReceive(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupFriendList(String filter) {
        List<Friend> friends = friendManagementService.getFriends();
        // clear layout of content
        ((LinearLayout)findViewById(R.id.friendList)).removeAllViews();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        friends.stream()
                .filter(f -> f.getName().contains(filter))
                .forEach(f ->  {
                    ft.add(R.id.friendList, new FriendOverviewFragment(f));
                });

        ft.commit();
    }
}