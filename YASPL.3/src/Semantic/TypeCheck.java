package Semantic;

public class TypeCheck {

	/*
	 * checkInt: Verifica il tipo di operatore e il tipo di valore assegnatogli
	 * typeId = tipo dell'identificatore, val= valore da assegnare alla variabile
	 * 
	 * Verifica prima che il tipo della variabile sia un int oppure un double
	 * Se è double o int allora la variabile a cui può assegnarlo può essere Int o double
	 */
	public static void checkInt(String typeId, String val) throws Exception {
		try {
			if((typeId.equals("INT_CONST")) || (typeId.equals("DOUBLE_CONST"))) {
				Integer.parseInt(val);
				Double.parseDouble(val);
			} else {
				throw new Exception("ERRORE SEMANTICO: Type Mismatch Impossibile assegnare a " + typeId +" Valore: " +val);		
			}
		} catch(NumberFormatException e) {
			//e.printStackTrace();
			throw new Exception("NumberFormatException");
		}
		catch(Exception e){
			//e.printStackTrace();
			throw new Exception("Correlazione non corrispondente");
		}
	}

	/*
	 * checkDouble: Verifica il tipo di operatore e il tipo del valore assegnatogli
	 * typeId = tipo dell'identificatore, val= valore da assegnare alla variabile
	 * 
	 * Verifica prima che il tipo della variabile sia un double
	 * Se è Double allora la variabile a cui è assegnato può essere solo DOUBLE
	 * 
	 */
	public static void checkDouble(String typeId, String val) throws Exception {
		try {
			if(typeId.equals("DOUBLE_CONST")) {
				Double.parseDouble(val);
			} else {
				throw new Exception("ERRORE SEMANTICO: Type Mismatch Impossibile assegnare a " + typeId +" Valore: " +val);		
			}
		} catch(NumberFormatException e) {
			//e.printStackTrace();
			throw new Exception("NumberFormatException");
			//System.exit(0);
		}
		catch(Exception e){
			//e.printStackTrace();
			throw new Exception("Correlazione non corrispondente: "+typeId+ " "+val);
			//System.exit(0);
		}
	}

	/*
	 * checkString: Verifica il tipo di operatore e il tipo del valore assegnatogli
	 * typeId = tipo dell'identificatore, val= valore da assegnare alla variabile
	 * 
	 * Verifica prima che il tipo della variabile sia una String
	 * Se è String allora la variabile a cui è assegnato può essere solo String
	 * 
	 */
	public static void checkString(String typeId, String val) throws Exception {
		try {
			if(typeId.equals("STRING_CONST")) {

			}else {
				throw new Exception("ERRORE SEMANTICO: Type Mismatch Impossibile assegnare ad " + typeId +" Valore: " +val);		
			}
		}catch(Exception e) {
			throw new Exception("ERRORE SEMANTICO");
		}
	}

	/*
	 * checkChar: Verifica il tipo di operatore e il tipo del valore assegnatogli
	 * typeId = tipo dell'identificatore, val= valore da assegnare alla variabile
	 * 
	 * Verifica prima che il tipo della variabile sia una Char
	 * Se è Char allora la variabile a cui è assegnato può essere solo Char
	 * 
	 */
	public static void checkChar(String typeId, String val) throws Exception {
		try {
			if(typeId.equals("CHAR_CONST")) {
				if(val.length() != 1) {
					throw new Exception("ERRORE SEMANTICO:  Impossibile assegnare ad \" + typeId +\" Valore: \" +val");
				}
			}else {
				throw new Exception("ERRORE SEMANTICO: Impossibile assegnare ad " + typeId +" Valore: " +val);		
			}
		}catch(Exception e) {
			throw new Exception("ERRORE SEMANTICO: Impossibile assegnare ad "+ typeId+ " Valore: "+val);
		}
	}
	
	
	/*
	 * checkBool: Verifica il tipo di operatore e il tipo del valore assegnatogli
	 * typeId = tipo dell'identificatore, val= valore da assegnare alla variabile
	 * 
	 * Verifica prima che il tipo della variabile sia una Char
	 * Se è Char allora la variabile a cui è assegnato può essere solo Char
	 * 
	 */
	public static void checkBool(String typeId, String val) throws Exception {
		try {
			if(typeId.equals("BOOL_CONST")) {
				throw new Exception("ERRORE SEMANTICO: Type Mismatch Impossibile assegnare ad \" + typeId +\" Valore: \" +val");
			}else {
				throw new Exception("ERRORE SEMANTICO: Type Mismatch Impossibile assegnare ad " + typeId +" Valore: " +val);		
			}
		}catch(Exception e) {
			throw new Exception("ERRORE SEMANTICO");
		}
	}
}
