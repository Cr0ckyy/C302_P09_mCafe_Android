package com.example.c302_p09_mcafe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MenuItemAdapter extends ArrayAdapter<MenuCategoryItem> {

    private ArrayList<MenuCategoryItem> alMenuCatItem;
    private Context context;

    public MenuItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MenuCategoryItem> objects) {
        super(context, resource, objects);
        alMenuCatItem = objects;
        this.context = context;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.menu_item_row, parent, false);

        TextView tvItem = rowView.findViewById(R.id.txtItem);
        TextView tvItemPrice = rowView.findViewById(R.id.txtItemPrice);

        MenuCategoryItem menuCategoryItem = alMenuCatItem.get(position);

        tvItem.setText(menuCategoryItem.getDescription());
        tvItemPrice.setText(String.format("$%.2f", menuCategoryItem.getUnitPrice()));

        return rowView;
    }
}
