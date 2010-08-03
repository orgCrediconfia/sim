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
 * Administra los accesos a la base de datos para los negocios del Cliente.
 */
 
public class SimClienteNegocioDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT \n"+
			"	CN.CVE_GPO_EMPRESA, \n"+
			"	CN.CVE_EMPRESA, \n"+
			"	CN.ID_PERSONA, \n"+
			"	CN.ID_NEGOCIO, \n"+
			"	CN.NOM_NEGOCIO, \n"+
			"	CN.ID_TIPO_NEGOCIO, \n"+
			"	TN.NOM_TIPO_NEGOCIO, \n"+
			"	CN.RFC, \n"+
			"	CN.CVE_CLASE, \n"+
			"	DECODE(CN.B_PRINCIPAL,'V','Principal','F','Secundario') B_PRINCIPAL, \n"+
			"	CN.FECHA_INICIO_OPERACION, \n"+
			"	D.ID_DOMICILIO, \n"+
			"	D.CALLE, \n"+
			"	D.NUMERO_INT, \n"+
			"	D.NUMERO_EXT, \n"+
			"	D.CODIGO_POSTAL, \n"+
			"	D.ID_REFER_POST, \n"+
			"	D.NOM_ASENTAMIENTO, \n"+
			"	D.NOM_DELEGACION, \n"+
			"	D.NOM_CIUDAD, \n"+
			"	D.NOM_ESTADO \n"+
			"FROM SIM_CLIENTE_NEGOCIO CN, \n"+
			"     SIM_CAT_TIPO_NEGOCIO TN, \n"+
			"     RS_GRAL_DOMICILIO D \n"+
			"WHERE CN.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CN.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND CN.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			"AND TN.CVE_GPO_EMPRESA = CN.CVE_GPO_EMPRESA \n"+
			"AND TN.CVE_EMPRESA = CN.CVE_EMPRESA \n"+
			"AND TN.ID_TIPO_NEGOCIO = CN.ID_TIPO_NEGOCIO \n"+
			"AND D.CVE_GPO_EMPRESA (+)= CN.CVE_GPO_EMPRESA \n"+
			"AND D.CVE_EMPRESA (+)= CN.CVE_EMPRESA \n"+
			"AND D.IDENTIFICADOR (+)= CN.ID_NEGOCIO \n"+
			"AND D.CVE_TIPO_IDENTIFICADOR (+)= 'NEGOCIO' \n";
			
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
	
		sSql =  "SELECT \n"+
			"	CN.CVE_GPO_EMPRESA, \n"+
			"	CN.CVE_EMPRESA, \n"+
			"	CN.ID_PERSONA, \n"+
			"	CN.ID_NEGOCIO, \n"+
			"	CN.NOM_NEGOCIO, \n"+
			"	CN.ID_TIPO_NEGOCIO, \n"+
			"	TN.NOM_TIPO_NEGOCIO, \n"+
			"	CN.RFC, \n"+
			"	CN.CVE_CLASE, \n"+
			"	C.NOM_CLASE, \n"+
			"	CN.B_PRINCIPAL, \n"+
			"	CN.FECHA_INICIO_OPERACION, \n"+
			"	D.ID_DOMICILIO, \n"+
			"	D.CALLE, \n"+
			"	D.NUMERO_INT, \n"+
			"	D.NUMERO_EXT, \n"+
			"	D.CODIGO_POSTAL, \n"+
			"	D.ID_REFER_POST, \n"+
			"	D.NOM_ASENTAMIENTO, \n"+
			"	D.NOM_DELEGACION, \n"+
			"	D.NOM_CIUDAD, \n"+
			"	D.NOM_ESTADO \n"+
			"FROM SIM_CLIENTE_NEGOCIO CN, \n"+
			"     SIM_CAT_TIPO_NEGOCIO TN, \n"+
			"     RS_GRAL_DOMICILIO D, \n"+
			"     SIM_CAT_CLASE C \n"+
			"WHERE CN.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CN.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND CN.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			"AND CN.ID_NEGOCIO = '" + (String)parametros.getDefCampo("ID_NEGOCIO") + "' \n"+
			"AND TN.CVE_GPO_EMPRESA = CN.CVE_GPO_EMPRESA \n"+
			"AND TN.CVE_EMPRESA = CN.CVE_EMPRESA \n"+
			"AND TN.ID_TIPO_NEGOCIO = CN.ID_TIPO_NEGOCIO \n"+
			"AND D.CVE_GPO_EMPRESA (+)= CN.CVE_GPO_EMPRESA \n"+
			"AND D.CVE_EMPRESA (+)= CN.CVE_EMPRESA \n"+
			"AND D.IDENTIFICADOR (+)= CN.ID_NEGOCIO \n"+
			"AND D.CVE_TIPO_IDENTIFICADOR (+)= 'NEGOCIO' \n"+
			"AND C.CVE_GPO_EMPRESA (+)= CN.CVE_GPO_EMPRESA \n"+
			"AND C.CVE_EMPRESA (+)= CN.CVE_EMPRESA \n"+
			"AND C.CVE_CLASE (+)= CN.CVE_CLASE \n";
		
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
		
		String sIdNegocio = "";
		
		//OBTENEMOS EL SEQUENCE
		sSql = "SELECT SQ01_SIM_CLIENTE_NEGOCIO.nextval as ID_NEGOCIO FROM DUAL";
			ejecutaSql();
			if (rs.next()){
				sIdNegocio = rs.getString("ID_NEGOCIO");
			}

			//INSERTA EL REGISTRO DE DOMICILIO PARA PODER OBTENER EL ID_DOMICILIO DEL SEQUENCE
			sSql = "INSERT INTO SIM_CLIENTE_NEGOCIO ( "+
				   "CVE_GPO_EMPRESA, \n" +
				   "CVE_EMPRESA, \n" +
				   "ID_PERSONA, \n" +
				   "ID_NEGOCIO, \n" +
				   "NOM_NEGOCIO, \n" +
				   "ID_TIPO_NEGOCIO, \n" +
				   "RFC, \n" +
				   "CVE_CLASE, \n" +
				   "B_PRINCIPAL, \n" +
				   "FECHA_INICIO_OPERACION) \n" +
				" VALUES (" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PERSONA") + "', \n" +
				" " + sIdNegocio +", \n" +
				"'" + (String)registro.getDefCampo("NOM_NEGOCIO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TIPO_NEGOCIO") + "', \n" +
				"'" + (String)registro.getDefCampo("RFC") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_CLASE") + "', \n" +
				"'" + (String)registro.getDefCampo("B_PRINCIPAL") + "', \n" +
				"TO_DATE('" + (String)registro.getDefCampo("FECHA_INICIO_OPERACION") + "','DD/MM/YYYY')) \n" ;

		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_NEGOCIO", sIdNegocio);
		
		String sIdDomicilio = "";
		
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
				   "CVE_TIPO_IDENTIFICADOR) \n" +
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
				" " + sIdNegocio +", \n" +
				"'" + (String)registro.getDefCampo("CVE_TIPO_IDENTIFICADOR") + "') \n" ;

		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_DOMICILIO", sIdDomicilio);
	
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
		
		//ACTUALIZA LOS DATOS DEL NEGOCIO
		sSql = " UPDATE SIM_CLIENTE_NEGOCIO SET "+
			     " NOM_NEGOCIO ='" + (String)registro.getDefCampo("NOM_NEGOCIO") + "', \n" +
			     " ID_TIPO_NEGOCIO='" + (String)registro.getDefCampo("ID_TIPO_NEGOCIO") + "', \n" +
			     " RFC ='" + (String)registro.getDefCampo("RFC") + "', \n" +
			     " CVE_CLASE ='" + (String)registro.getDefCampo("CVE_CLASE") + "', \n" +
			     " FECHA_INICIO_OPERACION = TO_DATE('" + (String)registro.getDefCampo("FECHA_INICIO_OPERACION") + "','DD/MM/YYYY'), \n" +
			     " B_PRINCIPAL ='" + (String)registro.getDefCampo("B_PRINCIPAL") + "' \n" +
			     
		   	" WHERE ID_NEGOCIO = '" + (String)registro.getDefCampo("ID_NEGOCIO") + "' \n" +
		   	" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
		   	" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
		   	" AND ID_PERSONA = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" ;
		   	
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
		   	" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
		   	" AND IDENTIFICADOR = '" + (String)registro.getDefCampo("IDENTIFICADOR") + "' \n" +
		   	" AND CVE_TIPO_IDENTIFICADOR= '" + (String)registro.getDefCampo("CVE_TIPO_IDENTIFICADOR") + "' \n" ;
		   	
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
			sSql =  " DELETE FROM SIM_CLIENTE_NEGOCIO " +
				" WHERE ID_NEGOCIO = '" + (String)registro.getDefCampo("ID_NEGOCIO") + "' \n" +
		   		" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
		   		" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
		   		" AND ID_PERSONA = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" ;
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
	
		return resultadoCatalogo;
	}

}