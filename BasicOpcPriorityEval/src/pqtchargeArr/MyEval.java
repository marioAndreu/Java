package pqtchargeArr;
import java.util.ArrayList;

/*
*---------------*DOCUMENTATION*---------------
*-- Class Name:BasicOpcPriorityEval
*-- Version: 3.0
*-- Date: 15/01/2023
*-- Author: Mario Andreu Beltran
*-- Location: Barcelona, Catalonia. SPAIN
*-- Description:  Class that evaluates a string of characters by performing +, -, *, /, applying the precedence of operations. You can receive positive and negative numbers.
*-- Way of operating: The operation priority is carried out from left to right, first the * and /, and to finish the + and - are carried out.
*-- Use of negative numbers: Add symbol - followed by the operation symbol that precedes it. Example: 4*2+-1*-2  or  -4*2+-1*-2
*-- The method works successfully as long as the composition rules of the String to be evaluated are followed. 
		** Examples: -4*2/130+234--45/89*20000  400*-2/-130*2983634--45/89*20000/2
*-- Limitations:
*-- Divisions by zero or negative zero returns Infinity.
*-- Divisions zero by zero returns NaN.
*-- Operations to be carried out in a fully automated way, being possible to carry out the number of operations and types of operations that you want, taking into account the limits of Infinity. 
*-- Result size limit 9.0E37 y -9.0E37. After that Infinity skips.
*-- Unlimited maximum number of operations with quantity, Infinity will represent the end of the operation.
*-- It is possible that there are other limitations and some error not found by me. 
*-- I leave at your disposal the task of carrying out more exhaustive tests.
 */

public class MyEval {	
	private String concatenation = "";
	private ArrayList<String> arrOpcPre = new ArrayList();
	private boolean active_negatives = false;
	private boolean active_symbol = false;
	private boolean passOne = true;
	private boolean passTwo = true;
	private boolean passThree = true;
	private boolean sub = false;
	private float num_one = 0.0f, num_two = 0.0f, auxRes = 0.0f;
	private float pre_result = 0;
	private float pro_result = 0.0f;
	private float result = 0.0f;
	private int iter = 0;
	private int remov = 0;
	private int xSymbol = 0;
	private int nS = 0, nR = 0, nM = 0, nD = 0;
	private int iter_null = 0;
	private int keyy = 0;
	
	
	public float evalStr(String operation) {	// METHOD EVALUATE
		for (int b = 0; b < operation.length(); b++) { // PREPARE STR TO ARRAYLIST
			if (b == 0 && operation.charAt(b) == '-') {
				concatenation += operation.charAt(b);
				active_negatives = true;
				continue;

			} else if (b == 0) {
				active_negatives = true;
				concatenation += operation.charAt(b);
				continue;
			}

			else if (active_symbol && operation.charAt(b) == '-') {
				arrOpcPre.add(concatenation);
				concatenation = "";
				concatenation += operation.charAt(b);
				active_symbol = false;
				continue;

			} else if (operation.charAt(b) == '+' || operation.charAt(b) == '-' || operation.charAt(b) == '*'
					|| operation.charAt(b) == '/') {
				arrOpcPre.add(concatenation);
				concatenation = "";
				concatenation += operation.charAt(b);
				active_symbol = true;
				continue;

			} else if (operation.charAt(b) != '+' && operation.charAt(b) != '-' && operation.charAt(b) != '*'
					&& operation.charAt(b) != '/') {
				if (active_symbol) {
					arrOpcPre.add(concatenation);
					concatenation = "";
					concatenation += operation.charAt(b);
					if(b == operation.length() - 1) {
						arrOpcPre.add(concatenation);
						break;
					}
					active_symbol = false;
					continue;

				} else if (!active_symbol) {
					if (b == operation.length() - 1) {
						concatenation += operation.charAt(b);
						arrOpcPre.add(concatenation);
						break;
					} else {
						concatenation += operation.charAt(b);
					}
					continue;
				}
			}
		}

		for (String e : arrOpcPre) {
			if (e.equals("+")) {nS++;};
			if (e.equals("-")) {nR++;};
			if (e.equals("*")) {nM++;};
			if (e.equals("/")) {nD++;};
		}
		if (nS > 0 && nR == 0 && nM == 0 && nD == 0) {////////////////////// JUST ADD
			for (int u = 0; u < arrOpcPre.size(); u++) {
				remov++;
				if (iter == 0) {
					pre_result += Float.parseFloat(arrOpcPre.get(u));
					pro_result = pre_result;
					iter++;
				} else {
					if (arrOpcPre.get(u).equals("+")) {
						pre_result += Float.parseFloat(arrOpcPre.get(u + 1));
						pro_result = pre_result;
					}
				}
			}
			passOne = false;
			passTwo = false;
			passThree = false;
		}
		if (nR > 0 && nS == 0 && nM == 0 && nD == 0) {////////////////////// JUST SUBTRACT
			for (int u = 0; u < arrOpcPre.size(); u++) {
				remov++;
				if (iter == 0) {
					pre_result += Float.parseFloat(arrOpcPre.get(u));
					pro_result = pre_result;
					iter++;
				} else {
					if (arrOpcPre.get(u).equals("-")) {
						pre_result -= Float.parseFloat(arrOpcPre.get(u + 1));
						pro_result = pre_result;
					}
				}
			}
			passOne = false;
			passTwo = false;
			passThree = false;
		}
		if (nM > 0 && nD == 0 && nS == 0 && nR == 0) {////////////////////// JUST MULTY
			for (int y = 0; y < arrOpcPre.size(); y++) {
				if (arrOpcPre.get(y).equals("*")) {
					xSymbol = arrOpcPre.indexOf("*");
					num_one = Float.parseFloat(arrOpcPre.get(0));
					num_two = Float.parseFloat(arrOpcPre.get(y + 1));
					auxRes = num_one * num_two;
					arrOpcPre.add(0, String.valueOf(auxRes));
					arrOpcPre.remove(1);
					arrOpcPre.remove(1);
					nM--;
					if (nM == 0 && nD == 0) {
						pro_result = Float.parseFloat(arrOpcPre.get(0));
					}
				}
			}
			passOne = false;
			passTwo = false;
		}
		if (nD > 0 && nM == 0 && nS == 0 && nR == 0) {////////////////////// JUST DIVIDE
			for (int y = 0; y < arrOpcPre.size(); y++) {
				if (arrOpcPre.get(y).equals("/")) {
					xSymbol = arrOpcPre.indexOf("/");
					num_one = Float.parseFloat(arrOpcPre.get(0));
					num_two = Float.parseFloat(arrOpcPre.get(y + 1));
					auxRes = num_one / num_two;
					arrOpcPre.add(0, String.valueOf(auxRes));
					arrOpcPre.remove(1);
					arrOpcPre.remove(1);
					nD--;
					if (nM == 0 && nD == 0) {
						pro_result = Float.parseFloat(arrOpcPre.get(0));
					}
				}
			}
			passOne = false;
			passTwo = false;
		}

		if (passOne) {

			if (nM == 0 && nD == 0) {////////////////////// JUST ADD & SUBTRACT
				for (int u = 0; u < arrOpcPre.size(); u++) {
					remov++;
					if (iter == 0) {
						pre_result += Float.parseFloat(arrOpcPre.get(u));
						pro_result = pre_result;
						iter++;
					} else {
						if (arrOpcPre.get(u).equals("+")) {
							pre_result += Float.parseFloat(arrOpcPre.get(u + 1));
							pro_result = pre_result;
						}
						if (arrOpcPre.get(u).equals("-")) {
							pre_result -= Float.parseFloat(arrOpcPre.get(u + 1));
							pro_result = pre_result;
						}
					}
				}
				passTwo = false;
				passThree = false;
			} else if (nS == 0 && nR == 0) {////////////////////// JUST MULTY & DIVIDE
				for (int u = 0; u < arrOpcPre.size(); u++) {
					if (arrOpcPre.get(u).equals("*")) {
						xSymbol = arrOpcPre.indexOf("*");
						num_one = Float.parseFloat(arrOpcPre.get(0));
						num_two = Float.parseFloat(arrOpcPre.get(u + 1));
						auxRes = num_one * num_two;
						arrOpcPre.add(0, String.valueOf(auxRes));
						arrOpcPre.remove(1);
						arrOpcPre.remove(1);
						nM--;
						if (nM == 0 && nD == 0) {
							pro_result = Float.parseFloat(arrOpcPre.get(0));
						}
					}
					if (arrOpcPre.get(u).equals("/")) {
						xSymbol = arrOpcPre.indexOf("/");
						num_one = Float.parseFloat(arrOpcPre.get(0));
						num_two = Float.parseFloat(arrOpcPre.get(u + 1));
						auxRes = num_one / num_two;
						arrOpcPre.add(0, String.valueOf(auxRes));
						arrOpcPre.remove(1);
						arrOpcPre.remove(1);
						nD--;
						if (nM == 0 && nD == 0) {
							pro_result = Float.parseFloat(arrOpcPre.get(0));
						}
					}
				}
				passTwo = false;
			}
		}
		if (passTwo) {////////////////////// USE COMBINATION OF AT LEAST THREE SYMBOLS		
			for (int u = 1; u <= nM + nD; u++) {
				for (int v = 1; v < arrOpcPre.size(); v++) {
					if (arrOpcPre.get(v).equals("*") || arrOpcPre.get(v).equals("/")) {
						if (arrOpcPre.get(v).equals("*") || arrOpcPre.get(v).equals("/")) {
							if (arrOpcPre.get(v).equals("*")) {
								float num1 = Float.parseFloat(arrOpcPre.get(v - 1));
								float num2 = Float.parseFloat(arrOpcPre.get(v + 1));
								result = num1 * num2;
								arrOpcPre.add(v-1, null);
								arrOpcPre.add(v, null);
								arrOpcPre.add(v, null);
								sub=true;
								keyy=v-1;
								break;
							} else if (arrOpcPre.get(v).equals("/")) {
								float num1 = Float.parseFloat(arrOpcPre.get(v - 1));
								float num2 = Float.parseFloat(arrOpcPre.get(v + 1));
								result = num1 / num2;
								arrOpcPre.add(v, null);
								arrOpcPre.add(v - 1, null);
								arrOpcPre.add(v + 1, null);
								sub=true;
								keyy=v-1;
								break;
							}
						}
					}
				}
				boolean auxLlave= false;
				if (sub) {							
					for(int a2=0; a2<arrOpcPre.size();a2++) {
						String auxStr = String.valueOf(arrOpcPre.get(a2));
						if(auxStr == "null") {
							if(auxLlave) {
								
							}else{
								arrOpcPre.remove(a2);
								arrOpcPre.remove(a2);
								arrOpcPre.remove(a2);
								arrOpcPre.remove(a2);
								arrOpcPre.remove(a2);
								arrOpcPre.remove(a2);
								arrOpcPre.add(a2, String.valueOf(result));		
							}	
						}					
					}	
					iter_null=0;			
					sub=false;
				}
			}
		}	
		if(passThree) {
			for (int u = 0; u < arrOpcPre.size(); u++) {
				remov++;
				if (iter == 0) {
					pre_result += Float.parseFloat(arrOpcPre.get(u));
					pro_result = pre_result;
					iter++;
				} else {
					if (arrOpcPre.get(u).equals("+")) {
						pre_result += Float.parseFloat(arrOpcPre.get(u + 1));
						pro_result = pre_result;
					}
					if (arrOpcPre.get(u).equals("-")) {
						pre_result -= Float.parseFloat(arrOpcPre.get(u + 1));
						pro_result = pre_result;
					}
				}
			}
		}
		return pro_result;		
	}

	public static void main(String[] args) {
		String operation = "2*2*2*2";
		MyEval opc = new MyEval();
		System.out.println("Resultado calculo: "+opc.evalStr(operation));
	}
}

























