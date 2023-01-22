package my.edu.uitm.friendmanagement.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.time.Month;
import java.util.List;

import my.edu.uitm.friendmanagement.R;
import my.edu.uitm.friendmanagement.activities.abstractions.BaseActivity;
import my.edu.uitm.friendmanagement.helper.DependencyInjectionHelper;
import my.edu.uitm.friendmanagement.models.Friend;
import my.edu.uitm.friendmanagement.models.Gender;
import my.edu.uitm.friendmanagement.services.FriendManagementService;

public class ReportActivity extends BaseActivity {

    @RequiresApi(api = Build.VERSION_CODES.N|Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_report);
            initActivity("Report");

            FriendManagementService friendManagementService = DependencyInjectionHelper.getFriendManagementServiceByContext(this);

            // count friends for report
            List<Friend> friends = friendManagementService.getFriends();

            ((TextView)findViewById(R.id.maleFriendsCount))
                    .setText(friends.stream().filter(f -> Gender.MALE.equals(f.getGender())).count() + "");
            ((TextView)findViewById(R.id.femaleFriendsCount))
                    .setText(friends.stream().filter(f -> Gender.FEMALE.equals(f.getGender())).count() + "");

            TableLayout monthCountTable = findViewById(R.id.monthCountTable);
            setTableRow(monthCountTable, "Month of Birthday", "Count of Friends");
            for (int i=1; i<=12; i++) {
                Month month = Month.of(i);

                int finalI = i;
                setTableRow(monthCountTable, month.name()
                        , friends.stream().filter(f -> finalI-1 == f.getBirthdate().getMonth()).count() + "");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setTableRow(TableLayout table, String valueCol1, String valueCol2) {
        TableRow tr = new TableRow(table.getContext());
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        tr.addView(getColumnTextView(tr.getContext(), valueCol1));
        tr.addView(getColumnTextView(tr.getContext(), valueCol2));
        table.addView(tr);
    }

    private TextView getColumnTextView(Context context, String text) {
        TextView column = new TextView(context);
        column.setWidth(400);
        column.setText(text);

        return column;
    }
}