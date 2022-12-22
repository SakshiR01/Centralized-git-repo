properties([
    parameters([
        choice(name: "TYPE", choices: ["nodejs", "java"], description: "Languages"),
    ])
])
if (params.LANGUAGE == "nodejs") {
env.NODE_NAME = 'nodejs_runner_16'
env.POD_TEMPLATE_NAME = 'nodejs-16'
env.CONTAINER_NAME = 'nodejs-16' 
} else {
    env.NODE_NAME_1 = 'nodejs-14'
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
            }
           }
         }

node('image_builder_trivy') {
       try {
       stage('Build_image') {
                dir ('repo') {
                  container('docker-image-builder-trivy') {
                  withCredentials([usernamePassword(credentialsId: 'docker_registry', passwordVariable: 'docker_pass', usernameVariable: 'docker_user')]) {
//                   sh 'echo environment is : $ENVIRONMENT'
                  sh 'pwd' 
                  sh 'sed -i -e "s/TYPE/$TYPE/g" DockerFile'
                  sh 'sed -i -e "s/PORT/$PORT/g" DockerFile'
                  sh 'docker image build -f Dockerfile --build-arg TYPE=$TYPE -t registry-np.geminisolutions.com/$TYPE:1.0-$BUILD_NUMBER -t registry-np.geminisolutions.com/$TYPE .'
                  sh 'trivy image -f json registry-np.geminisolutions.com/$TYPE:1.0-$BUILD_NUMBER > trivy-report.json'
	      archiveArtifacts artifacts: 'trivy-report.json', onlyIfSuccessful: true
                    sh '''docker login -u $docker_user -p $docker_pass https://registry-np.geminisolutions.com'''
                  sh 'docker push registry-np.geminisolutions.com/$TYPE:1.0-$BUILD_NUMBER'
                  sh 'docker push registry-np.geminisolutions.com/$TYPE'
                  sh 'rm -rf build/'
               }
             }
            }
       }
       stage('Deployment_stage') {
               dir ('repo') {
                   container('docker-image-builder-trivy') {
                   kubeconfig(credentialsId: 'KubeConfigCred') {
                   sh 'pwd' 
                   sh 'sed -i -e "s/TYPE/$TYPE/g" deployment-nodejs.yaml'
                   sh 'sed -i -e "s/PORT/$PORT/g" deployment-nodejs.yaml'    
                   sh '/usr/local/bin/kubectl apply -f deployment-nodejs.yaml -n main'
                   sh '/usr/local/bin/kubectl rollout restart Deployment $TYPE -n main'

                   }
                   }
               }
           }
    } finally {
         //sh 'echo current_image="registry-np.geminisolutions.com/helpdesk/server:1.0-$BUILD_NUMBER" > build.properties'
         //archiveArtifacts artifacts: 'build.properties', onlyIfSuccessful: true
         }
        }     
