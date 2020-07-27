#Curl-запросы для тестирования MealRestController:

####1. Получение всей еды:</a>
`curl http://localhost:8080/topjava/rest/meals`
####2. Получение еды по фильтру:
`curl 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30&startTime=08:00&endTime=20:00'`
####3. Получение еды по id:
`curl http://localhost:8080/topjava/rest/meals/100002`
####4. Добавление новой еды:
`curl -d '{"dateTime":"2020-02-01T18:00:00","description":"Созданный ужин","calories":300}' -H "Content-Type: application/json" -X POST http://localhost:8080/topjava/rest/meals`
####5. Редактирование существующей еды по её id:
`curl -X PUT -H "Content-Type: application/json" -d '{"id":100002,"dateTime":"2020-01-30T10:00:00","description":"Обновленный завтрак","calories":200}' 'http://localhost:8080/topjava/rest/meals/100002'`
####6. Удаление еды по id:
`curl -X DELETE http://localhost:8080/topjava/rest/meals/100002` 
