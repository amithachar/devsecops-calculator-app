pipeline{
	agent any

	tools {
	  jdk 'java-17'
	  maven 'maven'
	}

	environment{
	   IMAGE_NAME = "amithachar/devsecops-calci-app:${GIT_COMMIT}"
        AWS_REGION = "ap-south-1"
        CLUSTER_NAME = "itkannadigaru-cluster"
        NAMESPACE = "itkannadigaru"
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
					sh 'mvn clean package org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'   
	                 }
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

    //    stage('OWASP Dependency Check') {
    //             steps {
    //                  sh 'mvn org.owasp:dependency-check-maven:check -Dformat=ALL'
    //              }
    //          }

        stage('trivy base image scan') {
		 steps {
			sh 'bash trivy-docker-image-scan.sh'
		}
		}	

		 stage('OPA CONFTEST'){
                    steps{
                        sh 'docker run --rm -v $(pwd):/project openpolicyagent/conftest test --policy dockerfile-security.rego Dockerfile' 
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
        stage('Updating the K8 clsuter'){
            steps{
                sh '''
                    aws eks update-kubeconfig --region ${AWS_REGION} --name ${CLUSTER_NAME}
                '''
            }
        }

        stage('OPA-kubernetes'){
            steps{
                sh 'docker run --rm -v $(pwd):/project openpolicyagent/conftest test --policy opa-k8s-security.rego deployment.yml'
            }
        }

        stage('Deploying to EKS'){
            steps{
                withKubeConfig(caCertificate: '', clusterName: 'itkannadigaru-cluster', contextName: '', credentialsId: 'kube', namespace: 'itkannadigaru', restrictKubeConfigAccess: false, serverUrl: 'https://CA99879FAA017F0E1703499159C69075.gr7.ap-south-1.eks.amazonaws.com') {
                    sh " sed -i 's|replace|${IMAGE_NAME}|g' deployment.yml "
                    sh " kubectl apply -f deployment.yml -n ${NAMESPACE}"
                }
            }
        }        

    } 
}
