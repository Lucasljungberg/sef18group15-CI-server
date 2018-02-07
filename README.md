# sef18group15-CI-server
Assignment 2 for sef18 course made by Group 15.

This is a small CI-server with some basic functionality. On each commit, it fetches the code from our GitHub repo, builds and tests it, and then notifies (via email or by setting the commit status on repository) the contributors about the built status.  Compilation is triggered as webhook, the CI server compiles the branch where the change has been made, as specified in the HTTP payload.



## Compilation         -- to be done
##### how compilation has been implemented and unit-tested.

## Notifications

### Email
Email notifications are sent to all the group members (from a dummy email address) after each commit regarding the status of the commit (corresponding build).

The sendEmail function is implemented in the SendEmail.java file and tested in the SendEmailTest.java file. Gmail is used as the mail service, and the dummy user (from whose address the emails are sent) is authorised using their Gmail email address and password. In the test file, a test email is being sent to Olzhas' email address.



### Commit status               -- to be done



## Instructions.
1. Folders are structured in a format suitable for Maven. All the tests are located in the test folder, and all the src files in the main folder. Compilation and testing are then done by executing the following commands, while being in the project directory: "mvn compile" and "mvn test".
2. All the dependencis are listed in the "pom.xml" file.
3. JUnit is used as the testing framework.
4.
