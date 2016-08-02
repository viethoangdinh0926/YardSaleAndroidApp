package com.viet.yardsale.android_php.yardsale;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import com.viet.yardsale.MainActivity;
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
public class CheckAppVersion extends AsyncTask {
    private Activity activity;

    public CheckAppVersion(Activity activity){
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            String data = "";
            String link = "";

            String version = (String) params[0];

            link = StaticComponents.dbAdress + "checkVersion.php";
            data += "&" + URLEncoder.encode("version", "UTF-8") + "=" + URLEncoder.encode(version, "UTF-8");

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
    protected void onPostExecute(Object result) {
        StaticComponents.alreadyCheckAppVersion = true;
        StaticComponents.unfreezeActivity(activity); //finish processing
        if (((String) result).equals("0")) {
            StaticComponents.showAds = false;
            ((MainActivity) activity).findViewById(R.id.bLoginLayout).setVisibility(View.VISIBLE);
        } else if (((String) result).equals("1")) {
            StaticComponents.showAds = true;
            ((MainActivity)activity).findViewById(R.id.adLayout).setVisibility(View.VISIBLE);
            ((MainActivity) activity).findViewById(R.id.bLoginLayout).setVisibility(View.VISIBLE);
        } else if (((String) result).equals("not ok")) {
            ((MainActivity) activity).findViewById(R.id.updateAppLayout).setVisibility(View.VISIBLE);
        } else {//lose internet connection
            ((MainActivity) activity).findViewById(R.id.checkInternetLayout).setVisibility(View.VISIBLE);
            StaticComponents.alreadyCheckAppVersion = false;
        }
    }
}
