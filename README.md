# Bookstore CRUD (Spring Boot 3 + JWT + Bootstrap/jQuery)

Bu proje, verilen `bookstore` MariaDB şemasına göre JWT tabanlı kimlik doğrulama ile
Books, Customers ve Orders için tam CRUD REST API'ları ve sade bir Bootstrap/jQuery frontend içerir.

## Hızlı Başlangıç

1) `application.properties` dosyasında DB kullanıcı/parolasını ayarlayın.
```
spring.datasource.url=jdbc:mariadb://localhost:3306/bookstore
spring.datasource.username=root
spring.datasource.password=
```
2) DB şemasını mevcut dump ile kurun (sizin gönderdiğiniz SQL uygun). Kullanıcı tablosunda
`admin` kaydı örneği eklidir ve parola bcrypt ile `admin` olarak ayarlı görünmektedir.

3) Çalıştırın:
```
mvn spring-boot:run
```
Uygulama varsayılan olarak `http://localhost:8080`'da açılır.
Frontend: `http://localhost:8080/index.html`

## Giriş
- Varsayılan kullanıcı: `admin`
- Parola: `admin`
- Roller: `ROLE_ADMIN` (authorities tablosundan çekilir)

## API Örnekleri
- POST `/api/auth/login` `{ "username":"admin", "password":"admin" }` -> `token`
- GET `/api/books` (Authorization: Bearer <token>)
- POST/PUT/DELETE `/api/books/{id}` benzeri

## Notlar
- `spring.jpa.hibernate.ddl-auto=none` (mevcut şemayı kullanır). Eğer tablo/kolon adlarınız
  yukarıdaki entity alanlarıyla birebir eşleşmiyorsa, entity alan adlarını şemanıza göre değiştirin.
- CORS; `app.cors.allowed-origins` ile konfigüre edilmiştir.
- JWT süresi `app.jwt.expire-minutes` ile ayarlanır.
