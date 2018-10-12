**Applikation för enkel vy av status för tjänster i Nagios**

**Usage**

Property `nagios.monitored.services.filter` styr vilka tjänster som ska hämtas från Nagios Core 3.x.
Värdet kan sättas till en substräng av tjänstenamn definierade i Nagios, eller en 
explicit lista av tjänster som man är intresserad av. 

**För att köra i docker**
1. mvn package

2. cd docker

3. docker build -t simple-monitoring:latest .

4. docker run -p 8080:8080 -e spring.profiles.active='mdh-dev' --name simple-monitoring-container simple-monitoring



