/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;

import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.publicaciones.datos.PubAdministracionCarpetas;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.util.Iterator;
import com.rapidsist.comun.util.Fecha2;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de autorización de publicaciones.
 */
public class PublAutorizacionDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		boolean bNomPublicacion = false;
		LinkedList lista = new LinkedList();
		String sFiltro = (String) parametros.getDefCampo("Filtro");

		sSql =  "SELECT PUB_PUB.* \n"+
				" FROM RS_PUB_GPO_RESP_SECCION GPO_RES_SEC, \n"+
				"      RS_PUB_SECCION PUB_SEC, \n"+
				"      RS_PUB_PUBLICACION PUB_PUB \n"+
				" WHERE GPO_RES_SEC.CVE_GPO_EMPRESA = '"+(String) parametros.getDefCampo("CVE_GPO_EMPRESA")+"' \n"+
				"       AND GPO_RES_SEC.CVE_PORTAL = '"+(String) parametros.getDefCampo("CVE_PORTAL")+"' \n"+
				"       AND GPO_RES_SEC.CVE_USUARIO = '" + (String) parametros.getDefCampo("CVE_USUARIO") + "' \n"+
				"       AND GPO_RES_SEC.CVE_GPO_EMPRESA = PUB_SEC.CVE_GPO_EMPRESA \n"+
				"       AND GPO_RES_SEC.CVE_PORTAL = PUB_SEC.CVE_PORTAL \n"+
				"       AND GPO_RES_SEC.CVE_GPO_RESP = PUB_SEC.CVE_GPO_RESP \n"+
				"       AND PUB_SEC.CVE_GPO_EMPRESA = PUB_PUB.CVE_GPO_EMPRESA \n"+
				"       AND PUB_SEC.CVE_PORTAL = PUB_PUB.CVE_PORTAL \n"+
				"       AND PUB_SEC.CVE_SECCION = PUB_PUB.CVE_SECCION \n"+
				"       AND PUB_PUB.B_AUTORIZADO = 'F' \n";
		if (parametros.getDefCampo("CVE_SECCION") != null) {
			sSql = sSql + "AND PUB_PUB.CVE_SECCION ='" + (String) parametros.getDefCampo("CVE_SECCION") + "' \n";
			bNomPublicacion = true;
		}
		if (parametros.getDefCampo("F_INI_VIGENCIA") != null) {
			sSql = sSql + " AND PUB_PUB.F_INI_VIGENCIA ='" + (String) parametros.getDefCampo("F_INI_VIGENCIA") + "' \n";
			bNomPublicacion = true;
		}
		if (parametros.getDefCampo("F_FIN_VIGENCIA") != null) {
			sSql = sSql + " AND PUB_PUB.F_FIN_VIGENCIA ='" + (String) parametros.getDefCampo("F_FIN_VIGENCIA") + "' \n";
			bNomPublicacion = true;
		}
		if (parametros.getDefCampo("ID_NIVEL_ACCESO") != null) {
			sSql = sSql + " AND PUB_PUB.ID_NIVEL_ACCESO ='" + (String) parametros.getDefCampo("ID_NIVEL_ACCESO") + "' \n";
			bNomPublicacion = true;
		}
		if (parametros.getDefCampo("NOM_PUBLICACION") != null) {
			sSql = sSql + " AND PUB_PUB.NOM_PUBLICACION LIKE'%" + (String) parametros.getDefCampo("NOM_PUBLICACION") + "%' \n";
			bNomPublicacion = true;
		}
		sSql = sSql  + " ORDER BY PUB_PUB.ID_PUBLICACION ";
		System.out.println("no entendi publicaciones aprobadas"+sSql);
		ejecutaSql();
		while (rs.next()){
			Registro registro= new Registro();
			registro.addDefCampo("CVE_GPO_EMPRESA",rs.getString("CVE_GPO_EMPRESA"));
			registro.addDefCampo("CVE_PORTAL",rs.getString("CVE_PORTAL"));
			registro.addDefCampo("CVE_SECCION",rs.getString("CVE_SECCION"));
			registro.addDefCampo("ID_PUBLICACION",rs.getString("ID_PUBLICACION"));
			registro.addDefCampo("ID_NIVEL_ACCESO",rs.getString("ID_NIVEL_ACCESO"));
			registro.addDefCampo("NOM_PUBLICACION",rs.getString("NOM_PUBLICACION"));
			registro.addDefCampo("DESC_PUBLICACION",rs.getString("DESC_PUBLICACION"));
			registro.addDefCampo("URL_PUBLICACION",rs.getString("URL_PUBLICACION"));
			registro.addDefCampo("URL_IMAGEN",rs.getString("URL_IMAGEN"));
			if (rs.getString("B_AUTORIZADO").equals("V")){
				registro.addDefCampo("B_AUTORIZADO","SI");
			}
			else{
				registro.addDefCampo("B_AUTORIZADO","NO");
			}
			registro.addDefCampo("F_INI_VIGENCIA",Fecha2.formatoSimple(rs.getDate("F_INI_VIGENCIA")));
			registro.addDefCampo("F_FIN_VIGENCIA",Fecha2.formatoSimple(rs.getDate("F_FIN_VIGENCIA")));
			registro.addDefCampo("PASSWORD",rs.getString("PASSWORD"));
			registro.addDefCampo("CONTADOR",rs.getString("CONTADOR"));
			registro.addDefCampo("CVE_USUARIO",rs.getString("CVE_USUARIO"));
			registro.addDefCampo("B_PRINCIPAL",rs.getString("B_PRINCIPAL"));
			lista.add(registro);
		}
		return lista;
	}

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql = " SELECT \n"+
			   " PS.CVE_GPO_EMPRESA, \n"+
			   " PS.CVE_PORTAL, \n"+
			   " PS.CVE_SECCION, \n"+
			   " PS.CVE_SECCION_PADRE, \n"+
			   " PS.CVE_GPO_RESP, \n"+
			   " PS.NOM_SECCION, \n"+
			   " PS.B_BLOQUEADO, \n"+
			   " PS.NUM_ORDEN, \n"+
			   " GR.CVE_GPO_RESP, \n"+
			   " GR.DESC_GPO_RESP \n"+
			   " FROM \n"+
			   " RS_PUB_SECCION PS, RS_PUB_CONFIG_GPO_RESP GR \n"+
			   " WHERE PS.CVE_PORTAL ='" + (String)parametros.getDefCampo("CVE_PORTAL") + "' \n"+
			   " AND PS.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND PS.CVE_SECCION = '" + (String)parametros.getDefCampo("CVE_SECCION") + "' \n"+
			   " AND PS.CVE_GPO_RESP = GR.CVE_GPO_RESP \n"+
			   " AND  PS.CVE_GPO_EMPRESA = GR.CVE_GPO_EMPRESA \n";

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
		String sIdPublicacion = "";
		LinkedList listaPublicaciones = (LinkedList)registro.getDefCampo("ListaPublicaciones");
		if (listaPublicaciones != null){
			Iterator lista = listaPublicaciones.iterator();
			while (lista.hasNext()){
				sIdPublicacion = (String)lista.next();
				sSql = "UPDATE RS_PUB_PUBLICACION SET " +
					" B_AUTORIZADO           ='V' \n" +
					" WHERE CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_PORTAL         ='" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n"+
					" AND ID_PUBLICACION    ='" + sIdPublicacion + "' \n";
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
		boolean bBorroArchivo = false;
		String sIdPublicacion = "";
		LinkedList listaPublicaciones = (LinkedList)registro.getDefCampo("ListaPublicaciones");
		if (listaPublicaciones != null){
			Iterator lista = listaPublicaciones.iterator();
			while (lista.hasNext()){
				sIdPublicacion = (String)lista.next();
				//SE OBTIENE EL REGISTRO DE LA PUBLICACION SOLICITADA
				//Y EL RESULTADO SE REGRESA A LA CLASE QUE BORRA LOS ARCHIVOS
				sSql = " SELECT * \n"+
					" FROM RS_PUB_PUBLICACION \n"+
					" WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND CVE_PORTAL = '" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n"+
					" AND ID_PUBLICACION = '" + sIdPublicacion + "' \n";
				this.ejecutaSql();
				Registro parametros = this.getConsultaRegistro();
				//SE INSTANCIA LA CLASE QUE MANIPULA LOS ARCHIVOS
				PubAdministracionCarpetas AdmonCarpetas = new PubAdministracionCarpetas();
				try{
					//SI SE BORRO EL REGISTRO EL ARCHIVO FISICAMENTE ENTONCES TAMBIEN SE BORRA DE LA BASE DE DATOS
					bBorroArchivo = AdmonCarpetas.BorrarArchivo(parametros, this);
					sSql = "DELETE FROM RS_PUB_PUBLICACION " +
						" WHERE CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						" AND CVE_PORTAL         ='" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n"+
						" AND ID_PUBLICACION    ='" + sIdPublicacion + "' \n";
					 //VERIFICA SI NO SE ACTUALIZO EL REGISTRO
					 if (ejecutaUpdate() == 0) {
						 resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
					 }
				}
				catch (Exception e){
					//SI NO SE PUDO BORRAR EL ARCHIVO
					e.printStackTrace();
				}
			 }
		 }
		 return resultadoCatalogo;
	}
}
