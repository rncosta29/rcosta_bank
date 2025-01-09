pipeline {
    agent any

    environment {
        SONAR_HOST_URL = "http://192.168.15.13:9000"
        BRANCH_NAME = "${env.BRANCH_NAME}" // A branch é obtida a partir do GitHub/Webhook
		SONAR_SCANNER = "/opt/sonar-scanner/sonar-scanner-4.8.0.2856-linux/bin/sonar-scanner"
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Check Branch') {
            when {
                branch 'main'
                branch 'hml'
                branch 'dev'
            }
            steps {
                echo "Branch ${env.BRANCH_NAME} é válida. Continuando com o pipeline..."
            }
        }
        
        stage('Checkout') {
            steps {
                script {
                    echo "Checkout da branch: ${BRANCH_NAME}"
                    git branch: "${BRANCH_NAME}", url: 'https://github.com/rncosta29/rcosta_bank.git'
                }
            }
        }

        stage('Build API Account') {
            steps {
                script {
                    echo "Iniciando o build da API Account"
                    dir('microservices/api-account') {
                        sh 'chmod +x ./mvnw'
                        sh './mvnw clean install -DskipTests'
                    }
                }
            }
        }

        stage('SonarQube Analysis API Account') {
            steps {
                script {
                    echo "Iniciando a análise no SonarQube para API Account"
					dir('microservices/api-account') {
						sh """
						${env.SONAR_SCANNER} \
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
        }

        stage('Build API Credit') {
            steps {
                script {
                    echo "Iniciando o build da API Credit"
                    dir('microservices/api-credit') {
                        sh 'chmod +x ./mvnw'
                        sh './mvnw clean install -DskipTests'
                    }
                }
            }
        }

        stage('SonarQube Analysis API Credit') {
            steps {
                script {
                    echo "Iniciando a análise no SonarQube para API Credit"
					dir('microservices/api-credit') {
						sh """
						${env.SONAR_SCANNER} \
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
