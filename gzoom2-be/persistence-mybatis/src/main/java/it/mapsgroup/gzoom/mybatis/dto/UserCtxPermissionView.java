package it.mapsgroup.gzoom.mybatis.dto;

public class UserCtxPermissionView {
    private String userLoginId;
    private String userCtx;
    private String ctxPermission;


    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getUserCtx() {
        return userCtx;
    }

    public void setUserCtx(String userCtx) {
        this.userCtx = userCtx;
    }

    public String getCtxPermission() {
        return ctxPermission;
    }

    public void setCtxPermission(String ctxPermission) {
        this.ctxPermission = ctxPermission;
    }
}
