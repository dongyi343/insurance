package insurance.edu.hust.ccstudio.insurance;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by stacy on 2017/8/16.
 */

public class HttpUtil {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public static OkHttpClient client = new OkHttpClient();

    public static void jsonPost(final String url, final String json,final okhttp3.Callback listener) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(listener);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    public static void getJson(final String address, final okhttp3.Callback listener) throws  IOException{
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    Request request=new Request.Builder().url(address).build();
//                    client.newCall(request).enqueue(listener);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    public static void getAllInfo(final String address, final String[] paths,final String infos,final okhttp3.Callback listener) throws IOException{
       new Thread(new Runnable() {
           @Override
           public void run() {
               try{
                   MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM);
                   for(int i=0;i<paths.length;i++){
                       File file=new File(paths[i]);
                       if(file!=null){
                           builder.addFormDataPart("img", file.getName(),RequestBody.create(MEDIA_TYPE_PNG,file));
                       }
                   }
                   builder.addFormDataPart("info",infos);
                   MultipartBody body=builder.build();
                   Request request=new Request.Builder().url(address).post(body).build();
                   client.newCall(request).enqueue(listener);
               }catch (Exception e){
                   e.printStackTrace();
               }
           }
       }).start();
    }

    public static void find(final String address,final String info,final Callback listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    RequestBody requestBody=new FormBody.Builder().add("info",info).build();
                    Request request=new Request.Builder().url(address).post(requestBody).build();
                    client.newCall(request).enqueue(listener);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
