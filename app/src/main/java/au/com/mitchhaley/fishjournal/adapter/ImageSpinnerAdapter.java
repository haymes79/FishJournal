package au.com.mitchhaley.fishjournal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import au.com.mitchhaley.fishjournal.R;

/**
 * Created by mitch on 8/03/14.
 */
public class ImageSpinnerAdapter extends ArrayAdapter<String> {

    private String[] values;
    private int[] images;
    private  Context context;


    public ImageSpinnerAdapter(Context context, int textViewResourceId, String[] values) {
        super(context, textViewResourceId, values);
        this.values = values;
        this.context = context;
    }

    public void setImages(int[] images) {
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View row=inflater.inflate(R.layout.image_spinner_row, parent, false);
        TextView label=(TextView) row.findViewById(R.id.rowTextValue);
        label.setText(values[position]);

        ImageView icon=(ImageView) row.findViewById(R.id.rowImage);
        if (images[position] == -1) {
            icon.setImageDrawable( new ColorDrawable(Color.TRANSPARENT));
        } else {
            icon.setImageResource(images[position]);
        }

        return row;
    }
}