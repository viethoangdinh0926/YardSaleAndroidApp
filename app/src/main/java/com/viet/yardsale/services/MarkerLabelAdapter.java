package com.viet.yardsale.services;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.viet.yardsale.R;

/**
 * Created by Viet on 6/19/2015.
 */
public class MarkerLabelAdapter implements GoogleMap.InfoWindowAdapter {
    private View markerLabel = null;
    private LayoutInflater inflater=null;


    public MarkerLabelAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (markerLabel == null) {
            markerLabel = inflater.inflate(R.layout.marker_label_layout, null);
        }

        String[] user_info = marker.getTitle().split("\\s+");
        if(user_info.length == 9) {/*
            float scale = ((TextView) markerLabel.findViewById(R.id.phone)).getContext().getResources().getDisplayMetrics().density;
            int textView_height = (int)(30*scale + 0.5f);
            int tetView_width = (int)((Math.max(Math.max(("Open date: " + user_info[0]).length(),("Phone: " + user_info[3]).length()),("Email: " + user_info[4]).length())*10)*scale + 0.5f);
            ((LinearLayout) markerLabel.findViewById(R.id.emailLayout)).setLayoutParams(new LinearLayout.LayoutParams(tetView_width, textView_height));
            ((LinearLayout) markerLabel.findViewById(R.id.phoneLayout)).setLayoutParams(new LinearLayout.LayoutParams(tetView_width, textView_height));
            */

            markerLabel.findViewById(R.id.emailLayout).setVisibility(View.VISIBLE);
            markerLabel.findViewById(R.id.phoneLayout).setVisibility(View.VISIBLE);
            markerLabel.findViewById(R.id.closeTime).setVisibility(View.VISIBLE);
            markerLabel.findViewById(R.id.openTime).setVisibility(View.VISIBLE);
            markerLabel.findViewById(R.id.space33).setVisibility(View.VISIBLE);
            markerLabel.findViewById(R.id.space34).setVisibility(View.VISIBLE);

            ((TextView) markerLabel.findViewById(R.id.openDate)).setText("Open date: " + user_info[0]);
            ((TextView) markerLabel.findViewById(R.id.openTime)).setText("Open time: " + user_info[1]);
            ((TextView) markerLabel.findViewById(R.id.closeTime)).setText("Close time: " + user_info[6]);
            StaticComponents.owner_of_yard_sale = user_info[2];//the owner of this marker
            ((TextView) markerLabel.findViewById(R.id.phone)).setText("Phone: " + user_info[3]);
            ((TextView) markerLabel.findViewById(R.id.email)).setText("Email: " + user_info[4]);

            StaticComponents.number_of_yard_sale_photos = user_info[5];

            StaticComponents.looking_at_yard_sale_latitude = user_info[7];
            StaticComponents.looking_at_yard_sale_longtitude = user_info[8];

            if(!StaticComponents.number_of_yard_sale_photos.equals("0")){
                markerLabel.findViewById(R.id.viewDetailLayout).setVisibility(View.VISIBLE);
            }
            else {
                markerLabel.findViewById(R.id.viewDetailLayout).setVisibility(View.GONE);
            }
        }
        else {
            ((TextView) markerLabel.findViewById(R.id.openDate)).setText(marker.getTitle());
            markerLabel.findViewById(R.id.emailLayout).setVisibility(View.GONE);
            markerLabel.findViewById(R.id.phoneLayout).setVisibility(View.GONE);
            markerLabel.findViewById(R.id.openTime).setVisibility(View.GONE);
            markerLabel.findViewById(R.id.closeTime).setVisibility(View.GONE);
            markerLabel.findViewById(R.id.viewDetailLayout).setVisibility(View.GONE);
            markerLabel.findViewById(R.id.space33).setVisibility(View.GONE);
            markerLabel.findViewById(R.id.space34).setVisibility(View.GONE);
        }


        return markerLabel;
    }


}
