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
                script {
                    if (isUnix()) {
                        sh 'docker compose build --pull'
                    } else {
                        bat 'docker compose build --pull'
                    }
                }
            }
        }

        stage('Start Services') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker compose up -d employee-backend manager-backend frontend selenium'
                    } else {
                        bat 'docker compose up -d employee-backend manager-backend frontend selenium'
                    }
                }
            }
        }

        stage('Python Tests') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker compose run --rm employee-backend python -m pytest'
                    } else {
                        bat 'docker compose run --rm employee-backend python -m pytest'
                    }
                }
            }
        }

        stage('Java Tests') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            docker run --rm --network p1-group3_default \
                            -v "$WORKSPACE/manager_app:/app" \
                            -w /app \
                            maven:3.9.9-eclipse-temurin-21 \
                            mvn test -Dmanager.base.url=http://manager-backend:9090 \
                                   -Demployee.base.url=http://employee-backend:8080 \
                                   -Dfrontend.url=http://frontend:5173 \
                                   -Dselenium.url=http://selenium:4444
                        '''
                    } else {
                        bat "docker run --rm --network p1-group3_default -v \"%WORKSPACE%\\manager_app\":/app -w /app maven:3.9.9-eclipse-temurin-21 mvn test -Dmanager.base.url=http://manager-backend:9090 -Demployee.base.url=http://employee-backend:8080 -Dfrontend.url=http://frontend:5173 -Dselenium.url=http://selenium:4444"
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker compose up -d'
                    } else {
                        bat 'docker compose up -d'
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                if (isUnix()) {
                    sh 'docker compose down'
                } else {
                    bat 'docker compose down'
                }
            }
        }
    }
}

