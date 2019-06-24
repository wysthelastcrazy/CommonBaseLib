package com.wys.commonbaselib.bean;

public class UserBean {


    /**
     * userInfo : {"gradeId":0,"headPic":"","isGradeChoose":true,"isPwdEmpty":true,"nickName":"","sex":"","token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC93d3cuZ2Fvc2kuY29tIiwiYXVkIjoiaHR0cDpcL1wvd3d3Lmdhb3NpLmNvbSIsImlhdCI6MTUzNDczNDcxMSwibmJmIjoxNTM0NzM0NzExLCJleHAiOjE1MzUzMzk1MTEsInVpZCI6IjkifQ.cIXDGRnycTT39Yd9fU2b5no7QMeyX8QtGK-JbE-QDdw"}
     */

    private UserInfoBean userInfo;

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfoBean {
        /**
         * gradeId : 0
         * headPic :
         * isGradeChoose : true
         * isPwdEmpty : true
         * nickName :
         * sex :
         * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC93d3cuZ2Fvc2kuY29tIiwiYXVkIjoiaHR0cDpcL1wvd3d3Lmdhb3NpLmNvbSIsImlhdCI6MTUzNDczNDcxMSwibmJmIjoxNTM0NzM0NzExLCJleHAiOjE1MzUzMzk1MTEsInVpZCI6IjkifQ.cIXDGRnycTT39Yd9fU2b5no7QMeyX8QtGK-JbE-QDdw
         *reportUrl
         */

        private String headPic;
        private boolean isGradeChoose;
        private boolean isPwdEmpty;
        private String nickName;
        private String sex;
        private String token;
        private String userGradeName;
        private String userGradeId;
        private String reportUrl;
        private String alias;   //别名，umeng推送

        public String getUserGradeName() {
            return userGradeName;
        }

        public void setUserGradeName(String userGradeName) {
            this.userGradeName = userGradeName;
        }

        public String getUserGradeId() {
            return userGradeId;
        }

        public void setUserGradeId(String userGradeId) {
            this.userGradeId = userGradeId;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public boolean isIsGradeChoose() {
            return isGradeChoose;
        }

        public void setIsGradeChoose(boolean isGradeChoose) {
            this.isGradeChoose = isGradeChoose;
        }

        public boolean isIsPwdEmpty() {
            return isPwdEmpty;
        }

        public void setIsPwdEmpty(boolean isPwdEmpty) {
            this.isPwdEmpty = isPwdEmpty;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getReportUrl() {
            return reportUrl;
        }

        public void setReportUrl(String reportUrl) {
            this.reportUrl = reportUrl;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }
    }
}
