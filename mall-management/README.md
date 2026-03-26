# 商城管理系统

## 项目简介
基于Spring Boot + MyBatis + MySQL + Redis + Bootstrap构建的商城管理系统。

## 技术栈
- 后端：Spring Boot 2.7.18 + MyBatis + MySQL 8.0 + Redis
- 前端：Bootstrap 5 + Thymeleaf
- 构建工具：Maven

## 项目结构
```
mall-management/
├── pom.xml                          # Maven配置文件
├── sql/
│   └── mall_management.sql          # 数据库脚本
└── src/main/
    ├── java/com/mall/
    │   ├── common/                  # 通用类
    │   ├── config/                  # 配置类
    │   ├── controller/              # 控制器层
    │   ├── entity/                  # 实体类
    │   ├── mapper/                  # MyBatis Mapper接口
    │   ├── service/                 # 服务层
    │   ├── util/                    # 工具类
    │   └── MallManagementApplication.java  # 启动类
    └── resources/
        ├── mapper/                  # MyBatis XML映射文件
        ├── templates/               # Thymeleaf模板
        └── application.yml          # 应用配置
```

## 功能模块
1. **用户登录** - 用户认证与会话管理
2. **用户管理** - 用户增删改查、启用/停用功能
3. **商品管理** - 商品增删改查、图片上传
4. **订单管理** - 订单查看、关闭订单
5. **账单管理** - 账单查看、Excel导出
6. **物流管理** - 物流信息更新

## 运行步骤

### 1. 环境准备
- JDK 1.8+
- MySQL 8.0+
- Redis
- Maven 3.6+

### 2. 数据库配置
```sql
-- 执行SQL脚本创建数据库和表
source sql/mall_management.sql
```

### 3. 修改配置
编辑 `src/main/resources/application.yml`，修改数据库和Redis连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mall_management?...
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: 
```

### 4. 启动项目
```bash
cd mall-management
mvn spring-boot:run
```

### 5. 访问系统
- 地址：http://localhost:8080
- 账号：admin
- 密码：admin123

## 默认账号
| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 管理员 |
| user1 | admin123 | 普通用户 |
