package com.app.user;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import com.app.pharmasist.R;
import com.app.tools.MyTools;
import com.app.tools.ToastTask;

import java.sql.Time;

public class User {
    private static int id;
    private static String fname;
    private static String lname;
    private static String phone;
    private static String username;
    private static String pass;
    private static double locationx;
    private static double locationy;
    private static Time moorningStart;
    private static Time moorningEnd;
    private static Time eveningStart;
    private static Time eveningEnd;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        User.id = id;
    }

    public static String getFname() {
        return fname;
    }

    public static void setFname(String fname) {
        User.fname = fname;
    }

    public static String getLname() {
        return lname;
    }

    public static void setLname(String lname) {
        User.lname = lname;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        User.phone = phone;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        User.pass = pass;
    }

    public static double getLocationx() {
        return locationx;
    }

    public static void setLocationx(double locationx) {
        User.locationx = locationx;
    }

    public static double getLocationy() {
        return locationy;
    }

    public static void setLocationy(double locationy) {
        User.locationy = locationy;
    }

    public static Time getMoorningStart() {
        return moorningStart;
    }

    public static void setMoorningStart(Time moorningStart) {
        User.moorningStart = moorningStart;
    }

    public static Time getMoorningEnd() {
        return moorningEnd;
    }

    public static void setMoorningEnd(Time moorningEnd) {
        User.moorningEnd = moorningEnd;
    }

    public static Time getEveningStart() {
        return eveningStart;
    }

    public static void setEveningStart(Time eveningStart) {
        User.eveningStart = eveningStart;
    }

    public static Time getEveningEnd() {
        return eveningEnd;
    }

    public static void setEveningEnd(Time eveningEnd) {
        User.eveningEnd = eveningEnd;
    }

    public static void destructUser(){
        id = 0;
        fname = null;
        lname = null;
        phone = null;
        username = null;
        pass = null;
        locationx = 0.0;
        locationy = 0.0;
        moorningStart = null;
        moorningEnd = null;
        eveningStart = null;
        eveningEnd = null;
    }

    public static boolean checkBlankFields(String errMsg, EditText... editTexts){
        return MyTools.isBlank(editTexts[0], errMsg)
                && MyTools.isBlank(editTexts[1], errMsg)
                && MyTools.isBlank(editTexts[2], errMsg)
                && MyTools.isBlank(editTexts[3], errMsg)
                && MyTools.isBlank(editTexts[4], errMsg)
                && MyTools.isBlank(editTexts[5], errMsg)
                && MyTools.isBlank(editTexts[6], errMsg)
                && MyTools.isBlank(editTexts[7], errMsg)
                && MyTools.isBlank(editTexts[8], errMsg);
    }

    public static boolean checkSyntaxeFields(EditText... editTexts){
        return MyTools.isRespectSyntaxe(editTexts[0], MyTools.nameSyntaxe)
                && MyTools.isRespectSyntaxe(editTexts[1], MyTools.nameSyntaxe)
                && MyTools.isRespectSyntaxe(editTexts[2], MyTools.phoneSyntaxe)
                && MyTools.isRespectSyntaxe(editTexts[3], MyTools.userNameSyntaxe)
                && MyTools.isRespectSyntaxe(editTexts[4], MyTools.passwordSyntaxe)
                && MyTools.isRespectSyntaxe(editTexts[5], MyTools.timeSyntaxe)
                && MyTools.isRespectSyntaxe(editTexts[6], MyTools.timeSyntaxe)
                && MyTools.isRespectSyntaxe(editTexts[7], MyTools.timeSyntaxe)
                && MyTools.isRespectSyntaxe(editTexts[8], MyTools.timeSyntaxe);
    }

    /*
        This method use to set the syntaxe error for the fields
    */

    public static void setErrorForAllEditText(Context context, Button button, EditText... editTexts){

        MyTools.setError(editTexts[0], MyTools.nameSyntaxe, context.getString(R.string.name_syntaxe), button);
        MyTools.setError(editTexts[1], MyTools.nameSyntaxe, context.getString(R.string.name_syntaxe), button);
        MyTools.setError(editTexts[2], MyTools.phoneSyntaxe, context.getString(R.string.phone_syntaxe), button);
        MyTools.setError(editTexts[3], MyTools.userNameSyntaxe, context.getString(R.string.username_syntaxe), button);
        MyTools.setError(editTexts[4], MyTools.passwordSyntaxe, context.getString(R.string.time_syntaxe), button);
        MyTools.setError(editTexts[5], MyTools.timeSyntaxe, context.getString(R.string.time_syntaxe), button);
        MyTools.setError(editTexts[6], MyTools.timeSyntaxe, context.getString(R.string.time_syntaxe), button);
        MyTools.setError(editTexts[7], MyTools.timeSyntaxe, context.getString(R.string.time_syntaxe), button);
        MyTools.setError(editTexts[8], MyTools.timeSyntaxe, context.getString(R.string.time_syntaxe), button);

    }

    public static boolean checkTimes(Context context, EditText... times){
        Time timeMorning = Time.valueOf(times[0].getText().toString());
        Time timeMorning2 = Time.valueOf(times[1].getText().toString());
        Time timeEvening = Time.valueOf(times[2].getText().toString());
        Time timeEvening2 = Time.valueOf(times[3].getText().toString());
        if(timeMorning.compareTo(timeMorning2) == -1)
            if(timeMorning2.compareTo(timeEvening) == -1)
                if (timeEvening.compareTo(timeEvening2) == -1)
                    return true;
        new ToastTask(context).showMsg(context.getString(R.string.check_times));
        return false;
    }
}
