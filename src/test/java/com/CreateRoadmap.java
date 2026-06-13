package com;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateRoadmap {
    public static void main(String[] args) {
        String content = "# 📑 BABY KIDSWEAR PLATFORM: END-TO-END QA ARCHITECT BLUEPRINT\n" +
                "---\n\n" +
                "## 📅 PHASE 1: DAY 1 TO LIVE – ENTERPRISE SOFTWARE LIFECYCLE (SDLC/STLC)\n\n" +
                "```\n" +
                "[Sprint 0: Requirements & FRS] ➡️ [Sprint 1-2: Test Strategy & Automation Design] ➡️ [Sprint 3-4: CI/CD Setup & Execution] ➡️ [Sprint 5: Regression & Sign-Off] ➡️ [Day N: Live Go-Live & Monitoring]\n" +
                "```\n\n" +
                "### 1. Sprint 0: Requirements Gathering & Analysis (Day 1 - 5)\n" +
                "* **Objective:** Analyze the Business Requirements Document (BRD) and Functional Requirement Specifications (FRS).\n" +
                "* **QA Architect Role:** Identify gaps and ambiguities in requirements, then conduct review meetings with Developers and Product Owners.\n\n" +
                "### 2. Sprint 1 & 2: Test Planning & Framework Architecture Design (Day 6 - 15)\n" +
                "* **Objective:** Create the Master Test Plan and Test Strategy documents, and set up the core automation framework.\n" +
                "* **Automation Setup:** Initialize a Java-based Hybrid Framework (incorporating both Data-Driven and Keyword-Driven methodologies) in GitHub, building Page Object Model (POM), Utilities, and Base classes from scratch.\n\n" +
                "### 3. Sprint 3 & 4: Test Development, Scripting & Execution (Day 16 - 30)\n" +
                "* **Objective:** Develop and execute automation scripts for newly completed features and API endpoints.\n" +
                "* **Execution:** Write API automation scripts using RestAssured and validate database integrity against the cloud database (MySQL).\n" +
                "* **CI/CD Integration:** Configure the Jenkins Pipeline (via a Jenkinsfile) and set up automatic build triggers using GitHub Webhooks.\n\n" +
                "### 4. Sprint 5: Regression Testing & Sign-Off (Day 31 - 40)\n" +
                "* **Objective:** Execute the full automation regression test suite once the code freeze is in effect.\n" +
                "* **Sign-Off:** Verify test results via Extent Reports, ensure zero critical/blocker bugs remain, and submit the formal \"QA Sign-Off\" document to Release Management.\n\n" +
                "### 5. Deployment Day: Go-Live & Post-Production Smoke Test (Day N)\n" +
                "* **Objective:** Verify application stability immediately after deployment to the live production server.\n" +
                "* **Execution:** Run the automated Smoke Test Suite in the live production environment to validate overall system health and grant final approval.\n\n" +
                "---\n\n" +
                "## 📑 2. FUNCTIONAL REQUIREMENT SPECIFICATIONS (FRS) - PRODUCT MODULE\n\n" +
                "* **FRS-001: Product Creation (POST):** Admin users must be able to add new products with a Name, Price, Category, and Image URL. The price must always be a positive number (> 0).\n" +
                "* **FRS-002: Product Retrieval (GET):** When a user accesses the store, all products must load dynamically from the database with correct prices and images. Fetching by a specific Product ID must return only that product's details.\n" +
                "* **FRS-003: Product Update/Delete (PUT/DELETE):** Only authorized Admin users have permissions to update product prices or remove a product from the system.\n\n" +
                "---\n\n" +
                "## 📊 3. USE CASE DIAGRAM (ARCHITECTURE MAP)\n\n" +
                "```\n" +
                "  👨‍💼 [Admin User] ──────➡️ ( Create / Update / Delete Product ) ──┐\n" +
                "                                                                   │\n" +
                "                                                                   🔽\n" +
                "  🌐 [End Customer] ────➡️ ( View Products & Place Orders ) ──➡️ [ SPRING BOOT BACKEND ]\n" +
                "                                                                   │\n" +
                "                                                                   🔽\n" +
                "  🗄️ [Live Database] ⬅️─── ( Reads/Writes Text & Price Data ) ──────┤\n" +
                "                                                                   │\n" +
                "                                                                   🔽\n" +
                "  ☁️ [Cloud Storage] ⬅️─── ( Loads Product Images via URL ) ────────┘\n" +
                "```\n\n" +
                "---\n\n" +
                "## 📐 4. SYSTEM BLUEPRINT & ROADMAP (ARCHITECT LEVEL)\n\n" +
                "```\n" +
                "[ Local IntelliJ IDEA ] \n" +
                "       │\n" +
                "       ▼ (git push)\n" +
                "[ GitHub Repository (main/develop branches) ]\n" +
                "       │\n" +
                "       ├─► [ Webhook Triggers ] ──► [ Local Jenkins Server (Localhost:8080) ]\n" +
                "       │                                     │\n" +
                "       │                                     ▼ (Executes Jenkinsfile)\n" +
                "       │                                [ Maven Clean Test & Extent Reports ]\n" +
                "       │\n" +
                "       ▼ (Auto-Deployment via Git Integration)\n" +
                "[ Live Cloud Hosting (Render / Railway) ]\n" +
                "       │\n" +
                "       ├─► [ Reads/Writes Text/Price ] ──► [ Live Cloud Database (MySQL/Aiven) ]\n" +
                "       └─► [ Fetches Product Photos ]  ──► [ Cloudinary / AWS S3 Storage ]\n" +
                "```\n\n" +
                "---\n\n" +
                "## 📝 5. MASTER TEST PLAN (MTP)\n\n" +
                "### 1. Test Items & Scope\n" +
                "* **In-Scope:** Product Module APIs (POST, GET, PUT, DELETE), Database Validation (Data Integrity), UI Integration with Thymeleaf, and Jenkins CI/CD Pipeline verification.\n" +
                "* **Out-of-Scope:** Performance/Load Testing (via JMeter) and Security/Penetration Testing.\n\n" +
                "### 2. Testing Types & Methodologies\n" +
                "* **API Testing:** Validate response status codes (200 OK, 201 Created, 400 Bad Request) and verify the JSON response payload structures using RestAssured.\n" +
                "* **Database Testing:** Verify database schemas, null constraints, and primary keys by running direct SQL queries.\n" +
                "* **CI/CD Regression:** Ensure the deployment pipeline automatically triggers and completes successfully on every code push.\n\n" +
                "### 3. Pass/Fail Criteria\n" +
                "* **Pass:** 100% of critical and major bugs must be resolved, and all automation regression scripts must pass cleanly.\n" +
                "* **Fail:** The release will be blocked if there is even one unresolved blocker/critical defect, or if the automation pass rate falls below 95%.\n\n" +
                "---\n\n" +
                "## 🔍 6. TRM (TEST REQUIREMENT MATRIX / TRACEABILITY)\n\n" +
                "| Requirement ID | Requirement Description | Test Case ID | Test Automation Scenario | Expected Result |\n" +
                "| :--- | :--- | :--- | :--- | :--- |\n" +
                "| REQ-01-PROD | Create new product with valid data | TC-API-001 | ProductTests.testPostProduct() | Status Code 201, Product ID generated, entry added to DB |\n" +
                "| REQ-02-PROD | Fetch product details by ID | TC-API-002 | ProductTests.testGetProductById() | Status Code 200, matching name, price & image url returned |\n" +
                "| REQ-03-VAL | Prevent duplicate or negative price | TC-API-003 | ProductTests.testInvalidPriceValidation() | Status Code 400 Bad Request, proper error message thrown |\n" +
                "| REQ-04-DB | Verify Data Integrity in DB | TC-DB-001 | Direct Database JDBC connection query | Data in MySQL matches exactly with the API payload sent |";

        try {
            // Generates the content directly inside roadmap.md
            Files.write(Paths.get("roadmap.md"), content.getBytes());
            System.out.println("🔥 SUCCESS! roadmap.md has been generated with full English blueprint and diagrams successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}