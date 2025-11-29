# 1단계: Gradle로 빌드
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

# 2단계: 실행 환경
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/app-0.0.1-SNAPSHOT.jar app.jar

# 환경변수 설정
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 포트 노출
EXPOSE 8080

# 실행
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
