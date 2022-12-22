properties([
    parameters([
        choice(name: "TYPE", choices: ["nodejs", "java"], description: "Languages"),
    ])
])
if (params.LANGUAGE == "nodejs") {
env.NODE_NAME = 'nodejs_runner_16'
env.POD_TEMPLATE_NAME = 'nodejs-16'
env.CONTAINER_NAME = 'nodejs-16'
env.IMAGE_BUILDER_TRIVY = 'image_builder_trivy' 
} else {
    env.NODE_NAME_1 = 'nodejs-14'
}

node('nodejs_runner_16') {
      stage('repo_checkout') {
             dir ('repo') {
             checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg:  [], \
    userRemoteConfigs: [[credentialsId: 'admingithub', url: 'https://github.com/SakshiR01/Centralized-git-repo.git', poll: 'false']]])
             }
      }
      stage('Nodejs_Build') {
            container('nodejs-16') {
                dir ('repo'){
                  sh 'rm -rf package-lock.json'
                  //sh 'npm cache clean --force'
                  sh 'env'
            }
           }
         }
node('image_builder_trivy') {
       try {
       stage('Build_image') {
                dir ('repo') {
                  container('docker-image-builder-trivy') {
                  withCredentials([usernamePassword(credentialsId: 'docker_registry', passwordVariable: 'docker_pass', usernameVariable: 'docker_user')]) {
                  sh 'echo environment is : $TYPE'
                  sh 'docker image build -f Dockerfile --build-arg TYPE=$TYPE -t registry-np.geminisolutions.com/helpdesk/server:1.0-$BUILD_NUMBER -t registry-np.geminisolutions.com/centralized/server .'
                  sh 'trivy image -f json registry-np.geminisolutions.com/centralized/server:1.0-$BUILD_NUMBER > trivy-report.json'
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
               dir ('repo') {
                   container('docker-image-builder-trivy') {
                   kubeconfig(credentialsId: 'KubeConfigCred') {
                   sh '/usr/local/bin/kubectl apply -f deployment-nodejs.yaml -n dev'
                   sh '/usr/local/bin/kubectl rollout restart Deployment centralized-js -n dev'

                   }
                   }
               }
           }
    } finally {
         //sh 'echo current_image="registry-np.geminisolutions.com/helpdesk/server:1.0-$BUILD_NUMBER" > build.properties'
         //archiveArtifacts artifacts: 'build.properties', onlyIfSuccessful: true
         }
        }       
