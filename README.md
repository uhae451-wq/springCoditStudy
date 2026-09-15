# Spring Backend Study

학원에서 배운 Java / Spring Backend 내용을 주제별로 간략히 정리합니다.

## 학습 기록

| 주제 | 실습 패키지 |
| --- | --- |
| [파일 입출력·객체 직렬화·파일 저장소](#file-io) | `d0824` |
| [Spring Bean과 YAML 설정](#spring-bean-yaml) | `d0831` |
| [Spring MVC 요청 처리·첨부파일·예외 처리](#spring-mvc) | `d0909` |
| [요청 검증·DTO 분리·공통 API 응답](#api-response) | `d0915` |

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
