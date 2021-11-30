# Get all meals
curl -v http://localhost:8000/topjava/rest/meals
# Get one meal
curl -v http://localhost:8000/topjava/rest/meals/100007
# Delete meal
curl -v -X DELETE http://localhost:8000/topjava/rest/meals/100008
# Get meals filtered by date/time 
curl -v "http://localhost:8000/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30&startTime=00%3A00&endTime=20%3A00"
# Create new meal
curl -v -X POST -H "Content-Type: application/json; charset=UTF-8" -d {"dateTime":"2020-02-01T18:00:00","description":"Созданный ужин","calories":300} http://localhost:8000/topjava/rest/meals
## or
curl -v -X POST -H "Content-Type: application/json; charset=UTF-8" --data-binary "@create_data.txt" http://localhost:8000/topjava/rest/meals
### content of create_data.txt
#### {"dateTime":"2020-02-01T18:00:00","description":"Созданный ужин","calories":300}

# Update meal
curl -v -X PUT -H "Content-Type: application/json; charset=UTF-8" -d {"id":100002,"dateTime":"2020-01-30T10:02:00","description":"Обновленный завтрак","calories":500} http://localhost:8000/topjava/rest/meals/100002
## or
curl -v -X PUT -H "Content-Type: application/json; charset=UTF-8" --data-binary "@update_data.txt" http://localhost:8000/topjava/rest/meals/100002
### content of update_data.txt
#### {"id":100002,"dateTime":"2020-01-30T10:02:00","description":"Обновленный завтрак","calories":500}

