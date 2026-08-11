pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Images') {
            steps {
                sh 'docker-compose build'
            }
        }

        stage('Python Tests') {
            steps {
                sh 'docker compose run --rm employee python -m pytest'
            }
        }

        stage('Java Tests') {
            steps {
                sh 'docker run --rm -v "$PWD/manager_app:/app" -w /app maven:3.9.9-eclipse-temurin-21 mvn test'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker compose up -d'
            }
        }
    }
}

