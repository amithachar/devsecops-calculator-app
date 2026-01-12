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
        stage('Sonar-qube-scan'){
            steps{
                sh """
                    mvn sonar:sonar \
                    -Dsonar.projectKey=cal-app \
                    -Dsonar.host.url=http://44.247.122.167:9000 \
                    -Dsonar.login=f0b1ec4019d3a4ab3d0e1628c0414d46accc4f51

                """
            }
        }           
    }
}
