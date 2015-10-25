package cn.edu.bzu.lims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.edu.bzu.lims.MainActivity;
import cn.edu.bzu.lims.R;
import cn.edu.bzu.lims.bean.News;
import cn.edu.bzu.lims.bean.User;

/**
 * Created by monster on 2015/10/22.
 */
public class NewActivity extends AppCompatActivity {

    private Toolbar mToolBar ;
    private MaterialEditText edt_title ;
    private MaterialEditText edt_content;
    private String title ,content;
    private User userCurrent;  //当前登录的用户
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_new);
        initToolBar();
        initView();
    }



    /**
     * 初始化视图
     */
    private void initView() {
        edt_title= (MaterialEditText) findViewById(R.id.edt_title);
        edt_content= (MaterialEditText) findViewById(R.id.edt_content);
        userCurrent= BmobUser.getCurrentUser(NewActivity.this,User.class);
    }

    private void initToolBar() {
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("发布新公告");
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationIcon(R.mipmap.ic_menu_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            sendTo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 发布公告
     */
    private void sendTo() {
        title=edt_title.getText().toString();
        content=edt_content.getText().toString();
        if(TextUtils.isEmpty(title)&&TextUtils.isEmpty(content)){
            Toast.makeText(NewActivity.this,"账号或密码未填写",Toast.LENGTH_SHORT).show();
        }else{
            News news =new News();
            news.setTitle(title);
            news.setContent(content);
            news.setUser(userCurrent);

            news.save(NewActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    //TODO  关于Finish的处理
                    Toast.makeText(NewActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(NewActivity.this, MainActivity.class);
                    startActivity(i);
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(NewActivity.this,"添加失败"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
