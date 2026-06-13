# 📑 BABY KIDSWEAR PLATFORM: END-TO-END QA ARCHITECT BLUEPRINT
---

## 📅 PHASE 1: DAY 1 TO LIVE – ENTERPRISE SOFTWARE LIFECYCLE (SDLC/STLC)

[
  Sprint 0: Requirements & FRS -> 
  Sprint 1-2: Test Strategy & Automation Design -> 
  Sprint 3-4: CI/CD Setup & Execution -> 
  Sprint 5: Regression & Sign-Off -> 
  Day N: Live Go-Live & Monitoring
]

### 1. Sprint 0: Requirements Gathering & Analysis (Day 1 - 5)
* **Objective:** Business Requirements Document (BRD) మరియు Functional Requirement Specifications (FRS) అనలైజ్ చేయడం.
* **QA Architect Role:** రిక్వైర్మెంట్స్‌లో ఉన్న గ్యాప్స్ ని ఐడెంటిఫై చేసి డెవలపర్స్, ప్రొడక్ట్ ఓనర్లతో రివ్యూ మీటింగ్స్ కండక్ట్ చేయడం.

### 2. Sprint 1 & 2: Test Planning & Framework Architecture Design (Day 6 - 15)
* **Objective:** Test Plan, Test Strategy డాక్యుమెంట్స్ క్రియేట్ చేయడం మరియు ఆటోమేషన్ ఫ్రేమ్‌వర్క్ బేస్ డిజైన్ సెట్ చేయడం.
* **Automation Setup:** Java-based Hybrid Framework (Data-Driven + Keyword-Driven) ని GitHub లో ఇనిషియలైజ్ చేసి, POM, Utilities, Base classes స్క్రాచ్ నుండి బిల్డ్ చేయడం.

### 3. Sprint 3 & 4: Test Development, Scripting & Execution (Day 16 - 30)
* **Objective:** డెవలపర్స్ కంప్లీట్ చేసిన ఫీచర్స్ మరియు API ఎండ์పాయింట్స్‌పై ఆటోమేషన్ స్క్రిప్ట్స్ రన్ చేయడం.
* **Execution:** RestAssured వాడుతూ API测试 కేసెస్ రాసి, క్లౌడ్ డేటాబేస్ (MySQL) తో డేటా ఇంటెగ్రిటీని వాలిడేట్ చేయడం.
* **CI/CD Integration:** Jenkins Pipeline (Jenkinsfile) కాన్ఫిగర్ చేసి, GitHub వెబ్‌హుక్స్ ద్వారా ఆటోమేటిక్ బిల్డ్స్ సెట్ చేయడం.

### 4. Sprint 5: Regression Testing & Sign-Off (Day 31 - 40)
* **Objective:** కోడ్ ఫ్రీజ్ అయ్యాక కంప్లీట్ ఆటోమేషన్ రిగ్రెషన్ సూట్ రన్ చేయడం.
* **Sign-Off:** Extent Reports ద్వారా టెస్ట్ రిపోర్ట్స్ ని వెరిఫై చేసి, ఎలాంటి క్రిటికల్ బగ్స్ లేవని కన్ఫర్మ్ చేసుకున్నాక "QA Sign-Off" డాక్యుమెంట్‌ను రిలీజ్ మేనేజ్‌మెంట్‌కి సబ్మిట్ చేయడం.

### 5. Deployment Day: Go-Live & Post-Production Smoke Test (Day N)
* **Objective:** అప్లికేషన్ ప్రొడクション సర్వర్‌లోకి వెళ్ళిన వెంటనే లైవ్ లో కరెక్ట్‌గా పనిచేస్తుందో లేదో వెరిఫై చేయడం.
* **Execution:** లైవ్ ఎన్విరాన్‌మెంట్‌లో Smoke Test Suite రన్ చేసి గ్రీన్ సిగ్నల్ ఇవ్వడం.

---

## 📑 2. FUNCTIONAL REQUIREMENT SPECIFICATIONS (FRS) - PRODUCT MODULE

* **FRS-001: Product Creation (POST):** అడ్మిన్ యూజర్ ప్రొడక్ట్ పేరు, ప్రైస్, కేటగిరీ, మరియు ఇమేజ్ URL లతో కొత్త ప్రొడక్ట్‌ని సిస్టమ్‌లోకి యాడ్ చేయగలగాలి. ప్రైస్ ఎప్పుడూ పాజిటివ్ నంబర్ అయి ఉండాలి (> 0).
* **FRS-002: Product Retrieval (GET):** కస్టమర్ సైట్ ఓపెన్ చేయగానే డేటాబేస్ నుండి ప్రోడక్ట్స్ అన్నీ ప్రైస్ మరియు ఇమేజ్‌లతో సహా స్క్రీన్ మీద లోడ్ అవ్వాలి.
* **FRS-003: Product Update/Delete (PUT/DELETE):** అడ్మిన్ మాత్రమే ప్రోడక్ట్ ప్రైస్ మార్చడానికి లేదా ప్రోడక్ట్‌ని సిస్టమ్ నుండి డిలీట్ చేయడానికి పర్మిషన్ ఉండాలి.

---

## 📊 3. USE CASE DIAGRAM (ARCHITECTURE MAP)

  [Admin User] -------> ( Create / Update / Delete Product ) ---┐
                                                                   |
                                                                   v
  [End Customer] ----─> ( View Products & Place Orders ) ───> [ SPRING BOOT BACKEND ]
                                                                   |
                                                                   v
  [Live Database] <--- ( Reads/Writes Text & Price Data ) ─────────┤
                                                                   |
                                                                   v
  [Cloud Storage] <--- ( Loads Product Images via URL ) ───────────┘

---

## 📐 4. SYSTEM BLUEPRINT & ROADMAP (ARCHITECT LEVEL)

[ Local IntelliJ IDEA ] 
       |
       v (git push)
[ GitHub Repository (main/develop branches) ]
       |
       ├─► [ Webhook Triggers ] ──► [ Local Jenkins Server (Localhost:8080) ]
       │                                     │
       │                                     ▼ (Executes Jenkinsfile)
       │                                [ Maven Clean Test & Extent Reports ]
       |
       v (Auto-Deployment via Git Integration)
[ Live Cloud Hosting (Render / Railway) ]
       |
       ├─► [ Reads/Writes Text/Price ] ──► [ Live Cloud Database (MySQL/Aiven) ]
       └─► [ Fetches Product Photos ]  ──► [ Cloudinary / AWS S3 Storage ]

---

## 📝 5. MASTER TEST PLAN (MTP)

### 1. Test Items & Scope
* **In-Scope:** Product Module APIs (POST, GET, PUT, DELETE), Database Validation, UI Integration with Thymeleaf, Jenkins CI/CD Pipeline.
* **Out-of-Scope:** Performance/Load Testing (JMeter), Security Testing.

### 2. Testing Types & Methodologies
* **API Testing:** RestAssured లైబ్రరీ వాడి స్టేటస్ కోడ్స్ మరియు JSON రెస్పాన్స్ బాడీని వాలిడేట్ చేస్తాం.
* **Database Testing:** SQL క్వెరీస్ ద్వారా డేటా టైప్స్, నల్ వాల్యూస్ వెరిఫై చేస్తాం.
* **CI/CD Regression:** ప్రతి కోడ్ చేంజ్ కి పైప్‌లైన్ ఆటోమేటిక్‌గా రన్ అవ్వాలి.

### 3. Pass/Fail Criteria
* **Pass:** 100% క్రిటికల్ మరియు మేజర్ బగ్స్ ఫిక్స్ అవ్వాలి. ఆటోమేషన్ స్క్రిప్ట్స్ అన్నీ పాస్ అవ్వాలి.
* **Fail:** ఒకవేళ ఒక్క బ్లాకర్ లేదా క్రిటికల్ బగ్ ఉన్నా రిలీజ్ ఆపేస్తాం.

---

## 🔍 6. TRM (TEST REQUIREMENT MATRIX)

| Requirement ID | Requirement Description | Test Case ID | Test Automation Scenario | Expected Result |
| :--- | :--- | :--- | :--- | :--- |
| REQ-01-PROD | Create new product | TC-API-001 | ProductTests.testPostProduct() | Status Code 201, Entry added to DB |
| REQ-02-PROD | Fetch product details | TC-API-002 | ProductTests.testGetProductById() | Status Code 200, Matching data returned |
| REQ-03-VAL  | Prevent negative price | TC-API-003 | ProductTests.testInvalidPriceValidation() | Status Code 400 Bad Request |
| REQ-04-DB   | Verify Data Integrity | TC-DB-001 | Direct Database JDBC query | Data in MySQL matches payload |