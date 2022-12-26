properties([
    parameters([
        choice(name: "TYPE", choices: ["nodejs-16", "nodejs-14", "nodejs-12", "java-11"], description: "LANGUAGES"),
    ])
])
if (params.TYPE == "nodejs-16") {
env.NODE_NAME1 = 'nodejs_runner_16' 
} 
else if(params.TYPE == "nodejs-14")
{
    env.NODE_NAME2 = 'nodejs_runner_14' 
}
else if(params.TYPE == "nodejs-12")
{
    env.NODE_NAME3 = 'nodejs_runner' 
}
else {
    env.NODE_NAME = 'java-11'
}

node('nodejs_runner_16') {
      stage('Repo_Checkout') {
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
                  // sh 'npm install mongodb'
//                   sh 'npm install'
//                   sh 'npm run build'
                  dir ("${env.TYPE}/target")
            }
           }
         }
       }
node('image_builder_trivy') {
       try {
       stage('Build_image') {
                dir ('repo') {
                  container('docker-image-builder-trivy') {
                  withCredentials([usernamePassword(credentialsId: 'docker_registry', passwordVariable: 'docker_pass', usernameVariable: 'docker_user')]) {
                  sh 'echo TYPE is : $NODE_NAME1'
                  sh 'docker image build -f Dockerfile --build-arg NODE_NAME1=$NODE_NAME1 -t registry-np.geminisolutions.com/$NODE_NAME1:1.0-$BUILD_NUMBER -t registry-np.geminisolutions.com/$NODE_NAME1 .'
                  sh 'trivy image -f json registry-np.geminisolutions.com/$NODE_NAME1:1.0-$BUILD_NUMBER > trivy-report.json'
	      archiveArtifacts artifacts: 'trivy-report.json', onlyIfSuccessful: true
                    sh '''docker login -u $docker_user -p $docker_pass https://registry-np.geminisolutions.com'''
                  sh 'docker push registry-np.geminisolutions.com/$NODE_NAME1:1.0-$BUILD_NUMBER'
                  sh 'docker push registry-np.geminisolutions.com/$NODE_NAME1'
                  sh 'rm -rf build/'
               }
             }
            }
       }
       stage('Deployment_stage') {
               dir ('repo') {
                   container('docker-image-builder-trivy') {
                   kubeconfig(credentialsId: 'KubeConfigCred') {
                   sh '/usr/local/bin/kubectl apply -f deployment-nodejs.yaml -n main'
                   sh '/usr/local/bin/kubectl rollout restart Deployment $NODE_NAME1 -n main'

                   }
                   }
               }
           }
    } finally {
         //sh 'echo current_image="registry-np.geminisolutions.com/helpdesk/server:1.0-$BUILD_NUMBER" > build.properties'
         //archiveArtifacts artifacts: 'build.properties', onlyIfSuccessful: true
         }
        }
