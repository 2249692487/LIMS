package cn.edu.bzu.lims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.edu.bzu.lims.R;
import cn.edu.bzu.lims.bean.News;
import cn.edu.bzu.lims.bean.User;
import cn.edu.bzu.lims.util.TestNetWork;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * 设置界面
 * Created by monster on 2015/10/22.
 */
public class SettingActivity extends AppCompatActivity implements  View.OnClickListener{
    private Toolbar mToolBar;
    private Button btn_sms , btn_trash , btn_update , btn_help , btn_about , btn_exit;
    //private BmobQuery<News> query;
    public BmobUser userCurrent;
    private  MaterialDialog mMaterialDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        userCurrent=BmobUser.getCurrentUser(this, User.class);
        initToolBar();
        initView();
        initEvent();
    }

    /**
     * 注册监听事件
     */
    private void initEvent() {
        btn_sms.setOnClickListener(this);
        btn_trash.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_help.setOnClickListener(this);
        btn_about.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        btn_sms= (Button) findViewById(R.id.btn_sms);
        btn_trash= (Button) findViewById(R.id.btn_trash);
        btn_update= (Button) findViewById(R.id.btn_update);
        btn_help= (Button) findViewById(R.id.btn_help);
        btn_about= (Button) findViewById(R.id.btn_about);
        btn_exit= (Button) findViewById(R.id.btn_exit);
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("设置");
        mToolBar.setNavigationIcon(R.mipmap.ic_menu_back);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sms:
                //TODO 消息推送
                Toast.makeText(SettingActivity.this, "由于管理员限制，消息推送不可关闭", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_trash:
                BmobQuery.clearAllCachedResults(this); //清除缓存数据
                Toast.makeText(SettingActivity.this, "清除缓存完成", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_update:
                Boolean isNet=new TestNetWork().isNetworkAvailable(this);
                if(isNet){
                    UmengUpdateAgent.forceUpdate(SettingActivity.this);  //手动检查更新
                    UmengUpdateAgent.setUpdateAutoPopup(false);
                    UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

                        @Override
                        public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                            switch (updateStatus) {
                                case UpdateStatus.Yes: //存在更新
                                    UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
                                    break;
                                case UpdateStatus.No: // 没有更新
                                    Toast.makeText(SettingActivity.this, "已是最新", Toast.LENGTH_SHORT).show();
                                    break;
                                case UpdateStatus.Timeout:
                                    Toast.makeText(SettingActivity.this, "超时", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                    UmengUpdateAgent.update(SettingActivity.this);
                }else{
                    Toast.makeText(SettingActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_help:
                //反馈
                Intent i = new Intent(SettingActivity.this,QuestionActivity.class);
                startActivity(i);
                break;
            case R.id.btn_about:
                 mMaterialDialog = new MaterialDialog(this)
                        .setTitle("About Me")
                        .setMessage("We are a free team and we expect you to join us.")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();

                            }
                        })
                        .setNegativeButton("CANCEL", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();

                            }
                        });
                mMaterialDialog.show();
                break;
            case R.id.btn_exit:
                userCurrent.logOut(SettingActivity.this);
                Intent loginIntent = new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;

        }
    }
}
