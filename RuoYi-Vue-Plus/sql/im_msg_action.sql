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

 Date: 19/05/2024 16:50:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for im_msg_action
-- ----------------------------
DROP TABLE IF EXISTS `im_msg_action`;
CREATE TABLE `im_msg_action`  (
  `id` bigint NOT NULL COMMENT 'id',
  `msg_id` bigint NULL DEFAULT NULL COMMENT '聊天id',
  `endorse_count` int NULL DEFAULT 0 COMMENT '赞同量',
  `full_count` int NULL DEFAULT 0 COMMENT '满分量',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞量',
  `create_dept` bigint NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天动作表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
