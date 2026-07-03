# Library Management DevOps Pipeline Project

This repository contains a complete, industry-standard, yet beginner-friendly **CI/CD DevOps pipeline project** using a CLI-based **Library Management System** in **Java 17**.

This project is tailored specifically for **B.Tech University Practical & Viva Voce Examinations**. It focuses on demonstrating modern DevOps automation practices (Git, GitHub, Maven, Jenkins, and Unit Testing) rather than complex database or framework code, allowing a beginner to completely build, test, and run it in **30–45 minutes**.

---

## 1. Project Architecture & Flow

### Why Library Management is the Best DevOps Project Choice
1. **Clear Business Domain:** Everyone understands a library (Books, Borrowing, Returning). The examiner requires zero explanation of the business logic.
2. **Encapsulates Core OOPs:** It demonstrates encapsulation, lists, object mutation, and modularity in under 150 lines of Java.
3. **Ideal for Unit Testing:** Tracking book availability creates perfect binary test cases (success/failure scenarios) for JUnit 5, showcasing clean CI/CD assertions.
4. **Non-Interactive Simulation:** The runtime is simulated using automated console logs. This ensures that the Jenkins pipeline can execute and exit cleanly without hanging waiting for input.

### ASCII Flow Diagram
```text
  [ Developer Workspace ] 
             │ (Writes Code / Runs JUnit Tests Locally)
             ▼
      [ Git Commit & Push ]
             │
             ▼
        [ GitHub ] ──────────(Webhook Trigger)──────────┐
             │                                          │
             ▼                                          ▼
      [ Source Repo ]                             [ Jenkins Server ]
                                                        │
                                                        ▼
                                                [ Pipeline Execution ]
                                                ├── Stage 1: Clone SCM
                                                ├── Stage 2: Maven Compile
                                                ├── Stage 3: JUnit Test
                                                └── Stage 4: Simulated Run
```

### Folder Structure
The project uses the standard **Maven directory layout**:
```text
library-management/
├── .gitignore               # Configures files to exclude from Git tracking
├── JenkinsFile              # Declarative CI/CD pipeline definition
├── pom.xml                  # Maven Project Object Model (dependencies/plugins)
├── Readme.md                # Technical documentation & Exam guide (This File)
└── src/
    ├── main/
    │   └── java/
    │       ├── Book.java    # Represents Book entity (states: borrow, return)
    │       ├── Library.java # Encapsulates collection of books and search logic
    │       └── Main.java    # CLI Simulation entry point (non-interactive)
    └── test/
        └── java/
            └── LibraryTest.java # JUnit 5 test suite (verifies library rules)
```

---

## 2. Core Concepts Explained

### 2.1 Git Workflow
**Git** is a distributed version control system. It allows multiple developers to track changes in code. The workflow is:
1. **Working Directory:** The place where you edit your files.
2. **Staging Area (`git add`):** A buffer zone where you select changes you want to commit next.
3. **Local Repository (`git commit`):** A history log stored on your computer of all commits.
4. **Remote Repository (`git push`):** A central server (like GitHub) hosting the code for sharing and backup.

### 2.2 Apache Maven
**Maven** is a build automation tool for Java projects. It uses `pom.xml` (Project Object Model) to:
- **Dependency Management:** Automatically downloads libraries (like JUnit) from the Maven Central Repository.
- **Lifecycle Management:** Standardizes build phases like `compile`, `test`, `package` (JAR generation), and `clean`.

### 2.3 Jenkins
**Jenkins** is an open-source automation server. It acts as the Orchestrator in a DevOps pipeline. When a developer pushes code to GitHub, Jenkins intercepts this, clones the code, compiles it, runs test cases, and flags success or failure.

### 2.4 CI/CD (Continuous Integration & Continuous Delivery)
- **Continuous Integration (CI):** Developers merge code changes back to the main branch frequently. An automated build and test process validates the changes.
- **Continuous Delivery (CD):** Once the build passes CI, it is automatically prepared, packaged (like a JAR), and ready to deploy to production.

---

## 3. Comprehensive Setup Guide

### Step 1: Git and GitHub Setup
1. Create a free account on [GitHub](https://github.com).
2. Install Git on your machine.
3. Navigate to the project root directory in VS Code terminal.
4. Run the following Git commands:
   ```bash
   # Initialize Git repository
   git init

   # Stage all files
   git add .

   # Commit changes locally
   git commit -m "Initial commit: Library Management DevOps project"

   # Rename branch to main
   git branch -M main
   ```
5. Go to GitHub and click **New Repository**. Name it `library-management`. Do **not** check "Add a README" or ".gitignore".
6. Copy the commands under "...or push an existing repository from the command line":
   ```bash
   git remote add origin https://github.com/<YOUR_GITHUB_USERNAME>/library-management.git
   git push -u origin main
   ```

### Step 2: Local Maven Build & Test Verification
Before running Jenkins, verify that the project builds locally:
```bash
# Clean previous builds and compile source files
mvn clean compile

# Execute JUnit 5 Unit Tests
mvn test

# Run the simulation locally
mvn exec:java
```

### Step 3: Jenkins Installation & Configuration
1. **Install Jenkins:**
   - Download the installer or install Jenkins on your machine.
2. **Unlock Jenkins:** Access `http://localhost:8080`. Retrieve the administrator password from the path shown on the screen.
3. **Install Plugins:** Choose **"Install Suggested Plugins"**.
4. **Create Admin User:** Set up your credentials.
5. **Configure Maven in Jenkins:**
   - Go to **Manage Jenkins** -> **Tools**.
   - Scroll down to **Maven installations** and click **Add Maven**.
   - Set Name to `Maven 3` and check **Install automatically**. Click **Save**.

### Step 4: Jenkins Pipeline Configuration
1. From the Jenkins dashboard, click **New Item**.
2. Name it `Library-Management-Pipeline` and select **Pipeline**. Click **OK**.
3. Under the **Build Triggers** section, check **GitHub hook trigger for GITScm polling**.
4. Scroll to **Pipeline** section:
   - Definition: Choose **Pipeline script from SCM**.
   - SCM: Select **Git**.
   - Repository URL: Enter your GitHub repository URL (e.g., `https://github.com/<YOUR_GITHUB_USERNAME>/library-management.git`).
   - Credentials: Click **Add** -> **Jenkins** if it's a private repo, otherwise leave blank.
   - Script Path: Enter `JenkinsFile` (Ensure casing matches exactly).
5. Click **Save**.
6. Run the first build manually by clicking **Build Now** on the left menu.

### Step 5: Webhook Configuration (For Automated Builds)
To make Jenkins build code *automatically* on every `git push`:
1. Go to your **GitHub repository** -> **Settings** -> **Webhooks** -> **Add webhook**.
2. Set **Payload URL** to: `http://<YOUR_JENKINS_PUBLIC_IP>:8080/github-webhook/`
   *(If working on localhost, download `ngrok` and route port 8080: `ngrok http 8080`. Copy the forwarding URL).*
3. Set Content type to `application/json`.
4. Select **Just the push event** and click **Add webhook**.
5. Test it by editing `Main.java` locally, committing, and pushing. Jenkins will immediately start building!

---

## 4. Expected Console Outputs

### Expected Local `mvn test` Output
```text
[INFO] Scanning for projects...
[INFO] -------------------< com.devops:library-management >--------------------
[INFO] Building DevOps Library Management Project 1.0.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- clean:3.2.0:clean (default-clean) @ library-management ---
[INFO] 
[INFO] --- resources:3.3.0:resources (default-resources) @ library-management ---
[INFO] 
[INFO] --- compiler:3.11.0:compile (default-compile) @ library-management ---
[INFO] Compiling 3 source files to C:\Users\...\library-management\target\classes
[INFO] 
[INFO] --- surefire:3.1.2:test (default-test) @ library-management ---
[INFO] Running LibraryTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.08 s - in LibraryTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### Expected Jenkins Pipeline Console Output
```text
Started by user Admin
Obtained JenkinsFile from git https://github.com/student/library-management.git
[Pipeline] Start of Pipeline
[Pipeline] node
Running on Jenkins in C:\ProgramData\Jenkins\.jenkins\workspace\Library-Management-Pipeline
[Pipeline] {
[Pipeline] stage (Clone)
[Pipeline] {
[Pipeline] echo
=== STAGE 1: CLONE ===
[Pipeline] echo
Cloning code from repository...
[Pipeline] checkout
...
[Pipeline] }
[Pipeline] stage (Build)
[Pipeline] {
[Pipeline] echo
=== STAGE 2: BUILD ===
[Pipeline] echo
Compiling Java source files using Maven...
[Pipeline] sh
+ mvn clean compile
[INFO] Compiling 3 source files...
[INFO] BUILD SUCCESS
[Pipeline] }
[Pipeline] stage (Test)
[Pipeline] {
[Pipeline] echo
=== STAGE 3: TEST ===
[Pipeline] echo
Running JUnit 5 unit tests...
[Pipeline] sh
+ mvn test
[INFO] Running LibraryTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
[Pipeline] }
[Pipeline] stage (Run)
[Pipeline] {
[Pipeline] echo
=== STAGE 4: RUN ===
[Pipeline] echo
Executing the application simulation...
[Pipeline] sh
+ mvn exec:java
=================================================
   LIBRARY MANAGEMENT SYSTEM - SIMULATION RUN    
=================================================
[INFO] Initializing Library database...
[INFO] Adding sample books to library catalog...
[SUCCESS] Total books in catalog: 3
...
=================================================
   SIMULATION COMPLETED - PIPELINE SUCCESSFUL    
=================================================
[Pipeline] }
[Pipeline] stage (Post Actions)
[Pipeline] junit
Recording test results
[Pipeline] echo
Build, Test, and Execution completed successfully!
[Pipeline] End of Pipeline
Finished: SUCCESS
```

---

## 5. Viva Voce Q&A (20+ Questions)

### Q1: What is the goal of this project?
**A:** The goal is to build an automated CI/CD pipeline using Jenkins to fetch, build, unit test, and run a simple Java-based Library Management application, showing the concepts of modern DevOps automation.

### Q2: What tech stack are you using and why?
**A:** Java 17 for the programming logic, Maven for compilation and dependency management, JUnit 5 for writing unit tests, Git & GitHub for version control, and Jenkins for build orchestration.

### Q3: What is the purpose of `pom.xml` in Maven?
**A:** It stands for Project Object Model. It defines the project configuration, version target (Java 17), dependencies (JUnit 5 APIs), and plugin lifecycles.

### Q4: Explain the difference between `git add` and `git commit`.
**A:** `git add` copies files to the Staging Area (preparing them to be saved). `git commit` takes a snapshot of the staged files and registers them permanently in the local Git repository history.

### Q5: What is a Jenkins Pipeline?
**A:** A suite of plugins that supports implementing and integrating continuous delivery pipelines into Jenkins. It is configured using a `Jenkinsfile`.

### Q6: What is a Declarative Pipeline vs. a Scripted Pipeline?
**A:** Declarative pipeline is a newer, simpler, structured approach with strict syntax (using blocks like `pipeline`, `agent`, `stages`). Scripted pipeline uses Groovy code and is more complex. We use a Declarative Pipeline.

### Q7: Why are we using a non-interactive simulation in `Main.java`?
**A:** Because Jenkins pipelines run in background environments without a terminal screen. If the program pauses to wait for user keyboard inputs (`Scanner.nextLine()`), the pipeline will hang forever.

### Q8: What does `mvn clean` do?
**A:** It deletes the `/target` directory created by previous builds to ensure that the next compilation starts from scratch and is not polluted by stale classes.

### Q9: How does JUnit 5 ensure project quality in CI/CD?
**A:** Jenkins executes `mvn test` in the pipeline. If any JUnit assertion fails (e.g. library size is wrong or a borrowed book is marked available), the test phase returns an error exit code, which fails the Jenkins build immediately.

### Q10: What is the purpose of the `.gitignore` file?
**A:** It tells Git to ignore compiled outputs (like `/target`) and user-specific configuration files (like IDE settings). These files shouldn't be shared because they are generated locally and vary between environments.

### Q11: Explain the stages in your `JenkinsFile`.
**A:** 
- **Clone:** Pulls the latest code version from GitHub.
- **Build:** Compiles the source files.
- **Test:** Runs all Unit Tests.
- **Run:** Runs the simulation to verify execution.

### Q12: How does Jenkins know when a developer pushes code to GitHub?
**A:** We use a Webhook. GitHub sends an HTTP POST notification to Jenkins at a specific URL (`/github-webhook/`) when a commit is pushed, triggering a build automatically.

### Q13: What is the purpose of the JUnit post-build action in the pipeline?
**A:** The line `junit '**/target/surefire-reports/*.xml'` parses JUnit XML results so that Jenkins can display graphs, statistics, and detailed test outcomes directly in the Jenkins web portal.

### Q14: What is Continuous Integration (CI)?
**A:** A development practice where developers frequently integrate code changes into a shared repository. Each integration is verified by an automated build and test process to detect errors as quickly as possible.

### Q15: What is Continuous Deployment (CD) vs. Continuous Delivery?
**A:** In Continuous Delivery, changes that pass the pipeline are automatically built and packaged, ready for human-approved deployment. In Continuous Deployment, every passing change is deployed to production automatically without human intervention.

### Q16: What does the term "Continuous Delivery" mean in DevOps?
**A:** It means that code is always in a deployable state. Any successful commit that passes the pipeline is automatically packaged (as a JAR) and ready to be pushed to staging or production.

### Q17: What does `isUnix()` do in your Jenkinsfile?
**A:** It checks whether the Jenkins agent is running on Linux/Unix or Windows. It allows executing `sh` for Linux and `bat` for Windows commands, making the pipeline cross-platform.

### Q18: What is a branch in Git?
**A:** A branch is an independent line of development. The default branch is `main`. Developers create new branches to test features without affecting stable code.

### Q19: What is the Maven Central Repository?
**A:** A remote repository hosted by the Maven community containing thousands of open-source libraries. Maven automatically downloads required JAR files (like JUnit) from it.

### Q20: Explain the Maven lifecycle order.
**A:** `validate` -> `compile` -> `test` -> `package` -> `integration-test` -> `verify` -> `install` -> `deploy`. Calling any phase automatically runs all preceding phases.

### Q21: What is a build trigger in Jenkins?
**A:** An event that initiates a build. Examples include manual clicks, timed schedules (cron), polling the SCM for changes, or receiving webhook HTTP notifications.

---

## 6. Common Interview / External Exam Questions
1. **Explain the benefits of DevOps over traditional software development models.**
   - *Answer:* DevOps bridges the gap between Development and Operations. It uses automation to speed up deployment, minimize human error, increase release frequency, and provide rapid feedback.
2. **What is Infrastructure as Code (IaC)?**
   - *Answer:* IaC is the practice of managing and provisioning computing infrastructure through machine-readable configuration files (like Terraform or Ansible) rather than manual interactive configuration tools.
3. **What is a "broken build" and how do you handle it?**
   - *Answer:* A broken build occurs when compilation fails or unit tests fail in the pipeline. The team must prioritize fixing the main branch build before committing new features.

---

## 7. Troubleshooting & Common Errors

### Error 1: Maven compiler complains about Java Version
* **Symptom:** `Source option 5 is no longer supported` or `Unsupported class version`.
* **Fix:** Ensure you have Java 17 installed and that environment variable `JAVA_HOME` points to JDK 17. Check that `<maven.compiler.source>` is set to `17` in `pom.xml`.

### Error 2: Jenkins pipeline hangs indefinitely in the "Run" stage
* **Symptom:** The pipeline keeps spinning, never finishing the build.
* **Fix:** The program is waiting for user keyboard inputs. Ensure you are using the non-interactive CLI simulation code in `Main.java` and not a `Scanner` listening to `System.in`.

### Error 3: Jenkins says `mvn: command not found`
* **Symptom:** Script exits with code 127 in Jenkins build logs.
* **Fix:** Ensure Maven is added to the system PATH variable on the machine running the Jenkins agent, or configure Maven in Jenkins Tool Settings and reference it in the pipeline environment.
