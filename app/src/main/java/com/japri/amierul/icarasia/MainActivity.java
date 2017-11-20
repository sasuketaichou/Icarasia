package com.japri.amierul.icarasia;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    private final static String SPLASH_TAG = "splash.tag";
    private final static String LOGIN_TAG = "login.tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(LOGIN_TAG)){
                initHome(savedInstanceState.getString(LOGIN_TAG));
            } else if(savedInstanceState.containsKey(SPLASH_TAG)){
                initView();
            }
        } else {
            initialView();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        //save state
        outState.putString(SPLASH_TAG,"splash");

        //save login
        if(!ValidUtil.isEmpty(getTagLogin())){
            outState.putString(LOGIN_TAG,getTagLogin());
        }

        super.onSaveInstanceState(outState);
    }

}
