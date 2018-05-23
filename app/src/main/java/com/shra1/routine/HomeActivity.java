package com.shra1.routine;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.shra1.routine.activities.DailyExpenseActivity;
import com.shra1.routine.activities.SettingsActivity;
import com.shra1.routine.activities.TiffinActivity;
import com.shra1.routine.adapters.MainMenuItemsAdapter;
import com.shra1.routine.asynctasks.DailyExpenseEntriesSynchronizingTask;
import com.shra1.routine.asynctasks.TiffinEntriesSynchronizingTask;
import com.shra1.routine.database.MyDatabase;
import com.shra1.routine.mymodels.DailyExpenseEntry;
import com.shra1.routine.mymodels.MainMenuItem;
import com.shra1.routine.mymodels.TiffinEntry;
import com.shra1.routine.utils.SharedPreferencesManager;
import com.shra1.routine.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static com.shra1.routine.utils.Utils.checkUserNameNotSet;

public class HomeActivity extends AppCompatActivity {
    List<MainMenuItem> mainMenuItems;
    Context mCtx;
    Activity mAct;
    ProgressDialog p;
    SharedPreferencesManager sharedPreferencesManager;
    //DIALOG WIDGETS
    Dialog userNameInputDialog;
    EditText etDLUNIUserName;
    Button bDLUNISave;
    Button bDLUNICancle;
    MyDatabase myDatabase;
    //ACTIVITY WIDGETS
    private GridView gvHAMainMenu;
    private View.OnClickListener tiffinClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkUserNameNotSet(mCtx)) {
                Crouton.makeText(mAct, "Please register the Username first", Style.ALERT).show();
            } else {
                Intent tiffinIntent = new Intent(mCtx, TiffinActivity.class);
                startActivity(tiffinIntent);
            }
        }
    };
    private View.OnClickListener settingsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent settingsIntent = new Intent(mCtx, SettingsActivity.class);
            startActivity(settingsIntent);
        }
    };
    private View.OnClickListener saveButtonDialogClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Utils.cantBeEmptyET(etDLUNIUserName)) return;

            String userName = etDLUNIUserName.getText().toString().trim();
            sharedPreferencesManager.setUserName(userName);

            userNameInputDialog.dismiss();
        }
    };
    private View.OnClickListener cancleButtonDialogClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userNameInputDialog.dismiss();
        }
    };
    private View.OnClickListener dailyExpenseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkUserNameNotSet(mCtx)) {
                Crouton.makeText(mAct, "Please register the Username first", Style.ALERT).show();
            } else {
                Intent tiffinIntent = new Intent(mCtx, DailyExpenseActivity.class);
                startActivity(tiffinIntent);
            }
        }
    };
    private String TAG = "HomeActivity";
    private View.OnClickListener syncClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkUserNameNotSet(mCtx)) {
                Crouton.makeText(mAct, "Please register the Username first", Style.ALERT).show();
            } else {
                syncTiffinEntries(new SyncTiffinEntriesCallback() {
                    @Override
                    public void onCompleteSuccessfully() {

                        syncDailyExpenseEntries(new SyncDailyExpenseEntriesCallback() {
                            @Override
                            public void onCompleteSuccessfully() {

                            }
                        });

                    }
                });

            }
        }
    };

    private void syncDailyExpenseEntries(final SyncDailyExpenseEntriesCallback c) {
        final List<DailyExpenseEntry> localDailyExpenseEntries = DailyExpenseEntry.DBCommands.getAllDailyExpenseEntries(myDatabase.getReadableDatabase());

        DailyExpenseEntry.FirebaseCommands.getAllDailyExpenseEntries(new DailyExpenseEntry.FirebaseCommands.GetAllDailyExpenseEntriesCallback() {
            @Override
            public void onPreExecute() {
                p.setMessage("Fetching Daily Expense Entries...");
                p.show();
            }

            @Override
            public void onSuccess(List<DailyExpenseEntry> firebaseDailyExpenseEntries) {
                //START SYNCHRONIZATION
                DailyExpenseEntriesSynchronizingTask
                        dailyExpenseEntriesSynchronizingTask
                        = new DailyExpenseEntriesSynchronizingTask(new DailyExpenseEntriesSynchronizingTask.DailyExpenseEntriesSynchronizingTaskCallback() {
                    @Override
                    public void onProcessStart() {
                        p.setMessage("Synchronizing Daily Expense Entries...");
                        p.show();
                    }

                    @Override
                    public void onProcessEnd() {
                        p.dismiss();
                        c.onCompleteSuccessfully();
                    }
                });

                dailyExpenseEntriesSynchronizingTask
                        .execute(localDailyExpenseEntries, firebaseDailyExpenseEntries, myDatabase);
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onComplete() {
                //FETCH COMPLETE
                p.dismiss();
            }
        });
    }

    private void syncTiffinEntries(final SyncTiffinEntriesCallback c) {
        final List<TiffinEntry> localTiffinEntries = TiffinEntry.DBCommands.getAllTiffinEntries(myDatabase.getReadableDatabase());

        TiffinEntry.FirebaseCommands.getAllTiffinEntries(new TiffinEntry.FirebaseCommands.GetAllTiffinEntriesCallback() {
            @Override
            public void onPreExecute() {
                p.setMessage("Fetching Tiffin Entries...");
                p.show();
            }

            @Override
            public void onSuccess(List<TiffinEntry> firebaseTiffinEntries) {
                //START SYNCHRONIZATION

                TiffinEntriesSynchronizingTask
                        tiffinEntriesSynchronizingTask
                        = new TiffinEntriesSynchronizingTask(new TiffinEntriesSynchronizingTask.TiffinEntriesSynchronizingTaskCallback() {
                    @Override
                    public void onProcessStart() {
                        p.setMessage("Synchronizing Tiffin Entries...");
                        p.show();
                    }

                    @Override
                    public void onProcessEnd() {
                        p.dismiss();
                        c.onCompleteSuccessfully();
                    }
                });

                tiffinEntriesSynchronizingTask
                        .execute(localTiffinEntries, firebaseTiffinEntries, myDatabase);
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onComplete() {
                //FETCH COMPLETE
                p.dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mCtx = this;
        mAct = this;
        p = new ProgressDialog(mCtx);

        MAIN();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(mCtx, "Please grant all permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        MAIN();
    }

    private void MAIN() {
        myDatabase = MyDatabase.getInstance(mCtx);

        sharedPreferencesManager = SharedPreferencesManager.getInstance(mCtx);

        gvHAMainMenu = findViewById(R.id.gvHAMainMenu);

        mainMenuItems = new ArrayList<>();

        mainMenuItems.add(new MainMenuItem("Tiffin", R.drawable.ic_lunchbox, tiffinClickListener));
        mainMenuItems.add(new MainMenuItem("Daily Expense", R.drawable.ic_expense, dailyExpenseClickListener));
        mainMenuItems.add(new MainMenuItem("Settings", R.drawable.ic_settings, settingsClickListener));
        mainMenuItems.add(new MainMenuItem("Sync data", R.drawable.ic_sync, syncClickListener));

        MainMenuItemsAdapter adapter = new MainMenuItemsAdapter(mCtx, mainMenuItems);

        gvHAMainMenu.setAdapter(adapter);

        if (checkUserNameNotSet(mCtx)) {
            showUserNameInputDialog();
        }

    }

    private void showUserNameInputDialog() {
        userNameInputDialog = new Dialog(mCtx);

        userNameInputDialog.setContentView(R.layout.dialog_layout_user_name_input);
        userNameInputDialog.setCancelable(false);

        etDLUNIUserName = userNameInputDialog.findViewById(R.id.etDLUNIUserName);
        bDLUNISave = (Button) userNameInputDialog.findViewById(R.id.bDLUNISave);
        bDLUNICancle = (Button) userNameInputDialog.findViewById(R.id.bDLUNICancle);

        bDLUNISave.setOnClickListener(saveButtonDialogClickListener);
        bDLUNICancle.setOnClickListener(cancleButtonDialogClickListener);

        userNameInputDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkUserNameNotSet(mCtx)) {

        } else {
            setTitle("Hello " + sharedPreferencesManager.getUserName() + "!");
        }
    }

    private interface SyncDailyExpenseEntriesCallback {
        void onCompleteSuccessfully();
    }

    private interface SyncTiffinEntriesCallback {
        void onCompleteSuccessfully();
    }

}
