# Spring Backend Study

학원에서 배운 Java / Spring Backend 내용을 주제별로 간략히 정리합니다.

## 학습 기록

| 주제 | 실습 패키지 |
| --- | --- |
| [파일 입출력·객체 직렬화·파일 저장소](#file-io) | `d0824` |
| [Spring Bean과 YAML 설정](#spring-bean-yaml) | `d0831` |
| [Spring MVC 요청 처리·첨부파일·예외 처리](#spring-mvc) | `d0909` |
| [요청 검증·DTO 분리·공통 API 응답](#api-response) | `d0915` |
| [트레이너·예약 API 통합 실습](#trainer-reservation) | `d0916` |
| [SQL 테이블·조건 조회·수정](#sql-basics) | `sql/start.sql`, `sql/0923.sql` |
| [SQL 조인·집계·관계 테이블](#sql-relations) | `sql/0928.sql` |
| [외래 키·주문 데이터·인덱스](#sql-constraints-index) | `sql/0929.sql` |
| [JPA 엔티티·영속성 컨텍스트](#jpa-entity) | `d0930` |
| [JPA 연관관계·Spring Data JPA](#jpa-relations) | `d1001` |
| [트랜잭션 전파·N+1 문제 해결](#transaction-fetch) | `d1001` |
| [게시글·댓글 연관관계·페이징](#jpa-board) | `d1002` |
| [도서 주문 API 통합 실습](#book-order) | `d1006` |
| [N+1 문제 재현과 해결](#n-plus-one) | `d1012` |
| [커스텀 검증·에러 코드·공통 예외 응답](#error-code) | `d1026` |
| [로깅 설정·Actuator 헬스 체크](#logging-actuator) | `d1026`, `logback-spring.xml` |
| [JUnit·Mockito·JPA Repository 테스트](#test-basics) | `test/d1027` → `main/d1026` |
| [도서 주문 도메인·예외 처리·요청 추적](#book-order-testing) | `main/d1027` |
| [도메인·Repository·Service·Controller 테스트](#layer-tests) | `test/d1028` → `main/d1027` |

---

<a id="file-io"></a>

## 파일 입출력·객체 직렬화·파일 저장소

**실습 패키지:** `d0824`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 파일과 경로 | `Path`로 경로를 표현하고, `Files`로 폴더 생성·파일 조회·삭제 수행 |
| 직렬화·역직렬화 | `Serializable` 객체를 `ObjectOutputStream`으로 파일에 저장하고, `ObjectInputStream`으로 복원 |
| 파일 기반 저장소 | 회원 한 명을 하나의 `.ser` 파일로 저장하고, ID로 해당 파일을 찾아 처리 |
| 수정 반영 | 메모리의 객체를 변경한 뒤 `save()`로 파일을 다시 써야 수정 내용이 저장됨 |
| 자원 정리 | `try-with-resources`로 파일 입출력 스트림과 `Files.list()`의 스트림을 자동으로 닫음 |
| 조회 결과와 실패 | 회원이 없으면 `Optional.empty()`를 반환하고, 파일 읽기 실패는 예외로 전달 |

### 실습 내용

| 코드·설정 | 실습 내용 |
| --- | --- |
| `Member` | ID·이름·이메일을 가진 직렬화 대상 객체. `changeName()`으로 이름 변경 |
| `FileMemberRepository` 생성자 | `fileio-practice/member` 저장 폴더 준비 |
| `save()`, `findById()` | 회원을 `UUID.ser` 파일로 저장하고 ID로 조회·복원. 같은 ID로 저장하면 기존 파일을 덮어씀 |
| `findAll()`, `findByEmail()` | 저장된 회원 파일을 읽어 전체 목록을 만들고, 이메일이 일치하는 회원 조회 |
| `existsById()`, `deleteById()` | 파일 경로로 존재 여부를 확인하고 해당 회원 파일 삭제 |
| `FileIOMain` | 회원 저장 → 새 저장소 인스턴스에서 복원 → 이름 변경 후 저장 전·후 비교 → 파일 삭제 |

### 정리

- **파일 저장과 복원:** 메모리의 객체를 파일로 남기고, 저장소 객체를 새로 만들어도 데이터를 다시 읽는 흐름을 익힌다.
- **파일 기반 CRUD:** 회원의 생성·조회·수정·삭제를 파일 쓰기·읽기·삭제로 구현하는 방법을 익힌다.
- **객체 변경과 저장:** 메모리에서 값을 바꾸는 것과 파일에 반영하는 것이 별개임을 확인한다.

---

<a id="spring-bean-yaml"></a>

## Spring Bean과 YAML 설정

**실습 패키지:** `d0831`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| Bean 등록 | `@Component`, `@Service`로 자동 등록하거나 `@Configuration` + `@Bean`으로 직접 등록 |
| 생성자 주입 | 필요한 의존성을 생성자로 전달받음. 생성자가 하나면 `@Autowired` 생략 가능 |
| Bean 선택 | 기본 후보는 `@Primary`, 특정 후보 지정은 `@Qualifier`, 전체 주입은 `List<타입>` |
| YAML 설정 | `@Value("${my.greeting}")`으로 설정값 주입 |
| 프로필 | `application-dev/prod.yaml`과 `@Profile`로 환경별 설정·Bean 구성 분리 |

### 실습 내용

| 코드·설정 | 실습 내용 |
| --- | --- |
| `PokemonService` | `@Primary`로 `Charmander`, `@Qualifier`로 `Squirtle`, `List<Pokemon>`으로 둘 다 주입 |
| `ValueYaml` | YAML에서 읽은 인사말 `Hello, Spring!` 출력 |
| `DataSourceInfoPrinter` | 환경별 메시지를 출력하는 Bean 등록 |
| 환경 설정 | 공통 포트 `8080`, 개발 `8081` / `DEBUG`, 운영 `8082` / `WARN`. IntelliJ의 **Active profiles**에서 선택 |

### 정리

- **Bean과 의존성 주입:** Spring에 객체를 등록하고, 필요한 객체를 주입받는 방법을 익힌다.
- **설정과 프로필:** YAML 설정값을 코드에서 사용하고, 개발·운영 환경에 따라 설정과 Bean을 바꾸는 방법을 익힌다.

---

<a id="spring-mvc"></a>

## Spring MVC 요청 처리·첨부파일·예외 처리

**실습 패키지:** `d0909`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 요청 데이터 받기 | `@RequestParam`은 요청 파라미터, `@PathVariable`은 URL 경로 값, `@RequestBody`는 JSON 등의 요청 본문을 객체로 받음 |
| 헤더와 쿠키 | `@RequestHeader`로 헤더, `@CookieValue`로 쿠키를 읽고, `HttpServletResponse`로 쿠키를 설정 |
| 화면 응답 | `@Controller`에서 `Model`에 데이터를 담고 `test`, `errorPage` 같은 뷰 이름을 반환 |
| 파일 업로드 | `MultipartFile`로 파일을 받고, 검증 후 UUID 기반 이름으로 `uploads` 폴더에 저장 |
| 파일 응답 | `ResponseEntity<Resource>`로 파일을 보내고, `Content-Disposition: attachment`로 다운로드를 유도 |
| 예외 처리 | 컨트롤러 내부 `@ExceptionHandler`가 우선하며, 해당 처리기가 없으면 `@ControllerAdvice`의 공통 처리기를 사용 |

### 실습 내용

| 코드·설정 | 실습 내용 |
| --- | --- |
| `TrainerController → TrainerService → TrainerRepository` | 트레이너 등록·단건 조회·전체 조회. `ConcurrentHashMap`에 저장하고 `AtomicLong`으로 ID 생성 |
| `PracticeController` | User-Agent 조회, JSON 수신·출력, 유효기간 1시간의 HttpOnly 쿠키 설정·조회 |
| `ImageController` | 빈 파일·확장자·Content-Type 검증, 단일·다중 업로드, 다운로드, 삭제 후 `204` 응답 |
| `AController`, `BController`, `ExceptionController` | 개별·공통 예외 처리 비교. `/b/error`는 공통 처리기로 전달 |
| `ImageUploadException`, `ErrorResponse` | 예외에 오류 코드를 담고, 화면에 전달할 코드·메시지를 객체로 구성 |

### 정리

- **요청과 응답:** 파라미터·JSON·헤더·쿠키를 읽고, 트레이너 등록·조회를 통해 요청이 Controller → Service → Repository로 전달되어 화면에 결과를 보여주는 흐름을 익힌다.
- **첨부파일 처리:** 클라이언트가 보낸 파일을 받아 저장하고, 다운로드·삭제하는 흐름을 익힌다.
- **예외 처리:** 요청 처리 중 발생한 예외를 오류 화면으로 연결하고, 컨트롤러별 처리와 공통 처리의 적용 범위·우선순위를 익힌다.

---

<a id="api-response"></a>

## 요청 검증·DTO 분리·공통 API 응답

**실습 패키지:** `d0915`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| JSON 응답 | `@RestController`에서 반환한 객체를 응답 본문으로 전달 |
| DTO 분리 | 요청 DTO로 입력을 받고, 도메인 객체에서 응답에 필요한 값만 응답 DTO로 변환 |
| 요청 검증 | DTO에 `@NotBlank`, `@Email`, `@Min`, `@Max`, `@Positive` 조건을 선언하고 `@Valid`로 검증 실행 |
| 공통 응답 형식 | `ApiResponse<T>`의 `success`, `data`, `error`, `timestamp`로 성공·실패 응답 구조를 통일 |
| HTTP 상태 코드 | `ResponseEntity`로 응답 본문과 함께 `200`, `201`, `400`, `404` 등 상태 코드를 지정 |
| JSON 예외 처리 | `@RestControllerAdvice`와 `@ExceptionHandler`로 예외를 받아 오류 코드·메시지를 담은 응답으로 변환 |

### 실습 내용

| 코드·설정 | 실습 내용 |
| --- | --- |
| `CreateCoffeeRequest`, `Coffee`, `CoffeeResponse` | 요청 → 도메인 → 응답 DTO 변환. `CoffeeResponse.from()`으로 `id`, `name`, `price`만 반환 |
| `CoffeeController` | `/coffee`는 DTO 반환, `/coffee1`은 `@Valid` 검증 추가, `/coffee2`는 공통 응답 형식과 `201 Created` 적용 |
| `RequestDTO`, `Test`, `ResponseDTO` | 이름·이메일·나이 검증 조건을 선언하고 요청·도메인·응답 객체를 분리 |
| `TestController` | `/test1`은 요청 검증, `/test2`는 DTO 목록 반환, `/test3`은 공통 성공 응답, `/test4`는 사용자 정의 예외 발생 |
| `ApiResponse`, `ApiError` | `success()`는 데이터를, `fail()`은 오류 코드·메시지를 담아 공통 응답 생성 |
| `CoffeeGlobalExceptionHandler` | 입력 검증 실패 시 필드별 오류 메시지를 모아 `400`과 `INVALID_INPUT` 반환 |
| `DefaultGlobalExceptionHandler` | `CustomException`을 공통 실패 응답으로 변환하고 `404` 반환 |

### 정리

- **DTO 분리:** 입력받을 값과 응답으로 보여줄 값을 구분하고, 요청 → 도메인 → 응답으로 변환하는 흐름을 익힌다.
- **요청 검증:** 잘못된 입력을 검증하고, 어떤 항목이 잘못됐는지 클라이언트에 전달하는 방법을 익힌다.
- **공통 API 응답:** 성공 데이터와 오류를 일정한 JSON 구조로 반환하고, 처리 결과에 맞는 HTTP 상태 코드를 함께 전달하는 방법을 익힌다.

---

<a id="trainer-reservation"></a>

## 트레이너·예약 API 통합 실습

**실습 패키지:** `d0916`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 계층별 요청 처리 | Controller에서 요청·응답을 처리하고, Service에서 업무 조건을 확인하며, Repository에서 데이터를 저장·조회 |
| 폼과 파일 수신 | `multipart/form-data` 요청에서 `@ModelAttribute`로 트레이너 정보를, `MultipartFile`로 선택적 프로필 이미지를 받음 |
| 조건별 조회 | 선택적 쿼리 파라미터로 트레이너 전문 분야나 예약의 트레이너 ID를 지정해 목록 필터링 |
| 예약 입력 검증 | `@Valid`와 `@NotNull`, `@NotBlank`, `@Future`로 필수 입력과 미래 예약 시간 검증 |
| 업무 예외와 응답 | 트레이너·예약 없음, 예약 충돌 등의 예외를 공통 오류 응답과 HTTP 상태 코드로 변환 |
| API 문서 정보 | Swagger/OpenAPI의 `@Tag`, `@Schema`로 API 설명과 요청 필드 예시 작성 |

### 실습 내용

| 코드·설정 | 실습 내용 |
| --- | --- |
| `TrainerController2` | `/v2/trainers`에서 트레이너·이미지 등록, 전문 분야별 목록 조회, 프로필 이미지 응답 |
| `TrainerService2`, `TrainerRepository2` | 트레이너를 메모리에 저장하고, ID 조회 시 존재 여부 확인 및 전문 분야 필터링 |
| `ReservationController` | `/v2/reservations`에서 예약 생성·단건 및 목록 조회·시간 변경·삭제. 생성은 `201`, 삭제는 `204` 응답 |
| `ReservationService` | 예약 생성 전 트레이너 존재 확인, 생성·변경 시 충돌 검사 호출, 조회·변경·삭제 시 예약 존재 확인 |
| `ReservationRepository_1` | 예약을 메모리에 저장하고 조회·시간 변경·삭제 및 충돌 검사 메서드 구성 |
| 요청·응답 DTO | 생성·시간 변경 입력을 구분하고, 트레이너 및 예약 조회 결과를 응답 객체로 변환 |
| `GlobalExceptionHandler`, `ApiResponse`, `ApiError` | 입력 오류 `400`, 대상 없음 `404`, 예약 충돌 `409`, 예상하지 못한 오류 `500`의 공통 응답 구성 |

### 정리

- **기능 통합:** 요청 검증·파일 업로드·DTO·공통 응답을 트레이너와 예약 API에 함께 적용하는 흐름을 익힌다.
- **예약 처리:** 트레이너와 예약을 연결하고, 등록·조회·시간 변경·삭제 과정에서 필요한 업무 조건을 확인하는 방법을 익힌다.
- **결과 전달:** 정상 처리와 업무 예외를 구분해 클라이언트에 응답하고, API 설명과 입력 예시를 문서화하는 방법을 익힌다.


---

<a id="sql-basics"></a>

## SQL 테이블·조건 조회·수정

**실습 파일:** `sql/start.sql`, `sql/0923.sql`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 테이블 정의 | `CREATE TABLE`과 자료형, `PRIMARY KEY`, `NOT NULL`, `UNIQUE`, `CHECK`, `DEFAULT`로 저장 규칙을 정의 |
| 데이터 조작 | `INSERT`, `SELECT`, `UPDATE`, `DELETE`로 행을 추가·조회·수정·삭제 |
| 조건과 정렬 | `WHERE`, `AND`·`OR`, `IN`, `BETWEEN`, `LIKE`, `ORDER BY`로 조회 결과를 선택·정렬 |
| 결과 범위 | `DISTINCT`, 계산식, `LIMIT`, `OFFSET`으로 중복 제거·계산·결과 구간 조회 |
| 트랜잭션 | `BEGIN` 이후 변경을 `COMMIT`으로 확정하거나 `ROLLBACK`으로 취소 |

### 실습 내용

| SQL | 실습 내용 |
| --- | --- |
| `start.sql` | 테이블 생성·변경·삭제와 `pokemon_sample`의 제약 조건 정의 |
| `0923.sql` | 포켓몬 테이블 생성 및 데이터 삽입, 조건 검색과 정렬 연습 |
| `UPDATE`, `DELETE` 실습 | 조건에 맞는 행을 변경·삭제하고 트랜잭션 롤백으로 변경 취소 확인 |
| 외래 키 기초 | `trainers`와 `pokemon`에 트레이너 ID를 연결 |

### 정리

- **SQL CRUD:** 테이블 구조와 제약 조건을 정하고 SQL로 데이터를 직접 다루는 흐름을 익힌다.
- **조건 조회와 트랜잭션:** 조회 조건을 조합하고, 여러 변경을 확정하거나 되돌리는 방법을 익힌다.

---

<a id="sql-relations"></a>

## SQL 조인·집계·관계 테이블

**실습 파일:** `sql/0928.sql`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 조인 | `INNER JOIN`으로 양쪽에 연결된 행을 조회하고, `LEFT JOIN`으로 왼쪽 행을 유지하며 연결 데이터가 없는 경우도 조회 |
| 다대다 관계 | 연결 테이블 `pokemon_moves`에 두 테이블의 ID를 저장해 포켓몬과 기술의 관계를 표현 |
| 집계 | `COUNT`, `AVG`, `MAX`, `MIN`으로 여러 행을 요약하고 `GROUP BY`로 그룹별 결과 계산 |
| 그룹 조건 | `HAVING`으로 집계 결과에 조건을 적용하고 `COALESCE`로 `NULL` 표시를 대체 |

### 실습 내용

| SQL | 실습 내용 |
| --- | --- |
| 트레이너와 포켓몬 조인 | 트레이너 조건에 맞는 포켓몬을 찾고, 주인이 없는 포켓몬은 야생으로 표시 |
| 기술 연결 테이블 | 포켓몬·기술 기본 테이블과 복합 기본 키를 가진 `pokemon_moves`를 생성하고 연결 데이터 조회 |
| 집계 쿼리 | 타입별 포켓몬 수, 트레이너별 보유 수, 기술별 사용 수와 평균 레벨 계산 |

### 정리

- **관계형 조회:** 여러 테이블에 나뉜 데이터를 조인해 하나의 결과로 조회하는 방법을 익힌다.
- **집계와 다대다:** 연결 테이블로 다대다 관계를 표현하고 그룹별 통계를 계산하는 방법을 익힌다.

---

<a id="sql-constraints-index"></a>

## 외래 키·주문 데이터·인덱스

**실습 파일:** `sql/0929.sql`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 삭제 참조 규칙 | 외래 키의 `ON DELETE RESTRICT`, `CASCADE`, `SET NULL`로 참조 대상 삭제 시 동작을 지정 |
| 제약 조건 | 기본 키·외래 키·`UNIQUE`·`CHECK`로 데이터의 유효성과 관계를 제한 |
| 주문 상세 | 주문과 메뉴의 연결을 `order_items`로 표현하고 수량·주문 시점 가격을 함께 저장 |
| 인덱스 | 조회 조건에 맞는 인덱스를 만들고 `EXPLAIN ANALYZE`로 쿼리 실행 계획과 시간을 확인 |

### 실습 내용

| SQL | 실습 내용 |
| --- | --- |
| 삭제 규칙 비교 | 회원을 참조하는 주문을 만들고 삭제 제한·연쇄 삭제·외래 키 `NULL` 설정 결과 비교 |
| 카페 주문 모델 | 회원·카테고리·메뉴·주문·주문 항목 테이블을 만들고 주문 내역 조인 조회 |
| 대량 데이터 조회 | `bigdata` 조회를 실행 계획으로 살펴보고 이메일 컬럼 인덱스 생성 |

### 정리

- **관계와 삭제:** 외래 키 삭제 규칙은 연결 데이터의 보존·삭제 방식에 맞춰 선택한다.
- **조회 성능 확인:** 인덱스 추가 전후의 실행 계획을 비교해 검색 조건에 맞는 인덱스의 효과를 확인한다.

---

<a id="jpa-entity"></a>

## JPA 엔티티·영속성 컨텍스트

**실습 패키지:** `d0930`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 엔티티 매핑 | `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`으로 객체와 테이블·컬럼을 매핑 |
| 생성자와 기본 생성자 | 애플리케이션에서는 필요한 값으로 객체를 만들고, JPA용 기본 생성자는 `protected`로 둠 |
| 영속성 컨텍스트 | `EntityManager.persist()`로 엔티티를 관리하고, 조회한 엔티티의 변경은 트랜잭션 안에서 감지됨 |
| 쓰기와 초기화 | `flush()`는 대기 중인 SQL을 DB에 보내고, `clear()`는 영속성 컨텍스트를 비움 |
| enum·비영속 값 | `EnumType.STRING`으로 enum 이름을 저장하고 `@Transient` 필드는 DB 컬럼에서 제외 |

### 실습 내용

| 코드 | 실습 내용 |
| --- | --- |
| `Student`, `Instructor`, `Category`, `Course`, `Enrollment` | 학생·강사·강의·수강 정보 엔티티와 필드 제약, 생성 시 기본값 정의 |
| `PracticeTest` | `EntityManager`를 주입받아 저장·조회·수정·삭제, `flush()`와 `clear()` 확인 |
| 엔티티 메서드 | `Course.changePrice()`와 `Enrollment.updateProgress()`로 상태 변경을 엔티티 동작으로 표현 |

### 정리

- **엔티티 생명주기:** 엔티티를 영속 상태로 만들고 변경 사항을 트랜잭션에서 반영하는 흐름을 익힌다.
- **매핑 기준:** 객체 필드의 저장 여부, 제약 조건, enum 표현 방식을 JPA 어노테이션으로 정의한다.

---

<a id="jpa-relations"></a>

## JPA 연관관계·Spring Data JPA

**실습 패키지:** `d1001`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 연관관계 매핑 | `@ManyToOne`, `@OneToMany`, `@OneToOne`과 `@JoinColumn`으로 엔티티 간 관계를 표현 |
| 연관관계 주인 | 외래 키를 가진 쪽이 관계를 저장하며, 일대다에서는 보통 다 쪽의 `@ManyToOne`이 주인 |
| 지연 로딩 | `FetchType.LAZY`로 연관 객체 사용 시점에 조회하도록 설정 |
| Repository | `JpaRepository` 기본 CRUD와 메서드 이름 기반 쿼리로 저장·조회 기능을 구성 |
| 트랜잭션 경계 | `@Transactional(readOnly = true)`를 기본으로 두고 서비스 메서드에서 조회·변경 작업 수행 |
| 페이지 조회 | `Pageable`로 페이지·크기·정렬 조건을 전달하고 `Page` 결과를 받음 |

### 실습 내용

| 코드 | 실습 내용 |
| --- | --- |
| 병원 엔티티 | `Department`와 `Doctor`, `Patient`와 `Allergy`, `Reservation`과 `MedicalRecord`, `MedicalRecord`와 `Prescription` 등 관계 구성 |
| 관계 저장·조회 테스트 | `EntityManager`로 엔티티를 저장하고 연관 객체를 조회하며 관계 주인과 양방향 컬렉션을 확인 |
| `DoctorRepository` | 이름·면허번호·부서 조건 조회, 존재 여부 확인, `Pageable` 기반 페이지 조회 메서드 정의 |
| `PatientRepository`, `PatientService` | 전화번호 중복 확인, 이름 검색, 서비스에서 환자 생성·조회·정보 변경 |
| 테스트 데이터 | 부서·의사·환자·알레르기·예약·진료 기록·처방 데이터를 연결해 생성 |

### 정리

- **객체 간 관계:** 외래 키가 있는 연관관계 주인과 양방향 참조를 구분해 엔티티 관계를 구성한다.
- **데이터 접근:** Spring Data JPA의 기본 기능과 메서드 이름 기반 쿼리로 조회를 만들고 서비스 트랜잭션에서 사용한다.

---

<a id="transaction-fetch"></a>

## 트랜잭션 전파·N+1 문제 해결

**실습 패키지:** `d1001`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 트랜잭션 묶음 | `@Transactional` 메서드 안의 결제·예약 확정·알림 기록을 하나의 작업 단위로 처리 |
| 전파 옵션 | `Propagation.REQUIRES_NEW`로 호출한 쪽과 별도의 트랜잭션을 만들어 독립적으로 커밋 |
| 롤백 규칙 | 런타임 예외는 기본적으로 롤백되며 `noRollbackFor`로 특정 예외는 롤백에서 제외 |
| N+1 문제 | 연관 엔티티를 지연 로딩하면 목록 조회 뒤 건마다 추가 쿼리가 발생 |
| 페치 조인·`@EntityGraph` | `join fetch` JPQL이나 `@EntityGraph(attributePaths)`로 연관 객체를 한 번에 조회 |

### 실습 내용

| 코드 | 실습 내용 |
| --- | --- |
| `ReservationConfirmService` | 결제 → 예약 확정 → 알림 기록을 한 흐름으로 묶고, 장애 여부에 따라 예외를 발생시켜 결과 비교 |
| `PaymentService` | `REQUIRES_NEW`로 결제 기록을 먼저 커밋하고, 금액 부족 시 `PaymentFiledException` 발생 |
| `NofityService` | `REQUIRES_NEW`로 알림 로그를 별도 저장해 바깥 트랜잭션이 롤백돼도 기록 유지 |
| `ReservationRepository` | 기간·상태 조건 조회, `findTop5By...`, `join fetch` 쿼리와 `@EntityGraph`를 적용한 조회·페이징 |
| `TestController1002` | `/test/1` 요청으로 예약 확정 트랜잭션 동작 확인 |

### 정리

- **트랜잭션 분리:** 실패해도 남겨야 하는 기록(결제·알림)은 `REQUIRES_NEW`로 분리해 롤백 범위를 조절한다.
- **조회 최적화:** 연관 객체를 함께 쓰는 조회는 페치 조인이나 `@EntityGraph`로 N+1 문제를 줄인다.

---

<a id="jpa-board"></a>

## 게시글·댓글 연관관계·페이징

**실습 패키지:** `d1002`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 양방향 연관관계 | `Post`의 `@OneToMany(mappedBy)`와 `Comment`의 `@ManyToOne`으로 게시글·댓글 관계를 구성 |
| 연관관계 편의 메서드 | `addComment()`에서 컬렉션 추가와 `setPost()`를 함께 처리해 양쪽 참조를 일치시킴 |
| 영속성 전이·고아 객체 | `CascadeType.ALL`, `orphanRemoval = true`로 게시글 저장·삭제 시 댓글도 함께 처리 |
| 쿼리 메서드·JPQL | `findByTitleContaining`, `findByAuthor`와 `@Query`·`@Param`으로 제목 검색 |
| `Page`와 `Slice` | `Page`는 전체 개수 조회까지 수행하고, `Slice`는 다음 페이지 존재 여부만 확인 |

### 실습 내용

| 코드 | 실습 내용 |
| --- | --- |
| `Member`, `Post`, `Comment` | 회원·게시글·댓글 엔티티와 길이·`unique`·`nullable` 제약 정의 |
| `PostRepository` | 키워드 검색, 작성자별 조회, `Slice` 반환 페이지 조회 메서드 정의 |
| `MemberTest` | 회원과 게시글 50건 생성, `PageRequest.of(1, 10, Sort.by("title"))` 페이징, 댓글 추가 후 게시글 삭제 |

### 정리

- **관계 관리:** 편의 메서드와 cascade 설정으로 부모-자식 엔티티를 함께 저장·삭제하는 방법을 익힌다.
- **페이징 선택:** 전체 건수가 필요한 경우는 `Page`, 더보기 형태는 `Slice`를 선택한다.

---

<a id="book-order"></a>

## 도서 주문 API 통합 실습

**실습 패키지:** `d1006`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 엔티티 설계 | 회원·분류·도서·주문·주문 항목·결제 엔티티를 연관관계와 enum 상태로 구성 |
| 도메인 메서드 | `removeStock()`, `addStock()`, `OrderD1006.cancel()`에서 재고 검증·복구와 취소 상태 변경 처리 |
| 주문 가격 보존 | `OrderItemD1006`에 주문 시점의 가격을 저장하고 총액은 항목 합계로 계산 |
| DTO 변환 | 요청·응답 DTO를 분리하고 `from()`으로 엔티티를 응답 객체로 변환. 지연 로딩이라 트랜잭션 안에서 호출 |
| 예외 매핑 | `@RestControllerAdvice`에서 재고 부족·잘못된 상태는 `400`, 대상 없음은 `404`로 응답 |

### 실습 내용

| 코드 | 실습 내용 |
| --- | --- |
| `DataInitializer` | 실행 시 분류·회원 5명·도서 10권 기본 데이터 생성 |
| `Book1006Controller`, `Book1006Service` | `/v1/books`에서 도서 등록(`201`)과 분류별 페이지 조회 |
| `Order1006Controller`, `Order1006Service` | `/v1/orders`에서 주문 생성(`201`)·단건 및 목록 조회·취소(`204`). 주문 시 재고 차감, 주문 저장, 결제 저장을 한 트랜잭션으로 처리 |
| `Book1006Repository` | 분류별 페이지 조회, 제목 검색, 재고 미만 조회와 JPQL 기반 재고 부족 도서 조회 |
| 예외 클래스 | `OutOfStockException`, `BookNotFoundException`, `MemberNotFoundException`, `OrderNotFoundException` |

### 정리

- **업무 규칙 캡슐화:** 재고 차감·취소 복구 같은 규칙을 엔티티 메서드에 두고 서비스는 흐름만 조합한다.
- **트랜잭션 일관성:** 주문·재고·결제를 한 트랜잭션으로 묶어 중간에 실패하면 모두 롤백되도록 한다.
- **API 완성:** JPA 연관관계, DTO, 페이징, 공통 예외 처리를 합쳐 REST API로 제공한다.

---

<a id="n-plus-one"></a>

## N+1 문제 재현과 해결

**실습 패키지:** `d1012`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| N+1 문제 | 목록을 1번 조회한 뒤 연관 컬렉션에 접근할 때마다 쿼리가 N번 더 실행되는 문제 |
| 기본 로딩 전략 | `@OneToMany`는 기본이 `LAZY`, `@ManyToOne`은 기본이 `EAGER`라서 `LAZY`를 직접 명시 |
| fetch join | `join fetch` JPQL로 팀과 멤버를 한 번의 쿼리로 조회하고 `distinct`로 중복 제거 |
| `@EntityGraph` | 어노테이션만으로 fetch join과 같은 효과를 내며 `LEFT OUTER JOIN`으로 조회 |
| 배치 사이즈 | `setFetchBatchSize`로 연관 컬렉션을 `IN` 조건으로 묶어 조회해 쿼리 수를 줄임 |
| 쿼리 횟수 측정 | Hibernate `Statistics`로 실행된 SQL 수를 세어 `assertThat`으로 검증 |

### 실습 내용

| 코드 | 실습 내용 |
| --- | --- |
| `Team1012`, `Member1012` | 팀 1 : 멤버 N 관계 엔티티. 멤버가 외래 키를 가진 연관관계 주인 |
| `TeamRepositoryD1012` | fetch join 쿼리와 `@EntityGraph` 쿼리 정의 |
| `NPlusOneTest` | 팀 5개 × 멤버 3명을 저장하고 `flush()`·`clear()`로 캐시를 비운 뒤 쿼리 수 비교 |
| 테스트별 쿼리 수 | `findAll()`만 호출하면 1번, 멤버 접근 시 1+N번(6번), fetch join·`@EntityGraph`는 1번, 배치 사이즈는 2번 |
| `jpa-n-plus-1.md` | 재현 과정과 해결책별 실제 SQL, 한계를 글로 정리 |

### 정리

- **원인 확인:** 지연 로딩된 컬렉션을 반복문에서 사용하면 건마다 쿼리가 나가므로 쿼리 횟수를 숫자로 확인한다.
- **해결 선택:** 항상 함께 쓰는 연관 데이터는 fetch join이나 `@EntityGraph`로, 넓게 줄이고 싶을 때는 배치 사이즈를 사용한다.

---

<a id="error-code"></a>

## 커스텀 검증·에러 코드·공통 예외 응답

**실습 패키지:** `d1026`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| Bean Validation | `@Valid`와 `@NotNull`, `@NotEmpty`, `@Min`, `@NotBlank`로 요청 값을 검증 |
| 커스텀 검증 어노테이션 | `@Constraint`, `@Target`, `@Retention`으로 `@BannedWord`, `@Phone`을 만들고 `ConstraintValidator`로 검증 로직 구현 |
| 에러 코드 enum | `ErrorCode`에 코드·메시지·HTTP 상태를 함께 정의해 예외 응답을 일관되게 구성 |
| 예외 계층 | 추상 `BaseException`이 `ErrorCode`를 가지고, 회원·상품·재고·이메일 중복 예외가 상속 |
| 공통 예외 처리 | `@RestControllerAdvice`에서 업무 예외, 검증 실패, 예상하지 못한 예외를 구분해 응답 |

### 실습 내용

| 코드 | 실습 내용 |
| --- | --- |
| `Member1026Controller`, `Member1026Service` | `/api/members`에서 회원 가입(`201`)과 조회. 이메일 중복 시 `409` |
| `Product1026Controller`, `Product1026Service` | `/api/products`에서 상품 등록. 이후 JPA 저장소로 전환 |
| `Order1026Controller`, `Order1026Service` | `/api/orders`에서 회원·상품 확인, 재고 차감, 총 주문 금액 계산 |
| `BannedWordValidator`, `PhoneValidator` | 금지어 포함 여부와 `010-0000-0000` 형식을 정규식으로 검사. null은 통과 |
| `ErrorResponse` | 에러 코드·메시지와 필드별 오류 목록을 담는 공통 응답 |
| `ApiExceptionHandler1026` | 업무 예외는 `ErrorCode`의 상태로, 검증 실패는 `400`, 그 외는 `500`으로 응답하고 로그 기록 |
| Repository 전환 | 처음에는 `ConcurrentHashMap`과 `AtomicLong`으로 구현하고, 테스트 실습에서 `JpaRepository`와 엔티티 매핑으로 전환 |

### 정리

- **검증 확장:** 기본 검증으로 부족한 규칙은 커스텀 어노테이션과 `ConstraintValidator`로 직접 만든다.
- **오류 응답 통일:** 에러 코드와 예외 계층을 두어 어떤 예외든 같은 형식으로 클라이언트에 전달한다.

---

<a id="logging-actuator"></a>

## 로깅 설정·Actuator 헬스 체크

**실습 파일:** `application.yaml`, `logback-spring.xml`, `d1026`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 로그 레벨 | `root: info`를 기본으로 두고 패키지별로 `DEBUG`·`TRACE`를 조절. 서비스에서는 `@Slf4j`의 `log.info`·`warn`·`error` 사용 |
| 로그 출력 설정 | `logging.pattern.console`과 `logging.file.name`으로 콘솔 패턴과 `logs/app.log` 파일 지정 |
| Logback 설정 | `logback-spring.xml`에서 콘솔·파일 appender를 정의하고 `springProfile`로 환경별 출력 분리 |
| 롤링 정책 | 날짜별로 파일을 나누고 50MB 초과 시 분할, 30일 보관, 전체 3GB 제한 |
| Actuator | `health`, `info`, `metrics`만 노출하고 `show-details: always`로 항목별 상태 표시 |
| 커스텀 헬스 인디케이터 | `HealthIndicator`를 구현해 서비스에 맞는 상태 정보를 `/actuator/health`에 추가 |

### 실습 내용

| 코드·설정 | 실습 내용 |
| --- | --- |
| `application.yaml` | 로그 레벨·패턴·파일 경로와 `management` 노출 범위 설정 |
| `logback-spring.xml` | `local` 프로필은 콘솔만, `prod` 프로필은 콘솔과 롤링 파일에 기록 |
| `MemberHealthIndicator` | 회원 저장소의 회원 수를 상태 정보로 노출하고, 접근 실패 시 `DOWN`과 사유 반환 |
| `build.gradle` | `spring-boot-starter-actuator` 의존성 추가 |
| 서비스 로그 | 주문 요청·재고 확인·주문 완료 단계에 로그를 남기고, 예외 핸들러에서 스택트레이스까지 기록 |

### 정리

- **로그 관리:** 환경에 따라 출력 위치와 레벨을 나누고, 파일은 날짜·크기 기준으로 보관한다.
- **상태 모니터링:** Actuator로 애플리케이션과 저장소 상태를 확인하고 노출할 엔드포인트를 제한한다.


---

<a id="test-basics"></a>

## JUnit·Mockito·JPA Repository 테스트

**실습 패키지:** `src/test/java/.../d1027` → `src/main/java/.../d1026`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 테스트 구성 | `@Test`, `@DisplayName`으로 검증할 동작을 표현하고 Given → When → Then으로 준비·실행·검증 구분 |
| 테스트 준비 | `@BeforeEach`에서 각 테스트에 필요한 객체나 데이터를 새로 준비 |
| 결과와 예외 검증 | JUnit의 `assertEquals`, `assertThrows`와 AssertJ의 `assertThat`으로 결과·예외·조회 목록 확인 |
| Mock과 의존성 주입 | `MockitoExtension`, `@Mock`, `@InjectMocks`로 Repository를 대체하고 서비스의 분기와 계산 검증 |
| 호출 검증과 Spy | `when().thenReturn()`으로 응답을 정하고, `verify()`·`never()`로 호출 여부 확인. `spy()`는 실제 객체의 동작과 호출을 함께 확인 |
| JPA 테스트 | `@DataJpaTest`로 엔티티와 Repository를 로딩해 쿼리 조건을 검증하고 테스트별 트랜잭션 롤백으로 데이터 분리 |

### 실습 내용

| 코드 | 실습 내용 |
| --- | --- |
| `Member1026`, `Product1026` | JPA 엔티티·ID 자동 생성·기본 생성자를 추가하고 기존 메모리 저장소를 `JpaRepository`로 전환 |
| `ProductTest` | 재고 차감, 재고 부족 예외, 재고와 주문 수량이 같은 경계값 확인. 현재 `@SpringBootTest` 사용 |
| `Member1026ServiceTest` | 회원 가입 결과와 이메일 중복 예외 검증 |
| `Order1026ServiceTest` | 정상 주문·여러 품목의 총액, 회원·상품 없음, 재고 부족 예외, 실패 시 다음 저장소 미호출과 Spy의 재고 차감 확인 |
| `Member1026RepositoryTest` | 저장된 이메일과 없는 이메일의 `existsByEmail()` 결과 비교 |
| `ProductRepositoryTest` | 재고 이하·상품명 포함·빈 결과·가격 범위 조회 검증. 가격 범위 테스트는 결과 포함 여부를 확인하며 정렬 순서는 별도 검증하지 않음 |
| `Product1026ServiceTest` | Mock과 서비스 주입 설정까지 작성한 상태이며 테스트 메서드는 아직 없음 |

### 정리

- **검증 대상 분리:** 서비스의 업무 분기는 Mock으로, 실제 쿼리 조건은 JPA 테스트로 확인한다.
- **실패와 경계값:** 정상 결과뿐 아니라 재고가 딱 맞는 경우와 대상 없음·재고 부족 예외도 테스트로 남긴다.

---

<a id="book-order-testing"></a>

## 도서 주문 도메인·예외 처리·요청 추적

**실습 패키지:** `src/main/java/.../d1027`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 도메인 검증 | 요청 DTO의 검증과 별개로 엔티티도 수량과 재고를 검사해 직접 호출되는 경우의 규칙을 보호 |
| 주문 트랜잭션 | 회원·도서 조회 → 재고 차감 → 주문·결제 저장을 한 트랜잭션에서 수행 |
| 변경 감지와 취소 | 관리 중인 엔티티의 상태와 재고를 변경하면 별도 `save()` 없이 반영 |
| 업무 예외 구분 | `BusinessException`과 `ErrorCode`로 대상 없음·재고 부족·잘못된 수량·중복 취소를 구분 |
| 예외 처리 범위 | `@RestControllerAdvice(basePackages = ...)`로 날짜별 실습의 예외 처리기가 서로 간섭하지 않도록 범위 지정 |
| 요청 식별자 | 필터에서 요청 ID를 MDC에 넣고 오류 응답에도 담음. 처리 후 `finally`에서 MDC 정리 |

### 실습 내용

| 코드 | 실습 내용 |
| --- | --- |
| `Book1027Controller`, `Book1027Service` | `/d1027/v1/books`에서 도서 등록과 분류별 페이지 조회. 없는 분류는 업무 예외로 처리 |
| `Order1027Controller`, `Order1027Service` | `/d1027/v1/orders`에서 주문 생성·조회·취소. 주문 생성 메서드는 주문 ID 반환 |
| `Book1027` | 1 미만 수량과 재고 부족을 거부하고 재고 차감·복구 수행 |
| `Order1027`, `OrderItem1027` | 주문 시점 가격 보존, 품목별 금액 합산, 양방향 관계 설정, 취소 시 재고 복구와 중복 취소 차단 |
| `Member1027` | GOLD 등급으로 변경하고 `earnPoint()` 호출 시 주문 금액의 10% 적립. 주문 서비스에는 아직 연결하지 않음 |
| `ApiExceptionHandler1027`, `Error1027Response` | 업무 예외·필드 검증 실패·예상하지 못한 예외를 코드·메시지·필드 오류·요청 ID로 변환 |
| `RequestIdFilter` | 요청마다 짧은 UUID를 MDC의 `requestId`에 저장하고 처리 종료 시 정리 |

### 정리

- **규칙의 위치:** 재고·취소 규칙은 도메인에서 지키고, 서비스는 저장소 호출과 트랜잭션 흐름을 조합한다.
- **오류 추적:** 오류 응답에 요청 ID를 담아 해당 요청의 처리 흐름을 찾을 기준을 마련한다.

---

<a id="layer-tests"></a>

## 도메인·Repository·Service·Controller 테스트

**실습 패키지:** `src/test/java/.../d1028` → `src/main/java/.../d1027`

### 핵심 개념

| 개념 | 요약 |
| --- | --- |
| 도메인 테스트 | 객체의 상태 변화·계산·예외 조건을 확인. Spring이 필요 없는 규칙은 객체를 직접 생성해 검증 가능 |
| Repository 테스트 | `@DataJpaTest`로 조회 조건·정렬·DB 제약 확인. `saveAndFlush()`로 SQL을 실행시켜 제약 위반 확인 |
| 서비스 단위 테스트 | Mockito로 저장소를 대체하고 업무 분기·재고 변화·주문 및 결제 저장 호출 검증 |
| 서비스 통합 테스트 | `@SpringBootTest`로 실제 서비스와 저장소를 연결해 DB 반영을 확인. Mock 테스트로는 실제 트랜잭션 롤백을 검증할 수 없음 |
| MVC 테스트 | `@WebMvcTest`와 `MockMvc`로 요청 경로·상태 코드·JSON·입력 검증 확인. `@MockitoBean`으로 서비스 대체 |
| 검증문과 테스트 이름 | 테스트 이름에 적은 의도가 실제 assertion으로 확인되는지 점검해야 함 |

### 실습 내용

| 코드 | 실습 내용 |
| --- | --- |
| `domain/BookTest` | 재고 차감·경계값·재고 부족·음수 수량 거부와 주문 취소 후 재고 복구 확인. 현재 Spring 통합 테스트가 함께 포함됨 |
| `domain/MemberTest` | GOLD 회원의 50,000원 주문에 대한 5,000포인트 적립 확인 |
| `repository/MemberRepositoryTest` | 같은 이메일을 `saveAndFlush()`로 저장해 DB의 unique 제약 위반 확인 |
| `repository/BookRepositoryTest` | 분류·재고 조건에 맞는 도서 수와 가격 내림차순 결과 확인 |
| `service/OrderServiceTest` | 정상 주문의 재고 차감·주문 및 결제 저장, 회원 없음·재고 부족, 취소 상태와 재고 복구 검증 |
| `service/OrderServiceIntergrationTest` | 실제 주문·취소 후 DB에서 재고 재조회. `@AfterEach`에서 결제 → 주문 → 회원·도서 순으로 데이터 정리 |
| `controller/BookControllerTest` | 도서 등록 `201`과 ID, 빈 제목·저자의 `400` 및 필드별 오류, 쿼리 파라미터를 포함한 조회 요청 확인 |
| `controller/OrderControllerTest` | 없는 주문의 `404`·`ORDER_NOT_FOUND`, 단건 조회 `200`, 취소 `204`와 요청·응답 출력 확인 |

현재 `OrderTest`의 총액 테스트에는 검증문이 없고, `OrderServiceTest`의 여러 품목 총액 테스트는 주문 ID 반환값을 금액과 비교하고 있어 보완이 필요하다. 통합 테스트의 롤백 시나리오도 현재 예외와 도서 제목만 확인하므로, 재고 복원과 주문·결제 미저장까지 검증한 상태는 아니다.

### 정리

- **계층별 책임:** 도메인은 규칙, Repository는 쿼리와 제약, Service는 업무 흐름, Controller는 HTTP 계약을 확인한다.
- **검증 범위:** 테스트가 실행되는 것과 의도한 동작을 충분히 검증하는 것은 구분한다. 특히 롤백은 실제 DB 상태로 확인한다.

**관련 학습 문서:** [테스트 입문](test-code-beginner.md) · [계층별 테스트 가이드](test-code-guide.md) · [TDD 입문](test-code-tdd-intro.md)
