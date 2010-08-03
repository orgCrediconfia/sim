/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.generales.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para las firmas electrónicas.
 */
 
public class SimGeneralesFirmaElectronicaDAO extends Conexion2 implements OperacionConsultaRegistro, OperacionModificacion {

	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
	
		if (parametros.getDefCampo("MODULO").equals("CAJA")){
		
		sSql =  "SELECT \n"+
				"S.CVE_GPO_EMPRESA, \n"+
				"S.CVE_EMPRESA, \n"+
				"S.CVE_USUARIO_COORDINADOR, \n"+ 
		        //"UC.CVE_USUARIO CAJERO,  \n"+
				"P.NOM_COMPLETO NOM_COORDINADOR,  \n"+
		        //"PC.NOM_COMPLETO NOM_CAJERO, \n"+
				"TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS') AS FH_DESPLIEGUE \n"+ 
				"FROM SIM_CAT_SUCURSAL S,  \n"+
				"SIM_SUCURSAL_CAJA SC, \n"+
				"RS_GRAL_USUARIO U, \n"+
				"RS_GRAL_PERSONA P \n"+
		        //"RS_GRAL_USUARIO UC, \n"+ 
				//"RS_GRAL_PERSONA PC \n"+
				"WHERE S.CVE_GPO_EMPRESA = '"+(String)parametros.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
				"AND S.CVE_EMPRESA = '"+(String)parametros.getDefCampo("CVE_EMPRESA")+"' \n" +
				"AND SC.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+ 
				"AND SC.CVE_EMPRESA = S.CVE_EMPRESA  \n"+
				"AND SC.ID_SUCURSAL = S.ID_SUCURSAL \n"+
				"AND SC.ID_SUCURSAL ||'-'|| SC.ID_CAJA = '"+(String)parametros.getDefCampo("ID_CAJA")+"' \n" +
				"AND U.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+
				"AND U.CVE_EMPRESA = S.CVE_EMPRESA \n"+
				"AND U.CVE_USUARIO = S.CVE_USUARIO_COORDINADOR \n"+ 
				"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA = U.CVE_EMPRESA  \n"+
				"AND P.ID_PERSONA = U.ID_PERSONA \n";
				/*
		        "AND UC.CVE_PUESTO = 'Caj' \n"+
				"AND UC.CVE_USUARIO = '"+(String)parametros.getDefCampo("CVE_USUARIO_CAJERO")+"' \n" +
				"AND PC.CVE_GPO_EMPRESA = UC.CVE_GPO_EMPRESA \n"+ 
				"AND PC.CVE_EMPRESA = UC.CVE_EMPRESA \n"+
				"AND PC.ID_PERSONA = UC.ID_PERSONA \n";
				*/
		
		}else if (parametros.getDefCampo("MODULO").equals("CIERRE")){
			sSql =  "SELECT \n"+
					"U.CVE_GPO_EMPRESA, \n"+
					"U.CVE_EMPRESA, \n"+
					"U.CVE_USUARIO, \n"+
					"P.NOM_COMPLETO, \n"+
					"TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS') AS FH_DESPLIEGUE \n"+ 
					"FROM RS_GRAL_USUARIO U, \n"+
					"RS_GRAL_PERSONA P \n"+
					"WHERE U.CVE_GPO_EMPRESA = '"+(String)parametros.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
					"AND U.CVE_EMPRESA = '"+(String)parametros.getDefCampo("CVE_EMPRESA")+"' \n" +
					"AND U.CVE_USUARIO = '"+(String)parametros.getDefCampo("CVE_USUARIO")+"' \n" +
					"AND U.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					"AND U.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					"AND U.ID_PERSONA = P.ID_PERSONA \n";
		}
		
		/*
		if (parametros.getDefCampo("CVE_INTEGRANTE").equals("CAJERO")){
			sSql =  "SELECT \n"+
					"U.CVE_GPO_EMPRESA, \n"+
					"U.CVE_EMPRESA, \n"+
					"U.CVE_USUARIO, \n"+
					"U.ID_PERSONA, \n"+
					"U.CVE_PUESTO, \n"+
					"P.NOM_COMPLETO, \n"+
					"TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS') AS FH_DESPLIEGUE \n"+
					"FROM RS_GRAL_USUARIO U, \n"+
					"RS_GRAL_PERSONA P \n"+
					"WHERE U.CVE_GPO_EMPRESA = '"+(String)parametros.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
					"AND U.CVE_EMPRESA = '"+(String)parametros.getDefCampo("CVE_EMPRESA")+"' \n" +
					"AND U.CVE_PUESTO = 'Caj' \n"+
					"AND U.CVE_USUARIO = '"+(String)parametros.getDefCampo("CVE_USUARIO_CAJERO")+"' \n" +
					"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
					"AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+
					"AND P.ID_PERSONA = U.ID_PERSONA \n";
			System.out.println("valida el cajero"+sSql);
		}else if (parametros.getDefCampo("CVE_INTEGRANTE").equals("COORDINADOR")){
			sSql =  "SELECT \n"+
					"S.CVE_GPO_EMPRESA, \n"+
					"S.CVE_EMPRESA, \n"+
					"S.CVE_USUARIO_COORDINADOR, \n"+
					"P.NOM_COMPLETO, \n"+
					"TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS') AS FH_DESPLIEGUE \n"+
					"FROM SIM_CAT_SUCURSAL S, \n"+
					"SIM_SUCURSAL_CAJA SC, \n"+
					"RS_GRAL_USUARIO U, \n"+
					"RS_GRAL_PERSONA P \n"+
					"WHERE S.CVE_GPO_EMPRESA = '"+(String)parametros.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
					"AND S.CVE_EMPRESA = '"+(String)parametros.getDefCampo("CVE_EMPRESA")+"' \n" +
					"AND SC.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+
					"AND SC.CVE_EMPRESA = S.CVE_EMPRESA \n"+
					"AND SC.ID_SUCURSAL = S.ID_SUCURSAL \n"+
					"AND SC.ID_SUCURSAL ||'-'|| SC.ID_CAJA = '"+(String)parametros.getDefCampo("ID_CAJA")+"' \n" +
					"AND U.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+
					"AND U.CVE_EMPRESA = S.CVE_EMPRESA \n"+
					"AND U.CVE_USUARIO = S.CVE_USUARIO_COORDINADOR \n"+
					"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
					"AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+ 
					"AND P.ID_PERSONA = U.ID_PERSONA \n";
			System.out.println("valida el coordinador"+sSql);
		}else if (parametros.getDefCampo("CVE_INTEGRANTE").equals("GERENTE")){
			sSql =  "SELECT \n"+
					"S.CVE_GPO_EMPRESA, \n"+ 
					"S.CVE_EMPRESA, \n"+ 
					"S.CVE_USUARIO_GERENTE, \n"+ 
					"P.NOM_COMPLETO, \n"+ 
					"TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS') AS FH_DESPLIEGUE \n"+ 
					"FROM SIM_CAT_SUCURSAL S, \n"+ 
					"SIM_SUCURSAL_CAJA SC, \n"+ 
					"RS_GRAL_USUARIO U, \n"+ 
					"RS_GRAL_PERSONA P \n"+ 
					"WHERE S.CVE_GPO_EMPRESA = '"+(String)parametros.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
					"AND S.CVE_EMPRESA = '"+(String)parametros.getDefCampo("CVE_EMPRESA")+"' \n" +
					"AND SC.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+ 
					"AND SC.CVE_EMPRESA = S.CVE_EMPRESA \n"+  
					"AND SC.ID_SUCURSAL = S.ID_SUCURSAL \n"+ 
					"AND SC.ID_SUCURSAL ||'-'|| SC.ID_CAJA = '"+(String)parametros.getDefCampo("ID_CAJA")+"' \n" +
					"AND U.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+ 
					"AND U.CVE_EMPRESA = S.CVE_EMPRESA \n"+ 
					"AND U.CVE_USUARIO = S.CVE_USUARIO_GERENTE \n"+ 
					"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+  
					"AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+ 
					"AND P.ID_PERSONA = U.ID_PERSONA \n";
			System.out.println("valida el GERENTE"+sSql);
		}
		*/
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		if (registro.getDefCampo("MODULO").equals("Caja")){
			sSql =  "SELECT \n"+
					"U.CVE_GPO_EMPRESA, \n"+
					"U.CVE_EMPRESA, \n"+
					"U.CVE_USUARIO, \n"+
					"P.NOM_COMPLETO, \n"+
					"TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS') AS FH_DESPLIEGUE \n"+
					"FROM RS_GRAL_USUARIO U, \n"+
					"RS_GRAL_PERSONA P \n"+
					"WHERE U.CVE_GPO_EMPRESA = '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
					"AND U.CVE_EMPRESA = '"+(String)registro.getDefCampo("CVE_EMPRESA")+"' \n" +
					"AND U.CVE_PUESTO = 'Caj' \n"+
					"AND U.CVE_USUARIO = '"+(String)registro.getDefCampo("CVE_USUARIO_CAJERO")+"' \n" +
					"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
					"AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+
					"AND P.ID_PERSONA = U.ID_PERSONA \n"+
					"AND U.PASSWORD = '"+(String)registro.getDefCampo("PASSWORD_FIRMA") +"' \n" +
		            "UNION \n"+
		            "SELECT \n"+
					"S.CVE_GPO_EMPRESA, \n"+
					"S.CVE_EMPRESA, \n"+
					"S.CVE_USUARIO_COORDINADOR, \n"+
					"P.NOM_COMPLETO, \n"+
					"TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS') AS FH_DESPLIEGUE \n"+
					"FROM SIM_CAT_SUCURSAL S, \n"+
					"SIM_SUCURSAL_CAJA SC, \n"+
					"RS_GRAL_USUARIO U, \n"+
					"RS_GRAL_PERSONA P \n"+
					"WHERE S.CVE_GPO_EMPRESA = '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
					"AND S.CVE_EMPRESA = '"+(String)registro.getDefCampo("CVE_EMPRESA")+"' \n" +
					"AND SC.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA  \n" +
					"AND SC.CVE_EMPRESA = S.CVE_EMPRESA  \n" +
					"AND SC.ID_SUCURSAL = S.ID_SUCURSAL  \n" +
					"AND SC.ID_SUCURSAL ||'-'|| SC.ID_CAJA = '"+(String)registro.getDefCampo("ID_CAJA")+"' \n" +
					"AND U.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA  \n" +
					"AND U.CVE_EMPRESA = S.CVE_EMPRESA  \n" +
					"AND U.CVE_USUARIO = S.CVE_USUARIO_COORDINADOR  \n" +
					"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA  \n" +
					"AND P.CVE_EMPRESA = U.CVE_EMPRESA   \n" +
					"AND P.ID_PERSONA = U.ID_PERSONA  \n" +
					"AND U.PASSWORD = '"+(String)registro.getDefCampo("PASSWORD_FIRMA") +"' \n" ;
			
			ejecutaSql();
			if(rs.next()){
				resultadoCatalogo.Resultado.addDefCampo("VALIDACION_CORRECTA","SI");
			}else {
				resultadoCatalogo.Resultado.addDefCampo("VALIDACION_CORRECTA","NO");
			}
		} else if (registro.getDefCampo("MODULO").equals("Cierre")){
			sSql =  "SELECT \n"+
					"U.CVE_GPO_EMPRESA, \n"+
					"U.CVE_EMPRESA, \n"+
					"U.CVE_USUARIO, \n"+
					"P.NOM_COMPLETO, \n"+
					"TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS') AS FH_DESPLIEGUE \n"+ 
					"FROM RS_GRAL_USUARIO U, \n"+
					"RS_GRAL_PERSONA P \n"+
					"WHERE U.CVE_GPO_EMPRESA = '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
					"AND U.CVE_EMPRESA = '"+(String)registro.getDefCampo("CVE_EMPRESA")+"' \n" +
					"AND U.CVE_USUARIO = '"+(String)registro.getDefCampo("CVE_USUARIO")+"' \n" +
					"AND U.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					"AND U.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					"AND U.ID_PERSONA = P.ID_PERSONA \n"+
					"AND U.PASSWORD = '"+(String)registro.getDefCampo("PASSWORD_FIRMA") +"' \n" ;
			ejecutaSql();
			if(rs.next()){
				resultadoCatalogo.Resultado.addDefCampo("VALIDACION_CORRECTA","SI");
			}else {
				resultadoCatalogo.Resultado.addDefCampo("VALIDACION_CORRECTA","NO");
			}
		}
				/*
		String sIdBitacora = "";
		
		// SI EL ID_BITACORA NO ES IGUAL A 'null' RECUPERA EL ID_BITACORA DEL REGISTRO
		if(!registro.getDefCampo("ID_BITACORA").equals("null")){
			sIdBitacora = (String)registro.getDefCampo("ID_BITACORA");
		}
	
		// SI EL PARAMETRO DE LA BITACORA NO ES NULO ES QUE YA EXISTE EL REGISTRO
		if(registro.getDefCampo("ID_BITACORA").equals("null")){
			
			// SE DA DE ALTA EL REGISTRO EN LA BITACORA
			sSql =  "SELECT SQ01_SIISP_BIT_FIRMA.NEXTVAL ID_BITACORA FROM DUAL \n";

			ejecutaSql();
			if(rs.next()){
				sIdBitacora = rs.getString("ID_BITACORA");
			
			sSql =  "INSERT INTO SIISP_BIT_FIRMA ( \n"+
					" CVE_GPO_EMPRESA, 			\n" +
					" CVE_EMPRESA, 				\n" +
					" ID_BITACORA, 				\n"+
					" ID_USUARIO_FIRM, 			\n"+
					" ID_INTEGRANTE1, 			\n"+
					" ID_INTEGRANTE2, 			\n"+
					" FH_DESPLIEGUE, 			\n"+
					" FH_REGISTRO, 				\n"+
					" TXT_NOTA, 				\n"+
					" URL, 						\n"+
					" NUM_INTENTO_USUARIO, 		\n"+
					" NUM_INTENTO_INTEGRANTE1, 	\n"+
					" NUM_INTENTO_INTEGRANTE2) 	\n" +
				"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						   sIdBitacora + ", \n" +
					"'" + (String)registro.getDefCampo("ID_USUARIO") + "', \n" +
						  (String)registro.getDefCampo("ID_INTEGRANTE1") + ", \n" +
						  (String)registro.getDefCampo("ID_INTEGRANTE2") + ", \n" +
					"TO_DATE('" + (String)registro.getDefCampo("FH_DESPLIEGUE") + "','DD/MM/YYYY HH24:MI:SS'), \n" +
					"SYSDATE, 	\n" +
					"'" + (String)registro.getDefCampo("TX_NOTA") 	+ "', \n" +
					"'" + (String)registro.getDefCampo("URL") 		+ "', \n" +
					"0, 		\n" +
					"0, 		\n" +
					"0) 		\n" ;

				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				resultadoCatalogo.Resultado.addDefCampo("ID_BITACORA",sIdBitacora);
				
			}
		}
		*/
		/*
		// VERIFICA QUE INTEGRANTE SE VA A VALIDAR
		if(registro.getDefCampo("CVE_INTEGRANTE").equals("CAJERO")){
			sSql =  "SELECT \n"+
					"U.CVE_GPO_EMPRESA, \n"+
					"U.CVE_EMPRESA, \n"+
					"U.CVE_USUARIO, \n"+
					"U.ID_PERSONA, \n"+
					"U.CVE_PUESTO, \n"+
					"P.NOM_COMPLETO, \n"+
					"TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS') AS FH_DESPLIEGUE \n"+
					"FROM RS_GRAL_USUARIO U, \n"+
					"RS_GRAL_PERSONA P \n"+
					"WHERE U.CVE_GPO_EMPRESA = '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
					"AND U.CVE_EMPRESA = '"+(String)registro.getDefCampo("CVE_EMPRESA")+"' \n" +
					"AND U.CVE_PUESTO = 'Caj' \n"+
					"AND U.CVE_USUARIO = '"+(String)registro.getDefCampo("CVE_USUARIO_CAJERO")+"' \n" +
					"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
					"AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+
					"AND P.ID_PERSONA = U.ID_PERSONA \n"+
					"AND CVE_USUARIO = '"+(String)registro.getDefCampo("PASSWORD_FIRMA")	+"' \n" ;

			ejecutaSql();
			if(rs.next()){
				// SI COINCIDE EL PASSWORD FIRMA LA VALIDACIÓN ES CORRECTA
				
				sSql =  "	UPDATE SIISP_BIT_FIRMA SET   	\n" +
						" 		PASS_FIRMA_INTEGRANTE1 	= '"+(String)registro.getDefCampo("PASSWORD_FIRMA")	+"', \n" +
						"       NUM_INTENTO_INTEGRANTE1 = NVL(NUM_INTENTO_INTEGRANTE1,0) + 1, 	\n" +
						"       FH_REGISTRO             = SYSDATE     							\n" +
						"	WHERE   CVE_GPO_EMPRESA 	= '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
						"		AND CVE_EMPRESA 		= '"+(String)registro.getDefCampo("CVE_EMPRESA")	+"' \n" +
						"		AND ID_BITACORA 		=  " +sIdBitacora+"  \n" ;

				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				resultadoCatalogo.Resultado.addDefCampo("VALIDACION_CORRECTA","SI");
			}else{
				
				// SI NO COINCIDE EL PASSWORD SE AUMENTA EL NÚMERO DE INTENTOS
				sSql =  "	UPDATE SIISP_BIT_FIRMA SET   	\n" +
						"       NUM_INTENTO_INTEGRANTE1 = NVL(NUM_INTENTO_INTEGRANTE1,0) + 1 \n" +
						"	WHERE   CVE_GPO_EMPRESA 	= '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
						"		AND CVE_EMPRESA 		= '"+(String)registro.getDefCampo("CVE_EMPRESA")	+"' \n" +
						"		AND ID_BITACORA 		=  " +sIdBitacora+"  \n" ;

				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				resultadoCatalogo.Resultado.addDefCampo("VALIDACION_CORRECTA","NO");
				
			}
		}else{
			if(registro.getDefCampo("CVE_INTEGRANTE").equals("COORDINADOR")){
		
				sSql =  "SELECT \n"+
						"S.CVE_GPO_EMPRESA, \n"+
						"S.CVE_EMPRESA, \n"+
						"S.CVE_USUARIO_COORDINADOR, \n"+
						"P.NOM_COMPLETO, \n"+
						"TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS') AS FH_DESPLIEGUE \n"+
						"FROM SIM_CAT_SUCURSAL S, \n"+
						"SIM_SUCURSAL_CAJA SC, \n"+
						"RS_GRAL_USUARIO U, \n"+
						"RS_GRAL_PERSONA P \n"+
						"WHERE S.CVE_GPO_EMPRESA = '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
						"AND S.CVE_EMPRESA = '"+(String)registro.getDefCampo("CVE_EMPRESA")+"' \n" +
						"AND SC.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+
						"AND SC.CVE_EMPRESA = S.CVE_EMPRESA \n"+
						"AND SC.ID_SUCURSAL = S.ID_SUCURSAL \n"+
						"AND SC.ID_SUCURSAL ||'-'|| SC.ID_CAJA = '"+(String)registro.getDefCampo("ID_CAJA")+"' \n" +
						"AND U.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+
						"AND U.CVE_EMPRESA = S.CVE_EMPRESA \n"+
						"AND U.CVE_USUARIO = S.CVE_USUARIO_COORDINADOR \n"+
						"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+ 
						"AND P.ID_PERSONA = U.ID_PERSONA \n"+
						"AND CVE_USUARIO = '"+(String)registro.getDefCampo("PASSWORD_FIRMA")	+"' \n" ;

				ejecutaSql();
				if(rs.next()){
					
					//  SI COINCIDE EL PASSWORD FIRMA LA VALIDACIÓN ES CORRECTA
					sSql =  "	UPDATE SIISP_BIT_FIRMA SET   	\n" +
							" 		PASS_FIRMA_INTEGRANTE2 	= '"+(String)registro.getDefCampo("PASSWORD_FIRMA")	+"', \n" +
							"       NUM_INTENTO_INTEGRANTE2 = NVL(NUM_INTENTO_INTEGRANTE2,0) + 1, 	\n" +
							"       FH_REGISTRO             = SYSDATE     							\n" +
							"	WHERE   CVE_GPO_EMPRESA 	= '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
							"		AND CVE_EMPRESA 		= '"+(String)registro.getDefCampo("CVE_EMPRESA")	+"' \n" +
							"		AND ID_BITACORA 		=  " +sIdBitacora+"  \n" ;

					if (ejecutaUpdate() == 0){
						resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
					}
					
					resultadoCatalogo.Resultado.addDefCampo("VALIDACION_CORRECTA","SI");
				}else{
					
					// SI NO COINCIDE EL PASSWORD SE AUMENTA EL NÚMERO DE INTENTOS
					sSql =  "	UPDATE SIISP_BIT_FIRMA SET   	\n" +
							"       NUM_INTENTO_INTEGRANTE2 = NVL(NUM_INTENTO_INTEGRANTE2,0) + 1 \n" +
							"	WHERE   CVE_GPO_EMPRESA 	= '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
							"		AND CVE_EMPRESA 		= '"+(String)registro.getDefCampo("CVE_EMPRESA")	+"' \n" +
							"		AND ID_BITACORA 		=  " +sIdBitacora+"  \n" ;

					if (ejecutaUpdate() == 0){
						resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
					}
					
					resultadoCatalogo.Resultado.addDefCampo("VALIDACION_CORRECTA","NO");
				}
			}else{
				if(registro.getDefCampo("CVE_INTEGRANTE").equals("GERENTE")){
					
					sSql =  "SELECT \n"+
							"S.CVE_GPO_EMPRESA, \n"+ 
							"S.CVE_EMPRESA, \n"+ 
							"S.CVE_USUARIO_GERENTE, \n"+ 
							"P.NOM_COMPLETO, \n"+ 
							"TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS') AS FH_DESPLIEGUE \n"+ 
							"FROM SIM_CAT_SUCURSAL S, \n"+ 
							"SIM_SUCURSAL_CAJA SC, \n"+ 
							"RS_GRAL_USUARIO U, \n"+ 
							"RS_GRAL_PERSONA P \n"+ 
							"WHERE S.CVE_GPO_EMPRESA = '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
							"AND S.CVE_EMPRESA = '"+(String)registro.getDefCampo("CVE_EMPRESA")+"' \n" +
							"AND SC.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+ 
							"AND SC.CVE_EMPRESA = S.CVE_EMPRESA \n"+  
							"AND SC.ID_SUCURSAL = S.ID_SUCURSAL \n"+ 
							"AND SC.ID_SUCURSAL ||'-'|| SC.ID_CAJA = '"+(String)registro.getDefCampo("ID_CAJA")+"' \n" +
							"AND U.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+ 
							"AND U.CVE_EMPRESA = S.CVE_EMPRESA \n"+ 
							"AND U.CVE_USUARIO = S.CVE_USUARIO_GERENTE \n"+ 
							"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+  
							"AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+ 
							"AND P.ID_PERSONA = U.ID_PERSONA \n"+
							"AND U.CVE_USUARIO = '"+(String)registro.getDefCampo("PASSWORD_FIRMA")	+"' \n" ;
					ejecutaSql();
					if(rs.next()){
						
						//  SI COINCIDE EL PASSWORD FIRMA LA VALIDACIÓN ES CORRECTA
						sSql =  "	UPDATE SIISP_BIT_FIRMA SET   	\n" +
								" 		PASS_FIRMA_USUARIO	 	= '"+(String)registro.getDefCampo("PASSWORD_FIRMA")	+"', \n" +
								"       NUM_INTENTO_USUARIO 	= NVL(NUM_INTENTO_USUARIO,0) + 1, 	\n" +
								"       FH_REGISTRO             = SYSDATE     							\n" +
								"	WHERE   CVE_GPO_EMPRESA 	= '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
								"		AND CVE_EMPRESA 		= '"+(String)registro.getDefCampo("CVE_EMPRESA")	+"' \n" +
								"		AND ID_BITACORA 		=  " +sIdBitacora+"  \n" ;

						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
						resultadoCatalogo.Resultado.addDefCampo("VALIDACION_CORRECTA","SI");
					}else{
						
						// SI NO COINCIDE EL PASSWORD SE AUMENTA EL NÚMERO DE INTENTOS
						sSql =  "	UPDATE SIISP_BIT_FIRMA SET   	\n" +
								"       NUM_INTENTO_USUARIO     = NVL(NUM_INTENTO_USUARIO,0) + 1 \n" +
								"	WHERE   CVE_GPO_EMPRESA 	= '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
								"		AND CVE_EMPRESA 		= '"+(String)registro.getDefCampo("CVE_EMPRESA")	+"' \n" +
								"		AND ID_BITACORA 		=  " +sIdBitacora+"  \n" ;

						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
						
						resultadoCatalogo.Resultado.addDefCampo("VALIDACION_CORRECTA","NO");
					}
				}
			}
		}
		*/
		return resultadoCatalogo;
	}


}

