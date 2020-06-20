package com.test.commonporject.test;


import com.google.gson.annotations.SerializedName;

public class InfoBean {

    private UserBean info;

    public UserBean getInfo() {
        return info;
    }

    public void setInfo(UserBean info) {
        this.info = info;
    }

    public static class UserBean {
        private String userId;
        @SerializedName(value = "uid", alternate = "id")
        private String uid;
        @SerializedName(value = "username", alternate = "name")
        private String userName;
        @SerializedName("realname")
        private String realName;
        @SerializedName("vip_status")
        private String vipStatus;
        @SerializedName("vip_grade")
        private String vipGrade;
        private String avatar;
        private String rongyunToken;
        private String roomId;
        private String type;
        private String logo;
        private boolean fansStatus;//与我的关系
        private boolean attentionStatus;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getVipStatus() {
            return vipStatus;
        }

        public void setVipStatus(String vipStatus) {
            this.vipStatus = vipStatus;
        }

        public String getVipGrade() {
            return vipGrade;
        }

        public void setVipGrade(String vipGrade) {
            this.vipGrade = vipGrade;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getRongyunToken() {
            return rongyunToken;
        }

        public void setRongyunToken(String rongyunToken) {
            this.rongyunToken = rongyunToken;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public boolean isFansStatus() {
            return fansStatus;
        }

        public void setFansStatus(boolean fansStatus) {
            this.fansStatus = fansStatus;
        }

        public boolean isAttentionStatus() {
            return attentionStatus;
        }

        public void setAttentionStatus(boolean attentionStatus) {
            this.attentionStatus = attentionStatus;
        }
    }
}
