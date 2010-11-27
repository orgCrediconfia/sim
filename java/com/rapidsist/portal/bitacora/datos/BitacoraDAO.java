/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.bitacora.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.ByteArrayInputStream;
import java.sql.ResultSetMetaData;

/**
 * Administra los accesos a la base de datos para la bitácora.
 */
public class BitacoraDAO extends Conexion2 {

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		//REALIZA UNA CONSULTA DINÁMICA PARA OBTENER UN REGISTRO DE UNA TABLA
		// ESPECÍFICA
		sSql = "SELECT * FROM "+(String)parametros.getDefCampo("CVE_TABLA") +" \n "+
			   " WHERE  "+(String)parametros.getDefCampo("FILTRO");

		ejecutaSql();

		Registro resultado = getConsultaRegistro();
		return resultado;
	}

	/**
	 * Obtiene la lista de los campos llave de una tabla específica.
	 * @param parametros Parámetros que se le envian a la consulta para obtener el registro
	 * @return Los campos del registro.
	 * @throws SQLException Arroja una SQLException si se genera un error al accesar
	 * la base de datos.
	 */
	public LinkedList getTablaBitacora(Registro parametros) throws SQLException{
		sSql = " SELECT CVE_TABLA_BITACORA \n"+
			   " FROM RS_CONF_TABLA_BITACORA \n"+
			   " WHERE CVE_TABLA ='"+(String)parametros.getDefCampo("CVE_TABLA")+"'";
		ejecutaSql();
		return this.getConsultaLista();
	}


	/**
	 * Obtiene la lista de los campos llave de una tabla específica.
	 * @param sNomTabla Nombre de la tabla.
	 * @return Los campos del registro.
	 * @throws SQLException Arroja una SQLException si se genera un error al accesar
	 * la base de datos.
	 */
	public LinkedList getCamposLlaveTabla(String sNomTabla) throws SQLException{
		sSql = " SELECT COLUMN_NAME COLUMNA \n" +
		       " FROM USER_CONSTRAINTS CONS, \n" +
			   " USER_CONS_COLUMNS COL, USER_CONSTRAINTS X \n" +
			   " WHERE COL.TABLE_NAME = CONS.TABLE_NAME \n" +
			   " AND COL.CONSTRAINT_NAME = CONS.CONSTRAINT_NAME\n" +
			   " AND CONS.CONSTRAINT_TYPE = 'P' \n" +
			   " AND COL.TABLE_NAME = '" + sNomTabla + "' \n" +
			   " AND X.CONSTRAINT_NAME(+) = CONS.R_CONSTRAINT_NAME\n" +
			   " AND X.OWNER(+) = CONS.R_OWNER \n" ;

		ejecutaSql();

		return this.getConsultaLista();
	}


	/**
	 * Obtiene la lista de los campos de una tabla específica.
	 * @param sNomTabla Nombre de la tabla.
	 * @return Los campos del registro.
	 * @throws SQLException Arroja una SQLException si se genera un error al accesar
	 * la base de datos.
	 */
	public LinkedList getCamposTabla(String sNomTabla) throws SQLException{
		LinkedList listaColumnas = new LinkedList();
		sSql = "SELECT * FROM "+sNomTabla+" WHERE ROWNUM = 0 ";

		ejecutaSql();
		ResultSetMetaData data = rs.getMetaData();
		for(int i = 1; i<= data.getColumnCount(); i++){
			if(data.getColumnType(i) != 2004 && data.getColumnType(i) != 2005){
				if (data.isSearchable(i)){
					Registro registro = new Registro();
					registro.addDefCampo("COLUMNA", data.getColumnName(i));
					listaColumnas.add(registro);
				}
			}
		}
		return listaColumnas;
	}


	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		sSql = "SELECT * FROM RS_CONF_APLICACION " ;
		ejecutaSql();
		return this.getConsultaLista();
	}
	
	/**
	 * Inserta un registro a la tabla RS_BITACORA_FUNCION
	 * @param sCveGpoEmpresa Clave del grupo de empresas.
	 * @param sCvePortal Clave del portal.
	 * @param sCveFuncion Clave de la función solicitada al sistema.
	 * @param sNomFuncion Nombre de la función solicitada al sistema.
	 * @param sCveUsuario Clave del usuario .
	 * @param sNomUsuario Nombre del usuario.
	 * @param sUrlServidor Url del servidor.
	 * @param sParametros  Objeto.
	 * @param sDireccionIpUsuario Dirección ip del usuario. 
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo registraVisitaFuncion(String sCveGpoEmpresa, String sCvePortal, String sCveFuncion, String sNomFuncion, String sCveUsuario, String sNomUsuario, String sUrlServidor, String sParametros, String sDireccionIpUsuario) throws SQLException{
		ResultadoCatalogo resultadoCatalogo= new ResultadoCatalogo();
		sSql = "INSERT INTO RS_BITACORA_FUNCION( \n"+
						"CVE_GPO_EMPRESA, \n"+
						"CVE_PORTAL , \n"+
						"CVE_USUARIO, \n"+
						"NOM_PERSONA, \n"+
						"CVE_FUNCION, \n"+
						"NOM_FUNCION, \n"+
						"URL_SERVIDOR, \n"+
						"PARAMETROS, \n" +
						"IP_USUARIO, \n" +
						"F_REGISTRO) \n"+
					"VALUES ( \n"+
						"'" + sCveGpoEmpresa + "',\n" +
						"'" + sCvePortal  + "',\n" +
						"'" + sCveUsuario + "',\n" +
						"'" + sNomUsuario + "',\n" +
						"'" + sCveFuncion + "',\n" +
						"'" + sNomFuncion + "',\n" +
						"'" + sUrlServidor + "',\n" +
						"'" + sParametros + "',\n" +
						"'" + sDireccionIpUsuario + "',\n" +
						"SYSDATE )\n" ;
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
	
	/**
	 * Inserta un registro a la tabla RS_BITACORA.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo= new ResultadoCatalogo();
		sSql = "INSERT INTO RS_BITACORA( \n"+
					   "CVE_BITACORA, \n"+
					   "XML_MOV_REG) \n"+
				"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_BITACORA") + "',\n" +
					"'" + (String)registro.getDefCampo("XML_MOV_REG") + "')\n" ;
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
	/**
	 * Recupera el valor proximo de un secuence
	 * @return String que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public String indice() throws SQLException{
		//ResultadoCatalogo resultadoCatalogo= new ResultadoCatalogo();
		String sIndice = "";
		sSql = "SELECT SQ01_RS_BITACORA_INDICE.nextval as indice FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIndice = rs.getString("indice");
	   }
		return sIndice;
	}
	/**
	 * Inserta un registro en la tabla RS_BITACORA_INDICE.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
		public ResultadoCatalogo insertaIndice(Registro registro) throws SQLException{
			ResultadoCatalogo resultadoCatalogo= new ResultadoCatalogo();
				sSql = "INSERT INTO RS_BITACORA_INDICE( \n"+
						   "CVE_GPO_EMPRESA, \n"+
						   "ID_BITACORA_INDICE, \n"+
						   "CVE_BITACORA, \n"+
						   "CVE_USUARIO, \n"+
						   "F_ALTA) \n"+
					"VALUES ( \n"+
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "',\n" +
						"" + (String)registro.getDefCampo("INDICE") + ",\n" +
						"'" + (String)registro.getDefCampo("CVE_BITACORA") + "',\n" +
						"'" + (String)registro.getDefCampo("CVE_USUARIO") + "',\n"  +
						"SYSDATE)\n";

			//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}

			return resultadoCatalogo;
		}


	/**
	 * Modifica un registro en la tabla RS_BITACORA.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql = "UPDATE RS_BITACORA SET \n"+
					   "XML_MOV_REG = ? " +
				"WHERE CVE_BITACORA = '" + registro.getDefCampo("CVE_BITACORA") + "'";
		//DA DE ALTA EL DOCUMENTO EN LA BASE DE DATOS
		PreparedStatement ps = this.conn.prepareStatement(sSql);
		String sCadena = (String)registro.getDefCampo("XML_MOV_REG");
		ByteArrayInputStream byteArray = new ByteArrayInputStream((sCadena).getBytes());
		ps.setAsciiStream(1, byteArray, sCadena.length());
		//EJECUTA EL PreparedStatement
		int iNumRegistros = ps.executeUpdate();
		//VERIFICA SI NO SE MODIFICO EL REGISTRO
		if (iNumRegistros == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		ps.close();
		return resultadoCatalogo;
	}

	/**
	 * Borra un registro de la tabla RS_CONF_APLICACION_FUNCION.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();

		//VERIFICA SI LA FUNCION EXISTE EN LA TABLA RS_CONF_MENU
		sSql =  "SELECT CVE_APLICACION FROM RS_CONF_APLICACION_FUNCION \n"+
				"WHERE CVE_APLICACION= '" + (String)registro.getDefCampo("CVE_APLICACION") + "' \n";
		ejecutaSql();
		if (rs.next()){
			resultadoCatalogo.mensaje.setClave("NO_BORRAR_REGISTRO_RELACIONADO");
		}
		else{
			//BORRA LA FUNCION
			sSql = "DELETE FROM RS_CONF_APLICACION " +
					  " WHERE CVE_APLICACION='" + (String)registro.getDefCampo("CVE_APLICACION") + "' \n";

			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		}
		return resultadoCatalogo;
	}

	/**
	* Obtiene la descripción de un campo de tabla USER_COL_COMMENTS.
	* @param sTabla Nombre de la tabla.
	* @param sCampo Nombre del campo.
	* @return Descripción del campo consultado.
	*/
	public String getEtiquetaColumna (String sTabla, String sCampo) {
		String sDescripcionCampos = "";
		try{
			// OBTIENE LA DESCRIPCION DEL CAMPO.
			sSql = " SELECT COLUMN_NAME,  \n"+
			"        DECODE(COMMENTS, null, 'Sin comentario',  COMMENTS) DESCRIPCION_CAMPO \n"+
				" FROM USER_COL_COMMENTS  \n"+
				" WHERE TABLE_NAME ='"+sTabla+"'  \n"+
				"        AND COLUMN_NAME = '"+sCampo+"' \n";
			ejecutaSql();
			if (rs.next()){
				sDescripcionCampos = rs.getString("DESCRIPCION_CAMPO");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
		}
		return sDescripcionCampos;
	}

}
