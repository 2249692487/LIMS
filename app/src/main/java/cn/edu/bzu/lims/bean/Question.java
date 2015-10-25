package cn.edu.bzu.lims.bean;

import cn.bmob.v3.BmobObject;

/**
 * 问题反馈
 * Created by monster on 2015/10/24.
 */
public class Question extends BmobObject {
    private String Ques; //问题

    public String getQues() {
        return Ques;
    }

    public void setQues(String ques) {
        Ques = ques;
    }
}
