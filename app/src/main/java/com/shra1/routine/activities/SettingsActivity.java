package com.shra1.routine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.shra1.routine.R;
import com.shra1.routine.services.UserPresentRecieverRegistrationService;
import com.shra1.routine.utils.Constants;
import com.shra1.routine.utils.SharedPreferencesManager;
import com.shra1.routine.utils.Utils;

import static com.shra1.routine.utils.Utils.checkUserNameNotSet;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferencesManager sharedPreferencesManager;
    private Context mCtx;

    private EditText etSAUserName;
    private CheckBox cbSAEnableNotificationsForTiffinEntry;
    private TextView tvSANotificationServiceStatus;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mCtx = this;

        initViews();

        sharedPreferencesManager = SharedPreferencesManager.getInstance(mCtx);

        if (checkUserNameNotSet(mCtx)) {
        } else {
            etSAUserName.setText(sharedPreferencesManager.getUserName());
            etSAUserName.setEnabled(false);
        }

        if (sharedPreferencesManager.getEnableNotificationsForTiffinEntry()) {
            cbSAEnableNotificationsForTiffinEntry
                    .setChecked(true);
            checkUserPresentRecieverRegistrationServiceStatus();
        } else {
            cbSAEnableNotificationsForTiffinEntry
                    .setChecked(false);
        }

        cbSAEnableNotificationsForTiffinEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c = (CheckBox) v;
                if (c.isChecked()) {
                    sharedPreferencesManager.setEnableNotificationsForTiffinEntry(true);
                    checkUserPresentRecieverRegistrationServiceStatus();
                } else {
                    sharedPreferencesManager.setEnableNotificationsForTiffinEntry(false);
                    stopUserPresentRecieverRegistrationService();
                    tvSANotificationServiceStatus.setVisibility(View.GONE);
                }
            }
        });


    }

    private boolean checkUserPresentRecieverRegistrationServiceStatus() {
        tvSANotificationServiceStatus.setVisibility(View.VISIBLE);
        if (Constants.isMyServiceRunning(mCtx, UserPresentRecieverRegistrationService.class)) {
            Spanned spanned = Html.fromHtml("Service is <b><font color=\"#00ff00\">Running!</b></font>");
            tvSANotificationServiceStatus
                    .setText(spanned);
            return true;
        } else {
            Spanned spanned = Html.fromHtml("Service is <b><font color=\"#ff0000\">Not Running!</font> Click here to start the service.</b>");
            tvSANotificationServiceStatus.setText(spanned);
            tvSANotificationServiceStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUserPresentRecieverRegistrationService();
                    checkUserPresentRecieverRegistrationServiceStatus();
                    tvSANotificationServiceStatus.setOnClickListener(null);
                }
            });
            return false;
        }
    }

    private void startUserPresentRecieverRegistrationService() {
        if (Constants.isMyServiceRunning(mCtx, UserPresentRecieverRegistrationService.class)) {
        } else {
            Intent intent = new Intent(mCtx, UserPresentRecieverRegistrationService.class);
            startService(intent);
        }
    }

    private void stopUserPresentRecieverRegistrationService() {
        if (Constants.isMyServiceRunning(mCtx, UserPresentRecieverRegistrationService.class)) {
            Intent intent = new Intent(mCtx, UserPresentRecieverRegistrationService.class);
            stopService(intent);
        }
    }

    private void initViews() {
        etSAUserName = findViewById(R.id.etSAUserName);
        cbSAEnableNotificationsForTiffinEntry = (CheckBox) findViewById(R.id.cbSAEnableNotificationsForTiffinEntry);
        tvSANotificationServiceStatus = (TextView) findViewById(R.id.tvSANotificationServiceStatus);
        tvSANotificationServiceStatus.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings_activity, menu);
        return true;
    }

    public void mnuSASave(MenuItem item) {
        if (Utils.cantBeEmptyET(etSAUserName)) return;
        String userName = etSAUserName.getText().toString().trim();
        sharedPreferencesManager.setUserName(userName);
        finish();
    }
}
