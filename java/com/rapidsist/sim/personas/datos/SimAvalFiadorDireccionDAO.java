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
 * Administra los accesos a la base de datos para la direcciones del aval o fiador.
 */
 
public class SimAvalFiadorDireccionDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT \n"+
			"	CVE_GPO_EMPRESA, \n"+
			"	CVE_EMPRESA, \n"+
			"	ID_DOMICILIO, \n"+
			"	IDENTIFICADOR, \n"+
			"	CVE_TIPO_IDENTIFICADOR, \n"+
			"	CALLE, \n"+
			"	NUMERO_INT, \n"+
			"	NUMERO_EXT, \n"+
			"	CODIGO_POSTAL, \n"+
			"	ID_REFER_POST, \n"+
			"	NOM_ASENTAMIENTO, \n"+
			"	NOM_DELEGACION, \n"+
			"	NOM_CIUDAD, \n"+
			"	NOM_ESTADO, \n"+
			"	FECHA_INICIO_RESIDENCIA, \n"+
			"	ID_TIPO_DOMICILIO, \n"+
			"       DOMICILIO_FISCAL \n" +
			"FROM RS_GRAL_DOMICILIO \n"+
			"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND IDENTIFICADOR = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			"AND CVE_TIPO_IDENTIFICADOR = 'AVAL' \n";
			
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
	
		sSql = "SELECT \n"+
			"	CVE_GPO_EMPRESA, \n"+
			"	CVE_EMPRESA, \n"+
			"	ID_DOMICILIO, \n"+
			"	IDENTIFICADOR, \n"+
			"	CVE_TIPO_IDENTIFICADOR, \n"+
			"	CALLE, \n"+
			"	NUMERO_INT, \n"+
			"	NUMERO_EXT, \n"+
			"	CODIGO_POSTAL, \n"+
			"	ID_REFER_POST, \n"+
			"	NOM_ASENTAMIENTO, \n"+
			"	NOM_DELEGACION, \n"+
			"	NOM_CIUDAD, \n"+
			"	NOM_ESTADO, \n"+
			"	FECHA_INICIO_RESIDENCIA, \n"+
			"	ID_TIPO_DOMICILIO, \n"+
			"       DOMICILIO_FISCAL \n" +
			" FROM RS_GRAL_DOMICILIO \n" +
			" WHERE ID_DOMICILIO  = '" + (String) parametros.getDefCampo("ID_DOMICILIO") + "' \n" +
			" AND IDENTIFICADOR = '" + (String)parametros.getDefCampo("IDENTIFICADOR") + "' \n" +
			" AND CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
			" AND CVE_TIPO_IDENTIFICADOR = 'AVAL' \n";
			
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
		//String sNomCompleto = null;
		String sIdDomicilio = "";
		
		//String sIdExConyuge = "";
		
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
				   "IDENTIFICADOR, \n" +
				   "CVE_TIPO_IDENTIFICADOR, \n" +
			           "FECHA_INICIO_RESIDENCIA, \n" +
			           "ID_TIPO_DOMICILIO, \n" +
			           "DOMICILIO_FISCAL) \n" +
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
				"'" + (String)registro.getDefCampo("IDENTIFICADOR") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_TIPO_IDENTIFICADOR") + "', \n" +
				" TO_DATE('" + registro.getDefCampo("FECHA_INICIO_RESIDENCIA") + "','DD/MM/YYYY'), \n" +
				"'" + (String)registro.getDefCampo("ID_TIPO_DOMICILIO") + "', \n" +
				"'" + (String)registro.getDefCampo("DOMICILIO_FISCAL") + "') \n" ;

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
			     " NOM_ESTADO ='" + (String)registro.getDefCampo("NOM_ESTADO") + "', \n" +
			     " FECHA_INICIO_RESIDENCIA =TO_DATE('" + (String)registro.getDefCampo("FECHA_INICIO_RESIDENCIA") + "','DD/MM/YYYY'), \n" +
			     " ID_TIPO_DOMICILIO ='" + (String)registro.getDefCampo("ID_TIPO_DOMICILIO") + "', \n" +
			     " DOMICILIO_FISCAL ='" + (String)registro.getDefCampo("DOMICILIO_FISCAL") + "' \n" +			     
		   	" WHERE ID_DOMICILIO = '" + (String)registro.getDefCampo("ID_DOMICILIO") + "' \n" +
		   	" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
		   	" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
		   	" AND IDENTIFICADOR = '" + (String)registro.getDefCampo("IDENTIFICADOR") + "' \n" +
		   	" AND CVE_TIPO_IDENTIFICADOR= 'AVAL' \n" ;
		   	
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
			sSql = "DELETE FROM RS_GRAL_DOMICILIO " +
			   " WHERE ID_DOMICILIO 	  ='" + (String)registro.getDefCampo("ID_DOMICILIO") + "' \n" +
			   " AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA  ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND IDENTIFICADOR = '" + (String)registro.getDefCampo("IDENTIFICADOR") + "' \n" +
		   	   " AND CVE_TIPO_IDENTIFICADOR= 'AVAL' \n" ;
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
	
		return resultadoCatalogo;
	}

}