# MiniBank — Practicals 1–14

A Java 17 console banking project covering OOP, interfaces, packages, annotations/reflection, checked exceptions, concurrency, serialization/NIO, collections, generics, a repository pattern, tests, and runnable-JAR packaging.

## Requirements
- JDK 17 or newer
- Optional: Maven 3.8+ to run JUnit tests

## Project layout
- `src/model`: Account hierarchy, Customer, interfaces, annotations
- `src/exception`: checked banking exception hierarchy
- `src/util`: validation, repository, account store, persistence, reports, transaction processing
- `src/service`: menu, BankService, worker/buffer/session and audit demo
- `test`: JUnit 5 tests

## Compile and run on Windows PowerShell
Run these commands from the project root:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem -Recurse -Path src -Filter *.java).FullName
java -cp out service.MiniBank
```

## Build a runnable JAR
After compiling:

```powershell
jar cfm MiniBank.jar manifest.txt -C out .
java -jar MiniBank.jar
```

On macOS/Linux, compile with `mkdir -p out && javac -d out $(find src -name "*.java")`.

## Run tests
A Maven `pom.xml` is included. From the project root, run:

```bash
mvn test
```

## Menu
1. Open account
2. Deposit
3. Withdraw
4. Transfer
5. Statement
6. List accounts
7. Sort accounts by balance
8. End-of-day report
9. Thread-safety demo
10. Batch transaction demo
11. Save accounts
12. Exit (saves accounts)
13. Annotation validation demo
14. AutoCloseable demo
15. Print methods marked `@Audit`

The first launch seeds three sample accounts if no saved state exists. Runtime files are created in `data/` and `logs/` relative to the working directory. Keep these files private if they contain real information; this is a learning project, not production banking software.

## Notes
- Account deposits and withdrawals are synchronized to protect a shared account balance.
- The transaction pool uses four worker threads and a `ConcurrentHashMap` for its account registry.
- `Repository<T>` finds the key by walking the class hierarchy for a field annotated `@Id`.
- The producer-consumer `TransactionBuffer<T>` demonstrates `wait()`/`notifyAll()`; the opposite-order transfer deadlock is avoided in `Account.transfer` by ordering locks by account number.
- The menu's transaction batch is a demonstration and does not persist every batch operation into the audit log; use the regular BankService operations for logged transactions.
- This is educational sample code. Do not use it to store or move real money.
