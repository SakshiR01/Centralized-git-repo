properties([
    parameters([
        choice(name: "TYPE", choices: ["nodejs", "java"], description: "LANGUAGES"),
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
             checkout([$class: 'GitSCM', branches: [[name: '*/beta']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg:  [], \
    userRemoteConfigs: [[credentialsId: 'admingithub', url: 'https://github.com/SakshiR01/Centralized-git-repo.git', poll: 'false']]])
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
       stage('Deployment_stage') {
               dir ('repo') {
                   container('nodejs-16') {
                   sh 'pwd' 
                   sh 'sed -i -e "s/TYPE/$TYPE/g" Deployment-nodejs.yaml'

                   }
                   }
               }      
            finally {
         //sh 'echo current_image="registry-np.geminisolutions.com/helpdesk/server:1.0-$BUILD_NUMBER" > build.properties'
         //archiveArtifacts artifacts: 'build.properties', onlyIfSuccessful: true
        }    
        }
