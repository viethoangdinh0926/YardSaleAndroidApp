package com.viet.yardsale.android_php_yardsale;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by Viet on 5/29/2015.
 */

    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.io.OutputStreamWriter;
    import java.net.URL;
    import java.net.URLConnection;
    import java.net.URLEncoder;

    import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.viet.yardsale.ConfirmSignupActivity;
import com.viet.yardsale.MainActivity;
import com.viet.yardsale.R;
import com.viet.yardsale.SignUpActivity;
import com.viet.yardsale.services.StaticComponents;

public class SignupLoginOperations extends AsyncTask {
        private Activity activity;
        private String refUser;
        private int queryType;


        //flag 0 means get and 1 means post.(By default it is get.)
        public SignupLoginOperations(Activity activity, int queryType) {
            this.activity = activity;
            this.queryType = queryType;
            refUser = null;
            //this.statusField = statusField;
            //this.roleField = roleField;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try{
                String data = "";
                String link = "";
                if(queryType == 0) {//Log in
                    String username = (String) params[0];
                    refUser = username;
                    String password = (String) params[1];

                    link = StaticComponents.dbAdress + "signin.php";
                    data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                }
                else if(queryType == 1){//check if username is available
                    String username = (String) params[0];
                    link = StaticComponents.dbAdress + "checkUsername.php";
                    data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                }
                else if(queryType == 2){//sign up
                    String username = (String) params[0];
                    refUser = username;
                    String password = (String) params[1];
                    String cpassword = (String) params[2];
                    String email = (String) params[3];
                    StaticComponents.signup_or_forget_password_user_email = email;

                    link = StaticComponents.dbAdress + "signup.php";
                    data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("cpassword", "UTF-8") + "=" + URLEncoder.encode(cpassword, "UTF-8");
                    data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                }

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }

                return sb.toString();
            }
            catch(Exception e){
                //return new String("Exception: " + e.getMessage());
                return "Please check internet connection.";
            }
        }


        @Override
        protected void onPostExecute(Object result){
            StaticComponents.unfreezeActivity(activity);
            if(activity instanceof MainActivity) {//login operation
                //hide keyboard
                InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText)activity.findViewById(R.id.userName)).getWindowToken(), 0);
                imm.hideSoftInputFromWindow(((EditText) activity.findViewById(R.id.password)).getWindowToken(), 0);

                if(((String)result).equals(refUser)){//login successfully
                    StaticComponents.currentUser = refUser;//set user for the app
                    activity.findViewById(R.id.bLoginLayout).setVisibility(View.INVISIBLE);
                    activity.findViewById(R.id.aLoginLayout).setVisibility(View.VISIBLE);
                    ((MainActivity)activity).findViewById(R.id.signin_err_layout).setVisibility(View.INVISIBLE);
                }
                else {
                    ((TextView)((MainActivity) activity).findViewById(R.id.err)).setText((String)result);
                    ((MainActivity)activity).findViewById(R.id.signin_err_layout).setVisibility(View.VISIBLE);
                }
            }
            else if(activity instanceof SignUpActivity){
                if(((String)result).equals("Ok")){//if the username is available
                    ((SignUpActivity)activity).getCheckUserAvailableTextView().setText("ok");
                    ((TextView)((SignUpActivity) activity).findViewById(R.id.notice)).setText("");
                }
                else if(((String)result).equals("Not Ok")){//the username is not available
                    ((SignUpActivity)activity).getCheckUserAvailableTextView().setText("not ok");
                    ((TextView)((SignUpActivity) activity).findViewById(R.id.notice)).setText("");
                }
                else if(((String)result).equals(refUser)){//when sign up successful

                    Intent intent = new Intent(activity, ConfirmSignupActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                    //user = refUser;
                }
                else {//sign up fail
                    ((TextView)((SignUpActivity) activity).findViewById(R.id.notice)).setText((String)result);
                }
            }
        }


}
