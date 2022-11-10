package com.example.librarymanagement;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.librarymanagement.dao.AdminDAO;
import com.example.librarymanagement.service.LoginTestService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    IntentFilter intentFilter;
    LoginButton btnFacebook;
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton btnGoogle;
    EditText edtUser, edtPass;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        EditText edtUser = findViewById(R.id.edtUser);
        EditText edtPass = findViewById(R.id.edtPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        intentFilter = new IntentFilter();
        intentFilter.addAction("checkLogin");
        intentFilter.addAction("");
        AdminDAO adminDAO = new AdminDAO(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUser.getText().toString();
                String pass = edtPass.getText().toString();
                    Intent intent = new Intent(LoginActivity.this, LoginTestService.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user", user);
                    bundle.putString("pass", pass);
                    intent.putExtras(bundle);
                    startService(intent);
            }
        });


        btnFacebook = findViewById(R.id.btnFacebook);
        callbackManager = CallbackManager.Factory.create();

        btnGoogle = findViewById(R.id.btnGoogle);

        // check login or not
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            getUserProfile(accessToken);
            Toast.makeText(this, "Đã đăng nhập", Toast.LENGTH_SHORT).show();
        }
                //sign in Facebook button
                btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getUserProfile(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Hủy đăng nhập", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "Lỗi trong quá trình đăng nhập", Toast.LENGTH_SHORT).show();
                    }
                });
                //sign in Google button
                btnGoogle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        checkLoginGoogle.launch(signInIntent);
                    }
                });


        //sign in with Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myBroadcast, intentFilter);
    }

    public BroadcastReceiver myBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case "checkLogin":
                    Bundle bundle = intent.getExtras();
                    boolean check = bundle.getBoolean("check");
                    if(check){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {
                        Toast.makeText(context, "Sign in failed!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

        //google
        ActivityResultLauncher<Intent> checkLoginGoogle = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            try {
                                //đăng nhập thành công
                                GoogleSignInAccount account = task.getResult(ApiException.class);
                                String displayName = account.getDisplayName();
                                String email = account.getEmail();
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công - " + displayName + " - " + email, Toast.LENGTH_SHORT).show();
                                btnGoogle.setVisibility(View.GONE);
                            } catch (ApiException e) {
                                //đăng nhập thất bại
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


        //facebook login
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }

        private void getUserProfile(AccessToken accessToken) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        String name = object.getString("name");
                        String email = object.getString("id");
                        String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                        Toast.makeText(LoginActivity.this, name + " - " + email + " - " + image, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,picture.width(200)");
            request.setParameters(parameters);
            request.executeAsync();
    }
    // sign in Google or not
    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Toast.makeText(this, "Have signed in Google!" + account.getEmail(), Toast.LENGTH_SHORT).show();
            btnGoogle.setVisibility(View.GONE);
        }
    }



}