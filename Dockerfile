# Gradle과 Java 17이 포함된 공식 Docker 이미지 사용
FROM gradle:7.6.2-jdk17 AS builder

WORKDIR /app

# Gradle 캐시 활용하여 의존성 다운로드 속도 개선
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon

# 전체 프로젝트 복사 (한 번에 복사하여 레이어 최적화)
COPY . .

# 테스트 제외하고 빌드 (-x test)
RUN ./gradlew clean build -x test --no-daemon --parallel --build-cache

# 최종 실행 환경 (JDK 17 사용)
FROM openjdk:17-jdk

WORKDIR /app

# 빌드된 JAR 파일을 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 실행
CMD ["java", "-jar", "app.jar"]
