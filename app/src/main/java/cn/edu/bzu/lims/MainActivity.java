package cn.edu.bzu.lims;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.umeng.update.UmengUpdateAgent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.edu.bzu.lims.activity.DetailsActivity;
import cn.edu.bzu.lims.activity.DutyActivity;
import cn.edu.bzu.lims.activity.LoginActivity;
import cn.edu.bzu.lims.activity.MyNewsActivity;
import cn.edu.bzu.lims.activity.NewActivity;
import cn.edu.bzu.lims.activity.SettingActivity;
import cn.edu.bzu.lims.activity.UserInformActivity;
import cn.edu.bzu.lims.adapter.InformRecyclerAdapter;
import cn.edu.bzu.lims.bean.News;
import cn.edu.bzu.lims.bean.User;
import cn.edu.bzu.lims.receiver.MyPushMessageReceiver;
import cn.edu.bzu.lims.util.ImageLoader;
import cn.edu.bzu.lims.util.TestNetWork;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    private Toolbar mToolBar;

    private RecyclerView mRecyclerView;
    private InformRecyclerAdapter mAdapter;

    private FloatingActionButton mFab;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView; //菜单的所在布局
    private ActionBarDrawerToggle mActionBarDrawerToggle;    //ActionBarDrawerToggle 点击菜单出现与关闭

    private SwipeRefreshLayout swipeRefreshLayout;

    public BmobUser userCurrent;

   private ProgressWheel progressWheel;

    private CircleImageView iv_UserPhoto;
    private TextView tv_name;
    private TextView tv_major;

    private String objectId;
    private Boolean isNet; //检测网络状态
    private BmobQuery<News> query;

    private MyPushMessageReceiver receiver ; //广播

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkNet();
    }

    /**
     * 进入程序的操作
     */
    private void firstEnter() {
        userCurrent=BmobUser.getCurrentUser(this, User.class);
        if(userCurrent==null){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }else{
        //TODO 推送消息的 Notification 设置
            registerReceiver(receiver,new IntentFilter("PushConstants.ACTION_MESSAGE"));

            initView();
            initToolBar();
            initSwipe();
            getData(); //初始化数据源
            initAction();
        }
    }

    /**
     * 检测网络状态
     */
    private void checkNet() {
        isNet=new TestNetWork().isNetworkAvailable(this);
        if(isNet){
            UmengUpdateAgent.update(this);
            firstEnter();
        }else {
            //TODO 这个地方存在bug -- 点击否的处理问题
            new TestNetWork().showNetDialog(this);
            firstEnter();
        }
    }


    /**
     * 得到网络数据
     */
    private void getData() {
        query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存获取数据，如果没有，再从网络获取。
        query.include("user"); //包含作者
        query.setLimit(30);  //限制接收的数据数量
        query.order("-updatedAt");
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(7));//此表示缓存七天天
        boolean isCache = query.hasCachedResult(this,News.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.findObjects(MainActivity.this, new FindListener<News>() {
            @Override
            public void onSuccess(List<News> list) {
                progressWheel.setVisibility(View.INVISIBLE);
                if (list != null) {
                    initData(list);
                } else {
                    Toast.makeText(MainActivity.this, "暂时没有公告", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int i, String s) {
             //  Toast.makeText(MainActivity.this, "查询数据失败", Toast.LENGTH_SHORT).show();
            }
        });

        //初始化左侧菜单栏
        objectId = userCurrent.getObjectId();
        BmobQuery<User> userBmobQuery =  new BmobQuery<>();
        userBmobQuery.getObject(MainActivity.this, objectId, new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
                String url =user.getUserPhoto().getFileUrl(MainActivity.this);
                iv_UserPhoto.setTag(url);
                new ImageLoader().showImageByThread(iv_UserPhoto,url);
                tv_name.setText(user.getName());
                tv_major.setText(user.getMajor());
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e("UserQuery","查询左侧菜单栏用户信息出现错误:"+s);
            }
        });
    }

    /**
     * 对数据进行赋值
     * @param list
     */
    private void initData(List<News> list) {
        mAdapter= new InformRecyclerAdapter(list,this);
        mRecyclerView.setAdapter(mAdapter);

        //设置RecyclerView的布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        //设置RecyclerView的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

      mAdapter.setOnItemClickListener(new InformRecyclerAdapter.OnItemClickListener() {
          @Override
          public void OnItemClick(int position, View view) {
              View v =  mRecyclerView.getChildAt(position);
              TextView tv_objectId = (TextView) v.findViewById(R.id.tv_objectId);
              String objectId = tv_objectId.getText().toString();
              //公告详情页面的跳转
              Intent i =new Intent(MainActivity.this, DetailsActivity.class);
              i.putExtra("objectId",objectId);
              startActivity(i);
          }

          @Override
          public void OnItemLongClick(int position, View view) {
             // Toast.makeText(MainActivity.this, "Click: " + position, Toast.LENGTH_SHORT).show();
          }
      });
    }

    /**
     * 借助SwipeRefreshLayout实现下拉刷新的操作
     */
    private void initSwipe() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        //设置刷新时动画的颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 刷新的时候获取数据
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                        getData();
                        Toast.makeText(MainActivity.this, "刷新结束", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    /**
     * 初始化左侧菜单动作
     */
    private void initAction() {
        iv_UserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userInformIntent = new Intent(MainActivity.this, UserInformActivity.class);
                startActivity(userInformIntent);
            }
        });

        //设置侧滑菜单点击监听
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    // 菜单操作
                    case R.id.menu_item_user:
                        Intent userInformIntent = new Intent(MainActivity.this, UserInformActivity.class);
                        startActivity(userInformIntent);
                        break;
                    case R.id.menu_item_watch:
                        Intent dutyIntent = new Intent(MainActivity.this, DutyActivity.class);
                        startActivity(dutyIntent);
                        break;
                    case R.id.menu_item_news:
                        Intent myNewsIntent = new Intent(MainActivity.this, MyNewsActivity.class);
                        startActivity(myNewsIntent);
                        break;
                    case R.id.menu_item_setting:
                        Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(settingIntent);
                        break;
                    case R.id.menu_item_exit:
                        userCurrent.logOut(MainActivity.this);
                        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return false;
            }
        });

        //Fab事件
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 发布内容的处理
                Intent  newIntent = new Intent(MainActivity.this, NewActivity.class);
                startActivity(newIntent);
                finish();
            }
        });
    }

    private void initView() {
       progressWheel= (ProgressWheel) findViewById(R.id.progress_wheel);
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView= (RecyclerView) findViewById(R.id.rv_news);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.dl_drawer);
        mNavigationView= (NavigationView) findViewById(R.id.nv_main_menu);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        progressWheel.setVisibility(View.VISIBLE);

        iv_UserPhoto= (CircleImageView) findViewById(R.id.iv_userPhoto);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_major= (TextView) findViewById(R.id.tv_major);
    }


    private void initToolBar() {
        mToolBar.setTitle("机房公告");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.drawer_open, R.string.drawer_close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

// 未启用右侧菜单栏

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
