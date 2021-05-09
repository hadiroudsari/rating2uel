package com.example.rating2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rating2.requests.Post;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final static int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("1004804260320-a7cvfc02hck8rikflvbhjg8um3appmf7.apps.googleusercontent.com")
                .requestProfile()
                .build();
        // updateUI(account);
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(gso,
                        mGoogleSignInClient);
            }
        });

    }

    private void signIn(GoogleSignInOptions gso, GoogleSignInClient mGoogleSignInClient) {
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            System.out.println("-----------" + account.getEmail());
            System.out.println("------------------------------" + account.getIdToken() + "-----" +
                    "------------");
            updateUI(account);
            // Signed in successfully, show authenticated UI.
        } catch (IOException | ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refe//            updateUI(null);r to the GoogleSignInStatusCodes class reference for more information.
            e.printStackTrace();
            System.out.println("login failed");
        }


    }

    public void updateUI(final GoogleSignInAccount account) throws IOException {
        final Profile profile = new Profile();
        if (account != null) {
            Toast.makeText(this, "U get info from google server", Toast.LENGTH_LONG).show();
            //here is the place to send request to server ans save user data in database
            //     Post p=new Post();

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://hadi.shghgh.ir:8443/maro/MainServlet";



 //            Request a string response from the provided URL.
//            handleSSLHandshake();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                         //   textView.setText("Response is: "+ response.substring(0,500));
                            System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhResponse is: "+ response.toString());
                            String[] nameAndSerial=response.toString().split("_");
                            profile.setName(nameAndSerial[0]);
                            profile.setSerialNumber(nameAndSerial[1]);
                            newActivity(profile);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjThat didn't work!");
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    System.out.println("  sending...     :"+account.getIdToken());
                    String body=account.getIdToken();
                    try {
                        return body.getBytes(StandardCharsets.UTF_8);
                    }catch(Exception e){
                        throw new RuntimeException("Encoding not support",e);
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "text/plain");
                    return headers;
                }
            };

            // Add the request to the RequestQueue.

            queue.add(stringRequest);

            //   p.send();
//            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
//            //    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
//            i.putExtra("Profile", profile);
//            startActivity(i);
//            finish();
            //   startActivity(new Intent(this,SecondActivity.class));

        } else {
            Toast.makeText(this, "U Didnt signed in", Toast.LENGTH_LONG).show();
        }

    }

    public void newActivity(Profile profile){
        Intent i = new Intent(MainActivity.this, ProfileActivity.class);
        //    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        i.putExtra("Profile", profile);
        startActivity(i);
        finish();
    }

}