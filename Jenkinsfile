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

       stage('Deployment_stage') {
               dir ('repo') {
                   container('nodejs-16') {
                   kubeconfig(credentialsId: 'KubeConfigCred') {
                   sh '/usr/local/bin/kubectl apply -f deployment-nodejs.yaml -n main'
                   sh '/usr/local/bin/kubectl rollout restart Deployment centralized-js -n main'

                   }
                   }
               }
           }
    }     
