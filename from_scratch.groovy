node {
	// "Below line sets "Discard Builds more than 5"
	properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')),
	
	// "Below lines triggers this job every mintute"
	pipelineTriggers([cron('* * * * *')])
	])
	stage("Pull Repo"){
		git    'https://github.com/farrukh90/cool_website.git'
}
	stage("Stage2"){
		echo "hello"
}
	stage("Stage3"){
		echo "hello"
}
	stage("Stage4"){
		echo "hello"
}
	
	stage("Stage5"){
		echo "hello"
	}
}
