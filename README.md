# Architrave Backend Repository.

Seamless Portfolio / Archive for Artist

Architrave is a portfolio web service that empowers contemporary artists\
to directly manage their projects and works.

## Now Status
MVP-1 released.

Contact us for a test Artist ID

## Key Features
### MVP-1
| Object            | involved          | CRUD                         |
|-------------------|-------------------|------------------------------|
| Auth              | -                 | Signin, Login                |
| Member            | -                 | Read                         |
| LandingBox        | -                 | Read, Update                 |
| Project           | -                 | Create, Read, Update         |
| ->                | ProjectInfo       | Create, Read, Update, Delete |
| ->                | ProjectElement    | Create, Read, Update, Delete |
| Work              | -                 | Create, Read, Update         | 
| TextBox           | -                 | Create, Read, Update         | 
| Divider           | -                 | Create, Read, Delete         | 


### APIs
- [Swagger 링크](http://43.202.45.205:8080/swagger-ui/index.html)

## Tech Stack
- Java with Spring Boot
- Spring Data JPA, AWS RDS: PostgreSQL for DB
- Spring Security, JWT for auth
- AWS EC2 for deploy

## Settings
### ERD
![arch_mvp-1_ERD](https://github.com/user-attachments/assets/95533ea9-b641-4a94-94bd-385039c7749e)


### Architecture
![arch_mvp-1_Architecture](https://github.com/user-attachments/assets/3c8af999-e1e9-4924-b0a1-e4b71354b2b8)


## Contact
| Position    | Name                       | Email                 |
|-------------|----------------------------|-----------------------|
| PM          | Kim Youngdong              | nfs82young@gmail.com  |
| Frontend    | Kim Youngdong              | nfs82young@gmail.com  |
| Backend     | Kim Youngdong              | nfs82young@gmail.com  |
| Design      | Jung Joohee                | jjh62128@gmail.com    |
| Design      | Choi Jihee                 | zhee4820@gmail.com    |
