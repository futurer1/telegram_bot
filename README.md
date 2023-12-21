
Для работы бота необходимо установить переменные окружения
----------------------------------------------------------

```
sudo nano ~/.bashrc
```
```
source ~/.bashrc
```

// токен, имя бота, id владельца бота, ссылка на внешний адрес https для получения ответов по webhook

```
TG_BOT_TOKEN
TG_BOT_NAME
TG_BOT_OWNER
TG_BOT_URI
```

// пользователь и пароль для RabbitMQ

```
RMQ_USER
RMQ_PASSWORD
```

// пользователь и пароль для PostgreSQL

```
PG_USER
PG_PASSWORD
```

// соль для генерации адреса скачивания документов и фото
// соль для шифрования id пользователя

```
TG_BOT_SALT_PHOTO
TG_BOT_SALT_DOC
TG_BOT_SALT_USERID
```

// имя сервера исходящей почты (например, smtp.mail.ru или smtp.yandex.ru), название ящика и пароль
```
TG_BOT_MAIL_HOST
TG_BOT_MAIL_NAME
TG_BOT_MAIL_PASSWORD
```