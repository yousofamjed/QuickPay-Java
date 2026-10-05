---
name: angular-conventions
description: QuickPay frontend conventions for components, styling and directory layout. Use whenever creating, moving, reviewing or modifying anything under frontend/ (components, pages, services, models, routes, SCSS), or when deciding where new Angular code should go.
---

# QuickPay Angular conventions

Full rationale and option comparisons: `docs/angular-conventions.md`. If you change a rule here, change it there too.
Before writing code, also call the angular-cli MCP `get_best_practices` for the workspace.

## Components
- Standalone only. Never write `standalone: true`, `changeDetection: OnPush`, `NgModule` or `CommonModule`.
- **Page** = routed component in `features/<f>/pages/<name>-page/`. It may inject services, router and APIs, and owns the state (signals) and the backend error handling.
- **Presentational** = everything else, in `features/<f>/components/` or `shared/ui/`. It must **not inject services**. It communicates only through `input()` / `output()` / `model()`. Pipes and directives are allowed.
- Naming: file `payment-table.ts`, class `PaymentTable`, selector `qp-payment-table`. Pages use the suffix `-page` (`PaymentListPage`). No `.component` suffix.
- Use `inject()`, `signal()`/`computed()`, the `host: {}` object, `@if`/`@for`/`@switch`, and `[class.x]`/`[style.x]`. Don't use decorators for inputs, outputs or host bindings, and don't use `ngClass`/`ngStyle`.
- Use a separate `.html` template unless the template is under about 10 lines.

## Styles
- SCSS + Angular Material (M3). Theme goes in `src/styles/_theme.scss` (`mat.theme()`), tokens in `src/styles/_tokens.scss`.
- Use component-scoped `.scss`. Take colors only from `var(--mat-sys-*)` or the tokens file, never hard-coded hex.
- Never use `::ng-deep`, `!important`, or `.mat-mdc-*`/`.mdc-*` selectors. Customize Material through its `mat.*-overrides()` mixins in `_theme.scss`.
- Material first. A custom control goes in `shared/ui/`, is built from Material and native elements, and must be keyboard- and ARIA-accessible.

## Directories
```
src/app/core/{auth,http,layout}   app-wide singletons
src/app/shared/{ui,pipes,models}  reused by 2+ features; imports nothing from features
src/app/features/<f>/             <f>.routes.ts, <f>-api.ts, models/, pages/, components/
```
- A new feature gets its own folder, lazy-loaded with `loadChildren` from `app.routes.ts` and guarded by its role (PAYMENT or TEMPLATE).
- Features never import from other features. Code moves to `shared/` only once a second feature needs it.
- HTTP lives in `<f>-api.ts` (`PaymentApi`), never in components. Models are `interface`/`type` only, mirror the backend JSON exactly, and go in `features/<f>/models/`. Cross-feature shapes such as `Page<T>` and `ApiViolation` go in `shared/models/`.
- No barrel `index.ts` files.

## Checklist before finishing a frontend change
1. Every new file is in the folder this layout requires.
2. No presentational component injects anything.
3. No hard-coded colors, no `::ng-deep`, and no Material internal classes.
4. The new route is lazy and guarded.
