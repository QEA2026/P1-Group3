pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Python Tests') {
            steps {
                dir('employee_app') {
                    bat 'pytest'
                }
            }
        }

        stage('Java Tests') {
            steps {
                dir('manager_app') {
                    bat 'mvn test'
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker compose build'
            }
        }
    }
}