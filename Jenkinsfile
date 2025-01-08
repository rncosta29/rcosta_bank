pipeline {
    agent any

    environment {
        SONAR_HOST_URL = "http://192.168.15.13:9000"
		BRANCH_NAME = "hml"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: "${BRANCH_NAME}", url: 'https://github.com/rncosta29/rcosta_bank.git'
            }
        }

        stage('Build API Account') {
            steps {
                dir('microservices/api-account') {
                    sh './mvnw clean install -DskipTests'
                }
            }
        }

        stage('SonarQube Analysis API Account') {
            steps {
                dir('microservices/api-account') {
                    sh """
                    sonar-scanner \
                        -Dsonar.projectKey=rcosta_account \
                        -Dsonar.projectName=rcosta_account \
                        -Dsonar.projectVersion=1.0 \
                        -Dsonar.sources=src \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=sqp_12eb4412f20a0bddd08525c08299061fbdc8a68f
                    """
                }
            }
        }

        stage('Build API Credit') {
            steps {
                dir('microservices/api-credit') {
                    sh './mvnw clean install -DskipTests'
                }
            }
        }

        stage('SonarQube Analysis API Credit') {
            steps {
                dir('microservices/api-credit') {
                    sh """
                    sonar-scanner \
                        -Dsonar.projectKey=rcosta_bank \
                        -Dsonar.projectName=rcosta_bank \
                        -Dsonar.projectVersion=1.0 \
                        -Dsonar.sources=src \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=sqp_38351d60619d7b309c4a420f0e4406c7a107e9bf
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizado!'
        }
        success {
            echo 'Pipeline executado com sucesso!'
        }
        failure {
            echo 'Falha na execução do pipeline!'
        }
    }
}
