package com.dream.facebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class CustomFacebokLogInActivity extends AppCompatActivity {


    private LoginButton loginButton;
    private CallbackManager callbackManager;
    ImageView imageView;
    Button fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_facebok_log_in);
        callbackManager = CallbackManager.Factory.create();
        //The original Facebook button
        final LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);

//Our custom Facebook button
        fb = (Button) findViewById(R.id.fb);


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Main", response.toString());
                                setProfileToView(object);
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }


            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onClickFacebookButton(View view) {
        if (view == fb) {
            loginButton.performClick();
        }

    }

    private void setProfileToView(JSONObject jsonObject) {
        try {
            Log.d("FACEBOOK" , "PROFILE"+jsonObject.toString());
            Log.d("FACEBOOK" , "PROFILE"+jsonObject.getString("email"));
            Log.d("FACEBOOK" , "PROFILE"+jsonObject.getString("name"));
            Log.d("FACEBOOK" , "PROFILE"+jsonObject.getString("id"));
            //imageView.setImageBitmap(getFacebookProfilePicture(jsonObject.getString("id")));

            String link = "https://graph.facebook.com/914131568783715/picture?type=large";

            Glide.with(getApplicationContext()).load(link).into(imageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CODE" , ""+requestCode);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
