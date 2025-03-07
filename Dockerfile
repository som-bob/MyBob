# Java 17 기반 이미지 사용
FROM openjdk:17-jdk

# 컨테이너 내부의 작업 디렉토리
WORKDIR /app

# JAR 파일을 컨테이너에 복사
COPY target/*.jar app.jar

# 컨테이너 실행 시 JAR 파일 실행
CMD ["java", "-jar", "app.jar"]
