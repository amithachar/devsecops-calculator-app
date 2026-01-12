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
        }              
    }
}
