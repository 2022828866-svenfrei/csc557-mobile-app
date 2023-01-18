package my.edu.uitm.friendmanagement.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import my.edu.uitm.friendmanagement.repositories.FriendManagementRepository;
import my.edu.uitm.friendmanagement.services.FriendManagementService;

public class DependencyInjectionHelper {
    private static final String FRIEND_MANAGAMENT_PREFERENCES = "FriendManagementPrefs";
    private static SharedPreferences sharedPreferences;

    public static FriendManagementService getFriendManagementServiceByActivity(Activity activity) {
        FriendManagementRepository repository = new FriendManagementRepository(activity);

        return new FriendManagementService(repository
                , getSharedPreferences(activity));
    }

    private static SharedPreferences getSharedPreferences(Activity activity) {
        if (sharedPreferences == null)
            sharedPreferences = activity.getSharedPreferences(FRIEND_MANAGAMENT_PREFERENCES, Context.MODE_PRIVATE);

        return  sharedPreferences;
    }
}
