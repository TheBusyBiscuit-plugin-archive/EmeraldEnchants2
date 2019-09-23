package me.mrCookieSlime.EmeraldEnchants;

public class RomanNumberConverter {
	
	public static int convertRoman(String roman) {
		 boolean number = true;
		 try {
			 Integer.parseInt(roman);
		 } catch(NumberFormatException x) {
			 number = false;
		 }
		 
		 if (number) return Integer.parseInt(roman);
		 else {
			 int result = 0;
			 int mediator = 0;
			 
			 for (int i = 0; i < roman.length() - 1; i++) {
				 if (getInt(roman.charAt(i)) > getInt(roman.charAt(i + 1))) {
					 result = result + getInt(roman.charAt(i)) + mediator;
					 mediator = 0;
				 }
				 else if (getInt(roman.charAt(i)) == getInt(roman.charAt(i + 1))) mediator = mediator + getInt(roman.charAt(i));
				 else if (getInt(roman.charAt(i)) < getInt(roman.charAt(i + 1))) mediator = -mediator - getInt(roman.charAt(i));
			 }
			 return result + mediator + getInt(roman.charAt(roman.length() - 1));
		 }
	}
		 
	
	private static int getInt(char romanChar) {
		if (romanChar == 'I') return 1;
		else if (romanChar == 'V') return 5;
		else if (romanChar == 'X') return 10;
		else if (romanChar == 'L') return 50;
		else if (romanChar == 'C') return 100;
		else if (romanChar == 'D') return 500;
		else if (romanChar == 'M') return 1000;
		else return 0;
	}
	
	public static String convertNumber(int input) {
	    if (input > 3999) return String.valueOf(input);
	    if (!EmeraldEnchants.getInstance().getCfg().getBoolean("roman-numerals")) return String.valueOf(input);
	    String s = "";
	    while (input >= 1000) {
	        s += "M";
	        input -= 1000;        
	    }
	    while (input >= 900) {
	        s += "CM";
	        input -= 900;
	    }
	    while (input >= 500) {
	        s += "D";
	        input -= 500;
	    }
	    while (input >= 400) {
	        s += "CD";
	        input -= 400;
	    }
	    while (input >= 100) {
	        s += "C";
	        input -= 100;
	    }
	    while (input >= 90) {
	        s += "XC";
	        input -= 90;
	    }
	    while (input >= 50) {
	        s += "L";
	        input -= 50;
	    }
	    while (input >= 40) {
	        s += "XL";
	        input -= 40;
	    }
	    while (input >= 10) {
	        s += "X";
	        input -= 10;
	    }
	    while (input >= 9) {
	        s += "IX";
	        input -= 9;
	    }
	    while (input >= 5) {
	        s += "V";
	        input -= 5;
	    }
	    while (input >= 4) {
	        s += "IV";
	        input -= 4;
	    }
	    while (input >= 1) {
	        s += "I";
	        input -= 1;
	    }    
	    return s;
	}
	
}
    
