package com.example.testapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.offer18.sdk.Offer18;
import com.offer18.sdk.constant.Constant;
import com.offer18.sdk.contract.Callback;
import com.offer18.sdk.contract.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    HashMap<String, String> p = new HashMap<String, String>();
    View trackConversionBtn;
    EditText offerId, accountId, tid, domain, advSub1, advSub2, advSub3, advSub4, advSub5, event, payout, sale, coupon,
            allowMultiConversion, currency, statusContainer;
    Spinner postbackType, isGlobalPixel;
    LinearLayout container;
    TextView error;
    List<HashMap<String, EditText>> additionArgs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.container);
        offerId = findViewById(R.id.offer_id);
        domain = findViewById(R.id.domain);
        accountId = findViewById(R.id.account_id);
        tid = findViewById(R.id.tid);
        advSub1 = findViewById(R.id.adv_sub1);
        advSub2 = findViewById(R.id.adv_sub2);
        advSub3 = findViewById(R.id.adv_sub3);
        advSub4 = findViewById(R.id.adv_sub4);
        advSub5 = findViewById(R.id.adv_sub5);
        event = findViewById(R.id.event);
        sale = findViewById(R.id.sale);
        payout = findViewById(R.id.payout);
        coupon = findViewById(R.id.coupon);
        currency = findViewById(R.id.currency);
        postbackType = findViewById(R.id.postback_type);
        statusContainer = findViewById(R.id.error_container);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.postback_type,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postbackType.setAdapter(adapter);
        isGlobalPixel = findViewById(R.id.is_global_pixel);
        ArrayAdapter<CharSequence> adapterGlobal = ArrayAdapter.createFromResource(
                this,
                R.array.is_global_pixel,
                android.R.layout.simple_spinner_item
        );
        adapterGlobal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isGlobalPixel.setAdapter(adapterGlobal);
        allowMultiConversion = findViewById(R.id.allow_multi_conversion);
        trackConversionBtn = findViewById(R.id.button);
        trackConversionBtn.setOnClickListener(v -> {
            try {
                HashMap<String, String> args = new HashMap<String, String>();
                Offer18.init(getBaseContext());
                args.put("domain", domain.getText().toString());
                args.put("offer_id", offerId.getText().toString());
                args.put("account_id", accountId.getText().toString());
                args.put("tid", tid.getText().toString());
                args.put("adv_sub1", advSub1.getText().toString());
                args.put("adv_sub2", advSub2.getText().toString());
                args.put("adv_sub3", advSub3.getText().toString());
                args.put("adv_sub4", advSub4.getText().toString());
                args.put("adv_sub5", advSub5.getText().toString());
                args.put("event", event.getText().toString());
                args.put("sale", sale.getText().toString());
                args.put("payout", payout.getText().toString());
                args.put("coupon", coupon.getText().toString());
                args.put("currency", currency.getText().toString());
                args.put("t", postbackType.getSelectedItem().toString().equals("iframe") ? Constant.POSTBACK_TYPE_IFRAME : Constant.POSTBACK_TYPE_PIXEL);
                args.put("gb", isGlobalPixel.getSelectedItem().toString());
                if (!additionArgs.isEmpty()) {
                    for (int i = 0; i < additionArgs.size(); i++) {
                        HashMap<String, EditText> arg = additionArgs.get(i);
                        String userValue = arg.get("user_key").getText().toString();
                        if (Objects.isNull(userValue) || userValue.isEmpty()) {
                            continue;
                        }
                        args.put(userValue, arg.get("user_value").getText().toString());
                    }
                }
                Offer18.trackConversion(args, new Callback() {
                    @Override
                    public void onSuccess(Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusContainer.setText(response.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onError(Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusContainer.setText(response.getMessage());
                            }
                        });
                        Log.d("o18", response.getMessage());
                    }
                });
                Log.d("o18", args.toString());
            } catch (Exception e) {
                Log.d("o18", e.getMessage());
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * @param menu The options menu in which you place your items.
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("o18", Integer.toString(item.getItemId()));
        if (item.getItemId() == R.id.add_new) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            EditText userKey = new EditText(getApplicationContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            userKey.setLayoutParams(layoutParams);
            userKey.setHint("User key");
            EditText userValue = new EditText(getApplicationContext());
            userValue.setHint("Value");
            userValue.setLayoutParams(layoutParams);
            linearLayout.addView(userKey);
            linearLayout.addView(userValue);
            HashMap<String, EditText> args = new HashMap<>();
            args.put("user_key", userKey);
            args.put("user_value", userValue);
            additionArgs.add(args);
            this.container.addView(linearLayout);
        }
        return true;
    }
}