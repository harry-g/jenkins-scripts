#!groovy
// this is for Groovy Postbuild only

def build = manager.build
build.description = ''

def warnings=0
def errors=0

// check all build log lines
for (def line: build.logFile) {    

   // write summary
   def matcher = (line =~ /Building (.* with .*).../) 
   if (matcher.matches()) {
      build.description += matcher[0][1] + '\n'
   }

   // collect errors and warnings
   matcher =  (line =~ /^[\[]?ERROR:?\s?(.*)/)
   if (matcher.matches()) {
      errors++
      // add message line to build description
      build.description += matcher[0][1] + '\n'
      println "Found ERROR - setting build to FAILURE!"
      manager.buildFailure()
   }

   matcher = (line =~ /^[\[]?WARNING:?\s?(.*)/) 
   if (matcher.matches()) {
      warnings++
      // add message line to build description
      build.description += matcher[0][1] + '\n'
      // only set result if worse
      if (manager.build.result != hudson.model.Result.FAILURE) {
         println "Found WARNING - setting build to UNSTABLE!"
         manager.buildUnstable()
      }
   }

}

// add status icon with number of errors/warnings
if (errors) {
   def msg = "$errors ERROR(s) found"
   manager.createSummary("error.gif").appendText(msg, true)
   manager.addErrorBadge(msg)
}
else if (warnings) {
   def msg = "$warnings WARNING(s) found"
   manager.createSummary("warning.gif").appendText(msg, true)
   manager.addWarningBadge(msg)
}
