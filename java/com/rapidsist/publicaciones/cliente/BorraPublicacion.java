package com.rapidsist.publicaciones.cliente;

import java.io.File;

//BORRA LA PUBLICACION DE ACUERDO AL PATH
public class BorraPublicacion {

	public void BorraArchivo(String sPath) {
		File inputFile = new File(sPath);
		inputFile.delete();
    }
}