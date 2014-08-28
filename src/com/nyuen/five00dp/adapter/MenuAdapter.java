package com.nyuen.five00dp.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nyuen.five00dp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;

    private List<String> mMenuItem;

    public MenuAdapter(Context context) {

        mInflater = LayoutInflater.from(context);       
        mMenuItem = Arrays.asList(context.getResources().getStringArray(R.array.category));
//        mMenuItem = new ArrayList<String>();
//        mMenuItem.add("Home");
//        mMenuItem.add("Profile");
//        mMenuItem.add("A");
//        mMenuItem.add("B");
//        mMenuItem.add("C");
    }

    @Override
    public int getCount() {
        return mMenuItem.size();
    }

    @Override
    public String getItem(int position) {
        return mMenuItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuHolder holder;
        
        if (convertView == null) {
            holder = new MenuHolder();
            convertView = mInflater.inflate(R.layout.menu_list_item, null, false);
            
            holder.textViewMenuItem = (TextView) convertView.findViewById(R.id.textViewMenuItem);
            convertView.setTag(holder);
        } else {
            holder = (MenuHolder) convertView.getTag();
        }
        
        holder.textViewMenuItem.setText(getItem(position));
        
        return convertView;
    }

    private class MenuHolder {
        TextView textViewMenuItem;
    }
}
