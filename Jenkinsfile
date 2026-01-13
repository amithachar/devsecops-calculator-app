pipeline{
    agent any

    tools {
        jdk 'java-17'
        maven 'maven'
    }

    stages{
        stage('Git-checkout'){
            steps{
                git url: 'https://github.com/ManojKRISHNAPPA/devsecops-1311-cal-app.git', branch: 'batch-2026'
            }     
        }

        stage('comile'){
            steps{
                sh 'mvn clean compile'
            }
        }
        stage('test'){
            steps{
                sh 'mvn clean test'
            }
        }  
        stage('Package'){
            steps{
                sh 'mvn clean package'
            }
        }
        stage('code coverage'){
            steps{
                sh 'mvn jacoco:report'
            }
            post{
                always{
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        inclusionPattern: '**/*.class' 
                    )
                }
            }
        }
        // stage('Sonar-qube-scan'){
        //     steps{
        //         sh """
        //             mvn sonar:sonar \
        //             -Dsonar.projectKey=dev-cal \
        //             -Dsonar.host.url=http://44.246.164.160:9000 \
        //             -Dsonar.login=2cd82092a2d65f363c54f752f8e42295b7c268d6

        //         """
        //     }
        // } 

        stage('SonarQube Analysis'){
            steps{
                script{
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn clean package org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'

                    }
                }
            }
        }           
    }
}
