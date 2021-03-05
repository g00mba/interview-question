Backbase Interview question Solution by Mauricio Batlle
==================
![last commit](https://img.shields.io/github/last-commit/g00mba/interview-question?style=plastic)


This is a very basic spring-boot app. Run it (using `mvn spring-boot:run`) or your favorite IDE.

the template was used to develop a course registration system.

# Requirements
A course entity has a `title`, `startDate`, `endDate`, `capacity`, and `remainingPlaces`. this code 
implements an api that handles the creation courses as well as the creation and deletion of users.
the API was developed according to all the requirements requested


### Create course (POST http://localhost:5000/courses) STATUS: COMPLETED
with body
```json
{
  "title": "Course title",
  "startDate": "2021-05-01",
  "endDate": "2021-05-05",
  "capacity": 10
}
```
Response should be 201:
```json
{
  "id": 1,
  "title": "Course title",
  "startDate": "2021-05-01",
  "endDate": "2021-05-05",
  "capacity": 10,
  "remaining": 10
}
```


### Search course by title (GET http://localhost:5000/courses?q=title) STATUS: COMPLETED
Response should be 200 with body :
```json
[
  {
  "id": 1,
  "title": "Course title",
  "startDate": "2021-05-01",
  "endDate": "2021-05-05",
  "capacity": 10,
  "remaining": 10
  },
  ...
]
```


### Get course details (GET http://localhost:5000/courses/1) STATUS: COMPLETED
Response should be 200 with body :
```json
  {
  "id": 1,
  "title": "Course title",
  "startDate": "2021-05-01",
  "endDate": "2021-05-05",
  "capacity": 10,
  "remaining": 5,
  "participants":[
    {"name":"Daniel", "registrationDate":"2021-05-01"},
    ...
  ]
  },
```


### Sign up user for course (POST http://localhost:5000/courses/1/add) STATUS: COMPLETED
Body should be user details:
```json
{
  "registrationDate": "2021-04-01",
  "name": "Daniel"
}
```
NOTE: as the courseId can be inferred from the path, there was no need to include the courseId in the body.

Response should be: 
* 200 if registration was successful, and a response body similar to get course details request.
* 400 if `name` already enrolled to the course.
* 400 if `registrationDate` is 3 days before course `startDate` or after.
* 400 if course is full.
* 404 if course does not exist.


### Cancel user enrollment (POST http://localhost:5000/courses/1/remove) STATUS: COMPLETED
Body should be user details:
```json
{
  "cancelDate": "2021-05-01",
  "name": "Daniel"
}
```
NOTE: as the courseId can be inferred from the path, there was no need to include the courseId in the body.

Response should be: 
* 200 if cancellation was successful, and a response body similar to get course details request.
* 404 if course does not exist or user is not enrolled to course.
* 400 if `cancelDate` is 3 days before course `startDate` or after.


## Guidelines (all guidelines completed successfully)
* Fork this repository and push your commits
* Use the spring-boot template given
* Write javadocs on classes and methods
* Write unit-tests and/or integration-tests (Higher coverage is better)
* All classes given are meant to used as reference - once they are not needed, they can be removed.
* This project uses [lombok](https://projectlombok.org/) - use it when possible
* Properly organize your project with `.gitignore` file, `readme` file explaining how to run the project, etc.

