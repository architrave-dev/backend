# Architrave Backend Repository.

Seamless Portfolio / Archive for Artist

Architrave is a portfolio web service that empowers contemporary artists\
to directly manage their projects and works.

## Now Status
MVP-2 released.

Contact us for a test Artist ID

## Key Features
### MVP-2
Automatically token renewal using RefreshToken when AccessToken expires. \
Project delete feature. \
Resize feature for Work type in ProjectElement. \
Create Works page. 
- Sync with Work added when creating ProjectElement.

Create About page.
- Manage profile photo, name, nationality, email, and contact with MemberInfo
- Manage artist's career history with Career

### APIs
- [Swagger API](http://43.202.45.205:8080/swagger-ui/index.html)

## Tech Stack
- Java with Spring Boot
- Spring Data JPA, AWS RDS: PostgreSQL for DB
- Spring Security, JWT for auth
- AWS EC2, Docker for deploy

## Settings
### ERD
![arch_mvp-2 ERD](https://github.com/user-attachments/assets/a8e80292-455e-4c80-b7eb-3b0347234e3b)


### Architecture
![arch_mvp-2_Architecture](https://github.com/user-attachments/assets/c5efd860-57ed-4009-9554-b08114b74d8a)

## Considerations 
### About Auth
[AccessToken and RefreshToken Usage Flow](https://github.com/architrave-dev/backend/wiki/About-Auth#mvp-2)
### About Deploy
[Deployment Process Improvement Needed](https://github.com/architrave-dev/backend/wiki/About-Deploy#deployment-process-improvement-needed)


## Contact
| Position    | Name                       | Email                 |
|-------------|----------------------------|-----------------------|
| PM          | Kim Youngdong              | nfs82young@gmail.com  |
| Frontend    | Kim Youngdong              | nfs82young@gmail.com  |
| Backend     | Kim Youngdong              | nfs82young@gmail.com  |
| Design      | Jung Joohee                | jjh62128@gmail.com    |
| Design      | Choi Jihee                 | zhee4820@gmail.com    |
