package insurance.edu.hust.ccstudio.insurance;

import android.app.Activity;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class FindCustomerActivity extends TabActivity {

    protected String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("TabDemoActivity");
        username=getIntent().getStringExtra("username");
        TabHost tabHost = getTabHost();
        tabHost.getTabWidget().bringToFront();
        LayoutInflater.from(this).inflate(R.layout.activity_find_customer,
                tabHost.getTabContentView(),true);
        tabHost.addTab(tabHost.newTabSpec("findCustomer").setIndicator("我的保单", getResources().getDrawable(R.drawable.add))
                .setContent(R.id.view1));
        tabHost.addTab(tabHost.newTabSpec("checkBills").setIndicator("查询处理表单")
                .setContent(R.id.view2));
        tabHost.addTab(tabHost.newTabSpec("addBills").setIndicator("添加保单")
                .setContent(R.id.view3));
        tabHost.addTab(tabHost.newTabSpec("userCenter").setIndicator("个人中心")
                .setContent(R.id.view4));

        tabHost.addTab(tabHost.newTabSpec("contactUs").setIndicator("联系我们")
                .setContent(R.id.view4));

        //标签切换事件处理，setOnTabChangedListener
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("addBills")) { //第三个标签
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final String data[]={"车险","寿险","财产险","意外险"};
                            new AlertDialog.Builder(FindCustomerActivity.this).setTitle("请选择需要添加的保险类型")
                                    .setItems(data,new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(data[i].equals("车险")){
                                                Intent intent=new Intent(FindCustomerActivity.this,AddCarInsuranceActivity.class);
                                                intent.putExtra("username",username);
                                                startActivity(intent) ;
                                            }else if(data[i].equals("寿险")){
                                                Intent intent=new Intent(FindCustomerActivity.this,AddLifeInsuranceActivity.class);
                                                intent.putExtra("username",username);
                                                startActivity(intent);
                                            }else if(data[i].equals("财产险")){
                                                Intent intent=new Intent(FindCustomerActivity.this,AddWealthActivity.class);
                                                intent.putExtra("username",username);
                                                startActivity(intent);
                                            }else{
                                                Intent intent=new Intent(FindCustomerActivity.this,AddAccidentActivity.class);
                                                intent.putExtra("username",username);
                                                startActivity(intent);
                                            }
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                        }
                    });
                }else if(tabId.equals("checkBills")){
                    findCheckBills();
                }else if(tabId.equals("userCenter")){
                    findUserInfo();
                }
            }
        });
    }

    protected void findCheckBills(){

    }

    protected void findUserInfo(){

    }
}
