package my.edu.uitm.friendmanagement.activities.abstractions;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import my.edu.uitm.friendmanagement.R;
import my.edu.uitm.friendmanagement.activities.LoginActivity;
import my.edu.uitm.friendmanagement.activities.OverviewActivity;
import my.edu.uitm.friendmanagement.activities.ReportActivity;

public class BaseActivity extends AppCompatActivity {

    public void initActivity(String activityName) {
        setTitle(activityName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuOverview:
                startActivity(new Intent(this, OverviewActivity.class));
                break;
            case R.id.menuReport:
                startActivity(new Intent(this, ReportActivity.class));
                break;
            case R.id.menuLogout:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case android.R.id.home:
                super.onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
