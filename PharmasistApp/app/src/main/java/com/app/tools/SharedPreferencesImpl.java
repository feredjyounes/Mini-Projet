package com.app.tools;

import android.content.SharedPreferences;

import java.util.ArrayList;

public class SharedPreferencesImpl {

    /*
        This part specify for the shared preferennces
     */
    private SharedPreferences sp;

    public SharedPreferencesImpl(SharedPreferences sharedPreferences){
        this.sp = sharedPreferences;
    }

    public void save(String value){
        if(!ifValueExist(value)) {
            int index = sp.getInt("index", 1);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(index + "", value);
            index = index + 1;
            editor.putInt("index", index);
            editor.commit();
        }
    }

    public boolean ifValueExist(String value){
        int index = sp.getInt("index", 0);
        if(index == 0)
            return false;
        int i = index - 1;
        boolean exist = false;
        while((i > 0) && (exist == false)){
            if(value.trim().equals(sp.getString(i+"", "empty").trim())){
                exist = true;
            }
            i--;
        }
        return exist;
    }

    public ArrayList<String> load(){
        int index = sp.getInt("index", 0);
        ArrayList<String> list = new ArrayList<>();
        if(index == 0)
            return null;
        for(int i = index-1; i > 0; i--){
            list.add(sp.getString(i+"", "empty"));
        }
        return list;
    }

    public void clear(){
        sp.edit().clear().commit();
    }

}
