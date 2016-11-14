#!/bin/bash
echo '# chocolate configuration' > config.ini
echo cocoa=$cocoa >> config.ini
echo white=$white >> config.ini
echo sugar=$sugar >> config.ini
echo nuts=$nuts >> config.ini
echo almonds=$almonds >> config.ini
echo orange=$orange >> config.ini
echo vanilla=$vanilla >> config.ini
echo chili=$chili >> config.ini
echo fairtrade=$fairtrade >> config.ini
echo organic=$organic >> config.ini
echo '' >> config.ini
echo '# test configuation' >> config.ini
echo acceptanceCriteria=$acceptanceCriteria >> config.ini
echo '' >> config.ini
echo '# packaging configuration' >> config.ini
echo noAluminium=$noAluminium >> config.ini
echo boxSize=$boxSize >> config.ini
echo hauler=$hauler >> config.ini

echo '// packaging configuration' > config.groovy
echo noAluminium=$noAluminium >> config.groovy
echo boxSize=$boxSize >> config.groovy
echo hauler=\'$hauler\' >> config.groovy