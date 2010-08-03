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
import java.util.Iterator;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de usuario - perfiles.
 */
public class UsuarioEmpresaPerfilDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		if (parametros.getDefCampo("Filtro").equals("PerfilesDisponibles")){
			//OBTIENE LAS APLICACIONES ASIGNADAS A LA EMPRESA
			sSql =  " SELECT PE.CVE_APLICACION," +
					"        PE.CVE_PERFIL \n" +
					"  FROM RS_CONF_APL_GPO_EMPRESA AG, \n" +
					"      RS_CONF_PERFIL PE \n" +
					"  WHERE AG.CVE_GPO_EMPRESA= '" + (String) parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					"    AND AG.CVE_APLICACION = PE.CVE_APLICACION \n"+
					" MINUS \n" +
					" SELECT CVE_APLICACION,\n" +
					"        CVE_PERFIL \n" +
					"  FROM RS_GRAL_USUARIO_PERFIL \n" +
					"  WHERE CVE_GPO_EMPRESA='" + (String) parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					"    AND CVE_USUARIO='" + (String) parametros.getDefCampo("CVE_USUARIO") + "' \n" +
					"  ORDER BY CVE_APLICACION, CVE_PERFIL \n";
		}
		else{
			sSql = "SELECT \n" +
				" UP.CVE_GPO_EMPRESA, \n" +
				" UP.CVE_USUARIO, \n" +
				" UP.CVE_PERFIL, \n" +
				" UP.CVE_APLICACION, \n" +
				" UP.B_BLOQUEADO, \n" +
				" A.NOM_APLICACION, \n" +
				" A.TX_DESC_APLICACION \n" +
				" FROM RS_GRAL_USUARIO_PERFIL UP, RS_CONF_APLICACION A \n" +
				" WHERE UP.CVE_GPO_EMPRESA= '" + (String) parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND UP.CVE_USUARIO = '" + (String) parametros.getDefCampo("CVE_USUARIO") + "' \n" +
				" AND UP.CVE_APLICACION = A.CVE_APLICACION \n";
		}
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
	   sSql= "SELECT \n"+
		   " UP.CVE_GPO_EMPRESA, \n"+
		   " UP.CVE_USUARIO, \n"+
		   " UP.CVE_PERFIL, \n"+
		   " UP.CVE_APLICACION, \n"+
		   " UP.B_BLOQUEADO, \n"+
		   " A.NOM_APLICACION, \n"+
		   " A.TX_DESC_APLICACION, \n"+
		   " P.NOM_PERFIL \n"+
		   " FROM RS_GRAL_USUARIO_PERFIL UP, RS_CONF_APLICACION A, RS_CONF_PERFIL P \n"+
		   " WHERE UP.CVE_GPO_EMPRESA= '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
		   " AND UP.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n" +
		   " AND UP.CVE_PERFIL = '" + (String)parametros.getDefCampo("CVE_PERFIL") + "' \n" +
		   " AND UP.CVE_APLICACION ='" + (String)parametros.getDefCampo("CVE_APLICACION") + "'"+
		   " AND UP.CVE_APLICACION = A.CVE_APLICACION \n" +
		   " AND UP.CVE_APLICACION = P.CVE_APLICACION \n"+
		   " AND UP.CVE_PERFIL = P.CVE_PERFIL \n";
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
		ResultadoCatalogo resultadoCatalogo= new ResultadoCatalogo();

		LinkedList listaAplicaciones = (LinkedList)registro.getDefCampo("ListaAplicaciones");
		if (listaAplicaciones != null){
			Iterator lista = listaAplicaciones.iterator();
			Registro aplicacionPerfil;
			while (lista.hasNext()){
				aplicacionPerfil = (Registro)lista.next();
				sSql = "INSERT INTO RS_GRAL_USUARIO_PERFIL( \n" +
						 "CVE_GPO_EMPRESA, \n" +
						 "CVE_USUARIO, \n" +
						 "CVE_PERFIL, \n" +
						 "CVE_APLICACION, \n" +
						 "B_BLOQUEADO) \n" +
					   "VALUES ( \n" +
						 "'" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "',\n" +
						 "'" + (String) registro.getDefCampo("CVE_USUARIO") + "',\n" +
						 "'" + (String) aplicacionPerfil.getDefCampo("CVE_PERFIL") + "',\n" +
						 "'" + (String) aplicacionPerfil.getDefCampo("CVE_APLICACION") + "',\n" +
						 "'F')\n";
				ejecutaUpdate();
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
		LinkedList listaAplicaciones = (LinkedList)registro.getDefCampo("ListaAplicaciones");
		if (listaAplicaciones != null){
			Iterator lista = listaAplicaciones.iterator();
			Registro aplicacionPerfil;
			while (lista.hasNext()) {
				aplicacionPerfil = (Registro) lista.next();
				sSql = "UPDATE RS_GRAL_USUARIO_PERFIL SET \n" +
					   "B_BLOQUEADO  ='" + (String) aplicacionPerfil.getDefCampo("B_BLOQUEADO") + "'\n" +
					"WHERE CVE_GPO_EMPRESA  = '" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					 " AND CVE_USUARIO = '" + (String) registro.getDefCampo("CVE_USUARIO") + "' \n" +
					 " AND CVE_PERFIL = '" + (String) aplicacionPerfil.getDefCampo("CVE_PERFIL") + "' \n" +
					 " AND CVE_APLICACION = '" + (String) aplicacionPerfil.getDefCampo("CVE_APLICACION") + "' \n";
				ejecutaUpdate();
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
			Registro aplicacionPerfil;
			while (lista.hasNext()) {
				aplicacionPerfil = (Registro) lista.next();
				//BORRA LA FUNCION
				sSql = " DELETE FROM RS_GRAL_USUARIO_PERFIL " +
					" WHERE CVE_GPO_EMPRESA  = '" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_USUARIO = '" + (String) registro.getDefCampo("CVE_USUARIO") + "' \n" +
					" AND CVE_PERFIL = '" + (String) aplicacionPerfil.getDefCampo("CVE_PERFIL") + "' \n" +
					" AND CVE_APLICACION = '" + (String) aplicacionPerfil.getDefCampo("CVE_APLICACION") + "' \n";
				ejecutaUpdate();
			}
		}
		return resultadoCatalogo;
	}
}
