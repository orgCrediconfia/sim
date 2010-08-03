/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.generales.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.sql.SQLException;
import java.util.Random;

/**
 * Administra los accesos a la base de datos para el catálogo de quejas y sugerencias.
 */
public class QuejaSugerenciaDAO extends Conexion2 implements OperacionAlta,OperacionConsultaRegistro {

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		boolean bInserta = true;
		Random random = new Random();
		int iResultado = 0;

		while (bInserta){		
			//OBTIENE UN NUMERO ALEATORIO DE QUEJA/SUGERENCIA
			
			iResultado = random.nextInt(99999999);
			//VALIDA QUE EL NUMERO SEA MAYOR A CERO
			if (iResultado>0){
				String sResultado = Integer.toString(iResultado);
				registro.addDefCampo("NUM_QUEJA_SUGERENCIA",sResultado);
				//VERIFICA SI EL NUMERO YA EXISTE EN LA TABLA
				Registro resultado = getRegistro(registro);
				if (resultado == null){
					//EL NUMERO DE QUEJA/SUGERENCIA NO EXISTE
					sSql = "INSERT INTO RS_GRAL_QUEJA ( "+
						   "CVE_GPO_EMPRESA, \n" +
						   "CVE_EMPRESA, \n" +
						   "ID_QUEJA, \n" +
						   "NUM_QUEJA_SUGERENCIA, \n" +
						   "NOMBRE_COMPLETO, \n" +
						   "CLAVE_PRODUCTO, \n" +
						   "F_COMPRA, \n" +
						   "CLAVE_LOTE, \n" +
						   "EMAIL, \n" +
						   "NOMBRE_DISTRIBUIDOR, \n" +
						   "QUEJA_SUGERENCIA, \n" +
						   "SOLUCION, \n" +
						   "ESTATUS, \n" +
						   "F_REGISTRO) \n" +
						" VALUES (" +
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" +(String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"SQ01_RS_GRAL_QUEJA.nextval, \n" +
							(String)registro.getDefCampo("NUM_QUEJA_SUGERENCIA") + ", \n"+
							"'" + (String)registro.getDefCampo("NOMBRE_COMPLETO") + "', \n"+
							"'" + (String)registro.getDefCampo("CLAVE_PRODUCTO") + "', \n" +
							"TO_DATE('" + registro.getDefCampo("F_COMPRA") + "','DD/MM/YYYY'), \n"+
							"'" + (String)registro.getDefCampo("CLAVE_LOTE") + "', \n" +
							"'" + (String)registro.getDefCampo("EMAIL") + "', \n" +
							"'" + (String)registro.getDefCampo("NOMBRE_DISTRIBUIDOR") + "', \n" +
							"'" + (String)registro.getDefCampo("QUEJA_SUGERENCIA") + "', \n" +
							"'" + (String)registro.getDefCampo("SOLUCION") + "', \n" +
							"'" + (String)registro.getDefCampo("ESTATUS") + "', \n" +
							"SYSDATE) \n";
						
					//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
					if (ejecutaUpdate() == 0){
						resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
					}
		
					resultadoCatalogo.Resultado = new Registro();
					//VALORES PARA REGRESAR A LA JSP
					resultadoCatalogo.Resultado.addDefCampo("NOMBRE_COMPLETO", (String)registro.getDefCampo("NOMBRE_COMPLETO"));
					resultadoCatalogo.Resultado.addDefCampo("NUM_QUEJA_SUGERENCIA", sResultado);
	
					bInserta = false;
				}//VERIFICA SI EL NUMERO YA EXISTE EN LA TABLA
			}//VALIDA QUE EL NUMERO SEA MAYOR A CERO
		}//WHILE
		return resultadoCatalogo;
	}
	
	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql = "SELECT * FROM RS_GRAL_QUEJA \n" +
			   "WHERE CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   "AND CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   "AND NUM_QUEJA_SUGERENCIA=" + (String)parametros.getDefCampo("NUM_QUEJA_SUGERENCIA") + "\n";
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}