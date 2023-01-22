package my.edu.uitm.friendmanagement.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import my.edu.uitm.friendmanagement.activities.EditFriendActivity;
import my.edu.uitm.friendmanagement.models.Friend;
import my.edu.uitm.friendmanagement.viewmodel.FriendOverviewViewModel;
import my.edu.uitm.friendmanagement.R;

public class FriendOverviewFragment extends Fragment {
    private Friend friend;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public FriendOverviewFragment(Friend friend) {
        this.friend = friend;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_friend_overview, container, false);

        ((TextView)rootView.findViewById(R.id.textName)).setText(friend.getName());
        ((TextView)rootView.findViewById(R.id.textBirthdate))
                .setText(dateFormat.format(friend.getBirthdate()));

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Calendar friendCal = Calendar.getInstance();
        friendCal.setTime(friend.getBirthdate());
        friendCal.set(Calendar.YEAR, 0);


        rootView.findViewById(R.id.buttonNotify).setVisibility(Math.abs(cal.getTime().compareTo(friendCal.getTime())) == 0 ? View.VISIBLE : View.INVISIBLE);
        rootView.findViewById(R.id.buttonNotify).setOnClickListener(l -> {
            sendMessageToWhatsApp(friend.getPhoneNo());
        });

        rootView.findViewById(R.id.buttonEdit).setOnClickListener(l -> {
            Intent intent = new Intent(getContext(), EditFriendActivity.class);
            intent.putExtra("friendId", friend.getId() + "");
            startActivity(intent);
        });

        return rootView;
    }

    private void sendMessageToWhatsApp(String phoneNo) {
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("content://com.android.contacts/data/" + phoneNo));
        i.setType("text/plain");
        i.setPackage("com.whatsapp");
        i.putExtra(Intent.EXTRA_TEXT, "Happy Birthday!!!");

        if (!isAppInstalled("com.whatsapp")) {
            Toast.makeText(getActivity(),
                            "Please install whatsapp first to send auto message!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        try {
            startActivity(i);
        }
        catch (Exception ex) {
            i = new Intent(Intent.ACTION_SEND, Uri.parse("content://com.android.contacts/data/" + phoneNo));
            i.setType("text/plain");
            i.setPackage("com.whatsapp");
            i.putExtra(Intent.EXTRA_TEXT, "Happy Birthday!!!");
            startActivity(i);
        }
    }

    private boolean isAppInstalled(String packageName) {
        try {
            getActivity().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }
}