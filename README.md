### Hexlet tests and linter status:
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pavelchervonenko_java-project-99&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=pavelchervonenko_java-project-99) [![Actions Status](https://github.com/pavelchervonenko/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/pavelchervonenko/java-project-99/actions) [![Java CI](https://github.com/pavelchervonenko/java-project-99/actions/workflows/main.yml/badge.svg)](https://github.com/pavelchervonenko/java-project-99/actions/workflows/main.yml)

# Task Manager

Task Manager — это веб-приложение для управления задачами. Позволяет создавать задачи, назначать исполнителей, устанавливать статусы и метки для удобной организации работы.

## Сайт

[https://task-manager-duou.onrender.com](https://task-manager-duou.onrender.com)

Для входа используйте:
- **Email:** hexlet@example.com
- **Password:** qwerty

## Технологии

- **Java 21**
- **Spring Boot 4** — основной фреймворк
- **Spring Security + JWT** — аутентификация
- **Spring Data JPA / Hibernate** — работа с базой данных
- **PostgreSQL** — база данных (production)
- **H2** — база данных (development / тесты)
- **MapStruct** — маппинг DTO
- **Lombok** — уменьшение boilerplate-кода
- **Gradle** — сборка проекта
- **Sentry** — мониторинг ошибок
- **SonarCloud** — анализ качества кода
- **Docker** — контейнеризация
- **Render** — хостинг

## Как запустить локально

### Требования

- Java 21
- Node.js и npm (для сборки фронтенда)

### Шаги

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/pavelchervonenko/java-project-99.git
   cd java-project-99
   ```

2. Создайте файл `.env` в корне проекта и заполните переменные:
   ```
   ADMIN_PASSWORD=qwerty
   SENTRY_AUTH_TOKEN=your_token
   SENTRY_DSN=your_dsn
   ```

3. Сгенерируйте RSA-ключи для JWT:
   ```bash
   mkdir -p src/main/resources/certs
   openssl genrsa -out src/main/resources/certs/private.pem 2048
   openssl rsa -in src/main/resources/certs/private.pem -pubout -out src/main/resources/certs/public.pem
   ```

4. Соберите фронтенд:
   ```bash
   npm i @hexlet/java-task-manager-frontend
   npx build-frontend
   ```

5. Запустите:
   ```bash
   ./gradlew bootRun
   ```

6. Откройте в браузере: [http://localhost:8080](http://localhost:8080)

## API документация

После запуска доступна по адресу: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
