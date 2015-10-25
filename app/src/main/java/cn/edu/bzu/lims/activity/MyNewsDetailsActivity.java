package cn.edu.bzu.lims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.GetListener;
import cn.edu.bzu.lims.R;
import cn.edu.bzu.lims.bean.News;
import cn.edu.bzu.lims.util.ImageLoader;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by monster on 2015/10/24.
 */
public class MyNewsDetailsActivity extends AppCompatActivity {
    private String objectId;
    private Toolbar mToolBar;
    private CircleImageView iv_userPhoto;
    private TextView tv_name,tv_date,tv_title,tv_content;
    private String fileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO 布局和公告详情一样
        setContentView(R.layout.activity_details);
        objectId = getIntent().getStringExtra("objectId");
        initToolBar();
        initView();
        queryData();
    }
    /**
     * 根据id查询数据
     */
    private void queryData() {
        BmobQuery<News> query = new BmobQuery<News>();
        query.include("user"); //包含作者
        query.getObject(MyNewsDetailsActivity.this, objectId, new GetListener<News>() {
            @Override
            public void onSuccess(News news) {
                //得到对应的值
                fileUrl = news.getUser().getUserPhoto().getFileUrl(MyNewsDetailsActivity.this);
                iv_userPhoto.setTag(fileUrl);
                new ImageLoader().showImageByThread(iv_userPhoto, fileUrl);
                tv_name.setText(news.getUser().getName());
                tv_date.setText(news.getUpdatedAt());
                tv_title.setText(news.getTitle());
                tv_content.setText(news.getContent());
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(MyNewsDetailsActivity.this, "查询数据失败" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 初始化视图
     */
    private void initView() {
        iv_userPhoto= (CircleImageView) findViewById(R.id.iv_userPhoto);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_date= (TextView) findViewById(R.id.tv_date);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_content= (TextView) findViewById(R.id.tv_content);
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("公告详情");
        mToolBar.setNavigationIcon(R.mipmap.ic_menu_back);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_delete:
                        deleteNews();
                        break;
                    case R.id.action_update:
                        updateNews();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 更新新闻
     */
    private void updateNews() {
        Intent updateIntent = new Intent(MyNewsDetailsActivity.this,UpdateActivity.class);
        updateIntent.putExtra("objectId",objectId);
        startActivity(updateIntent);
    }

    /**
     * 删除公告
     */
    private void deleteNews() {
        News news=new News();
        news.setObjectId(objectId);
        news.delete(this, new DeleteListener() {
            @Override
            public void onSuccess() {
                finish();
                Toast.makeText(MyNewsDetailsActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MyNewsDetailsActivity.this,MyNewsActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(MyNewsDetailsActivity.this,"删除失败"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_operate,menu);
        return  true;
    }
}
