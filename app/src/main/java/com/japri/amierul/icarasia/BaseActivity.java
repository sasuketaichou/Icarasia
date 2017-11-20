package com.japri.amierul.icarasia;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;


public abstract class BaseActivity  extends AppCompatActivity {

    private LoginEngine loginEngine;
    private ProgressDialog mProgressDialog;

    public void setTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    public void addFragment(Fragment fragment){
        switchFragment(fragment,false);
    }

    public void switchFragment(Fragment fragment){
        switchFragment(fragment,true);
    }

    private void switchFragment(Fragment fragment,boolean addToBackStack){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_root,fragment);

        if(addToBackStack){
            transaction.addToBackStack(null);
        }

        transaction.commit();

    }

    public void cleanBackStack(){
        //clear
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void cleanSwitchFragment(Fragment fragment){
        cleanBackStack();
        //add
        addFragment(fragment);
    }

    public void switchBackFragment(){

        getSupportFragmentManager()
                .popBackStackImmediate();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void switchHomeFragment(String id){

        //set tag
        setHasLogin(id);
        cleanBackStack();

        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeFragment fragment = (HomeFragment) fragmentManager.findFragmentByTag(HomeFragment.TAG);

        if(fragment == null){
            fragment = HomeFragment.newInstance(id);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_root,fragment,HomeFragment.TAG);
        transaction.commit();
    }

    public void setHasLogin(String id){
        if(loginEngine == null){
            loginEngine = new LoginEngine();
        }
        loginEngine.setLoginTag(id);
    }

    public void resetHasLogin() {
        if(loginEngine != null){
            loginEngine.setLoginTag("");
        }
    }

    public String getTagLogin(){
        return loginEngine == null ? null:loginEngine.getLoginTag();
    }

    public void initialView(){

        //clear
        resetHasLogin();

        ImageView splashView = new ImageView(this);
        splashView.setScaleType(ImageView.ScaleType.FIT_XY);
        splashView.setImageResource(R.drawable.blue);
        setContentView(splashView);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                initView();
            }

        }, 3000);
    }

    public void initView(){
        initContent();
        initLogin();
    }

    private void initContent(){
        setContentView(R.layout.activity_main);
        initActionBar();
    }

    public void initHome(String id){
        initContent();
        switchHomeFragment(id);
    }

    protected void initActionBar(){
        Toolbar toolbar = findViewById(R.id.layout_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initLogin() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        LoginFragment fragment = (LoginFragment) fragmentManager.findFragmentByTag(LoginFragment.TAG);

        if(fragment == null){
            fragment = LoginFragment.newInstance();
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_root,fragment,LoginFragment.TAG);
        transaction.commit();
    }


}
