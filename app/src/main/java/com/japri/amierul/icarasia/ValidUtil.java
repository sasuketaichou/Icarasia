package com.japri.amierul.icarasia;

import android.text.TextUtils;
import android.widget.EditText;

public class ValidUtil{

    public static final String emailRegex = "^\\S+@\\S+\\.\\S+$";
    public static final String passwordRegex = "(?=.*[a-zA-Z])(?=.*[!@#$%&*()_+=|<>?{}\\[\\]~-]).{8,20}";
    public static final String mobileRegex = "^601[0-9]{8,9}$";

    public static boolean checkEmail(String string){
        return string != null && string.matches(emailRegex);
    }

    public static boolean checkEmail(EditText editText){
        return editText != null && checkEmail(editText.getText().toString());
    }

    public static boolean checkPassword(String string){
        return string != null && string.matches(passwordRegex);
    }

    public static boolean checkPassword(EditText editText){
        return editText != null && checkPassword(editText.getText().toString());
    }

    public static boolean checkMobile(String string) {
        return string != null && string.matches(mobileRegex);
    }

    public static boolean checkMobile(EditText editText){
        return editText != null && checkMobile(editText.getText().toString());
    }

    public static boolean isEmpty(EditText editText){
        return editText != null && TextUtils.isEmpty(editText.getText().toString());
    }

    public static boolean isEmpty(String string){
        return TextUtils.isEmpty(string);
    }


}
