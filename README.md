# SauceDemo Automation Tests

## Objective

Automate a set of test cases for [saucedemo.com](https://www.saucedemo.com) using Java, Selenium, and a test framework
(e.g., JUnit).

1. Login with valid credentials works (a positive test case).
2. Login with invalid credentials does not work (a negative test case).
3. Logged-in users can add to and check out items in the cart.

## Prerequisites

- Java **21** installed (`java -version` to confirm).
- Maven **3.9+** installed (`mvn -v` to confirm).
- Chrome or Edge browser installed (latest stable versions).

## Getting Started

1. Clone this repository.
```git
$ git clone 
```

2. Run `mvn clean test` to execute the tests.