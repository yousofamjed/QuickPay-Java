# Day 2 — Testing with Mockito & MockMvc

## Goal

Test the Payment Service's two layers in isolation — the service layer with **Mockito** (mocking the
repository), and the web layer with **MockMvc** (mocking the service) — and understand why each test
mocks the *one layer directly below* what it's actually testing, never the database itself.

## Why it matters

A test that hits a real database is slow, and it's testing two things at once (your logic *and* the
database) instead of one. Mocking the layer underneath lets you test `PaymentService`'s logic with
zero database involved, and test `PaymentController`'s HTTP handling with zero service logic
involved — each test fails for exactly one reason, not several tangled together.

## Concept walkthrough

### 1. What Mockito actually does

Mockito creates a **fake implementation** of an interface (or class) at runtime — an object that
looks real to your code, but does nothing unless you tell it to, and remembers every call made to it.

```java
PaymentRepository mockRepository = Mockito.mock(PaymentRepository.class);

Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(somePayment));
```

`when(...).thenReturn(...)` scripts the fake's behavior: "when `findById(1L)` is called, return this
specific value" — no real database, no real query, just a pre-programmed answer.

### 2. `@Mock` and `@InjectMocks`

Writing `Mockito.mock(...)` by hand for every dependency gets repetitive. Annotations do it for you:

```java
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void createSavesAndReturnsThePayment() {
        Payment input = new Payment();
        input.setAmount(new BigDecimal("100.00"));

        Payment saved = new Payment();
        saved.setId(1L);
        saved.setAmount(new BigDecimal("100.00"));

        Mockito.when(paymentRepository.save(input)).thenReturn(saved);

        Payment result = paymentService.create(input);

        assertEquals(1L, result.getId());
        Mockito.verify(paymentRepository).save(input);
    }
}
```

- **`@Mock`** — creates the fake `PaymentRepository`.
- **`@InjectMocks`** — creates a real `PaymentService`, and injects the `@Mock`s into its
  constructor automatically. `PaymentService` itself is **not** mocked — you're testing its real
  code, just with a fake repository underneath it.
- **`verify(paymentRepository).save(input)`** — asserts that `save` was actually called with that
  exact argument, not just that the method returned the right thing. Useful when the point of the
  test is "did this side effect happen," not just "was the return value right."

No Spring context, no database, no application starting — this test runs in milliseconds.

### 3. What MockMvc actually does

Mockito mocks *objects*. **MockMvc** is different — it simulates the **HTTP layer** itself: real
request routing, real JSON serialization/deserialization, real status codes — without starting an
actual embedded server or opening a real port.

```java
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void createReturns201() throws Exception {
        Payment saved = new Payment();
        saved.setId(1L);
        saved.setAmount(new BigDecimal("100.00"));

        Mockito.when(paymentService.create(Mockito.any())).thenReturn(saved);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 100.00}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100.00));
    }
}
```

- **`@WebMvcTest(PaymentController.class)`** — loads *only* the web layer for this one controller
  (routing, JSON conversion, validation) — not the full application, not JPA, not a real database.
  Much faster than `@SpringBootTest`, and it fails for web-layer reasons only.
- **`@MockBean`** — replaces `PaymentService` in the (partial) Spring context with a Mockito mock.
  This is the same mocking mechanism as `@Mock`, just registered into Spring's context instead of
  created by hand.
- **`mockMvc.perform(...)`** — simulates an actual HTTP request (method, path, headers, body)
  through Spring MVC's real routing and serialization, without a real server running.
- **`andExpect(...)`** — asserts against the real HTTP response: status code, JSON body via
  `jsonPath("$.field")`.

### 4. The rule that ties both together: mock the layer directly below

| Testing this | Mock this | Tool |
|---|---|---|
| `PaymentService` | `PaymentRepository` | Mockito (`@Mock`/`@InjectMocks`) |
| `PaymentController` | `PaymentService` | MockMvc + `@MockBean` |

Never mock the layer you're actually testing. Never reach past the layer directly underneath
either — a controller test shouldn't need to know `PaymentRepository` exists at all; it only needs
`PaymentService`'s behavior scripted.

## Core exercises

Work against the Payment Service from Phase 1 (`create`/view/list, backed by H2).

### Exercise 1 — Unit test `PaymentService.create`

Using `@Mock`/`@InjectMocks`, mock `PaymentRepository.save(...)` and confirm `PaymentService.create(...)`
returns what the repository gave back. Verify `save` was actually called with the payment you passed in.

### Exercise 2 — Unit test the view-by-id case

Mock `PaymentRepository.findById(...)` returning an `Optional` with a payment, and confirm the
service returns it correctly. Then mock it returning `Optional.empty()` and confirm your service
handles that case the way it's supposed to (however you currently signal "not found").

### Exercise 3 — Unit test the list case

Mock `PaymentRepository.findAll()` returning a small fixed list, and confirm the service returns
exactly that list back, untouched.

### Exercise 4 — `@WebMvcTest` for `POST /api/payments`

Using `@WebMvcTest`/`MockMvc`/`@MockBean`, mock `PaymentService.create(...)` and confirm:
- The endpoint returns `201 Created`.
- The JSON response body contains the fields you expect (`jsonPath`).

### Exercise 5 — `@WebMvcTest` for the two `GET` endpoints

Same pattern: mock `PaymentService`'s list/view methods, confirm `GET /api/payments` and
`GET /api/payments/{id}` return `200 OK` with the expected JSON shape.

## Stretch exercises (if you finish early)

- Write a test where the mocked repository throws an exception, and confirm your service either
  propagates it or handles it — whichever your current code actually does; the point is knowing
  which, not which is "correct" yet (that's a later topic).
- Use `Mockito.verify(paymentRepository, Mockito.times(1))` explicitly, and deliberately call the
  method twice in your test code to watch the verification fail — confirms you understand what
  `verify` is actually counting.

## Done checklist

- [ ] I can explain what a mock actually is — a fake object that does nothing unless scripted.
- [ ] I can explain the difference between `@Mock` and `@InjectMocks`.
- [ ] I can explain what `@WebMvcTest` loads (and doesn't load) compared to the full application.
- [ ] I can explain why `@MockBean` is needed in a `@WebMvcTest` for a controller with a service
      dependency.
- [ ] I can explain the rule "mock the layer directly below the one you're testing," and why a
      controller test shouldn't reach past the service to mock the repository.
- [ ] `PaymentService`'s create/view/list methods each have a passing Mockito-based unit test, with
      no Spring context and no real database involved.
- [ ] `PaymentController`'s three endpoints each have a passing `@WebMvcTest`/MockMvc test.

## Suggested resources

- Mockito official documentation — https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html
- Spring's testing reference, `@WebMvcTest` — https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html#testing.spring-boot-applications.spring-mvc-tests
- Baeldung, "Mockito with JUnit" — https://www.baeldung.com/mockito-junit-5-extension
