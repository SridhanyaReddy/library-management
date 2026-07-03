pipeline {
    // Run this pipeline on any available Jenkins agent
    agent any

    stages {
        stage('Clone') {
            steps {
                echo '=== STAGE 1: CLONE ==='
                echo 'Cloning code from repository...'
                // The checkout scm command automatically checks out the git repository that triggered the build
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '=== STAGE 2: BUILD ==='
                echo 'Compiling Java source files using Maven...'
                script {
                    // Check if the agent is running Linux/macOS or Windows and use correct script shell
                    if (isUnix()) {
                        sh 'mvn clean compile'
                    } else {
                        bat 'mvn clean compile'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                echo '=== STAGE 3: TEST ==='
                echo 'Running JUnit 5 unit tests...'
                script {
                    if (isUnix()) {
                        sh 'mvn test'
                    } else {
                        bat 'mvn test'
                    }
                }
            }
        }

        stage('Run') {
            steps {
                echo '=== STAGE 4: RUN ==='
                echo 'Executing the application simulation...'
                script {
                    if (isUnix()) {
                        sh 'mvn exec:java'
                    } else {
                        bat 'mvn exec:java'
                    }
                }
            }
        }
    }
    
    // Post-build actions to display test results and state in Jenkins
    post {
        always {
            echo 'Archiving test results...'
            // JUnit plugin scans for test XML reports in the target directory and parses them for Jenkins UI display
            junit '**/target/surefire-reports/*.xml'
        }
        success {
            echo 'Build, Test, and Execution completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check build console logs for compile errors or test failures.'
        }
    }
}
