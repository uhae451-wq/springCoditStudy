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
| `MemberD1006` | ID·이름·이메일을 가진 직렬화 대상 객체. `changeName()`으로 이름 변경 |
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
| `Test0916Controller` | `/test1`은 요청 검증, `/test2`는 DTO 목록 반환, `/test3`은 공통 성공 응답, `/test4`는 사용자 정의 예외 발생 |
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
| `Student`, `Instructor`, `CategoryD1006`, `Course`, `Enrollment` | 학생·강사·강의·수강 정보 엔티티와 필드 제약, 생성 시 기본값 정의 |
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
| `BookController`, `BookService` | `/v1/books`에서 도서 등록(`201`)과 분류별 페이지 조회 |
| `OrderController`, `OrderService` | `/v1/orders`에서 주문 생성(`201`)·단건 및 목록 조회·취소(`204`). 주문 시 재고 차감, 주문 저장, 결제 저장을 한 트랜잭션으로 처리 |
| `BookRepository` | 분류별 페이지 조회, 제목 검색, 재고 미만 조회와 JPQL 기반 재고 부족 도서 조회 |
| 예외 클래스 | `OutOfStockException`, `BookNotFoundException`, `MemberNotFoundException`, `OrderNotFoundException` |

### 정리

- **업무 규칙 캡슐화:** 재고 차감·취소 복구 같은 규칙을 엔티티 메서드에 두고 서비스는 흐름만 조합한다.
- **트랜잭션 일관성:** 주문·재고·결제를 한 트랜잭션으로 묶어 중간에 실패하면 모두 롤백되도록 한다.
- **API 완성:** JPA 연관관계, DTO, 페이징, 공통 예외 처리를 합쳐 REST API로 제공한다.
