import datetime
import decimal
import json
import os
import random
import time
from enum import Enum

import paho.mqtt.client as mqtt


class RunMode(Enum):
    STREAM = "STREAM"
    ITERATION = "ITERATION"
    RANGE = "RANGE"


one_year_in_milliseconds = 31_556_952_000
latitude = random.randrange(-90, 90)
longitude = random.randrange(-180, 180)
run_mode: RunMode = RunMode(os.getenv("RUN_MODE"))
tenant = os.getenv("TENANT")
sensor_token = os.getenv("SENSOR_TOKEN")


def random_decimal_string(min_value: int, max_value: int):
    return str(decimal.Decimal(random.randrange(min_value * 100, max_value * 100)) / 100)


def get_current_time():
    return round(time.time() * 1000)


def random_time_from_last_year():
    current_time = get_current_time()
    return random.randrange(current_time - one_year_in_milliseconds, current_time)


def get_season_dependent_data(timestamp, winter_supplier, spring_supplier, summer_supplier, autumn_supplier):
    dt = datetime.datetime.fromtimestamp(timestamp / 1000)
    month = dt.month
    if 3 <= month <= 5:
        return spring_supplier()
    elif 6 <= month <= 8:
        return summer_supplier()
    elif 9 <= month <= 11:
        return autumn_supplier()
    else:
        return winter_supplier()


def get_temperature(timestamp):
    return get_season_dependent_data(
        timestamp,
        lambda: random_decimal_string(-10, 5),
        lambda: random_decimal_string(0, 15),
        lambda: random_decimal_string(10, 30),
        lambda: random_decimal_string(10, 20))


def get_humidity(timestamp):
    return get_season_dependent_data(
        timestamp,
        lambda: random_decimal_string(20, 50),
        lambda: random_decimal_string(60, 100),
        lambda: random_decimal_string(40, 70),
        lambda: random_decimal_string(60, 90))


def get_base_sensor_data(timestamp):
    data = dict()
    if run_mode == RunMode.STREAM:
        timestamp = get_current_time()
    elif run_mode == RunMode.ITERATION:
        timestamp = random_time_from_last_year()
    data["id"] = timestamp
    data["timestamp"] = timestamp
    data["tenantId"] = tenant
    data["token"] = sensor_token
    data["location"] = dict()
    data["location"]["lat"] = latitude
    data["location"]["lon"] = longitude
    return data


def get_weather_data(timestamp):
    data = get_base_sensor_data(timestamp)
    data["airTemperature"] = get_temperature(timestamp)
    data["wetBulbTemperature"] = get_temperature(timestamp)
    data["humidity"] = get_humidity(timestamp)
    data["rainIntensity"] = random_decimal_string(0, 200)
    data["intervalRain"] = random_decimal_string(0, 50)
    data["totalRain"] = get_season_dependent_data(
        timestamp,
        lambda: random_decimal_string(50, 300),
        lambda: random_decimal_string(300, 700),
        lambda: random_decimal_string(500, 1000),
        lambda: random_decimal_string(300, 500))
    data["windDirection"] = random_decimal_string(0, 300)
    data["windSpeed"] = get_season_dependent_data(
        timestamp,
        lambda: random_decimal_string(15, 20),
        lambda: random_decimal_string(0, 5),
        lambda: random_decimal_string(10, 15),
        lambda: random_decimal_string(5, 10))
    data["maxWindSpeed"] = random_decimal_string(15, 20)
    data["barometricPressure"] = random_decimal_string(700, 750)
    return data


def get_water_quality_data(timestamp):
    data = get_base_sensor_data(timestamp)
    data["waterTemperature"] = get_temperature(timestamp)
    data["turbidity"] = random_decimal_string(0, 10)
    data["transducerDepth"] = random_decimal_string(0, 3)
    data["waveHeight"] = get_season_dependent_data(
        timestamp,
        lambda: random_decimal_string(20, 30),
        lambda: random_decimal_string(10, 15),
        lambda: random_decimal_string(0, 5),
        lambda: random_decimal_string(5, 10))
    data["wavePeriod"] = random.randint(0, 10)
    return data


def get_air_quality_data(timestamp):
    data = get_base_sensor_data(timestamp)
    data["n2o"] = random_decimal_string(0, 75)
    data["o3"] = random_decimal_string(0, 180)
    data["co2"] = get_season_dependent_data(
        timestamp,
        lambda: random_decimal_string(50, 120),
        lambda: random_decimal_string(40, 75),
        lambda: random_decimal_string(20, 50),
        lambda: random_decimal_string(65, 100))
    data["so2"] = random_decimal_string(0, 8)
    data["dust"] = get_season_dependent_data(
        timestamp,
        lambda: random_decimal_string(5, 10),
        lambda: random_decimal_string(5, 15),
        lambda: random_decimal_string(15, 45),
        lambda: random_decimal_string(5, 20))
    return data


def publish_message(client, topic: str, data_supplier, timestamp=0):
    data = data_supplier(timestamp)

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
    number_of_iterations: int = int(os.getenv("NUMBER_OF_ITERATIONS"))
    water_quality_topic = os.getenv("WATER_QUALITY_TOPIC")
    weather_topic = os.getenv("WEATHER_TOPIC")
    air_quality_topic = os.getenv("AIR_QUALITY_TOPIC")

    client = create_client()

    time.sleep(30)
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
            client.loop()
    elif run_mode == RunMode.RANGE:
        end_time = datetime.datetime.now()
        start_time = end_time
        start_time -= datetime.timedelta(weeks=104)
        current_time = start_time
        while current_time <= end_time:
            current_time += datetime.timedelta(minutes=5)
            timestamp = int(current_time.timestamp() * 1000)
            publish_message(client, water_quality_topic, get_water_quality_data, timestamp)
            publish_message(client, weather_topic, get_weather_data, timestamp)
            publish_message(client, air_quality_topic, get_air_quality_data, timestamp)
            client.loop()
    else:
        print("Not existing run mode: " + run_mode.__str__())
    client.disconnect()


generate_sensor_data()
