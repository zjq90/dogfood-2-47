package com.mall.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private Integer status;
    private String role;
    private Date createTime;
    private Date updateTime;
}
