SELECT current_database();

create table test_table(
                           id integer,
                           memo varchar(50)
);

insert into test_table values (1,'첫번째');

insert into test_table values (2,'두번째');

select * from test_table;

update test_table set memo = '첫번째(수정후)' where id = 1;

delete from test_table;

truncate test_table;

drop table test_table;

alter table test_table add column create_at timestamp;

alter table test_table drop column create_at;

alter table test_table rename to test_table_renamed;

select * from test_table_renamed;

drop table test_table_renamed;

SELECT *
FROM information_schema.tables
WHERE table_schema = 'public';

CREATE TABLE pokemon_sample (
    id SERIAL PRIMARY KEY,
    pokemon_no INTEGER NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    level INTEGER NOT NULL CHECK (level >= 1 AND level <= 100),
    description TEXT,
    experience BIGINT NOT NULL DEFAULT 0 CHECK (experience >= 0),
    is_legendary BOOLEAN NOT NULL DEFAULT FALSE,
    discovered_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE pokemon_sample ALTER COLUMN created_at TYPE TIMESTAMP USING created_at::TIMESTAMP;

ALTER TABLE pokemon    ALTER COLUMN level SET NOT NULL;
