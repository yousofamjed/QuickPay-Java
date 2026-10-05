# QuickPay Frontend — Angular Conventions

Applies to the Angular workspace in `frontend/` (Angular v20+, targeting v22 defaults).
Claude Code follows these automatically via the `angular-conventions` skill
(`.claude/skills/angular-conventions/SKILL.md`) — keep the two in sync.

## Part A: Conventions

### 1. Component convention

#### Options compared

| Decision | Option | Pros | Cons |
|----------|--------|------|------|
| **Component system** | NgModule-based | Familiar from older tutorials; one place to declare many components | Deprecated direction since v17; extra boilerplate; harder lazy loading and tree-shaking |
| | **Standalone** ✅ | Default since v19/v20; each component declares its own imports; `loadComponent` lazy loading; what the CLI generates | Imports repeated per component |
| **Responsibility split** | No split (every component does everything) | Fastest to start | HTTP, state and markup mixed; components hard to reuse and test |
| | Smart/container vs dumb/presentational | Clear data flow; dumb components trivially testable | "Container" is vague — teams argue about which components qualify |
| | **Page / presentational** ✅ | Same benefits, but the boundary is objective: *routed = page* | Small pages may feel like extra files |
| **File naming** | `feature-name.component.ts`, class `FeatureNameComponent` | Type visible in the file name; matches older docs | Angular v20+ CLI no longer generates it — every `ng generate` would need renaming |
| | **`feature-name.ts`, class `FeatureName`** ✅ | Current Angular style guide and CLI default | Need another way to tell routed components apart → `-page` suffix |

#### Chosen and why

**Standalone only, page/presentational split, v20+ naming.** Standalone + new naming is what the
CLI and the official style guide produce, so we never fight the tooling. The page/presentational split
fits this backend well: pages own the calls to `/api/payments` / `/api/templates` and deal with its two error
shapes (400 JSON `[{message,violator}]` vs plain text); presentational components only ever see clean
typed data, so they can be tested by setting inputs alone.

#### Rules

- No `NgModule`s. Don't write `standalone: true` (default since v20) or `changeDetection: OnPush`
  (default since v22). Import exactly the directives/pipes the template uses — never `CommonModule`.

| Kind | Lives in | Responsibility | May inject services? | Talks via |
|------|----------|----------------|----------------------|-----------|
| **Page** (smart) | `features/<feature>/pages/` | Routed screen. Reads route params, calls services, owns feature state (signals), handles errors/navigation. | Yes | Services, router |
| **Presentational** (dumb) | `features/<feature>/components/` or `shared/ui/` | Renders data and emits user intent. No HTTP, no router, no global state. | No | `input()` / `output()` / `model()` |

- One page per route; pages are lazy-loaded via `loadComponent`.
- Presentational components never know *where* data comes from — e.g. `payment-table` gets
  `payments = input.required<Payment[]>()` and emits `pageChange = output<PageRequest>()`; the
  `payment-list-page` does the `GET /api/payments` call.
- If a presentational component starts needing a service, it is really a page (or the logic belongs in the page).
- Pipes and directives are imports, not injections — allowed in presentational components.

| Thing | File | Class | Selector |
|-------|------|-------|----------|
| Page | `payment-list-page.ts` | `PaymentListPage` | `qp-payment-list-page` |
| Presentational | `payment-table.ts` | `PaymentTable` | `qp-payment-table` |
| Template / styles | `payment-table.html` / `.scss` (same folder) | — | — |
| Test | `payment-table.spec.ts` | — | — |

- kebab-case files, one component per file, folder named after the component.
- Selector prefix `qp-` (set as `prefix` in `angular.json`).
- Inline template only when it's under ~10 lines; otherwise a sibling `.html`.
- `input()`, `output()`, `model()` — not `@Input`/`@Output`. `inject()` — not constructor injection.
- State in `signal()`, derived state in `computed()`; never `mutate`.
- Host bindings in the `host: {}` object — not `@HostBinding`/`@HostListener`.
- Templates: `@if` / `@for` / `@switch`; `[class.x]`/`[style.x]` (no `ngClass`/`ngStyle`); no complex logic.

### 2. Style convention

#### Options compared

| Option | Pros | Cons |
|--------|------|------|
| Plain CSS | No build step; native nesting and custom properties now cover a lot | No mixins, so Material's theming API (`mat.theme()`, override mixins) can't be used |
| **SCSS** ✅ | Superset of CSS; required for Angular Material theming; partials/mixins for tokens | One more syntax to learn (small) |
| Utility CSS (Tailwind) | Fast layout; no naming | Long class lists in templates; overlaps with Material's own styling; another tool to configure |
| No UI library (hand-built controls) | Full visual control | We'd have to rebuild tables, paginators, dialogs, form fields — and their accessibility |
| **Angular Material (M3)** ✅ | Official, versioned with Angular; accessible controls; `mat-table`/`mat-paginator`/`matSort` | Looks "Material" unless themed; must customize via its API, not CSS hacks |
| PrimeNG / other libraries | Big component catalog | Third-party release cycle vs Angular majors; its own theming system |

#### Chosen and why

**SCSS + Angular Material.** The screens are forms, tables and dialogs. `mat-paginator`'s 0-based
`pageIndex`/`pageSize` and `matSort`'s `{active, direction}` map 1:1 onto the backend's
`pageNumber`/`pageSize`/`sortBy`/`direction`. Material's controls come with keyboard/ARIA support toward our AXE +
WCAG AA requirement. SCSS is needed for Material's theming mixins.

#### Rules

Workspace created with `ng new --style=scss`, then `ng add @angular/material`.

| What | Where |
|------|-------|
| Theme (palette, typography, density) | `src/styles/_theme.scss`, applied once with `mat.theme()` in `styles.scss` |
| Design tokens (spacing, radii, extra colors) | CSS custom properties on `:root` in `src/styles/_tokens.scss` |
| Global resets / layout helpers | `src/styles.scss` only — keep it small |
| Everything else | The component's own `.scss` (emulated encapsulation, the default) |

- Colors come from Material system tokens (`var(--mat-sys-primary)`, `var(--mat-sys-error)`, …) or our
  `_tokens.scss` — no hard-coded hex values in component styles.
- Customize Material through its override mixins (e.g. `mat.button-overrides(...)`) in `_theme.scss` —
  never by targeting internal `.mat-mdc-*` / `.mdc-*` classes.
- No `::ng-deep`, no `!important`.
- Style the `:host` and the component's own elements; layout with flexbox/grid; kebab-case class
  names, scoped by encapsulation (no BEM prefixes needed).
- Material first: build a custom component only when Material has no equivalent (e.g. the bulk-upload
  file drop zone). Compose it from Material pieces + native elements, style it with the same system
  tokens, put it in `shared/ui/`, and give it the same keyboard/ARIA behavior a Material control would have.
- Status / validation colors (e.g. `PENDING` chip, row error highlight in bulk results) are tokens, so
  they stay consistent and contrast-checked in one place.

### 3. Directory convention

#### Options compared

| Option | Example | Pros | Cons |
|--------|---------|------|------|
| Type-based | `components/`, `services/`, `models/` at the top | Simple for tiny apps; obvious where "a service" goes | One feature spread across many folders; folders grow without limit; no boundary to lazy-load or delete a feature |
| **Feature-based + `core/` + `shared/`** ✅ | `features/payments/…`, `features/templates/…` | Everything for a feature in one place; one folder = one lazy route = one role guard; easy to delete or move | Needs a rule for when code becomes "shared" |
| Library-based monorepo (Nx libs) | `libs/payments/feature`, `libs/payments/data-access` | Enforced boundaries, per-lib builds | Heavy tooling for a two-feature app |

| Placement question | Options | Chosen |
|--------------------|---------|--------|
| Shared/reusable components | Top-level `components/`; per-feature copies; `shared/ui/` | **`shared/ui/`**, only once a second feature needs it |
| Services | Global `services/`; inside the feature | **Inside the feature** (`features/<f>/<f>-api.ts`); app-wide singletons in `core/` |
| Models (DTO types) | Global `models/`; inside the feature | **`features/<f>/models/`**; types the backend shares across endpoints (`Page<T>`, `ApiViolation`) in `shared/models/` |

#### Chosen and why

**Feature-based, with `core/` and `shared/`.** The backend already has two hard boundaries: PAYMENT vs
TEMPLATE role, `/api/payments/**` vs `/api/templates/**`. Mirroring that, each feature folder is
one lazy-loaded route tree behind one role guard. A frontend developer working on templates touches
`features/templates/` and nothing else. Nx-style libraries would enforce the same boundaries, but they cost more
tooling than a two-feature training app needs.

#### Layout

```
frontend/src/app/
  app.ts  app.routes.ts  app.config.ts
  core/                          # app-wide singletons, created once
    auth/                        #   auth state, Basic-auth interceptor, role guard
    http/                        #   API base URL, error-normalizing interceptor
    layout/                      #   shell: toolbar, nav (pages-level, injected services allowed)
  shared/                        # reusable, feature-agnostic, no feature imports
    ui/                          #   presentational components (e.g. file-drop-zone)
    pipes/                       #   pure pipes
    models/                      #   cross-feature types: page.ts (Page<T>), api-violation.ts
  features/
    payments/
      payments.routes.ts         #   lazy child routes, guarded for PAYMENT
      payment-api.ts             #   PaymentApi — HTTP calls for /api/payments
      models/                    #   payment.ts, payment-filter.ts, bulk-result.ts
      pages/
        payment-list-page/       #   payment-list-page.ts|html|scss|spec.ts
        payment-create-page/
        bulk-upload-page/
      components/
        payment-table/
        payment-filters/
        bulk-result-table/
    templates/
      templates.routes.ts        #   guarded for TEMPLATE
      template-api.ts
      models/  pages/  components/
frontend/src/styles/  _theme.scss  _tokens.scss
```

#### Rules

- **Adding a feature** = new folder under `features/` with `<feature>.routes.ts`, lazy-loaded from
  `app.routes.ts` via `loadChildren`, guarded by the role it needs.
- **Dependency direction:** `features → shared, core` · `core → shared` · `shared →` nothing in the app.
  A feature never imports from another feature. If two features need the same thing, move it to `shared/`.
- **Promotion rule:** code starts inside the feature that needs it. It moves to `shared/` only when a
  second feature uses it, so no speculative "might be reusable" code goes to `shared/`.
- **Services:** one `<feature>-api.ts` per backend resource (class `PaymentApi`, `TemplateApi`) — the
  `-api` suffix avoids clashing with the `Payment` model and says what it does. Singletons via
  `@Service()` (v22) / `providedIn: 'root'`. Feature-local UI state stays in the page's signals.
- **Models:** `interface`/`type` only (no classes), one file per backend shape, named after it
  (`payment.ts` → `Payment`, `CreatePaymentRequest`). They mirror the backend's JSON exactly;
  any reshaping for display happens in the page or a pipe.
- No barrel `index.ts` files — import from the file directly (keeps tree-shaking and "go to
  definition" simple).
