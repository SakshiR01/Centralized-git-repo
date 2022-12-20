properties([
    parameters([
        choice(name: "LANGUAGE", choices: ["nodejs", "python"], description: "Languages"),
    ])
])
if (params.LANGUAGE == "nodejs") {
env.NODE_NAME = 'nodejs_runner_16'
env.POD_TEMPLATE_NAME = 'nodejs-16'
env.CONTAINER_NAME = 'nodejs-16'	
} else {
    env.MAILGUN_USERNAME = 'api-test'
}

node('nodejs_runner_16') {
      stage('language_checkout) {
             dir ('language') {
             checkout([$class: 'GitSCM', branches: [[name: '*/nodejs']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg:  [], \
    userRemoteConfigs: [[credentialsId: 'admingithub', url: 'https://github.com/SakshiR01/Centralized-git-repo.git', poll: 'false']]])
             }
      }
      stage('Nodejs_Build') {
            container('nodejs-16') {
                dir ('centralized'){
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
   
