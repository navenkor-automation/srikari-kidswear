# 📑 BABY KIDSWEAR PLATFORM: END-TO-END QA ARCHITECT BLUEPRINT
---

## 📅 PHASE 1: DAY 1 TO LIVE – ENTERPRISE SOFTWARE LIFECYCLE (SDLC/STLC)

```
[Sprint 0: Requirements & FRS] ➡️ [Sprint 1-2: Test Strategy & Automation Design] ➡️ [Sprint 3-4: CI/CD Setup & Execution] ➡️ [Sprint 5: Regression & Sign-Off] ➡️ [Day N: Live Go-Live & Monitoring]
```

### 1. Sprint 0: Requirements Gathering & Analysis (Day 1 - 5)
* **Objective:** Analyze the Business Requirements Document (BRD) and Functional Requirement Specifications (FRS).
* **QA Architect Role:** Identify gaps and ambiguities in requirements, then conduct review meetings with Developers and Product Owners.

### 2. Sprint 1 & 2: Test Planning & Framework Architecture Design (Day 6 - 15)
* **Objective:** Create the Master Test Plan and Test Strategy documents, and set up the core automation framework.
* **Automation Setup:** Initialize a Java-based Hybrid Framework (incorporating both Data-Driven and Keyword-Driven methodologies) in GitHub, building Page Object Model (POM), Utilities, and Base classes from scratch.

### 3. Sprint 3 & 4: Test Development, Scripting & Execution (Day 16 - 30)
* **Objective:** Develop and execute automation scripts for newly completed features and API endpoints.
* **Execution:** Write API automation scripts using RestAssured and validate database integrity against the cloud database (MySQL).
* **CI/CD Integration:** Configure the Jenkins Pipeline (via a Jenkinsfile) and set up automatic build triggers using GitHub Webhooks.

### 4. Sprint 5: Regression Testing & Sign-Off (Day 31 - 40)
* **Objective:** Execute the full automation regression test suite once the code freeze is in effect.
* **Sign-Off:** Verify test results via Extent Reports, ensure zero critical/blocker bugs remain, and submit the formal "QA Sign-Off" document to Release Management.

### 5. Deployment Day: Go-Live & Post-Production Smoke Test (Day N)
* **Objective:** Verify application stability immediately after deployment to the live production server.
* **Execution:** Run the automated Smoke Test Suite in the live production environment to validate overall system health and grant final approval.

---

## 📑 2. FUNCTIONAL REQUIREMENT SPECIFICATIONS (FRS) - PRODUCT MODULE

* **FRS-001: Product Creation (POST):** Admin users must be able to add new products with a Name, Price, Category, and Image URL. The price must always be a positive number (> 0).
* **FRS-002: Product Retrieval (GET):** When a user accesses the store, all products must load dynamically from the database with correct prices and images. Fetching by a specific Product ID must return only that product's details.
* **FRS-003: Product Update/Delete (PUT/DELETE):** Only authorized Admin users have permissions to update product prices or remove a product from the system.

---

## 📊 3. USE CASE DIAGRAM (ARCHITECTURE MAP)

```
  👨‍💼 [Admin User] ──────➡️ ( Create / Update / Delete Product ) ──┐
                                                                   │
                                                                   🔽
  🌐 [End Customer] ────➡️ ( View Products & Place Orders ) ──➡️ [ SPRING BOOT BACKEND ]
                                                                   │
                                                                   🔽
  🗄️ [Live Database] ⬅️─── ( Reads/Writes Text & Price Data ) ──────┤
                                                                   │
                                                                   🔽
  ☁️ [Cloud Storage] ⬅️─── ( Loads Product Images via URL ) ────────┘
```

---

## 📐 4. SYSTEM BLUEPRINT & ROADMAP (ARCHITECT LEVEL)

```
[ Local IntelliJ IDEA ] 
       │
       ▼ (git push)
[ GitHub Repository (main/develop branches) ]
       │
       ├─► [ Webhook Triggers ] ──► [ Local Jenkins Server (Localhost:8080) ]
       │                                     │
       │                                     ▼ (Executes Jenkinsfile)
       │                                [ Maven Clean Test & Extent Reports ]
       │
       ▼ (Auto-Deployment via Git Integration)
[ Live Cloud Hosting (Render / Railway) ]
       │
       ├─► [ Reads/Writes Text/Price ] ──► [ Live Cloud Database (MySQL/Aiven) ]
       └─► [ Fetches Product Photos ]  ──► [ Cloudinary / AWS S3 Storage ]
```

---

## 📝 5. MASTER TEST PLAN (MTP)

### 1. Test Items & Scope
* **In-Scope:** Product Module APIs (POST, GET, PUT, DELETE), Database Validation (Data Integrity), UI Integration with Thymeleaf, and Jenkins CI/CD Pipeline verification.
* **Out-of-Scope:** Performance/Load Testing (via JMeter) and Security/Penetration Testing.

### 2. Testing Types & Methodologies
* **API Testing:** Validate response status codes (200 OK, 201 Created, 400 Bad Request) and verify the JSON response payload structures using RestAssured.
* **Database Testing:** Verify database schemas, null constraints, and primary keys by running direct SQL queries.
* **CI/CD Regression:** Ensure the deployment pipeline automatically triggers and completes successfully on every code push.

### 3. Pass/Fail Criteria
* **Pass:** 100% of critical and major bugs must be resolved, and all automation regression scripts must pass cleanly.
* **Fail:** The release will be blocked if there is even one unresolved blocker/critical defect, or if the automation pass rate falls below 95%.

---

## 🔍 6. TRM (TEST REQUIREMENT MATRIX / TRACEABILITY)

| Requirement ID | Requirement Description | Test Case ID | Test Automation Scenario | Expected Result |
| :--- | :--- | :--- | :--- | :--- |
| REQ-01-PROD | Create new product with valid data | TC-API-001 | ProductTests.testPostProduct() | Status Code 201, Product ID generated, entry added to DB |
| REQ-02-PROD | Fetch product details by ID | TC-API-002 | ProductTests.testGetProductById() | Status Code 200, matching name, price & image url returned |
| REQ-03-VAL | Prevent duplicate or negative price | TC-API-003 | ProductTests.testInvalidPriceValidation() | Status Code 400 Bad Request, proper error message thrown |
| REQ-04-DB | Verify Data Integrity in DB | TC-DB-001 | Direct Database JDBC connection query | Data in MySQL matches exactly with the API payload sent |