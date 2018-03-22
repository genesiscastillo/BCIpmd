package cl.bci.pmd;

import java.io.FileInputStream;
import java.io.IOException;

public class Test1 {
	public void doSomething() {
		try {
			FileInputStream fis = new FileInputStream("/tmp/bugger");
		} catch (IOException ioe) {

		}
	}

}