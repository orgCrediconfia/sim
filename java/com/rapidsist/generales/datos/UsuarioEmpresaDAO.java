/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.generales.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta; import com.rapidsist.portal.catalogos.OperacionBaja; import com.rapidsist.portal.catalogos.OperacionModificacion; import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de usuarios.
 */
public class UsuarioEmpresaDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//OBTENEMOS EL FILTRO DE BUSQUEDA
		String sFiltro = (String)parametros.getDefCampo("Filtro");
		if (sFiltro.equals("UsuarioAplicacion")){
			sSql = "SELECT U.CVE_GPO_EMPRESA, U.CVE_USUARIO, U.ID_PERSONA, U.LOCALIDAD, U.PASSWORD, U.NOM_ALIAS, U.CVE_EMPRESA, P.NOM_COMPLETO  \n"+
				" FROM RS_GRAL_USUARIO U, RS_GRAL_PERSONA P, RS_GRAL_USUARIO_PERFIL UP  \n"+
				" WHERE U.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND U.ID_PERSONA = P.ID_PERSONA \n"+
				" AND U.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				" AND U.CVE_GPO_EMPRESA = UP.CVE_GPO_EMPRESA \n"+
				" AND U.CVE_USUARIO = UP.CVE_USUARIO \n"+
				" AND UP.CVE_APLICACION = '" + (String)parametros.getDefCampo("CVE_APLICACION") + "' \n";

			if (parametros.getDefCampo("NOM_COMPLETO") != null){
				sSql = sSql + "       AND UPPER(P.NOM_COMPLETO) LIKE '%" + ( (String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
			}
			if (parametros.getDefCampo("CVE_USUARIO_BUSQUEDA") != null){
				sSql = sSql + "       AND UPPER(U.CVE_USUARIO) LIKE '%" + ( (String) parametros.getDefCampo("CVE_USUARIO_BUSQUEDA")).toUpperCase() + "%' \n";
			}
			sSql = sSql + " ORDER BY U.CVE_USUARIO \n" ;

		}else{
			sSql = "SELECT U.CVE_GPO_EMPRESA, U.CVE_USUARIO, U.ID_PERSONA, U.LOCALIDAD, U.PASSWORD, U.NOM_ALIAS, U.CVE_EMPRESA, P.NOM_COMPLETO \n"+
				"  FROM RS_GRAL_USUARIO U, RS_GRAL_PERSONA P \n"+
				" WHERE U.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				  " AND U.ID_PERSONA = P.ID_PERSONA \n" +
				  " AND U.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n";
			  if (parametros.getDefCampo("NOM_COMPLETO") != null){
				  sSql = sSql + "       AND UPPER(P.NOM_COMPLETO) LIKE '%" + ( (String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
			  }
			  if (parametros.getDefCampo("CVE_USUARIO_BUSQUEDA") != null){
				  sSql = sSql + "       AND UPPER(U.CVE_USUARIO) LIKE '%" + ( (String) parametros.getDefCampo("CVE_USUARIO_BUSQUEDA")).toUpperCase() + "%' \n";
			  }
			  sSql = sSql + " ORDER BY U.CVE_USUARIO \n" ;

		}
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql = "SELECT U.CVE_GPO_EMPRESA, U.CVE_USUARIO, U.ID_PERSONA, U.LOCALIDAD, U.PASSWORD, U.NOM_ALIAS, U.CVE_EMPRESA, P.NOM_COMPLETO \n"+
				"  FROM RS_GRAL_USUARIO U, RS_GRAL_PERSONA P \n"+
				" WHERE U.ID_PERSONA = P.ID_PERSONA \n" +
				"       AND U.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"       AND U.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";

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

		sSql = "INSERT INTO RS_GRAL_USUARIO (" +
						 " CVE_GPO_EMPRESA, " +
						 " CVE_USUARIO, " +
						 " ID_PERSONA, " +
						 " LOCALIDAD, " +
						 " PASSWORD, " +
						 " F_PROX_CAMB_PASS, " +
						 " NUM_LOG_INVALIDO, " +
						 " CVE_USUARIO_AUTENTIFICACION, " +
						 " CVE_ROL_APLICACION_WEB, " +
						 " NOM_ALIAS, " +
						 " CVE_EMPRESA) " +
					" VALUES (" +
						 "'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', " +
						 "'" + (String)registro.getDefCampo("CVE_USUARIO") + "', " +
						 (String)registro.getDefCampo("ID_PERSONA") + ", " +
						 "'" + (String)registro.getDefCampo("LOCALIDAD") + "', "+
						 "'" + (String)registro.getDefCampo("PASSWORD") + "', "+
						 " sysdate, " +
						 " 0,"+
						 "'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + (String)registro.getDefCampo("CVE_USUARIO") + "', " +
						 "'AGSA', "+
						 "'" + (String)registro.getDefCampo("NOM_ALIAS") + "', "+
						 "'" + (String)registro.getDefCampo("CVE_EMPRESA") + "') ";
			//VERIFICA SI NO SE MODIFICO EL REGISTRO
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
		sSql = " UPDATE RS_GRAL_USUARIO SET "+
					" ID_PERSONA = " + (String)registro.getDefCampo("ID_PERSONA") + ", " +
					" LOCALIDAD = '" + (String)registro.getDefCampo("LOCALIDAD")+"' , ";
					
					//SI EL PASSWORD ESTA VACIO CONSERVAR EL ACTUAL EN BASE DE DATOS
					if (!registro.getDefCampo("PASSWORD").equals("")){
					sSql =sSql+"PASSWORD = '" + (String)registro.getDefCampo("PASSWORD")+"' ,";
					}

		sSql= sSql+" NOM_ALIAS = '" + (String)registro.getDefCampo("NOM_ALIAS") + "' " +
				"  WHERE CVE_USUARIO = '" + (String)registro.getDefCampo("CVE_USUARIO") + "'" +
				"  AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "'" ;
		//VERIFICA SI NO SE MODIFICO EL REGISTRO
		
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

		//VERIFICA SI LA FUNCION EXISTE EN LA TABLA RS_CONF_MENU
		sSql =  "SELECT * FROM RS_GRAL_USUARIO_PERFIL \n"+
				"WHERE CVE_USUARIO= '" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
				" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		ejecutaSql();
		if (rs.next()){
			resultadoCatalogo.mensaje.setClave("NO_BORRAR_REGISTRO_RELACIONADO");
		}
		else{
					sSql = "DELETE FROM RS_GRAL_USUARIO \n" +
						" WHERE CVE_USUARIO = '" + (String) registro.getDefCampo("CVE_USUARIO") + "' \n" +
						" AND CVE_GPO_EMPRESA = '" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";

					//VERIFICA SI NO SE ACTUALIZO EL REGISTRO
					if (ejecutaUpdate() == 0) {
						resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
					}
				}
		return resultadoCatalogo;
	}
}