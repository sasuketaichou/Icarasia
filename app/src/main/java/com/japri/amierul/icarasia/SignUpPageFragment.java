package com.japri.amierul.icarasia;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class SignUpPageFragment extends BasePagerFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText etEmail, etPassword, etFirstName, etLastName, etMobile;
    private TextView tvEmailError, tvPasswordError, tvMobileError;

    private RealmEngine realmEngine;
    private String userTypeString;

    private final static String TAG = "SignUpPageFragment";

    private final static String SI_EMAIL = "email.outstate";
    private final static String SI_PASSWORD = "password.outstate";
    private final static String SI_FIRST = "first.outstate";
    private final static String SI_LAST = "last.outstate";
    private final static String SI_MOBILE = "mobile.outstate";

    public SignUpPageFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realmEngine = new RealmEngine(getContext());
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pager_signup,container,false);

        LinearLayout llEmail = view.findViewById(R.id.ll_email);
        LinearLayout llPassword = view.findViewById(R.id.ll_password);
        LinearLayout llFirstName = view.findViewById(R.id.ll_firstName);
        LinearLayout llLastName = view.findViewById(R.id.ll_lastName);
        LinearLayout llMobile = view.findViewById(R.id.ll_mobile);
        LinearLayout llUserType = view.findViewById(R.id.ll_userType);

        etEmail = llEmail.findViewById(R.id.et_value);
        etPassword = llPassword.findViewById(R.id.et_value);
        etFirstName = llFirstName.findViewById(R.id.et_value);
        etLastName = llLastName.findViewById(R.id.et_value);
        etMobile = llMobile.findViewById(R.id.et_value);

        tvEmailError = llEmail.findViewById(R.id.tv_error);
        tvPasswordError= llPassword.findViewById(R.id.tv_error);
        tvMobileError = llMobile.findViewById(R.id.tv_error);

        etEmail.setHint(getString(R.string.email_hint));
        etPassword.setHint(getString(R.string.password_hint));
        etFirstName.setHint(getString(R.string.first_name));
        etLastName.setHint(getString(R.string.last_name));
        etMobile.setHint(getString(R.string.mobile_hint));

        Button buttonSignup = view.findViewById(R.id.btn_signup);
        buttonSignup.setOnClickListener(this);

        Spinner spUserType = llUserType.findViewById(R.id.sp_userType);
        spUserType.setOnItemSelectedListener(this);

        ArrayAdapter<String> spAdapter = initAdapter();

        spAdapter.addAll(loadUserType());
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUserType.setAdapter(spAdapter);
        spUserType.setSelection(spAdapter.getCount());

        return view;
    }

    private ArrayAdapter<String> initAdapter(){

        return new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)view.findViewById(android.R.id.text1)).setText("");
                    ((TextView)view.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return view;
            }

            @Override
            public int getCount() {
                return super.getCount()-1;
            }
        };
    }

    private List<String> loadUserType() {

        List<String> userType = new ArrayList<>();
        userType.add(getString(R.string.ut_Broker));
        userType.add(getString(R.string.ut_Agent));
        userType.add(getString(R.string.ut_Dealer));
        userType.add(getString(R.string.ut_Private));
        //initial
        userType.add(getString(R.string.user_type));

        return userType;
    }

    public static SignUpPageFragment newInstance(){
        SignUpPageFragment fragment = new SignUpPageFragment();
        return fragment;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_signup:
                validate();
                break;
        }

    }

    private void validate() {

        if(validateForm()){

            showProgressDialog();

            //check if user exist
            UserModel exist = realmEngine.checkIfExist(etEmail.getText().toString());

            if(exist != null){

                //show error
                hideProgressDialog();
                showErrorText(tvEmailError,getString(R.string.email_exist));

            } else {
                //proceed to save to db
                final UserModel form = new UserModel();
                form.setEmail(etEmail.getText().toString());
                form.setPassword(etPassword.getText().toString());
                form.setMobile(etMobile.getText().toString());
                form.setUserType(userTypeString);
                form.setFirstName(etFirstName.getText().toString());
                form.setLastName(etLastName.getText().toString());

                realmEngine.createUserAcc(form, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                        hideProgressDialog();
                        //reset
                        resetField();
                        //proceed
                        switchHomeFragment(form.getEmail());

                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {

                        hideProgressDialog();
                        //show error
                        Log.e(TAG,error.toString());
                    }
                });
            }
        }
    }

    private boolean validateForm(){

        boolean valid = !ValidUtil.isEmpty(etEmail);
        valid &= !ValidUtil.isEmpty(etPassword);
        valid &= !ValidUtil.isEmpty(etMobile);
        valid &= !userTypeString.equals(getString(R.string.user_type));

        if(valid){

            if (!ValidUtil.checkEmail(etEmail)){
                showErrorText(tvEmailError,getString(R.string.email_error));
                valid = false;
            } else {
                hideErrorText(tvEmailError);
            }

            if(!ValidUtil.checkPassword(etPassword)){
                showErrorText(tvPasswordError,getString(R.string.password_error));
                valid = false;
            } else{
                hideErrorText(tvPasswordError);
            }

            if (!ValidUtil.checkMobile(etMobile)){
                showErrorText(tvMobileError,getString(R.string.mobile_error));
                valid = false;
            } else {
                hideErrorText(tvMobileError);
            }
        } else {
            valid = false;
            showSnackBar(getString(R.string.complete_form));
        }

        return valid;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        userTypeString = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String mobile = etMobile.getText().toString();

        if(!ValidUtil.isEmpty(email)){
            outState.putString(SI_EMAIL,email);
        }
        if(!ValidUtil.isEmpty(password)){
            outState.putString(SI_PASSWORD,password);
        }
        if(!ValidUtil.isEmpty(firstName)){
            outState.putString(SI_FIRST,firstName);
        }
        if(!ValidUtil.isEmpty(lastName)){
            outState.putString(SI_LAST,lastName);
        }
        if(!ValidUtil.isEmpty(mobile)){
            outState.putString(SI_MOBILE,mobile);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(SI_EMAIL)){
                etEmail.setText(savedInstanceState.getString(SI_EMAIL));
            }
            if(savedInstanceState.containsKey(SI_PASSWORD)){
                etPassword.setText(savedInstanceState.getString(SI_PASSWORD));
            }
            if(savedInstanceState.containsKey(SI_FIRST)){
                etFirstName.setText(savedInstanceState.getString(SI_FIRST));
            }
            if(savedInstanceState.containsKey(SI_LAST)){
                etLastName.setText(savedInstanceState.getString(SI_LAST));
            }
            if(savedInstanceState.containsKey(SI_MOBILE)){
                etMobile.setText(savedInstanceState.getString(SI_MOBILE));
            }
        }
    }

    private void resetField(){
        etEmail.setText("");
        etPassword.setText("");
        etFirstName.setText("");
        etLastName.setText("");
        etMobile.setText("");
    }
}
