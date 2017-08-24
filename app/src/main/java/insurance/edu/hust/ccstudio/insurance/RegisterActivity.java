package insurance.edu.hust.ccstudio.insurance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText name,mobile,password,repassword,idcard;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=(EditText)findViewById(R.id.name);
        mobile=(EditText)findViewById(R.id.mobile);
        password=(EditText)findViewById(R.id.rg_password);
        repassword=(EditText)findViewById(R.id.rg_repassword);
        idcard=(EditText)findViewById(R.id.id_card);
        register=(Button)findViewById(R.id.con_register);
    }

    protected boolean check(){
        boolean flag=true;
        if(name.getText().toString()==null){
            Toast.makeText(RegisterActivity.this,"请输入姓名",Toast.LENGTH_SHORT);
            flag=false;
        }
        if(mobile.getText().toString()==null){
            Toast.makeText(RegisterActivity.this,"请输入电话号码",Toast.LENGTH_SHORT);
            flag=false;
        }else{
            Pattern p = Pattern.compile("^(13[0-9]|15([0-3]|[5-9])|14[5,7,9]|17[1,3,5,6,7,8]|18[0-9])\\d{8}$");
            Matcher m = p.matcher(mobile.getText().toString());
            flag=m.matches();
        }
        if(password.getText().toString()==null){
            Toast.makeText(RegisterActivity.this,"请输入密码",Toast.LENGTH_SHORT);
            flag=false;
        }else{
            if(!password.getText().toString().equals(repassword.getText().toString())){
                Toast.makeText(RegisterActivity.this,"请再次输入密码",Toast.LENGTH_SHORT);
                flag=false;
            }
        }
        if(idcard.getText().toString()==null){
            Toast.makeText(RegisterActivity.this,"请输入正确的身份证号",Toast.LENGTH_SHORT);
            flag=false;
        }else{
            if(!checkIdCrad(idcard.getText().toString())){
                Toast.makeText(RegisterActivity.this,"输入的身份证号码错误",Toast.LENGTH_SHORT);
                flag=false;
            }
        }
        return flag;
    }

    protected boolean checkIdCrad(String id){
        // 对身份证号进行长度等简单判断
        if (id.length() != 18 || !id.matches("\\d{17}[0-9X]"))
        {
            return false;
        }
        // 1-17位相乘因子数组
        int[] factor = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        // 18位随机码数组
        char[] random = "10X98765432".toCharArray();
        // 计算1-17位与相应因子乘积之和
        int total = 0;
        for (int i = 0; i < 17; i++)
        {
            total += Character.getNumericValue(id.charAt(i)) * factor[i];
        }
        // 判断随机码是否相等
        return random[total % 11] == id.charAt(17);
    }

    @Override
    protected void onResume() {
        super.onResume();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()) {
                    try {
                        JSONObject info = new JSONObject();
                        info.put("username", name.getText().toString());
                        info.put("mobile", mobile.getText().toString());
                        info.put("password", password.getText().toString());
                        info.put("idcard", idcard.getText().toString());
                        Log.d("info", info.toString());
                        HttpUtil.jsonPost("http://10.0.2.2:8080/insurance/Register", info.toString(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String result=response.body().string();
                                if(!result.equals("failure")){
                                    Intent intent=new Intent(RegisterActivity.this,FindCustomerActivity.class);
                                    intent.putExtra("username",result);
                                    startActivity(intent);
                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this,"输入密码错误",Toast.LENGTH_SHORT);
                                        }
                                    });
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
