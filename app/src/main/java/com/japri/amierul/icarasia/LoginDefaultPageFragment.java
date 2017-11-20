package com.japri.amierul.icarasia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginDefaultPageFragment extends BasePagerFragment implements View.OnClickListener {

    private EditText username,password;
    private TextView tv_error;

    private RealmEngine realmEngine;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realmEngine = new RealmEngine(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pager_login,container,false);

        username = view.findViewById(R.id.input_username);
        password = view.findViewById(R.id.input_password);

        tv_error = view.findViewById(R.id.tv_login_error);

        Button buttonLogin = view.findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(this);

        return view;
    }

    public LoginDefaultPageFragment(){}

    public static LoginDefaultPageFragment newInstance(){
        LoginDefaultPageFragment fragment = new LoginDefaultPageFragment();
        return fragment;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_login:
                validate();
                break;
        }

    }

    private void validate() {
        if(validateForm()){

            if(tv_error.getVisibility() == View.VISIBLE){
                tv_error.setVisibility(View.GONE);
            }

            showProgressDialog();

            //check with db
            UserModel userModel = realmEngine.checkIfExist(username.getText().toString());
            if(userModel != null){

                switchHomeFragment(userModel.getEmail());
                hideProgressDialog();
            } else {
                hideProgressDialog();
                if(tv_error.getVisibility() != View.VISIBLE){
                    tv_error.setVisibility(View.VISIBLE);
                }
                String loginError = getString(R.string.email_error)+"\n"+getString(R.string.password_error);
                tv_error.setText(loginError);
            }
        } else {
            showSnackBar(getString(R.string.complete_form));
        }
    }

    private boolean validateForm() {

        boolean notEmpty = !ValidUtil.isEmpty(username);
        notEmpty &= !ValidUtil.isEmpty(password);

        return notEmpty;
    }

}
