DROP TABLE IF EXISTS categories cascade;
DROP TABLE IF EXISTS products;

create table categories
(
    category_id int not null auto_increment unique,
    category_name varchar(50),
    primary key(category_id)
);


create table products
(
    product_id int not null auto_increment unique,
    product_name varchar(50),
    product_price double,
    category_categoryId int,
    primary key(product_id),
    foreign key(category_categoryId) references categories(category_id) on delete cascade
);