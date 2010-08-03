/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.generales.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de contactos.
 */
 
public class ContactoDAO extends Conexion2 {
//ESTE CODIGO SIRVER PARA QUE LA ALTA NO SEA AUTOMATICA	
//public class ContactoDAO extends Conexion2 implements OperacionAlta {


	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql = "INSERT INTO RS_GRAL_CONTACTO ( "+
					"CVE_GPO_EMPRESA, \n" +
					"ID_CONTACTO, \n" +
					"NOMBRE_COMPLETO, \n" +
					"EMAIL, \n" +
					"DIRECCION, \n" +
					"COLONIA, \n" +
					"CIUDAD, \n" +
					"ESTADO, \n" +
					"CODIGO_POSTAL, \n" +
					"TELEFONO, \n" +
					"FAX, \n" +
					"COMENTARIOS) \n" +
				" VALUES (" +
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"SQ01_RS_GRAL_CONTACTO.nextval, \n" +
					"'" + (String)registro.getDefCampo("NOMBRE_COMPLETO") + "', \n" +
					"'" + (String)registro.getDefCampo("EMAIL") + "', \n" +
					"'" + (String)registro.getDefCampo("DIRECCION") + "', \n" +
					"'" + (String)registro.getDefCampo("COLONIA") + "', \n" +
					"'" + (String)registro.getDefCampo("CIUDAD") + "', \n" +
					"'" + (String)registro.getDefCampo("ESTADO") + "', \n" +
					"'" + (String)registro.getDefCampo("CODIGO_POSTAL") + "', \n" +
					"'" + (String)registro.getDefCampo("TELEFONO") + "', \n" +
					"'" + (String)registro.getDefCampo("FAX") + "', \n" +
					"'" + (String)registro.getDefCampo("COMENTARIOS") + "')";
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}



}