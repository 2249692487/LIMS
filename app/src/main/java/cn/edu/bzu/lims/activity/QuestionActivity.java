package cn.edu.bzu.lims.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.listener.SaveListener;
import cn.edu.bzu.lims.R;
import cn.edu.bzu.lims.bean.Question;

/**
 * Created by monster on 2015/10/24.
 */
public class QuestionActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private MaterialEditText edt_question_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_question);
        initToolBar();
        initView();
    }

    private void initView() {
        edt_question_text= (MaterialEditText) findViewById(R.id.edt_question_text);
    }

    private void initToolBar() {
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("问题反馈");
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
            uploadQuestion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 反馈问题
     */
    private void uploadQuestion() {
        String question =edt_question_text.getText().toString();
        if(TextUtils.isEmpty(question)){
            Toast.makeText(QuestionActivity.this,"问题未填写",Toast.LENGTH_SHORT).show();
        }else {
            Question ques = new Question();
            ques.setQues(question);
            ques.save(QuestionActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    finish();
                    Toast.makeText(QuestionActivity.this,"我们已收到你的反馈，谢谢",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(QuestionActivity.this,"发送反馈失败"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
