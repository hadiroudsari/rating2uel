package com.example.rating2;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rating2.requests.Common;
import com.example.rating2.utill.Counter;
import com.example.rating2.utill.RequestCallBack;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class ProfileActivity extends AppCompatActivity {
    //   Common common;
    final Counter counter = new Counter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Profile profile = (Profile) getIntent().getSerializableExtra("Profile");
//        System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" + profile.getName());
        TextView welcomTextView = (TextView) findViewById(R.id.welcomeText);
        welcomTextView.setText("Hello " + profile.getName());
//        TextView textViewTimer=(TextView)findViewById(R.id.textViewTimer);
        Button button = findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("starting...");
                initiatingGame(profile);
                counter.setNumber(0);
            }
        });
    }

    //    public void initiatingGame(final Profile profile){
//        //String Request initialized
//        String url = "https://hadi.shghgh.ir:8443/maro/resteasy/gate/start/"+profile.getName()+"/"+profile.getSerialNumber();
//        System.out.println("url:"+url);
//        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(url);
//        Retrofit retrofit=builder.build();
//
//    }
    public void initiatingGame(final Profile profile) {
        if(counter.getNumber()==1 || counter.getNumber()==3 || counter.getNumber()==5){
            Toast.makeText(this, "Searching for other player...", Toast.LENGTH_LONG).show();

        }
        RequestQueue queue = Volley.newRequestQueue(this);
        //String Request initialized
        String url = "https://hadi.shghgh.ir:8443/maro/resteasy/gate/start/" + profile.getName() + "/" + profile.getSerialNumber() + "/" + counter.getNumber();
        System.out.println("url:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    //   textView.setText("Response is: "+ response.substring(0,500));
                    System.out.println("Response is: " + response.toString());
                    ObjectMapper objectMapper = new ObjectMapper();
                    Common common = null;
                    try {
                        common = objectMapper.readValue(response.toString(), Common.class);
                        System.out.println("status is:" + common.getStatus());
                        if(common.getStatus().equals("no")){
                            counter.setNumber(0);
                            Toast.makeText(this, "There is no one to play", Toast.LENGTH_LONG).show();
                                                }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    if (common.getStatus().equals("startok") || common.getStatus().equals("alreadystarted")) {
//                            Toast.makeText(ProfileActivity.this, "initiating the game...", Toast.LENGTH_LONG).show();
                        System.out.println("initiating the game.............................................");
                        pooling(profile, new RequestCallBack() {
                            @Override
                            public void onSuccess(String responce) {
                                System.out.println("responce arrive to callback");
                                System.out.println(responce.toString());
                                System.out.println("after success callback");
                                SystemClock.sleep(2000);
                                System.out.println("after 2 second the counter is " + counter.getNumber());
                                if (counter.getNumber() < 8) {
                                    initiatingGame(profile);
                                } else {
                                    System.out.println("counter is " + counter.getNumber() + " there is no one to play");
                                }
                            }
                        }, counter);
//
                    } else if (common.getStatus().equals("attack")) {
                        System.out.println("time to attack");
                        TextView textViewTimer = (TextView) findViewById(R.id.textViewTimer);
                        TextView textViewBanner = findViewById(R.id.textViewBanner);
                        TextView textViewOpponent = findViewById(R.id.textViewOpponent);
                        textViewTimer.setVisibility(View.INVISIBLE);
                        textViewBanner.setVisibility(View.INVISIBLE);
                        textViewOpponent.setVisibility(View.INVISIBLE);
                        profile.setOpponent(common.getOpponent());
                        profile.setDuelTime(common.getDuelTime());
                        //Timer duration
                        System.out.println("dueltime: " + profile.getDuelTime());
                        System.out.println("remain to dueltime " + Math.abs(Long.parseLong(profile.getDuelTime()) - System.currentTimeMillis()));
                        long duration = Long.parseLong(profile.getDuelTime()) - System.currentTimeMillis();
                        if (duration > 0) {
                            //initialize countdown
                            textViewTimer.setVisibility(View.VISIBLE);
                            textViewBanner.setVisibility(View.VISIBLE);
                            textViewOpponent.setVisibility(View.VISIBLE);
                            textViewOpponent.setText("You will duel with " + profile.getOpponent());
                            new CountDownTimer(duration, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    System.out.println();
                                    String tick = String.format(Locale.ENGLISH, "%02d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
                                    textViewTimer.setText(tick);
                                }

                                @Override
                                public void onFinish() {
                                    newActivity(profile);
                                }
                            }.start();
                        } else {
                            newActivity(profile);
                        }
//                        newActivity(profile);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("error on responce ");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void pooling(Profile profile, final RequestCallBack requestCallBack, Counter counter) {
        System.out.println("on pooling============================================");
        counter.incriment();
        RequestQueue queue = Volley.newRequestQueue(this);
        //String Request initialized
        String url = "https://hadi.shghgh.ir:8443/maro/resteasy/gate/pooling/" + profile.getName() + "/" + profile.getSerialNumber();
        System.out.println("url:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("on responce pooling");
                        // Display the first 500 characters of the response string.
                        //   textView.setText("Response is: "+ response.substring(0,500));
                        System.out.println("Response is: " + response.toString());
                        requestCallBack.onSuccess(response.toString());
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        Common common= null;
//                        try {
////                            common = objectMapper.readValue(response.toString(), Common.class);
////                            //        map= objectMapper.readValue(response.toString(), new TypeReference<Map<String,Object>>(){});
////                            System.out.println("status is:"+common.getStatus());
//////                            ProfileActivity.this.common.setStatus(common.getStatus());
////                            ProfileActivity.this.common=common;
//                        } catch (JsonProcessingException e) {
//                            e.printStackTrace();
//                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error on responce0 ");
                error.printStackTrace();
                System.out.println("error on responce1 ");
            }
        });
        // Add the request to the RequestQueue.
        System.out.println("before put  pooling request into queue");
        queue.add(stringRequest);
//        queue.notify();
        System.out.println("after put  pooling request into queue");

    }


    public void newActivity(Profile profile) {
        Intent i = new Intent(ProfileActivity.this, BattleActivity.class);
        i.putExtra("Profile", profile);
        startActivity(i);
        finish();
    }
}