import sys
import time
import random
from Adafruit_IO import MQTTClient
from uart import *
#from simpleAI import *

AIO_FEED_IDs = ["button1","button2","sensor1","sensor2","sensor3","ai","Ack","door","motion"]
AIO_USERNAME = "benzdht"
AIO_KEY = ""

def connected(client):
    print("Connected ...")
    for topic in AIO_FEED_IDs:
        client.subscribe(topic)
    client.publish("Ack","0")


def subscribe(client , userdata , mid , granted_qos):
    print("Subscribe successfully ...")

def disconnected(client):
    print("Disconnected ...")
    client.publish("Ack","2")
    sys.exit(1)

def message(client , feed_id , payload):
    print("Receive data: " + payload, feed_id)
    if feed_id == "Ack":
        if payload == "0.5":
            client.publish("Ack","0")
    if feed_id == "button1":
        client.publish("Ack","1")
        if payload == '1':
            writeData("Light Bulb is turned On\n")
        elif payload == '0':
            writeData("Light Bulb is turned OFF\n")
    if feed_id == 'button2':
        client.publish("Ack","1")
        if payload == '1':
            writeData("Fan is turned ON\n")
        elif payload == '0':
            writeData("Fan is turned OFF\n")
    if feed_id == 'door':
        client.publish("Ack","1")
        if payload == '1':
            writeData("Door is LOCKED\n")
        elif payload == '0':
            writeData("Door is UNLOCKED\n")



        

client = MQTTClient(AIO_USERNAME , AIO_KEY)
client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe
client.connect()
client.loop_background()
try:
    while True:
        readSerial(client)
     # Listen to the keyboard for presses.
except KeyboardInterrupt:
    client.publish("Ack","2")
    time.sleep(2)