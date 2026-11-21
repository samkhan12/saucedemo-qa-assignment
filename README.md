# QA Assignment - SauceDemo and ReqRes

![CI](https://github.com/<YOUR-USERNAME>/<YOUR-REPO>/actions/workflows/ci.yml/badge.svg)

All four parts are in this one repo.

- **Part 1** - test plan and 48 test cases: `docs/TEST_PLAN.md`, `docs/TEST_CASES.md` (also as `docs/test-cases.csv`)
- **Part 2** - bug reports in `docs/bugs/`, summary in `docs/BUG_REPORTS.md`. Each bug is also a GitHub Issue and sits on the Project board (Issues and Projects tabs)
- **Part 3** - UI automation with Selenium, TestNG and Maven (Java 17)
- **Part 4** - API tests in Postman, run through Newman
- **Bonus** - GitHub Actions workflow runs both suites on every push

## What you need
Node 18+, Java 17+, Maven, and Chrome. The chromedriver is downloaded automatically by Selenium, so there is nothing else to set up.

## How to run
```
npm install
npm test
```
`npm test` runs the UI tests first and then the API tests.

You can also run them separately:

- `npm run test:ui` - the 8 Selenium tests (headless Chrome)
- `npm run test:ui:headed` - same 8 tests with the browser visible, so you can watch them run
- `npm run test:api` - Newman collection
- `npm run evidence` - re-takes the bug screenshots (see Part 2 below)

Reports are in `target/surefire-reports` (TestNG) and `target/newman`. If a UI test fails it saves a screenshot to `target/screenshots`.

## UI automation
I used the page object pattern. `BaseTest` starts a fresh Chrome for every test, so tests don't share a cart or session. Waits are all explicit, no `Thread.sleep`.

```
src/test/java/com/saucedemo/
  base/BaseTest.java
  pages/      LoginPage, InventoryPage, CartPage, CheckoutPage
  tests/      LoginTests, InventoryTests, CartTests, CheckoutTests, BugEvidenceTests
  utils/Evidence.java
```

The regression tests run on `standard_user`. The broken behaviour of `problem_user` and `performance_glitch_user` is not in the regression suite (it would fail every run). It is in `BugEvidenceTests`, which only records what happens and takes screenshots.

The assignment asks for at least 8 automated cases, so `npm test` runs these 8:

- **TC-001** valid login (LoginTests)
- **TC-002** locked out user (LoginTests)
- **TC-003** wrong password (LoginTests)
- **TC-015** sort by price low to high (InventoryTests)
- **TC-018** add several items and check the cart (CartTests)
- **TC-022** full purchase (CheckoutTests)
- **TC-025** postal code is required (CheckoutTests)
- **TC-026** total = item total + tax (CheckoutTests)

The other cases in `docs/TEST_CASES.md` (negative, edge, UI, and the problem_user / performance_glitch_user ones) are manual. The ones needed for the bug reports are captured by `npm run evidence`.

## API tests
The collection is in `postman/`. Every request checks the status code and the shape of the body, not just the 200.

- GET list of users and GET single user (200, fields and types)
- POST create user (201, echoed fields, id, createdAt)
- POST register and login, success (token present)
- POST login and register with missing email / password / empty body (400, error message, no token)
- GET user 23 and unknown resource 23 (404, no data in the body)
- PUT update user (200, updatedAt) and DELETE (204, empty body)

reqres.in demo endpoints worked without a key when I checked the docs. If that changes, put the key in `postman/reqres.postman_environment.json` under `api_key` and it will be sent as `x-api-key`.

## Part 2 - bugs, issues and board
1. `npm run evidence` runs the problem_user / performance_glitch_user flows and saves screenshots to `docs/screenshots`. What it saw is written to `docs/screenshots/observations.txt`.
2. Push the screenshots to GitHub.
3. `npm run issues` creates one issue per bug (needs the `gh` CLI and `gh auth login`).
4. `npm run board` creates the Kanban project and adds the issues (needs `gh auth refresh -s project`).

## CI
`.github/workflows/ci.yml` runs the UI job and the API job on every push and uploads the reports.
