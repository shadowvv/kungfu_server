CREATE DATABASE `kungfu` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION = 'N' */;

CREATE TABLE `game_param`
(
    `nextPlayerId` int NOT NULL COMMENT '玩家id当前值'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_bin COMMENT ='游戏参数'；

CREATE TABLE `player_info`
(
    `id`             int         NOT NULL COMMENT '玩家id',
    `userName`       varchar(45) NOT NULL COMMENT '玩家登录名',
    `password`       varchar(45) NOT NULL COMMENT '登录密码',
    `nickName`       varchar(45) NOT NULL COMMENT '玩家昵称',
    `battleCount`    int         DEFAULT '0' COMMENT '战斗次数',
    `winCount`       int         DEFAULT '0' COMMENT '胜利次数',
    `weaponUseCount` varchar(45) DEFAULT NULL COMMENT '各个武器使用次数，使用JSON存储',
    `weaponWinCount` varchar(45) DEFAULT NULL COMMENT '各个武器胜利次数，使用JSON存储',
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    UNIQUE KEY `userName_UNIQUE` (`userName`) /*!80000 INVISIBLE */
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3 COMMENT ='玩家信息'；

ALTER TABLE `kungfu`.`game_param`
    ADD COLUMN `id` INT NOT NULL DEFAULT 1 FIRST,
    ADD PRIMARY KEY (`id`);