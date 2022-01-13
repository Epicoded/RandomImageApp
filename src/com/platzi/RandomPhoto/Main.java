package com.platzi.RandomPhoto;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		int optionMenu = -1;
		String[] bottom = { "1. ver fotos", "2. salir" };

		do {
			//menu principal
			String option = (String) JOptionPane.showInputDialog(null, "Photo Java", "Main menu",
					JOptionPane.INFORMATION_MESSAGE, null, bottom, bottom[0]);
			
			//validacion de seleccion del usuario
			for(int i = 0; i<bottom.length; i++) {
				
				if(option.equals(bottom[i])) {
					optionMenu = i;
				}
			}
			
			switch(optionMenu) {
			
			case 0:
				PhotoService.seeImage();
				break;
			default:
				break;
			}
		} while (optionMenu != 1);
	}

}
