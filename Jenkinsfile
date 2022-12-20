properties([
    parameters([
        choice(name: "ENVIRONMENT", choices: ["beta", "production"], description: "Target Environment"),
    ])
])
if (params.ENVIRONMENT == "beta") {
env.JWT_SECRET = 'p4sta.w1th-b0logn3s3-s@ucx'
env.JWT_ALGO = 'HS256'
env.MONGODB_URI = 'mongodb+srv://admin:sr4IXfCLYYNlHPMX@helpdesk.7qd80m7.mongodb.net/helpdesk?retryWrites=true&w=majority'
env.PORT = 3000
env.LOG_LEVEL = 'debug'
env.MAILGUN_API_KEY = '1212312312312'
env.MAILGUN_USERNAME = 'api'
env.MAILGUN_DOMAIN = 'helpdesk@geminisolutions.com'
env.AGENDA_DB_COLLECTION='agendaJobs'
env.AGENDA_POOL_TIME='60'
env.AGENDA_CONCURRENCY='10'
} else {
    env.MAILGUN_USERNAME = 'api-test'
}

node('nodejs_runner_16') {
      stage('helpdesk_checkout') {
             dir ('helpdesk') {
             checkout([$class: 'GitSCM', branches: [[name: '*/beta']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg:  [], \
    userRemoteConfigs: [[credentialsId: 'admingithub', url: 'git@github.com:Gemini-Solutions/helpdesk-server.git', poll: 'false']]])
             }
      }
      stage('Nodejs_Build') {
            container('nodejs-16') {
                dir ('helpdesk'){
                  sh 'rm -rf package-lock.json'
                  //sh 'npm cache clean --force'
                  sh 'env'
                  // sh 'npm install mongodb'
                  sh 'npm install'
                  sh 'npm run build'
            }
           }
         }
       }
    node('image_builder_trivy') {
       try {
       stage('Build_image') {
                dir ('helpdesk') {
                  container('docker-image-builder-trivy') {
                  withCredentials([usernamePassword(credentialsId: 'docker_registry', passwordVariable: 'docker_pass', usernameVariable: 'docker_user')]) {
                  sh 'echo environment is : $ENVIRONMENT'
                  sh 'docker image build -f Dockerfile --build-arg ENVIRONMENT=$ENVIRONMENT -t registry-np.geminisolutions.com/helpdesk/server:1.0-$BUILD_NUMBER -t registry-np.geminisolutions.com/helpdesk/server .'
                  sh 'trivy image -f json registry-np.geminisolutions.com/helpdesk/server:1.0-$BUILD_NUMBER > trivy-report.json'
	      archiveArtifacts artifacts: 'trivy-report.json', onlyIfSuccessful: true
                    sh '''docker login -u $docker_user -p $docker_pass https://registry-np.geminisolutions.com'''
                  sh 'docker push registry-np.geminisolutions.com/helpdesk/server:1.0-$BUILD_NUMBER'
                  sh 'docker push registry-np.geminisolutions.com/helpdesk/server'
                  sh 'rm -rf build/'
               }
             }
            }
       }
       stage('Deployment_stage') {
               dir ('helpdesk') {
                   container('docker-image-builder-trivy') {
                   kubeconfig(credentialsId: 'KubeConfigCred') {
                   sh '/usr/local/bin/kubectl apply -f Deployment-beta.yaml -n dev'
                   sh '/usr/local/bin/kubectl rollout restart Deployment hd-server-beta -n dev'

                   }
                   }
               }
           }
    } finally {
         //sh 'echo current_image="registry-np.geminisolutions.com/helpdesk/server:1.0-$BUILD_NUMBER" > build.properties'
         //archiveArtifacts artifacts: 'build.properties', onlyIfSuccessful: true
         }
        }
