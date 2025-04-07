# Architrave Backend Repository.

Seamless Portfolio / Archive for Artist

Architrave is a portfolio web service that empowers contemporary artists\
to directly manage their projects and works.

## Now Status
MVP-4 released.

Contact us for a test Artist ID

## Key Features
### MVP-4
- Changed page-level bulk updates to individual updates
- Created Contact Page 
- Created Settings Page
- Created image processing logic using AWS Lambda functions
  - Creates 5 responsive image sizes based on device width
  - Removes related images when an object is deleted

### APIs
- [Swagger API](https://api.architrive.com/swagger-ui/index.html)
  
## Tech Stack
- Java with Spring Boot
- Spring Data JPA, AWS RDS: PostgreSQL for DB
- Spring Security, JWT for auth
- AWS EC2, Docker for deploy

## Settings
### ERD
![arch_mvp-4_ERD](https://github.com/user-attachments/assets/10bac757-80c3-41c3-ac58-787472bdde91)

### Architecture
![MVP-4 Architecture](https://github.com/user-attachments/assets/3d70efb8-4a1d-453c-a825-6a7a168b73c8)


## Considerations 
### MVP-4
- [Individual updates vs Bulk updates](https://github.com/architrave-dev/backend/wiki/Change-update-system)
### MVP-3
- [Owner Verification as a Cross-Cutting Concern](https://github.com/architrave-dev/backend/wiki/Apply-AOP#ownercheck)
### MVP-2
- [AccessToken and RefreshToken Usage Flow](https://github.com/architrave-dev/backend/wiki/About-Auth#mvp-2)
- [Deployment Process Improvement Needed](https://github.com/architrave-dev/backend/wiki/About-Deploy#deployment-process-improvement-needed)


## Contact
| Position    | Name                       | Email                 |
|-------------|----------------------------|-----------------------|
| PM          | Kim Youngdong              | nfs82young@gmail.com  |
| Frontend    | Kim Youngdong              | nfs82young@gmail.com  |
| Backend     | Kim Youngdong              | nfs82young@gmail.com  |
| Design      | Jung Joohee                | jjh62128@gmail.com    |
| Design      | Choi Jihee                 | zhee4820@gmail.com    |
