from locust import HttpUser, task, between

class OrderWithPetsTest(HttpUser):
    wait_time = between(1, 5)
    host = "http://localhost:8080/api"

    @task(1)
    def list_pets(self):
        self.client.get("/v3/pet/findByStatus?status=available")

    @task(1)
    def get_pet(self):
        pet_id = 11
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
        pet_id = 11
        self.client.delete(f"/v3/pet/{pet_id}")

    @task(2)
    def find_order_by_id(self):
        order_id = 1
        self.client.get(f"/v3/store/order/{order_id}")
