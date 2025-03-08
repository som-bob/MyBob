# Gradle과 Java 17이 포함된 공식 Docker 이미지 사용
FROM gradle:7.6.2-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트의 모든 파일을 컨테이너 내부로 복사
COPY . .

# Gradle을 이용해 프로젝트 빌드 (테스트 제외)
RUN ./gradlew clean build -x test

# 빌드된 JAR 파일만 가져오는 단계
FROM openjdk:17-jdk

WORKDIR /app

# 위에서 빌드한 JAR 파일을 복사 (최신 JAR만 사용)
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 실행
CMD ["java", "-jar", "app.jar"]
