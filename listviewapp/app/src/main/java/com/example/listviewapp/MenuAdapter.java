package com.example.listviewapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuAdapter extends ArrayAdapter<MenuModel> implements View.OnClickListener{

    private ArrayList<MenuModel> dataSet;
    private Context mContext;

    public static class ViewHolder {
        public TextView txtTitle;
        public TextView txtDescription;
        public ImageView imgIcon;
    }

    public MenuAdapter(ArrayList<MenuModel> data, Context context) {
        super(context, -1, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        final Object object= getItem(position);
        final MenuModel dataModel=(MenuModel)object;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MenuModel dataModel = getItem(position);
        final ViewHolder viewHolder;
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View result;
        String desc = dataModel.getDescription();

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_menu, parent, false);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.firstLine);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.secondLine);
            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;
        viewHolder.txtTitle.setText(dataModel.getTitle());
        viewHolder.txtDescription.setText(dataModel.getDescription());
        viewHolder.imgIcon.setImageResource(dataModel.getIcon());
        viewHolder.imgIcon.setOnClickListener(this);
        viewHolder.imgIcon.setTag(position);

        return convertView;
    }
}