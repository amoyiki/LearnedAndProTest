/*
MySQL Data Transfer
Source Host: localhost
Source Database: test
Target Host: localhost
Target Database: test
Date: 2016/1/21 15:17:37
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for t_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `sn` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_role_res
-- ----------------------------
DROP TABLE IF EXISTS `t_role_res`;
CREATE TABLE `t_role_res` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `res_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `t_resource` VALUES ('1', '系统管理', 'admin:*', '/user/**');
INSERT INTO `t_resource` VALUES ('2', '员工管理', 'user:*', '/user/emp/**');
INSERT INTO `t_resource` VALUES ('3', '员工添加', 'user:add', '/use/emp/add');
INSERT INTO `t_resource` VALUES ('4', '员工删除', 'user:delete', '/user/emp/delete');
INSERT INTO `t_resource` VALUES ('5', '角色管理', 'role:*', '/user/role/**');
INSERT INTO `t_resource` VALUES ('6', '角色添加', 'role:add', '/user/role/add');
INSERT INTO `t_resource` VALUES ('7', '角色修改', 'role:update', '/user/role/update');
INSERT INTO `t_resource` VALUES ('8', '资源管理', 'res:*', '/user/res/**');
INSERT INTO `t_resource` VALUES ('9', '员工资源', null, '/user/empRes/list');
INSERT INTO `t_role` VALUES ('1', '管理员', 'ADMIN');
INSERT INTO `t_role` VALUES ('2', '开发组', 'EMP');
INSERT INTO `t_role` VALUES ('3', '产品组', 'TEST');
INSERT INTO `t_role` VALUES ('4', '销售组', 'SALE');
INSERT INTO `t_role` VALUES ('5', '普通用户', 'USER');
INSERT INTO `t_role_res` VALUES ('1', '1', '1');
INSERT INTO `t_role_res` VALUES ('2', '2', '2');
INSERT INTO `t_role_res` VALUES ('3', '3', '2');
INSERT INTO `t_role_res` VALUES ('4', '4', '2');
INSERT INTO `t_role_res` VALUES ('5', '5', '3');
INSERT INTO `t_role_res` VALUES ('16', '4', '5');
INSERT INTO `t_user` VALUES ('1', '张三', 'e7a6d0f97db7ea1c4a0c1c137cbf771c', '1', 'zhangsan');
INSERT INTO `t_user` VALUES ('2', '管理员', 'bbad8d72c1fac1d081727158807a8798', '1', 'admin');
INSERT INTO `t_user` VALUES ('3', '孔浩', '524b8786be47bd962de7bfa8c9c3a26a', '1', 'kh');
INSERT INTO `t_user` VALUES ('4', '1', '6512bd43d9caa6e02c990b0a82652dca', '1', '1');
INSERT INTO `t_user_role` VALUES ('2', '1', '2');
INSERT INTO `t_user_role` VALUES ('7', '5', '1');
INSERT INTO `t_user_role` VALUES ('9', '3', '3');
INSERT INTO `t_user_role` VALUES ('10', '2', '4');
