# JDK 21 사용
FROM eclipse-temurin:21-jdk

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper와 설정 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY gradle.properties .

# 소스 코드 복사
COPY src src

# Gradle 실행 권한 부여 및 빌드
RUN chmod +x gradlew && ./gradlew build -x test --no-daemon

# 환경변수 설정
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 포트 노출
EXPOSE 8080

# 실행 (빌드된 JAR 파일 직접 실행)
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar build/libs/*.jar"]
