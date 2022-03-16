-- MySQL database
-- This is a manual migration

-- Checker for migration
SELECT * FROM information_schema.tables WHERE table_schema=:database AND table_name=:table;

CREATE TABLE `<prefix>parties_new` (
	`id`			CHAR(36) NOT NULL PRIMARY KEY,
	`name`			VARCHAR(255),
	`tag`			VARCHAR(255),
	`leader`		CHAR(36),
	`description`	VARCHAR(255),
	`motd`			VARCHAR(500),
	`color`			VARCHAR(255),
	`kills`			INTEGER DEFAULT 0,
	`password`		VARCHAR(255),
	`home`			VARCHAR(255),
	`protection`	INTEGER DEFAULT 0,
	`experience`	REAL DEFAULT 0,
	`follow`		INTEGER DEFAULT 1
);

SELECT * FROM `<prefix>parties`;

INSERT INTO `<prefix>parties_new` (`id`, `name`, `tag`, `leader`, `description`, `motd`, `color`, `kills`, `password`, `home`, `protection`, `experience`, `follow`)
	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
	ON DUPLICATE KEY
	UPDATE `name`=VALUES(`name`), `tag`=VALUES(`tag`), `leader`=VALUES(`leader`), `description`=VALUES(`description`), `motd`=VALUES(`motd`), `color`=VALUES(`color`), `kills`=VALUES(`kills`), `password`=VALUES(`password`), `home`=VALUES(`home`), `protection`=VALUES(`protection`), `experience`=VALUES(`experience`), `follow`=VALUES(`follow`);

CREATE TABLE `<prefix>players_new` (
	`uuid`			CHAR(36) NOT NULL PRIMARY KEY,
	`party`			CHAR(36),
	`rank`			INTEGER DEFAULT 0,
	`nickname`		VARCHAR(100),
	`chat`			INTEGER DEFAULT 0,
	`spy`			INTEGER DEFAULT 0,
	`mute`			INTEGER DEFAULT 0
);

SELECT * FROM `<prefix>players`;

INSERT INTO `<prefix>players_new` (`uuid`, `party`, `rank`, `nickname`, `chat`, `spy`, `mute`)
VALUES (?, ?, ?, ?, ?, ?, ?)
	ON DUPLICATE KEY UPDATE `party`=VALUES(`party`), `rank`=VALUES(`rank`), `nickname`=VALUES(`nickname`), `chat`=VALUES(`chat`), `spy`=VALUES(`spy`), `mute`=VALUES(`mute`);

DROP TABLE `<prefix>parties`;
DROP TABLE `<prefix>players`;
DROP TABLE `<prefix>versions`;

ALTER TABLE `<prefix>parties_new` RENAME TO `<prefix>parties`;
ALTER TABLE `<prefix>players_new` RENAME TO `<prefix>players`;