import json
import random
import time

import paho.mqtt.client as mqtt

broker = "localhost"
port = 1883
topic = "building/sensor"
username = "myuser"
password = "mypassword"

client = mqtt.Client()
client.username_pw_set(username, password)
client.connect(broker, port)

data = dict()

iterations_count = random.randint(0, 20)
for i in range(iterations_count):
    sleep_duration = random.randint(0, 1)
    current_time = round(time.time() * 1000)
    data["id"] = current_time
    data["timestamp"] = current_time
    data["tenantId"] = "001"
    data["token"] = "123"

    data_out = json.dumps(data)

    print("Publish Topic: ", topic, "Data: ", data_out)
    result = client.publish(topic, data_out, 0)
    time.sleep(sleep_duration)
    client.loop()
client.disconnect()
