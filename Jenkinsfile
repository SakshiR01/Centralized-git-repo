properties([
    parameters([
        choice(name: "TYPE", choices: ["nodejs", "java"], description: "LANGUAGES"),
    ])
])
if (params.TYPE == "nodejs") {
env.NODE_NAME = 'nodejs_runner_16'
env.POD_TEMPLATE_NAME = 'nodejs-16'
env.CONTAINER_NAME = 'nodejs-16' 
} 
else if(params.TYPE == "nodejs-12")
{
    env.NODE_NAME = 'nodejs_runner' 
}
else {
    env.NODE_NAME = 'java'
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
                dir ('helpdesk'){
                  sh 'rm -rf package-lock.json'
                  //sh 'npm cache clean --force'
                  sh 'env'
//                   sh 'pwd'  
//                   sh 'sed -i -e "s/TYPE/$TYPE/g" Dockerfile'  
//                   sh 'docker image build -f Dockerfile --build-arg TYPE=$TYPE -t registry-np.geminisolutions.com/helpdesk/server:1.0-$BUILD_NUMBER -t registry-np.geminisolutions.com/$TYPE .'
//                   sh '''docker login -u $docker_user -p $docker_pass https://registry-np.geminisolutions.com'''
//                   sh 'docker push registry-np.geminisolutions.com/$TYPE:1.0-$BUILD_NUMBER'
//                   sh 'docker push registry-np.geminisolutions.com/$TYPE'
//                   sh 'rm -rf build/'  
                  // sh 'npm install mongodb'
//                   sh 'npm install'
//                   sh 'npm run build'
            }
           }
         }
       stage('Deployment_stage') {
               dir ('repo') {
                   container('nodejs-16') {
                   sh 'pwd' 
                   sh 'sed -i -e "s/TYPE/$TYPE/g" deployment-nodejs.yaml'

                   }
                   }
               }      
//             finally {
//          sh 'echo current_image="registry-np.geminisolutions.com/$TYPE:1.0-$BUILD_NUMBER" > build.properties'
//          archiveArtifacts artifacts: 'build.properties', onlyIfSuccessful: true
//         }    
        }
