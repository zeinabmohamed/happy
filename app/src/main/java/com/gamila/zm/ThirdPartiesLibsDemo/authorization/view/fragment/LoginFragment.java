package com.gamila.zm.ThirdPartiesLibsDemo.authorization.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gamila.zm.ThirdPartiesLibsDemo.AppConstants;
import com.gamila.zm.ThirdPartiesLibsDemo.R;
import com.gamila.zm.ThirdPartiesLibsDemo.locations.view.activity.HomeActivity;
import com.gamila.zm.ThirdPartiesLibsDemo.util.SharedPreferencesUtil;
import com.gamila.zm.ThirdPartiesLibsDemo.util.ShowMessageUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = LoginFragment.class.getName();
    private static final int RC_SIGN_IN = 1001;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessFBToken;
    private GoogleApiClient mGoogleApiClient;
    private String accessGToken;
    private EditText usernameTxt;
    private EditText passwordTxt;

    private Button loginButtonServer;
    private TextView hintTXT;
    private ProfileTracker mProfileTracker;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        initFBLogin();


        initG_PluseLogin();


        accessFBToken = AccessToken.getCurrentAccessToken();
        accessGToken = SharedPreferencesUtil.getStringPreference(getActivity(),AppConstants.GOOGLE_TOKEN);

        if ((accessFBToken != null && !TextUtils.isEmpty(accessFBToken.getToken()))
        || (accessGToken != null && !TextUtils.isEmpty(accessGToken))
        ||SharedPreferencesUtil.getBooleanPreference(getActivity(),AppConstants.IS_LOGEDIN,false)) {

            startHomeActivity(SharedPreferencesUtil.getStringPreference(getActivity(),AppConstants.USER_NAME));
        }

    }

    private void initG_PluseLogin() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    /***
     * for google pluse login
     *
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        ShowMessageUtil.showSnackBar(getView(), getString(R.string.server_connection_error),
                ContextCompat.getColor(getActivity(),R.color.accent));
    }

    private void initFBLogin() {
        callbackManager = CallbackManager.Factory.create();

        // to keep track if access token changes
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                Log.d(TAG, "onCurrentAccessTokenChanged() called with: " + "oldAccessToken = " +
                        "[" + oldAccessToken + "], currentAccessToken = [" + currentAccessToken + "]");

                accessFBToken = currentAccessToken;
            }
        };

    }

    private void signInByGmail() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void startHomeActivity(String username) {

        SharedPreferencesUtil.setStringPreference(getActivity(),AppConstants.USER_NAME,username);
        startActivity(new Intent(getContext(), HomeActivity.class));
        getActivity().finish();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("public_profile");
        // If using in a fragment
        loginButton.setFragment(this);
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "onSuccess() called with: " + "loginResult = [" + loginResult + "]");
                accessFBToken = loginResult.getAccessToken();
                // login success
                accessTokenTracker.startTracking();

                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            Log.v("facebook - profile", profile2.getFirstName());
                            mProfileTracker.stopTracking();

                            startHomeActivity(profile2.getName());
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());
                    startHomeActivity(Profile.getCurrentProfile().getName());
                }



            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "onCancel() called with: " + "");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "onError() called with: " + "exception = [" + exception + "]");
                ShowMessageUtil.showSnackBar(getView(), getString(R.string.server_connection_error),
                        ContextCompat.getColor(getActivity(),R.color.accent));
            }
        });


        view.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInByGmail();

            }
        });


        hintTXT = (TextView)view.findViewById(R.id.hintTXT);
        hintTXT.setText(Html.fromHtml(getString(R.string.login_hint)));

        usernameTxt = (EditText)view.findViewById(R.id.usernameTxt);
        passwordTxt = (EditText)view.findViewById(R.id.passwordTxt);
        loginButtonServer = (Button)view.findViewById(R.id.loginButtonServer);
        loginButtonServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithServer();
            }
        });



        return view;
    }

    private void loginWithServer() {
        // we here call api service to authenticate the user
        // after validation

        if(!TextUtils.isEmpty(usernameTxt.getText().toString())
                && usernameTxt.getText().toString().equals("admin")){


            if(!TextUtils.isEmpty(passwordTxt.getText().toString())
                    && passwordTxt.getText().toString().equals("123456")){

                SharedPreferencesUtil.setBooleanPreference(getActivity(),
                        AppConstants.IS_LOGEDIN,true);

                startHomeActivity(usernameTxt.getText().toString());

            }else {
                ShowMessageUtil.showSnackBar(getView(),getString(R.string.password_error),Color.RED);

            }
        }else {
            ShowMessageUtil.showSnackBar(getView(),getString(R.string.username_error),Color.RED);
        }
    }


    // to retrieve data after get login access token
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


           /* mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);*/
            SharedPreferencesUtil.setStringPreference(getActivity(), AppConstants.GOOGLE_TOKEN,
                    acct.getId());
            startHomeActivity(acct.getDisplayName());
        } else {
            // Signed out, show unauthenticated UI.
           /* updateUI(false);*/
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }


}
