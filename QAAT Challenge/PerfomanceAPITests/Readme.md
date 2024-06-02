# PetStoreProject

This project is the web test automation scripts for the pet store API

Programing language: Java
Framework: locust

## Dependencies

* python
* locust


## Installation

To get started with the project,

Install pip, download and complete the installation and
restart the machine

In the console install locust

```console
pip install locust
```

## Test and Deploy

To run all the tcs:

```console
locust -f .\Locustfile\{filename} --headless -u {num of users}    
```

# Report

Executive Summary:
The objective of this performance testing is to evaluate the behavior and responsiveness of the APIs exposed by Petstore's Swagger series under different load conditions. I evaluate the ability of the API handling CRUD operations, response time under different load levels. 

Methodology:

I designed and executed using the locust tool a series of tests. The tests are focused o critical areas as CRUD, scalability, responses times.

CRUD operations:

* Tests on CRUD operations were performed on the pets, orders and user operations.

* The create, update and delete operations have shown acceptable response times.
  However the read may fail depending on the number of requests made at the same time.

User loading:

* Different levels of user load were simulated to evaluate the ability of the APIs to handle large numbers of simultaneous requests.
* Some spikes in response times were observed under extremely high user loads, but overall, the APIs maintained acceptable performance.


Response Times:

* API response times were monitored under different loading conditions to ensure that they met the defined response time requirements.

Conclusions:

* Petstore's Swagger series APIs showed good overall performance under different load conditions. 
* CRUD operations, user load and response times remained within acceptable limits in most cases.
* Some areas of potential improvement were identified in terms of scalability and response times, which could be addressed by additional optimizations in the API implementation. For example Get functions
* Other things to work is relates to the status code in the Post methods 
