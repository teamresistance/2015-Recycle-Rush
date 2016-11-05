package org.teamResistance.robot15;

public class Util {
	
    public static double span( double input, double inLo, double inHi, double outLo, double outHi) {
    	if ( input < inLo ){
    		return outLo;
    	} else if ( input > inHi ) {
    		return outHi;
    	} else {
    		return (outLo + (((input - inLo) / (inHi - inLo)) * (outHi - outLo)));
    	}
    }
	
}
