FROM eclipse-temurin:17-jdk
RUN useradd -m -s /bin/bash appuser
WORKDIR /app
COPY . .
RUN chown -R appuser:appuser /app
RUN chmod 777 run-with-add-opens.sh
USER appuser
EXPOSE 8081
ENTRYPOINT ["./run-with-add-opens.sh"]