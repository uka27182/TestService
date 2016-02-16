-- postgres
-- DROP TABLE task;
-- DROP INDEX idx_task_uuid;

CREATE TABLE IF NOT EXISTS task (
  uuid VARCHAR(40)
  ,action VARCHAR(30)
  ,userName VARCHAR(30)
  ,args VARCHAR(100)
  ,state INTEGER
  ,result VARCHAR(200)
  ,post_dt timestamp default now()
  ,solved_dt TIMESTAMP
  ,solvingstart_dt TIMESTAMP
);

-- DO $$
-- BEGIN
--   IF NOT EXISTS (
--       SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace WHERE c.relname = 'task'
--   ) THEN
--     CREATE INDEX idx_task_uuid ON task (uuid);
--   END IF;
-- END$$;

