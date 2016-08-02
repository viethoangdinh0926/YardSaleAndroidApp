package com.viet.yardsale.android_php_yardsale;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.viet.yardsale.R;
import com.viet.yardsale.search_yardsale_operations.YardSalePreviewActivity;
import com.viet.yardsale.services.StaticComponents;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Viet on 6/25/2015.
 */
public class ReportAYardSale extends AsyncTask {
    private Activity activity;

    public ReportAYardSale(Activity activity){
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            String data = "";
            String link = "";

            String reporter = (String) params[0];
            String reportedUser = (String) params[1];

            link = StaticComponents.dbAdress + "reportAYardSale.php";
            data += "&" + URLEncoder.encode("reporter", "UTF-8") + "=" + URLEncoder.encode(reporter, "UTF-8");
            data += "&" + URLEncoder.encode("reportedUser", "UTF-8") + "=" + URLEncoder.encode(reportedUser, "UTF-8");


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
            return "Please check internet connection.";
        }
    }

    @Override
    protected void onPostExecute(Object result){
        StaticComponents.unfreezeActivity(activity);//release the activity after finish processing
        String tempResult = (String)result;
        if(tempResult.equals("You cannot report a Yard Sale twice.") || tempResult.equals("Thank you for your report")){
            ((TextView)((YardSalePreviewActivity) activity).findViewById(R.id.reportBtt)).setEnabled(false);
        }
        ((TextView)activity.findViewById(R.id.resultReport)).setText(tempResult);
        activity.findViewById(R.id.resultReportLayout).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.space65).setVisibility(View.VISIBLE);
    }
}