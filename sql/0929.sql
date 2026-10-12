create table shop_members(
    id serial primary key ,
    name varchar(50) not null
);

insert into shop_members(name)values ('김철수'),('이영희');

CREATE TABLE orders_restrict (
    id SERIAL PRIMARY KEY,
    member_id INTEGER REFERENCES shop_members(id) ON DELETE RESTRICT,
    memo VARCHAR(50)
);

CREATE TABLE orders_cascade (
    id SERIAL PRIMARY KEY,
    member_id INTEGER REFERENCES shop_members(id) ON DELETE CASCADE,
    memo VARCHAR(50)
);

CREATE TABLE orders_setnull (
    id SERIAL PRIMARY KEY,
    member_id INTEGER REFERENCES shop_members(id) ON DELETE SET NULL,
    memo VARCHAR(50)
);

INSERT INTO orders_restrict (member_id, memo) VALUES (1, '김철수 주문');
INSERT INTO orders_cascade  (member_id, memo) VALUES (1, '김철수 주문');
INSERT INTO orders_setnull  (member_id, memo) VALUES (1, '김철수 주문');

delete from shop_members where id = 1;

delete from orders_restrict where id = 1;

select * from shop_members;

select * from orders_cascade;

select * from orders_setnull;

CREATE TABLE cafe_members (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE menus (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    price INTEGER NOT NULL CHECK (price >= 0),
    is_sold_out BOOLEAN NOT NULL DEFAULT FALSE,
    category_id INTEGER REFERENCES categories(id) ON DELETE SET NULL
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    member_id INTEGER NOT NULL REFERENCES cafe_members(id) ON DELETE RESTRICT,
    status VARCHAR(20) NOT NULL DEFAULT '접수'
        CHECK (status IN ('접수', '제조중', '완료', '취소')),
    ordered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    menu_id  INTEGER NOT NULL REFERENCES menus(id) ON DELETE RESTRICT,
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity > 0),
    price INTEGER NOT NULL,
    UNIQUE (order_id, menu_id)
);

INSERT INTO categories (name) VALUES ('커피'), ('차'), ('디저트');

INSERT INTO menus (name, price, category_id) VALUES
    ('아메리카노', 4500, 1),
    ('카페라떼',  5000, 1),
    ('얼그레이',   5500, 2),
    ('치즈케이크', 6500, 3);

INSERT INTO cafe_members (name, phone) VALUES
    ('김철수', '010-1111-1111'),
    ('이영희', '010-2222-2222');

INSERT INTO orders (member_id) VALUES (1);

INSERT INTO order_items (order_id, menu_id, quantity, price) VALUES
    (1, 1, 2, 4500),
    (1, 4, 1, 6500);

select c.name,m.name,oi.quantity,oi.price, oi.price*oi.quantity as 합계
from cafe_members c join orders o
    on c.id = o.member_id
    join order_items oi
        on o.id = oi.order_id
    join menus m on oi.menu_id = m.id;


CREATE TABLE bigdata (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(100),
    age INTEGER
);

-- 10만 행 생성 (몇 초 걸린다)
INSERT INTO bigdata (name, email, age)
SELECT
    '사용자' || i,
    'user' || i || '@example.com',
    (i % 60) + 20
FROM generate_series(1, 10000000) AS i;

select count(*) from bigdata;

drop table bigdata;

explain analyse
select * from bigdata where email = '777777@example.com';

create index idx_bigdata_email on bigdata (email);












