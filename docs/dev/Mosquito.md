# Mosquito

Mosquito is a broker often used with raspberry pi and arduino, it allows to redistribute the data it receives to several clients.

## Installation

Install for Ubuntu.
```sh
sudo apt install mosquitto
sudo apt install mosquitto-clients
```

## Ports

Open port for mosquitto (Optionnal)
```sh
iptables -t filter -A INPUT -p tcp --dport 1883 -j LOGACCEPT
```

## Authentication.
* Make a password file with user gamasenseit.
   ```sh
   sudo mosquitto_passwd -c /etc/mosquitto/passwd gamasenseit
   ```
* Create a configuration file for gamesenseit.
   ```sh
   sudo cp /etc/mosquitto/mosquitto.conf /etc/mosquitto/gamasenseit.conf
   sudo nano /etc/mosquitto/gamasenseit.conf
   ```
* Uncomment the lines below / put the values there.
    * `listener 1883` for set the port on 1883
    * `password_file /etc/mosquitto/passswd` : path to passwords file.
    * `allow_anonymous false` allow only authenticated users.
* Assign configuration to mosquitto.
   ```sh
   mosquitto -v -c '/etc/mosquitto/gamasenseit.conf'
   ```

## Commands
For subscribe and publish (_see more with `man mosquitto_sub` and `man mosquitto_pub`_).
```sh
mosquitto_sub [-h host] [-u user] [-P password] [-t topic]
mosquitto_pub [-h host] [-u user] [-P password] [-t topic] -m [message]
```