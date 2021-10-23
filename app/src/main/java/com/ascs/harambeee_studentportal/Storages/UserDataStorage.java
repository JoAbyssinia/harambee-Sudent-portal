package com.ascs.harambeee_studentportal.Storages;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDataStorage {

    SharedPreferences preferences;
    public UserDataStorage(Context context){
        preferences = context.getSharedPreferences("emailandpass",0);
    }

    public void setEmailPassId(String emailPAss, String sId){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email&pass", emailPAss);
        editor.putString("sid",sId);
        editor.apply();
    }

    public void setEmailPass(String emailPAss){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email&pass", emailPAss);
        editor.apply();
    }

    public void setStudentRealID(String real){
        SharedPreferences.Editor  editor = preferences.edit();
        editor.putString("StudentRealID",real);
        editor.apply();
    }

    public String getEmailPass(){
        return preferences.getString("email&pass",String.valueOf(0));
    }

    public String getStudentID(){
        return preferences.getString("sid",String.valueOf(0));
    }

    public String getStudentRealID(){
        return preferences.getString("StudentRealID",String.valueOf(0));
    }


}
