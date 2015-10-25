package cn.edu.bzu.lims.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.edu.bzu.lims.R;
import cn.edu.bzu.lims.bean.User;
import cn.edu.bzu.lims.util.ImageLoader;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by monster on 2015/10/22.
 */
public class UserInformActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private BmobUser bmobUser;
    private CircleImageView ivUserPhoto;
    private TextView tv_name ,tv_grade,tv_major,tv_phone,tv_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinform);
        bmobUser=BmobUser.getCurrentUser(this);
        initToolBar();
        initView();
        getInform();
    }

    /**
     * 得到个人信息
     */
    private void getInform() {
       String objectId =  bmobUser.getObjectId();

        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(this, objectId, new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
                String url =user.getUserPhoto().getFileUrl(UserInformActivity.this);
                ivUserPhoto.setTag(url);
                new ImageLoader().showImageByThread(ivUserPhoto, url);

                tv_name.setText(user.getName());
                tv_grade.setText(user.getGrade());
                tv_major.setText(user.getMajor());
                tv_phone.setText(user.getMobilePhoneNumber());
                tv_mail.setText(user.getEmail());

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserInformActivity.this,"查询数据失败"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initView() {
        ivUserPhoto= (CircleImageView) findViewById(R.id.iv_userPhoto);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_grade= (TextView) findViewById(R.id.tv_grade);
        tv_major= (TextView) findViewById(R.id.tv_major);
        tv_phone= (TextView) findViewById(R.id.tv_phone);
        tv_mail= (TextView) findViewById(R.id.tv_mail);
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("个人资料");
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationIcon(R.mipmap.ic_menu_back);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
