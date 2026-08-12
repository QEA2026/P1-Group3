pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
    stage('Debug Compose Config') {
        steps {
            script {
                if (isUnix()) {
                    sh 'ls -la docker-compose*.yml'
                    sh 'docker compose -f docker-compose.yml -f docker-compose.ci.yml -p p1group3ci config'
                } else {
                    bat 'dir docker-compose*.yml'
                    bat 'docker compose -f docker-compose.yml -f docker-compose.ci.yml -p p1group3ci config'
                }
            }
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
                script {
                    if (isUnix()) {
                        sh '''
                            docker build \
                                -f manager_app/Dockerfile.test \
                                -t manager-java-tests \
                                manager_app

                            docker run --rm manager-java-tests
                        '''
                    } else {
                        bat '''
                            docker build -f manager_app\\Dockerfile.test -t manager-java-tests manager_app
                            docker run --rm manager-java-tests
                        '''
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

