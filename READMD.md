# Spring Backend Study

학원에서 배운 Java / Spring Backend 내용을 날짜별로 간략히 정리합니다.

## 학습 기록

| 날짜 | 주제 | 정리 |
| --- | --- | --- |
| 2026-08-31 | Spring Bean과 YAML 설정 | [내용 보기](#day-2026-08-31) |

---

<a id="day-2026-08-31"></a>

## 2026-08-31 · Spring Bean과 YAML 설정

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

- `PokemonService`: `@Primary`로 `Charmander`, `@Qualifier("squirtle")`로 `Squirtle`, `List<Pokemon>`으로 둘 다 주입.
- `ValueYaml`: 설정된 인사말 `Hello, Spring!` 출력.
- 환경 설정: 공통 포트 `8080`, 개발 `8081` / `DEBUG`, 운영 `8082` / `WARN`. IntelliJ의 **Active profiles**에서 선택.
- `DataSourceInfoPrinter`: 환경별 메시지를 출력하는 Bean 등록 실습.

**설계 이유:** 구현체 선택과 환경 설정을 분리하면 사용하는 코드의 변경을 줄일 수 있다.

### 코드 확인 메모

- `Charmander`와 `Squirtle`의 공격 메시지가 서로 뒤바뀌어 있음.
- 기본 `DataSourceConfig`에 프로필 조건이 없어 개발·운영 Bean과 이름이 충돌할 수 있음.

### 오늘 배운 것 한 줄 요약

Spring에 객체 생성·주입을 맡기고, 프로필로 환경별 설정을 분리한다.
