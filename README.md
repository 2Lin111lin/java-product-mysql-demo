# java-product-mysql-demo
Java控制台商品管理系统，JDBC + MySQL实现CRUD

## 项目功能
- 添加商品
- 根据ID查询商品
- 修改商品价格
- 删除商品
- 查询全部商品

## 环境要求
- JDK 21
- MySQL 8.0+
- MySQL JDBC驱动 mysql-connector

## 数据库建表SQL
```sql
CREATE DATABASE product_db;
USE product_db;
CREATE TABLE product(
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    price DOUBLE
);
