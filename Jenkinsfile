#!groovy
def artServer = Artifactory.server 'art-p-01'
def buildInfo = Artifactory.newBuildInfo()
def distDir = 'build/dist/'

pipeline {
    libraries {
        lib('jenkins-pipeline-shared')
    }
    options {
        skipDefaultCheckout()
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
        timeout(time: 15, unit: 'MINUTES')
        timestamps()
        ansiColor('xterm')
    }
    environment {
//        BUILD_ENV = "CI"
//        STAGE = "Unknown"
//        ARTIFACTORY_CREDS = credentials("sbr-artifactory-user")
//        ARTIFACTORY_HOST_NAME = "art-p-01"
//        MASTER = "master"
//
        ENVIRONMENT = "${params.ENV_NAME}"
//        PROD1_NODE = "cdhdn-p01-01"
//        SSH_KEYNAME = "sbr-${params.ENV}-ci-ssh-key"
        SERVICE_NAME = 'sbr-sampling-uk.gov.ons.registers.service'
    }
    parameters {
        choice(choices: 'dev\ntest\nbeta', description: 'Into what environment wants to deploy oozie config e.g. dev, test or beta?', name: 'ENV_NAME')
    }
    agent { label 'download.jenkins.slave' }
    stages {
        stage('Checkout') {
            agent { label 'download.jenkins.slave' }
            environment{
                STAGE = "Checkout"
            }
            steps {
                deleteDir()
                checkout scm
//                script {
//                    buildInfo.name = "${MODULE_NAME}"
//                    buildInfo.number = "${BUILD_NUMBER}"
//                    buildInfo.env.collect()
//                }
                colourText("info","BuildInfo: ${buildInfo.name}-${buildInfo.number}")
                stash name: 'Checkout'
            }
        }
        stage('Build'){
//            agent { label 'build.gradle-4-9' }
            agent any
            environment{
                STAGE = "Build"
            }
            steps {
                unstash name: 'Checkout'
                // TODO: fixup
                colourText("info", "Building ${env.BUILD_ID} on ${env.JENKINS_URL} from branch ${env.BRANCH_NAME}")
                sh "gradle clean compileScala"
                // milestone label: 'post build', ordinal: 1
            }
            post {
                success {
                    postSuccess()
                }
                failure {
                    postFail()
                }
            }
        }
        stage('Static Analysis'){
            failfast true
            parallel {
                stage('Test Unit')
            }
        }
    }
    post {
        always {
            script {
                colourText("info", 'Post steps initiated')
                deleteDir()
            }
        }
        success {
            colourText("success", "All stages complete. Build was successful.")
            // sendNotifications currentBuild.result, "\$SBR_EMAIL_LIST"
        }
        unstable {
            colourText("warn", "Something went wrong, build finished with result ${currentBuild.result}. This may be caused by failed tests, code violation or in some cases unexpected interrupt.")
            // sendNotifications currentBuild.result, "\$SBR_EMAIL_LIST", "${STAGE}"
        }
        failure {
            colourText("warn","Process failed at: ${STAGE_NAME}")
            // sendNotifications currentBuild.result, "\$SBR_EMAIL_LIST", "${STAGE}"
        }
    }
}

def installPythonModule(String module){
    sh """pip install ${module} -i http://${ARTIFACTORY_CREDS_USR}:${ARTIFACTORY_CREDS_PSW}@${ARTIFACTORY_HOST_NAME}/artifactory/api/pypi/yr-python/simple --trusted-host ${ARTIFACTORY_HOST_NAME}"""
}

def archiveToHDFS(){
    echo "archiving jar to [$ENV] environment"
    sshagent(credentials: ["sbr-$ENV-ci-ssh-key"]){
        sh """
            ssh sbr-$ENV-ci@$PROD1_NODE mkdir -p $MODULE_NAME/
            echo "Successfully created new directory [$MODULE_NAME/]"
            scp -r $WORKSPACE/$MODULE_NAME/* sbr-$ENV-ci@$PROD1_NODE:$MODULE_NAME/
            echo "Successfully moved artifact [JAR] and scripts to $MODULE_NAME/"
            ssh sbr-$ENV-ci@$PROD1_NODE hdfs dfs -put -f $MODULE_NAME/ hdfs://prod1/user/sbr-$ENV-ci/lib/
            echo "Successfully copied jar file to HDFS"

        """
    }
}

def postSuccess() {
    colourText('info', "Stage: ${env.STAGE_NAME} successfull!")
}

def postFail() {
    colourText('warn', "Stage: ${env.STAGE_NAME} failed!")
}
