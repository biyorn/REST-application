CREATE TABLE certificate (
	id BIGSERIAL primary key,
	name VARCHAR(15) not null,
	description VARCHAR(255) not null,
	price NUMERIC(10,2) not null,
	creation_date TIMESTAMP not null,
	modification_date TIMESTAMP not null,
	duration_days INT not null,
	active BOOLEAN not null
);

CREATE TABLE tag (
	id BIGSERIAL primary key,
	title VARCHAR(20) not null,
	CONSTRAINT name UNIQUE(title)
);

CREATE TABLE tag_certificate(
	certificate_id INT references certificate(id) ON DELETE CASCADE,,
	tag_id INT references tag(id) ON DELETE CASCADE
);

CREATE TABLE users(
    id BIGSERIAL primary key,
    login VARCHAR(30) not null,
    password VARCHAR(60),
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    CONSTRAINT name UNIQUE(login)
);

CREATE TABLE orders(
    id BIGSERIAL primary key,
    cost NUMERIC(20, 2) not null,
    time TIMESTAMP not null,
    user_id BIGINT not null references users(id) ON DELETE CASCADE
);

CREATE TABLE role(
    id BIGSERIAL primary key,
    name VARCHAR(15) not null
);

CREATE TABLE user_role(
    user_id BIGINT not null references users(id),
    role_id BIGINT not null references role(id)
);

CREATE TABLE bought_certificate(
    id BIGSERIAL primary key,
    order_id BIGINT not null references orders(id),
    certificate_id BIGINT not null references certificate(id),
    price NUMERIC(20, 2) not null
);

-- QUERY
SELECT t.id, t.title FROM orders o
    JOIN users u ON u.id = o.user_id
    JOIN bought_certificate oc ON oc.order_id = o.id
    JOIN certificate c ON oc.certificate_id = c.id
    JOIN tag_certificate tc ON c.id = tc.certificate_id
    JOIN tag t ON t.id = tc.tag_id
    WHERE u.id = (SELECT u.id FROM users u
        JOIN orders o ON u.id = o.user_id
        GROUP BY u.id
        ORDER BY SUM(o.cost) DESC LIMIT 1)
    GROUP BY t.id
    ORDER BY SUM(o.cost) DESC LIMIT 1


