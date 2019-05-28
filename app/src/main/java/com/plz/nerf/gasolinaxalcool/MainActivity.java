package com.plz.nerf.gasolinaxalcool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    AdRequest adRequest;
    private AdView mAdView;
    private Button mButton;
    private Button mResetButton;
    private EditText mGas;
    private EditText mAlc;
    private TextView mAlcPercent;
    private int editFlag; //Flag used to alternate between edit text listeners.
    private int[] userChoice; //Flag to indicate which of the editors was touched.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        editFlag = -1;
        userChoice = new int[2];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupAds();
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
                if(isActiveEditText(mGas)){
                    FuelPicker fp;
                    if (!hasClicked(mAlc)) {
                        mAlc.setText("");
                        fp = new FuelPicker(mAlc.getText().toString(), mGas.getText().toString());
                        mAlc.setText(fp.getEquivalentAlcoholPrice()+"");

                    }
                    fp = new FuelPicker(mAlc.getText().toString(), mGas.getText().toString());
                    mAlcPercent.setText(fp.getAlcoholGasRelation()+"%");
                    setColors(fp);

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
                if(isActiveEditText(mAlc)){
                    FuelPicker fp;
                    if (!hasClicked(mGas)) {
                        mGas.setText("");
                        fp = new FuelPicker(mAlc.getText().toString(), mGas.getText().toString());
                        mGas.setText(fp.getEquivalentGasPrice()+"");
                        mAlcPercent.setText(fp.getAlcoholGasRelation()+"%");
                        setColors(fp);
                    }
                    fp = new FuelPicker(mAlc.getText().toString(), mGas.getText().toString());
                    mAlcPercent.setText(fp.getAlcoholGasRelation()+"%");
                    setColors(fp);
                }
            }
        });
        mAlcPercent = findViewById(R.id.alcoolPercentage);



        mButton = findViewById(R.id.calculateButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FuelPicker fp = new FuelPicker(mAlc.getText().toString(), mGas.getText().toString());
                mAlcPercent.setText(fp.getAlcoholGasRelation()+"%");
                setColors(fp);
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
            mGas.setBackgroundColor(getResources().getColor(R.color.goodGreen));
            mAlc.setBackgroundColor(getResources().getColor(R.color.badRed));
        }else{
            mGas.setBackgroundColor(getResources().getColor(R.color.badRed));
            mAlc.setBackgroundColor(getResources().getColor(R.color.goodGreen));
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

    private void reset(){
        mAlc.setText("");
        mGas.setText("");
        mAlcPercent.setText("");
        mGas.setBackgroundColor(getResources().getColor(R.color.grey));
        mAlc.setBackgroundColor(getResources().getColor(R.color.grey));
        editFlag = -1;
        userChoice[0] = 0;
        userChoice[1] = 0;
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

    private void resetColors(){
        mGas.setBackgroundColor(getResources().getColor(R.color.grey));
        mAlc.setBackgroundColor(getResources().getColor(R.color.grey));
    }
}
