
# docker build -t diegorivash/app-authorsspring .
# docker login
# docker push diegorivash/app-authorsspring

# LO EJECUTO EN LA CASA.
# docker run app-authorsspring

FROM eclipse-temurin:21.0.7_6-jre-alpine-3.21

# CREAMOS UN DIRECTORIO PARA LA APLICACION
RUN mkdir /app
WORKDIR /app

# IDIOMA
ENV LANGUAGE='en_US:en'

# COPIAMOS EL JAR DE SPRING BOOT (build con Gradle)
COPY build/libs/*.jar app.jar

# EXPONEMOS EL PUERTO (opcional, para documentación)
EXPOSE 8080

# COMANDO PARA EJECUTAR LA APLICACION SPRING BOOT
CMD ["java", "-jar", "app.jar"]