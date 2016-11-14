#!groovy

def call(cocoa, white, sugar, nuts, almonds, orange, vanilla, chili, fairtrade, organic) {
    node ('Tools:production') {
        def log = sh (script:"""#!/bin/bash
        white () {
            if $white; then 
                echo ' white'
            fi
        }
        cocoaType() {
            echo 'cocoa '
            if $white; then 
                echo 'butter'
            else
                echo 'mass'
            fi
        }
        fairtrade() {
            if $fairtrade; then 
                echo ' fairtrade'
            fi
        }
        organic() {
            if $organic; then 
                echo ' organic'
            fi
        }

        if [ \$((cocoa + sugar)) -gt 100 ]; then
            echo ERROR: invalid recipe! Sum of coca and sugar must at max. 100%
            exit 0
        fi

        echo Building\$(fairtrade)\$(organic)\$(white) chocolate with $cocoa% cocoa and $sugar% sugar...
        echo This is a\$(fairtrade)\$(organic)\$(white) chocolate bar consisting of > chocolate.choc
        echo $cocoa% \$(cocoaType) >> chocolate.choc

        if [ $cocoa -ge 70 ]; then
            echo WARNING: we might have a shortage of cocoa soon!
        fi

        echo $sugar% sugar >> chocolate.choc

        if $nuts; then 
            echo adding nuts...
            echo nuts >> chocolate.choc
        fi
        if $almonds; then 
            echo adding almonds...
            echo almonds >> chocolate.choc
        fi
        if $orange; then 
            echo adding orange oil...
            echo orange oil >> chocolate.choc
        fi
        if $vanilla; then 
            echo adding vanilla...
            echo vanilla >> chocolate.choc
        fi
        if $chili; then 
            echo adding chili...
            echo chili >> chocolate.choc
        fi
        """, returnStdout: true).trim()
        echo log
        
        def warnings=0
        def errors=0
        currentBuild.description = ''

        // check all build log lines
        for (def line: log.split('\n')) {    
        
           def matcher = (line =~ /Building (.* with .*).../) 
           if (matcher.matches()) {
              currentBuild.description += matcher[0][1] + '\n'
           }
        
           matcher =  (line =~ /^[\[]?ERROR:?\s?(.*)/)
           if (matcher.matches()) {
              errors++
              currentBuild.description += matcher[0][1] + '\n'
              println "Found ERROR - setting build to FAILURE!"
              manager.buildFailure()
           }
        
           matcher = (line =~ /^[\[]?WARNING:?\s?(.*)/) 
           if (matcher.matches()) {
              warnings++
              currentBuild.description += matcher[0][1] + '\n'
              if (manager.build.result != hudson.model.Result.FAILURE) {
                 println "Found WARNING - setting build to UNSTABLE!"
                 manager.buildUnstable()
              }
           }
        
        }

        if (errors) {
            def msg = "$errors ERROR(s) found"
            manager.createSummary('error.gif').appendText(msg, true)
            manager.addErrorBadge(msg)
            throw new Exception('Errors found - aborting pipeline!')
        }
        else if (warnings) {
            def msg = "$warnings WARNING(s) found"
            manager.createSummary('warning.gif').appendText(msg, true)
            manager.addWarningBadge(msg)
        }
    }
}
