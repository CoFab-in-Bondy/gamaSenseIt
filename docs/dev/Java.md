# Install JDK 17

## Windows

You can use the [Installer](https://www.oracle.com/java/technologies/downloads/#jdk17-windows) for install jdk 17 on Windows. 

## Linux

Get installer for jdk 17.
```
sudo add-apt-repository ppa:linuxuprising/java
sudo apt -y update && sudo apt -y upgrade
sudo apt install oracle-java17-installer
```

Change the default java used.
```
sudo update-alternatives --config java
```