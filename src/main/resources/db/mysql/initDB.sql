-- my sql
-- DROP TABLE task IF EXISTS;

CREATE TABLE IF NOT EXISTS task (
  uuid VARCHAR(40)
  ,action VARCHAR(30)
  ,userName VARCHAR(30)
  ,args VARCHAR(100)
  ,state INTEGER
  ,result VARCHAR(200)
  ,post_dt timestamp
  ,solved_dt TIMESTAMP
  ,solving_dt TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_uuid ON task (uuid);


