FROM python:2.7-stretch 

RUN pip install locustio pyzmq && \
    mkdir /locust

WORKDIR /locust

ADD requirements.txt requirements.txt

RUN pip install -r requirements.txt

ADD locustfile.py locustfile.py

ENTRYPOINT [ "locust", "--no-web"]
