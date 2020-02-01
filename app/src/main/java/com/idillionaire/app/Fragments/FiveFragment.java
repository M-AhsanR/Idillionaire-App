package com.idillionaire.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.idillionaire.app.Activities.ChangePassword;
import com.idillionaire.app.Activities.EditProfile;
import com.idillionaire.app.Activities.GetStarted;
import com.idillionaire.app.Activities.PrivacyPolicy;
import com.idillionaire.app.Activities.ReportProblem;
import com.idillionaire.app.Activities.SplashActivity;
import com.idillionaire.app.Activities.TermstoUse;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FiveFragment extends Fragment {

    TextView general, options, shareapp, rateus, account, edit_profile, change_pass,
            settings, push_notification, support, reportProblem, about, link, privacyPolicy,
            termstoUse, followUs, facebook, twitter, instagram, signout;

    SharedPreferences sp;

    String fblink;
    String twitterlink;
    String instalink;

    LinearLayout login_visibility;
    LinearLayout login_visibility_line;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_five, container, false);

        general = (TextView) view.findViewById(R.id.general);
        options = (TextView) view.findViewById(R.id.options);
        shareapp = (TextView) view.findViewById(R.id.shareApp);
        rateus = (TextView) view.findViewById(R.id.rateUs);
        account = (TextView) view.findViewById(R.id.account);
        edit_profile = (TextView) view.findViewById(R.id.editProfile);
        change_pass = (TextView) view.findViewById(R.id.changePass);
        settings = (TextView) view.findViewById(R.id.settings);
        push_notification = (TextView) view.findViewById(R.id.notifications);
        support = (TextView) view.findViewById(R.id.support);
        reportProblem = (TextView) view.findViewById(R.id.reportProblem);
        about = (TextView) view.findViewById(R.id.about);
        link = (TextView) view.findViewById(R.id.link);
        privacyPolicy = (TextView) view.findViewById(R.id.privacyPolicy);
        termstoUse = (TextView) view.findViewById(R.id.termstoUse);
        followUs = (TextView) view.findViewById(R.id.followUs);
        facebook = (TextView) view.findViewById(R.id.facebook);
        twitter = (TextView) view.findViewById(R.id.twitter);
        instagram = (TextView) view.findViewById(R.id.instagram);
        signout = (TextView) view.findViewById(R.id.signout);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaLTStd-Roman.otf");

        general.setTypeface(font);
        options.setTypeface(font);
        shareapp.setTypeface(font);
        rateus.setTypeface(font);
        account.setTypeface(font);
        edit_profile.setTypeface(font);
        change_pass.setTypeface(font);
        settings.setTypeface(font);
        push_notification.setTypeface(font);
        support.setTypeface(font);
        reportProblem.setTypeface(font);
        about.setTypeface(font);
        link.setTypeface(font);
        privacyPolicy.setTypeface(font);
        termstoUse.setTypeface(font);
        followUs.setTypeface(font);
        facebook.setTypeface(font);
        twitter.setTypeface(font);
        instagram.setTypeface(font);
        signout.setTypeface(font);

        login_visibility = view.findViewById(R.id.login_visibility);
        login_visibility_line = view.findViewById(R.id.login_visibility_line);

        sp = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String login = sp.getString("LOGIN", "");

        if (login.equals("social")) {
            login_visibility.setVisibility(View.GONE);
            login_visibility_line.setVisibility(View.GONE);
        } else {
            login_visibility.setVisibility(View.VISIBLE);
            login_visibility_line.setVisibility(View.VISIBLE);
        }


        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.idillionaire.app")));
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editprofileintent = new Intent(getActivity(), EditProfile.class);
                startActivity(editprofileintent);
            }
        });

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editprofileintent = new Intent(getActivity(), ChangePassword.class);
                startActivity(editprofileintent);
            }
        });

        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Idillionaire \n" + "Download the Idillionaire App for more inspiration! \n" + "\n" + "https://play.google.com/store/apps/details?id=com.idillionaire.app");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share the App with"));






            }
        });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
                sp.edit().putBoolean("logged", false).apply();
                sp = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = sp.edit();
                mEditor.putString("USER_EMAIL", "");
                mEditor.putString("USER_PASSWORD", "");
                mEditor.putString("USER_TOKEN", "");
                mEditor.putString("USER_ID", "");
                mEditor.putString("NAME", "");
                mEditor.putBoolean("SUBSCRIBED", false);
                mEditor.putString("FAVORITELIST", "");
                mEditor.putString("GALLERYLIST", "");
                mEditor.putString("LOGIN", "");
                mEditor.putString("LOGIN_TYPE", "");
                mEditor.putString("SESSION_ID", "");
                mEditor.apply();
                sp = getContext().getSharedPreferences("SCHEDULE_ARR", MODE_PRIVATE);
                sp.edit().putString("schedule_array", "").apply();

                Intent intent = new Intent(getContext(), GetStarted.class);
                startActivity(intent);
                getActivity().finish();


                SharedPreferences mprefs = getContext().getSharedPreferences("USER_DATA", MODE_PRIVATE);
                String USER_TOKEN = mprefs.getString("USER_TOKEN", "");

                Map<String, String> postpram = new HashMap<String, String>();

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-sh-auth", USER_TOKEN);

                ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.LOGOUT, getActivity(), postpram, headers, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String ERROR) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(String.valueOf(result));
                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
        reportProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReportProblem.class);
                startActivity(intent);
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webview = new Intent(Intent.ACTION_VIEW);
                webview.setData(Uri.parse("http://www.idillionaire.com"));
                startActivity(webview);
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), PrivacyPolicy.class);
//                startActivity(intent);
                Intent webview = new Intent(Intent.ACTION_VIEW);
                webview.setData(Uri.parse("http://idillionaire.com/privacy-policy.html"));
                startActivity(webview);
            }
        });
        termstoUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TermstoUse.class);
                startActivity(intent);
            }
        });
        fblink = SplashActivity.fblink;
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent webview = new Intent(Intent.ACTION_VIEW);
                webview.setData(Uri.parse("https://www.facebook.com/" + fblink + "/"));
                startActivity(webview);
            }
        });
        twitterlink = SplashActivity.twitterlink;
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent webview = new Intent(Intent.ACTION_VIEW);
                webview.setData(Uri.parse("https://twitter.com/" + twitterlink));
                startActivity(webview);
            }
        });
        instalink = SplashActivity.instalink;
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent webview = new Intent(Intent.ACTION_VIEW);
                webview.setData(Uri.parse("https://www.instagram.com/" + instalink));
                startActivity(webview);
            }
        });

        return view;
    }

}
