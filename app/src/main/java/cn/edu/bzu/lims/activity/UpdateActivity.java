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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.edu.bzu.lims.MainActivity;
import cn.edu.bzu.lims.R;
import cn.edu.bzu.lims.bean.News;

/**
 * Created by monster on 2015/10/24.
 */
public class UpdateActivity extends AppCompatActivity {
    private MaterialEditText edt_title ,edt_content;
    private String objectId;
    private Toolbar mToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 布局和创建以公告一样
        setContentView(R.layout.activtiy_new);
        objectId = getIntent().getStringExtra("objectId");
        initToolBar();
        initView();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        edt_title= (MaterialEditText) findViewById(R.id.edt_title);
        edt_content= (MaterialEditText) findViewById(R.id.edt_content);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        BmobQuery<News> query = new BmobQuery<>();
        query.getObject(UpdateActivity.this, objectId, new GetListener<News>() {
            @Override
            public void onSuccess(News news) {
                edt_title.setText(news.getTitle());
                edt_content.setText(news.getContent());
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UpdateActivity.this,"查询数据失败",Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initToolBar() {
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("更新公告");
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
            String title = edt_title.getText().toString();
            String content =edt_content.getText().toString();

            if(TextUtils.isEmpty(title)||TextUtils.isEmpty(content)){
                Toast.makeText(UpdateActivity.this,"标题或内容未填写",Toast.LENGTH_SHORT).show();
            }else {
                News news = new News();
                news.setTitle(title);
                news.setContent(content);
                news.update(this, objectId, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        finish();
                        Toast.makeText(UpdateActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdateActivity.this, MainActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(UpdateActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
