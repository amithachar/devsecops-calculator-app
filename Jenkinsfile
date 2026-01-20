pipeline {
    agent any

    tools {
        jdk 'java-17'
        maven 'maven'
    }

    environment{
        IMAGE_NAME = "manojkrishnappa/dev-sec-ops:${GIT_COMMIT}"
        AWS_REGION ="us-west-2"
        CLSUTER_NAME = "itkannadigaru-cluster"
        NAMESPACE = "itkannadigaru"
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
                        sh 'docker run --rm -v $(pwd):/project openpolicyagent/conftest test --policy dockerfile-security.rego Dockerfile' 
                    }
                }
            }
        }

        stage('Docker Image Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME} .'
            }
        }

        stage('Docker-Login'){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                            sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                        }
                }
            }
        }

        stage('Dockehub'){
            steps{
               sh 'docker push ${IMAGE_NAME}'
            }
        }

        stage('update k8 cluster'){
            steps{
                sh 'aws eks update-kubeconfig --region ${AWS_REGION} --name ${CLSUTER_NAME}'
            }
        }
        stage('OPA-kubernetes'){
            steps{
                sh 'docker run --rm -v $(pwd):/project openpolicyagent/conftest test --policy opa-k8s-security.rego deployment.yml'
            }
        }

        stage('Deployment'){
            steps{
                withKubeConfig(caCertificate: '', clusterName: 'itkannadigaru-cluster', contextName: '', credentialsId: 'kube', namespace: 'itkannadigaru', restrictKubeConfigAccess: false, serverUrl: 'https://68D48662F34038BB6C8BDE1BD2CA22BE.gr7.us-west-2.eks.amazonaws.com') {
                    sh " sed -i 's|replace|${IMAGE_NAME}|g' deployment.yml "
                    sh " kubectl apply -f deployment.yml -n ${NAMESPACE}"
                }
            }
        }        
    }
}
