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
                    sh 'pytest'
                }
            }
        }

        stage('Java Tests') {
            steps {
                dir('manager_app') {
                    sh 'mvn test'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker compose build'
            }
        }
    }
}

