-- 创建数据库
CREATE DATABASE IF NOT EXISTS mall_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE mall_db;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `avatar` VARCHAR(255) COMMENT '头像',
    `role` TINYINT DEFAULT 2 COMMENT '角色：1-管理员，2-普通用户',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `category_id` BIGINT NOT NULL COMMENT '分类ID',
    `price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    `original_price` DECIMAL(10,2) COMMENT '原价',
    `stock` INT DEFAULT 0 COMMENT '库存',
    `image` VARCHAR(255) COMMENT '商品图片',
    `description` TEXT COMMENT '商品描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    `pay_amount` DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    `pay_type` TINYINT COMMENT '支付方式：1-微信，2-支付宝',
    `pay_time` DATETIME COMMENT '支付时间',
    `order_status` TINYINT DEFAULT 1 COMMENT '订单状态：1-待付款，2-待发货，3-已发货，4-已完成，5-已关闭',
    `consignee` VARCHAR(50) NOT NULL COMMENT '收货人',
    `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `address` VARCHAR(500) NOT NULL COMMENT '收货地址',
    `remark` VARCHAR(500) COMMENT '订单备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单明细表
CREATE TABLE IF NOT EXISTS `order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `product_image` VARCHAR(255) COMMENT '商品图片',
    `price` DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    `quantity` INT NOT NULL COMMENT '购买数量',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '商品总金额',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 物流表
CREATE TABLE IF NOT EXISTS `logistics` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
    `shipping_company` VARCHAR(100) COMMENT '物流公司',
    `shipping_no` VARCHAR(100) COMMENT '物流单号',
    `shipping_status` TINYINT DEFAULT 1 COMMENT '物流状态：1-待发货，2-已发货，3-运输中，4-已签收',
    `tracking_info` TEXT COMMENT '物流跟踪信息',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流表';

-- 账单表
CREATE TABLE IF NOT EXISTS `bill` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `bill_no` VARCHAR(32) NOT NULL COMMENT '账单编号',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '账单金额',
    `bill_type` TINYINT NOT NULL COMMENT '账单类型：1-收入，2-支出',
    `bill_status` TINYINT DEFAULT 1 COMMENT '账单状态：1-已确认，2-待确认',
    `remark` VARCHAR(500) COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_bill_no` (`bill_no`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表';

-- 插入默认管理员用户
INSERT INTO `user` (`username`, `password`, `real_name`, `role`, `status`) 
VALUES ('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 1, 1);

-- 插入测试分类数据
INSERT INTO `category` (`name`, `parent_id`, `sort`) VALUES 
('手机数码', 0, 1),
('电脑办公', 0, 2),
('家用电器', 0, 3),
('服装鞋帽', 0, 4),
('食品饮料', 0, 5);

-- 插入测试商品数据
INSERT INTO `product` (`name`, `category_id`, `price`, `original_price`, `stock`, `description`, `status`) VALUES 
('iPhone 15 Pro Max', 1, 9999.00, 10999.00, 100, '苹果最新款手机，A17 Pro芯片，钛金属设计', 1),
('华为 Mate 60 Pro', 1, 6999.00, 7999.00, 150, '华为旗舰手机，麒麟芯片，卫星通信', 1),
('MacBook Pro 14英寸', 2, 14999.00, 16999.00, 50, '苹果笔记本电脑，M3 Pro芯片', 1),
('联想 ThinkPad X1 Carbon', 2, 9999.00, 11999.00, 80, '联想商务笔记本，轻薄便携', 1),
('戴森 V15 吸尘器', 3, 4999.00, 5499.00, 60, '戴森无线吸尘器，激光探测', 1);