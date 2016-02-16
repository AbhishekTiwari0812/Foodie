package com.example.abhishek.foodie;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.LinearGradient;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Activity base_activity;
    private ArrayList<UserListItem> list_items;
    Resources resources;
    LayoutInflater inflater;

    public CustomAdapter(Activity a, ArrayList<UserListItem> list_items, Resources resources) {
        base_activity = a;
        this.list_items = list_items;
        this.resources = resources;
        inflater = (LayoutInflater) base_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (convertView == null) {
            listView = inflater.inflate(R.layout.daily_user_list_item, parent, false);
        }
        TextView user_index = (TextView) listView.findViewById(R.id.daily_user_index);
        user_index.setText(" " + (position + 1) + ".)");
        return listView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {

        return list_items.size();
    }

    @Override
    public Object getItem(int position) {

        return list_items.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return list_items.size() == 0 ? true : false;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    void _(Object str) {
        Log.d("Error:", "" + str);
    }
}