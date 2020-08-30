-- MySQL database
CREATE TABLE IF NOT EXISTS ${prefix}parties (
	`name`			VARCHAR(40) NOT NULL PRIMARY KEY,
	`leader`		VARCHAR(40) NOT NULL,
	`description`	VARCHAR(100),
	`motd`			VARCHAR(500),
	`color`			VARCHAR(30),
	`kills`			INTEGER DEFAULT 0,
	`password`		VARCHAR(64),
	`home`			VARCHAR(50),
	`protection`	INTEGER DEFAULT 0,
	`experience`	REAL DEFAULT 0,
	`follow`		INTEGER DEFAULT 1
);

CREATE TABLE IF NOT EXISTS ${prefix}players (
	`uuid`			VARCHAR(40) NOT NULL PRIMARY KEY,
	`party`			VARCHAR(50) NOT NULL,
	`rank`			INTEGER DEFAULT 0,
    `chat`			INTEGER DEFAULT 0,
	`spy`			INTEGER DEFAULT 0,
	`mute`			INTEGER DEFAULT 0
);