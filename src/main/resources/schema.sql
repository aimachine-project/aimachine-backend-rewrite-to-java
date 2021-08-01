CREATE TABLE if not exists user (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(255),
  password VARCHAR(255),
  role VARCHAR(255),

  primary key (id)
);