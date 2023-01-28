import decimal
import json
import os
import random
import time
from enum import Enum

import paho.mqtt.client as mqtt

one_year_in_milliseconds = 31_556_952_000


class RunMode(Enum):
    STREAM = "STREAM"
    ITERATION = "ITERATION"


def random_decimal_string(min_value: int, max_value: int):
    return str(decimal.Decimal(random.randrange(min_value * 100, max_value * 100)) / 100)


def random_time_from_last_year():
    current_time = round(time.time() * 1000)
    return random.randrange(current_time - one_year_in_milliseconds, current_time)


def get_base_sensor_data():
    data = dict()
    current_time = random_time_from_last_year()
    data["id"] = current_time
    data["timestamp"] = current_time
    data["tenantId"] = "001"
    data["token"] = "123"
    return data


def get_weather_data():
    data = get_base_sensor_data()
    data["airTemperature"] = random_decimal_string(0, 50)
    data["wetBulbTemperature"] = random_decimal_string(0, 50)
    data["humidity"] = random_decimal_string(0, 100)
    data["humidity"] = random_decimal_string(0, 10)
    data["rainIntensity"] = random_decimal_string(0, 200)
    data["intervalRain"] = random_decimal_string(0, 50)
    data["totalRain"] = random_decimal_string(0, 1000)
    data["windDirection"] = random_decimal_string(0, 300)
    data["windSpeed"] = random_decimal_string(0, 15)
    data["maxWindSpeed"] = random_decimal_string(15, 20)
    data["barometricPressure"] = random_decimal_string(700, 750)
    data["barometricPressure"] = random_decimal_string(0, 1500)
    return data


def get_water_quality_data():
    data = get_base_sensor_data()
    data["waterTemperature"] = random_decimal_string(0, 35)
    data["turbidity"] = random_decimal_string(0, 10)
    data["transducerDepth"] = random_decimal_string(0, 3)
    data["waveHeight"] = random_decimal_string(0, 25)
    data["wavePeriod"] = random.randint(0, 10)
    return data


def get_air_quality_data():
    data = get_base_sensor_data()
    data["n2o"] = random_decimal_string(0, 75)
    data["o3"] = random_decimal_string(0, 180)
    data["co2"] = random_decimal_string(0, 120)
    data["so2"] = random_decimal_string(0, 8)
    data["dust"] = random_decimal_string(0, 45)
    return data


def publish_message(client, topic: str, data_supplier):
    data = data_supplier()

    data_out = json.dumps(data)

    print("Publish Topic: ", topic, "Data: ", data_out)
    result = client.publish(topic, data_out, 0)
    print(result)


def create_client():
    broker = os.getenv("BROKER_HOST")
    port: int = int(os.getenv("BROKER_PORT"))
    username = os.getenv("BROKER_USER")
    password = os.getenv("BROKER_PASSWORD")

    client = mqtt.Client()
    client.username_pw_set(username, password)

    is_connected = False
    while not is_connected:
        try:
            client.connect(broker, port)
            is_connected = True
        except Exception:
            print("MQTT broker is not available yet")
            time.sleep(1)
    return client


def generate_sensor_data():
    run_mode: RunMode = RunMode(os.getenv("RUN_MODE"))
    number_of_iterations: int = int(os.getenv("NUMBER_OF_ITERATIONS"))
    water_quality_topic = os.getenv("WATER_QUALITY_TOPIC")
    weather_topic = os.getenv("WEATHER_TOPIC")
    air_quality_topic = os.getenv("AIR_QUALITY_TOPIC")

    client = create_client()

    if run_mode == RunMode.STREAM:
        while True:
            publish_message(client, water_quality_topic, get_water_quality_data)
            time.sleep(1)

            publish_message(client, weather_topic, get_weather_data)
            time.sleep(1)

            publish_message(client, air_quality_topic, get_air_quality_data)
            time.sleep(1)

            client.loop()
    elif run_mode == RunMode.ITERATION:
        for i in range(number_of_iterations):
            publish_message(client, water_quality_topic, get_water_quality_data)
            publish_message(client, weather_topic, get_weather_data)
            publish_message(client, air_quality_topic, get_air_quality_data)

            time.sleep(1)
            client.loop()
    else:
        print("Not existing run mode: " + run_mode.__str__())
    client.disconnect()


generate_sensor_data()

# broker = os.getenv("BROKER_HOST")
# port: int = int(os.getenv("BROKER_PORT"))
# username = os.getenv("BROKER_USER")
# password = os.getenv("BROKER_PASSWORD")
# run_mode: RunMode = RunMode(os.getenv("RUN_MODE"))
# number_of_iterations: int = int(os.getenv("NUMBER_OF_ITERATIONS"))
# water_quality_topic = os.getenv("WATER_QUALITY_TOPIC")
# weather_topic = os.getenv("WEATHER_TOPIC")
# air_quality_topic = os.getenv("AIR_QUALITY_TOPIC")
#
# client = mqtt.Client()
# client.username_pw_set(username, password)
# is_connected = False
# while not is_connected:
#     try:
#         client.connect(broker, port)
#         is_connected = True
#     except Exception:
#         print("MQTT broker is not available yet")
#         time.sleep(1)
#
# iterations_count = random.randint(1, 10_000)
# for i in range(iterations_count):
#     sleep_duration = random.randint(0, 1)
#
#     publish_message("building/floor_1/bedroom/water_quality", get_water_quality_data)
#     publish_message("building/weather", get_weather_data)
#     publish_message("building/001/air_quality", get_air_quality_data)
#
#     time.sleep(sleep_duration)
#     client.loop()
# client.disconnect()
