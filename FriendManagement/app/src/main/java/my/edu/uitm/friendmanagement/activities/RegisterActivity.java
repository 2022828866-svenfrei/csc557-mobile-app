package my.edu.uitm.friendmanagement.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import my.edu.uitm.friendmanagement.R;
import my.edu.uitm.friendmanagement.fragments.DatePickerFragment;
import my.edu.uitm.friendmanagement.helper.DependencyInjectionHelper;
import my.edu.uitm.friendmanagement.models.Gender;
import my.edu.uitm.friendmanagement.models.Login;
import my.edu.uitm.friendmanagement.services.FriendManagementService;

public class RegisterActivity extends AppCompatActivity {

    private FriendManagementService friendManagementService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // hide actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Gender spinner
        Spinner genderSpinner = findViewById(R.id.inputGender);
        genderSpinner.setAdapter(new ArrayAdapter<Gender>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Gender.values()));

        // Datepicker dialog setup
        EditText inputBirthdate = findViewById(R.id.inputBirthdate);
        inputBirthdate.setOnClickListener(l -> showDatePickerDialog());

        // Back button setup
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(l -> onBackButtonClick());

        // Register button setup
        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(l -> onRegisterClick());

        // set instance of service
        friendManagementService = DependencyInjectionHelper.getFriendManagementServiceByActivity(this);
    }

    private void onBackButtonClick() {
        super.onBackPressed();
    }

    private void onRegisterClick() {
        String name = ((EditText)findViewById(R.id.inputName)).getText().toString().trim();
        Gender gender = (Gender)((Spinner)findViewById(R.id.inputGender)).getSelectedItem();
        String birthdate = ((EditText)findViewById(R.id.inputBirthdate)).getText().toString().trim();
        String phoneNo = ((EditText)findViewById(R.id.inputPhoneNo)).getText().toString().trim();
        String email = ((EditText)findViewById(R.id.inputEmail)).getText().toString().trim();
        String password = ((EditText)findViewById(R.id.inputPassword)).getText().toString().trim();

        if (name.isEmpty()
                || birthdate.isEmpty()
                || phoneNo.isEmpty()
                || email.isEmpty()
                || password.isEmpty())
            Toast.makeText(getApplicationContext()
                    , "Please fill out all the fields!"
                    , Toast.LENGTH_LONG).show();
        else {
            Login login = friendManagementService.getLoginByEmail(email);

            if (login != null)
                Toast.makeText(getApplicationContext()
                        , "User with this email address already exists!"
                        , Toast.LENGTH_LONG).show();
            else {
                try {
                    friendManagementService.insertLogin(name
                            , gender
                            , dateFormat.parse(birthdate)
                            , phoneNo
                            , email
                            , password);

                    Toast.makeText(getApplicationContext()
                            , "Register was successful"
                            , Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(this, OverviewActivity.class));
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext()
                            , "Input date is invalid!"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showDatePickerDialog() {
        EditText inputBirthdate = findViewById(R.id.inputBirthdate);

        DialogFragment newFragment = new DatePickerFragment(d -> onSetDateCallback(d), inputBirthdate.getText().toString().trim());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onSetDateCallback(LocalDate date) {
        EditText inputBirthdate = findViewById(R.id.inputBirthdate);

        inputBirthdate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}