package cn.edu.bzu.lims.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import cn.edu.bzu.lims.R;
import cn.edu.bzu.lims.bean.News;
import cn.edu.bzu.lims.util.ImageLoader;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 公告详情页面
 * Created by monster on 2015/10/22.
 */
public class DetailsActivity extends AppCompatActivity{
    private Toolbar mToolBar;
    private CircleImageView  iv_userPhoto;
    private TextView tv_name,tv_date,tv_title,tv_content;
    private String objectId;
    private String fileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        query.getObject(DetailsActivity.this, objectId, new GetListener<News>() {
            @Override
            public void onSuccess(News news) {
                //得到对应的值
                fileUrl = news.getUser().getUserPhoto().getFileUrl(DetailsActivity.this);
                iv_userPhoto.setTag(fileUrl);
                new ImageLoader().showImageByThread(iv_userPhoto,fileUrl);
                tv_name.setText(news.getUser().getName());
                tv_date.setText(news.getUpdatedAt());
                tv_title.setText(news.getTitle());
                tv_content.setText(news.getContent());
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(DetailsActivity.this,"查询数据失败"+s,Toast.LENGTH_SHORT).show();
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
    }
}
