package com.japri.amierul.icarasia;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

public abstract class BasePagerFragment extends Fragment {

    public void switchHomeFragment(String email){
        ((MainActivity)getActivity()).switchHomeFragment(email);
    }



    public void showSnackBar(String string) {
        Snackbar.make(getView() == null ? getActivity().getWindow().getDecorView(): getView(),
                string,
                Snackbar.LENGTH_SHORT)
                .show();
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

    public void showProgressDialog() {
        ((MainActivity)getActivity()).showProgressDialog();
    }

    public void hideProgressDialog() {
        ((MainActivity)getActivity()).hideProgressDialog();
    }
}
