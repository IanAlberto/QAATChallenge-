from locust import HttpUser, task, between

class PetStoreTest(HttpUser):
    wait_time = between(1, 3)
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
                        "id": 1,
                        "name": "Dogs"
                    },
                    "photoUrls": [
                        "https://th.bing.com/th/id/R.6f5ff4a55984b199b086d382c24543df?rik=QTzOlhysHImPEQ&riu=http%3a%2f%2fwww.infoescola.com%2fwp-content%2fuploads%2f2010%2f08%2fdoberman_223996249.jpg&ehk=%2fDkftpHXIILaR8lEJ87ikNsWcPsVqngYYb1Ivb1CoF8%3d&risl=&pid=ImgRaw&r=0"
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
    def update_pet(self):
        pet_id = 11
        headers = {'Content-Type': 'application/json'}
        payload = {
                    "id": pet_id,
                    "name": "doggie",
                    "status": "sold"
                  }
        self.client.put(f"/v3/pet", json=payload, headers=headers)

    @task(2)
    def delete_pet(self):
        pet_id = 11
        self.client.delete(f"/v3/pet/{pet_id}")

    @task(3)
    def find_pet_by_status(self):
        status = "available"
        self.client.get(f"/v3/pet/findByStatus?status={status}")

    @task(3)
    def find_pet_by_tag(self):
        tag = "Dobber"
        self.client.get(f"/v3/pet/findByTags?tags={tag}")

    @task(3)
    def get_store_inventory(self):
        self.client.get("/v3/store/inventory")