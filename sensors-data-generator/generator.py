import json
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

current_time = time.time()
for i in range(3):
    data["id"] = current_time
    data["timestamp"] = current_time
    data["tenantId"] = "001"
    data["token"] = "123"

    data_out = json.dumps(data)

    print("Publish Topic: ", topic, "Data: ", data_out)
    result = client.publish(topic, data_out, 0)
    time.sleep(1)
    client.loop()
client.disconnect()
