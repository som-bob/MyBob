# Gradle과 Java 17이 포함된 공식 Docker 이미지 사용
FROM gradle:7.6.2-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 캐시를 활용하기 위해 필요한 파일만 먼저 복사
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

# 의존성 다운로드만 먼저 수행 (이 단계는 캐싱됨)
RUN ./gradlew dependencies --no-daemon

# 전체 프로젝트 복사 (이 단계에서 캐시 무효화 방지)
COPY . .

# Gradle 빌드 수행 (테스트 제외 + 빌드 캐시 활성화)
RUN ./gradlew clean build -x test --no-daemon --parallel --build-cache

# 실행할 JDK 17 이미지를 불러옴 (최적화된 실행 환경)
FROM openjdk:17-jdk

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 실행
CMD ["java", "-jar", "app.jar"]
