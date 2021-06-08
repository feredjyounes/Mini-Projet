package com.app.user;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import com.app.pharmasist.R;
import com.app.tools.MyTools;

import java.sql.Date;

public class Product {

    public int productId;
    public String productName;
    public String productForm;
    public String productDose;
    public int productQuantity;
    public double productPrice;
    public Date productExpiration;

    public Product(int productId, String productName, String productForm, String productDose,
                   int productQuantity, double productPrice, Date productExpiration) {
        this.productId = productId;
        this.productName = productName;
        this.productForm = productForm;
        this.productDose = productDose;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.productExpiration = productExpiration;
    }

    public static void setErrFields(Context context, Button button, EditText... editTexts){
        MyTools.setError(editTexts[0], MyTools.productNameSyntaxe, context.getString(R.string.name_syntaxe), button);
        MyTools.setError(editTexts[1], MyTools.doseSyntaxe, context.getString(R.string.dose_syntaxe), button);
        MyTools.setError(editTexts[2], MyTools.numberSyntaxe, context.getString(R.string.number_syntaxe), button);
        MyTools.setError(editTexts[3], MyTools.priceSyntaxe, context.getString(R.string.price_syntaxe), button);
        MyTools.setError(editTexts[4], MyTools.dateSyntaxe, context.getString(R.string.date_syntaxe), button);
    }

    public static boolean checkBlankFields(String errMsg, EditText... editTexts){
        return MyTools.isBlank(editTexts[0], errMsg)
                && MyTools.isBlank(editTexts[1], errMsg)
                && MyTools.isBlank(editTexts[2], errMsg)
                && MyTools.isBlank(editTexts[3], errMsg)
                && MyTools.isBlank(editTexts[4], errMsg);
    }

    public static boolean checkFieldsSyntaxe(EditText... editTexts){
        return MyTools.isRespectSyntaxe(editTexts[0], MyTools.productNameSyntaxe)
                && MyTools.isRespectSyntaxe(editTexts[1], MyTools.doseSyntaxe)
                && MyTools.isRespectSyntaxe(editTexts[2], MyTools.numberSyntaxe)
                && MyTools.isRespectSyntaxe(editTexts[3], MyTools.priceSyntaxe)
                && MyTools.isRespectSyntaxe(editTexts[4], MyTools.dateSyntaxe);
    }

}
