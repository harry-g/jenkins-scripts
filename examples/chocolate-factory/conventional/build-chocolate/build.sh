#!/bin/bash
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

if [ $((cocoa + sugar)) -gt 100 ]; then
	echo ERROR: invalid recipe! Sum of coca and sugar must at max. 100%
    exit 1
fi

echo "Building$(fairtrade)$(organic)$(white) chocolate with $cocoa% cocoa and $sugar% sugar..."
echo This is a$(fairtrade)$(organic)$(white) chocolate bar consisting of > chocolate.choc
echo $cocoa% $(cocoaType) >> chocolate.choc

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