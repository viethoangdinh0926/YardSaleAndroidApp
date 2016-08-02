package com.viet.yardsale.android_php_yardsale;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import com.viet.yardsale.ManageYardSaleActivity;
import com.viet.yardsale.R;
import com.viet.yardsale.services.StaticComponents;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Viet on 6/10/2015.
 */
public class IfUserPostYardSale extends AsyncTask {
    private Activity activity;

    public IfUserPostYardSale(Activity activity){
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {

            try {
                String data = "";
                String link = "";

                String username = (String) params[0];

                link = StaticComponents.dbAdress + "userHasYardSale.php";
                data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

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
        StaticComponents.unfreezeActivity(activity); //finish processing
        if(((String)result).equals("yes")){
            ((ManageYardSaleActivity) activity).findViewById(R.id.bClearLayout).setVisibility(View.VISIBLE);
        }
        else if(((String)result).equals("no")) {
            ((ManageYardSaleActivity) activity).findViewById(R.id.noYardSaleLayout).setVisibility(View.VISIBLE);
        }
        else {
            ((ManageYardSaleActivity) activity).findViewById(R.id.mainErrLayout).setVisibility(View.VISIBLE);
        }
    }
}
