CREATE DATABASE crm_app;
USE crm_app;

CREATE TABLE IF NOT EXISTS roles (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    fullname VARCHAR(100) NOT NULL,
    avatar VARCHAR(100),
    role_id INT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS status (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS jobs (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    start_date DATE,
    end_date DATE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tasks (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    start_date DATE,
    end_date DATE,
    user_id INT NOT NULL,
    job_id INT NOT NULL,
    status_id INT NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE users ADD FOREIGN KEY (role_id) REFERENCES roles (id)  ON DELETE CASCADE;
ALTER TABLE tasks ADD FOREIGN KEY (user_id) REFERENCES users (id)  ON DELETE CASCADE;
ALTER TABLE tasks ADD FOREIGN KEY (job_id) REFERENCES jobs (id)  ON DELETE CASCADE;
ALTER TABLE tasks ADD FOREIGN KEY (status_id) REFERENCES status (id)  ON DELETE CASCADE;

INSERT INTO roles( name, description ) VALUES ("ROLE_ADMIN", "Quản trị viên");
INSERT INTO roles( name, description ) VALUES ("ROLE_LEADER", "Quản lý dự án");
INSERT INTO roles( name, description ) VALUES ("ROLE_MEMBER", "Thành viên");

INSERT INTO status( name ) VALUES ("Chưa thực hiện");
INSERT INTO status( name ) VALUES ("Đang thực hiện");
INSERT INTO status( name ) VALUES ("Đã hoàn thành");

INSERT INTO users(email, password, fullname, avatar, role_id)
VALUES ('admin1@gmail.com', '1111', 'Anh Bình', 'admin.jpeg', 1);
INSERT INTO users(email, password, fullname, avatar, role_id)
VALUES ('leader1@gmail.com', '1111', 'Anh Khoa Mentor', 'leader.jpeg', 2);
INSERT INTO users(email, password, fullname, avatar, role_id)
VALUES ('leader2@gmail.com', '1111', 'Anh Hoàng Mentor', 'leader.jpeg', 2);
INSERT INTO users(email, password, fullname, avatar, role_id)
VALUES ('member1@gmail.com', '1111', 'Phạm Ngọc Hùng', 'member.jpeg', 3);
INSERT INTO users(email, password, fullname, avatar, role_id)
VALUES ('member2@gmail.com', '2222', 'Nguyễn Bảo Duy', 'member.jpeg', 3);
INSERT INTO users(email, password, fullname, avatar, role_id)
VALUES ('member3@gmail.com', '3333', 'Trần Công Huy', 'member.jpeg', 3);
INSERT INTO users(email, password, fullname, avatar, role_id)
VALUES ('member4@gmail.com', '4444', 'Đỗ Thành Huy', 'member.jpeg', 3);

INSERT INTO jobs(name, start_date, end_date) VALUES ('CRM', '2022-12-01', '2023-04-28');
INSERT INTO jobs(name, start_date, end_date) VALUES ('eFashion', '2023-03-15', '2023-04-28');

INSERT INTO tasks(name, start_date, end_date, user_id, job_id, status_id)
VALUES ('Database', '2023-02-15', '2023-02-20', 2, 1, 3);
INSERT INTO tasks(name, start_date, end_date, user_id, job_id, status_id)
VALUES ('Database', '2023-03-16', '2023-03-21', 3, 2, 3);
INSERT INTO tasks(name, start_date, end_date, user_id, job_id, status_id)
VALUES ('Back-End', '2023-02-15', '2023-04-01', 4, 1, 3);
INSERT INTO tasks(name, start_date, end_date, user_id, job_id, status_id)
VALUES ('Front-End', '2023-02-18', '2023-04-23', 5, 1, 1);
INSERT INTO tasks(name, start_date, end_date, user_id, job_id, status_id)
VALUES ('Front-End', '2023-03-20', '2023-04-26', 6, 2, 2);
INSERT INTO tasks(name, start_date, end_date, user_id, job_id, status_id)
VALUES ('Back-End', '2023-03-25', '2023-04-21', 7, 2, 3);
INSERT INTO tasks(name, start_date, end_date, user_id, job_id, status_id)
VALUES ('Supervisor', '2023-03-27', '2023-04-25', 4, 2, 3);
INSERT INTO tasks(name, start_date, end_date, user_id, job_id, status_id)
VALUES ('Supervisor', '2023-03-27', '2023-04-25', 4, 1, 3);