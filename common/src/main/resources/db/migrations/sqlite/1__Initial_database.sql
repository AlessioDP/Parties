-- SQLite database
CREATE TABLE IF NOT EXISTS `<prefix>parties` (
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

CREATE TABLE IF NOT EXISTS `<prefix>players` (
	'uuid'			VARCHAR NOT NULL PRIMARY KEY,
	'party'			VARCHAR,
	'rank'			INTEGER DEFAULT 0,
	'nickname'		VARCHAR,
	'chat'			INTEGER DEFAULT 0,
	'spy'			INTEGER DEFAULT 0,
	'mute'			INTEGER DEFAULT 0
);