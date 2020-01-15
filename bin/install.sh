echo 'Decompress app zip on temp dir'
sudo unzip mqtt-logger-0.0.1-SNAPSHOT.zip -d temp
echo 'Make dir to store app configuration'
sudo mkdir /etc/mqtt-logger
sudo chown openhabian /etc/mqtt-logger
chmod 744 /etc/mqtt-logger/
echo 'Move configuration file to location and set permissions'
mv temp/conf/core-conf-linux.properties /etc/mqtt-logger/mqtt-logger.conf
rm -q temp/conf
chmod 744 /etc/mqtt-logger/mqtt-logger.conf
echo 'Make dir to store logs'
sudo mkdir /var/mqtt-logger
sudo chown openhabian /var/mqtt-logger
chmod 744 /var/mqtt-logger
echo 'Configure app as a service to be launched on startup'
rm -q /etc/init.d/mqtt-logger
mv temp/mqtt-logger.service /etc/init.d/mqtt-logger
sudo chmod +x /etc/init.d/mqtt-logger
sudo update-rc.d mqtt-logger remove
sudo update-rc.d mqtt-logger defaults
echo 'Make dir to store app files'
sudo mkdir /opt/mqtt-logger
echo 'Move app files to destination'
mv temp/*.* /opt/mqtt-logger/
echo 'Remove temp dir'
rm -q temp