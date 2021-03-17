//Creates an active choice reactive parameter in Jenkins containing a list of tag names from a GitHub repository
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

//Grab values from credentials keychain
for (creds in jenkinsCredentials) {
  if(creds.id == "github-bot-account"){
    user = creds.username
    pwd = creds.password
    }
}


def gettags = ("git ls-remote -t -h https://${user}:${pwd}@github.com/${org}/${Course}.git").execute()
tagslist = gettags.text.readLines().collect {
it.split()[1].replaceAll('refs/heads/', '').replaceAll('refs/tags/', '').replaceAll("\\^\\{\\}", '')
}

newtagslist = tagslist.findAll {it =~ /^(main|[A-Z])/}
