package com.plz.nerf.gasolinaxalcool;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    AdRequest adRequest;
    private AdView mAdView;
    private Button mResetButton;
    private EditText mGas;
    private EditText mAlc;
    private TextView mAlcPercent;
    private ImageView mConclusionImage1;
    private ImageView mCuriosityImage;
    private TextView mConclusionText;
    private TextView mCuriosityText;
    private TextView mObsText1;
    private TextView mObsText2;
    Drawable mEditTextGreen;
    Drawable mEditTextRed;
    Drawable mEditText;
    private int editFlag; //Flag used to alternate between edit text listeners.
    private int[] userChoice; //Flag to indicate which of the editors was touched.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        editFlag = -1;
        userChoice = new int[2];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupAds();
        mEditTextGreen = ContextCompat.getDrawable(MainActivity.this, R.drawable.edit_text_pattern_green);
        mEditTextRed = ContextCompat.getDrawable(MainActivity.this, R.drawable.edit_text_pattern_red);
        mEditText = ContextCompat.getDrawable(MainActivity.this, R.drawable.edit_text_pattern);
        mConclusionText = findViewById(R.id.conclusionText);
        mCuriosityText = findViewById(R.id.curiosityText);
        mConclusionImage1 = findViewById(R.id.conclusionImage1);
        mCuriosityImage = findViewById(R.id.curiosityImage);
        mObsText1 = findViewById(R.id.observation1);
        mObsText2 = findViewById(R.id.observation2);
        mAlcPercent = findViewById(R.id.alcoolPercentage);

        //GAS SETUP
        mGas = findViewById(R.id.gasEditText);
        mGas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGas.setText("");
                resetColors();
                setEditFlag(mGas);
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
                    FuelPicker fp;
                    if (!hasClicked(mAlc) && !mGas.getText().toString().isEmpty()) {
                        fp = new FuelPicker("", mGas.getText().toString());
                        mAlc.setText(fp.getEquivalentAlcoholPrice()+"");
                    }else {
                        setConclusionVisibility();
                        fp = new FuelPicker(mAlc.getText().toString(), mGas.getText().toString());
                        mAlcPercent.setText(fp.getAlcoholGasRelation()+"%");
                        setConclusionsText(fp);
                        setColors(fp);
                    }
                    if(mGas.getText().toString().isEmpty()) {
                        userChoice[0] = 0;
                        resetColors();
                        mAlcPercent.setText("");
                    }else {
                        userChoice[0]= 1;
                    }

                }
            }
        });

        //ETHANOL SETUP
        mAlc = findViewById(R.id.alcoolEditText);
        mAlc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mAlc.setText("");
                resetColors();
                setEditFlag(mAlc);
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
                if(s.toString().length() > 1){
                    s.insert(1, ".");
                }
                if(isActiveEditText(mAlc)){
                    FuelPicker fp;
                    if (!hasClicked(mGas) && !mAlc.getText().toString().isEmpty()) {
                        fp = new FuelPicker(mAlc.getText().toString(), "");
                        mGas.setText(fp.getEquivalentGasPrice()+"");
                    }else {
                        setConclusionVisibility();
                        fp = new FuelPicker(mAlc.getText().toString(), mGas.getText().toString());
                        mAlcPercent.setText(fp.getAlcoholGasRelation()+"%");
                        setConclusionsText(fp);
                        setColors(fp);
                    }
                    if(mAlc.getText().toString().isEmpty()) {
                        userChoice[1] = 0;
                        resetColors();
                        mAlcPercent.setText("");
                    }else {
                        userChoice[1]= 1;
                    }
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

        if(fp.shouldUseGas()){
            mGas.setBackground(mEditTextGreen);
            mAlc.setBackground(mEditTextRed);
        }else{
            mGas.setBackground(mEditTextRed);
            mAlc.setBackground(mEditTextGreen);
        }
    }

    private void setupAds(){
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

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

    private boolean hasClicked(EditText editText){
        if(editText == findViewById(R.id.gasEditText) && userChoice[0] > 0){
            return true;
        } else if(editText == findViewById(R.id.alcoolEditText) && userChoice[1] > 0){
            return true;
        } else{
            return false;
        }
    }

    private void resetAllText(){
        mAlc.setText("");
        mGas.setText("");
        mAlcPercent.setText("");
    }

    private void reset(){
        editFlag = -1;
        userChoice[0] = 0;
        userChoice[1] = 0;
        resetAllText();
        resetColors();
        mGas.clearFocus();
        mAlc.clearFocus();
        setConclusionsVisible(View.INVISIBLE);
    }

    private void setEditFlag(EditText activeEditText){
        if(activeEditText == findViewById(R.id.alcoolEditText)){
            editFlag = 1;
            userChoice[1] = 1;
        }else if(activeEditText == findViewById(R.id.gasEditText)){
            editFlag = 0;
            userChoice[0] = 1;
        }
    }

    private void setConclusionsText(FuelPicker fp){
        double savings =fp.getSavings();
        double absolutePer40 = fp.absoluteSavingsPer40Liters();
        String fuelType;
        if(fp.shouldUseGas()){
            fuelType = " gasolina";
        }else{
            fuelType = " etanol";
        }
        mConclusionText.setText((getResources().getString(R.string.conclusionString)+String.format(" %.2f%% ", savings)+getResources().getString(R.string.conclusionString2)+fuelType));
        mCuriosityText.setText((getResources().getString(R.string.rendimento)
                +fuelType
                +getResources().getString(R.string.rendimento2)
                +String.format(" %.2f", absolutePer40)
                +getResources().getString(R.string.rendimento3)
                +String.format(" %.2f ", (absolutePer40/0.064))
                +getResources().getString(R.string.rendimento4)

        ));
    }

    private  void setConclusionVisibility(){
        if((mGas.getText().toString()=="0.0" || mGas.getText().toString().isEmpty()) || (mAlc.getText().toString()=="0.0" || mAlc.getText().toString().isEmpty())){
            setConclusionsVisible(View.INVISIBLE);
        }else{
            setConclusionsVisible(View.VISIBLE);
        }
    }

    private void setConclusionsVisible(int visibility){

        mConclusionText.setVisibility(visibility);
        mCuriosityText.setVisibility(visibility);
        mConclusionImage1.setVisibility(visibility);
        mCuriosityImage.setVisibility(visibility);
        mObsText1.setVisibility(visibility);
        mObsText2.setVisibility(visibility);
    }

    private void resetColors(){
        mGas.setBackground(mEditText);
        mAlc.setBackground(mEditText);
    }
}
