/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.personas.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.OperacionBaja; 
import com.rapidsist.portal.catalogos.OperacionModificacion; 
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para la direcciones del cliente.
 */
 
public class SimClienteReferenciaDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =	" SELECT \n"+
			        "CR.CVE_GPO_EMPRESA, \n"+
			        "CR.CVE_EMPRESA, \n"+ 
			        "CR.ID_PERSONA, \n"+
			        "CR.ID_REFERENCIA, \n"+
			        "CR.FECHA_VERIFICACION, \n"+ 
			        "CR.CVE_VERIFICADOR, \n"+
			        "CR.TIPO_REFERENCIA, \n" +
			        "CR.ID_RESULTADO_VERIFICACION, \n"+
			        "R.NOM_RESULTADO_VERIFICACION, \n"+
			        "P.AP_PATERNO, \n"+
			        "P.AP_MATERNO, \n"+
			        "P.NOMBRE_1, \n"+
			        "P.NOMBRE_2, \n"+
			        "P.NOM_COMPLETO, \n"+
			        "PU.NOM_COMPLETO USUARIO, \n"+
			        "D.ID_DOMICILIO, \n"+
			        "D.CALLE, \n"+
			        "D.NUMERO_INT, \n"+
			        "D.NUMERO_EXT, \n"+
			        "D.CODIGO_POSTAL, \n"+
			        "D.ID_REFER_POST, \n"+
			        "D.NOM_ASENTAMIENTO, \n"+
			        "D.NOM_DELEGACION, \n"+
			        "D.NOM_CIUDAD, \n"+
			        "D.NOM_ESTADO \n"+
			"FROM SIM_CLIENTE_REFERENCIA CR, \n"+
			"     RS_GRAL_PERSONA P, \n"+
			"     RS_GRAL_PERSONA PU, \n"+
			"     SIM_CAT_RESULTADO_VERIFICACION R, \n"+
			"     RS_GRAL_DOMICILIO D \n"+
			"WHERE CR.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CR.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND CR.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			"AND P.CVE_GPO_EMPRESA = CR.CVE_GPO_EMPRESA \n"+
			"AND P.CVE_EMPRESA = CR.CVE_EMPRESA \n"+
			"AND P.ID_PERSONA = CR.ID_REFERENCIA \n"+
			"AND PU.CVE_GPO_EMPRESA = CR.CVE_GPO_EMPRESA \n"+
			"AND PU.CVE_EMPRESA = CR.CVE_EMPRESA \n"+
			"AND PU.ID_PERSONA = CR.ID_PERSONA \n"+
			"AND R.CVE_GPO_EMPRESA (+)= CR.CVE_GPO_EMPRESA \n"+
			"AND R.CVE_EMPRESA (+)= CR.CVE_EMPRESA \n"+
			"AND R.ID_RESULTADO_VERIFICACION (+)= CR.ID_RESULTADO_VERIFICACION \n"+
			"AND D.CVE_GPO_EMPRESA = CR.CVE_GPO_EMPRESA \n"+
			"AND D.CVE_EMPRESA = CR.CVE_EMPRESA \n"+
			"AND D.IDENTIFICADOR = CR.ID_REFERENCIA \n";
			
		ejecutaSql();
		return getConsultaLista();
	}

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
	
		sSql =	" SELECT \n"+
			        "CR.CVE_GPO_EMPRESA, \n"+
			        "CR.CVE_EMPRESA, \n"+ 
			        "CR.ID_PERSONA, \n"+
			        "CR.ID_REFERENCIA, \n"+
			        "CR.FECHA_VERIFICACION, \n"+ 
			        "CR.CVE_VERIFICADOR, \n"+
			        "CR.TIPO_REFERENCIA, \n" +
			        "CR.ID_RESULTADO_VERIFICACION, \n"+
			        "R.NOM_RESULTADO_VERIFICACION, \n"+
			        "P.AP_PATERNO, \n"+
			        "P.AP_MATERNO, \n"+
			        "P.NOMBRE_1, \n"+
			        "P.NOMBRE_2, \n"+
			        "P.NOM_COMPLETO, \n"+
			        "PU.NOM_COMPLETO USUARIO, \n"+
			        "D.ID_DOMICILIO, \n"+
			        "D.CALLE, \n"+
			        "D.NUMERO_INT, \n"+
			        "D.NUMERO_EXT, \n"+
			        "D.CODIGO_POSTAL, \n"+
			        "D.ID_REFER_POST, \n"+
			        "D.NOM_ASENTAMIENTO, \n"+
			        "D.NOM_DELEGACION, \n"+
			        "D.NOM_CIUDAD, \n"+
			        "D.NOM_ESTADO \n"+
			"FROM SIM_CLIENTE_REFERENCIA CR, \n"+
			"     RS_GRAL_PERSONA P, \n"+
			"     RS_GRAL_PERSONA PU, \n"+
			"     SIM_CAT_RESULTADO_VERIFICACION R, \n"+
			"     RS_GRAL_DOMICILIO D \n"+
			"WHERE CR.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CR.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND CR.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			"AND CR.ID_REFERENCIA = '" + (String)parametros.getDefCampo("ID_REFERENCIA") + "' \n"+
			"AND P.CVE_GPO_EMPRESA = CR.CVE_GPO_EMPRESA \n"+
			"AND P.CVE_EMPRESA = CR.CVE_EMPRESA \n"+
			"AND P.ID_PERSONA = CR.ID_REFERENCIA \n"+
			"AND PU.CVE_GPO_EMPRESA = CR.CVE_GPO_EMPRESA \n"+
			"AND PU.CVE_EMPRESA = CR.CVE_EMPRESA \n"+
			"AND PU.ID_PERSONA = CR.ID_PERSONA \n"+
			"AND R.CVE_GPO_EMPRESA (+)= CR.CVE_GPO_EMPRESA \n"+
			"AND R.CVE_EMPRESA (+)= CR.CVE_EMPRESA \n"+
			"AND R.ID_RESULTADO_VERIFICACION (+)= CR.ID_RESULTADO_VERIFICACION \n"+
			"AND D.CVE_GPO_EMPRESA = CR.CVE_GPO_EMPRESA \n"+
			"AND D.CVE_EMPRESA = CR.CVE_EMPRESA \n"+
			"AND D.IDENTIFICADOR = CR.ID_REFERENCIA \n";
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdReferencia = "";
		String sIdDomicilio = "";
		
		//OBTENEMOS EL SEQUENCE
		sSql = "SELECT SQ01_RS_GRAL_PERSONA.nextval as ID_PERSONA FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdReferencia = rs.getString("ID_PERSONA");
		}

		sSql = "INSERT INTO RS_GRAL_PERSONA ( "+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"ID_PERSONA, \n" +
			"AP_PATERNO, \n" +
			"AP_MATERNO, \n" +
			"NOMBRE_1, \n" +
			"NOMBRE_2, \n" +
			"NOM_COMPLETO, \n" +
			"B_PERSONA_FISICA) \n" +
		" VALUES (" +
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			sIdReferencia + ", \n" +
			"'" + (String)registro.getDefCampo("AP_PATERNO") + "', \n" +
			"'" + (String)registro.getDefCampo("AP_MATERNO") + "', \n" +
			"'" + (String)registro.getDefCampo("NOMBRE_1") + "', \n" +
			"'" + (String)registro.getDefCampo("NOMBRE_2") + "', \n" +
			"'" + (String)registro.getDefCampo("NOM_COMPLETO") + "', \n" +
			"'V') \n" ;
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_REFERENCIA", sIdReferencia);
		
		/*
		sSql =  "INSERT INTO SIM_TIPO_PERSONA ( "+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"ID_PERSONA, \n" +
			"CVE_TIPO_PERSONA) \n" +		
			" VALUES (" +
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			sIdReferencia + ", \n" +
			"'REFERENCIA') \n" ;
			
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		*/
		
		//OBTENEMOS EL SEQUENCE
		sSql = "SELECT SQ01_RS_GRAL_DOMICILIO.nextval as ID_DOMICILIO FROM DUAL";
			ejecutaSql();
			if (rs.next()){
				sIdDomicilio = rs.getString("ID_DOMICILIO");
			}

			//INSERTA EL REGISTRO DE DOMICILIO PARA PODER OBTENER EL ID_DOMICILIO DEL SEQUENCE
			sSql = "INSERT INTO RS_GRAL_DOMICILIO ( "+
				   "CVE_GPO_EMPRESA, \n" +
				   "CVE_EMPRESA, \n" +
				   "ID_DOMICILIO, \n" +
				   "CALLE, \n" +
				   "NUMERO_INT, \n" +
				   "NUMERO_EXT, \n" +
				   "CODIGO_POSTAL, \n" +
				   "ID_REFER_POST, \n" +
				   "NOM_ASENTAMIENTO, \n" +
				   "TIPO_ASENTAMIENTO, \n" +
				   "NOM_DELEGACION, \n" +
				   "NOM_CIUDAD, \n" +
				   "NOM_ESTADO, \n" +
				   "IDENTIFICADOR) \n" +
				" VALUES (" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				" " + sIdDomicilio +", \n" +
				"'" + (String)registro.getDefCampo("CALLE") + "', \n" +
				"'" + (String)registro.getDefCampo("NUMERO_INT") + "', \n" +
				"'" + (String)registro.getDefCampo("NUMERO_EXT") + "', \n" +
				"'" + (String)registro.getDefCampo("CODIGO_POSTAL") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_REFER_POST") + "', \n" +
				"'" + (String)registro.getDefCampo("NOM_ASENTAMIENTO") + "', \n" +
				"'" + (String)registro.getDefCampo("TIPO_ASENTAMIENTO") + "', \n" +				
				"'" + (String)registro.getDefCampo("NOM_DELEGACION") + "', \n" +
				"'" + (String)registro.getDefCampo("NOM_CIUDAD") + "', \n" +
				"'" + (String)registro.getDefCampo("NOM_ESTADO") + "', \n" +
				" " + sIdReferencia +") \n" ;

		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_DOMICILIO", sIdDomicilio);
	
		sSql = "INSERT INTO SIM_CLIENTE_REFERENCIA ( "+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PERSONA, \n" +
				"ID_REFERENCIA, \n" +
				"TIPO_REFERENCIA, \n" +
				"FECHA_VERIFICACION, \n" +
				"CVE_VERIFICADOR, \n" +
				"ID_RESULTADO_VERIFICACION) \n" +
			" VALUES (" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PERSONA") + "', \n" +
				sIdReferencia + ", \n" +
				"'" + (String)registro.getDefCampo("TIPO_REFERENCIA") + "', \n" +
				"TO_DATE('" + (String)registro.getDefCampo("FECHA_VERIFICACION") + "','DD/MM/YYYY'), \n" +
				"'" + (String)registro.getDefCampo("CVE_VERIFICADOR") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_RESULTADO_VERIFICACION") + "') \n" ;
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
	
		return resultadoCatalogo;
	}

	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		//ACTUALIZA LA PERSONA
		sSql =  "UPDATE RS_GRAL_PERSONA SET "+
				"AP_PATERNO = '" + (String)registro.getDefCampo("AP_PATERNO") + "', \n" +
				"AP_MATERNO = '" + (String)registro.getDefCampo("AP_MATERNO") + "', \n" +
				"NOMBRE_1 = '" + (String)registro.getDefCampo("NOMBRE_1") + "', \n" +
				"NOMBRE_2 = '" + (String)registro.getDefCampo("NOMBRE_2") + "', \n" +
				"NOM_COMPLETO = '" + (String)registro.getDefCampo("NOM_COMPLETO") + "' \n" +
			"WHERE ID_PERSONA = '" + (String)registro.getDefCampo("ID_REFERENCIA")+ "' \n" +
			"AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
			"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA")+"' \n" ;
	
		//VERIFICA SI NO SE ACTUALIZÓ EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		
		//ACTUALIZA EL DOMICILIO
		sSql = " UPDATE RS_GRAL_DOMICILIO SET "+
			     " CALLE ='" + (String)registro.getDefCampo("CALLE") + "', \n" +
			     " NUMERO_INT='" + (String)registro.getDefCampo("NUMERO_INT") + "', \n" +
			     " NUMERO_EXT='" + (String)registro.getDefCampo("NUMERO_EXT") + "', \n" +
			     " CODIGO_POSTAL ='" + (String)registro.getDefCampo("CODIGO_POSTAL") + "', \n" +
			     " ID_REFER_POST ='" + (String)registro.getDefCampo("ID_REFER_POST") + "', \n" +
			     " NOM_ASENTAMIENTO ='" + (String)registro.getDefCampo("NOM_ASENTAMIENTO") + "', \n" +
			     " TIPO_ASENTAMIENTO ='" + (String)registro.getDefCampo("TIPO_ASENTAMIENTO") + "', \n" +
			     " NOM_DELEGACION ='" + (String)registro.getDefCampo("NOM_DELEGACION") + "', \n" +
			     " NOM_CIUDAD ='" + (String)registro.getDefCampo("NOM_CIUDAD") + "', \n" +
			     " NOM_ESTADO ='" + (String)registro.getDefCampo("NOM_ESTADO") + "' \n" +    
		   	" WHERE ID_DOMICILIO = '" + (String)registro.getDefCampo("ID_DOMICILIO") + "' \n" +
		   	" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
		   	" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" ;
		   	
		   	
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		
		sSql = " UPDATE SIM_CLIENTE_REFERENCIA SET "+
		     " FECHA_VERIFICACION = TO_DATE('" + (String)registro.getDefCampo("FECHA_VERIFICACION") + "','DD/MM/YYYY'), \n" +
		     " CVE_VERIFICADOR='" + (String)registro.getDefCampo("CVE_VERIFICADOR") + "', \n" +
		     " ID_RESULTADO_VERIFICACION='" + (String)registro.getDefCampo("ID_RESULTADO_VERIFICACION") + "' \n" +
		     " WHERE ID_PERSONA = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" +
		     " AND ID_REFERENCIA = '" + (String)registro.getDefCampo("ID_REFERENCIA") + "' \n" +
		     " AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
		     " AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" ;
		  	   	
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
			
		return resultadoCatalogo;
	}

		/**
		 * Borra un registro.
		 * @param registro Llave primaria.
		 * @return Objeto que contiene el resultado de la ejecución de este método.
		 * @throws SQLException Si se genera un error al accesar la base de datos.
		 */
		public ResultadoCatalogo baja(Registro registro) throws SQLException{
			ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
			sSql =  " DELETE FROM SIM_CLIENTE_REFERENCIA " +
			   	" WHERE ID_PERSONA 	  ='" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" +
			   	" AND ID_REFERENCIA = '" + (String)registro.getDefCampo("ID_REFERENCIA") + "' \n" +
			   	" AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   	" AND CVE_EMPRESA  ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			
			/*
			sSql =  " DELETE FROM RS_GRAL_DOMICILIO " +
			   	" WHERE ID_DOMICILIO 	  ='" + (String)registro.getDefCampo("ID_DOMICILIO") + "' \n" +
			   	" AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   	" AND CVE_EMPRESA  ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			
			sSql =  " DELETE FROM RS_GRAL_PERSONA " +
			   	" WHERE ID_PERSONA 	  ='" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" +
			   	" AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   	" AND CVE_EMPRESA  ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			*/
		return resultadoCatalogo;
	}

}