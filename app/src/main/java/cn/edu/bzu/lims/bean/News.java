package cn.edu.bzu.lims.bean;

import cn.bmob.v3.BmobObject;

/**
 * 公告实体类
 * Created by monster on 2015/10/22.
 */
public class News extends BmobObject {

    private String title ;  //标题
    private String content ; //内容
    private User user;  //发布人的具体信息

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
