#!/bin/bash
echo -e '<testsuite tests="3">
    <testcase classname="Optical" name="OpticalTest"/>
    <testcase classname="Taste" name="TasteTest"/>
    <testcase classname="Ingredients" name="IngredientTest">' > test.xml

findContents() {
	grep $acceptanceCriteria chocolate.choc
}
echo Testing chocolate for $acceptanceCriteria
if [ -z $(findContents) ]; then
	echo WARNING: no $acceptanceCriteria found
    echo -e '        <failure type="IngredientMissing">Ingredient '$acceptanceCriteria' missing</failure>\n    </testcase>' >> test.xml
else
	echo yummy chocolate!
    echo -e '</testcase>' >> test.xml
fi

echo '</testsuite>' >> test.xml
