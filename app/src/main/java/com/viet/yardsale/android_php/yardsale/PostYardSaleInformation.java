package com.viet.yardsale.android_php.yardsale;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.viet.yardsale.post_yardsale_operations.PostYardSaleMainActivity;
import com.viet.yardsale.R;
import com.viet.yardsale.post_yardsale_operations.SuccessfulPostBasicYardSaleActivity;
import com.viet.yardsale.services.StaticComponents;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Viet on 6/5/2015.
 */
public class PostYardSaleInformation extends AsyncTask {
    private Activity activity;

    public PostYardSaleInformation(Activity activity){
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String data = "";
            String link = "";

            String latitude = (String) params[0];
            String longtitude = (String) params[1];
            String request = (String) params[2];
            String phone = (String) params[3];
            String username = (String) params[4];
            String expire = (String) params[5];
            String openHr = (String) params[6];
            String showEmail = (String) params[7];

            link = StaticComponents.dbAdress + "/updateLocation.php";
            data = URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8");
            data += "&" + URLEncoder.encode("longtitude", "UTF-8") + "=" + URLEncoder.encode(longtitude, "UTF-8");
            data += "&" + URLEncoder.encode("request", "UTF-8") + "=" + URLEncoder.encode(request, "UTF-8");
            data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
            data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("expire", "UTF-8") + "=" + URLEncoder.encode(expire, "UTF-8");
            data += "&" + URLEncoder.encode("openHr", "UTF-8") + "=" + URLEncoder.encode(openHr, "UTF-8");
            data += "&" + URLEncoder.encode("showEmail", "UTF-8") + "=" + URLEncoder.encode(showEmail, "UTF-8");

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
        StaticComponents.unfreezeActivity(activity);//release the activity after finish processing

        if(((String)result).equals("ok")){
            if(((PostYardSaleMainActivity) activity).ls.mGoogleApiClient.isConnected()) {
                ((PostYardSaleMainActivity) activity).ls.mGoogleApiClient.disconnect();
            }
            Intent intent = new Intent(activity, SuccessfulPostBasicYardSaleActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
        else {
            ((TextView) ((PostYardSaleMainActivity) activity).findViewById(R.id.errMessage)).setText((String)result);
            ((PostYardSaleMainActivity)activity).findViewById(R.id.errLayout).setVisibility(View.VISIBLE);
            ((PostYardSaleMainActivity)activity).findViewById(R.id.space66).setVisibility(View.VISIBLE);
        }
    }
}
