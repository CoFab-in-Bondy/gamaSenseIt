# Install JDK 17

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