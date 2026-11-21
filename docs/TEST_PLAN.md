# Test Plan - SauceDemo

QA Tester: Mohd Salman
Site under test: https://www.saucedemo.com/

## 1\. Purpose

Check that the main shopping flow works: login, browsing and sorting products, cart, checkout and order confirmation. Also check that it behaves properly when the input is wrong, and find the defects in the two "broken" users (`problem\_user` and `performance\_glitch\_user`).

## 

## 2\. Scope

In scope:

* Login and logout (valid, invalid, locked out, empty fields, direct URL access, session after logout)
* Product list and sorting
* Cart (add, remove, badge count, cart kept after refresh)
* Checkout (form validation, totals, cancel, finish, empty cart, back button after the order)
* Basic UI, responsive and keyboard checks
* API testing of reqres.in

Out of scope: real payments (there are none), load testing, full accessibility audit, browsers other than Chrome for automation.

## 

## 3\. Test users

All use the password `secret\_sauce`.

* `standard\_user` - normal behaviour, used as the reference
* `locked\_out\_user` - login should be blocked
* `problem\_user` - seeded UI and functional bugs
* `performance\_glitch\_user` - slow responses



## 4\. Approach (Manual/Automation)

* Functional testing of each flow, happy path first, then variations
* Negative and error guessing: empty fields, wrong case, spaces only, SQL / script text, opening URLs directly
* Edge cases: empty cart checkout, very long input, double click, refresh and back button after the order
* For the broken users I compare what they do against `standard\_user`
* Some exploratory testing after the scripted cases
* UI checks at desktop and mobile width, keyboard only, 200% zoom
* Automation for the stable cases (Selenium, TestNG, Maven) and Postman/Newman for the API

The cases are in `TEST\_CASES`.



## 5\. Environment

**Manual** - WebView, Chrome, Firefox and phone-size window

**Automation** - Chrome latest for automation stack Java 17, Maven, Node 18, GitHub Actions for CI.



## 6\. Entry / exit

Start when the site is reachable and the cases are written. Finish when all High priority cases are run, every bug found is logged as a GitHub Issue with a screenshot, and the automated suites pass for the standard user flows.



## 7\. Bug handling

One GitHub Issue per bug with severity, priority, steps, expected vs actual and a screenshot. Issues are tracked on a Kanban board (To Do / In Progress / Done).

* Severity: Critical, Blocker, Major, Minor, Low = cosmetic
* Priority: P1 = fix now, P2 = next release, P3 = backlog



## 8\. Risks

* The site is a public demo and can change, so locators are kept in page objects only
* performance\_glitch\_user is slow, so I use explicit waits and keep that user out of the regression suite



## 9\. Coverage

* Login: TC-001 to TC-010
* Product list and sorting: TC-011 to TC-016, TC-032, TC-033, TC-038, TC-047
* Cart: TC-017 to TC-021, TC-034, TC-039 to TC-041
* Checkout: TC-022 to TC-031, TC-035, TC-037, TC-042, TC-043
* Broken users: TC-032 to TC-037
* UI / accessibility: TC-007, TC-012, TC-044 to TC-048



### 10\. Logging Workspace

* Manual Test\_Cases - saucedemo-qa-assignment\\docs
* Automation Test\_Cases - saucedemo-qa-assignment\\docs
* Bugs Report - saucedemo-qa-assignment\\docs\\screenshots
* Observation Text(execution log) - saucedemo-qa-assignment\\docs\\screenshots



### 10\. Resource

* QA Tester: Mohd Salman

