package com.example.splitit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.splitit.model.Group;
import com.example.splitit.repository.OnRepositoryActionListener;
import com.example.splitit.ui.fragment.GroupsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_friends, R.id.navigation_groups, R.id.navigation_activity)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean firstOpen = sharedPref.getBoolean(getString(R.string.app_open_first_time), true);
        if (firstOpen){
            showSyncRequestDialog();
            Log.i("SharedPref", "Application has been opened for the first time");
        }
    }

    private void showSyncRequestDialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_request_backup, null);

        Button syncButton = dialogView.findViewById(R.id.button_sync);
        Button noSyncButton = dialogView.findViewById(R.id.button_no_sync);

        syncButton.setOnClickListener(v -> {

            dialogBuilder.dismiss();
        });

       noSyncButton.setOnClickListener(v -> {
            dialogBuilder.dismiss();
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.app_open_first_time), false);
        editor.apply();
    }

    private void syncDatabaseRequest(){
        String url = "https://my-json-server.typicode.com/MoldovanG/JsonServer/users";
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                List<User> userList = new ArrayList<>();
//                for (int index = 0; index < response.length(); index++) {
//                    try {
//                        User user = User.fromJson(response.getJSONObject(index).toString());
//                        userList.add(user);
//                    } catch (JSONException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//                notifyUserRecyclerView(userList);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(), "Volley error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        RequestHelper.getRequestHelperInstance(getContext()).addToRequestQueue(jsonArrayRequest);

    }
}
