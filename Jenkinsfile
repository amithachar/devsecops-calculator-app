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
        stage('Package'){
            steps{
                sh '''
                    mvn sonar:sonar \
                        -Dsonar.projectKey=devsecops-cal \
                        -Dsonar.host.url=http://34.209.102.67:9000 \
                        -Dsonar.login=03672f816998b463488f32a50996b6e5bb543bec

                '''
            }
        }           
    }
}
