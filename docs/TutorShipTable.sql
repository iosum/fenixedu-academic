DROP TABLE IF EXISTS TUTOR_SHIP;
CREATE TABLE TUTOR_SHIP(
	ID_INTERNAL int(11) NOT NULL auto_increment,
	KEY_TEACHER int(11) NOT NULL,
	KEY_STUDENT int(11) NOT NULL,
	ACK_OPT_LOCK int(11) not null default '1',
	PRiMARY KEY (ID_INTERNAL),
	UNIQUE U1 (KEY_TEACHER, KEY_STUDENT)
) TYPE=InnoDB;