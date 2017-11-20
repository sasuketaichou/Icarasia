package com.japri.amierul.icarasia;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setTitle(String title){
        ((MainActivity)getActivity()).setTitle(title);
    }

    public void switchFragment(Fragment fragment){
        ((MainActivity)getActivity()).switchFragment(fragment);
    }

    public void switchBackFragment(){
        ((MainActivity)getActivity()).switchBackFragment();
    }

    public void cleanSwitchFragment(Fragment fragment){
        ((MainActivity)getActivity()).cleanSwitchFragment(fragment);
    }


    public void showProgressDialog() {
        ((MainActivity)getActivity()).showProgressDialog();
    }

    public void hideProgressDialog() {
        ((MainActivity)getActivity()).hideProgressDialog();
    }

    public void switchLogout(){
        ((MainActivity)getActivity()).cleanBackStack();
        ((MainActivity)getActivity()).initialView();
    }

    public void showErrorText(TextView errView, String errMessage){

        boolean notVisible = errView.getVisibility() != View.VISIBLE;

        if(notVisible){
            errView.setVisibility(View.VISIBLE);
        }

        errView.setText(errMessage);
    }

    public void hideErrorText(TextView errView){
        errView.setVisibility(View.GONE);
    }
}
