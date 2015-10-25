package cn.edu.bzu.lims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import cn.bmob.v3.listener.SaveListener;
import cn.edu.bzu.lims.BaseActivity;
import cn.edu.bzu.lims.MainActivity;
import cn.edu.bzu.lims.R;
import cn.edu.bzu.lims.bean.User;

/**
 * 程序登陆
 * @author monster
 * @date 2015-10-21
 */

public class LoginActivity extends BaseActivity {
    private Toolbar mToolBar;
    private ProgressWheel progressWheel;
    private EditText edt_user , edt_password;
    private Button btn_login;
    private String mUserString , mPassString ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolBar();
        initView();
        initEvent();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        edt_user= (EditText) findViewById(R.id.edt_user);
        edt_password= (EditText) findViewById(R.id.edt_password);
        btn_login= (Button) findViewById(R.id.btn_login);

        progressWheel= (ProgressWheel) findViewById(R.id.progress_wheel);

    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("LIMS");
        setSupportActionBar(mToolBar);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUserString=edt_user.getText().toString();
                mPassString=edt_password.getText().toString();

                if(TextUtils.isEmpty(mUserString)||TextUtils.isEmpty(mPassString)){
                    Toast.makeText(LoginActivity.this,"账号或密码未填写",Toast.LENGTH_SHORT).show();
                }else {
                    progressWheel.setVisibility(View.VISIBLE);
                    User user = new User();
                    user.setUsername(mUserString);
                    user.setPassword(mPassString);

                    user.login(LoginActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            progressWheel.setVisibility(View.INVISIBLE);
                            toast("登陆成功");
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            progressWheel.setVisibility(View.INVISIBLE);
                            toast("登陆失败"+s);
                        }
                    });
                }

            }
        });
    }

}
