# Recruitment Applicant Tracking System (ATS)

End-to-end Recruitment ATS built with Spring Boot, Maven, MySQL and a responsive browser UI.

## Workflow

Candidate Portal → Browse Jobs → Register/Login → Profile/Resume → Apply → Admin/Recruiter sees application → Screening → Shortlisted → Interview → Selected/Rejected → Candidate sees status.

## Features

### Candidate
- Public job search and job details
- Candidate registration/login
- Profile management
- Resume upload
- Job application with duplicate protection
- My Applications
- Application status timeline
- Interview visibility

### Admin / Recruiter
- Dashboard with live database metrics
- Candidate management
- Job creation/editing/open/close
- Application review
- Status updates with immutable history
- Interview scheduling
- Alerts/exceptions
- Candidate/application search

## Stack
- Java 17
- Spring Boot 4.1.1
- Spring Data JPA / Hibernate
- MySQL 8
- Maven
- Bootstrap 5
- Vanilla JavaScript
- Jenkins

## Database

Existing tables are preserved. The application adds `user_accounts` and `interviews` automatically with `ddl-auto=update`.

Existing core tables:
- candidates
- jobs
- applications
- recruiters
- application_status_history
- application_exceptions

## Configuration

Set the database password before starting:

```powershell
$env:DB_PASSWORD="your_password"
```

Optional:
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/recruitment_ats"
$env:DB_USERNAME="ats_user"
$env:APP_PORT="8080"
```

## Run locally

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

Open `http://localhost:8080`.

Health:
`http://localhost:8080/actuator/health`

## Demo accounts

Created automatically if they do not already exist:

- Admin: `admin@ats.local` / `Admin@123`
- Recruiter: `recruiter@ats.local` / `Recruiter@123`

Candidate accounts are created from the Register screen.

These are local demonstration credentials only; replace them for production.

## Resume storage

Uploaded resumes are stored under `uploads/resumes/`. Do not commit that directory to Git.

## DevOps

The repository includes:
- `.gitignore`
- GitHub issue templates
- branch policy
- Jenkinsfile
- Maven wrapper

Jenkins pipeline stages:
Checkout → Build & Test → Package → Archive Artifact → Deploy → Deployment Evidence.

Never commit database passwords or other secrets.

## Git Workflow
- main: release-ready branch
- development: integration branch
- feature/<short-description>: feature development
- fix/<short-description>: bug fixes
- hotfix/<short-description>: urgent fixes
