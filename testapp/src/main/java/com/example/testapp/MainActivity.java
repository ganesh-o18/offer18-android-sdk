package com.example.testapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.offer18.sdk.Offer18;
import com.offer18.sdk.constant.Constant;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    HashMap<String, String> p = new HashMap<String, String>();
    HashMap<String, String> args = new HashMap<String, String>();
    View trackConversionBtn;
    EditText offerId, accountId, tid, domain, advSub1, advSub2, advSub3, advSub4, advSub5, event, payout, sale, coupon,
            allowMultiConversion, currency;
    Spinner postbackType, isGlobalPixel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        offerId = findViewById(R.id.offer_id);
        domain = findViewById(R.id.domain);
        accountId = findViewById(R.id.account_id);
        tid = (EditText) findViewById(R.id.tid);
        advSub1 = (EditText) findViewById(R.id.adv_sub1);
        advSub2 = (EditText) findViewById(R.id.adv_sub2);
        advSub3 = (EditText) findViewById(R.id.adv_sub3);
        advSub4 = (EditText) findViewById(R.id.adv_sub4);
        advSub5 = (EditText) findViewById(R.id.adv_sub5);
        event = (EditText) findViewById(R.id.event);
        sale = (EditText) findViewById(R.id.sale);
        payout = (EditText) findViewById(R.id.payout);
        coupon = (EditText) findViewById(R.id.coupon);
        currency = (EditText) findViewById(R.id.currency);
        postbackType = (Spinner) findViewById(R.id.postback_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.postback_type,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postbackType.setAdapter(adapter);
        isGlobalPixel = (Spinner) findViewById(R.id.is_global_pixel);
        ArrayAdapter<CharSequence> adapterGlobal = ArrayAdapter.createFromResource(
                this,
                R.array.is_global_pixel,
                android.R.layout.simple_spinner_item
        );
        adapterGlobal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isGlobalPixel.setAdapter(adapterGlobal);
        allowMultiConversion = (EditText) findViewById(R.id.allow_multi_conversion);
        trackConversionBtn = (Button) findViewById(R.id.button);
        trackConversionBtn.setOnClickListener(v -> {
            try {
                Offer18.init(getBaseContext(), p);
                args.put("domain", domain.getText().toString());
                args.put(Constant.OFFER_ID, offerId.getText().toString());
                args.put(Constant.ACCOUNT_ID, accountId.getText().toString());
                args.put(Constant.TID, tid.getText().toString());
                args.put(Constant.ADV_SUB_1, advSub1.getText().toString());
                args.put(Constant.ADV_SUB_2, advSub2.getText().toString());
                args.put(Constant.ADV_SUB_3, advSub3.getText().toString());
                args.put(Constant.ADV_SUB_4, advSub4.getText().toString());
                args.put(Constant.ADV_SUB_5, advSub5.getText().toString());
                args.put(Constant.EVENT, event.getText().toString());
                args.put(Constant.SALE, sale.getText().toString());
                args.put(Constant.PAYOUT, payout.getText().toString());
                args.put(Constant.COUPON, coupon.getText().toString());
                args.put(Constant.CURRENCY, currency.getText().toString());
                args.put(Constant.POSTBACK_TYPE, postbackType.getSelectedItem().toString().equals("iframe") ? Constant.POSTBACK_TYPE_IFRAME : Constant.POSTBACK_TYPE_PIXEL);
                args.put(Constant.IS_GLOBAL_PIXEL, isGlobalPixel.getSelectedItem().toString());
                Offer18.trackConversion(args);
                Toast.makeText(MainActivity.this, "Conversion Recorded  ", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.d("o18", e.getMessage());
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}