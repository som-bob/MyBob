# 🍱 MyBob - 레시피 추천 백엔드 서비스

> 냉장고에 등록한 재료 정보를 기반으로, 레시피를 조회하고 관리할 수 있는 사용자 중심의 웹 API 서버입니다.
> Spring Boot 기반으로, 레시피 등록/수정/조회, 냉장고 재료 관리, 최근 본 레시피 저장 등의 기능을 제공합니다.

---

### 📌 프로젝트 개요

- **프로젝트 명**: MyBob  
- **목표**
  - 냉장고에 등록한 재료 정보를 기반으로, 레시피를 조회하고 관리 기능을 제공하는 백엔드 서비스 구현
  - GitHub Actions로 CI/CD 자동화 구성 (EC2 Docker 배포 자동화)
- **형태**: 개인 사이드 프로젝트  
- **역할**: 백엔드 설계 및 개발 전체 담당  
- **진행 기간**: 2024.12 ~ (진행 중)  
- **배포 주소**: 추후 추가 예정  

---

### 🛠 사용 기술 스택

| 구분           | 기술명                                                                  |
|----------------|-------------------------------------------------------------------------|
| Language       | Java 17                                                                 |
| Framework      | Spring Boot 3, Spring Security, Spring Validation                       |
| Build Tool     | Gradle                                                                  |
| Database       | MySQL, Redis                                                            |
| ORM            | JPA (Hibernate), QueryDSL                                               |
| Design         | Domain-Driven Design (DDD), Interface 중심 설계                         |
| Auth           | OAuth2, JWT (쿠키 저장 방식)                                            |
| Infra & DevOps | Docker, GitHub Actions, AWS (EC2, RDS, S3, CloudFront, ELB 등)         |
| Monitoring     | Prometheus, Grafana (예정)                                              |
| Test           | JUnit 5, Mockito, WebTestClient, BDD/TDD 적용                           |
| Docs           | Swagger / OpenAPI                                                       |

---

### 📦 주요 기능

- **회원 인증 및 보안**
  - OAuth2 (Google) 로그인
  - JWT 토큰 발급 및 쿠키 저장 방식 인증
  - 비회원 접근 차단 및 GUEST 권한 반환 후 프론트에서 회원가입 유도

- **레시피 관리**
  - 레시피 등록 / 수정 / 삭제 / 조회
  - 사용자가 선택한 재료 기반 레시피 필터링
  - Spring Data JPA + QueryDSL

- **재료 관리**
  - 카테고리별 재료 등록 및 조회
  - 냉장고에 보유한 재료 관리 기능

- **최근 본 레시피**
  - Redis List 구조를 사용하여 사용자 당 모든 레시피 최대 10개 저장
  - 중복 접근 시 최신 위치로 이동 (LRU 방식)

- **레시피 추천**
  - 냉장고 재료 기반 레시피 추천 기능

- **관리자 기능 (예정)**
  - 레시피 등록 및 재료 관리

---

### 🧪 테스트
- 단위 테스트: JUnit5 + Mockito
- 통합 테스트: WebTestClient 활용 (Spring Security 제외 테스트는 standaloneSetup())
- BDD 기반 테스트 구조 적용


### 📈 성능 최적화 및 고도화 방향 (진행 중)
- Redis 캐시 전략 도입 (단건 조회 TTL 적용 예정)
- Kafka 기반 주문 이벤트 처리 구조 도입 예정
- 관리자 대시보드 개발 예정
- MSA 분리 구조 계획 (공통 모듈 정리 및 API 게이트웨이 분리 포함)
- 모니터링 도입 예정 (Prometheus + Grafana)


### 👋 Author
SM N (som-bob)
Backend Developer / Interested in Distributed Systems, Observability, and Clean Architecture
📧 Email: ndanl4647@gmail.com
📌 GitHub: [https://github.com/som-bob](https://github.com/Sumsan38)



