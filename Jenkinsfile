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
                sh 'docker compose build'
            }
        }

        stage('Python Tests') {
            steps {
                sh 'docker compose run --rm employee python -m pytest'
            }
        }

        stage('Java Tests') {
            steps {
                sh 'docker compose run --rm manager mvn test'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker compose up -d'
            }
        }
    }
}

