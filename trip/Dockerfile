#Adpated from https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/scores/Dockerfile
FROM adoptopenjdk/openjdk11:alpine

COPY target/trip.jar .

CMD java -Dspring.profiles.active=FakeData  -jar trip.jar