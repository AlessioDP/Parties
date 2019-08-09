-- Start and end placeholders are used to obtain right queries

/*START_PARTIES*/
CREATE TABLE '{table_parties}' (
	'name'			VARCHAR({varcharsize}) NOT NULL PRIMARY KEY,
	'leader'		VARCHAR({varcharsize}) NOT NULL,
	'description'	VARCHAR({varcharsize}) DEFAULT '',
	'motd'			VARCHAR({varcharsize}) DEFAULT '',
	'color'			VARCHAR({varcharsize}) DEFAULT '',
	'kills'			INTEGER DEFAULT 0,
	'password'		VARCHAR({varcharsize}) DEFAULT '',
	'home'			VARCHAR({varcharsize}) DEFAULT '',
	'protection'	INTEGER DEFAULT 0 NOT NULL,
	'experience'	REAL DEFAULT 0,
	'follow'		INTEGER DEFAULT 1 NOT NULL);
/*END_PARTIES*/
	
/*START_PLAYERS*/
CREATE TABLE '{table_players}' (
	'uuid'			VARCHAR({varcharsize}) NOT NULL PRIMARY KEY,
	'party'			VARCHAR({varcharsize}) NOT NULL,
	'rank'			INTEGER DEFAULT 0,
	'spy'			INTEGER DEFAULT 0 NOT NULL,
	`mute`			INTEGER DEFAULT 0 NOT NULL);
/*END_PLAYERS*/

/*START_VERSIONS*/
CREATE TABLE '{table_versions}' (
	'name'		VARCHAR({varcharsize}) NOT NULL PRIMARY KEY,
	'version'	INTEGER NOT NULL);
/*END_VERSIONS*/