-- Start and end placeholders are used to obtain right queries

/*START_PARTIES*/
CREATE TABLE '{table_parties}' (
	'name'			VARCHAR({varcharsize}) NOT NULL,
	'leader'		VARCHAR({varcharsize}) NOT NULL,
	'description'	VARCHAR({varcharsize}) DEFAULT '',
	'motd'			VARCHAR({varcharsize}) DEFAULT '',
	'color'			VARCHAR({varcharsize}) DEFAULT '',
	'kills'			INT DEFAULT 0,
	'password'		VARCHAR({varcharsize}) DEFAULT '',
	'home'			VARCHAR({varcharsize}) DEFAULT '',
	'protection'	INT DEFAULT 0 NOT NULL,
	'experience'	REAL DEFAULT 0,
	PRIMARY KEY ('name'));
/*END_PARTIES*/
	
/*START_PLAYERS*/
CREATE TABLE '{table_players}' (
	'uuid'		VARCHAR({varcharsize}) NOT NULL,
	'party'		VARCHAR({varcharsize}) NOT NULL,
	'rank'		INT DEFAULT 0,
	'name'		VARCHAR({varcharsize}) DEFAULT '',
	'timestamp'	INT,
	'spy'		INT DEFAULT 0 NOT NULL,
	`mute`		INT DEFAULT 0 NOT NULL,
	PRIMARY KEY ('uuid'));
/*END_PLAYERS*/

/*START_LOG*/
CREATE TABLE '{table_log}' (
	'id'		INT NOT NULL,
	'date'		DATETIME,
	'level'		TINYINT,
	'position'	VARCHAR({varcharsize}),
	'message'	VARCHAR({varcharsize}),
	PRIMARY KEY ('id'));
/*END_LOG*/

/*START_VERSIONS*/
CREATE TABLE '{table_versions}' (
	'name'		VARCHAR({varcharsize}) NOT NULL,
	'version'	INT NOT NULL,
	PRIMARY KEY ('name'));
/*END_VERSIONS*/