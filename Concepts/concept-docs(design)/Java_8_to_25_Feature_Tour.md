# Java 8 → 25: The Complete Feature Tour

> A senior-interview reference. Organized by **LTS milestone**, because that's how the industry (and interviewers) actually think.

## The mental model first (WHY this layout)

Since Java 9, Oracle ships a new version **every 6 months**. Most are "stepping stone" releases nobody runs in production. Every 2–3 years one is blessed as **LTS (Long-Term Support)** — the one companies actually deploy and support for years.

The LTS line is: **8 → 11 → 17 → 21 → 25**.

So the smart way to remember 17 versions is not "what came in 12, what came in 13..." but **which LTS a feature landed in**. That's the story below. Non-LTS versions are folded in as "the road to" each LTS.

Think of it like a food-delivery app's release train: you push tiny builds weekly, but only a few become the "stable" versions you tell customers to install. Same idea.

---

# 🟢 Java 8 (2014 LTS) — The Functional Revolution

The single biggest release in Java's history. Before this, Java was purely object-oriented and verbose. Java 8 bolted on **functional programming** and changed how everyone writes code.

### 1. Lambda expressions
**WHY:** Before, to pass "a piece of behaviour" you wrote a whole anonymous class. Painful for one line of logic.

```java
List<String> restaurants = List.of("Meghana", "Truffles", "Empire");

// Old way — anonymous class, 5 lines for a comparison
restaurants.sort(new Comparator<String>() {
    public int compare(String first, String second) {
        return first.length() - second.length();
    }
});

// Java 8 — lambda, one line
restaurants.sort((first, second) -> first.length() - second.length());
```

A lambda is just **an anonymous function you can pass around like a value**. `(input) -> output`.

### 2. Streams API
**WHY:** You used to write `for` loops for *everything* — filter, transform, collect. Streams let you describe **what** you want, not **how** to loop.

```java
List<Order> orders = orderService.findAll();

// "Total revenue from delivered orders above ₹500"
double revenue = orders.stream()
        .filter(order -> order.getStatus() == DELIVERED)   // keep only delivered
        .filter(order -> order.getAmount() > 500)          // keep only big ones
        .mapToDouble(Order::getAmount)                     // extract the number
        .sum();                                            // add them up
```

Read it top to bottom like an assembly line. Each stage passes items to the next.

**Interview angle:** know that streams are *lazy* (nothing runs until a terminal operation like `sum()`/`collect()`), and that `filter`/`map` are *intermediate* operations.

### 3. Method references
Shorthand for a lambda that just calls one method. `Order::getAmount` means `order -> order.getAmount()`.

### 4. `Optional<T>`
**WHY:** To kill the `NullPointerException`. Instead of returning `null` and hoping the caller checks, you return a box that clearly says "might be empty."

```java
Optional<Customer> found = customerRepo.findByEmail(email);

String name = found
        .map(Customer::getName)         // if present, get the name
        .orElse("Guest");               // if empty, fall back
```

### 5. Default methods on interfaces
**WHY:** So the JDK could add new methods (like `List.sort`) to existing interfaces **without breaking every class that implemented them.** You provide a `default` body right in the interface.

### 6. New Date/Time API (`java.time`)
**WHY:** The old `Date` and `Calendar` were mutable, thread-unsafe, and confusing (month 0 = January 🤦). The new API is immutable and readable.

```java
LocalDate today = LocalDate.now();
LocalDate deliveryDate = today.plusDays(2);        // clean, immutable
LocalDateTime slot = LocalDateTime.of(2026, 8, 20, 13, 30);
```

**Key Takeaways (Java 8):** Lambdas + Streams + Optional + `java.time`. If you know only four things about Java 8, know these four.

---

# 🟡 The Road to 11 (Java 9 & 10)

### Java 9 (2017)
- **Module System (JPMS / "Project Jigsaw"):** lets you split a huge app into modules that explicitly declare what they expose (`module-info.java`). Powerful but heavy — most apps still don't use it. Interviewers rarely go deep here.
- **Collection factory methods:** `List.of(...)`, `Set.of(...)`, `Map.of(...)` — create small immutable collections in one line.
- **`JShell`:** a REPL (Read-Eval-Print Loop) to try Java snippets in the terminal, like Python's shell.
- Private methods allowed inside interfaces.

### Java 10 (2018)
- **`var` — local variable type inference.** The compiler figures out the type; you skip writing it twice.

```java
var orders = new ArrayList<Order>();        // compiler knows it's ArrayList<Order>
var total   = 0.0;                          // it's a double
```
**Gotcha nobody tells you:** `var` is *not* dynamic typing. The type is fixed at compile time — you just didn't type it out. It only works for **local variables**, never fields or method parameters.

---

# 🟢 Java 11 (2018 LTS) — The Cleanup Release

The first LTS after 8. Less flashy, mostly quality-of-life polish. This is still the "safe old baseline" many enterprises sat on for years.

### 1. New `String` methods
```java
"  ".isBlank();              // true  — blank = empty or only whitespace
"  hi  ".strip();            // "hi"  — Unicode-aware trim()
"ha".repeat(3);              // "hahaha"
"line1\nline2".lines();      // Stream of lines
```

### 2. Standard HTTP Client (`java.net.http`)
**WHY:** The old `HttpURLConnection` was clunky. This one supports HTTP/2, async, and reads like modern code — no need to pull in Apache HttpClient for simple calls.

```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.swiggy.com/menu"))
        .build();

HttpResponse<String> response =
        client.send(request, HttpResponse.BodyHandlers.ofString());

System.out.println(response.body());
```

### 3. Run a single `.java` file directly
`java Hello.java` — no separate `javac` compile step for quick scripts.

### 4. `var` in lambda parameters
Small: `(var first, var second) -> ...` — mainly useful when you want to add annotations.

**Key Takeaways (Java 11):** New HTTP client and the handy `String` methods are the two that come up in interviews.

---

# 🟡 The Road to 17 (Java 12 → 16) — The Incubator Years

This stretch is where the *modern* language features cooked. Each shipped as "preview" first, then got finalized. Grouping them by what they became:

### Switch expressions (final in Java 14)
**WHY:** The old `switch` was statement-only, needed `break` everywhere (forget one → bug), and couldn't return a value.

```java
// New: switch as an EXPRESSION that returns a value, with arrow syntax
int prepTimeMinutes = switch (foodCategory) {
    case BIRYANI, THALI      -> 30;
    case STARTER, BEVERAGE   -> 10;
    case DESSERT             -> 5;
    default                  -> 15;
};
```
No `break`. No fall-through bugs. Returns a value. Multiple labels per branch.

### Text blocks (final in Java 15)
**WHY:** Multi-line strings (JSON, SQL, HTML) used to be a mess of `\n` and `+`.

```java
String query = """
        SELECT name, amount
        FROM orders
        WHERE status = 'DELIVERED'
        """;
```
Triple quotes. What you see is what you get.

### Records (final in Java 16)
**WHY:** To kill boilerplate. A plain data-carrier class needed ~40 lines: fields, constructor, getters, `equals`, `hashCode`, `toString`. A record is **one line**.

```java
public record MenuItem(String name, double price, boolean vegetarian) { }
```
That single line auto-generates the constructor, accessors (`name()`, `price()`...), `equals`, `hashCode`, and `toString`. Records are **immutable** — perfect for DTOs and value objects.

```java
var paneer = new MenuItem("Paneer Tikka", 280.0, true);
paneer.price();      // 280.0  (accessor is price(), not getPrice())
```

### Pattern matching for `instanceof` (final in Java 16)
**WHY:** To remove the redundant cast after a type check.

```java
// Old: check, then cast the same thing again
if (payment instanceof CardPayment) {
    CardPayment card = (CardPayment) payment;
    charge(card.getCardNumber());
}

// New: check and bind in one shot
if (payment instanceof CardPayment card) {   // 'card' is ready to use
    charge(card.getCardNumber());
}
```

### Also in this window
- **`Stream.toList()`** (Java 16) — shortcut for `.collect(Collectors.toList())`.
- **Helpful NullPointerExceptions** (Java 14) — the message now tells you *exactly which* variable was null.

---

# 🟢 Java 17 (2021 LTS) — The Modern Baseline

The LTS everyone finally jumped to from 8/11. If a codebase says "Java 17," it means it has records, text blocks, and modern switch. Two headline features finalized here:

### 1. Sealed classes
**WHY:** To control **who is allowed to extend/implement** your type. Great for modelling a fixed set of options (a closed hierarchy).

```java
// Only these three payment types are allowed. Nobody can add a fourth.
public sealed interface Payment permits CardPayment, UpiPayment, CashPayment { }

public final class CardPayment implements Payment { /* ... */ }
public final class UpiPayment  implements Payment { /* ... */ }
public final class CashPayment implements Payment { /* ... */ }
```
**Why it matters:** the compiler now *knows* the complete list. Combined with switch (next), it can warn you if you forgot to handle a case. That's compile-time safety you can't get from a normal interface.

### 2. Pattern matching for `switch` (preview here, final in 21)
Switch that matches on **type**, not just value:

```java
String describe(Payment payment) {
    return switch (payment) {
        case CardPayment card -> "Card ending " + card.lastFour();
        case UpiPayment  upi  -> "UPI: " + upi.vpa();
        case CashPayment cash -> "Cash on delivery";
        // no default needed — sealed type means these are ALL the cases
    };
}
```

**Key Takeaways (Java 17):** Sealed classes + pattern-matching switch = you can model a fixed set of cases and let the compiler enforce you handled every one. This is the "algebraic data types" pattern people rave about.

---

# 🟡 The Road to 21 (Java 18 → 20)

Mostly previews maturing toward the big 21 release. Worth knowing:
- **UTF-8 is the default charset** (Java 18) — no more platform-dependent encoding surprises.
- **Simple Web Server** (Java 18) — `jwebserver` command for a quick static file server during dev.
- **Virtual threads, structured concurrency, scoped values, record patterns** all previewed here and landed properly in 21. See below.

---

# 🟢 Java 21 (2023 LTS) — The Concurrency Revolution

The most important LTS since 8. If Java 8 changed how you write logic, **Java 21 changed how you handle scale.** This is *the* version to know cold for senior interviews.

### 1. Virtual Threads (Project Loom) ⭐ THE BIG ONE
**WHY:** A traditional Java thread maps 1:1 to a heavy OS thread (~1 MB stack each). So you could only run a few thousand before the machine choked. For a server handling 50,000 concurrent requests that mostly *wait* on the DB/network, that's a disaster — you needed complex async/reactive code (WebFlux, callbacks) to survive.

**Virtual threads** are lightweight threads managed by the JVM, not the OS. You can run **millions**. They're cheap because when one blocks on I/O, the JVM parks it and reuses the underlying OS thread for someone else.

```java
// Old: fixed pool, limited to a few hundred/thousand
ExecutorService oldPool = Executors.newFixedThreadPool(200);

// Java 21: one virtual thread PER TASK — spin up as many as you like
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (Order order : incomingOrders) {         // could be 1,00,000 orders
        executor.submit(() -> processOrder(order));
    }
}   // try-with-resources waits for all to finish
```

```java
// Fire a single one directly
Thread.ofVirtual().start(() -> sendNotification(customer));
```

**The killer point for interviews:** virtual threads let you write **simple, blocking, synchronous code** (easy to read and debug) while getting the scalability that previously *required* reactive frameworks. You write `orderRepo.save(order)` normally — no `Mono`/`Flux` gymnastics — and it still scales. This is why Loom is a huge deal: it may make a lot of reactive code unnecessary.

**Gotcha:** don't pool virtual threads (they're cheap, create fresh ones). And watch for "pinning" — holding a `synchronized` block during I/O can pin the virtual thread to its carrier and kill the benefit. Prefer `ReentrantLock`.

### 2. Record patterns
**WHY:** To *destructure* a record — pull its fields out directly in a pattern match.

```java
record Location(double lat, double lng) { }
record Delivery(String orderId, Location location) { }

String describe(Object obj) {
    // Match the shape AND unpack lat/lng in one line
    if (obj instanceof Delivery(String orderId, Location(double lat, double lng))) {
        return orderId + " heading to " + lat + "," + lng;
    }
    return "unknown";
}
```

### 3. Pattern matching for switch (finalized)
Now standard (previewed in 17). Plus **guarded patterns** with `when`:

```java
String priority = switch (order) {
    case Order o when o.getAmount() > 5000 -> "VIP";     // extra condition
    case Order o when o.isExpress()        -> "Fast";
    case Order o                           -> "Normal";
};
```

### 4. Sequenced Collections
**WHY:** Java had no clean, uniform way to get the first/last element across `List`, `Deque`, `LinkedHashSet`. Now there's a common interface.

```java
List<String> deliveryQueue = new ArrayList<>(List.of("A", "B", "C"));

deliveryQueue.getFirst();      // "A"   (no more get(0))
deliveryQueue.getLast();       // "C"   (no more get(size()-1))
deliveryQueue.reversed();      // view in reverse order
deliveryQueue.addFirst("Z");   // push to front
```

**Key Takeaways (Java 21):** **Virtual threads** is the headline — understand *why* they matter (blocking code that scales, potentially replacing reactive). Record patterns + guarded switch round out the pattern-matching story.

---

# 🟡 The Road to 25 (Java 22 → 24)

### Java 22 (2024)
- **Unnamed variables & patterns (`_`):** use underscore for a variable you're forced to declare but don't care about.
  ```java
  try { riskyCall(); }
  catch (Exception _) { log.warn("failed, ignoring"); }   // don't need the exception object
  ```
- **Foreign Function & Memory API (finalized):** the modern, safe replacement for JNI to call native C code and access off-heap memory.

### Java 23 (2024)
- **Markdown in Javadoc:** write doc comments in Markdown (`///`) instead of clunky HTML tags.

### Java 24 (2025)
- **Stream Gatherers (finalized):** custom intermediate stream operations. You could always write your own *terminal* collector (`Collectors`), but never a custom *middle* stage. Gatherers fix that — think "build your own `map`/`filter`-style step."
  ```java
  // Group orders into fixed-size batches of 3 for bulk processing
  List<List<Order>> batches = orders.stream()
          .gather(Gatherers.windowFixed(3))
          .toList();
  ```
- **Class-File API (finalized):** a standard JDK API to read/write `.class` bytecode (replaces the old ASM library dependency for tooling).

---

# 🟢 Java 25 (2025 LTS) — The On-Ramp + Polish

Released **16 September 2025**, the current LTS after 21. Theme: make Java **easier to start** (for beginners) and **cleaner to write** (for experts). 18 JEPs total; here are the ones that matter.

### 1. Compact Source Files & Instance `main` Methods ⭐
**WHY:** To fix Java's embarrassing "Hello World." A beginner shouldn't need to understand `public`, `static`, `String[] args`, and class declarations just to print one line.

```java
// Java 25 — this is a COMPLETE, runnable program. No class. No static. No String[] args.
void main() {
    IO.println("Order received!");
}
```
- No enclosing class needed (compiler wraps it for you).
- `main` can be an **instance** method, and can take **no args**.
- New `java.lang.IO` class: `IO.println(...)` / `IO.readln(...)` — simple console I/O without `System.out` / `Scanner`.

For a senior, the value isn't Hello World — it's that **small scripts and prototypes** get way less ceremony.

### 2. Module Import Declarations
**WHY:** Cut down import spam. One line imports everything a whole module exports.

```java
import module java.base;    // now List, Map, ArrayList, etc. are all available
```

### 3. Flexible Constructor Bodies
**WHY:** Earlier, `super(...)` or `this(...)` had to be the *very first* statement in a constructor. So you couldn't validate arguments *before* calling the parent — you'd construct the parent, then discover the input was garbage.

```java
public class ExpressOrder extends Order {
    private final int distanceKm;

    public ExpressOrder(int distanceKm) {
        // NOW allowed: validate BEFORE calling super()
        if (distanceKm > 15) {
            throw new IllegalArgumentException("Too far for express");
        }
        super(OrderType.EXPRESS);        // super() no longer has to be line 1
        this.distanceKm = distanceKm;
    }
}
```

### 4. Scoped Values (finalized)
**WHY:** A safer, faster replacement for `ThreadLocal` — and it plays nicely with virtual threads (where millions of `ThreadLocal`s would be wasteful). You bind a value for the duration of a task and everything called inside can read it, immutably.

```java
private static final ScopedValue<String> CURRENT_USER = ScopedValue.newInstance();

ScopedValue.where(CURRENT_USER, "bala@example.com")
        .run(() -> processCheckout());   // anything called in here can read CURRENT_USER.get()
```

### 5. Still in Preview (know the names, don't rely on them yet)
- **Primitive types in patterns** (3rd preview) — pattern matching with `int`, `double`, etc.
- **Structured Concurrency** (5th preview) — treat a group of related concurrent tasks as one unit (if one fails, cancel the rest). Pairs with virtual threads.
- **Stable Values** (preview) — deferred-init immutable holders (like a safe, lazy `final`).

**Key Takeaways (Java 25):** Instance `main` + `IO` (lower the on-ramp), flexible constructors (validate before `super`), and scoped values (the `ThreadLocal` successor for the virtual-thread era).

---

# 📋 One-Page Cheat Sheet

| Feature | Landed (final) | One-line reason |
|---|---|---|
| Lambdas, Streams | 8 | Functional style, no more boilerplate loops |
| Optional | 8 | Kill NullPointerException |
| `java.time` | 8 | Sane, immutable date/time |
| `var` | 10 | Skip repeating the type |
| HTTP Client | 11 | Modern HTTP/2 client built in |
| Switch expressions | 14 | Switch returns a value, no `break` bugs |
| Text blocks | 15 | Clean multi-line strings |
| Records | 16 | One-line immutable data class |
| Pattern matching `instanceof` | 16 | Check + cast in one step |
| Sealed classes | 17 | Lock down who can extend |
| Pattern matching `switch` | 21 | Match on type, with `when` guards |
| Record patterns | 21 | Destructure records in a match |
| **Virtual threads** | **21** | **Blocking code that scales to millions** |
| Sequenced Collections | 21 | Uniform `getFirst()`/`getLast()` |
| Stream Gatherers | 24 | Custom intermediate stream steps |
| Instance `main` / `IO` | 25 | Java without the ceremony |
| Flexible constructors | 25 | Validate before `super()` |
| Scoped Values | 25 | `ThreadLocal` successor for virtual threads |

---

## P.S. — Jargon decoder

- **LTS (Long-Term Support):** a release companies get years of updates for. The "production-grade" versions.
- **JEP (Java Enhancement Proposal):** the spec document for each new feature. Interviewers sometimes drop JEP numbers to sound fancy; you rarely need them.
- **Preview feature:** finished but not permanent — you must enable it with a flag, and it might change. Don't build production on preview.
- **Terminal vs intermediate (streams):** intermediate ops (`filter`, `map`) are lazy and return another stream; terminal ops (`collect`, `sum`, `toList`) actually run the pipeline.
- **Immutable:** can't be changed after creation. Records and `java.time` types are immutable.
- **Pinning (virtual threads):** when a virtual thread gets stuck to its OS carrier thread (e.g., inside `synchronized` during I/O), losing the scalability benefit.

---

### If I were prepping *you* for the Coforge round

Interviewers love three threads here:
1. **"Why virtual threads over reactive/WebFlux?"** — blocking code that's readable *and* scales; potential to simplify the reactive stack.
2. **"Records + sealed + pattern-matching switch"** — how they combine into compiler-enforced exhaustive handling (algebraic data types).
3. **"What's the difference between `var` and a dynamically-typed language?"** — a favourite trap. Answer: none at runtime; `var` is pure compile-time inference, type is fixed.

Want me to go deep on any one of these with the full WHY → concept → code → gotcha treatment? Virtual threads is the one I'd drill hardest for a Lead role.
