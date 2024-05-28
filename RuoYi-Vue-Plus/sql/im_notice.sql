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

 Date: 19/05/2024 16:53:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for im_notice
-- ----------------------------
DROP TABLE IF EXISTS `im_notice`;
CREATE TABLE `im_notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `notice_type` tinyint NULL DEFAULT 1 COMMENT '通知类型:1精选推送 2用户消息 3有人@你',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知标题',
  `read_status` tinyint(1) NULL DEFAULT 0 COMMENT '是否已读:1已读 0未读',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '通知内容',
  `from_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源url,可前往查看',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_dept` bigint NULL DEFAULT NULL COMMENT '创建部门',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1791132392409792514 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
