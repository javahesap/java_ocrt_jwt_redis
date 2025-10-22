# Bookstore

Çalıştırma adımları:

1. `pom.xml` ile bağımlılıkları indirin.
2. `src/main/resources/application.properties` içindeki DB bilgileri sizde zaten:
   - jdbc:mysql://localhost:3306/bookstore
   - user: root
   - pass: (boş)
3. Aşağıdaki SQL'i kendi veritabanınızda **manuel** çalıştırın:

```
-- Admin kullanıcı (username: admin, password: 123456)
INSERT INTO users(username, password, enabled) VALUES
('admin', '$2b$12$.EJE42WgcIqB4qRRiHRJcO3IesqOj.a7OOR7hbuA/Qgub.IbzIOBm', 1);

INSERT INTO authorities(username, authority) VALUES
('admin', 'ROLE_ADMIN'),
('admin', 'ROLE_USER');
```

> Not: `users`/`authorities` tablolarınızda `username` alanını unique yapmanız önerilir.

4. Uygulamayı çalıştırın: `http://localhost:8081/`
5. CRUD ekranları: /books, /customers, /orders
6. REST: /api/books, /api/customers, /api/orders
