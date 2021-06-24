FROM openjdk:11.0.7-jre

ENV APP_HOME /usr/local/onboardexp
ENV PATH $APP_HOME:$PATH

WORKDIR $APP_HOME

ADD target/*.jar ./api.jar

EXPOSE 8081
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "api.jar"]