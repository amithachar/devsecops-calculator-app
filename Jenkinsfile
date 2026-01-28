pipeline{
	agent any

	tools {
	  jdk 'java-17'
	  maven 'maven'
	}

	stages {
	   stage ('Git-checkout') {
	     steps{
	        git url: 'https://github.com/amithachar/devsecops-calculator-app.git' , branch : 'main'
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
	}
}
