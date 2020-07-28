# Curl-queries for testing MealRestController:

#### 1. Get all meals:
`curl http://localhost:8080/topjava/rest/meals`
#### 2. Get meals by filter:
`curl 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30&startTime=08:00&endTime=20:00'`
#### 3. Get meal by id:
`curl http://localhost:8080/topjava/rest/meals/100002`
#### 4. Add new meal:
`curl -d '{"dateTime":"2020-02-01T18:00:00","description":"Созданный ужин","calories":300}' -H "Content-Type: application/json" -X POST http://localhost:8080/topjava/rest/meals`
#### 5. Edit existing meal by id:
`curl -X PUT -H "Content-Type: application/json" -d '{"id":100002,"dateTime":"2020-01-30T10:00:00","description":"Обновленный завтрак","calories":200}' 'http://localhost:8080/topjava/rest/meals/100002'`
#### 6. Remove existing meal by id:
`curl -X DELETE http://localhost:8080/topjava/rest/meals/100002` 
