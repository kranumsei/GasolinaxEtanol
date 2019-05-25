package com.plz.nerf.gasolinaxalcool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private AdView mAdView;
    private Button mButton;
    private Button mResetButton;
    private EditText mGas;
    private EditText mAlc;
    private TextView mAlcPercent;
    private TextView mGasPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGas = findViewById(R.id.gasEditText);
        mGas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGas.setText("");
                mGas.setBackgroundColor(getResources().getColor(R.color.grey));
                mAlc.setBackgroundColor(getResources().getColor(R.color.grey));
                return false;
            }
        });
        mGasPercent = findViewById(R.id.gasPercentage);

        mAlc = findViewById(R.id.alcoolEditText);
        mAlc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mAlc.setText("");
                mGas.setBackgroundColor(getResources().getColor(R.color.grey));
                mAlc.setBackgroundColor(getResources().getColor(R.color.grey));
                return false;
            }
        });
        mAlcPercent = findViewById(R.id.alcoolPercentage);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mButton = findViewById(R.id.calculateButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FuelPicker fp = new FuelPicker(mAlc.getText().toString(), mGas.getText().toString());
                if(!mAlc.getText().toString().equals("") && !mGas.getText().toString().equals("")){

                }else if(!mAlc.getText().toString().equals("")){
                    mGas.setText(fp.getEquivalentGasPrice()+"");
                    fp = new FuelPicker(mAlc.getText().toString(), mGas.getText().toString());
                }else if(!mGas.getText().toString().equals("")){
                    mAlc.setText(fp.getEquivalentAlcoholPrice()+"");
                    fp = new FuelPicker(mAlc.getText().toString(), mGas.getText().toString());
                }


                mAlcPercent.setText(fp.getAlcoholGasRelation()+"%");
                if(fp.shouldUseGas()){
                    mGas.setBackgroundColor(getResources().getColor(R.color.goodGreen));
                    mAlc.setBackgroundColor(getResources().getColor(R.color.badRed));
                }else{
                    mGas.setBackgroundColor(getResources().getColor(R.color.badRed));
                    mAlc.setBackgroundColor(getResources().getColor(R.color.goodGreen));
                }

            }


        });

        mResetButton = findViewById(R.id.resetButton);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlc.setText("");
                mGas.setText("");
                mAlcPercent.setText("");
                mGas.setBackgroundColor(getResources().getColor(R.color.grey));
                mAlc.setBackgroundColor(getResources().getColor(R.color.grey));
            }
        });

    }

}
