package com.japri.amierul.icarasia;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;

public class HomeFragment  extends BaseFragment implements View.OnClickListener{

    private TextView tv_firstName_value, tv_lastName_value, tv_mobile_value;

    public static final String TAG = "HomeFragment";
    public static final String EMAIL_KEY = "email.key";
    private String id;
    private String userTypeString;

    private RealmEngine realmEngine;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String email){
        HomeFragment fragment = new HomeFragment();
        Bundle arg = new Bundle();
        arg.putString(EMAIL_KEY,email);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            if(getArguments().containsKey(EMAIL_KEY)){
                id = getArguments().getString(EMAIL_KEY);
            }
        }

        realmEngine = new RealmEngine(getContext());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);

        LinearLayout ll_firstName = view.findViewById(R.id.home_ll_firstName);
        LinearLayout ll_lastName = view.findViewById(R.id.home_ll_lastName);
        LinearLayout ll_mobile = view.findViewById(R.id.home_ll_mobile);

        TextView tv_firstName_lbl = ll_firstName.findViewById(R.id.tv_label);
        TextView tv_lastName_lbl = ll_lastName.findViewById(R.id.tv_label);
        TextView tv_mobile_lbl = ll_mobile.findViewById(R.id.tv_label);

        tv_firstName_lbl.setText(getString(R.string.first_name));
        tv_lastName_lbl.setText(getString(R.string.last_name));
        tv_mobile_lbl.setText(getString(R.string.mobile));

        tv_firstName_value = ll_firstName.findViewById(R.id.tv_value);
        tv_lastName_value = ll_lastName.findViewById(R.id.tv_value);
        tv_mobile_value = ll_mobile.findViewById(R.id.tv_value);

        UserModel userModel = realmEngine.checkIfExist(id);

        tv_firstName_value.setText(userModel.getFirstName());
        tv_lastName_value.setText(userModel.getLastName());
        tv_mobile_value.setText(userModel.getMobile());
        userTypeString = userModel.getUserType();

        TextView tv_userType_edit = ll_mobile.findViewById(R.id.tv_edit);
        tv_userType_edit.setOnClickListener(this);
        Button btn_userType = view.findViewById(R.id.btn_usrType);
        btn_userType.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_edit:
                showEditDialog();
                break;
            case R.id.btn_usrType:
                String string = getString(R.string.user_type_toast)+" "+userTypeString;
                showToast(string);
                break;
        }
    }

    private void showToast(String string){
        Toast.makeText(getContext(),string,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_fragment_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_logout:
                showLogoutDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.logout));
        alertDialog.setMessage("00:5");
        alertDialog.show();

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                alertDialog.setMessage("Bye-bye. You will logout in 00:"+ (millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                alertDialog.dismiss();
                switchLogout();
            }
        }.start();

    }

    private void showEditDialog() {
        String mTitle = getString(R.string.edit);
        String message = getString(R.string.mobile_edit);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.view_edit_dialog,null);
        builder.setView(dialogView);

        TextView title = dialogView.findViewById(R.id.tv_title_dialog);
        title.setText(mTitle);

        TextView description = dialogView.findViewById(R.id.tv_note_dialog);
        description.setText(message);

        final TextView errView = dialogView.findViewById(R.id.tv_input_error);

        final EditText input = dialogView.findViewById(R.id.et_input_dialog);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                hideErrorText(errView);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String mobile = input.getText().toString();

                if(mobile.length() >= 11 &&
                        mobile.length() <= 12){

                    if(ValidUtil.checkMobile(mobile)){

                        if(mobile.equals(tv_mobile_value.getText().toString())){

                            //show error equal
                            showErrorText(errView,getString(R.string.mobile_error_equal));
                        } else {

                            //valid
                            hideErrorText(errView);
                        }

                    } else {

                        //not valid
                        showErrorText(errView,getString(R.string.mobile_error));
                    }
                } else {

                    //no error
                    hideErrorText(errView);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String mobile = input.getText().toString();

                if(ValidUtil.checkMobile(mobile) && !mobile.equals(tv_mobile_value.getText().toString())){
                    //save here
                    showProgressDialog();
                    saveNewMobile(mobile);
                } else {
                    showToast(getString(R.string.failed));
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.create().show();
    }

    private void saveNewMobile(final String mobile) {

        realmEngine.setMobile(id,
                mobile,
                new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                        //update view
                        tv_mobile_value.setText(mobile);
                        showToast(getString(R.string.success));

                        hideProgressDialog();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        showToast(getString(R.string.failed));
                        Log.e(TAG,error.toString());

                        hideProgressDialog();
                    }
                });
    }


}
