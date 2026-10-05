# QuickPay

## Token discipline (follow on every task)
- This file is the project map. Trust it; don't re-explore the codebase to rediscover what's written here.
- Before reading, locate: `grep -rn` / `git ls-files | grep` / LSP (jdtls, typescript-lsp) for definitions and references. Read only the lines you need (`offset`/`limit`, `sed -n`), never whole large files "just in case".
- Never re-read a file you just wrote or edited. Never dump build/test logs: pipe through `tail -n 40` or `grep -E "ERROR|FAIL|Tests run"`.
- Run the narrowest check: one test class (`-Dtest=ClassName`), one module (`-pl`), `-q` flag. Full build only before declaring a multi-module change done.
- Batch independent tool calls in one turn. Use a subagent only for broad searches whose file dumps you don't need to keep.
- Replies: short, no restating the request, no recap of unchanged code, no option surveys — give a recommendation. Show diffs/snippets only when asked or when essential.
- If something here is wrong or stale, fix this file in the same change instead of working around it.

## What it is
Payment-initiation backend (ProgressSoft training). Users create single payments or bulk-upload CSV/TXT files; every payment is saved as `PENDING` (no status transitions, no update/delete). Admins manage bulk-file **templates** (column order, max length, required). A frontend is being built on top of it.

## Layout
- `payments/domain` — pure Java: `Payment`, use cases (`CreatePaymentUseCase`, `UploadBulkPaymentUseCase`), `ValidatorChain` + 6 validators (all run, violations aggregated), `PaymentRepository` port.
- `payments/application` — Spring Boot 4.1 / Java 25 / Jackson 3: `controller/`, `service/`, `repository/` (JPA adapters + Specifications), `mapper/` (MapStruct; trims strings, blank→null, amount scaled to 2dp), `config/` (bean wiring, validators), `security/SecurityConfig`, `exception/GlobalExceptionHandler`, Liquibase in `resources/db/changelog`.
- External lib `com.progressoft.training:file-parser-core` (source: `~/Java Projects/file-parser/file-parser-core`): `Template`/`FieldDefinition`, CSV/TXT parsers, row rules, template CRUD use cases, `FileParserException` hierarchy.
- `samples/` request bodies + bulk files; `postman/` collection.

## Commands
- Tests: `mvn -q test` (root) · one module: `mvn -q -pl payments/domain test` · one class: `mvn -q -pl payments/application test -Dtest=PaymentServiceTest`
- Package: `mvn -q -DskipTests package` (Dockerfile copies `payments/application/target/application-1.0.0-SNAPSHOT.jar`, so package before `docker compose up --build`).
- DB only: `docker compose -f local/docker-compose.yml up -d`; run app with profiles `postgres,liquibase` and `DB_NAME=paymentsdb` (fallback default is the typo `paymentsd`).
- Swagger: `/swagger-ui/index.html` (needs Basic auth).

## API facts (frontend contract)
- Auth: HTTP Basic, in-memory users `tolay/12345` (PAYMENT), `admin/56789` (PAYMENT+TEMPLATE). `/api/payments/**` any role; `/api/templates/**` TEMPLATE only. No login/me endpoint. **No CORS configured.**
- Payments: `POST /api/payments` (201), `GET /api/payments/{id}`, `GET /api/payments` (filters: id, currency, status exact; sender/receiver/notes/creditorName contains; createdFrom/createdTo ISO instant; min/maxAmount; `pageNumber` 0-based, `pageSize` 1–100, `sortBy`, `direction` asc|desc) → `{content,pageNumber,pageSize,totalElements,totalPages}`.
- Bulk: `POST /api/payments/bulk` multipart `file` + optional text part `metadata` `{"templateName":..}` (default `default`). Parser chosen by extension (.csv comma, .txt pipe); first non-empty line skipped; columns mapped by position. Always 200 → `{successCount,failureCount,validRows,failures:[{rowNumber,errors[]}]}`. `payments.persist-db-on-error=false` ⇒ any failure saves nothing (validRows ids null).
- Templates: CRUD at `/api/templates` keyed by **name** (response has no id); list excludes the read-only JSON `default` template (fetch via `/name/default`). Field names must be from: senderAccount, receiverAccount, amount, currency, notes, creditorName.
- Validation: sender/receiver required ≤34, must differ (case-insensitive); amount >0, ≤2dp; currency in `payments.allowed-currencies` (case-sensitive); creditorName = exactly two alphanumeric words; notes ≤ `payments.notes-max-length`. docker-compose overrides: currencies JOD,USD, notes 20, persist-on-error true.
- Errors: validation 400 JSON `[{message,violator}]`; parser/template errors (incl. template not found) 400 plain text; payment not found 404 text; paging/sort 400 text.

## MCP servers (`.mcp.json`)
- `context7` — current docs for Spring Boot 4, Jackson 3, MapStruct, frontend libs. Use instead of web search or guessing APIs; request a narrow topic.
- `postgres` (DBHub, read-only, max 100 rows, config `.claude/dbhub.toml`) — inspect local `paymentsdb`. Select only needed columns and add `LIMIT`.
- `angular-cli` (`ng mcp`) — Angular best practices, docs search, workspace/project info. Use it before writing Angular code; prefer it over context7 for Angular.
- LSP (jdtls, typescript-language-server via plugins) — use for go-to-definition, references, diagnostics instead of grepping/reading whole files.
- `playwright` — drive the frontend in a browser. Prefer accessibility snapshots over screenshots; screenshot only for visual/layout checks.

## Conventions
- Business rules live in `payments/domain`; Spring stays in `application`. New validators: implement `Validator<Payment>`, register in `ValidatorConfig`.
- Schema changes only via new Liquibase changesets; never edit applied ones.
- Commit only when asked.
