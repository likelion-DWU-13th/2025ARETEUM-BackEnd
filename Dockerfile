# ===== Build Stage =====
FROM gradle:8.9-jdk21 AS build
WORKDIR /workspace

# 캐시 최적화
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN ./gradlew --version

# 소스 복사 & 빌드
COPY . .
RUN ./gradlew clean bootJar -x test

# ===== Run Stage =====
FROM eclipse-temurin:21-jre
WORKDIR /app

# 공통 설정
ENV TZ=Asia/Seoul \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0" \
    SPRING_PROFILES_ACTIVE=prod

COPY --from=build /workspace/build/libs/*SNAPSHOT*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-lc", "java $JAVA_OPTS -jar app.jar"]
