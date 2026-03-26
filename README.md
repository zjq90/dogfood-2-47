# 商城管理系统 (Mall Admin)

基于Spring Boot + MyBatis + Redis + Bootstrap的商城后台管理系统

## 技术栈

- **后端框架**: Spring Boot 2.7.14
- **ORM框架**: MyBatis
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **前端**: Bootstrap 5 + Thymeleaf
- **构建工具**: Maven
- **分页插件**: PageHelper

## 功能模块

1. **用户管理** - 用户登录、用户列表、启用/禁用用户
2. **商品管理** - 商品CRUD、图片上传、上架/下架
3. **订单管理** - 订单列表、关闭订单、订单发货
4. **账单管理** - 账单记录、Excel导出
5. **物流管理** - 物流信息更新、物流跟踪

## 项目结构

```
├── src/main/java/com/mall/
│   ├── MallAdminApplication.java    # 启动类
│   ├── config/                      # 配置类
│   ├── controller/                  # 控制器层
│   ├── entity/                      # 实体类
│   ├── mapper/                      # MyBatis Mapper接口
│   ├── interceptor/                 # 拦截器
│   └── service/                     # 业务层
│       └── impl/                    # 业务实现
├── src/main/resources/
│   ├── mapper/                      # MyBatis XML映射文件
│   ├── templates/                   # Thymeleaf模板
│   │   ├── fragments/               # 公共页面片段
│   │   ├── user/                    # 用户管理页面
│   │   ├── product/                 # 商品管理页面
│   │   ├── order/                   # 订单管理页面
│   │   ├── bill/                    # 账单管理页面
│   │   └── logistics/               # 物流管理页面
│   ├── static/                      # 静态资源
│   ├── application.yml              # 配置文件
│   └── db/schema.sql                # 数据库脚本
└── pom.xml                          # Maven配置
```

## 环境要求

- JDK 1.8+
- MySQL 5.7+
- Redis 5.0+
- Maven 3.6+

## 快速开始

### 1. 创建数据库

```sql
-- 执行数据库脚本
mysql -u root -p < src/main/resources/db/schema.sql
```

### 2. 配置数据库连接

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mall_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
  
  redis:
    host: localhost
    port: 6379
```

### 3. 编译运行

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

### 4. 访问系统

打开浏览器访问: http://localhost:8080

默认账号:
- 用户名: `admin`
- 密码: `admin123`

## 功能截图

### 登录页面
- 美观的渐变背景登录界面
- 支持记住密码功能

### 控制台
- 数据统计卡片展示
- 销售趋势图表
- 最新通知列表
- 最近订单和热销商品

### 用户管理
- 用户列表分页展示
- 搜索和筛选功能
- 新增/编辑用户
- 启用/禁用用户账号

### 商品管理
- 商品列表展示
- 图片上传功能
- 商品上架/下架
- 分类筛选

### 订单管理
- 订单状态管理
- 关闭订单功能
- 订单发货处理
- 订单详情查看

### 账单管理
- 收支记录管理
- Excel导出功能
- 按时间段筛选

### 物流管理
- 物流信息更新
- 物流状态跟踪
- 支持多家物流公司

## API接口

系统提供RESTful API接口，所有接口返回统一格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

### 主要接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /doLogin | POST | 用户登录 |
| /logout | GET | 用户退出 |
| /user/data | GET | 获取用户列表 |
| /product/data | GET | 获取商品列表 |
| /order/data | GET | 获取订单列表 |
| /bill/export | GET | 导出账单Excel |

## 开发说明

### 密码加密
系统使用MD5加密用户密码：
- admin123 → 0192023a7bbd73250516f069df18b500
- 123456 → e10adc3949ba59abbe56e057f20f883e

### 文件上传
配置文件上传路径：
```yaml
upload:
  path: d:/mall/upload/        # 上传文件存储路径
  access-path: /upload/        # 访问路径
```

### 缓存配置
使用Redis缓存：
- 用户会话缓存
- 商品信息缓存
- 分类数据缓存

## 许可证

MIT License
