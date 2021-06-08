package com.app.tools;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.location.LocationManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyTools {

    /*
        All this variables contain the syntaxe of etch field must be checked
     */

    public static String nameSyntaxe = "^([A-Za-z])([a-zA-Z\\s])+$";
    public static String phoneSyntaxe = "^(05|06|07)([0-9]{8})$";
    public static String userNameSyntaxe = "^[a-zA-Z0-9+\\.\\-_]+@[a-zA-Z]+\\.[a-zA-Z]+$";
    public static String passwordSyntaxe = "^[a-zA-Z0-9\\.\\-_]{3,90}$";
    public static String timeSyntaxe = "^([01]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
    public static String productNameSyntaxe ="^([a-zA-Z\\s])+$";
    public static String numberSyntaxe = "^\\d+$";
    public static String priceSyntaxe = "^\\d+(\\d+.\\d+)?$";
    public static String doseSyntaxe = "((\\d+/\\d+)|(\\d+)|(\\d+\\.\\d+))\\s?(ml|mg|g)";
    public static String dateSyntaxe = "^(\\d{4}\\-\\d{2}\\-\\d{2})$";

    /*
        This methode check if an edit text blank or not
        return true if the edit text doesn't blank
        return false if the edit text is blank
     */

    public static boolean isBlank(EditText editText, String msg){
        if(!(editText.getText().toString().trim().equals(""))){
            return true;
        }else{
            editText.setError(msg);
            return false;
        }
    }

    public static boolean isBlank(AutoCompleteTextView editText, String msg){
        if(!(editText.getText().toString().trim().equals(""))){
            return true;
        }else{
            editText.setError(msg);
            return false;
        }
    }

    /*
        This method check if an edit text respect an syntaxe you give it
     */

    public static boolean isRespectSyntaxe(EditText editText, String syntaxe){
        if(editText.getText().toString().trim().matches(syntaxe)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isRespectSyntaxe(AutoCompleteTextView editText, String syntaxe){
        if(editText.getText().toString().trim().matches(syntaxe)){
            return true;
        }else{
            return false;
        }
    }

    /*
        This methode check the syntaxe of an edit text and set error for it
        regExpSyn : mean the regular expression syntaxe
            for example : ([a-z][A-Z]){2,90} for the name and last name
     */

    public static void setError(final EditText editText, final String regExpSyn, final String msg, final Button btn){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().matches(regExpSyn)){
                    editText.setError(null);
                    btn.setEnabled(true);
                    //editText.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.accept, 0);
                }else{
                    editText.setError(msg);
                    btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static void setError(final AutoCompleteTextView editText, final String regExpSyn, final String msg, final Button btn){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().matches(regExpSyn)){
                    editText.setError(null);
                    btn.setEnabled(true);
                    //editText.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.accept, 0);
                }else{
                    editText.setError(msg);
                    btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*
        This method check if a pharmasit is open with times given
     */

    public static boolean isPharmasistOpen(Time... times){
        Time now = Time.valueOf(new SimpleDateFormat("HH:mm:ss").format(new Date()));

        int x = now.compareTo(times[0]);
        int y = now.compareTo(times[1]);

        int a = now.compareTo(times[2]);
        int b = now.compareTo(times[3]);

        if(((x >= 0) && (y < 0)) || ((a >= 0) && (b < 0))){
            return true;
        }else{
            return false;
        }
    }

    /*
        This method turn a double number with 2 number after the coma
     */

    public static String getFormat(double number){
        DecimalFormat twoPlaces = new DecimalFormat("0.00");
        return twoPlaces.format(number);
    }

    /*
        This method used to show time picker
     */

    public static void viewTimePicker(Context context, EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hour = "";
                        String min = "";
                        if((hourOfDay >= 0) && (hourOfDay <= 9)){
                            hour = "0" + hourOfDay;
                        }else{
                            hour = hourOfDay + "";
                        }
                        if((minute >= 0) && (minute <= 9)){
                            min = "0" + minute;
                        }else{
                            min = minute + "";
                        }
                        editText.setText(hour + ":" + min + ":00");
                    }
                }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });
    }

    /*
        This method used to show date picker
     */

    public static void viewDatePicker(Context context, EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH);
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String stringMonth = "";
                        String stringDay = "";
                        month += 1;
                        if((month >= 1) && (month <= 9)){
                            stringMonth = "0" + month;
                        }else{
                            stringMonth = month + "";
                        }
                        if((dayOfMonth >= 1) && (dayOfMonth <= 9)){
                            stringDay = "0" + dayOfMonth;
                        }else{
                            stringDay = dayOfMonth + "";
                        }
                        editText.setText(year + "-" + stringMonth + "-" + stringDay);
                    }
                }, y, m, d);
                datePickerDialog.show();
            }
        });
    }

    // check if the gps enabled or not

    public static boolean isLocationEnabled(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
