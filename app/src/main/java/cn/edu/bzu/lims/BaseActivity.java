package cn.edu.bzu.lims;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

/**
 * 程序的基础类
 * Created by monster on 2015/10/21.
 */
public class BaseActivity extends AppCompatActivity {

    private static  final String  APPID = "03eeac1ab410952076bdbd03c1682cdb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //初始化BmobSDK
        Bmob.initialize(this,APPID);
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, APPID);
    }

    /**
     * 弹出Toast语句
     * @param s
     */
    public void toast(String s){
        Toast.makeText(this,""+s,Toast.LENGTH_SHORT).show();
    }
}
