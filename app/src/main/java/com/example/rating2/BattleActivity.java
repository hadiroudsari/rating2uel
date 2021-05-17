package com.example.rating2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rating2.requests.Common;
import com.example.rating2.utill.RequestCallBack;
import com.example.rating2.view.BattleView;
import com.example.rating2.view.MyBattleView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BattleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
//        MyBattleView myBattleView=new MyBattleView(this);

        final Profile profile = (Profile) getIntent().getSerializableExtra("Profile");
        System.out.println("in battle with profile " + profile.getName());
        System.out.println("time and opponent" + profile.getOpponent() + "  " + profile.getDuelTime());
        Button gunButton = findViewById(R.id.button_gun);
        gunButton.setClickable(true);
        gunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("attack");
                attack(profile);
                gunButton.setClickable(false);

            }
        });
//        MyBattleView myBattleView=new MyBattleView(this,profile);
//        setContentView(myBattleView);

    }

    private void attack(Profile profile) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://hadi.shghgh.ir:8443/maro/resteasy/gate/attack/" + profile.getName() + "/" + profile.getSerialNumber() + "/" + System.currentTimeMillis();
        System.out.println("url:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            System.out.println("Response is: " + response);
            ObjectMapper objectMapper = new ObjectMapper();
            Common common = null;
            try {
                common = objectMapper.readValue(response.toString(), Common.class);
                System.out.println("status is:" + common.getStatus());
                if (common.getStatus().equals("ok") || common.getStatus().equals("alreadyattacked")) {
                    cheackWinner(profile, responce -> {
                        System.out.println("Response is: " +responce);
                        Common common2 = null;
                        try {
                            common2 = objectMapper.readValue(responce.toString(), Common.class);
                            System.out.println("proftile name " +profile.getName());
                            System.out.println("winner name " +common2.getWinnerName());
                            if(common2.getWinnerName().equals(profile.getName())){
                                System.out.println("you win shot Up");
                                MyBattleView myBattleView=new MyBattleView(this);
                                myBattleView.shotUp();
                            }else{
                                System.out.println("you lost shot down");
                            }
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                    });
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            System.out.println("error on responce ");
        });
        queue.add(stringRequest);
    }

    private void cheackWinner(Profile profile, RequestCallBack requestCallBack) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://hadi.shghgh.ir:8443/maro/resteasy/gate/check/" + profile.getName() + "/" + profile.getSerialNumber();
        System.out.println("url:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            requestCallBack.onSuccess(response);
        }, error -> {
            error.printStackTrace();
            System.out.println("error on responce ");
        });
        queue.add(stringRequest);

    }
}
