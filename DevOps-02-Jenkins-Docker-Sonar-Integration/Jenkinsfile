def IMAGE_NAME

pipeline {
    agent any
    options {
        timeout(time: 1, unit: 'HOURS') // Timeout the entire pipeline after 1 hour
        retry(1) // Retry failed stages up to 3 times
        buildDiscarder(logRotator(numToKeepStr: '10')) // Keep the last 10 builds
        skipDefaultCheckout() // Skip the default checkout
        timestamps()//add timestamp in the log
        disableConcurrentBuilds()// at a time, only one build should run

    }
    tools {
            jdk 'JDK 1.17' // Use JDK 11 installed on the agent
            gradle 'GRADLE_HOME' // Use Gradle 6.5 installed on the agent
    }
    environment {
        CONFIG_FILE = 'sdp.yml'
//         APP_VERSION = ''
    }
    stages {
        stage('Initialize') {
                steps {
                    script {
                         checkout scm
                         // Read the properties file
                         def config = readProperties file: "${CONFIG_FILE}"
                         env.DOCKER_CREDENTIALS_ID = config.dockerCredentialsId
                         env.SERVICE_NAME = config.serviceName
                         env.APP_VERSION = config.appVersion.toLowerCase()
                         env.DOCKER_REGISTRY = config.dockerRegistry
                         //logging values
                         env.IMAGE_NAME = "${env.SERVICE_NAME}-${env.APP_VERSION}"
                         echo "Docker Credentials ID: ${env.DOCKER_CREDENTIALS_ID}"
                         echo "Docker Image: ${env.SERVICE_NAME}"
                         echo "Docker Registry: ${env.IMAGE_NAME}"

                    }
                }
        }
        stage('Build') {
                    steps {
                        script {
                            // Build the project using Gradle
                            def version="${env.APP_VERSION}"
                            bat "gradle clean build -Pversion=${version}"

                            }
                    }
        }

        stage('SonarQube Analysis') {
                    steps {
                        script {
                            def scannerHome = tool 'SonarQubeScanner'
                            withSonarQubeEnv('sonar-server') {
                                bat "gradle sonarqube -Dsonar.projectKey=springboot-rest-api -Dsonar.host.url=http://localhost:9000 -Dsonar.login=squ_05f57d1e2165dc391ff2fe489cf6cd5b44c4649b"
                            }
                        }
                    }
                }

         stage('Build and push image') {
                    steps {
                        script {
                            // Build the Docker image
                          powershell """
                                     docker build --build-arg VERSION=${env.APP_VERSION} -t ${env.IMAGE_NAME}-${env.BUILD_ID} .
                                     """

                           // Push the Docker image to Docker Hub
                           /*
                           docker.withDockerRegistry([credentialsId: 'docker', url: 'https://hub.docker.com/']) {
                           docker.image("${env.IMAGE_NAME}-${env.BUILD_ID}").push('latest')
                           docker.image("${env.IMAGE_NAME}-${env.BUILD_ID}").push("${env.BUILD_ID}")
                           }
                           */



                        }
                    }
         }
    }
}
