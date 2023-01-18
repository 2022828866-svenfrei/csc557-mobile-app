package my.edu.uitm.friendmanagement.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

public class OverviewActivity extends BaseActivity {

    private FriendManagementService friendManagementService;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        initActivity("Welcome");

        friendManagementService = DependencyInjectionHelper.getFriendManagementServiceByActivity(this);
        setupFriendList("");

        // search box setup
        ((EditText)findViewById(R.id.inputSearch)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setupFriendList(((EditText)findViewById(R.id.inputSearch)).getText().toString().trim());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // add friend button setup
        findViewById(R.id.buttonAddFriend)
                .setOnClickListener(l -> startActivity(new Intent(this, EditFriendActivity.class)));
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