package com.plz.nerf.gasolinaxalcool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    AdRequest adRequest;
    private ImageButton mAboutButton;
    private Dialog popup;
    private AdView mAdView;
    private Button mResetButton;
    private EditText mGas;
    private EditText mAlc;
    private TextView mAlcPercent;
    private TextView mGasPercent;
    private ImageView mConclusionImage1;
    private ImageView mCuriosityImage;
    private TextView mConclusionText;
    private TextView mCuriosityText;
    private TextView mObsText1;
    private TextView mObsText2;
    private Drawable mEditTextGreen;
    private Drawable mEditTextYellow;
    private Drawable mEditTextRed;
    private Drawable mEditText;
    private double kmAlcool;
    private double kmGas;
    private String idealPercentage;
    private int editFlag; //Flag used to alternate between edit text listeners.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        editFlag = -1;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupAds();
        popup = new Dialog(MainActivity.this);
        mAboutButton = findViewById(R.id.about_button);
        mEditTextGreen = ContextCompat.getDrawable(MainActivity.this, R.drawable.edit_text_pattern_green);
        mEditTextYellow = ContextCompat.getDrawable(MainActivity.this, R.drawable.edit_text_pattern_yellow);
        mEditTextRed = ContextCompat.getDrawable(MainActivity.this, R.drawable.edit_text_pattern_red);
        mEditText = ContextCompat.getDrawable(MainActivity.this, R.drawable.edit_text_pattern);
        mConclusionText = findViewById(R.id.conclusionText);
        mCuriosityText = findViewById(R.id.curiosityText);
        mConclusionImage1 = findViewById(R.id.conclusionImage1);
        mCuriosityImage = findViewById(R.id.curiosityImage);
        mObsText1 = findViewById(R.id.observation1);
        mObsText2 = findViewById(R.id.observation2);
        mAlcPercent = findViewById(R.id.alcoolPercentage);
        mGasPercent = findViewById(R.id.gasPercentage);
        setKmAlcool(7);
        setKmGas(10);
        setIdealPercentage();
        mAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });

        //GAS SETUP
        mGas = findViewById(R.id.gasEditText);
        mGas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                resetColors();
                setEditFlag(mGas);
                mGas.setText("");
                mAlcPercent.setText("");
                return false;
            }
        });
        mGas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 1){
                    s.insert(1, ".");
                }
                if(isActiveEditText(mGas)){
                    FuelPicker fp = new FuelPicker(mAlc.getText().toString(), mGas.getText().toString());
                    if (fp.getAlcoholPrice() > 0.0 && fp.getGasPrice() == 0.0) {//!hasClicked(mGas) && !mAlc.getText().toString().isEmpty()) {
                        String gasPercent = getResources().getString(R.string.valor_equivalente) + String.format(" %.3f", fp.getEquivalentGasPrice()) + "";
                        mGasPercent.setText(gasPercent);
                    }else if (fp.getAlcoholPrice() == 0.0 && fp.getGasPrice() > 0.0) {//!hasClicked(mGas) && !mAlc.getText().toString().isEmpty()) {
                        String gasPercent = getResources().getString(R.string.valor_equivalente) + String.format(" %.3f", fp.getEquivalentAlcoholPrice()) + "";
                        mAlcPercent.setText(gasPercent);
                    } else if(fp.getGasPrice() > 0.0 && fp.getAlcoholPrice() > 0.0){
                        mGasPercent.setText("");
                        mAlcPercent.setText(fp.getAlcoholRelation()+"%\n"+getResources().getString(R.string.valor_ideal)+idealPercentage);
                    }else {
                        mGasPercent.setText("");
                        mAlcPercent.setText("");
                    }
                    setConclusionVisibility();
                    setConclusionsText(fp);
                    setColors(fp);
                }
            }
        });

        //ETHANOL SETUP
        mAlc = findViewById(R.id.alcoolEditText);
        mAlc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                resetColors();
                setEditFlag(mAlc);
                mAlc.setText("");
                mGasPercent.setText("");
                return false;
            }
        });
        mAlc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().startsWith(".")){
                    s.clear();
                }
                if(s.toString().length() > 1){
                    s.insert(1, ".");
                }
                if(isActiveEditText(mAlc)){
                    FuelPicker fp = new FuelPicker(mAlc.getText().toString(), mGas.getText().toString());
                    if (fp.getAlcoholPrice() > 0.0 && fp.getGasPrice() == 0.0) {//!hasClicked(mGas) && !mAlc.getText().toString().isEmpty()) {
                        String gasPercent = getResources().getString(R.string.valor_equivalente) + String.format(" %.3f", fp.getEquivalentGasPrice()) + "";
                        mGasPercent.setText(gasPercent);
                    }else if (fp.getAlcoholPrice() == 0.0 && fp.getGasPrice() > 0.0) {//!hasClicked(mGas) && !mAlc.getText().toString().isEmpty()) {
                        String gasPercent = getResources().getString(R.string.valor_equivalente) + String.format(" %.3f", fp.getEquivalentAlcoholPrice()) + "";
                        mAlcPercent.setText(gasPercent);
                    } else if(fp.getGasPrice() > 0.0 && fp.getAlcoholPrice() > 0.0){
                        mGasPercent.setText("");
                        mAlcPercent.setText(fp.getAlcoholRelation()+"%\n"+getResources().getString(R.string.valor_ideal)+idealPercentage);
                    } else {
                        mGasPercent.setText("");
                        mAlcPercent.setText("");
                    }
                    setConclusionVisibility();
                    setConclusionsText(fp);
                    setColors(fp);
                }
            }
        });

        mResetButton = findViewById(R.id.resetButton);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    }

    private void setColors(FuelPicker fp){
        if (fp.getAlcoholPrice() > 0.0 && fp.getGasPrice() == 0.0) {
            mGasPercent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.equivalentYellow));
            mAlc.setBackground(mEditTextYellow);
        }else if (fp.getAlcoholPrice() == 0.0 && fp.getGasPrice() > 0.0) {//!hasClicked(mGas) && !mAlc.getText().toString().isEmpty()) {
            mAlcPercent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.equivalentYellow));
            mGas.setBackground(mEditTextYellow);
        } else if(fp.getGasPrice() > 0.0 && fp.getAlcoholPrice() > 0.0){
            if(fp.shouldUseGas()){
                mGas.setBackground(mEditTextGreen);
                mAlc.setBackground(mEditTextRed);
            }else{
                mGas.setBackground(mEditTextRed);
                mAlc.setBackground(mEditTextGreen);
            }
            mAlcPercent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
            mGasPercent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
        }else {
            resetColors();
        }

    }

    private void setupAds(){
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");  // ca-app-pub-7107657737723421~3249481380

        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private boolean isActiveEditText(EditText editText){
        if(editText == findViewById(R.id.alcoolEditText) && editFlag == 1){
            return true;
        }else if (editText == findViewById(R.id.gasEditText) && editFlag == 0){
            return true;
        }else{
            return false;
        }
    }

    private void resetAllText(){
        mAlc.setText("");
        mGas.setText("");
        mAlcPercent.setText("");
        mGasPercent.setText("");
    }

    private void reset(){
        editFlag = -1;
        resetAllText();
        resetColors();
        mGas.clearFocus();
        mAlc.clearFocus();
        setConclusionsVisible(View.INVISIBLE);
    }

    private void setEditFlag(EditText activeEditText){
        if(activeEditText == findViewById(R.id.alcoolEditText)){
            editFlag = 1;
        }else if(activeEditText == findViewById(R.id.gasEditText)){
            editFlag = 0;
        }
    }

    private void setConclusionsText(FuelPicker fp){
        if(fp.getAlcoholPrice() == 0.0 || fp.getGasPrice() == 0.0){
            mConclusionText.setText(getResources().getString(R.string.informe_o_outro_valor));
            mConclusionText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.badRed));
            mConclusionImage1.setVisibility(View.INVISIBLE);
            mCuriosityText.setVisibility(View.INVISIBLE);
            mCuriosityImage.setVisibility(View.INVISIBLE);
            mObsText1.setVisibility(View.INVISIBLE);
            mObsText2.setVisibility(View.INVISIBLE);

        }else{
            double savings = fp.getSavings();
            double absolutePer40 = fp.absoluteSavingsPer40Liters();
            String fuelType;
            if(fp.shouldUseGas()){
                fuelType = " gasolina";
            }else{
                fuelType = " etanol";
            }
            mConclusionText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.goodGreen));
            mConclusionText.setText((getResources().getString(R.string.conclusionString)+String.format(" %.2f%% ", savings)+getResources().getString(R.string.conclusionString2)+fuelType));
            mCuriosityText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.goodGreen));
            mCuriosityText.setText((getResources().getString(R.string.rendimento)
                    +fuelType
                    +getResources().getString(R.string.rendimento2)
                    +String.format(" %.2f", absolutePer40)
                    +getResources().getString(R.string.rendimento3)
                    +String.format(" %.2f ", (absolutePer40/0.064))
                    +getResources().getString(R.string.rendimento4)
            ));
        }

    }

    private  void setConclusionVisibility(){
        boolean isAlcoolEmpty = mAlc.getText().toString().isEmpty();
        boolean isGasEmpty = mGas.getText().toString().isEmpty();
        if(isAlcoolEmpty && isGasEmpty){
            mConclusionText.setVisibility(View.INVISIBLE);
        } else if((isAlcoolEmpty && !isGasEmpty) || (isGasEmpty && !isAlcoolEmpty)){
            mConclusionText.setVisibility(View.VISIBLE);
        } else{
            setConclusionsVisible(View.VISIBLE);
        }
    }

    private void setConclusionsVisible(int visibility){
        mConclusionText.setVisibility(visibility);
        mConclusionImage1.setVisibility(visibility);
        mCuriosityText.setVisibility(visibility);
        mCuriosityImage.setVisibility(visibility);
        mObsText1.setVisibility(visibility);
        mObsText2.setVisibility(visibility);
    }

    private void resetColors(){
        mGas.setBackground(mEditText);
        mAlc.setBackground(mEditText);
    }

    private void setIdealPercentage(){
        double percent = (this.kmAlcool*100)/this.kmGas;
        this.idealPercentage = percent+"%";
    }

    public void ShowPopup(View v) {
        TextView txtclose;
        TextView about;
        hideKeyboard(MainActivity.this);
        popup.setContentView(R.layout.about_popup);
        txtclose = popup.findViewById(R.id.txtclose);
        about = popup.findViewById(R.id.textView);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        about.setBackground(mEditText);
        popup.show();
    }

    private void setKmAlcool(double kmAlcool){
        this.kmAlcool = kmAlcool;
    }

    private void setKmGas(double kmGas){
        this.kmGas = kmGas;
    }

    public static void hideKeyboard( Activity activity ) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService( Context.INPUT_METHOD_SERVICE );
        View f = activity.getCurrentFocus();
        if( null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom( f.getClass() ) )
            imm.hideSoftInputFromWindow( f.getWindowToken(), 0 );
        else
            activity.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
    }
}

