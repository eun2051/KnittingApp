# 1단계: Gradle로 빌드
FROM gradle:8.5-jdk21 AS build
WORKDIR /build
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle
COPY src ./src
RUN gradle build -x test --no-daemon

# 2단계: 실행 환경
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/build/libs/*.jar app.jar

# 환경변수 설정
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 포트 노출
EXPOSE 8080

# 실행
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
