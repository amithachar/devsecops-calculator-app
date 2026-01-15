FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
EXPOSE 8081
ENTRYPOINT [ "./run-with-add-opens.sh" ]
