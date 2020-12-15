-- MySQL database
CREATE TABLE IF NOT EXISTS <prefix>parties (
	"id"			CHAR(36) NOT NULL PRIMARY KEY,
	"name"			VARCHAR(255) NOT NULL,
	"tag"			VARCHAR(255),
	"leader"		CHAR(36),
	"description"	VARCHAR(255),
	"motd"			VARCHAR(500),
	"color"			VARCHAR(255),
	"kills"			INTEGER DEFAULT 0,
	"password"		VARCHAR(255),
	"home"			VARCHAR(255),
	"protection"	BOOL DEFAULT FALSE,
	"experience"	REAL DEFAULT 0,
	"follow"		BOOL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS <prefix>players (
	"uuid"			CHAR(36) NOT NULL PRIMARY KEY,
	"party"			CHAR(36),
	"rank"			INTEGER DEFAULT 0,
	"chat"			BOOL DEFAULT FALSE,
	"spy"			BOOL DEFAULT FALSE,
	"mute"			BOOL DEFAULT FALSE
);