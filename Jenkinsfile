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
                        sh 'docker compose -f docker-compose.yml -f docker-compose.ci.yml -p p1group3ci build --pull'
                    } else {
                        bat 'docker compose -f docker-compose.yml -f docker-compose.ci.yml -p p1group3ci build --pull'
                    }
                }
            }
        }

        stage('Start Services') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker compose -f docker-compose.yml -f docker-compose.ci.yml -p p1group3ci up -d employee-backend manager-backend frontend selenium'
                    } else {
                        bat 'docker compose -f docker-compose.yml -f docker-compose.ci.yml -p p1group3ci up -d employee-backend manager-backend frontend selenium'
                    }
                }
            }
        }

        stage('Python Tests') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker compose -f docker-compose.yml -f docker-compose.ci.yml -p p1group3ci run --rm employee-backend python -m pytest'
                    } else {
                        bat 'docker compose -f docker-compose.yml -f docker-compose.ci.yml -p p1group3ci run --rm employee-backend python -m pytest'
                    }
                }
            }
        }

        stage('Java Unit Tests') {
            steps {
                dir('manager_app') {
                    script {
                        if (isUnix()) {
                            sh '''
                                docker run --rm \
                                -v "$PWD:/app" \
                                -w /app \
                                maven:3.9.9-eclipse-temurin-21 \
                                mvn test -Dtest='!*RunCucumberTest'
                            '''
                        } else {
                            bat 'docker run --rm -v "%WORKSPACE%\\manager_app:/app" -w /app maven:3.9.9-eclipse-temurin-21 mvn test -Dtest="!*RunCucumberTest"'
                        }
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker compose -f docker-compose.yml -f docker-compose.ci.yml -p p1group3ci up -d'
                    } else {
                        bat 'docker compose -f docker-compose.yml -f docker-compose.ci.yml -p p1group3ci up -d'
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                if (isUnix()) {
                    sh 'docker compose -f docker-compose.yml -f docker-compose.ci.yml -p p1group3ci down --remove-orphans'
                } else {
                    bat 'docker compose -f docker-compose.yml -f docker-compose.ci.yml -p p1group3ci down --remove-orphans'
                }
            }
        }
    }
}

