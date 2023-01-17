package my.edu.uitm.friendmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import my.edu.uitm.friendmanagement.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // hide actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}