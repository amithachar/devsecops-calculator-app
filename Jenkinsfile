pipeline {
    agent any

    tools {
        jdk 'java-17'
        maven 'maven'
    }

    environment{
        IMAGE_NAME = "manojkrishnappa/dev-sec-ops:${GIT_COMMIT}"
    }
    stages {

        stage('Git Checkout') {
            steps {
                git url: 'https://github.com/ManojKRISHNAPPA/devsecops-1311-cal-app.git',
                    branch: 'batch-2026'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Test & Coverage') {
            steps {
                sh 'mvn test jacoco:report'
            }
            post {
                always {
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        inclusionPattern: '**/*.class'
                    )
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Vulnerability Scan') {
            parallel {

                // Uncomment if OWASP plugin is configured
                // stage('OWASP Dependency Check') {
                //     steps {
                //         sh 'mvn org.owasp:dependency-check-maven:check'
                //     }
                // }

                stage('Trivy Image Scan') {
                    steps {
                        sh 'bash trivy-docker-image-scan.sh'
                    }
                }

                stage('OPA CONFTEST'){
                    steps{
                        sh 'docker run --rm -v $(pwd):/project openpolicyagent/conftest test --policy dockerfile-security.rego Dockerfile-multistage' 
                    }
                }
            }
        }

        stage('Docker Image Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME} -f Dockerfile-multistage .'
            }
        }
    }
}
