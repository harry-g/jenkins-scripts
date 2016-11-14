#!groovy
// following line is for Groovy Postbuild only (remove for System Groovy)
def build = manager.build

// check build log lines
for (def line: build.logFile) { 
   if (line =~ /^[\[]?ERROR.*/) {
      println "Found ERROR - setting build to FAILURE!"
      build.result = hudson.model.Result.FAILURE
      // stop after first error found
      break
   }
   if (line =~ /^[\[]?WARNING.*/) {
      if (build.result < hudson.model.Result.UNSTABLE) {
         println "Found WARNING - setting build to UNSTABLE!"
         build.result = hudson.model.Result.UNSTABLE
         // continue checking on warnings to find errors
      }
   }
}
