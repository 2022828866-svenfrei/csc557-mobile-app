package my.edu.uitm.friendmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import my.edu.uitm.friendmanagement.R;
import my.edu.uitm.friendmanagement.helper.DependencyInjectionHelper;
import my.edu.uitm.friendmanagement.services.FriendManagementService;

public class LoginActivity extends AppCompatActivity {

    private FriendManagementService friendManagementService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // hide actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // setup button functionality
        Button loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(view -> onLoginButtonClick());

        Button registerButton = findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(view -> onRegisterButtonClick());

        // set instance of service
        friendManagementService = DependencyInjectionHelper.getFriendManagementServiceByContext(this);
    }

    private void onLoginButtonClick() {
        String email = ((EditText)findViewById(R.id.inputEmail)).getText().toString().trim();
        String password = ((EditText)findViewById(R.id.inputPassword)).getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty() &&
            friendManagementService.isLoginSuccessful(email, password)) {
            startActivity(new Intent(this, OverviewActivity.class));
        }
        else {
            Toast.makeText(getApplicationContext()
                    , "The email or password is incorrect!"
                    , Toast.LENGTH_SHORT).show();
        }
    }

    private void onRegisterButtonClick() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}