package com.example.rating2;

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

import java.util.Map;


public class ProfileActivity extends AppCompatActivity {
    //   Common common;
    final Counter counter = new Counter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Profile profile = (Profile) getIntent().getSerializableExtra("Profile");
        System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" + profile.getName());
        TextView welcomTextView = (TextView) findViewById(R.id.welcomeText);
        welcomTextView.setText("Hello " + profile.getName());
        Button button = findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("starting...");
                initiatingGame(profile);

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

        RequestQueue queue = Volley.newRequestQueue(this);
        //String Request initialized
        String url = "https://hadi.shghgh.ir:8443/maro/resteasy/gate/start/" + profile.getName() + "/" + profile.getSerialNumber();
        System.out.println("url:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //   textView.setText("Response is: "+ response.substring(0,500));
                        System.out.println("Response is: " + response.toString());
                        ObjectMapper objectMapper = new ObjectMapper();
                        Common common = null;
                        try {
                            common = objectMapper.readValue(response.toString(), Common.class);
//                            //        map= objectMapper.readValue(response.toString(), new TypeReference<Map<String,Object>>(){});
                            System.out.println("status is:" + common.getStatus());
////                            ProfileActivity.this.common.setStatus(common.getStatus());
//                            ProfileActivity.this.common=common;
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
                                    SystemClock.sleep(3000);
                                    System.out.println("after 3 second the counter is " + counter.getNumber());
                                    if (counter.getNumber() < 3) {
                                        initiatingGame(profile);
                                    } else {
                                        System.out.println("counter is " + counter.getNumber() + " there is no one to play");
                                    }
                                }
                            }, counter);
//
                        }else if(common.getStatus().equals("attack")){
                            System.out.println("time to attack");
                        }

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
}