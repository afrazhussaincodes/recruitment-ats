# Week 9: Selenium Test Design and Local Execution

## 1. Objective
To implement and execute automated browser-based tests for the Recruitment ATS application using Selenium WebDriver, Java, JUnit 5, and Maven.

## 2. Testing Environment
- Application: Recruitment ATS
- Backend: Spring Boot
- Programming language: Java 17
- Build tool: Maven
- Automation framework: Selenium WebDriver
- Test framework: JUnit 5
- Browser: Google Chrome
- Local URL: http://localhost:8080

## 3. Test Cases

| Test ID | Test Scenario | Expected Result |
|---|---|---|
| UI-01 | Load the homepage | Correct page title and hero heading are displayed |
| UI-02 | Browse open jobs | Open-jobs section becomes visible |
| UI-03 | Enter a job-search query | Search field accepts the entered text |

## 4. Test Execution Results

- Total tests executed: 3
- Passed: 3
- Failed: 0
- Errors: 0
- Skipped: 0
- Maven build status: BUILD SUCCESS

## 5. Failure Handling
The Selenium test class attempts to capture a browser screenshot when a test fails. Screenshots are stored in `test-evidence/screenshots`.

## 6. Reporting
The Maven Surefire test report records the number of tests executed, failures, errors, and skipped tests. An HTML report can also be generated for presentation.

## 7. Conclusion
The three implemented Selenium UI smoke tests executed successfully. The results confirm that the homepage loads, the open-jobs section can be displayed, and the job-search field accepts input.

These tests do not yet validate the complete registration, authentication, job application submission, or recruiter workflows.
