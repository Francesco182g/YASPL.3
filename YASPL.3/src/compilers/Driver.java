package compilers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import Semantic.SemanticVisitor;
import Syntax.ProgramOp;
import ToC.ToCVisitor;
import Visitor.XMLVisitor;

class Driver {

	public static void main(String[] args) {
		System.out.println("Apro il file..");
		String filen = "text1Calc";
		File file = new File(filen+".yasp");
		try {
			System.out.println("Analisi Lessicale");
			Lexer lexer = new Lexer(new FileInputStream(file));
			Parser parser = new Parser(lexer);
			//Analisi Sintattica
			ProgramOp root = (ProgramOp) parser.parse().value;
			//Visitor XML
			String xmlSource = root.accept(new XMLVisitor()).toString();
			FileWriter fw = new FileWriter("output.xml");
			fw.write(xmlSource);
			fw.close();

			//Analisi Semantica
			root.accept(new SemanticVisitor());
			//Visitor XML
			root.accept(new ToCVisitor(filen));

			//Lancio del file C
			Runtime runtime = Runtime.getRuntime(); //Runtime
			try
			{
				//Tempo di attesa...1000ms
				Thread.sleep(1000);

				//Auto Compilazione
				System.out.println("Avvio la compilazione e l'esecuzione di: "+filen+".c");
				String path = "cmd I: /c start gcc "+filen+".c  -o "+filen+".exe"+" \n";
				runtime.exec(path);
				//Tempo di attesa...2000ms
				Thread.sleep(2000);

				System.out.println("Indent");
				//Solo se ECLIPSE è amministratore! Indent
				runtime.exec("cmd I: /c start indent "+filen+".c  -o "+filen+".c"+" \n");

				//Esecuzione
				System.out.println("Esecuzione... "+filen+".exe");
				runtime.exec("cmd /C start "+filen+".exe ");

				//EMCC Only manual
				//System.out.println("Start EMCC, Wait, wait and wait...");
				//runtime.exec("emcc.bat I: /C start");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}	          
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/*
 * Progetto creato da Francesco Garofalo,
 * Disponibile interamente su Git,
 * Si prega di citare in caso di utilizzo.
 * 
 * Dedicato ad Anna Tomeo.
 */

