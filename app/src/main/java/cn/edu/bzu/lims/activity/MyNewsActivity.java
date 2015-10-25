package cn.edu.bzu.lims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.edu.bzu.lims.R;
import cn.edu.bzu.lims.adapter.InformRecyclerAdapter;
import cn.edu.bzu.lims.bean.News;
import cn.edu.bzu.lims.bean.User;

/**
 * 我的公告
 * Created by monster on 2015/10/22.
 */
public class MyNewsActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private RecyclerView mRecyclerView;
    private InformRecyclerAdapter mAdapter;
    private List<News>  mList;
    private BmobUser userCurrent;  //当前登录用户
    private String objectId;
    private ProgressWheel progressWheel;
    private TextView tv_tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mynews);
        userCurrent=BmobUser.getCurrentUser(this, User.class);
        if(userCurrent==null){
            Toast.makeText(this,"用户未登录",Toast.LENGTH_SHORT).show();
        }else {
            initToolBar();
            initView();
            initData();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        objectId=userCurrent.getObjectId();
        BmobQuery<News> query = new BmobQuery<>();
        query.include("user");
        query.addWhereEqualTo("user", objectId);
        query.findObjects(MyNewsActivity.this, new FindListener<News>() {
            @Override
            public void onSuccess(List<News> list) {
                progressWheel.setVisibility(View.INVISIBLE);
                if(list!=null){
                    toDoList(list);
                }else {
                    tv_tips.setVisibility(View.VISIBLE);
                    Toast.makeText(MyNewsActivity.this,"你暂时没有发公告",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyNewsActivity.this,"FAILED"+s,Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 对RecyclerView进行操作
     */
    private void toDoList(List<News> list) {
        mAdapter= new InformRecyclerAdapter(list,this);
        mRecyclerView.setAdapter(mAdapter);

        //设置RecyclerView的布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        //设置RecyclerView的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // 查看个人公告的详情

        mAdapter.setOnItemClickListener(new InformRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View view) {
                View v =mRecyclerView.getChildAt(position);
                TextView tv_objectId = (TextView) v.findViewById(R.id.tv_objectId);
                String objectId = tv_objectId.getText().toString();
                //公告详情页面的跳转
                Intent i =new Intent(MyNewsActivity.this, MyNewsDetailsActivity.class);
                i.putExtra("objectId",objectId);
                startActivity(i);
            }

            @Override
            public void OnItemLongClick(int position, View view) {

            }
        });
    }

    private void initView() {
        tv_tips= (TextView) findViewById(R.id.tv_tips);
        progressWheel= (ProgressWheel) findViewById(R.id.progress_wheel);
        mRecyclerView= (RecyclerView) findViewById(R.id.rv_mynews);
        mList=new ArrayList<>();
        progressWheel.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("我的公告");
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
