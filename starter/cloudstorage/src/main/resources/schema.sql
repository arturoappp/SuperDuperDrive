CREATE TABLE IF NOT EXISTS USERS (
  userid INT PRIMARY KEY auto_increment,
  username VARCHAR(20) UNIQUE,
  salt VARCHAR,
  password VARCHAR,
  firstname VARCHAR(20),
  lastname VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS NOTES (
    noteid INT PRIMARY KEY auto_increment,
    notetitle VARCHAR(20),
    notedescription VARCHAR (1000),
    userid INT,
    foreign key (userid) references USERS(userid)
);

CREATE TABLE IF NOT EXISTS FILES (
    fileId INT PRIMARY KEY auto_increment,
    filename VARCHAR,
    contenttype VARCHAR,
    filesize VARCHAR,
    userid INT,
    filedata BLOB,
    foreign key (userid) references USERS(userid)
);

CREATE TABLE IF NOT EXISTS CREDENTIALS (
    credentialid INT PRIMARY KEY auto_increment,
    url VARCHAR(100),
    username VARCHAR (30),
    key VARCHAR,
    password VARCHAR,
    userid INT,
    foreign key (userid) references USERS(userid)
);

INSERT INTO USERS (username, salt, password, firstname, lastname)
VALUES('a', 'ricaSalt', 'R5HO/sr9VVyKj3oXUtwi5b8WRSsavKs5CRieVAiUzPND6Ne+0kE9FtbP+SKMAnIhJQdpWMoiA8R5j7/SDkDdE8XFckbtdoPyZQks83g+P45AH7PWBan4RFgWJvqnSQWQU8axstQGLwoWo4WOACULZ8Ns2X60gDicesqAh8iy6h8SL73bKgmX+qsPd8+vsX5OADTmhKSm0wx5WnKU6y+CXN+PeJ5t8ypj5bXOkdlKBSZzjwn2BI8atFvT7+glPX4om+8uAr/evKO9NGxTAjyWwf5yTAVN2PFgU1aqV7q9jcu2KOalBvCMT7Hu9QS6vvvpY1+t5zwDbvbsctEItTYWVQ==', 'arturo', 'ramos');
