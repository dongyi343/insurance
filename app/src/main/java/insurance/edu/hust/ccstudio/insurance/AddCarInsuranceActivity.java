package insurance.edu.hust.ccstudio.insurance;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.Info;
import com.bm.library.PhotoView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddCarInsuranceActivity extends AppCompatActivity {

    protected EditText customerName,carId,fee,factFee;
    protected TextView startTime,endTime,carType,businessInsurance,company;
    protected Button addCard1,addCard2,addCar,addBill,cardAbulm1,addCarBill;
    protected PhotoView cardImage1,cardImage2,carImage,billPhoto;
    protected Spinner jqx;
    protected String data[],insurances[],businessResult="",companies[],chooseJqx;
    final int DATE_DIALOG=1,END_DATE=2,TAKE_CARD1=1,TAKE_CARD2=2,TAKE_CAR=3,TAKE_BILL=4;
    final int CHOOSE_CARD1=1,CHOOSE_CARD2=2,CHOOSE_CAR=3,CHOOSE_BILL=4;
    private int mYear,mMonth,mDay,eYear,eMonth,eDay;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_insurance);

        initView();
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(END_DATE);
            }
        });
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        eYear=ca.get(Calendar.YEAR);
        eMonth=ca.get(Calendar.MONTH);
        eDay=ca.get(Calendar.DAY_OF_MONTH);

        addCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               takePhoto("card1.jpg",TAKE_CARD1);
            }
        });

        addCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto("card2.jpg",TAKE_CARD2);
            }
        });

        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto("car.jpg",TAKE_CAR);
            }
        });

        addBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto("bill.jpg",TAKE_BILL);
            }
        });

        cardAbulm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(ContextCompat.checkSelfPermission(AddCarInsuranceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                 ActivityCompat.requestPermissions(AddCarInsuranceActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
             }else{
                 openAlbum(CHOOSE_CARD1);
             }
            }
        });
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display(mYear,mMonth,mDay,1);

        }
    };

    private DatePickerDialog.OnDateSetListener edateLinstener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            eYear = year;
            eMonth = monthOfYear;
            eDay = dayOfMonth;
            display(eYear,eMonth,eDay,0);

        }
    };

    //将时间显示在TextView中
    public void display(int year,int month,int day,int type) {
        if(type==1)
            startTime.setText(new StringBuffer().append(year).append("-").append(month+ 1).append("-").append(day).append(" "));
        else{
            if(type==0)
                endTime.setText(new StringBuffer().append(year).append("-").append(month+1).append("-").append(day).append(" "));
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
            case END_DATE:
                return new DatePickerDialog(this,edateLinstener,eYear,eMonth,eDay);
        }
        return null;
    }


    //初始化控件
    protected void initView(){
        customerName=(EditText)findViewById(R.id.customer_name);
        carId=(EditText)findViewById(R.id.car_id);
        jqx=(Spinner)findViewById(R.id.jqx);
        company=(TextView)findViewById(R.id.company);
        fee=(EditText)findViewById(R.id.fee);
        factFee=(EditText)findViewById(R.id.fact_fee);
        startTime=(TextView)findViewById(R.id.start_time);
        endTime=(TextView)findViewById(R.id.end_time);
        carType=(TextView)findViewById(R.id.car_type);
        businessInsurance=(TextView)findViewById(R.id.business_insurance);
        addCard1=(Button)findViewById(R.id.add_card1);
        addCard2=(Button)findViewById(R.id.add_card2);
        addCar=(Button)findViewById(R.id.add_car);
        addBill=(Button)findViewById(R.id.add_bill);
        cardImage1=(PhotoView) findViewById(R.id.card_image1);
        cardImage2=(PhotoView) findViewById(R.id.card_image2);
        carImage=(PhotoView)findViewById(R.id.car_image);
        billPhoto=(PhotoView) findViewById(R.id.bill_photo);
        cardAbulm1=(Button)findViewById(R.id.card_abulm1);
        addCarBill=(Button)findViewById(R.id.add_car_bill);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findCarType();
                if(data!=null)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(AddCarInsuranceActivity.this).setTitle("请选择车型")
                                    .setSingleChoiceItems(data, 0, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            carType.setText(data[i]);
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                        }
                    });
            }
        });

        businessInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findBusiness();
                new AlertDialog.Builder(AddCarInsuranceActivity.this).setTitle("请选择已购商业险")
                        .setMultiChoiceItems(insurances, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            businessResult=businessResult+insurances[i];
                        }
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        businessInsurance.setText(businessResult);
                    }
                }).show();
            }
        });

        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findCompany();
                new AlertDialog.Builder(AddCarInsuranceActivity.this).setTitle("请选择公司").setItems(companies, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        company.setText(companies[i]);
                    }
                }).show();
            }
        });

        jqx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chooseJqx=(String)jqx.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addCarBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    JSONObject info=new JSONObject();
                    info.put("name",customerName.getText());
                    info.put("carId",carId.getText());
                    info.put("carType",carType.getText());
                    info.put("company",company.getText());
                    info.put("jqx",chooseJqx);
                    info.put("business",data);
                    info.put("fee",fee.getText());
                    info.put("factFee",factFee.getText());
                    info.put("startTime",startTime.getText());
                    info.put("endTime",endTime.getText());
                    String paths[]=new String[4];
                    paths[0]=getExternalCacheDir()+"/card1.jpg";
                    paths[1]=getExternalCacheDir()+"/card2.jpg";
                    paths[2]=getExternalCacheDir()+"/car.jpg";
                    paths[3]=getExternalCacheDir()+"/bill.jpg";
                    HttpUtil.getAllInfo("http://10.0.2.2:8080/insurance/AddCarInsurance", paths, info.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result=response.body().string();
                            if(result.equals("success")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddCarInsuranceActivity.this, "申请成功", Toast.LENGTH_SHORT).show();
                                        new AlertDialog.Builder(AddCarInsuranceActivity.this)

                                                .setMessage("是否继续申请？")//设置显示的内容

                                                .setPositiveButton("是", new DialogInterface.OnClickListener() {//添加确定按钮


                                                    @Override

                                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                                        // TODO Auto-generated method stub

                                                        dialog.dismiss();
                                                    }

                                                }).setNegativeButton("否", new DialogInterface.OnClickListener() {//添加返回按钮


                                            @Override

                                            public void onClick(DialogInterface dialog, int which) {//响应事件

                                                // TODO Auto-generated method stub
                                                Intent intent = new Intent(AddCarInsuranceActivity.this, FindCustomerActivity.class);
                                                startActivity(intent);
                                            }

                                        }).show();//在按键响应事件中显示此对话框
                                    }
                                });
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case TAKE_CARD1:
                if(resultCode==RESULT_OK){
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        bitmap= ThumbnailUtils.extractThumbnail(bitmap,200,200);
                        cardImage1.setImageBitmap(bitmap);
                        display(cardImage1,addCard1);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case TAKE_CARD2:
                if(resultCode==RESULT_OK){
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        bitmap= ThumbnailUtils.extractThumbnail(bitmap,200,200);
                        cardImage2.setImageBitmap(bitmap);
                        display(cardImage2,addCard2);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case TAKE_CAR:
                if(resultCode==RESULT_OK){
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        bitmap= ThumbnailUtils.extractThumbnail(bitmap,200,200);
                        carImage.setImageBitmap(bitmap);
                        display(carImage,addCar);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            default:
            if(resultCode==RESULT_OK){
                try{
                    Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    bitmap= ThumbnailUtils.extractThumbnail(bitmap,200,200);
                    billPhoto.setImageBitmap(bitmap);
                    display(billPhoto,addBill);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
            break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum(CHOOSE_CARD1);
                }
        }
    }

    protected void findCarType(){
        try{
            HttpUtil.find("http://10.0.2.2:8080/insurance/FindInfo","carType",new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result=response.body().string();
                    try{
                        JSONArray types=new JSONArray(result);
                        data=new String[types.length()];
                        for(int i=0;i<types.length();i++){
                            data[i]=types.getJSONObject(i).optString("id")+types.getJSONObject(i).optString("name");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void findBusiness(){
       try{
           HttpUtil.find("http://10.0.2.2:8080/insurance/FindInfo","business", new Callback() {
               @Override
               public void onFailure(Call call, IOException e) {

               }

               @Override
               public void onResponse(Call call, Response response) throws IOException {
                    String result=response.body().string();
                    Log.d("business",result);
                   try{
                       JSONArray businesses=new JSONArray(result);
                       insurances=new String[businesses.length()];
                       for(int i=0;i<businesses.length();i++){
                           insurances[i]=businesses.getJSONObject(i).getString("id")+businesses.getJSONObject(i).getString("name");
                       }
                   }catch (Exception e){
                       e.printStackTrace();
                   }
               }
           });
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    protected void findCompany(){
        try{
            HttpUtil.find("http://10.0.2.2:8080/insurance/FindInfo","company", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result=response.body().string();
                    Log.d("company",result);
                    try{
                        JSONArray comp=new JSONArray(result);
                        companies=new String[comp.length()];
                        for(int i=0;i<comp.length();i++){
                            companies[i]=comp.getJSONObject(i).getString("id")+comp.getJSONObject(i).getString("name");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void takePhoto(String name,int type){
        File outputImage=new File(getExternalCacheDir(),name);
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(AddCarInsuranceActivity.this,"insurance.edu.hust.ccstudio.insurance.fileprovider",outputImage);
        }else{
            imageUri=Uri.fromFile(outputImage);
        }
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,type);
    }

    protected void display(final ImageView imageView, final Button button){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            }
        });
    }

    //打开相册
    protected void openAlbum(int type){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,type);
    }
}
