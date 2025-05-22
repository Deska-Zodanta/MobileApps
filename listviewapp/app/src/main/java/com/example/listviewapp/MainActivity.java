package com.example.listviewapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listViewmenu = (ListView) findViewById(R.id.listViewMenu);

        final ArrayList<MenuModel> listMenu = new ArrayList<>();
        listMenu.add(new MenuModel("Menu 1", "Description menu 1", R.drawable.minus_circle));
        listMenu.add(new MenuModel("Menu 2", "Description menu 2", R.drawable.notification));
        listMenu.add(new MenuModel("Menu 3", "Description menu 3", R.drawable.receive));
        listMenu.add(new MenuModel("Menu 4", "Description menu 4", R.drawable.return_truck));
        listMenu.add(new MenuModel("Menu 5", "Description menu 5", R.drawable.luggage_cart));
        listMenu.add(new MenuModel("Menu 6", "Description menu 6", R.drawable.dispatch));
        listMenu.add(new MenuModel("Menu 7", "Description menu 7",R.drawable.pallet));
        listMenu.add(new MenuModel("Menu 8", "Description menu 8", R.drawable.stock));
        listMenu.add(new MenuModel("Menu 9", "Description menu 9", R.drawable.minus_circle));
        listMenu.add(new MenuModel("Menu 10", "Description menu 10", R.drawable.notification));
        listMenu.add(new MenuModel("Menu 11", "Description menu 11", R.drawable.receive));
        listMenu.add(new MenuModel("Menu 12", "Description menu 12", R.drawable.return_truck));
        listMenu.add(new MenuModel("Menu 13", "Description menu 13", R.drawable.luggage_cart));
        listMenu.add(new MenuModel("Menu 14", "Description menu 14", R.drawable.dispatch));
        listMenu.add(new MenuModel("Menu 15", "Description menu 15",R.drawable.pallet));
        listMenu.add(new MenuModel("Menu 16", "Description menu 16", R.drawable.stock));

        final MenuAdapter menuAdapter = new MenuAdapter(listMenu, this);
        listViewmenu.setAdapter(menuAdapter);
        listViewmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuModel dataModel= listMenu.get(position);
                Snackbar.make(view, dataModel.getTitle()+ "\n" +dataModel.getDescription(), Snackbar.LENGTH_SHORT)
                        .setAction("No action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}