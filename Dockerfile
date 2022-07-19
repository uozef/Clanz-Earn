FROM maven:3-openjdk-17 as MAVEN_BUILD
WORKDIR /home/app/
COPY ./ ./
RUN mvn -Dmaven.test.skip clean package


FROM openjdk:17-alpine
RUN java --version
# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD /home/app/target/Clanz-Staking-0.0.1-SNAPSHOT.jar /Clanz-Staking-0.0.1-SNAPSHOT.jar
# set the startup command to execute the jar
ENTRYPOINT ["java", "-jar", "/Clanz-Staking-0.0.1-SNAPSHOT.jar"]
