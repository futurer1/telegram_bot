
Для запуска всех сервисов необходимо выполнить следующие настройки
------------------------------------------------------------------

### 1. Сгенерировать хеш для пароля, используя скрипт generate-rabbitmq-hash-pass.sh
```
$ sh generate-rabbitmq-hash-pass.sh mypassword
+Kwx/xr/rwlzrNSL5hLxCqS/zNQUGGmmIbOUkxTxAGCg5sWZ
```

### 2. Вставить полученный хеш в файл rabbitmq-definitions.json
```
...
"password_hash": "4rez2EPKHeA01GPPtEgPHN4WfRXVwFhQv10RuUBPTFS9XPiI",
...
```

### 3. Создать со своего аккаунта нового бота через https://t.me/BotFather

### 4. Установить и запустить ngrok на порту 8084 (см. https://github.com/futurer1/Linux/blob/master/Ngrok.md )

### 5. Создать следующие файлы .env на основе их шаблонов.

#### 5.1. Файл .env (шаблон template.env)

```
TG_BOT_SALT_PHOTO=asdlkjqwjcriASDLKJQWEPOI123
TG_BOT_SALT_DOC=qwepoikmxoiuZXCSWMNBGDBE332
TG_BOT_SALT_USERID=zxmnoikmxoiuZXCSWMNBGDBE653

TG_BOT_NAME=name-of-tgbot
TG_BOT_TOKEN=token-tgbot
TG_BOT_OWNER=12345678
TG_BOT_URI=https://c1e2-188-243-183-52.ngrok-free.app
```

TG_BOT_SALT_PHOTO - соль для формирования ссылок на файлы фото
TG_BOT_SALT_DOC - соль для формирования ссылок на файлы документов
TG_BOT_SALT_USERID - соль для формирования кода подтверждения активации регистрации

TG_BOT_NAME - имя бота
TG_BOT_TOKEN - токен
TG_BOT_OWNER - id владельца бота
TG_BOT_URI - ссылка на внешний адрес https, предоставленный ngrok для получения ответов по webhook

#### 5.2. Файл postgre.env (шаблон postgre.template.env)

PG_USER - пользователь
PG_PASSWORD - пароль
POSTGRES_DB=tgbotmiddle (имя базы можно оставить без изменений)

POSTGRES_USER - пользователь
POSTGRES_PASSWORD - пароль
POSTGRES_DB=tgbotmiddle (имя базы можно оставить без изменений)

#### 5.3. Файл rabbitmq.env (шаблон rabbitmq.template.env)

RMQ_USER - пользователь
RMQ_PASSWORD - пароль

#### 5.4. Файл mail.env (шаблон mail.template.env)

TG_BOT_MAIL_HOST - имя сервера исходящей почты (например, smtp.mail.ru или smtp.yandex.ru)
TG_BOT_MAIL_NAME - название ящика
TG_BOT_MAIL_PASSWORD - пароль для внешнего доступа к ящику

### 6. Собрать проект используя wrapper gradle

```
./gradlew build
```

### 7. Собрать образы и запустить контейнеры

```
docker-compose up -d
```
