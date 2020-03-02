package com.wys.commonbaselib.im;

/**
 * Created by yas on 2020-02-19
 * Describe:录制件播放，聊天室消息实体类
 */
public class GslRecMessage {
    private String userId;
    private String nickName;
    private String userRole;
    private String content;
    private boolean isSelf;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }
}
