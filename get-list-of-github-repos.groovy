//Creates an active choice parameter in Jenkins containing a list of repository names from a specified GitHub organization
import jenkins.*
import jenkins.model.* 
import hudson.*
import hudson.model.*
def jenkinsCredentials = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
        com.cloudbees.plugins.credentials.Credentials.class,
        Jenkins.instance,
        null,
        null
);

//Assumes we have the credentials for an access token secret saved to Jenkins Credentials. 

for (creds in jenkinsCredentials) {
    if(creds.id == "jenkins-release-token"){
    token = creds.secret
    }
}

list = []


// 1..10 being the number of pages that the organization might have

for (i in 1..10) {
  def command = """
  curl -H 'Authorization: token ${token}' https://api.github.com/orgs/${org}/repos?page=${i}
  """
  process = [ 'bash', '-c',  command].execute()
  
  list += new groovy.json.JsonSlurper().parseText(process.text).name
}

return list.sort()

// Use regex to be selective and filter out some repos if needed.
//newList = list.findAll { it =~ /^([DO]{1,2}\d{1,3})$/ }
//return newList.sort()
