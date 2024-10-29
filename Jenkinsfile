pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/Book-service-CI-CD/Book-Gateway.git', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                echo 'Building Book Gateway...'
                bat 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing Book Gateway...'
                bat 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying Book Gateway...'
                bat 'docker build -t book-gateway:latest .'
                bat 'docker run -d -p 8222:8222 book-gateway:latest'
            }
        }
    }
}
