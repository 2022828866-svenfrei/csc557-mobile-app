package my.edu.uitm.friendmanagement.activities;

import android.os.Bundle;

import my.edu.uitm.friendmanagement.R;
import my.edu.uitm.friendmanagement.activities.abstractions.BaseActivity;

public class ReportActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initActivity("Report");
    }
}