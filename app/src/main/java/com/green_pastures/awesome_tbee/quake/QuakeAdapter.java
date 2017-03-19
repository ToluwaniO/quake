package com.green_pastures.awesome_tbee.quake;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tbee on 3/1/17.
 */

public class QuakeAdapter extends ArrayAdapter<Quakes> {

    public QuakeAdapter(Context context, List<Quakes> quakesList){
        super(context, 0, quakesList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.quake_list, parent, false);
        }

        Quakes quakeItem = getItem(position);

        TextView mag = (TextView)listItemView.findViewById(R.id.magnitude_txtView);
        TextView distance = (TextView)listItemView.findViewById(R.id.distance_txtView);
        TextView location = (TextView)listItemView.findViewById(R.id.location_txtView);
        TextView date = (TextView)listItemView.findViewById(R.id.date_txtView);
        TextView time = (TextView)listItemView.findViewById(R.id.time_txtView);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(quakeItem.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);



        mag.setText(String.valueOf(quakeItem.getMagnitude()));
        date.setText(String.valueOf(quakeItem.getDate()));
        time.setText(String.valueOf(quakeItem.getTime()));
        distance.setText(quakeItem.getDistance());
        location.setText(quakeItem.getLocation());

        return listItemView;

    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
