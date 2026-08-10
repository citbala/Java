# Java 8 Stream – `filter` & `reduce` Explained

**Context:** Removing trailing `"N"` values from a level-order binary tree list.

```java
private static List<String> removeTrailingNullValues(List<String> list) {
    int lastNonNullIndex = IntStream.range(0, list.size())
            .filter(index -> !"N".equals(list.get(index)))
            .reduce((first, second) -> second)
            .orElse(-1);

    return list.subList(0, lastNonNullIndex + 1);
}
```

---

## 1. `filter` Operation

### What it does
`filter` keeps only the elements that satisfy a condition.  
In this case, we are filtering **indexes**, not the actual values.

```java
IntStream.range(0, list.size())
         .filter(index -> !"N".equals(list.get(index)))
```

### Step-by-step with example

**Original list:**
```
["1", "3", "2", "N", "N", "5", "4", "N", "N"]
```

| Index | Value | Kept by filter? |
|-------|-------|-----------------|
| 0     | "1"   | Yes             |
| 1     | "3"   | Yes             |
| 2     | "2"   | Yes             |
| 3     | "N"   | No              |
| 4     | "N"   | No              |
| 5     | "5"   | Yes             |
| 6     | "4"   | Yes             |
| 7     | "N"   | No              |
| 8     | "N"   | No              |

After `filter` we get a stream of indexes:

```
0, 1, 2, 5, 6
```

> **Important:** `filter` does **not** modify the original list.  
> It only creates a temporary stream of indexes that point to non-`"N"` values.

---

## 2. `reduce` Operation

### What it does
`reduce` combines all elements of a stream into a **single result** by repeatedly applying a binary operator.

```java
.reduce((first, second) -> second)
```

The lambda `(first, second) -> second` means:

> Always keep the second (later) value and discard the first.

### Step-by-step reduction

```
Stream after filter:  0, 1, 2, 5, 6
```

1. Take `0` and `1` → keep `1`
2. Take `1` and `2` → keep `2`
3. Take `2` and `5` → keep `5`
4. Take `5` and `6` → keep `6`

**Final result:** `6` (the last non-`"N"` index)

**Visual flow:**
```
0 → 1 → 2 → 5 → 6
```
Because we always choose the later value, we end up with the last index.

---

## 3. Why middle `"N"`s survive

Many people expect the middle `"N"`s to disappear, but they don’t — and that’s intentional.

- `filter` only helps us find the **last good index**.
- We then do `list.subList(0, lastNonNullIndex + 1)`.
- This keeps everything from the beginning up to that index (including middle `"N"`s).

**After processing:**
```
["1", "3", "2", "N", "N", "5", "4"]
```
Trailing `"N"`s are removed. Middle ones stay.

---

## 4. Complete working method

```java
private static List<String> removeTrailingNullValues(List<String> list) {
    int lastNonNullIndex = IntStream.range(0, list.size())
            .filter(index -> !"N".equals(list.get(index)))
            .reduce((first, second) -> second)
            .orElse(-1);

    return list.subList(0, lastNonNullIndex + 1);
}
```

---

## 5. Common mistake (Effectively Final error)

```java
list = list.subList(...);   // ← This reassignment causes the error
```

Variables used inside a lambda must be **final** or **effectively final**.  
Reassigning `list` breaks that rule.

**Solution:** Return the new list instead of reassigning the parameter.

---

## Quick Summary

| Operation | Purpose in this code                          | Result                  |
|-----------|-----------------------------------------------|-------------------------|
| `filter`  | Keep only indexes of non-`"N"` values         | `0, 1, 2, 5, 6`         |
| `reduce`  | Find the last remaining index                 | `6`                     |
| `subList` | Cut the original list up to that index        | Removes trailing `"N"`s |
```

The Markdown file has been created at:

**`/home/workdir/artifacts/java8-filter-reduce-explained.md`**

You can download it from there. Let me know if you want any changes!