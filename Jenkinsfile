properties([
    parameters([
	    choice(name: "TYPE", choices: ["java-11", "java-17", "java-18"], description: "LANGUAGES"),
        choice(name: "SERVICE", choices: ["Centralized-git-repo"], description: "services to be build"),
        choice(name: "PORT", choices: ["8081", "80", "8080", "8999"], description: "port to be used"),
    ])
])

env.BRANCH="*/main"
env.MAINTAINER= "sakshi.raghav@geminisolutions.com"

env.REGISTRY= params.SERVICE.toLowerCase()
env.TRIVY_NODE = 'image_builder_trivy'
env.TRIVY_CONTAINER = 'docker-image-builder-trivy'

if(params.TYPE == "java-11")
{
    env.NODE_NAME = 'maven_runner_java11'
    env.CONTAINER_NAME = 'maven-runner-11'
    env.STAGE_NAME = 'maven_Build'
    env.CMD1= 'rm -rf target'
    // env.CMD2= 'mvn package'
    env.IMAGE='adoptopenjdk/openjdk11'
    env.COPY_CMD= 'target/$SERVICE*.?ar /home/' 
    env.WORKDIR_CMD='/src'
}

node("${env.NODE_NAME}") {
      stage('Repo_Checkout') {
             dir ('repo') {
             checkout([$class: 'GitSCM', branches: [[name: "$BRANCH"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg:  [], \
    userRemoteConfigs: [[credentialsId: 'admingithub', url: 'https://github.com/SakshiR01/$SERVICE.git', poll: 'false']]])
             }
      }
	stage("${env.STAGE_NAME}") {
	      container("${env.CONTAINER_NAME}") {
            dir ('repo'){
		    if (env.NODE_NAME == 'maven_runner_java11'  || env.NODE_NAME == 'maven_runner_java17' || env.NODE_NAME == 'maven-runner-18')
			    {
                    sh 'env'
		  	        sh "${env.CMD1}"
			        // sh "${env.CMD2}"
		 	        dir ("${env.SERVICE}/target"){
		            sh 'pwd'
//                 	    sh 'chmod +x *.?ar'
			    }
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
                  sh 'sed -i -e "s/SERVICE/$SERVICE/g" -e "s/PORT/$PORT/g"  -e "s/REGISTRY/$REGISTRY/g" -e "s/IMAGE/$IMAGE/g" -e "s/MAINTAINER/$MAINTAINER/g" -e "s/COPY_CMD/$COPY_CMD/g" -e "s/WORKDIR_CMD/$WORKDIR_CMD/g" Dockerfile deployment-beta.yaml' 
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


