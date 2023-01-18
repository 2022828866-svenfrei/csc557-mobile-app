package my.edu.uitm.friendmanagement.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;

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
        rootView.findViewById(R.id.buttonEdit).setOnClickListener(l -> {
            Intent intent = new Intent(getContext(), EditFriendActivity.class);
            intent.putExtra("friendId", friend.getId() + "");
            startActivity(intent);
        });

        return rootView;
    }
}