create table trainers(
    id serial primary key ,
    name varchar(30) not null ,
    hometown varchar(50),
    badge_count integer not null default 0 check ( badge_count between 0 and 8  )
);
INSERT INTO trainers (name, hometown, badge_count) VALUES
('지우',   '태초마을',   8),
('이슬',   '회색마을',   5),
('웅',   '비주기마을', 4),
('실버',   '상록마을',   7),
('한울',   '검은마을',   0);

select * from pokemon p join trainers j on p.trainer_id = j.id where p.name='피카츄';

select p.name from pokemon p join trainers t on p.trainer_id = t.id where t.name = '지우';

select p.name,p.level from pokemon p join trainers t on p.trainer_id = t.id where t.badge_count >= 5 order by p.level;

select * from pokemon p join public.trainers t on t.id = p.trainer_id where t.hometown = '회색마을' and p.type = '물';

-- 실습1 태초마을 출신 트레이너의 포켓몬 중 공격력이 가장 높은 1마리
select p.name,p.attack from trainers t join pokemon p on t.id = p.trainer_id where t.hometown = '태초마을' order by p.attack desc limit 1;

-- 실습2 주인이 있는 포켓몬 중 전설인 포켓몬과 그 주인을 찾으세요
select * from trainers t join pokemon p on t.id = p.trainer_id and p.is_legendary = true;

-- 실습3 포켓몬 이름과 주인 이름을 피카츄(지우) 형태로 보여주세요
select t.name||'('||p.name||')' from trainers t join pokemon p on t.id = p.trainer_id;

-- 실습4 주인이 없는 포켓몬(야생)의 이름과 타입을 찾으세요
select p.name,p.type from pokemon p left join trainers t on p.trainer_id = t.id where p.trainer_id is null;

-- 실습 5 모든 포켓몬을 보여주되, 주인이 없으면 야생이라고 표시하세요
select p.name,COALESCE(t.name,'야생') from pokemon p left join trainers t on t.id = p.trainer_id ;

CREATE TABLE moves (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    power INTEGER NOT NULL CHECK (power >= 0)
);


INSERT INTO moves (name, type, power) VALUES
('백만볼트',   '전기', 90),
('전기충격',   '전기', 40),
('화염방사',   '불꽃', 90),
('불꽃세례',   '불꽃', 40),
('하이드로펌프','물',  110),
('물대포',     '물',   40),
('솔라빔',     '풀',   120),
('덩굴채찍', '풀',   35),
('몸통박치기', '노말', 40),
('파괴광선', '노말', 150);

CREATE TABLE pokemon_moves (
    pokemon_id INTEGER NOT NULL,
    move_id    INTEGER NOT NULL,
    learned_at DATE DEFAULT CURRENT_DATE,
    PRIMARY KEY (pokemon_id, move_id),
    FOREIGN KEY (pokemon_id) REFERENCES pokemon(id),
    FOREIGN KEY (move_id)    REFERENCES moves(id)
);

select * from pokemon;
select * from trainers;
select * from moves;
select * from pokemon_moves;

INSERT INTO pokemon_moves (pokemon_id, move_id) VALUES (1, 1);
INSERT INTO pokemon_moves (pokemon_id, move_id) VALUES (1, 2);

delete from pokemon_moves;

INSERT INTO pokemon_moves (pokemon_id, move_id)
SELECT p.id, m.id
FROM pokemon p, moves m
WHERE (p.name = '피카츄'   AND m.name IN ('백만볼트', '전기충격', '몸통박치기'))
   OR (p.name = '리자몽'   AND m.name IN ('화염방사', '불꽃세례', '몸통박치기'))
   OR (p.name = '거북왕'   AND m.name IN ('하이드로펌프', '물대포'))
   OR (p.name = '이상해꽃' AND m.name IN ('솔라빔', '덩굴채찍', '몸통박치기'));

SELECT * FROM pokemon_moves;

-- 다른 포켓몬들에게도 기술을 가르치자
INSERT INTO pokemon_moves (pokemon_id, move_id)
SELECT p.id, m.id
FROM pokemon p, moves m
WHERE (p.name = '라이카' AND m.name IN ('백만볼트', '몸통박치기'))
   OR (p.name = '망나녕' AND m.name IN ('파괴광선', '몸통박치기'))
   OR (p.name = '프리져' AND m.name IN ('몸통박치기'))
   OR (p.name = '헤라크로스' AND m.name IN ('몸통박치기'))
   OR (p.name = '땍구리' AND m.name IN ('몸통박치기'));

SELECT * FROM pokemon_moves;

select * from pokemon where name = '망나녕';

select p.name,m.name from pokemon p join pokemon_moves pm on p.id = pm.pokemon_id join moves m on pm.move_id = m.id;

select *
from trainers t join pokemon p
    on t.id = p.trainer_id
    join pokemon_moves pm
        on p.id = pm.pokemon_id
    join moves m on pm.move_id = m.id
where t.name = '지우';

select *
from pokemon p left join pokemon_moves pm
    on p.id = pm.pokemon_id;

-- 실습1
select p.name,m.name from pokemon p join pokemon_moves pm on p.id = pm.pokemon_id join moves m on pm.move_id = m.id where p.name = '피카츄';

select p.name from pokemon p join pokemon_moves pm on p.id = pm.pokemon_id join moves m on pm.move_id = m.id where m.name = '몸통박치기';

select p.name,m.name from pokemon p join pokemon_moves pm on p.id = pm.pokemon_id join moves m on pm.move_id = m.id and m.power >= 90;

select * from pokemon join pokemon_moves pm on pokemon.id = pm.pokemon_id join moves m on m.id = pm.move_id and m.type='불꽃';

select * from pokemon p join pokemon_moves pm on  p.id = pm.pokemon_id join moves m on m.id = pm.move_id and p.type = m.type;

select *
from pokemon p left join pokemon_moves pm
    on p.id = pm.pokemon_id
    left join moves m
        on pm.move_id = m.id
    join trainers t on p.trainer_id = t.id
where pm is null;

select count(*), avg(level),max(attack), min(defense) from pokemon;

select round(avg(level),2) from pokemon;

select count(*) from pokemon p join trainers t on p.trainer_id = t.id where t.name='지우';

select count(*) from pokemon p join pokemon_moves pm on p.id = pm.pokemon_id and p.name = '피카츄';

select type,count(*) from pokemon group by type;

select type,count(*) cnt from pokemon p group by type order by cnt desc;

select type, round(avg(level)) avg_level from pokemon p group by type order by avg_level desc ;

select count(*), type,is_legendary from pokemon group by type ,is_legendary;

select t.name,count(*) from trainers t left join pokemon p on t.id = p.trainer_id group by t.id, t.name having count(*) >=2 order by count(*)desc;

select t.name,avg(p.level) from trainers t left join pokemon p on t.id = p.trainer_id group by t.name,t.id;

select m.name,count(*) from moves m left join pokemon_moves pm on m.id = pm.move_id group by m.name;

-- 실습1 타입별 포켓몬 수를 많은 순으로 보여주세요
select type,count(*) cnt from pokemon group by type order by cnt desc;

select type from pokemon group by type having avg(level) >= 30;

select t.name,count(p.id) from trainers t left join pokemon p on t.id = p.trainer_id group by t.id;




