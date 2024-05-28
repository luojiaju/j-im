/*
 Navicat Premium Data Transfer

 Source Server         : master@localhost
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : localhost:13306
 Source Schema         : ry-vue

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 19/05/2024 16:52:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appbox_fixed_app_user_config
-- ----------------------------
DROP TABLE IF EXISTS `appbox_fixed_app_user_config`;
CREATE TABLE `appbox_fixed_app_user_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `app_id` bigint NULL DEFAULT NULL COMMENT '应用apo',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `identity` tinyint NULL DEFAULT 1 COMMENT '标识 例如:1 管理员 2机器人',
  `icon_url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像小图标',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_dept` bigint NULL DEFAULT NULL COMMENT '创建部门',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1756897331169980419 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应用固定用户配置表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
