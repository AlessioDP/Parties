-- H2 database
CREATE TABLE IF NOT EXISTS `<prefix>parties` (
	`id`			VARCHAR(40) NOT NULL PRIMARY KEY,
	`name`			VARCHAR(40) NOT NULL,
	`tag`			VARCHAR(20),
	`leader`		VARCHAR(40),
	`description`	VARCHAR(100),
	`motd`			VARCHAR(500),
	`color`			VARCHAR(30),
	`kills`			INT DEFAULT 0,
	`password`		VARCHAR(64),
	`home`			VARCHAR(50),
	`protection`	INT DEFAULT 0,
	`experience`	REAL DEFAULT 0,
	`follow`		INT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS `<prefix>players` (
	`uuid`			VARCHAR(40) NOT NULL PRIMARY KEY,
	`party`			VARCHAR(50),
	`rank`			INT DEFAULT 0,
	`chat`			INT DEFAULT 0,
	`spy`			INT DEFAULT 0,
	`mute`			INT DEFAULT 0
);