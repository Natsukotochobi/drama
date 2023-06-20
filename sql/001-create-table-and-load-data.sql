DROP TABLE IF EXISTS dramas;

CREATE TABLE dramas (
  id int unsigned AUTO_INCREMENT,
  title VARCHAR(100) NOT NULL,
  year VARCHAR(4),
  priority VARCHAR(1) NOT NULL,
  PRIMARY KEY(id)
);

INSERT INTO dramas (title, year, priority) VALUES ("気象庁の人々", "2022", "A");
INSERT INTO dramas (title, year, priority) VALUES ("先輩、その口紅塗らないで", "2021", "A");
INSERT INTO dramas (title, year, priority) VALUES ("偶然見つけたハル", "2019", "C");
