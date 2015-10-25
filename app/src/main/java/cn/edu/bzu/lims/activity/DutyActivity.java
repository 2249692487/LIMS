package cn.edu.bzu.lims.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.edu.bzu.lims.R;

/**
 * 值班表
 * Created by monster on 2015/10/22.
 */
public class DutyActivity extends AppCompatActivity {
    private Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);
        initToolBar();
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("值班表");
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
