package com.example.curfewdelivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;

import com.example.curfewdelivery.databinding.ListItemBinding;

import java.util.ArrayList;
import java.util.Hashtable;

class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> allItems;
    Hashtable<Integer, Boolean> checkedItems;
    public CustomAdapter(Context context, ArrayList<String> allItems, Hashtable<Integer, Boolean> checkedItems) {
        this.context = context;
        this.allItems = allItems;
        this.checkedItems = checkedItems;
    }



    @Override
    public int getCount() {
        return allItems.size();
    }

    @Override
    public Object getItem(int position) {
        return allItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        ListItemBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.list_item, container, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else {
            binding = (ListItemBinding) convertView.getTag();
        }

        binding.checkbox.setText(allItems.get(position));

        binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedItems.put(position,isChecked);
            }
        });

        return convertView;
    }
}