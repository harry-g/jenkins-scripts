#!groovy
// Searches Jenkins jobs for a String and reports occurences as html with links
//
// Jenkins Build Parameters needed:
// - Needle - what to search for
// - Type=Substring|CaseInsensitiveSubstring|Regexp - search type
// - Subdirectory - folder path in Jenkins
// - SearchByLine=true|false - use false only for regex spanning more than one line

// Additionally, set Jenkins home correctly here:
def baseDir = '/var/lib/jenkins/jobs'

def thr = Thread.currentThread()
def build = thr?.executable
Query = build.buildVariableResolver.resolve('Needle')
Type = build.buildVariableResolver.resolve('Type');
Subdirectory = build.buildVariableResolver.resolve('Subdirectory');
SearchByLine = build.buildVariableResolver.resolve('SearchByLine');
  
def jobsDir = "${baseDir}/${Subdirectory}"
def description = "<h2>$Query</h2>\n" << ''

if (Type == 'Regexp') {
  regexp = java.util.regex.Pattern.compile(Query, java.util.regex.Pattern.CASE_INSENSITIVE)
}

def matches(String line) {
  switch (Type) {
    case 'CaseInsensitiveSubstring':
      return line.toLowerCase().contains(Query.toLowerCase())
      break
    case 'Substring':
      return line.contains(Query)
      break
    case 'Regexp':
      java.util.regex.Matcher matcher = regexp.matcher(line)
      return matcher.find()
      break
  }
}
  
println "Searching for: ${Query}"
println "Type: ${Type}"
println "Search by line: ${SearchByLine}"

println "Groovy Version is: " + org.codehaus.groovy.runtime.InvokerHelper.getVersion()

new File(jobsDir).eachDirRecurse { jobDir ->
  def configXml = new File(jobDir, 'config.xml')
  if (!configXml.exists()) {
    return
  }
  if (jobDir.name.startsWith('builds')) {
    return
  }
  println "Searching in ${jobDir.name}" 
  def jobHeaderPrinted = false
  if (SearchByLine == true) {
     for (def line in configXml.readLines()) {
       if (matches(line)) {
         if (!jobHeaderPrinted) {
           description += "<h4><a href='/job/${jobDir.getAbsolutePath().substring(baseDir.length()+1).replace('//', '/').replace('/jobs', '/job')}/'>${jobDir.getAbsolutePath().substring(baseDir.length()+1).replace('//', '/').replace('/jobs/', ' &raquo; ')}</a></h4>\n"
           jobHeaderPrinted = true
         }
         if (jobDir.name.startsWith('JENKINS-INFO-')) {
           println '        Skipping JENKINS info job output...'
         } else {
           description += "<li> <pre>${line.replace('<', '&lt;')}</pre></li>\n"
         }
      }
    }
  } else { 
    if (matches(configXml.text)) {
       description += "<h4><a href='/job/${jobDir.getAbsolutePath().substring(baseDir.length()+1).replace('//', '/').replace('/jobs', '/job')}/'>${jobDir.getAbsolutePath().substring(baseDir.length()+1).replace('//', '/').replace('/jobs/', ' &raquo; ')}</a></h4>\n"
    }
  }
}

try {
  Thread.currentThread().executable.workspace.child("Search Results - ${Query}.html").write(description.toString(), 'UTF-8')
  Thread.currentThread().executable.workspace.child("Search Results.html").write(description.toString(), 'UTF-8')
} catch (Exception e) {
  // Exception never comes
  Thread.currentThread().executable.workspace.child("Search Results.html").write(description.toString(), 'UTF-8')
}