package my.edu.uitm.friendmanagement.services;

import android.content.SharedPreferences;

import java.util.Date;
import java.util.List;

import my.edu.uitm.friendmanagement.models.Friend;
import my.edu.uitm.friendmanagement.models.Gender;
import my.edu.uitm.friendmanagement.models.Login;
import my.edu.uitm.friendmanagement.repositories.FriendManagementRepository;

public class FriendManagementService {
    private FriendManagementRepository repository;

    // local file storage
    private SharedPreferences sharedPreferences;
    private static String KEY_CURRENT_USER = "current_user_id";

    public FriendManagementService(FriendManagementRepository repository, SharedPreferences sharedPreferences) {
        this.repository = repository;
        sharedPreferences = sharedPreferences;
    }

    public long insertLogin(String name, Gender gender, Date birthdate, String phoneNo, String email, String password) {
        return repository.insertLogin(name, gender, birthdate, phoneNo, email, password);
    }

    public boolean isLoginSuccessful(String email, String password) {
        Login login = repository.getLoginByEmail(email);

        boolean isSuccessful = login != null && login.getPassword().equals(password);

        if (isSuccessful) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_CURRENT_USER, login.getId() + "");
        }

        return isSuccessful;
    }

    public long insertFriend(String name, Gender gender, Date birthdate, String phoneNo, String email, String photo) {
        return repository.insertFriend(getCurrentUser(), name, gender, birthdate, phoneNo, email, photo);
    }

    public void updateFriend(Friend friend) {
        repository.updateFriend(friend);
    }

    public Friend getFriendById(long id) {
        return repository.getFriendById(id);
    }

    public List<Friend> getFriends() {
        return repository.getFriendsByFK(getCurrentUser());
    }

    public void deleteFriend(long id) {
        repository.deleteFriendById(id);
    }

    private long getCurrentUser() {
        return sharedPreferences.getLong(KEY_CURRENT_USER, 0);
    }
}
