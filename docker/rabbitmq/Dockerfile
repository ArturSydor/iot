#FROM rabbitmq:3.8-management
#
#RUN rabbitmq-plugins enable --offline rabbitmq_mqtt
#
#EXPOSE 15672 1883

FROM rabbitmq

RUN rabbitmq-plugins enable --offline rabbitmq_management
RUN rabbitmq-plugins enable --offline rabbitmq_mqtt

EXPOSE 15672 1883
