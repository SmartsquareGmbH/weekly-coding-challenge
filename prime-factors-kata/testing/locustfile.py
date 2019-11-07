from locust import TaskSet, task
from locust.contrib.fasthttp import FastHttpLocust
import random
import os
import json

EXPECTED_OUTPUTS = {
    256: [2, 2, 2, 2, 2, 2, 2, 2],
    4294967295: [3, 5, 17, 257, 65537],
    2147483649: [3, 715827883],
    4211: [4211],
    4513: [4513],
    5323: [5323],
    5807: [5807],
    6247: [6247],
    9923: [9923],
}

class StressTest(TaskSet):
    def on_start(self):
        self.n = random.choice(EXPECTED_OUTPUTS.keys())
        self.expected_response = EXPECTED_OUTPUTS[self.n]

    @task
    def fetch_random_prime_factor(self):
        response=self.client.get("/generate/%i" % self.n)
        assert response.status_code is 200, "Unexpected response code: %i" % response.status_code
        assert json.loads(response.text) == self.expected_response

class Instructor(FastHttpLocust):
    task_set=StressTest
