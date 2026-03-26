package com.mall.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String username;
    
    @JsonIgnore
    private String password;
    private String realName;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
    private Integer role;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public static final int STATUS_DISABLED = 0;
    public static final int STATUS_ENABLED = 1;
    public static final int ROLE_USER = 0;
    public static final int ROLE_ADMIN = 1;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getRole() {
        return role;
    }
    
    public void setRole(Integer role) {
        this.role = role;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getStatusText() {
        return status != null && status == 1 ? "启用" : "禁用";
    }
    
    public String getRoleText() {
        return role != null && role == 1 ? "管理员" : "普通用户";
    }
}
