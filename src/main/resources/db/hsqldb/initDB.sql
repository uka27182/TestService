-- hsqldb
--DROP TABLE task IF EXISTS;

CREATE TABLE IF NOT EXISTS task (
  uuid VARCHAR(40)
  ,action VARCHAR(30)
  ,userName VARCHAR(30)
  ,args VARCHAR(100)
  ,state INTEGER
  ,result VARCHAR(100)
  ,post_dt timestamp default current_timestamp
  ,solved_dt TIMESTAMP
  ,solvingStart_dt TIMESTAMP
);

-- CREATE INDEX idx_uuid ON task (uuid);
