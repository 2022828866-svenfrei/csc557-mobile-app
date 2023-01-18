package my.edu.uitm.friendmanagement.helper;

import android.app.Activity;
import android.content.Context;

import my.edu.uitm.friendmanagement.repositories.FriendManagementRepository;
import my.edu.uitm.friendmanagement.services.FriendManagementService;

public class DependencyInjectionHelper {
    private static final String FRIEND_MANAGAMENT_PREFERENCES = "FriendManagementPrefs";

    public static FriendManagementService getFriendManagementServiceByActivity(Activity activity) {
        FriendManagementRepository repository = new FriendManagementRepository(activity);

        return new FriendManagementService(repository
                , activity.getSharedPreferences(FRIEND_MANAGAMENT_PREFERENCES, Context.MODE_PRIVATE));
    }
}
