if (manager.logContains('^WARNING.*')) {
   manager.buildUnstable()
   manager.addWarningBadge('this seems not tasty')
} else if (manager.build.result != hudson.model.Result.FAILURE) {
   manager.addBadge('completed.gif', 'yummy chocolate')
}