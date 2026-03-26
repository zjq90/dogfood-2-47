-- 商城管理系统数据库脚本
-- 数据库: mall_db
-- 字符集: utf8mb4

-- 创建数据库
CREATE DATABASE IF NOT EXISTS mall_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mall_db;

-- 用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(255) COMMENT '头像URL',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    role TINYINT DEFAULT 0 COMMENT '角色: 0-普通用户, 1-管理员',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_username (username),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品分类表
DROP TABLE IF EXISTS category;
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID,0为顶级分类',
    level INT DEFAULT 1 COMMENT '层级',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_parent_id (parent_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
DROP TABLE IF EXISTS product;
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    description TEXT COMMENT '商品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '售价',
    original_price DECIMAL(10,2) COMMENT '原价',
    stock INT DEFAULT 0 COMMENT '库存数量',
    sales INT DEFAULT 0 COMMENT '销量',
    main_image VARCHAR(255) COMMENT '主图URL',
    images TEXT COMMENT '图片列表,JSON格式',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-下架, 1-上架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_category_id (category_id),
    KEY idx_status (status),
    KEY idx_price (price)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 订单表
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    pay_amount DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-待付款, 1-已付款, 2-已发货, 3-已完成, 4-已关闭',
    pay_type TINYINT COMMENT '支付方式: 1-支付宝, 2-微信',
    pay_time DATETIME COMMENT '支付时间',
    delivery_time DATETIME COMMENT '发货时间',
    receive_time DATETIME COMMENT '收货时间',
    close_time DATETIME COMMENT '关闭时间',
    remark VARCHAR(500) COMMENT '订单备注',
    receiver_name VARCHAR(50) COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) COMMENT '收货人电话',
    receiver_address VARCHAR(500) COMMENT '收货地址',
    logistics_no VARCHAR(50) COMMENT '物流单号',
    logistics_company VARCHAR(50) COMMENT '物流公司',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单商品表
DROP TABLE IF EXISTS order_item;
CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(255) COMMENT '商品图片',
    price DECIMAL(10,2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL COMMENT '数量',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_order_id (order_id),
    KEY idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品表';

-- 账单表
DROP TABLE IF EXISTS bill;
CREATE TABLE bill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '账单ID',
    bill_no VARCHAR(32) NOT NULL COMMENT '账单编号',
    type TINYINT NOT NULL COMMENT '类型: 1-收入, 2-支出',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    description VARCHAR(500) COMMENT '描述',
    order_id BIGINT COMMENT '关联订单ID',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-无效, 1-有效',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_bill_no (bill_no),
    KEY idx_type (type),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表';

-- 物流记录表
DROP TABLE IF EXISTS logistics;
CREATE TABLE logistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    logistics_no VARCHAR(50) NOT NULL COMMENT '物流单号',
    company VARCHAR(50) COMMENT '物流公司',
    status VARCHAR(20) COMMENT '物流状态',
    current_location VARCHAR(200) COMMENT '当前位置',
    details TEXT COMMENT '物流详情,JSON格式',
    update_time DATETIME COMMENT '物流更新时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_order_id (order_id),
    KEY idx_logistics_no (logistics_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流记录表';

-- 插入初始数据

-- 插入管理员用户 (密码: admin123 - MD5加密)
INSERT INTO sys_user (username, password, real_name, phone, email, status, role) VALUES
('admin', '0192023a7bbd73250516f069df18b500', '管理员', '13800138000', 'admin@mall.com', 1, 1);

-- 插入测试用户 (密码: 123456 - MD5加密)
INSERT INTO sys_user (username, password, real_name, phone, email, status, role) VALUES
('user01', 'e10adc3949ba59abbe56e057f20f883e', '张三', '13800138001', 'user01@mall.com', 1, 0),
('user02', 'e10adc3949ba59abbe56e057f20f883e', '李四', '13800138002', 'user02@mall.com', 1, 0),
('user03', 'e10adc3949ba59abbe56e057f20f883e', '王五', '13800138003', 'user03@mall.com', 0, 0);

-- 插入商品分类
INSERT INTO category (name, parent_id, level, sort, status) VALUES
('电子产品', 0, 1, 1, 1),
('手机', 1, 2, 1, 1),
('电脑', 1, 2, 2, 1),
('服装鞋帽', 0, 1, 2, 1),
('男装', 4, 2, 1, 1),
('女装', 4, 2, 2, 1),
('食品饮料', 0, 1, 3, 1),
('零食', 7, 2, 1, 1),
('饮料', 7, 2, 2, 1);

-- 插入商品
INSERT INTO product (name, category_id, description, price, original_price, stock, sales, status) VALUES
('iPhone 15 Pro', 2, '苹果最新旗舰手机', 7999.00, 8999.00, 100, 50, 1),
('iPhone 15', 2, '苹果标准版手机', 5999.00, 6999.00, 150, 80, 1),
('MacBook Pro 14', 3, '苹果专业笔记本', 14999.00, 16999.00, 50, 20, 1),
('华为Mate 60 Pro', 2, '华为旗舰手机', 6999.00, 7999.00, 80, 60, 1),
('小米14', 2, '小米旗舰手机', 3999.00, 4599.00, 200, 120, 1),
('休闲T恤男', 5, '舒适透气男士T恤', 99.00, 199.00, 500, 300, 1),
('连衣裙女', 6, '时尚女装连衣裙', 299.00, 599.00, 300, 150, 1),
('巧克力礼盒', 8, '进口巧克力礼盒', 199.00, 299.00, 200, 100, 1),
('可乐330ml*24', 9, '可口可乐整箱', 59.90, 79.90, 1000, 500, 1);

-- 插入订单
INSERT INTO `order` (order_no, user_id, total_amount, pay_amount, status, pay_type, pay_time, receiver_name, receiver_phone, receiver_address, remark) VALUES
('202403260001', 2, 7999.00, 7999.00, 3, 1, '2024-03-20 10:00:00', '张三', '13800138001', '北京市朝阳区xxx街道xxx号', '请尽快发货'),
('202403260002', 2, 5999.00, 5999.00, 2, 2, '2024-03-21 14:30:00', '张三', '13800138001', '北京市朝阳区xxx街道xxx号', ''),
('202403260003', 3, 14999.00, 14999.00, 1, 1, '2024-03-22 09:15:00', '李四', '13800138002', '上海市浦东新区xxx路xxx号', '发票抬头:李四'),
('202403260004', 4, 3999.00, 3999.00, 0, NULL, NULL, '王五', '13800138003', '广州市天河区xxx街xxx号', ''),
('202403260005', 2, 398.00, 398.00, 3, 1, '2024-03-18 16:45:00', '张三', '13800138001', '北京市朝阳区xxx街道xxx号', '');

-- 插入订单商品
INSERT INTO order_item (order_id, product_id, product_name, price, quantity, total_amount) VALUES
(1, 1, 'iPhone 15 Pro', 7999.00, 1, 7999.00),
(2, 2, 'iPhone 15', 5999.00, 1, 5999.00),
(3, 3, 'MacBook Pro 14', 14999.00, 1, 14999.00),
(4, 5, '小米14', 3999.00, 1, 3999.00),
(5, 6, '休闲T恤男', 99.00, 2, 198.00),
(5, 8, '巧克力礼盒', 199.00, 1, 199.00);

-- 插入账单
INSERT INTO bill (bill_no, type, amount, title, description, order_id, status) VALUES
('B202403200001', 1, 7999.00, '订单收入', 'iPhone 15 Pro销售', 1, 1),
('B202403210001', 1, 5999.00, '订单收入', 'iPhone 15销售', 2, 1),
('B202403220001', 1, 14999.00, '订单收入', 'MacBook Pro 14销售', 3, 1),
('B202403180001', 1, 398.00, '订单收入', '服装销售', 5, 1);

-- 插入物流记录
INSERT INTO logistics (order_id, logistics_no, company, status, current_location, details, update_time) VALUES
(1, 'SF1234567890', '顺丰速运', '已签收', '北京市朝阳区', '[{"time":"2024-03-20 10:00","content":"订单已创建"},{"time":"2024-03-20 18:00","content":"商品已发货"},{"time":"2024-03-22 14:00","content":"已签收"}]', '2024-03-22 14:00:00'),
(2, 'JD9876543210', '京东物流', '运输中', '上海市', '[{"time":"2024-03-21 14:30","content":"订单已创建"},{"time":"2024-03-21 20:00","content":"商品已发货"},{"time":"2024-03-23 08:00","content":"到达上海分拣中心"}]', '2024-03-23 08:00:00');
