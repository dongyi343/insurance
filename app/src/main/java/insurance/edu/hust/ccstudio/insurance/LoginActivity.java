package insurance.edu.hust.ccstudio.insurance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button login,register,reset;
    private EditText username,password;
    private RelativeLayout loginCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(Button)findViewById(R.id.button_login);
        register=(Button)findViewById(R.id.register);
        reset=(Button)findViewById(R.id.reset);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((username.getText().toString()==null)||(password.getText().toString()==null)){
                    Toast.makeText(LoginActivity.this,"请输入用户名或者密码",Toast.LENGTH_SHORT);
                }else{
                   try{
                       JSONObject info=new JSONObject();
                       info.put("username",username.getText().toString());
                       info.put("password",password.getText().toString());
                       HttpUtil.jsonPost("http://10.0.2.2:8080/insurance/Login", info.toString(), new Callback() {
                           @Override
                           public void onFailure(Call call, IOException e) {

                           }

                           @Override
                           public void onResponse(Call call, Response response) throws IOException {
                               String result=response.body().string();
                               if(!result.equals("failure")){
                                   Intent intent=new Intent(LoginActivity.this,FindCustomerActivity.class);
                                   intent.putExtra("username",result);
                                   startActivity(intent);
                               }
                           }
                       });
                   }catch(Exception e){
                       e.printStackTrace();
                   }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

      reset.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(LoginActivity.this,ResetActivity.class);
              startActivity(intent);
          }
      });
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    protected void save(){
        SharedPreferences.Editor editor = getSharedPreferences("usernameAndpwd",MODE_PRIVATE).edit();
        editor.putString("username",username.getText().toString());
        editor.putString("password",password.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("usernameAndpwd",MODE_PRIVATE);

        String name = sp.getString("username","");
        String pass = sp.getString("password","");

        if (!username.equals("")&&!password.equals(""))
        {
            username.setText(name);
            password.setText(pass);
        }
    }
}
