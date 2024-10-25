
---

### README для проекта TimeTracker

---

#### Описание проекта

**TimeTracker** — это RESTful приложение для управления проектами и отслеживания времени, разработанное с использованием Java Spring Boot, PostgreSQL и Flyway. Оно предоставляет возможности аутентификации JWT и управления правами доступа, позволяя пользователям добавлять проекты, записывать и отслеживать время, а также управлять проектами и пользователями.

#### Основные функции

- **CRUD для проектов**: создание, обновление, получение и удаление проектов.
- **Отслеживание времени**: запись и управление временем, потраченным на проекты.
- **Аутентификация и авторизация**: вход с помощью JWT токенов и ограничение доступа на основе ролей.
- **Сортировка и фильтрация данных**: возможность фильтрации проектов и записей по пользователям и другим критериям.

#### Технологии

- **Backend**: Java 17, Spring Boot, Spring Data JPA, Spring Security
- **База данных**: PostgreSQL
- **Миграции**: Flyway
- **JWT для аутентификации**: JSON Web Tokens (JWT)
- **Документация API**: OpenAPI (Swagger)
- **Контейнеризация**: Docker

#### Требования

- Docker и Docker Compose
- Java 17 (если запускать без Docker)
- PostgreSQL 15+ (если запускать без Docker)
- Maven 3.8+ (если запускать без Docker)

#### Установка и запуск с Docker

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/ВашПользователь/TimeTracker.git
   ```

2. Настройте файл `.env` для переменных окружения PostgreSQL, если требуется (например, пароль пользователя).

3. Постройте и запустите контейнеры с помощью Docker Compose:
   ```bash
   docker-compose up --build
   ```

   > Контейнеры для приложения и базы данных PostgreSQL будут созданы и запущены автоматически.

4. Для остановки контейнеров используйте:
   ```bash
   docker-compose down
   ```

#### Установка и запуск без Docker

1. Настройте базу данных PostgreSQL и добавьте параметры в `application.yml`.

2. Запустите миграции Flyway:
   ```bash
   mvn flyway:migrate
   ```

3. Соберите и запустите проект:
   ```bash
   mvn clean install
   java -jar target/TimeTracker-0.0.1-SNAPSHOT.jar
   ```

---

