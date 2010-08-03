/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;
import com.rapidsist.portal.catalogos.OperacionAlta; import com.rapidsist.portal.catalogos.OperacionBaja; import com.rapidsist.portal.catalogos.OperacionModificacion; import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import java.sql.SQLException;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Administra los accesos a la base de datos para el cat�logo de grupos responsable por secci�n
 */
public class PublUsuarioPublicacionDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		String sFiltro = (String)parametros.getDefCampo("Filtro");
		boolean bNomUsuario = false;

		if (sFiltro == null){

			sSql = " SELECT  \n" +
				" GS.CVE_GPO_EMPRESA, \n" +
				" GS.CVE_PORTAL, \n" +
				" GS.CVE_USUARIO, \n" +

				" PU.CVE_PERFIL_PUB, \n" +
				" PU.ID_NIVEL_ACCESO, \n" +

				" CP.ID_PERSONA, \n" +
				" CP.NOM_COMPLETO \n" +

				" FROM RS_PUB_GPO_RESP_SECCION GS, RS_CONF_USUARIO_PORTAL PU, RS_GRAL_USUARIO CU, RS_GRAL_PERSONA CP \n" +
				" WHERE GS.CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND GS.CVE_PORTAL = '" + parametros.getDefCampo("CVE_PORTAL") + "' \n" +
				" AND GS.CVE_GPO_RESP = '" + parametros.getDefCampo("CVE_GPO_RESP") + "' \n" +
				" AND GS.CVE_GPO_EMPRESA = PU.CVE_GPO_EMPRESA \n" +
				" AND GS.CVE_GPO_EMPRESA = CP.CVE_GPO_EMPRESA \n" +
				" AND GS.CVE_USUARIO = PU.CVE_USUARIO \n" +
				" AND PU.CVE_USUARIO = CU.CVE_USUARIO \n" +
				" AND CU.ID_PERSONA = CP.ID_PERSONA \n" +
				"AND CU.CVE_GPO_EMPRESA = CP.CVE_GPO_EMPRESA \n" +
				"AND PU.CVE_GPO_EMPRESA = CP.CVE_GPO_EMPRESA \n" +
				"AND GS.CVE_PORTAL = PU.CVE_PORTAL \n" +
				" ORDER BY CVE_GPO_RESP \n";
		}
		else{
			sSql = " SELECT  \n" +
						" GU.CVE_USUARIO, \n" +
						" GP.NOM_COMPLETO \n" +
						" FROM RS_GRAL_PERSONA GP, RS_GRAL_USUARIO GU \n" +
						" WHERE  ";

			 if (parametros.getDefCampo("NOM_COMPLETO") != null) {
				   sSql = sSql + " GP.NOM_COMPLETO LIKE'%" + (String) parametros.getDefCampo("NOM_COMPLETO") + "%' \n";
				   bNomUsuario = true;
			   }

				   sSql = sSql + (bNomUsuario ? " AND" : "") + " GU.CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   " AND GU.CVE_GPO_EMPRESA = GP.CVE_GPO_EMPRESA \n" +
				   " AND GU.ID_PERSONA = GP.ID_PERSONA \n" +
				   " AND GU.CVE_USUARIO NOT IN \n" +
				   " (SELECT \n" +
				   " PU.CVE_USUARIO \n" +
				   " FROM RS_CONF_USUARIO_PORTAL PU \n" +
				   " WHERE PU.CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   " AND PU.CVE_PORTAL = '" + parametros.getDefCampo("CVE_PORTAL") + "' ) \n" ;
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
		sSql = " SELECT * FROM RS_PUB_GPO_RESP_SECCION \n" +
			   " WHERE CVE_GPO_EMPRESA = '"+parametros.getDefCampo("CVE_GPO_EMPRESA")+"'"+
			   " AND CVE_PORTAL = '"+parametros.getDefCampo("CVE_PORTAL")+"'"+
			   " AND CVE_GPO_RESP = '"+parametros.getDefCampo("CVE_GPO_RESP")+"'"+
			   " ORDER BY CVE_GPO_RESP \n";
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
		String sCveUsuario = "";
		LinkedList listaUsuarios = (LinkedList)registro.getDefCampo("ListaUsuariosGrupo");
		if (listaUsuarios != null){
			Iterator lista = listaUsuarios.iterator();
			while (lista.hasNext()){
				sCveUsuario = (String)lista.next();
				sSql = "INSERT INTO RS_CONF_USUARIO_PORTAL( " +	
					"CVE_GPO_EMPRESA, " +
					"CVE_USUARIO, " +
					"CVE_PORTAL, " +
					"ID_NIVEL_ACCESO, " +
					"CVE_PERFIL_PUB, " +
					"CVE_ESTILO )"+
					" VALUES ( " +
					"'" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "'," +
					"'" + sCveUsuario + "'," +
					"'" + (String) registro.getDefCampo("CVE_PORTAL") + "'," +
					"'0'," +
					"''," +
					"'Default') ";
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
		sSql ="";
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
		String sCveUsuario = "";
		LinkedList listaProductos = (LinkedList)registro.getDefCampo("ListaUsuariosGrupo");
		if (listaProductos != null){
			Iterator lista = listaProductos.iterator();
			while (lista.hasNext()){
				sCveUsuario = (String)lista.next();
				sSql = "DELETE FROM RS_PUB_GPO_RESP_SECCION \n" +
						 " WHERE CVE_GPO_EMPRESA = '" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						 " AND CVE_PORTAL = '" + (String) registro.getDefCampo("CVE_PORTAL") + "' \n" +
						 " AND CVE_GPO_RESP = '" + (String) registro.getDefCampo("CVE_GPO_RESP") + "' \n" +
						 " AND CVE_USUARIO='" +  sCveUsuario + "' \n";
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
