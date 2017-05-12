package com.proximus.servicing;

def checkoutRepositories(def repoPrefix, def repos){

    println '##### Checkout Repositories #####'
    for(i=0;i<repos.size();i++){
        dir('./' + repos[i][0]){
            println '### REPOSITORY ##### ' + repos[i][0] + ', ' + repos[i][1]
            git url: repoPrefix + '/' + repos[i][0], branch:repos[i][1], credentialsId: 'c0271329-b7a9-4963-a007-17e0cb4197f4'
            
            if(repos[i][0] == 'magnolia-author'){
                dir('./magnolia-author-webapp/src/main/webapp/WEB-INF/config/jboss10004/mow_staging/'){ //to be removed, when the correct properties will be commited
                    sh 'pwd'    
                    sh 'cp /msservice/app/config/jboss10004/mow_staging/log4j.xml .'
                    sh 'cp /msservice/app/config/jboss10004/mow_staging/magnolia.properties .' 
                }
            }
        }
    }
    
}

def buildSnapshots(def repos){
    
    println '##### Build Snapshots #####'
    for(i=0;i<repos.size();i++){
        dir(repos[i][0]){
          println '##### REPO DIR ##### --->' + repos[i][0]
          sh 'mvn clean deploy -B -U --fail-at-end'
        }
    }
    
}

def generateTestReport(){
    
    println '##### Generate Test Report #####'
    junit '**/target/**/TEST*.xml'
        
}

def deployOnDEV(){
   
    sshagent (credentials: ['0ab2516e-597a-438b-aec8-7ae6c908f3d2']){
        println '##### Promote TO DEV  #####' 

        dir('./magnolia-author'){
            pom = readMavenPom file: 'pom.xml'
            authorCurrentVersion = pom.version   
            println '##### Magnolia Author Artifact --> ' + pom.artifactId + ', CurrentVersion --> ' + authorCurrentVersion
            
            sh "find . -name ${pom.artifactId}-${authorCurrentVersion}.ear -exec scp {} mowdeploy@el1628.bc:/jon/release/mow-staging/mow-staging-${authorCurrentVersion}.ear \\;"
            println '##### Secure Copying of magnolia-author-' + authorCurrentVersion + '.ear done.'
            sh 'ssh mowdeploy@el1628.bc pwd'
            sh "ssh -tt mowdeploy@el1628.bc sudo -u jbossadmin /jon/admin/bin/devOperations.sh -a mow-staging -e itt -o deploy -v ${authorCurrentVersion}"                        
        }

        dir('./iportal-project'){
            pom = readMavenPom file: 'pom.xml'
            iportalCurrentVersion = pom.version;   

            println '##### Secure Copying of iportal-project-' + iportalCurrentVersion + '.ear done.'
        }

        dir('./myproximus-project'){
            pom = readMavenPom file: 'pom.xml'
            myproximusCurrentVersion = pom.version;   

            println '##### Secure Copying of myproximus-project-' + myproximusCurrentVersion + '.ear done.'
        }

        dir('./magnolia-msw'){
            pom = readMavenPom file: 'pom.xml'
            mswCurrentVersion = pom.version;   

            println '##### Secure Copying of magnolia-msw-' + mswCurrentVersion + '.ear done.'
        }

        dir('./magnolia-cdn'){
            pom = readMavenPom file: 'pom.xml'
            cdnCurrentVersion = pom.version;   

            println '##### Secure Copying of magnolia-cdn-' + cdnCurrentVersion + '.ear done.'
        }
    }
    
}

/*
*sshCredentials = 0ab2516e-597a-438b-aec8-7ae6c908f3d2
*username = mowdeploy
*servername = el1628.bc
*destDir = /jon/release/mow-staging/
*/
def deployOnDEV(def sshCredentials, def username, def servername, def destDir){

    sshagent (credentials: [sshCredentials]){
        def server = username + '@' + servername

        println '##### Promote TO DEV  #####' 

        dir('./magnolia-author'){
            pom = readMavenPom file: 'pom.xml'
            authorCurrentVersion = pom.version   
            println '##### Magnolia Author Artifact --> ' + pom.artifactId + ', CurrentVersion --> ' + authorCurrentVersion
            
            sh "find . -name ${pom.artifactId}-${authorCurrentVersion}.ear -exec scp {} ${server}:${destDir}mow-staging-${authorCurrentVersion}.ear \\;"
            println '##### Secure Copying of magnolia-author-' + authorCurrentVersion + '.ear done.'
            sh 'ssh mowdeploy@el1628.bc pwd'
            sh "ssh -tt mowdeploy@el1628.bc sudo -u jbossadmin /jon/admin/bin/devOperations.sh -a mow-staging -e itt -o deploy -v ${authorCurrentVersion}"                        
        }

        dir('./iportal-project'){
            pom = readMavenPom file: 'pom.xml'
            iportalCurrentVersion = pom.version;   

            println '##### Secure Copying of iportal-project-' + iportalCurrentVersion + '.ear done.'
        }

        dir('./myproximus-project'){
            pom = readMavenPom file: 'pom.xml'
            myproximusCurrentVersion = pom.version;   

            println '##### Secure Copying of myproximus-project-' + myproximusCurrentVersion + '.ear done.'
        }

        dir('./magnolia-msw'){
            pom = readMavenPom file: 'pom.xml'
            mswCurrentVersion = pom.version;   

            println '##### Secure Copying of magnolia-msw-' + mswCurrentVersion + '.ear done.'
        }

        dir('./magnolia-cdn'){
            pom = readMavenPom file: 'pom.xml'
            cdnCurrentVersion = pom.version;   

            println '##### Secure Copying of magnolia-cdn-' + cdnCurrentVersion + '.ear done.'
        }
    }
    
}

def sayHelloToName(def name){
    println "Hello" + name;
}
