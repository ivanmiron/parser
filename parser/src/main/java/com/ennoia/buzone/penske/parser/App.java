package com.ennoia.buzone.penske.parser;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	FileScanner parser = new FileScanner(args[0]);
		try {
			parser.processFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
