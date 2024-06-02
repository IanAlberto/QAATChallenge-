import time
from locust import HttpUser, HttpLocust, task, between
import json

class UpdateOrderTest(HttpUser):
    wait_time = between(1, 5)
    host = "http://localhost:8080/api"

    @task(1)
    def list_pets(self):
        self.client.get("/v3/pet/findByStatus?status=available")

    @task(1)
    def get_pet(self):
        pet_id = 11  # Cambia esto al ID de una mascota específica
        self.client.get(f"/v3/pet/{pet_id}")

    @task(1)
    def add_pet(self):
        headers = {'Content-Type': 'application/json'}
        payload = {
            "id": 11,
            "name": "doggie",
            "category": {
                "id": 0,
                "name": "string"
            },
            "photoUrls": [
                "string"
            ],
            "tags": [
                {
                    "id": 0,
                    "name": "Dobber"
                }
            ],
            "status": "available"
        }
        self.client.post("/v3/pet", json=payload, headers=headers)

    @task(1)
    def delete_pet(self):
        pet_id = 11  # Cambia esto al ID de la mascota que agregaste en la tarea anterior
        self.client.delete(f"/v3/pet/{pet_id}")

    @task(2)
    def create_order(self):
        headers = {'Content-Type': 'application/json'}
        payload = {
            "id": 100,
            "petId": 11,
            "quantity": 1,
            "shipDate": "2024-06-02T08:44:59.147Z",
            "status": "placed",
            "complete": True
        }
        self.client.post("/v3/store/order", json=payload, headers=headers)
