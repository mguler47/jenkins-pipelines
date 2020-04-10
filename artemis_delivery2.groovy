 node {
	properties(
		[parameters(
			[choice(choices: 
			[
				'0.1', 
				'0.2', 
				'0.3', 
				'0.4', 
				'0.5'], 
	description: 'Which version of the app should I deploy? ', 
	name: 'Version')])])
	stage("Stage1"){
		timestamps {
			ws {
				checkout([$class: 'GitSCM', branches: [[name: 'dev']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/fuchicorp/artemis.git']]])
		}
	}
}
	stage("Get Credentials"){
		timestamps {
			ws{
				sh '''
					$(aws ecr get-login --no-include-email --region us-east-1)
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
					docker tag artemis:${Version} 713287746880.dkr.ecr.us-east-1.amazonaws.com/artemis:${Version}
					'''
		}
	}
}
	stage("Push Image"){
		timestamps {
			ws {
				sh '''
					docker push 713287746880.dkr.ecr.us-east-1.amazonaws.com/artemis:${Version}
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
}
