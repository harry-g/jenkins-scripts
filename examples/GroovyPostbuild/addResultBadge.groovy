#!groovy
// this is for Groovy Postbuild only

// check for Warning text, make build unstable & add badge
if (manager.logContains(&apos;^WARNING.*&apos;)) {
   manager.buildUnstable()
   manager.addWarningBadge(&apos;this seems not tasty&apos;)
// else if not failed, add OK badge   
} else if (manager.build.result != hudson.model.Result.FAILURE) {
   manager.addBadge(&apos;completed.gif&apos;, &apos;yummy chocolate&apos;)
}