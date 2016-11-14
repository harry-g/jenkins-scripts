# Simple Harddisk Monitoring with Jenkins

## Visualize used Space
* run build.sh in a Jenkins job on any Linux machine
* achive artifacts *.gz
* open with TKDU https://github.com/daniel-beck/tkdu (python needed)
* left-click to dive in, right-click to dive out

## Send Warning when Disk Space below threshold
* after running above build.sh
* copy build.groovy into build step system groovy (example for at least 10% space left)
* send mail on unstable build

Credits to https://github.com/daniel-beck