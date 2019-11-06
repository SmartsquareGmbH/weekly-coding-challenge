FROM python:2.7-stretch 

RUN pip install locustio pyzmq && \
    mkdir /locust

WORKDIR /locust

ADD locustfile.py locustfile.py
ADD requirements.txt requirements.txt

RUN pip install -r requirements.txt

EXPOSE 8089 5557 5558
ENTRYPOINT [ "locust", "--host=http://host.docker.internal:8080"]
