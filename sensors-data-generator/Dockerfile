FROM python:3.8

WORKDIR /usr/src/app

RUN pip install paho-mqtt

COPY . .

CMD [ "python", "./generator.py" ]