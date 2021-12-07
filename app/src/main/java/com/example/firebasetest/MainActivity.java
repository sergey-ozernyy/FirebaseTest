package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    public String TAG = "CURRENT_TOKEN";
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Получаем текущий токен
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast
                        Log.d(TAG, token);
                    }
                });

        //По нажатию на кнопку "Регистрация" отправляется POST запрос на сервер, который содержит токен и номер телефона пользователя
        Button registrationButton = (Button) findViewById(R.id.registration_button);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String myString="данные которые будет принимать и обрабатывать сервер";
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody formBody = new FormBody.Builder()
                                .add("token", token)
                                .add("phone", "845454214525")//номер статичный, т.к. на текущий момент больше интересует сама отправка токена
                                .build();
                        Request request = new Request.Builder()
                                .url("http://192.168.10.62:8080/inbox")
                                .post(formBody)
                                .build();
                        try {
                            Response response = okHttpClient.newCall(request).execute();
                            String stringResponse = response.body().string(); //в этой переменной будет ответ сервера
                        } catch (IOException e) {
                            Log.e(TAG, "Exception: "+Log.getStackTraceString(e));
                        }
                    }
                }).start();

                Toast.makeText(getApplicationContext(), "Запрос отправлен, токен:" + token, Toast.LENGTH_SHORT ).show();
            }
        });



    }

}



