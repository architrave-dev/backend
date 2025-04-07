# Architrave Backend Repository.

Seamless Portfolio / Archive for Artist

Architrave is a portfolio web service that empowers contemporary artists\
to directly manage their projects and works.

## Now Status
MVP-3 released.

Contact us for a test Artist ID

## Key Features
### MVP-3
- Separate logic for verifying website owner with AOP.  
- Implement CI/CD pipelines through GitHub Actions.  
- Domain settings with AWS Route53.  
- Use HTTPS with CloudFront(FE) and ALB(BE).  
- Establish a system of prefix for image files in S3.  

### APIs
- [Swagger API](https://api.architrive.com/swagger-ui/index.html)
  
## Tech Stack
- Java with Spring Boot
- Spring Data JPA, AWS RDS: PostgreSQL for DB
- Spring Security, JWT for auth
- AWS EC2, Docker for deploy

## Settings
### ERD
![arch_mvp-3 ERD](https://github.com/user-attachments/assets/6f400435-4201-466f-b759-8cdaf7dcba67)



### Architecture
![arch_mvp-3_Architecture](https://github.com/user-attachments/assets/0a7fa600-405e-46ee-bdd6-fbb610897052)



## Considerations 
### About AOP
[Owner Verification as a Cross-Cutting Concern](https://github.com/architrave-dev/backend/wiki/Apply-AOP#ownercheck)
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
