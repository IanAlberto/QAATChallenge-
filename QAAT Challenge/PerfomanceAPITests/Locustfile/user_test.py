import time
from locust import HttpUser, HttpLocust, task, between
import json
class UserTest(HttpUser):
    # Define the time to wait after each task
    wait_time = between(1, 5)
    host = "http://localhost:8080/api/v3"
    # HttpUser.weight = 5

    @task
    def create_user(self):
        user_info = {
                "id": 101,
                "username": "IanL",
                "firstName": "Ian",
                "lastName": "Linz",
                "email": "alberto.l@gmail.com",
                "password": "123456",
                "phone": "8492525252",
                "userStatus": 1
               }
        
        param = {
            'Accept':'application/json',
            'Content-Type':'application/json'
        }

        with self.client.post("/user", data=json.dumps(user_info), headers=param, catch_response=True) as response:
            if response.elapsed.total_seconds() > 100:
                response.failure("Request took too long")

    @task
    def login(self):   
        username = 'IanL'
        password = '123456'
        with self.client.get("/user/login?username={username}&password={password}") as response:
            if response.elapsed.total_seconds() > 100:
                response.failure("Request took too long")
    

    @task
    def logout(self):
        with self.client.get("/user/logout") as response:
            if response.elapsed.total_seconds() > 100:
                response.failure("Request took too long")
