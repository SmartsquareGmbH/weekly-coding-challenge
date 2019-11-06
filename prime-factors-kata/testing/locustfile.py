from locust import HttpLocust, TaskSet, task
import random
import os

class StressTest(TaskSet):
    def on_start(self):
        self.n = str(random.choice([4211, 4513, 5323, 5807, 6247, 6247, 9923]))

    @task
    def fetch_random_prime_factor(self):
        self.client.get("/generate/" + self.n)

class Instructor(HttpLocust):
    task_set = StressTest
