package com.aebiz.app.member.modules.commons.vo;

/**
 * 会员列表
 * Created by yangjian on 2017/6/14.
 */
public class Member {
    private String id;              // 会员id,member_user的主键
    private String accountId;
    private String username;
    private String nickname;
    private String email;
    private String mobile;
    private String money;
    private String score;
    private String type;
    private String level;
    private String state;           // 会员状态 1正常,2冻结

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTypeName() {
        return type;
    }

    public void setTypeName(String type) {
        this.type = type;
    }

    public String getLevelName() {
        return level;
    }

    public void setLevelName(String levelName) {
        this.level = level;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
