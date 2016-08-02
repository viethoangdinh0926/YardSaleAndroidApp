package com.viet.yardsale.android_php_yardsale;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import com.viet.yardsale.search_yardsale_operations.SearchYardSalesActivity;
import com.viet.yardsale.MapsActivity;
import com.viet.yardsale.R;
import com.viet.yardsale.services.StaticComponents;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Viet on 6/6/2015.
 */
public class GetAvailableYardSalesInformation extends AsyncTask {
    private Activity activity;

    public GetAvailableYardSalesInformation(Activity activity){
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {


            try {
                String data = "";
                String link = "";

                String latitude = (String) params[0];
                String longtitude = (String) params[1];
                String username = (String) params[2];
                String range = (String) params[3];

                link = StaticComponents.dbAdress + "getUserLocations.php";
                data = URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8");
                data += "&" + URLEncoder.encode("longtitude", "UTF-8") + "=" + URLEncoder.encode(longtitude, "UTF-8");
                data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("range", "UTF-8") + "=" + URLEncoder.encode(range, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            } catch (Exception e) {
                //return new String("Exception: " + e.getMessage());
                return "Please check internet connection.";
            }
        }



    @Override
    protected void onPostExecute(Object result){

        StaticComponents.unfreezeActivity(activity);//release the activity

        if(((String)result).equals("No yard sale found")||((String)result).equals("Please check internet connection.")){
            ((TextView) ((SearchYardSalesActivity) activity).findViewById(R.id.errMessage)).setText((String) result);
        }
        else {
            StaticComponents.returned_yard_sales_information = (String)result;
            ((TextView) ((SearchYardSalesActivity) activity).findViewById(R.id.errMessage)).setText("");
            Intent intent = new Intent(activity, MapsActivity.class);
            activity.startActivity(intent);
        }

    }
}
