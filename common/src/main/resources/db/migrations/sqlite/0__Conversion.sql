-- SQLite database
-- This is a manual migration

-- Checker for migration
SELECT * FROM sqlite_master WHERE type='table' AND name=:table;

CREATE TABLE `<prefix>parties_new` (
	'id'			VARCHAR NOT NULL PRIMARY KEY,
	'name'			VARCHAR,
	'tag'			VARCHAR,
	'leader'		VARCHAR,
	'description'	VARCHAR,
	'motd'			VARCHAR,
	'color'			VARCHAR,
	'kills'			INTEGER DEFAULT 0,
	'password'		VARCHAR,
	'home'			VARCHAR,
	'protection'	INTEGER DEFAULT 0,
	'experience'	REAL DEFAULT 0,
	'follow'		INTEGER DEFAULT 1
);

SELECT * FROM `<prefix>parties`;

INSERT OR REPLACE INTO `<prefix>parties_new` (`id`, `name`, `tag`, `leader`, `description`, `motd`, `color`, `kills`, `password`, `home`, `protection`, `experience`, `follow`)
	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

CREATE TABLE `<prefix>players_new` (
	'uuid'			VARCHAR NOT NULL PRIMARY KEY,
	'party'			VARCHAR,
	'rank'			INTEGER DEFAULT 0,
	`nickname`		VARCHAR,
	'chat'			INTEGER DEFAULT 0,
	'spy'			INTEGER DEFAULT 0,
	'mute'			INTEGER DEFAULT 0
);

SELECT * FROM `<prefix>players`;

INSERT OR REPLACE INTO `<prefix>players_new` (`uuid`, `party`, `rank`, `nickname`, `chat`, `spy`, `mute`)
	VALUES (?, ?, ?, ?, ?, ?, ?);

DROP TABLE `<prefix>parties`;
DROP TABLE `<prefix>players`;
DROP TABLE `<prefix>versions`;

ALTER TABLE `<prefix>parties_new` RENAME TO `<prefix>parties`;
ALTER TABLE `<prefix>players_new` RENAME TO `<prefix>players`;