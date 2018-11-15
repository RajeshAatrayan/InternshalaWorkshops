package com.ibrickedlabs.internshala.WorkshopsData;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ibrickedlabs.internshala.R;

public class WorkshopAdapter extends CursorAdapter {
    public WorkshopAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_workshop_items, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView workshopNameView = (TextView) view.findViewById(R.id.wsname);

        int workshopNameColIndex = cursor.getColumnIndex(WorkshopContract.WorkshopEntry.WORKSHOP_NAME);
        String workshopName = cursor.getString(workshopNameColIndex);
        workshopNameView.setText(workshopName);

    }
}
