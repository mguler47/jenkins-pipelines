node {
        stage("Stage1"){
                echo "hello"
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
        stage("Send Email to Support"){
                mail bcc: '', body: 'the job has been complete ', cc: 'support@gmail.com', from: '', replyTo: '', subject: 'pipeline job', to: 'mguler@gmail.com'
        }
}
