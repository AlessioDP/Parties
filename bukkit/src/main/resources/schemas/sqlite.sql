-- Start and end placeholders are used to obtain right queries

/*START_PARTIES*/
CREATE TABLE '{table_parties}' (
	'name'			VARCHAR({varcharsize}) NOT NULL,
	'leader'		VARCHAR({varcharsize}) NOT NULL,
	'description'	VARCHAR({varcharsize}) DEFAULT '',
	'motd'			VARCHAR({varcharsize}) DEFAULT '',
	'prefix'		VARCHAR({varcharsize}) DEFAULT '',
	'suffix'		VARCHAR({varcharsize}) DEFAULT '',
	'color'			VARCHAR({varcharsize}) DEFAULT '',
	'kills'			INT DEFAULT 0,
	'password'		VARCHAR({varcharsize}) DEFAULT '',
	'home'			VARCHAR({varcharsize}) DEFAULT '',
	PRIMARY KEY ('name'));
PRAGMA user_version = {version};
/*END_PARTIES*/
	
/*START_PLAYERS*/
CREATE TABLE '{table_players}' (
	'uuid'		VARCHAR({varcharsize}) NOT NULL,
	'party'		VARCHAR({varcharsize}) NOT NULL,
	'rank'		INT DEFAULT 0,
	'name'		VARCHAR({varcharsize}) DEFAULT '',
	'timestamp'	INT,
	'spy'		INT DEFAULT 0 NOT NULL,
	`notify`	INT DEFAULT 0 NOT NULL,
	PRIMARY KEY ('uuid'));
PRAGMA user_version = {version};
/*END_PLAYERS*/

/*START_LOG*/
CREATE TABLE '{table_log}' (
	'id'		INT NOT NULL,
	'date'		DATETIME,
	'level'		TINYINT,
	'position'	VARCHAR({varcharsize}),
	'message'	VARCHAR({varcharsize}),
	PRIMARY KEY ('id'));
PRAGMA user_version = {version};
/*END_LOG*/