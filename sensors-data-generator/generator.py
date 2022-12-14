import json
import random
import time

import paho.mqtt.client as mqtt


def get_base_sensor_data():
    data = dict()
    current_time = round(time.time() * 1000)
    data["id"] = current_time
    data["timestamp"] = current_time
    data["tenantId"] = "001"
    data["token"] = "123"
    return data


def get_weather_data():
    data = get_base_sensor_data()
    data["airTemperature"] = "12.34"
    return data


def get_water_quality_data():
    data = get_base_sensor_data()
    data["waterTemperature"] = "21.44"
    return data


def get_air_quality_data():
    data = get_base_sensor_data()
    data["dust"] = "0.56"
    return data


def publish_message(topic: str, data_supplier):
    data = data_supplier()

    data_out = json.dumps(data)

    print("Publish Topic: ", topic, "Data: ", data_out)
    result = client.publish(topic, data_out, 0)
    print(result)


broker = "localhost"
port = 1883
username = "myuser"
password = "mypassword"

client = mqtt.Client()
client.username_pw_set(username, password)
client.connect(broker, port)

iterations_count = random.randint(1, 2)
for i in range(iterations_count):
    sleep_duration = random.randint(0, 1)

    publish_message("building/floor_1/bedroom/water_quality", get_water_quality_data)
    publish_message("building/weather", get_weather_data)
    publish_message("building/001/air_quality", get_air_quality_data)

    time.sleep(sleep_duration)
    client.loop()
client.disconnect()
