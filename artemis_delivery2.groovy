 node {
	properties(
		[parameters(
		[choice(choices: 
		[
		'version/0.1', 
		'version/0.2', 
		'version/0.3', 
		'version/0.4', 
		'version/0.5'], 
	description: 'Which version of the app should I deploy? ', 
	name: 'Version'), 
	choice(choices: 
	[
		'dev1.senamina.com', 
		'qa1.senamina.com', 
		'stage1.senamina.com', 
		'prod1.senamina.com'], 
	description: 'Please provide an environment to build the application', 
	name: 'ENVIR')])])
	stage("Stage1"){
		timestamps {
			ws {
				checkout([$class: 'GitSCM', branches: [[name: '${Version}']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/fuchicorp/artemis.git']]])
		    }
	    }
    }
	stage("Get Credentials"){
		timestamps {
			ws{
				sh '''
					aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 549828394506.dkr.ecr.us-east-1.amazonaws.com/artemis
					'''
	        }
	    }
    }
	stage("Build Docker Image"){
		timestamps {
			ws {
				sh '''
					docker build -t artemis:${Version} .
					'''
		    }
	    }
    }
	stage("Tag Image"){
		timestamps {
			ws {
				sh '''
					docker tag artemis:${Version} 549828394506.dkr.ecr.us-east-1.amazonaws.com/artemis:latest
					'''
		    }
	    }
    }
	stage("Push Image"){
		timestamps {
			ws {
				sh '''
					docker push 549828394506.dkr.ecr.us-east-1.amazonaws.com/artemis:latest
					'''
		    }
	    }
    }
	stage("Send slack notifications"){
		timestamps {
			ws {
				echo "Slack"
				//slackSend color: '#BADA55', message: 'Hello, World!'
			}
		}
	}

	stage("Clean Up"){
			timestamps {
				ws {
					try {
						sh '''
							#!/bin/bash
							IMAGES=$(ssh centos@dev1.senamina.com docker ps -aq) 
							for i in \$IMAGES; do
								ssh centos@dev1.senamina.com docker stop \$i
								ssh centos@dev1.senamina.com docker rm \$i
							done 
							'''
					} catch(e) {
						println("Script failed with error: ${e}")
				        }
			        }
		        }
            }


        stage("Run Container"){
		timestamps {
			ws {
				sh '''
					ssh centos@dev1.senamina.com docker run -dti -p 5001:5000 549828394506.dkr.ecr.us-east-1.amazonaws.com/artemis:${Version}
					'''
			}
		}
	}

}
