package com.thetutors.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.thetutors.R;
import com.thetutors.controller.DatabaseHelper;
import com.thetutors.controller.VariabelConfig;
import com.thetutors.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class AuthActivity extends Activity {
    //List<String> permissionNeeds= Arrays.asList("user_photos","friends_photos", "email", "user_birthday", "user_friends");

    //facebook callbacks manager
    private CallbackManager mCallbackManager;
    private LoginButton mFbLoginButton;
    public String username="";
    GraphRequest request;
    VariabelConfig vg = VariabelConfig.getInstance();
    JSONObject jsonObject;
    Intent i;
    SharedPreferences sharedpreferences;
    DatabaseHelper dh;

    Button signIn;
    EditText editTextUsername, editTextPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_auth);

        dh = new DatabaseHelper(this.getApplicationContext());
        insertUserDB();

        i = new Intent(AuthActivity.this, WelcomeMessage.class);

        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);

        signIn = (Button) findViewById(R.id.signin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = editTextUsername.getText().toString();
                String inputPassword = editTextPassword.getText().toString();

                User user;
                if((user = dh.getUser(inputUsername)) != null){
                    if(user.getPassword().equalsIgnoreCase(inputPassword)){
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Wrong Username/Password", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong Username/Password", Toast.LENGTH_LONG).show();
                }

            }
        });

        sharedpreferences = getSharedPreferences("Auth", Context.MODE_PRIVATE);

        mFbLoginButton=(LoginButton)findViewById(R.id.facebook_login_button);

        if(!sharedpreferences.getString("username", "").equalsIgnoreCase("")){
            i = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        mFbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken.getCurrentAccessToken();

                request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.e("LoginActivity", response.toString());
                            jsonObject = response.getJSONObject();
                            // Application code
                            try {
                                username = object.getString("name");
                                Log.e("Username 1", username);
                                vg.setUsername(username);

                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString("username", username);
                                editor.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "name");
                request.setParameters(parameters);
                request.executeAsync();

                startActivity(i);
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        //Then launch OnSuccess method
    }

    public void insertUserDB(){
        User user = new User();
        user.setUsername("");
        user.setPassword("");

        try {
            dh.createUser(user);
        }
        catch(SQLiteConstraintException e){
            e.printStackTrace();
        }
    }
}

