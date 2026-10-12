create table pokemon(
    id serial primary key ,
    name varchar(50) NOT NULL UNIQUE ,
    type varchar(20) NOT NULL ,
    level integer not null CHECK (level >= 1 AND level <= 100) ,
    attack integer NOT NULL ,
    defense integer NOT NULL ,
    hp integer NOT NULL ,
    is_legendary boolean DEFAULT FALSE ,
    caughtAt timestamp DEFAULT CURRENT_TIMESTAMP
);

drop table pokemon;

insert into pokemon (name, type, level, attack, defense, hp)
values ('피카츄','전기',12,20, 11,100);

insert into pokemon (name, type, level, attack, defense, hp)
values ('피카츄','전기',12,20, 11,100);

insert into pokemon (name, type, level, attack, defense, hp)
values ('리자몽',2,'이십이',50, 22,150);

insert into pokemon (name, type, attack, defense, hp)
values ('꼬부기','물',20, 11,100);


ALTER TABLE pokemon_sample ALTER COLUMN created_at TYPE TIMESTAMP USING created_at::TIMESTAMP;

ALTER TABLE pokemon ALTER COLUMN level SET NOT NULL;

truncate table pokemon;

select * from pokemon;

INSERT INTO pokemon (name, type, level, attack, defense, hp, is_legendary) VALUES
('피카츄',     '전기',   35, 55,  40,  35,  FALSE),
('라이츄',     '전기',   50, 90,  55,  60,  FALSE),
('리자몽',     '불꽃',   36, 84,  78,  78,  FALSE),
('파이리',     '불꽃',   12, 52,  43,  39,  FALSE),
('리자드',     '불꽃',   24, 64,  58,  58,  FALSE),
('거북왕',     '물',     36, 83,  100, 79,  FALSE),
('꼬부기',     '물',     10, 48,  65,  44,  FALSE),
('아쿠스타',    '물',     27, 65,  60,  65,  FALSE),
('이상해꽃',   '풀',     32, 82,  83,  80,  FALSE),
('이상해씨',   '풀',     8,  49,  49,  45,  FALSE),
('모다피',     '풀',     18, 45,  50,  60,  FALSE),
('잠만보',     '노말',   40, 110, 65,  160, FALSE),
('캥카',       '노말',   28, 65,  60,  65,  FALSE),
('윤겔라',     '에스퍼',  45, 65,  65,  70,  FALSE),
('슬리프',     '에스퍼',  30, 50,  45,  60,  FALSE),
('캐이시',     '에스퍼',  16, 20,  15,  25,  FALSE),
('야도란',     '물',     22, 65,  60,  50,  FALSE),
('팬텀',       '고스트',  38, 65,  60,  60,  FALSE),
('고오스',     '고스트',  20, 35,  30,  30,  FALSE),
('딱충이',     '벌레',   25, 50,  95,  40,  FALSE),
('독침붕',     '벌레',   34, 75,  85,  55,  FALSE),
('헤라크로스', '벌레',   42, 125, 79,  80,  FALSE),
('피죤',       '비행',   15, 45,  40,  40,  FALSE),
('피죤투',     '비행',   33, 80,  75,  65,  FALSE),
('구구',       '비행',   9,  45,  40,  40,  FALSE),
('딱구리',     '바위',   28, 80,  50,  50,  FALSE),
('망나뇽',     '드래곤',  55, 134, 95,  91,  FALSE),
('프리져',     '얼음',   50, 85,  100, 90,  TRUE),
('썬더',       '전기',   50, 90,  85,  90,  TRUE),
('파이어',     '불꽃',   50, 100, 90,  90,  TRUE),
('뮤츠',       '에스퍼',  70, 110, 90,  106, TRUE);

-- 전기 타입만
SELECT name, type, level FROM pokemon WHERE type = '전기';

-- 레벨이 정확히 50
SELECT name, level FROM pokemon WHERE level = 50;

-- 레벨 40 초과
SELECT name, level FROM pokemon WHERE level > 40;

-- 레벨 40 이상
SELECT name, level FROM pokemon WHERE level >= 40;

-- 전기 타입이 아닌 것
SELECT name, type FROM pokemon WHERE type != '전기';

-- 레벨 20~40 사이 (양쪽 끝 포함)
SELECT name, level FROM pokemon WHERE level BETWEEN 20 AND 40;

-- 위와 완전히 같은 의미
SELECT name, level FROM pokemon WHERE level >= 20 AND level <= 40;

-- 불꽃 또는 물 또는 풀
SELECT name, type FROM pokemon WHERE type IN ('불꽃', '물', '풀');

-- 위와 같은 의미 (그러나 길다)
SELECT name, type FROM pokemon
WHERE type = '불꽃' OR type = '물' OR type = '풀';

-- 그 셋이 아닌 타입
SELECT name, type FROM pokemon WHERE type NOT IN ('불꽃', '물', '풀');

-- '리'로 시작하는 이름
SELECT name FROM pokemon WHERE name LIKE '리%';

-- '이'로 끝나는 이름
SELECT name FROM pokemon WHERE name LIKE '%이';

-- '드'가 중간 어딘가에 들어간 이름
SELECT name FROM pokemon WHERE name LIKE '%드%';

-- 정확히 3글자인 이름
SELECT name FROM pokemon WHERE name LIKE '___';

-- '리자'로 시작하고 뒤에 한 글자
SELECT name FROM pokemon WHERE name LIKE '리자_';

-- 불꽃 타입이면서 레벨 30 이상
SELECT name, type, level FROM pokemon
WHERE type = '불꽃' AND level >= 30;

-- 전설이거나 레벨 50 이상
SELECT name, level, is_legendary FROM pokemon
WHERE is_legendary = TRUE OR level >= 50;

-- 세 개 이상
SELECT name, type, level, attack FROM pokemon
WHERE type = '불꽃' AND level >= 20 AND attack >= 60;

alter table pokemon add column nickname varchar(100);

select * from pokemon where pokemon.nickname is null;

-- 실습
select name, level from pokemon where type = '물';
select name,level from pokemon where level >= 30 order by level;
select name from pokemon where is_legendary = true;
select name from pokemon order by attack limit 3;
select name from pokemon where name like '%리%';
select name,type,level from pokemon where type in ('불꽃','물') and level >= 20;
select name,hp from pokemon where hp between 50 and 100 order by hp desc;
select distinct type from pokemon;
select name,attack,defense from pokemon where attack > defense+20;
select name,attack+defense+hp total from pokemon where is_legendary = false order by total desc limit 5;
select name,attack,type from pokemon order by type,attack desc ;
select name,level from pokemon order by level desc limit 5 offset 5;

select * from pokemon;

begin;

update pokemon set level = 100;

select * from pokemon;

rollback;

update pokemon set level = 100 where name = '피카츄';

commit;

create table trainers(
    id serial primary key ,
    name varchar(5) not null ,
    pokemon_count integer not null default 0
);

insert into trainers (name,pokemon_count) values ('지우',6), ('이슬',3);

select * from trainers;

begin;
update trainers set pokemon_count = pokemon_count -1 where name = '지우';
update trainers set pokemon_count = pokemon_count +1 where name = '이슬';
commit;
rollback ;

-- 실습
insert into pokemon (name, type, level, attack, defense, hp) values ('이브이','노말',25,55,50,55);
insert into pokemon (name, type, level, attack, defense, hp) values ('샤미드','얼음',30,65,60,65),
                                                                    ('부스터','불꽃',28,65,55,65);
update pokemon set level = 45 where name = '거북왕';
update pokemon set attack = attack + 10 where type = '전기';
update pokemon set is_legendary = true where level >= 60;
select count(*) from pokemon where level < 15;
delete from pokemon where level < 15;
begin;
delete from pokemon where type = '바위';
rollback ;
begin ;
select hp from pokemon;
update pokemon set hp = hp*2;
commit;
drop table trainers;

alter table pokemon add column trainer_id integer;

alter table pokemon add constraint fk_pokemon_trainer foreign key(trainer_id) references trainers(id);

select name,pokemon.trainer_id from pokemon order by trainer_id;

update pokemon set trainer_id = 1 where name in ( '피카츄','리자몽','거북왕','이상해꽃');
UPDATE pokemon SET trainer_id = 2 WHERE name IN ('꼬부기', '아쿠스타');
UPDATE pokemon SET trainer_id = 3 WHERE name IN ('딱충이', '독침붕', '헤라크로스');
UPDATE pokemon SET trainer_id = 4 WHERE name IN ('딱구리', '망나뇽');






