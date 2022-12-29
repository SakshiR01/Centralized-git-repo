properties([
    parameters([
	    choice(name: "TYPE", choices: ["nodejs-16", "nodejs-14", "nodejs-12"], description: "LANGUAGES"),
        choice(name: "SERVICE", choices: ["Centralized-git-repo"], description: "services to be build"),
        choice(name: "PORT", choices: ["8081", "80", "8080", "8999"], description: "port to be used"),
    ])
])

//Please specify the branch Name
env.BRANCH="*/main"

//To convert the service to lowercase as docker registry should be in lower case.
env.REGISTRY= params.SERVICE.toLowerCase()

//Docker and trivy builer Stage environment variable.
env.TRIVY_NODE = 'image_builder_trivy'
env.TRIVY_CONTAINER = 'docker-image-builder-trivy'

//Conditions based on languages
if (params.TYPE == "nodejs-16") 
{
    env.NODE_NAME = 'nodejs_runner_16' 
    env.CONTAINER_NAME = 'nodejs-16'
    env.STAGE_NAME = 'Nodejs_Build'
    env.CMD1= 'npm install'
//  env.CMD2= 'npm run build'
    env.IMAGE='node:16'
    env.COPY_CMD= './build /usr/share/nginx/html'  
} 

else if (params.TYPE == "nodejs-14")
{
    env.NODE_NAME = 'nodejs_runner_14' 
    env.CONTAINER_NAME = 'nodejs-14'
    env.STAGE_NAME = 'Nodejs_Build'
    env.CMD1= 'npm install' 
    env.CMD2= 'npm run build'
    env.IMAGE=' nginx:1.17.1-alpine' 
//we need to add "\" before "/" to skip it, basically it will act as a de-limiter
    env.COPY_CMD= '.\/build \/usr\/share\/nginx\/html' 
}
else
{
    env.NODE_NAME = 'nodejs_runner'
    env.CONTAINER_NAME = 'nodejs-12'
    env.STAGE_NAME = 'Nodejs_Build' 
    env.CMD1= 'npm install'
    env.CMD2= 'npm run build'
    env.IMAGE='nginx:1.17.1-alpine'
    env.COPY_CMD= './build /usr/share/nginx/html' 
}

node ("${env.NODE_NAME}") {
      stage('Repo_Checkout') {
             dir ('repo') {
             checkout([$class: 'GitSCM', branches: [[name: "$BRANCH"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg:  [], \
    userRemoteConfigs: [[credentialsId: 'admingithub', url: 'https://github.com/SakshiR01/$SERVICE.git', poll: 'false']]])
             }
      }
	stage("${env.STAGE_NAME}") {
	      container("${env.CONTAINER_NAME}") {
            dir ('repo'){
		    if (env.NODE_NAME == 'nodejs_runner_16'  || env.NODE_NAME == 'nodejs_runner_14' || env.NODE_NAME == 'nodejs_runner')
			    {
                  	sh 'env'
		  	        sh "${env.CMD1}"
// 			        sh "${env.CMD2}"
			    }
            }
           }
         }
       }


//Docker and Deployment Stage
node("${env.TRIVY_NODE}") {
       try {
       stage('Build_image') {
                dir ('repo') {
			container("${env.TRIVY_CONTAINER}") {
                  withCredentials([usernamePassword(credentialsId: 'docker_registry', passwordVariable: 'docker_pass', usernameVariable: 'docker_user')]) {
                //   sh 'echo TYPE is : $SERVICE'
		        //   sh 'sed -i -e "s/SERVICE/$SERVICE/g" Dockerfile deployment-type.yaml' 
                  sh 'sed -i -e "s/PORT/$PORT/g" -e "s/REGISTRY/$REGISTRY/g" -e "s/COPY_CMD/$COPY_CMD/g" Dockerfile Deployment-beta.yaml'
		          sh 'cat Dockerfile'	  
                  sh 'docker image build -f Dockerfile --build-arg REGISTRY=$REGISTRY -t registry-np.geminisolutions.com/$REGISTRY:1.0-$BUILD_NUMBER -t registry-np.geminisolutions.com/$REGISTRY .'
                  sh 'trivy image -f json registry-np.geminisolutions.com/$REGISTRY:1.0-$BUILD_NUMBER > trivy-report.json' archiveArtifacts artifacts: 'trivy-report.json', onlyIfSuccessful: true
                  sh '''docker login -u $docker_user -p $docker_pass https://registry-np.geminisolutions.com'''
                  sh 'docker push registry-np.geminisolutions.com/$REGISTRY:1.0-$BUILD_NUMBER'
                  sh 'docker push registry-np.geminisolutions.com/$REGISTRY'
                  sh 'rm -rf build/'
               }
             }
          }
       }
       stage('Deployment_stage') {
               dir ('repo') {
                   container("${env.TRIVY_CONTAINER}") {
                   kubeconfig(credentialsId: 'KubeConfigCred') {
                   sh '/usr/local/bin/kubectl apply -f deployment-beta.yaml -n dev'
                   sh '/usr/local/bin/kubectl rollout restart Deployment $REGISTRY -n dev'

                   }
                 }
               }
           }
    } finally {
         //sh 'echo current_image="registry-np.geminisolutions.com/helpdesk/server:1.0-$BUILD_NUMBER" > build.properties'
         //archiveArtifacts artifacts: 'build.properties', onlyIfSuccessful: true
         }
        }
