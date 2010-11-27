/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;

import com.rapidsist.portal.catalogos.OperacionAlta; import com.rapidsist.portal.catalogos.OperacionBaja; import com.rapidsist.portal.catalogos.OperacionModificacion; import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de perfiles.
 */
public class PublPerfilDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		sSql = " SELECT * FROM RS_PUB_PERFIL \n" +
			   " WHERE CVE_GPO_EMPRESA = '"+parametros.getDefCampo("CVE_GPO_EMPRESA")+"'"+
			   " AND CVE_PORTAL = '"+parametros.getDefCampo("CVE_PORTAL")+"'"+
			   " ORDER BY CVE_PERFIL_PUB \n";
		ejecutaSql();		
		return this.getConsultaLista();
	}

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql = " SELECT * FROM RS_PUB_PERFIL \n" +
			   " WHERE CVE_PERFIL_PUB='" + (String)parametros.getDefCampo("CVE_PERFIL_PUB") + "' \n"+
			   " AND CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_PORTAL='" + (String)parametros.getDefCampo("CVE_PORTAL") + "' \n";
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql = "INSERT INTO RS_PUB_PERFIL ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_PORTAL, \n" +
					"CVE_PERFIL_PUB, \n" +
					"NOM_PERFIL, \n" +
					"DESC_PERFIL, \n" +
					"B_BLOQUEADO) \n"+
				"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_PORTAL")      + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_PERFIL_PUB")  + "', \n" +
					"'" + (String)registro.getDefCampo("NOM_PERFIL")  + "', \n" +
					"'" + (String)registro.getDefCampo("DESC_PERFIL")  + "', \n" +
					"'" + (String)registro.getDefCampo("B_BLOQUEADO")   + "') \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
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
		sSql = " UPDATE RS_PUB_PERFIL SET "+
			   " NOM_PERFIL           ='" + (String)registro.getDefCampo("NOM_PERFIL")      + "', \n" +
			   " DESC_PERFIL          ='" + (String)registro.getDefCampo("DESC_PERFIL")  + "', \n" +
			   " B_BLOQUEADO          ='" + (String)registro.getDefCampo("B_BLOQUEADO")   + "' \n" +
			   " WHERE CVE_PERFIL_PUB     ='" + (String)registro.getDefCampo("CVE_PERFIL_PUB")       + "' \n" +
			   " AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_PORTAL      ='" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
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
		//BORRA LA FUNCION
		sSql = "DELETE FROM RS_PUB_PERFIL " +
				  " WHERE CVE_PERFIL_PUB='" + (String)registro.getDefCampo("CVE_PERFIL_PUB") + "' \n" +
					" AND CVE_GPO_EMPRESA='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND CVE_PORTAL='" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}
