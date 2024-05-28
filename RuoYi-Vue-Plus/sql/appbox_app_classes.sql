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

 Date: 19/05/2024 16:45:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appbox_app_classes
-- ----------------------------
DROP TABLE IF EXISTS `appbox_app_classes`;
CREATE TABLE `appbox_app_classes`  (
  `classes_id` bigint NULL DEFAULT NULL,
  `app_id` bigint NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应用和分类关联表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
