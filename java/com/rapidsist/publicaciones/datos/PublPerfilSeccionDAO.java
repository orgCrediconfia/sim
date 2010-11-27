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
import java.util.Iterator;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de perfiles por sección.
 */
public class PublPerfilSeccionDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		String sFiltro = (String)parametros.getDefCampo("Filtro");
		boolean bNomSeccion = false;

		if (sFiltro == null){

			sSql = "SELECT \n" +
				" PS.CVE_PORTAL, \n" +
				" PS.CVE_GPO_EMPRESA, \n" +
				" S.CVE_SECCION, \n" +
				" S.NOM_SECCION \n" +
				" FROM RS_PUB_PERFIL_SECCION PS, RS_PUB_SECCION S \n" +
				" WHERE ";

			if (parametros.getDefCampo("CVE_GPO_RESPONSABLE") != null) {
				  sSql = sSql + " S.CVE_GPO_RESPONSABLE ='"+ (String) parametros.getDefCampo("CVE_GPO_RESPONSABLE") +"' \n";
				  bNomSeccion = true;
			}

			if (parametros.getDefCampo("NOM_SECCION") != null) {
				  sSql = sSql + " S.NOM_SECCION ='"+ (String) parametros.getDefCampo("NOM_SECCION") +"' \n";
				  bNomSeccion = true;
			}

			if (parametros.getDefCampo("B_BLOQUEADO") != null) {
				  sSql = sSql + " S.B_BLOQUEADO ='"+ (String) parametros.getDefCampo("B_BLOQUEADO") +"' \n";
				  bNomSeccion = true;
			}

				sSql = sSql + (bNomSeccion ? " AND" : "") + " PS.CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "'" +
				" AND PS.CVE_PORTAL = '" + parametros.getDefCampo("CVE_PORTAL") + "'" +
				" AND PS.CVE_PORTAL = S.CVE_PORTAL \n" +
				" AND PS.CVE_SECCION = S.CVE_SECCION \n" +
				" AND PS.CVE_PERFIL_PUB = '" + parametros.getDefCampo("CVE_PERFIL_PUB") + "'" +
				" ORDER BY CVE_SECCION \n";

		}else{

			sSql = "SELECT \n"+
				   " S.CVE_PORTAL, \n"+
				   " S.CVE_GPO_EMPRESA, \n"+
				   " S.CVE_SECCION, \n"+
				   " S.NOM_SECCION \n"+
				   " FROM RS_PUB_SECCION S \n"+
				   " WHERE ";

			if (parametros.getDefCampo("NOM_SECCION") != null) {
				  sSql = sSql + " S.NOM_SECCION  LIKE'%"+ (String) parametros.getDefCampo("NOM_SECCION") +"%' \n";
				  bNomSeccion = true;
			}

			sSql = sSql + (bNomSeccion ? " AND" : "") +" S.CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "'" +
				   " AND S.CVE_PORTAL = '" + parametros.getDefCampo("CVE_PORTAL") + "'" +
				   " AND S.CVE_SECCION NOT IN \n"+
				   " ( SELECT \n"+
					 " PS.CVE_SECCION \n"+
					 " FROM RS_PUB_PERFIL_SECCION PS, RS_PUB_SECCION S \n"+
					 " WHERE PS.CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "'" +
					 " AND PS.CVE_PORTAL = '" + parametros.getDefCampo("CVE_PORTAL") + "'" +
					 " AND PS.CVE_PORTAL = S.CVE_PORTAL \n"+
					 " AND PS.CVE_SECCION = S.CVE_SECCION \n"+
					 " AND PS.CVE_PERFIL_PUB = '" + parametros.getDefCampo("CVE_PERFIL_PUB") + "')" +
					 " ORDER BY CVE_SECCION \n";
		}
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
		String sCveSeccion = "";
		LinkedList listaSecciones = (LinkedList)registro.getDefCampo("ListaPerfilSeccion");
		if (listaSecciones != null){
			Iterator lista = listaSecciones.iterator();
			while (lista.hasNext()){
				sCveSeccion = (String)lista.next();
				sSql = "INSERT INTO RS_PUB_PERFIL_SECCION( " +
					"CVE_GPO_EMPRESA, " +
					"CVE_PORTAL, " +
					"CVE_SECCION, " +
					"CVE_PERFIL_PUB) " +
					" VALUES ( " +
					"'" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "'," +
					"'" + (String) registro.getDefCampo("CVE_PORTAL") + "'," +
					"'" + sCveSeccion + "'," +
					"'" + (String) registro.getDefCampo("CVE_PERFIL_PUB") + "')";
				//VERIFICA SI NO SE ACTUALIZO EL REGISTRO
				if (ejecutaUpdate() == 0) {
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
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
		sSql = "";
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
		String sCveSeccion = "";
		LinkedList listaProductos = (LinkedList)registro.getDefCampo("ListaPerfilSeccion");
		if (listaProductos != null){
			Iterator lista = listaProductos.iterator();
			while (lista.hasNext()){
				sCveSeccion = (String)lista.next();
				sSql = "DELETE FROM RS_PUB_PERFIL_SECCION \n" +
						 " WHERE CVE_GPO_EMPRESA = '" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						 " AND CVE_PORTAL = '" + (String) registro.getDefCampo("CVE_PORTAL") + "' \n" +
						 " AND CVE_PERFIL_PUB = '" + (String) registro.getDefCampo("CVE_PERFIL_PUB") + "' \n" +
						 " AND CVE_SECCION='" + sCveSeccion + "' \n";
				try{
					ejecutaUpdate();
				}
				catch(Exception e){
					//IGNORA LA EXCEPCION
				}
			}
		}
		return resultadoCatalogo;
	}
}
