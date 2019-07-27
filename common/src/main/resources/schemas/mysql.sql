-- Start and end placeholders are used to obtain right queries

/*START_PARTIES*/
CREATE TABLE `{table_parties}` (
	`name`			VARCHAR({varcharsize}) NOT NULL,
	`leader`		VARCHAR({varcharsize}) NOT NULL,
	`description`	VARCHAR({varcharsize}) DEFAULT '',
	`motd`			VARCHAR({varcharsize}) DEFAULT '',
	`color`			VARCHAR({varcharsize}) DEFAULT '',
	`kills`			INT DEFAULT 0,
	`password`		VARCHAR({varcharsize}) DEFAULT '',
	`home`			VARCHAR({varcharsize}) DEFAULT '',
	`protection`	TINYINT(1) DEFAULT 0 NOT NULL,
	`experience`	DOUBLE DEFAULT 0,
	`follow`		TINYINT(1) DEFAULT 1 NOT NULL,
	PRIMARY KEY (`name`))
 DEFAULT CHARSET='{charset}';
/*END_PARTIES*/

/*START_PLAYERS*/
CREATE TABLE `{table_players}` (
	`uuid`			VARCHAR({varcharsize}) NOT NULL,
	`party`			VARCHAR({varcharsize}) NOT NULL,
	`rank`			INT DEFAULT 0,
	`spy`			TINYINT(1) DEFAULT 0 NOT NULL,
	`mute`			TINYINT(1) DEFAULT 0 NOT NULL,
	PRIMARY KEY (`uuid`))
 DEFAULT CHARSET='{charset}';
/*END_PLAYERS*/

/*START_LOG*/
CREATE TABLE `{table_log}` (
	`id`		INT NOT NULL AUTO_INCREMENT,
	`date`		DATETIME,
	`level`		TINYINT,
	`position`	VARCHAR({varcharsize}),
	`message`	VARCHAR({varcharsize}),
	PRIMARY KEY (`id`))
 DEFAULT CHARSET='{charset}';
/*END_LOG*/

/*START_VERSIONS*/
CREATE TABLE `{table_versions}` (
	`name`		VARCHAR({varcharsize}) NOT NULL,
	`version`	INT NOT NULL,
	PRIMARY KEY (`name`))
 DEFAULT CHARSET='{charset}';
/*END_VERSIONS*/