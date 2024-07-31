-- schema.sql
CREATE TABLE IF NOT EXISTS account (
                                       account_id BIGINT NOT NULL AUTO_INCREMENT,
                                       cardinal INTEGER,
                                       total INTEGER,
                                       description VARCHAR(255),
                                       created_at DATETIME(6),
                                       modified_at DATETIME(6),
                                       PRIMARY KEY (account_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS attendance (
                                          attendance_id BIGINT NOT NULL AUTO_INCREMENT,
                                          is_attend BIT,
                                          attendance_date_time DATETIME(6),
                                          user_id BIGINT,
                                          week_id BIGINT,
                                          created_at DATETIME(6),
                                          modified_at DATETIME(6),
                                          PRIMARY KEY (attendance_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS comment (
                                       comment_id BIGINT NOT NULL AUTO_INCREMENT,
                                       is_deleted BIT DEFAULT FALSE NOT NULL,
                                       post_id BIGINT,
                                       time DATETIME(6),
                                       user_id BIGINT,
                                       content VARCHAR(255) NOT NULL,
                                       created_at DATETIME(6),
                                       modified_at DATETIME(6),
                                       PRIMARY KEY (comment_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS comment_children (
                                                children_comment_id BIGINT NOT NULL,
                                                comment_comment_id BIGINT NOT NULL,
                                                UNIQUE (children_comment_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS event (
                                     event_id BIGINT NOT NULL AUTO_INCREMENT,
                                     cardinal INTEGER,
                                     start_date_time DATETIME(6),
                                     end_date_time DATETIME(6),
                                     user_id BIGINT NOT NULL,
                                     content VARCHAR(255),
                                     location VARCHAR(255),
                                     member_number VARCHAR(255),
                                     required_items VARCHAR(255),
                                     title VARCHAR(255),
                                     type ENUM('ATTENDANCE', 'EVENT', 'NOTICE'),
                                     created_at DATETIME(6),
                                     modified_at DATETIME(6),
                                     PRIMARY KEY (event_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS event_file_urls (
                                               event_event_id BIGINT NOT NULL,
                                               file_urls_file_id BIGINT NOT NULL,
                                               UNIQUE (file_urls_file_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS file (
                                    file_id BIGINT NOT NULL AUTO_INCREMENT,
                                    url VARCHAR(255),
                                    created_at DATETIME(6),
                                    modified_at DATETIME(6),
                                    PRIMARY KEY (file_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS penalty (
                                       penalty_id BIGINT NOT NULL AUTO_INCREMENT,
                                       user_id BIGINT,
                                       penalty_description VARCHAR(255) NOT NULL,
                                       created_at DATETIME(6),
                                       modified_at DATETIME(6),
                                       PRIMARY KEY (penalty_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS post (
                                    post_id BIGINT NOT NULL AUTO_INCREMENT,
                                    user_id BIGINT,
                                    content VARCHAR(255) NOT NULL,
                                    title VARCHAR(255) NOT NULL,
                                    time DATETIME(6),
                                    total_comments BIGINT,
                                    created_at DATETIME(6),
                                    modified_at DATETIME(6),
                                    PRIMARY KEY (post_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS post_file_urls (
                                              file_urls_file_id BIGINT NOT NULL,
                                              post_post_id BIGINT NOT NULL,
                                              UNIQUE (file_urls_file_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS post_parent_comments (
                                                    parent_comments_comment_id BIGINT NOT NULL,
                                                    post_post_id BIGINT NOT NULL,
                                                    UNIQUE (parent_comments_comment_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS receipt (
                                       receipt_id BIGINT NOT NULL AUTO_INCREMENT,
                                       account_id BIGINT,
                                       amount INTEGER,
                                       date DATE,
                                       description VARCHAR(255),
                                       image_url VARCHAR(255),
                                       created_at DATETIME(6),
                                       modified_at DATETIME(6),
                                       PRIMARY KEY (receipt_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
                                     user_id BIGINT NOT NULL AUTO_INCREMENT,
                                     cardinals VARCHAR(255),
                                     email VARCHAR(255),
                                     name VARCHAR(255),
                                     password VARCHAR(255),
                                     refresh_token VARCHAR(255),
                                     student_id VARCHAR(255),
                                     tel VARCHAR(255),
                                     department ENUM('AI', 'BUSINESS', 'COMPUTER_SCIENCE', 'INDUSTRIAL_ENGINEERING', 'SW', 'VISUAL_DESIGN'),
                                     position ENUM('BE', 'D', 'FE'),
                                     role ENUM('ADMIN', 'USER'),
                                     status ENUM('ACTIVE', 'BANNED', 'LEFT', 'WAITING'),
                                     attendance_count INTEGER,
                                     attendance_rate INTEGER,
                                     created_at DATETIME(6),
                                     modified_at DATETIME(6),
                                     PRIMARY KEY (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS week (
                                    week_id BIGINT NOT NULL AUTO_INCREMENT,
                                    cardinal INTEGER,
                                    date DATE,
                                    week_number INTEGER,
                                    attendance_code VARCHAR(255),
                                    week_info VARCHAR(255),
                                    created_at DATETIME(6),
                                    modified_at DATETIME(6),
                                    PRIMARY KEY (week_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS receipt_images (
                                              receipt_receipt_id BIGINT NOT NULL,
                                              images_file_id BIGINT NOT NULL,
                                              PRIMARY KEY (receipt_receipt_id, images_file_id)
    ) ENGINE=InnoDB;

-- Foreign Key Constraints
ALTER TABLE attendance
    ADD CONSTRAINT FKjcaqd29v2qy723owsdah2t8vx FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE attendance
    ADD CONSTRAINT FK2r76ie9bkrqenigcqvelf2nnx FOREIGN KEY (week_id) REFERENCES week (week_id);

ALTER TABLE comment
    ADD CONSTRAINT FKs1slvnkuemjsq2kj4h3vhx7i1 FOREIGN KEY (post_id) REFERENCES post (post_id);

ALTER TABLE comment
    ADD CONSTRAINT FKqm52p1v3o13hy268he0wcngr5 FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE comment_children
    ADD CONSTRAINT FKhfy6pj40df0q5pdyuei2nadd3 FOREIGN KEY (children_comment_id) REFERENCES comment (comment_id);

ALTER TABLE comment_children
    ADD CONSTRAINT FKsmnqex4yd57o0g4wl6mwptcm3 FOREIGN KEY (comment_comment_id) REFERENCES comment (comment_id);

ALTER TABLE event
    ADD CONSTRAINT FK31rxexkqqbeymnpw4d3bf9vsy FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE event_file_urls
    ADD CONSTRAINT FK79wtbc2mtfshl4rq6exnsapd7 FOREIGN KEY (file_urls_file_id) REFERENCES file (file_id);

ALTER TABLE event_file_urls
    ADD CONSTRAINT FKicqnwj7k0ekl9rsswrxcgcjsl FOREIGN KEY (event_event_id) REFERENCES event (event_id);

ALTER TABLE penalty
    ADD CONSTRAINT FKqg3stjpb11u1ij1qclpi8ol9m FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE post
    ADD CONSTRAINT FK7ky67sgi7k0ayf22652f7763r FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE post_file_urls
    ADD CONSTRAINT FKmtbf98u6kx0x8r8hldolqqmjj FOREIGN KEY (file_urls_file_id) REFERENCES file (file_id);

ALTER TABLE post_file_urls
    ADD CONSTRAINT FKiilpq9ykr3rl9au2x99cvntta FOREIGN KEY (post_post_id) REFERENCES post (post_id);

ALTER TABLE post_parent_comments
    ADD CONSTRAINT FKm4ssgt3wvdhx68wp58m8q66b7 FOREIGN KEY (parent_comments_comment_id) REFERENCES comment (comment_id);

ALTER TABLE post_parent_comments
    ADD CONSTRAINT FKtm0bqww2mfv9r6nuo89okvbdw FOREIGN KEY (post_post_id) REFERENCES post (post_id);

ALTER TABLE receipt
    ADD CONSTRAINT FK7jbjwo4ybdl7qtjwkp4kitbh0 FOREIGN KEY (account_id) REFERENCES account (account_id);

ALTER TABLE receipt_images
    ADD CONSTRAINT UKhnd2vlybsr4ncw3r5ej75tcet UNIQUE (images_file_id);

ALTER TABLE receipt_images
    ADD CONSTRAINT FKrf6n56lhhudutieg866watbwn FOREIGN KEY (images_file_id) REFERENCES file (file_id);

ALTER TABLE receipt_images
    ADD CONSTRAINT FKp9c9dpxjpms5m7fxl4eojhsui FOREIGN KEY (receipt_receipt_id) REFERENCES receipt (receipt_id);