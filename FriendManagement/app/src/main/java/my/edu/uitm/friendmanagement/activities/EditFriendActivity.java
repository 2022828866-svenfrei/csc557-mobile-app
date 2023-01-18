package my.edu.uitm.friendmanagement.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import my.edu.uitm.friendmanagement.R;
import my.edu.uitm.friendmanagement.activities.abstractions.BaseActivity;
import my.edu.uitm.friendmanagement.fragments.DatePickerFragment;
import my.edu.uitm.friendmanagement.helper.DependencyInjectionHelper;
import my.edu.uitm.friendmanagement.models.Friend;
import my.edu.uitm.friendmanagement.models.Gender;
import my.edu.uitm.friendmanagement.services.FriendManagementService;

public class EditFriendActivity extends BaseActivity {

    private FriendManagementService friendManagementService;
    private String friendId;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);
        initActivity("Edit");
        friendManagementService = DependencyInjectionHelper.getFriendManagementServiceByActivity(this);

        // gender spinner setup
        Spinner genderSpinner = findViewById(R.id.inputGender);
        genderSpinner.setAdapter(new ArrayAdapter<Gender>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Gender.values()));

        // Datepicker dialog setup
        EditText inputBirthdate = findViewById(R.id.inputBirthdate);
        inputBirthdate.setOnClickListener(l -> showDatePickerDialog());

        // Check if edit or new
        friendId = getIntent().getStringExtra("friendId");

        if(friendId != null) {
            Friend friend = friendManagementService.getFriendById(Long.parseLong(friendId));

            ((EditText)findViewById(R.id.inputName)).setText(friend.getName());
            ((Spinner)findViewById(R.id.inputGender)).setSelection(friend.getGender().getIndex());
            ((EditText)findViewById(R.id.inputBirthdate))
                    .setText(dateFormat.format(friend.getBirthdate()));
            ((EditText)findViewById(R.id.inputPhoneNo)).setText(friend.getPhoneNo());
            ((EditText)findViewById(R.id.inputEmail)).setText(friend.getEmail());
        }
        else {
           findViewById(R.id.buttonDelete).setVisibility(View.GONE);
        }

        // set back button in menu bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // save button setup
        findViewById(R.id.buttonSave).setOnClickListener(l -> onSaveClick());

        // delete button setup
        findViewById(R.id.buttonDelete).setOnClickListener(l -> onDeleteClick());
    }

    private void onSaveClick() {
        String name = ((EditText)findViewById(R.id.inputName)).getText().toString().trim();
        Gender gender = (Gender)((Spinner)findViewById(R.id.inputGender)).getSelectedItem();
        String birthdate = ((EditText)findViewById(R.id.inputBirthdate)).getText().toString().trim();
        String phoneNo = ((EditText)findViewById(R.id.inputPhoneNo)).getText().toString().trim();
        String email = ((EditText)findViewById(R.id.inputEmail)).getText().toString().trim();

        if (name.isEmpty()
                || birthdate.isEmpty()
                || phoneNo.isEmpty()
                || email.isEmpty())
            Toast.makeText(getApplicationContext()
                    , "Please fill out all the fields!"
                    , Toast.LENGTH_LONG).show();
        else {
            try {
                if (friendId == null) {
                    friendManagementService.insertFriend(name
                            , gender
                            , dateFormat.parse(birthdate)
                            , phoneNo
                            , email
                            , null);

                    Toast.makeText(getApplicationContext()
                            , "Add friend was successful"
                            , Toast.LENGTH_SHORT).show();
                }
                else {
                    friendManagementService.updateFriend(Long.parseLong(friendId)
                            , name
                            , gender
                            , dateFormat.parse(birthdate)
                            , phoneNo
                            , email
                            , null);

                    Toast.makeText(getApplicationContext()
                            , "Update friend was successful"
                            , Toast.LENGTH_SHORT).show();
                }

                startActivity(new Intent(this, OverviewActivity.class));
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext()
                        , "Input date is invalid!"
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onDeleteClick() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditFriendActivity.this);
        alertDialog.setTitle("Confirm delete");
        alertDialog.setMessage("Do you want to delete this friend?");
        alertDialog.setPositiveButton("Yes", (d, i) -> {
            friendManagementService.deleteFriend(Long.parseLong(friendId));
            startActivity(new Intent(this, OverviewActivity.class));
        });
        alertDialog.setNegativeButton("No", (d, i) -> {});
        alertDialog.show();
    }

    private void showDatePickerDialog() {
        EditText inputBirthdate = findViewById(R.id.inputBirthdate);

        DialogFragment newFragment = new DatePickerFragment(d -> onSetDateCallback(d), inputBirthdate.getText().toString().trim());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void onSetDateCallback(LocalDate date) {
        EditText inputBirthdate = findViewById(R.id.inputBirthdate);

        inputBirthdate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}