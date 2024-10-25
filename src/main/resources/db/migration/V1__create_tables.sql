-- Создание таблицы для пользователей (UserSecurity)
CREATE TABLE user_security
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, -- Уникальный идентификатор пользователя
    login    VARCHAR(255) NOT NULL UNIQUE,                        -- Логин пользователя
    password VARCHAR(255) NOT NULL,                               -- Пароль пользователя
    role     VARCHAR(255) NOT NULL                                -- Роль пользователя (например, ADMIN, USER)
);

-- Создание таблицы для проектов (Project)
CREATE TABLE project
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, -- Уникальный идентификатор проекта
    project_name VARCHAR(255) NOT NULL,                               -- Имя проекта
    user_id      BIGINT,                                              -- Внешний ключ на таблицу user_security
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES user_security (id)
        ON DELETE SET NULL                                            -- При удалении пользователя ссылка становится NULL
);

-- Создание таблицы для записи времени (RecordTime)
CREATE TABLE record_time
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, -- Уникальный идентификатор записи
    total_time NUMERIC(21, 0) NOT NULL,                             -- Общее время разработки (изначально должно быть числовое)
    project_id BIGINT,                                              -- Внешний ключ на таблицу project
    user_id    BIGINT,                                              -- Внешний ключ на таблицу user_security
    CONSTRAINT fk_project FOREIGN KEY (project_id)
        REFERENCES project (id)
        ON DELETE CASCADE,                                          -- При удалении проекта все записи будут удалены
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES user_security (id)
        ON DELETE CASCADE                                           -- При удалении пользователя все записи будут удалены
);

-- Добавляем уникальный индекс для логина пользователя
CREATE UNIQUE INDEX user_security_login_uindex
    ON user_security (login);