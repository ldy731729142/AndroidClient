package com.example.androidclient;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText tvUserName;
    private EditText tvPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        tvUserName = (EditText) findViewById(R.id.tvUserName);
        tvPassword = (EditText) findViewById(R.id.tvPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String tvUserNameString = tvUserName.getText().toString().trim();
        if (TextUtils.isEmpty(tvUserNameString)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        String tvPasswordString = tvPassword.getText().toString().trim();
        if (TextUtils.isEmpty(tvPasswordString)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        InternetSever(tvUserNameString,tvPasswordString);
    }
    private void InternetSever(String username,String password) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        final Request request = new Request.Builder()
                .url("http://192.168.0.104:8000/login/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(LoginActivity.this, "网络异常，请稍后再试！", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String date=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson=new Gson();
                        Login_cs login_cs=gson.fromJson(date,Login_cs.class);
                        if (login_cs.get_$StatusCode185()==200){
                            Toast.makeText(LoginActivity.this, "欢迎登录，"+login_cs.getAdminname(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }else {
                            Toast.makeText(LoginActivity.this, login_cs.getCode(), Toast.LENGTH_SHORT).show();
                            tvPassword.setText("");
                            tvUserName.setText("");
                        }


                    }
                });

            }
        });


    }
}