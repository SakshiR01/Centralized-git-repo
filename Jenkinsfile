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

       stage('Deployment_stage') {
               dir ('repo') {
                   container('nodejs-16') {
                   sh 'pwd' 
                   sh 'sed -i -e "s/TYPE/$TYPE/g" deployment-nodejs.yaml'
//                    sh 'sed -i -e "s/PORT/$PORT/g" deployment-nodejs.yaml'    
//                    sh '/usr/local/bin/kubectl apply -f deployment-nodejs.yaml -n main'
//                    sh '/usr/local/bin/kubectl rollout restart Deployment $TYPE -n main'
                   }
               }
           }
     finally {
         //sh 'echo current_image="registry-np.geminisolutions.com/$TYPE:1.0-$BUILD_NUMBER" > build.properties'
         //archiveArtifacts artifacts: 'build.properties', onlyIfSuccessful: true
         }
        }     
