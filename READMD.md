# Spring Backend Study

학원에서 배운 Java / Spring Backend 내용을 주제별로 간략히 정리합니다.

## 학습 기록

| 주제 | 실습 패키지 |
| --- | --- |
| [Spring Bean과 YAML 설정](#spring-bean-yaml) | `d0831` |
| [Spring MVC 요청 처리·첨부파일·예외 처리](#spring-mvc) | `d0909` |

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
