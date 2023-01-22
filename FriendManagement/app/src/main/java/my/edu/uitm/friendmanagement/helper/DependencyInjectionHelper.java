package my.edu.uitm.friendmanagement.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import my.edu.uitm.friendmanagement.repositories.FriendManagementRepository;
import my.edu.uitm.friendmanagement.services.FriendManagementService;

public class DependencyInjectionHelper {
    private static final String FRIEND_MANAGAMENT_PREFERENCES = "FriendManagementPrefs";
    private static SharedPreferences sharedPreferences;

    public static FriendManagementService getFriendManagementServiceByContext(Context context) {
        return new FriendManagementService(getFriendManagementRepository(context)
                , getSharedPreferences(context));
    }

    public static FriendManagementRepository getFriendManagementRepository(Context context) {
        return new FriendManagementRepository(context);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(FRIEND_MANAGAMENT_PREFERENCES, Context.MODE_PRIVATE);

        return  sharedPreferences;
    }
}
