# [TDD] 선착순 쿠폰 발급 API로 배우는 3계층 TDD - Repository · Service · Controller 테스트를 먼저 작성하기

> TDD를 Controller 범위까지 배웠습니다. 개념만 읽으면 와닿지 않아서, **선착순 쿠폰 발급 API** 하나를 **Repository → Service → Controller** 순서로 **테스트를 먼저 작성하며** 만들어 봤습니다.
> 왜 TDD를 쓰는지, 계층마다 어떤 도구로 무엇을 검증하는지, 어떤 점이 중요한지를 **코드 위주**로 정리했습니다.

**환경**: Java 17, Spring Boot 4.1.1, JUnit 5, Mockito, AssertJ, H2

---

## 1. 왜 TDD인가?

TDD(Test-Driven Development)는 **테스트를 먼저 쓰고, 그 테스트를 통과시키는 코드를 나중에 쓰는** 개발 방식입니다.

```
🔴 Red       실패하는 테스트를 먼저 쓴다     → 아직 구현되지 않았음을 확인
🟢 Green     통과시키는 최소한의 코드를 쓴다  → 요구사항이 구현되었음을 확인
🔵 Refactor  통과 상태를 유지하며 정리한다    → 정리하다가 깨지지 않았음을 확인
```

### 쿠폰 발급 같은 기능에서 특히 필요한 이유

쿠폰 발급에는 **규칙이 많고, 하나라도 틀리면 곧바로 돈과 연결**됩니다.

| 규칙 | 틀리면 생기는 일 |
|---|---|
| 수량이 소진되면 발급 불가 | 100장 쿠폰이 101장 발급됨 |
| 한 회원은 1번만 발급 | 같은 회원이 쿠폰을 여러 장 가져감 |
| 만료된 쿠폰은 발급 불가 | 만료된 쿠폰이 발급됨 |
| 만료 시각 정각은? | 정각에 오는 사용자만 억울하게 거절됨 |

"되는 경우"만 눈으로 확인해서는 이런 **경계와 실패 경로**를 놓치기 쉽습니다. TDD는 이 규칙을 **구현 전에 테스트로 먼저 확정**하게 만듭니다.

### 이 글의 실습에서 테스트가 실제로 잡아준 것들

| 계층 | 테스트가 알려준 것 |
|---|---|
| Repository | 조건을 `<=`로 썼더니 **마지막 한 장이 더 발급됨** (`expected: 0 but was: 1`) |
| Service | 뼈대(`return null`)로는 **규칙 6개가 전부 실패**. 만료 시각 **정각**은 발급되어야 한다는 경계도 정의됨 |
| Controller | 에러 응답의 상태 코드가 **우연히 맞아도** 에러 코드 검증에서 걸림 |

---

## 2. 계층별로 무엇을, 어떤 도구로 테스트하나

한 요청은 이렇게 흘러갑니다.

```
HTTP 요청 → [Controller] → [Service] → [Repository] → DB
```

계층마다 **책임이 달라서 검증할 것도 도구도 다릅니다.**

| 계층 | 검증하는 것 | 테스트 도구 | 가짜(Mock)로 대체하는 것 |
|---|---|---|---|
| **Repository** | 쿼리가 맞는가 (조건·경계값) | `@DataJpaTest` | 없음 (임베디드 DB를 실제로 사용) |
| **Service** | 비즈니스 규칙·분기·예외·협력 호출 | `@ExtendWith(MockitoExtension)` | Repository |
| **Controller** | HTTP 계약 (경로·상태 코드·JSON·검증·에러 응답) | `@WebMvcTest` + `MockMvc` | Service |

### 어떤 순서로 만들까?

| 방식 | 순서 | 특징 |
|---|---|---|
| **Inside-out** (이 글) | Repository → Service → Controller | 아래 계층부터 만들면 위 계층 테스트가 컴파일 가능한 상태로 시작된다 |
| Outside-in | Controller → Service → Repository | 사용자 관점에서 시작, 아래 계층은 Mock으로 먼저 정의 |

---

## 3. 만들 기능

> **선착순 쿠폰 발급 API**
> `POST /api/coupons/{couponId}/issue` + 본문 `{"memberId": 10}`

| 상황 | 응답 | 에러 코드 |
|---|---|---|
| 발급 성공 | 201 + 발급 내역 | - |
| 없는 쿠폰 | 404 | `COUPON_001` |
| 만료된 쿠폰 | 400 | `COUPON_002` |
| 이미 발급받음 | 409 | `COUPON_003` |
| 수량 소진 | 409 | `COUPON_004` |
| `memberId` 누락 | 400 | `COMMON_001` |

**도메인**

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private int discountAmount;
  private int totalQuantity;      // 총 발급 가능 수량
  private int issuedQuantity;     // 지금까지 발급된 수량
  private LocalDateTime expiresAt;

  // 만료 시각 "이후"부터 만료. 만료 시각 정각까지는 사용 가능
  public boolean isExpiredAt(LocalDateTime now) {
    return now.isAfter(expiresAt);
  }
}

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssue {            // 누가 어떤 쿠폰을 언제 발급받았는지
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long couponId;
  private Long memberId;
  private LocalDateTime issuedAt;
}
```

**에러 처리** : 비즈니스 오류는 `ErrorCode` 하나로 표현하고, `@RestControllerAdvice`가 응답으로 변환합니다.

```java
public enum ErrorCode {
  COUPON_NOT_FOUND("COUPON_001", "쿠폰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  COUPON_EXPIRED  ("COUPON_002", "만료된 쿠폰입니다.",       HttpStatus.BAD_REQUEST),
  ALREADY_ISSUED  ("COUPON_003", "이미 발급받은 쿠폰입니다.", HttpStatus.CONFLICT),
  COUPON_SOLD_OUT ("COUPON_004", "쿠폰이 모두 소진되었습니다.", HttpStatus.CONFLICT),
  INVALID_INPUT   ("COMMON_001", "입력값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
  ...
}

public class BusinessException extends RuntimeException {
  private final ErrorCode errorCode;
  ...
}
```

---

## 4. Repository 계층 - `@DataJpaTest`

### 이 계층에서 필요한 쿼리 2개

1. 이 회원이 이 쿠폰을 **이미 발급받았는가** (중복 방지)
2. 남은 수량이 있을 때만 **발급 수량을 1 증가** (선착순)

### 🔴 Red : 테스트부터

```java
@DataJpaTest
class CouponRepositoryTest {

  @Autowired CouponRepository couponRepository;
  @Autowired CouponIssueRepository couponIssueRepository;

  private Coupon saveCoupon(int totalQuantity) {
    return couponRepository.save(
        new Coupon("신규가입 5000원", 5000, totalQuantity, LocalDateTime.of(2026, 12, 31, 23, 59)));
  }

  @Test
  @DisplayName("같은 쿠폰을 같은 회원이 이미 발급받았다면 true")
  void existsByCouponIdAndMemberId_true() {
    Coupon coupon = saveCoupon(10);
    couponIssueRepository.save(new CouponIssue(coupon.getId(), 100L, LocalDateTime.now()));

    assertThat(couponIssueRepository.existsByCouponIdAndMemberId(coupon.getId(), 100L)).isTrue();
  }

  @Test
  @DisplayName("다른 회원이거나 다른 쿠폰이면 false")
  void existsByCouponIdAndMemberId_false() {
    Coupon coupon = saveCoupon(10);
    Coupon other = saveCoupon(10);
    couponIssueRepository.save(new CouponIssue(coupon.getId(), 100L, LocalDateTime.now()));

    assertThat(couponIssueRepository.existsByCouponIdAndMemberId(coupon.getId(), 200L)).isFalse();  // 다른 회원
    assertThat(couponIssueRepository.existsByCouponIdAndMemberId(other.getId(), 100L)).isFalse();   // 다른 쿠폰
  }

  @Test
  @DisplayName("남은 수량이 있으면 발급 수량이 1 증가하고 1을 반환한다")
  void increaseIssuedQuantity_success() {
    Coupon coupon = saveCoupon(2);

    int updated = couponRepository.increaseIssuedQuantity(coupon.getId());

    assertThat(updated).isEqualTo(1);
    assertThat(couponRepository.findById(coupon.getId()).orElseThrow().getIssuedQuantity()).isEqualTo(1);
  }

  @Test
  @DisplayName("수량이 모두 소진되면 0을 반환하고 발급 수량은 더 늘어나지 않는다")
  void increaseIssuedQuantity_soldOut() {
    Coupon coupon = saveCoupon(2);

    couponRepository.increaseIssuedQuantity(coupon.getId());                  // 1번째
    couponRepository.increaseIssuedQuantity(coupon.getId());                  // 2번째 (마지막 1개)
    int updated = couponRepository.increaseIssuedQuantity(coupon.getId());    // 3번째 → 소진

    assertThat(updated).isZero();
    assertThat(couponRepository.findById(coupon.getId()).orElseThrow().getIssuedQuantity()).isEqualTo(2);
  }
}
```

메서드가 아직 없으니 **컴파일부터 실패**합니다.

```
error: cannot find symbol
  symbol:   method existsByCouponIdAndMemberId(Long,long)
  symbol:   method increaseIssuedQuantity(Long)
```

뼈대를 만듭니다. 이때 흔한 경계값 실수인 `<=`로 작성해 봅니다.

```java
public interface CouponIssueRepository extends JpaRepository<CouponIssue, Long> {

  boolean existsByCouponIdAndMemberId(Long couponId, Long memberId);    // 메서드 이름으로 쿼리 생성
}

public interface CouponRepository extends JpaRepository<Coupon, Long> {

  @Modifying(flushAutomatically = true, clearAutomatically = true)
  @Query("update Coupon c set c.issuedQuantity = c.issuedQuantity + 1 "
       + "where c.id = :id and c.issuedQuantity <= c.totalQuantity")      // ← 경계값 실수
  int increaseIssuedQuantity(@Param("id") Long id);
}
```

이번에는 **컴파일은 되는데 테스트가 실패**합니다.

```
CouponRepositoryTest > 수량이 모두 소진되면 0을 반환하고 발급 수량은 더 늘어나지 않는다 FAILED
  expected: 0
   but was: 1
```

→ 수량이 2장인 쿠폰이 **3번째에도 발급**되었습니다. 경계값 실수를 테스트가 잡았습니다.

### 🟢 Green : `<=` → `<`

```java
@Query("update Coupon c set c.issuedQuantity = c.issuedQuantity + 1 "
     + "where c.id = :id and c.issuedQuantity < c.totalQuantity")
int increaseIssuedQuantity(@Param("id") Long id);
```

```
BUILD SUCCESSFUL      (4 tests, 0 failures)
```

### 🧩 이 계층에서 쓰인 것

| | 설명 |
|---|---|
| `@DataJpaTest` | JPA 관련 빈(엔티티, Repository)**만** 로드하는 슬라이스 테스트. 임베디드 DB를 쓰고 **테스트마다 롤백**되어 데이터가 섞이지 않는다 |
| `existsByCouponIdAndMemberId` | **쿼리 메서드**. 이름대로 `WHERE coupon_id = ? AND member_id = ?` 쿼리가 만들어진다 |
| `@Modifying` + `@Query("update ...")` | 조회가 아닌 **UPDATE/DELETE 쿼리**. 변경된 **행 수**를 `int`로 반환한다 |
| `flushAutomatically` | 쿼리 실행 **전에** 영속성 컨텍스트의 변경을 DB에 반영 |
| `clearAutomatically` | 쿼리 실행 **후에** 영속성 컨텍스트를 비움 |

### 왜 `findById` 후 `+1` 하지 않고 UPDATE 쿼리를 쓰나?

```java
// ❌ 조회 → 비교 → 증가 : 동시에 두 요청이 들어오면 둘 다 "남아 있음"으로 보고 통과한다
Coupon coupon = couponRepository.findById(id).orElseThrow();
if (coupon.getIssuedQuantity() < coupon.getTotalQuantity()) {
  coupon.increase();
}
```

```sql
-- ✅ 조건 확인과 증가가 하나의 SQL 문장 : DB가 행 단위로 직렬화해서 초과 발급을 막는다
update coupon set issued_quantity = issued_quantity + 1
 where id = ? and issued_quantity < total_quantity
```

반환값이 `1`이면 발급 성공, `0`이면 소진입니다. 선착순처럼 **동시 요청**이 몰리는 기능에서 쓰는 일반적인 패턴이고, 이런 SQL은 **Mock으로는 절대 검증할 수 없어서 Repository 테스트가 필요**합니다.

### `clearAutomatically`가 없으면?

UPDATE 쿼리는 영속성 컨텍스트를 거치지 않고 DB를 직접 수정합니다. 그래서 같은 트랜잭션에서 이미 읽어둔 엔티티는 **옛날 값(1차 캐시)** 을 그대로 갖고 있습니다.

```
saveCoupon()                 → 영속성 컨텍스트에 issuedQuantity = 0 으로 캐시
increaseIssuedQuantity()     → DB는 1, 캐시는 여전히 0
findById()                   → 캐시에서 0 을 돌려준다   ← 테스트가 이상하게 실패하는 원인
```

`clearAutomatically = true`로 쿼리 후 컨텍스트를 비우면 다음 `findById`가 DB에서 새로 읽습니다.

### ✅ 이 계층에서 중요한 점

- **경계값**을 반드시 테스트한다. (`<` vs `<=`가 "수량 2장에 3번째 발급"으로 이어진다)
- 조건이 `AND`로 묶인 쿼리는 **조건 하나씩 다른 데이터**를 넣어 `false`가 나오는지 확인한다. (다른 회원 / 다른 쿠폰)
- UPDATE 쿼리는 **반환값(행 수)** 과 **DB에 실제로 반영된 값** 둘 다 검증한다.
- 비즈니스 규칙(만료, 중복 거절)은 여기서 검증하지 않는다. Repository의 책임이 아니다.

---

## 5. Service 계층 - Mockito

### 이 계층에서 검증할 것

- 비즈니스 **규칙의 순서와 분기** (없는 쿠폰 → 만료 → 중복 → 소진)
- 올바른 **예외(에러 코드)** 가 발생하는지
- 실패했을 때 **하지 말아야 할 일을 하지 않았는지** (`verify(..., never())`)
- DB도 스프링도 필요 없다. Repository는 Mock으로 대체한다.

### 시간에 의존하는 코드 : `Clock`을 주입한다

"만료 여부"는 **현재 시각**에 따라 결과가 달라집니다. 서비스 안에서 `LocalDateTime.now()`를 직접 부르면 테스트가 실행 시점에 따라 흔들립니다.

```java
LocalDateTime.now()              // ❌ 테스트에서 시각을 고정할 수 없다
LocalDateTime.now(clock)         // ✅ Clock 을 주입받으면 테스트에서 고정된 시각을 넣을 수 있다
```

```java
@Configuration
public class CouponConfig {
  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();      // 운영에서는 시스템 시계
  }
}
```

### 🔴 Red : 테스트부터

```java
@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

  private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
  private static final LocalDateTime NOW = LocalDateTime.of(2026, 10, 28, 12, 0);
  private static final LocalDateTime EXPIRES_AT = LocalDateTime.of(2026, 12, 31, 23, 59);

  @Mock CouponRepository couponRepository;
  @Mock CouponIssueRepository couponIssueRepository;

  CouponService couponService;

  @BeforeEach
  void setUp() {
    // 시간을 NOW 로 고정한 Clock 을 직접 넣어서 서비스를 만든다
    Clock fixedClock = Clock.fixed(NOW.atZone(ZONE).toInstant(), ZONE);
    couponService = new CouponService(couponRepository, couponIssueRepository, fixedClock);
  }

  private Coupon coupon(LocalDateTime expiresAt) {
    return new Coupon("신규가입 5000원", 5000, 100, expiresAt);
  }

  @Test
  @DisplayName("정상 발급 : 수량을 증가시키고 발급 내역을 저장해서 반환한다")
  void issue_success() {
    when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon(EXPIRES_AT)));
    when(couponIssueRepository.existsByCouponIdAndMemberId(1L, 10L)).thenReturn(false);
    when(couponRepository.increaseIssuedQuantity(1L)).thenReturn(1);
    when(couponIssueRepository.save(any(CouponIssue.class))).thenAnswer(inv -> inv.getArgument(0));

    CouponIssue result = couponService.issue(1L, 10L);

    assertThat(result.getCouponId()).isEqualTo(1L);
    assertThat(result.getMemberId()).isEqualTo(10L);
    assertThat(result.getIssuedAt()).isEqualTo(NOW);
  }

  @Test
  @DisplayName("없는 쿠폰이면 COUPON_NOT_FOUND")
  void issue_notFound() {
    when(couponRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> couponService.issue(99L, 10L))
        .isInstanceOf(BusinessException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.COUPON_NOT_FOUND);
  }

  @Test
  @DisplayName("만료된 쿠폰이면 COUPON_EXPIRED 이고 수량은 건드리지 않는다")
  void issue_expired() {
    when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon(NOW.minusSeconds(1))));   // 1초 전 만료

    assertThatThrownBy(() -> couponService.issue(1L, 10L))
        .isInstanceOf(BusinessException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.COUPON_EXPIRED);

    verify(couponRepository, never()).increaseIssuedQuantity(any());
  }

  @Test
  @DisplayName("경계값 : 만료 시각 정각에는 아직 발급할 수 있다")
  void issue_atExactExpiry() {
    when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon(NOW)));    // 만료 시각 == 현재
    when(couponIssueRepository.existsByCouponIdAndMemberId(1L, 10L)).thenReturn(false);
    when(couponRepository.increaseIssuedQuantity(1L)).thenReturn(1);
    when(couponIssueRepository.save(any(CouponIssue.class))).thenAnswer(inv -> inv.getArgument(0));

    assertThat(couponService.issue(1L, 10L)).isNotNull();
  }

  @Test
  @DisplayName("이미 발급받았다면 ALREADY_ISSUED 이고 수량은 건드리지 않는다")
  void issue_alreadyIssued() {
    when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon(EXPIRES_AT)));
    when(couponIssueRepository.existsByCouponIdAndMemberId(1L, 10L)).thenReturn(true);

    assertThatThrownBy(() -> couponService.issue(1L, 10L))
        .isInstanceOf(BusinessException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ALREADY_ISSUED);

    verify(couponRepository, never()).increaseIssuedQuantity(any());
  }

  @Test
  @DisplayName("수량이 소진되었다면 COUPON_SOLD_OUT 이고 발급 내역은 저장하지 않는다")
  void issue_soldOut() {
    when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon(EXPIRES_AT)));
    when(couponIssueRepository.existsByCouponIdAndMemberId(1L, 10L)).thenReturn(false);
    when(couponRepository.increaseIssuedQuantity(1L)).thenReturn(0);        // 소진

    assertThatThrownBy(() -> couponService.issue(1L, 10L))
        .isInstanceOf(BusinessException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.COUPON_SOLD_OUT);

    verify(couponIssueRepository, never()).save(any());
  }
}
```

`CouponService`가 아직 없어서 컴파일부터 실패합니다. 뼈대(동작 없음)를 만들면 **6개가 모두 실패**합니다.

```java
public CouponIssue issue(Long couponId, Long memberId) {
  return null;     // 뼈대 : 아직 구현하지 않음
}
```

```
6 tests completed, 6 failed
  Expecting code to raise a throwable.
  Cannot invoke "CouponIssue.getCouponId()" because "result" is null
```

### 🟢 Green : 규칙을 하나씩 구현

```java
@Service
@RequiredArgsConstructor
public class CouponService {

  private final CouponRepository couponRepository;
  private final CouponIssueRepository couponIssueRepository;
  private final Clock clock;

  @Transactional
  public CouponIssue issue(Long couponId, Long memberId) {
    LocalDateTime now = LocalDateTime.now(clock);

    Coupon coupon = couponRepository.findById(couponId)
        .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));      // issue_notFound

    if (coupon.isExpiredAt(now)) {
      throw new BusinessException(ErrorCode.COUPON_EXPIRED);                       // issue_expired, issue_atExactExpiry
    }

    if (couponIssueRepository.existsByCouponIdAndMemberId(couponId, memberId)) {
      throw new BusinessException(ErrorCode.ALREADY_ISSUED);                       // issue_alreadyIssued
    }

    if (couponRepository.increaseIssuedQuantity(couponId) == 0) {
      throw new BusinessException(ErrorCode.COUPON_SOLD_OUT);                      // issue_soldOut
    }

    return couponIssueRepository.save(new CouponIssue(couponId, memberId, now));   // issue_success
  }
}
```

```
BUILD SUCCESSFUL      (6 tests, 0 failures)
```

> 각 `if`가 **어떤 테스트 때문에 생겼는지** 주석으로 따라갈 수 있습니다. 이것이 TDD로 만든 코드의 특징입니다.

### 🧩 이 계층에서 쓰인 것

| | 설명 |
|---|---|
| `@ExtendWith(MockitoExtension.class)` | JUnit5에 Mockito를 연결. `@Mock`을 초기화한다 |
| `@Mock` | 가짜 객체. 지정하지 않으면 `null`/`false`/`0`/`Optional.empty()` 같은 기본값 |
| `Clock.fixed(...)` | 항상 같은 시각을 돌려주는 시계 |
| `when(...).thenReturn(...)` | 가짜가 돌려줄 값을 정함 (Stubbing) |
| `thenAnswer(inv -> inv.getArgument(0))` | **받은 인자를 그대로 돌려줌**. `save()`가 저장한 객체를 반환하는 동작을 흉내낼 때 쓴다 |
| `assertThatThrownBy(...)` | 예외 발생 검증 (AssertJ) |
| `.hasFieldOrPropertyWithValue("errorCode", ...)` | 예외의 **필드 값**까지 검증. "어떤 오류인지"를 확인한다 |
| `verify(mock, never()).메서드(any())` | 해당 메서드가 **한 번도 호출되지 않았는지** 검증 |

### 왜 `@InjectMocks`를 쓰지 않고 직접 생성했나?

`@InjectMocks`는 `@Mock`으로 만든 객체만 주입합니다. `Clock`은 Mock이 아니라 **고정된 진짜 시계**를 넣어야 하므로, `@BeforeEach`에서 생성자로 직접 조립했습니다.

```java
couponService = new CouponService(couponRepository, couponIssueRepository, fixedClock);
```

### ✅ 이 계층에서 중요한 점

- **실패 경로마다 테스트**를 만든다. 정상 하나만으로는 규칙 4개 중 하나도 검증되지 않는다.
- 예외는 **타입만이 아니라 에러 코드까지** 검증한다. (`BusinessException`이 같아도 `COUPON_EXPIRED`인지 `ALREADY_ISSUED`인지가 다르다)
- 실패 시 `verify(..., never())`로 **부작용이 없음**을 확인한다. (소진인데 발급 내역이 저장되면 안 된다)
- **경계값**: 만료 1초 전/정각을 따로 테스트한다. (`isAfter`인지 `isBefore`의 반대인지에 따라 결과가 갈린다)
- **시간·난수처럼 매번 달라지는 값**은 외부에서 주입받아 테스트에서 고정한다.
- `@Transactional`은 Mockito 테스트에서 **동작하지 않는다**. (스프링 프록시가 없다) 쿼리 정확성도 검증되지 않으므로 Repository 테스트가 따로 필요하다.

---

## 6. Controller 계층 - `@WebMvcTest` + `MockMvc`

### 이 계층에서 검증할 것

**HTTP 계약**입니다. 비즈니스 로직이 아니라 **요청과 응답의 모양**을 봅니다.

- 경로·메서드·**경로 변수·본문**이 서비스에 올바르게 전달되는가
- 상태 코드 (201, 400, 404, 409)
- 응답 JSON 구조
- 입력 검증(`@Valid`) 실패 시 응답
- 서비스에서 던진 예외가 **에러 응답으로 변환**되는가

Service는 `@MockitoBean`으로 대체합니다. 규칙은 Service 테스트에서 이미 검증했기 때문입니다.

### 🔴 Red : 테스트부터

```java
@WebMvcTest(CouponController.class)
class CouponControllerTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean CouponService couponService;

  @Test
  @DisplayName("발급에 성공하면 201 과 발급 내역을 반환하고, 경로·본문의 값이 서비스에 전달된다")
  void issue_success() throws Exception {
    when(couponService.issue(1L, 10L))
        .thenReturn(new CouponIssue(1L, 10L, LocalDateTime.of(2026, 10, 28, 12, 0)));

    mockMvc.perform(post("/api/coupons/{couponId}/issue", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"memberId": 10}
                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.couponId").value(1))
        .andExpect(jsonPath("$.memberId").value(10))
        .andExpect(jsonPath("$.issuedAt").value("2026-10-28T12:00:00"));

    verify(couponService).issue(1L, 10L);      // 경로 변수 1, 본문 10 이 그대로 전달되었는가
  }

  @Test
  @DisplayName("memberId 가 없으면 400 이고 서비스는 호출되지 않는다")
  void issue_missingMemberId() throws Exception {
    mockMvc.perform(post("/api/coupons/{couponId}/issue", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("COMMON_001"))
        .andExpect(jsonPath("$.errors[0].field").value("memberId"));

    verify(couponService, never()).issue(any(), any());
  }

  @ParameterizedTest(name = "{0} → HTTP {1}, 에러 코드 {2}")
  @CsvSource({
      "COUPON_NOT_FOUND, 404, COUPON_001",
      "COUPON_EXPIRED,   400, COUPON_002",
      "ALREADY_ISSUED,   409, COUPON_003",
      "COUPON_SOLD_OUT,  409, COUPON_004"
  })
  @DisplayName("서비스가 던진 예외가 상태 코드와 에러 코드로 변환된다")
  void issue_businessErrors(ErrorCode errorCode, int expectedStatus, String expectedCode) throws Exception {
    when(couponService.issue(1L, 10L)).thenThrow(new BusinessException(errorCode));

    mockMvc.perform(post("/api/coupons/{couponId}/issue", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"memberId": 10}
                """))
        .andExpect(status().is(expectedStatus))
        .andExpect(jsonPath("$.code").value(expectedCode));
  }
}
```

`CouponController`가 없어서 컴파일부터 실패합니다. 클래스 뼈대(엔드포인트 없음)를 만들고 실행하면 6개가 모두 실패합니다.

```
Status expected:<400> but was:<404>
Status expected:<201> but was:<404>
Response status expected:<409> but was:<404>
...
```

> 🔍 **상태 코드가 우연히 맞는 경우도 있습니다.** `COUPON_NOT_FOUND` 케이스는 기대하는 상태가 404인데, 엔드포인트가 없어서 나온 404와 **우연히 같습니다.**
> ```
> "COUPON_NOT_FOUND" → HTTP "404", 에러 코드 "COUPON_001" FAILED
>   No value at JSON path "$.code"
> ```
> 상태 코드만 검증했다면 이 테스트는 **Red에서 통과**해 버렸을 것입니다. 에러 응답은 상태 코드와 **에러 코드(본문)를 함께** 검증해야 합니다.

### 🟢 Green : 엔드포인트 구현

```java
public record IssueRequest(@NotNull(message = "회원 ID는 필수입니다.") Long memberId) {}

public record IssueResponse(Long couponId, Long memberId, LocalDateTime issuedAt) {
  public static IssueResponse from(CouponIssue issue) {
    return new IssueResponse(issue.getCouponId(), issue.getMemberId(), issue.getIssuedAt());
  }
}

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

  private final CouponService couponService;

  @PostMapping("/{couponId}/issue")
  @ResponseStatus(HttpStatus.CREATED)
  public IssueResponse issue(@PathVariable Long couponId, @Valid @RequestBody IssueRequest request) {
    CouponIssue issue = couponService.issue(couponId, request.memberId());
    return IssueResponse.from(issue);
  }
}
```

```
BUILD SUCCESSFUL      (6 tests, 0 failures)
```

### 🧩 이 계층에서 쓰인 것

| | 설명 |
|---|---|
| `@WebMvcTest(Controller.class)` | MVC 계층(컨트롤러, `@ControllerAdvice`, JSON 변환 등)**만** 로드. Service·Repository는 로드하지 않는다. 서버를 띄우지 않는다 |
| `MockMvc` | HTTP 요청을 **시뮬레이션**하는 도구 (포트 없이 빠르다) |
| `@MockitoBean` | 스프링 컨텍스트의 빈을 **Mockito Mock으로 교체**한다. Controller가 주입받는 Service를 가짜로 만든다 (`@MockBean`의 후속) |
| `perform(post(...).contentType(...).content(...))` | 요청을 보낸다. 본문은 텍스트 블록(`"""`)으로 JSON 작성 |
| `andExpect(status().isCreated())` | 상태 코드 검증 |
| `jsonPath("$.couponId").value(1)` | 응답 JSON 값 검증. `$`는 루트 |
| `@ParameterizedTest` + `@CsvSource` | **같은 테스트를 입력값 표로 반복**. 에러 코드 4개를 테스트 하나로 검증 |
| `verify(service).issue(1L, 10L)` | 경로 변수와 본문이 **올바른 인자로** 전달되었는지 |

### ✅ 이 계층에서 중요한 점

- 상태 코드와 함께 **에러 코드(본문)** 를 검증한다. 상태만 보면 우연히 통과하는 경우가 있다.
- 정상 하나가 아니라 **입력 검증 실패, 서비스 예외 매핑** 같은 실패 경로를 함께 테스트한다.
- 입력 검증에서 걸린 요청은 `verify(service, never())`로 **서비스까지 가지 않았음**을 확인한다.
- `verify(service).issue(1L, 10L)`로 **경로 변수·본문이 올바르게 연결**되었는지 확인한다. (잘못 연결되면 값이 뒤바뀐 채 동작한다)
- 비즈니스 로직(중복·소진 판단)은 여기서 검증하지 않는다. Service는 Mock이다.

---

## 7. 세 계층을 실제로 연결하는 테스트 - `@SpringBootTest`

계층별 테스트는 각자 따로 검증합니다. **서로 제대로 연결되어 동작하는지**는 핵심 시나리오 몇 개만 통합 테스트로 확인합니다.

```java
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CouponIssueFlowTest {

  @Autowired MockMvc mockMvc;
  @Autowired CouponRepository couponRepository;

  private void issue(Long couponId, long memberId, int expectedStatus) throws Exception {
    mockMvc.perform(post("/api/coupons/{id}/issue", couponId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"memberId\": " + memberId + "}"))
        .andExpect(status().is(expectedStatus));
  }

  @Test
  @DisplayName("1장짜리 쿠폰 : 첫 회원은 성공, 같은 회원은 중복, 다른 회원은 소진")
  void issueFlow() throws Exception {
    Coupon coupon = couponRepository.save(
        new Coupon("선착순 1명", 3000, 1, LocalDateTime.now().plusDays(7)));
    Long id = coupon.getId();

    issue(id, 10L, 201);        // 성공
    issue(id, 10L, 409);        // 이미 발급 (COUPON_003)
    issue(id, 11L, 409);        // 수량 소진 (COUPON_004)

    assertThat(couponRepository.findById(id).orElseThrow().getIssuedQuantity()).isEqualTo(1);
  }
}
```

- `@SpringBootTest` : 스프링 **전체**를 띄워 Controller → Service → Repository → DB를 실제로 연결
- `@AutoConfigureMockMvc` : 전체 컨텍스트에서도 `MockMvc`를 쓸 수 있게 함
- 이 테스트는 느리므로 **핵심 시나리오에만** 씁니다. 규칙 하나하나는 위의 계층별 테스트가 이미 검증했습니다.
- 만료일을 `LocalDateTime.now().plusDays(7)`로 상대값으로 잡아 **실행 날짜에 따라 깨지지 않게** 했습니다.

---

## 8. 계층별 한눈에 비교

| | Repository | Service | Controller | 통합 |
|---|---|---|---|---|
| **테스트 어노테이션** | `@DataJpaTest` | `@ExtendWith(MockitoExtension.class)` | `@WebMvcTest` | `@SpringBootTest` |
| **로드하는 것** | JPA 빈 + 임베디드 DB | 없음 (스프링 없음) | MVC 빈 | 전체 |
| **Mock 대상** | 없음 | `@Mock` Repository | `@MockitoBean` Service | 없음 |
| **주 검증 도구** | AssertJ | `assertThatThrownBy`, `verify` | `MockMvc`, `jsonPath` | `MockMvc` + Repository 조회 |
| **검증 대상** | 쿼리 (조건·경계값) | 규칙·분기·예외·부작용 | HTTP 계약 (상태·JSON·에러 응답) | 계층 간 연결 |
| **중요하게 볼 것** | 경계값, 반환 행 수, 조건 조합 | 실패 경로, `never()`, 시간 고정 | 에러 코드, 인자 전달, 입력 검증 | 핵심 시나리오만 |
| **검증하지 않는 것** | 비즈니스 규칙 | 쿼리 정확성, 트랜잭션 | 비즈니스 로직 | 세부 규칙 |
| **개수** | 적당히 | 가장 많이 | 적당히 | 가장 적게 |

### 한 가지 원칙

> **각 계층은 자기 책임만 검증한다.**
> Controller 테스트에서 "수량 소진 판단"을 검증하지 않고, Service 테스트에서 SQL을 검증하지 않습니다. 같은 것을 여러 계층에서 중복 검증하면 하나가 바뀔 때 테스트가 줄줄이 깨집니다.

---

## 9. 실행 결과

```bash
./gradlew test
```

| 테스트 클래스 | 계층 | 테스트 수 | 결과 |
|---|---|---|---|
| `CouponRepositoryTest` | Repository | 4 | ✅ |
| `CouponServiceTest` | Service | 6 | ✅ |
| `CouponControllerTest` | Controller | 6 (파라미터 4 포함) | ✅ |
| `CouponIssueFlowTest` | 통합 | 1 | ✅ |

> Spring Boot 4에서는 테스트 모듈이 분리되어 import 경로가 달라졌습니다.
> ```java
> import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;                 // Boot 3 : ...test.autoconfigure.orm.jpa.DataJpaTest
> import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;                    // Boot 3 : ...test.autoconfigure.web.servlet.WebMvcTest
> import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
> import org.springframework.test.context.bean.override.mockito.MockitoBean;              // Spring 6.2+ (@MockBean 의 후속)
> ```

---

## 10. 정리

- TDD는 **Red(실패 확인) → Green(최소 구현) → Refactor(정리)** 의 반복이고, 규칙이 많은 기능일수록 **요구사항을 테스트로 먼저 확정**하는 효과가 큽니다.
- 계층마다 **도구와 검증 대상이 다릅니다.**
  - Repository → `@DataJpaTest` : **쿼리**의 조건과 경계값, 변경된 행 수
  - Service → Mockito : **규칙·예외**, 실패 시 부작용이 없는지(`never()`)
  - Controller → `@WebMvcTest` + `MockMvc` : **HTTP 계약** (상태 코드 + 에러 코드, 입력 검증, 인자 전달)
  - 통합 → `@SpringBootTest` : 계층 연결을 **핵심 시나리오만**
- **Red의 실패 메시지**는 현재 코드의 동작을 알려줍니다. (`expected: 0 but was: 1`, `result is null`, 우연히 맞은 404)
- 정상 케이스만이 아니라 **실패 경로와 경계값**을 함께 테스트합니다.
- 시간처럼 변하는 값은 **외부에서 주입**(`Clock`)하고, 동시성이 걸린 로직은 **하나의 SQL로** 표현해서 Repository 테스트로 검증합니다.

### 다음에 학습할 것

- **동시성 테스트** : `ExecutorService` + `CountDownLatch`로 100명이 동시에 요청해도 초과 발급이 없는지 확인
- Outside-in 방식으로 Controller부터 시작하는 TDD
- Testcontainers : 실제 DB(PostgreSQL)로 Repository 테스트
- JaCoCo : 커버리지 측정
