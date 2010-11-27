/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta; import com.rapidsist.portal.catalogos.OperacionBaja; import com.rapidsist.portal.catalogos.OperacionModificacion; import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.util.Iterator;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de aplicaciones asignadas a grupo de empresas.
 */
public class GrupoEmpresasAplicacionDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros tomando como base el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//OBTENEMOS EL FILTRO DE BUSQUEDA
		String sFiltro = (String)parametros.getDefCampo("Filtro");

		if (sFiltro.equals("AplicacionesDisponibles")){

			sSql = " SELECT \n"+
				" AE.CVE_APLICACION, \n"+
				" AE.B_BLOQUEADO, \n"+
				" A.NOM_APLICACION, \n"+
				" A.TX_DESC_APLICACION \n"+
				" FROM RS_CONF_APLICACION AE, RS_CONF_APLICACION A \n"+
				" WHERE AE.CVE_APLICACION = A.CVE_APLICACION \n"+
				" AND AE.CVE_APLICACION NOT IN \n"+
				" ( SELECT AE.CVE_APLICACION \n"+
				" FROM RS_CONF_APL_GPO_EMPRESA AE \n"+
				" WHERE AE.CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "')";

				if (parametros.getDefCampo("NOM_APLICACION") != null) {
					sSql = sSql + " AND A.NOM_APLICACION LIKE'%" + (String) parametros.getDefCampo("NOM_APLICACION") + "%' \n";
				}
				sSql = sSql + "ORDER BY AE.CVE_APLICACION";
		}else{

			sSql = "SELECT "+
			   " AE.CVE_APLICACION, "+
			   " AE.B_BLOQUEADO, "+
			   " A.NOM_APLICACION," +
			   " A.TX_DESC_APLICACION "+
			   " FROM RS_CONF_APL_GPO_EMPRESA AE, RS_CONF_APLICACION A "+
			   " WHERE AE.CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "'" +
			   " AND AE.CVE_APLICACION = A.CVE_APLICACION "+
			   "ORDER BY AE.CVE_APLICACION";
		}
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene un registro en base a la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql = "SELECT "+
			   " AE.CVE_GPO_EMPRESA, "+
			   " AE.CVE_APLICACION, "+
			   " AE.B_BLOQUEADO, "+
			   " A.NOM_APLICACION," +
			   " A.TX_DESC_APLICACION "+
			   " FROM RS_CONF_APL_GPO_EMPRESA AE, RS_CONF_APLICACION A "+
			   " WHERE AE.CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "'" +
			   " AND AE.CVE_APLICACION = '" + parametros.getDefCampo("CVE_APLICACION") + "'" ;

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
		String sCveAplicacion = "";
		LinkedList listaAplicaciones = (LinkedList)registro.getDefCampo("ListaAplicaciones");
		if (listaAplicaciones != null){
			Iterator lista = listaAplicaciones.iterator();
			while (lista.hasNext()){
				Registro registroPermiso = (Registro)lista.next();
				sSql = "INSERT INTO RS_CONF_APL_GPO_EMPRESA( " +
					"CVE_GPO_EMPRESA, " +
					"CVE_APLICACION, " +
					"B_BLOQUEADO) " +
					" VALUES ( " +
					"'" + (String)registroPermiso.getDefCampo("CVE_GPO_EMPRESA") + "'," +
					"'" + (String)registroPermiso.getDefCampo("CVE_APLICACION") + "'," +
					"'F')";
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

		String sCveFuncion = "";
		LinkedList listaAplicaciones = (LinkedList)registro.getDefCampo("ListaAplicaciones");
		if (listaAplicaciones != null){
			Iterator lista = listaAplicaciones.iterator();
			while (lista.hasNext()){
				Registro registroPermiso = (Registro)lista.next();
				sSql = "UPDATE RS_CONF_APL_GPO_EMPRESA SET \n"+
						   "B_BLOQUEADO = '" + (String)registroPermiso.getDefCampo("B_BLOQUEADO") + "' \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registroPermiso.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							" AND CVE_APLICACION = '" + (String)registroPermiso.getDefCampo("CVE_APLICACION") + "' \n";
				//VERIFICA SI NO SE ACTUALIZO EL REGISTRO
				if (ejecutaUpdate() == 0) {
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
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
		LinkedList listaAplicaciones = (LinkedList)registro.getDefCampo("ListaAplicaciones");
		if (listaAplicaciones != null){
			Iterator lista = listaAplicaciones.iterator();
			while (lista.hasNext()){
				Registro registroPermiso = (Registro)lista.next();
				sSql =  "SELECT CVE_APLICACION FROM RS_CONF_PORTAL_APL \n"+
				" WHERE CVE_GPO_EMPRESA= '" + (String)registroPermiso.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND CVE_APLICACION= '" + (String)registroPermiso.getDefCampo("CVE_APLICACION") + "' \n" ;

				ejecutaSql();
				if (rs.next()){
					resultadoCatalogo.mensaje.setClave("NO_BORRAR_REGISTRO_RELACIONADO");
				}else{
					 sSql = "DELETE FROM RS_CONF_APL_GPO_EMPRESA \n" +
						 " WHERE CVE_GPO_EMPRESA= '" + (String)registroPermiso.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						 "   AND CVE_APLICACION='" + (String)registroPermiso.getDefCampo("CVE_APLICACION") + "' \n" ;
					try{
						ejecutaUpdate();
					}
					catch(Exception e){
						//IGNORA LA EXCEPCION
					}
				}

			}
		}
		return resultadoCatalogo;
	}
}
