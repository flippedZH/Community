CREATE TABLE question
(
	id int auto_increment,
	title varchar(50),
	description TEXT,
	gmt_create BIGINT,
	gmt_modified bigint,
	creator INT,
	comment_count INT default 0,
	view_count INT default 0,
	like_count INT default 0,
	tag varchar(256),
	constraint QUESTION_PK
		primary key (id)
);

