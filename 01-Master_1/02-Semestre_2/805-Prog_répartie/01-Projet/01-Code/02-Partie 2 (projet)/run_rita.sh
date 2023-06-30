cd RitaCouvoge
sudo /opt/tomcat9/bin/shutdown.sh
sudo rm -r /opt/tomcat9/webapps/RitaCou*
mvn clean package
cp target/RitaCouvoge.war /opt/tomcat9/webapps/RitaCouvoge.war 
sudo /opt/tomcat9/bin/startup.sh
