#!groovy

def call(acceptanceCriteria) {
    stage ('Acceptance Tests') {

        node ('Tools:test') {
            def log = sh (script:"""#!/bin/bash
                findContents() {
                    grep $acceptanceCriteria chocolate.choc
                }
                echo Testing chocolate for $acceptanceCriteria
                if [ -z \$(findContents) ]; then
                    echo WARNING: no $acceptanceCriteria found
                else
                    echo yummy chocolate!
                fi""", returnStdout: true).trim()
            echo log

            def jUnitXml ='''<testsuite tests="3">
        <testcase classname="Optical" name="OpticalTest"/>
        <testcase classname="Taste" name="TasteTest"/>
        <testcase classname="Ingredients" name="IngredientTest"'''

            if (log.contains('WARNING')) {
                manager.buildUnstable()
                manager.addBadge('delete.gif', 'ingredients missing!')
                jUnitXml += ">\n        <failure type=\"IngredientMissing\">Ingredient $acceptanceCriteria missing</failure>\n    </testcase>\n"
            } else if (manager.build.result != hudson.model.Result.FAILURE) {
                manager.addBadge('completed.gif', 'yummy chocolate')
                jUnitXml += '/>\n'
            }

            jUnitXml += '</testsuite>'

            writeFile file:'test.xml', text:jUnitXml
            junit 'test.xml'
        }

    }
}