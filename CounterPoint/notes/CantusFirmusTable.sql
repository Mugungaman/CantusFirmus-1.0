drop table mugunga.cantus_firmi;
CREATE TABLE MUGUNGA.CANTUS_FIRMI (
  ID INT NOT NULL AUTO_INCREMENT,
  MODE_ID SMALLINT NOT NULL DEFAULT 0,
  MELODY VARCHAR(100) NOT NULL,
  TIMESTAMP DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX CF_ID (`id` ASC) VISIBLE,
  UNIQUE INDEX CF_MODE_MELODY (MODE_ID, MELODY),
    CONSTRAINT MODE_MUST_EXIST
  FOREIGN KEY (MODE_ID)
  REFERENCES MUGUNGA.MODES(MODE_ID)
  ON UPDATE CASCADE
  ON DELETE RESTRICT) ;
  
drop table mugunga.FIRST_SPECIES;
CREATE TABLE MUGUNGA.FIRST_SPECIES (
  ID INT NOT NULL AUTO_INCREMENT,
  CANTUS_FIRMUS_ID INT not null,
  MELODY VARCHAR(100) NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE INDEX CF_ID (CANTUS_FIRMUS_ID, MELODY) VISIBLE,
  CONSTRAINT PARENT_MELODY
  FOREIGN KEY (CANTUS_FIRMUS_ID)
  REFERENCES MUGUNGA.CANTUS_FIRMI(ID)
  ON UPDATE CASCADE
  ON DELETE CASCADE) ;
  
drop table mugunga.MODES;
CREATE TABLE MUGUNGA.MODES (
	MODE_ID SMALLINT NOT NULL,
	MODE_NAME VARCHAR(50) NOT NULL,
	PRIMARY KEY(MODE_ID),
	UNIQUE INDEX(MODE_NAME) VISIBLE);
insert into mugunga.modes (mode_id, mode_name)
values(1, 'IONIAN');
insert into mugunga.modes (mode_id, mode_name)
values(2, 'DORIAN');
insert into mugunga.modes (mode_id, mode_name)
values(3, 'PHRYGIAN');
insert into mugunga.modes (mode_id, mode_name)
values(4, 'LYDIAN');
insert into mugunga.modes (mode_id, mode_name)
values(5, 'MIXOLYDIAN');
insert into mugunga.modes (mode_id, mode_name)
values(6, 'AEOLIAN');
insert into mugunga.modes (mode_id, mode_name)
values(7, 'LOCRIAN');