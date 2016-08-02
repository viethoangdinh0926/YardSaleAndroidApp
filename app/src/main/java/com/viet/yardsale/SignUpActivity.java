package com.viet.yardsale;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.viet.yardsale.android_php_yardsale.SignupLoginOperations;
import com.viet.yardsale.services.StaticComponents;


public class SignUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkUserName(View view){
        String tempUsername = ((EditText)findViewById(R.id.username)).getText().toString();
        if(tempUsername.length() > 5 && tempUsername.length() < 21) {
            SignupLoginOperations connector = new SignupLoginOperations(this, 1);
            connector.execute(((EditText) findViewById(R.id.username)).getText().toString());
            StaticComponents.freezeActivity(this);//suspending the activity
        }
        else {
            ((TextView)findViewById(R.id.notice)).setText("User name's length must be between 5 and 21");
        }
    }

    public void signUp(View view){
        String un = ((EditText)findViewById(R.id.username)).getText().toString();
        String pw = ((EditText)findViewById(R.id.password)).getText().toString();
        String cpw = ((EditText)findViewById(R.id.cpassword)).getText().toString();
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        if(un.length() > 5 && un.length() < 21) {
            if (pw.length() > 5 && pw.length() < 16) {
                if(pw.equals(cpw)){
                    if (StaticComponents.isValidEmail(email)){
                        SignupLoginOperations connector = new SignupLoginOperations(this, 2);
                        connector.execute(un, pw, cpw, email);
                        StaticComponents.freezeActivity(this);//suspending the activity
                    }
                    else {
                        ((TextView)findViewById(R.id.notice)).setText("Invalid email address");
                    }
                }
                else {
                    ((TextView)findViewById(R.id.notice)).setText("Password and confirm password do not match");
                }
            }
            else {
                ((TextView)findViewById(R.id.notice)).setText("Password's length must be between 5 and 16");
            }
        }
        else {
            ((TextView)findViewById(R.id.notice)).setText("User name's length must be between 5 and 21");
        }
    }

    public TextView getCheckUserAvailableTextView(){
        return (TextView)findViewById(R.id.checkmsg);
    }
}
