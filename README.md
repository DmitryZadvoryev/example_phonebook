# example_phonebook

Это пример приложения Java 8 / Gradle / PostgreSQL / Spring Boot / JUnit

### Требования:

- Java Platform (JDK) 8,
- PostgreSQL
- JUnit 5
- Gradle 6.0.1

### База данных
Созадать и сконфигурировать базу данных, `url, username, password` можно изменить в `application.properties`.

### Запуск
`git clone https://github.com/DmitryZadvoryev/example_phonebook.git`

`./gradlew build`

`./gradlew bootRun or java -jar build/libs/PhonebookApplication.jar`

http://localhost:8080/

### Запуск теста
`./gradlew test`
