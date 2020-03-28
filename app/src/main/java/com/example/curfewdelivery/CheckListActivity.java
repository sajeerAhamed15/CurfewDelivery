package com.example.curfewdelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.curfewdelivery.databinding.ActivityCheckListBinding;

import java.util.ArrayList;
import java.util.Hashtable;

public class CheckListActivity extends AppCompatActivity {


    ActivityCheckListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_list);
        setTitle("Check list");

        final ArrayList<String> allItems = fetchAllItems();
        final Hashtable<Integer, Boolean> checkedItems = new Hashtable<>();
        checkedItems.put(0,true);

        CustomAdapter customAdapter = new CustomAdapter(this, allItems, checkedItems);
        binding.listview.setAdapter(customAdapter);

        binding.proceedBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkedItems.containsValue(true)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckListActivity.this);
                    builder.setMessage("Please select items to place the order")
                            .setTitle(getString(R.string.app_name))
                            .setNegativeButton("Back",null).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckListActivity.this);
                    builder.setMessage("Are you sure?")
                            .setTitle(getString(R.string.app_name))
                            .setPositiveButton("Place the order", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    placeOrder(allItems,checkedItems);

                                    Intent d = new Intent(CheckListActivity.this, OrderPlacedActivity.class);
                                    startActivity(d);
                                }
                            })
                            .setNegativeButton("Back",null).show();
                }
            }
        });

    }

    private void placeOrder(ArrayList<String> allItems, Hashtable<Integer, Boolean> checkedItems) {

    }

    private ArrayList<String> fetchAllItems() {
        ArrayList<String> allItems = new ArrayList<>();
        allItems.add("Rice");
        allItems.add("Dhal");
        allItems.add("Potato");
        allItems.add("Big onion");
        allItems.add("Sugar");
        allItems.add("Milk powder");
        allItems.add("Tea leaves");
        allItems.add("Vegetable Oil");
        allItems.add("Flour");
        allItems.add("Coconuts");
        allItems.add("Fish");
        allItems.add("Chicken");
        allItems.add("Noodles");
        allItems.add("Vegetables");
        allItems.add("Chillies/Spices");
        return allItems;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
