def call(Map params = [:]) {
  // start Default Arguments
  def args = [
         NEXUS_IP             : '10.1.2.210',
  ]
  args <<params
   
   // End Default + Required Arguments

  pipeline {
  agent {
    lable "${args.SLAVE_LABLE}"
  }

  environment {
    COMPONENT       =  "${args.COMPONENT}"
    NEXUS_IP        =  "${args.NEXUS_IP}"
    PROJECT_NAME    =  "${args.PROJECT_NAME}"
    SLAVE_LABLE     =  "${args.SLAVE_LABLE}"
  }

  stages {

    stage('prepare artifacts') {
      when {
        environment name: 'COMPONENT', value: 'frontend'
        
      } 


      steps {
         sh '''
           cd static
           zip -r ../${COMPONENT}.zip *
        '''     
      }  
    }

    stage('Upload Artifacts') {
      steps {
        sh '''
         curl -f -v -u admin:admin --upload-file frontend.zip http://${NEXUS_IP}:8081/repository/Frontend/frontend.zip
        '''
      }
    }
   }  
  }
} 