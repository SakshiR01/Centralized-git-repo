properties([
    parameters([
        choice(name: "TYPE", choices: ["nodejs-16", "nodejs-14", "nodejs-12", "java-11"], description: "LANGUAGES"),
        choice(name: "SERVICES", choices: ["abcd", "efgh", "ijkl", "mnop"], description: "services to be build"),
        choice(name: "PORT", choices: ["8081", "80", "8080", "8999"], description: "port to be used"),
    ])
])

env.TRIVY_NODE = 'image_builder_trivy'
env.TRIVY_CONTAINER = 'docker-image-builder-trivy'
if (params.TYPE == "nodejs-16") 
{
    env.NODE_NAME = 'nodejs_runner_16' 
    env.CONTAINER_NAME = 'nodejs-16'
    env.STAGE_NAME = 'Nodejs_Build'
} 
else if(params.TYPE == "nodejs-14")
{
    env.NODE_NAME = 'nodejs_runner_14' 
    env.CONTAINER_NAME = 'nodejs-14'
    env.STAGE_NAME = 'Nodejs_Build'
}
else if(params.TYPE == "nodejs-12")
{
    env.NODE_NAME = 'nodejs_runner'
    env.CONTAINER_NAME = 'nodejs-12'
    env.STAGE_NAME = 'Nodejs_Build' 
}
else {
    env.NODE_NAME = 'maven_runner_java11'
    env.CONTAINER_NAME = 'maven-runner-11'
    env.STAGE_NAME = 'maven_Build'
}

node ($SERVICE) {
      stage('Repo_Checkout') {
             dir ('repo') {
             checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg:  [], \
    userRemoteConfigs: [[credentialsId: 'admingithub', url: 'https://github.com/SakshiR01/Centralized-git-repo.git', poll: 'false']]])
             }
      }
      stage($STAGE_NAME) {
            container($CONTAINER_NAME) {
                dir ('repo'){
                  sh 'rm -rf package-lock.json'
                  //sh 'npm cache clean --force'
                  sh 'env'
                  // sh 'npm install mongodb'
//                   sh 'npm install'
//                   sh 'npm run build'
                  dir ("${env.SERVICE}/target"){
		          sh 'pwd'
//                 sh 'chmod +x *.jar'
              }
            }
           }
         }
       }
node($TRIVY_NODE) {
       try {
       stage('Build_image') {
                dir ('repo') {
                  container($TRIVY_CONTAINER) {
                  withCredentials([usernamePassword(credentialsId: 'docker_registry', passwordVariable: 'docker_pass', usernameVariable: 'docker_user')]) {
                  sh 'echo TYPE is : $SERVICE'
		        //   sh 'sed -i -e "s/SERVICE/$SERVICE/g" Dockerfile deployment-type.yaml' 
                  sh 'sed -i -e "s/SERVICE/$SERVICE/g" -e "s/PORT/$PORT/g" Dockerfile deployment-beta.yaml' 
		          sh 'cat Dockerfile'	  
                  sh 'docker image build -f Dockerfile --build-arg SERVICE=$SERVICE -t registry-np.geminisolutions.com/$SERVICE:1.0-$BUILD_NUMBER -t registry-np.geminisolutions.com/$SERVICE .'
                  sh 'trivy image -f json registry-np.geminisolutions.com/$SERVICE:1.0-$BUILD_NUMBER > trivy-report.json'
	      archiveArtifacts artifacts: 'trivy-report.json', onlyIfSuccessful: true
                  sh '''docker login -u $docker_user -p $docker_pass https://registry-np.geminisolutions.com'''
                  sh 'docker push registry-np.geminisolutions.com/$SERVICE:1.0-$BUILD_NUMBER'
                  sh 'docker push registry-np.geminisolutions.com/$SERVICE'
                  sh 'rm -rf build/'
               }
             }
            }
       }
       stage('Deployment_stage') {
               dir ('repo') {
                   container($TRIVY_CONTAINER) {
                   kubeconfig(credentialsId: 'KubeConfigCred') {
                   sh '/usr/local/bin/kubectl apply -f deployment-beta.yaml -n dev'
                   sh '/usr/local/bin/kubectl rollout restart Deployment $SERVICE -n dev'

                   }
                   }
               }
           }
    } finally {
         //sh 'echo current_image="registry-np.geminisolutions.com/helpdesk/server:1.0-$BUILD_NUMBER" > build.properties'
         //archiveArtifacts artifacts: 'build.properties', onlyIfSuccessful: true
         }
        }
