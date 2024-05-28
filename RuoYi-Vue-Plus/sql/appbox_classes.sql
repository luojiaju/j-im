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

 Date: 19/05/2024 16:46:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appbox_classes
-- ----------------------------
DROP TABLE IF EXISTS `appbox_classes`;
CREATE TABLE `appbox_classes`  (
  `id` bigint NOT NULL COMMENT 'id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '上级分类',
  `cover` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '分类封面',
  `classes_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `icon` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '分类icon',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '应用简介',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_dept` bigint NULL DEFAULT NULL COMMENT '创建部门',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应用分类表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
