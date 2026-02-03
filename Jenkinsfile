pipeline{
	agent any

	tools {
	  jdk 'java-17'
	  maven 'maven'
	}

	stages {
	   stage ('Git-checkout') {
	     steps{
	        git url: 'https://github.com/amithachar/devsecops-calculator-app.git' , branch : 'testing'
	     }
	   }

	   stage ('compile') {
	      steps{
	           sh 'mvn clean compile'
	           }
	   }
	   stage ('test') {
	      steps{
	           sh 'mvn clean test'
	           }
	   }
	   stage ('package') {
	    steps{
               sh 'mvn clean package'
	         }
       }
       stage ('Jacoco Report') {
	    steps{
           sh 'mvn jacoco:report'
	   }    
       post{
        always{
          jacoco execPattern: '**/target/jacoco.exec', 
          classPattern: '**/target/classes', 
          sourcePattern: '**/src/main/java', 
          inclusionPattern: '**/*.class'
        }
       }
	   }
//	   stage ('Sonarqube SAST scan') {
//		steps{
//              sh '''
//			  mvn sonar:sonar \
//              -Dsonar.projectKey=devsecops \
//              -Dsonar.host.url=http://13.201.46.222:9000 \
//              -Dsonar.login=39ba82e5d30643d41961780227aa115eae91ff13
//		     '''
//             }  
//		}

	    stage ('Sonarqube Analysis') {
		 steps{
              script {
				withSonarQubeEnv('SonarQube') {
					sh'mvn clean package org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'   
	                 }
		        }
             }  
		}	

        stage("Quality Gate") {
         steps {
             timeout(time: 1, unit: 'HOURS') {
              waitForQualityGate abortPipeline: true
                }
             }
         }	
   }
}
