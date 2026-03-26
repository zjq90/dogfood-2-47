-- 商城管理系统数据库表结构
-- 数据库: mall_management

CREATE DATABASE IF NOT EXISTS mall_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mall_management;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用，0-停用',
    role VARCHAR(20) DEFAULT 'user' COMMENT '角色：admin-管理员，user-普通用户',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    category VARCHAR(50) COMMENT '商品分类',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    stock INT DEFAULT 0 COMMENT '库存数量',
    image_url VARCHAR(500) COMMENT '商品图片URL',
    description TEXT COMMENT '商品描述',
    status TINYINT DEFAULT 1 COMMENT '状态：1-上架，0-下架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name),
    INDEX idx_category (category),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    pay_amount DECIMAL(10,2) COMMENT '实付金额',
    status TINYINT DEFAULT 0 COMMENT '订单状态：0-待付款，1-已付款，2-已发货，3-已完成，4-已关闭',
    receiver_name VARCHAR(50) COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) COMMENT '收货人电话',
    receiver_address VARCHAR(200) COMMENT '收货地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单详情表
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单详情ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(100) COMMENT '商品名称',
    product_price DECIMAL(10,2) COMMENT '商品单价',
    quantity INT DEFAULT 1 COMMENT '购买数量',
    total_price DECIMAL(10,2) COMMENT '小计金额',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

-- 账单表
CREATE TABLE IF NOT EXISTS bill (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '账单ID',
    bill_no VARCHAR(50) NOT NULL UNIQUE COMMENT '账单编号',
    order_id BIGINT COMMENT '关联订单ID',
    user_id BIGINT COMMENT '用户ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '账单金额',
    type TINYINT DEFAULT 1 COMMENT '账单类型：1-收入，2-支出',
    payment_method VARCHAR(20) COMMENT '支付方式：alipay-支付宝，wechat-微信，bank-银行卡',
    status TINYINT DEFAULT 1 COMMENT '状态：1-已完成，0-待处理',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_bill_no (bill_no),
    INDEX idx_order_id (order_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表';

-- 物流表
CREATE TABLE IF NOT EXISTS logistics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '物流ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    logistics_no VARCHAR(50) COMMENT '物流单号',
    logistics_company VARCHAR(50) COMMENT '物流公司',
    status TINYINT DEFAULT 0 COMMENT '物流状态：0-待发货，1-已发货，2-运输中，3-派送中，4-已签收',
    current_location VARCHAR(100) COMMENT '当前位置',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_logistics_no (logistics_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流表';

-- 物流轨迹表
CREATE TABLE IF NOT EXISTS logistics_trace (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '轨迹ID',
    logistics_id BIGINT NOT NULL COMMENT '物流ID',
    location VARCHAR(100) COMMENT '位置',
    description VARCHAR(200) COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_logistics_id (logistics_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流轨迹表';

-- 插入默认管理员账号 (密码: admin123，使用MD5加密)
INSERT INTO sys_user (username, password, real_name, phone, email, status, role) 
VALUES ('admin', '0192023a7bbd73250516f069df18b500', '系统管理员', '13800138000', 'admin@mall.com', 1, 'admin');

-- 插入测试用户
INSERT INTO sys_user (username, password, real_name, phone, email, status, role) 
VALUES ('user1', '0192023a7bbd73250516f069df18b500', '测试用户', '13800138001', 'user1@mall.com', 1, 'user');

-- 插入测试商品
INSERT INTO product (name, category, price, stock, description, status) VALUES
('iPhone 15 Pro', '手机', 7999.00, 100, '苹果最新款手机', 1),
('MacBook Pro', '电脑', 14999.00, 50, '苹果笔记本电脑', 1),
('AirPods Pro', '耳机', 1999.00, 200, '苹果无线耳机', 1),
('iPad Pro', '平板', 6999.00, 80, '苹果平板电脑', 1),
('Apple Watch', '手表', 2999.00, 150, '苹果智能手表', 1);
