pipeline{
    agent any

    tools {
        jdk 'java-17'
        maven 'maven'
    }

    stages{
        stage('Git-checkout'){
            steps{
                git url: ' ', branch: ' ', credentials: ''
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
    }
}
