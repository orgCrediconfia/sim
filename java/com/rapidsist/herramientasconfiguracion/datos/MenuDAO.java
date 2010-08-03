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
import java.util.Iterator;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de menús.
 */
public class MenuDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene las opciones de un menú. Esta función es recursiva si solo es llamada por getMenu()
	 * @param listaFinalServlet Es la lista de objetos que se regresan al servlet que hace llamada
	 *          al EJB. Esta es una lista de objetos tipo Registro.
	 * @param listaOpciones Es la lista de opciones de menú encontradas para un nivel. Para cada
	 *          objeto de esta lista se buscará si tiene a su vez hijos, conviertiendose de esta
	 *          forma en un nivel más dentro de la estructura de un menú.
	 * @param sIdAplicacion Clave de la aplicación a la cual se busca sus opciones de menú
	 * @param iNivelActual Es el nivel dentro del arbol que se forma para cada menú. Es importante
	 *          llevar el número de nivel para mostrar los registros en la consulta indentados
	 *          deacuerdo a su nivel.
	 */
	public void construyeMenu(LinkedList listaFinalServlet, LinkedList listaOpciones, String sIdAplicacion, int iNivelActual, String sCveOpcionExcluir) throws SQLException{
		int iNuevoNivel = iNivelActual;
		String sIndentado = "&nbsp;&nbsp;&nbsp;&nbsp;";
		String sBloqueIndentado = "";

		//RECORRE LA LISTA DE OPCIONES ENCONTRADAS. PARA CADA OPCION SE BUSCARA SI TIENE A SU VEZ
		//OTRAS SUBOPCIONES ASIGNADAS
		Iterator lista = listaOpciones.iterator();
		while (lista.hasNext()){
			Registro opcion = (Registro)lista.next();

			//CALCULA LA INDENTACION
			sBloqueIndentado = "";
			for (int iContador = 0; iContador < iNivelActual; iContador ++){
				sBloqueIndentado = sBloqueIndentado + sIndentado;
			}
			opcion.addDefCampo("BLOQUE_INDENTADO",sBloqueIndentado);
			opcion.addDefCampo("NOM_OPCION_INDENTADO", sBloqueIndentado + (String)opcion.getDefCampo("NOM_OPCION"));

			listaFinalServlet.add(opcion);

			//OBTIENE LA CLAVE DE LA OPCION DEL MENU
			String sCveOpcion = (String)opcion.getDefCampo("CVE_OPCION");

			//VERIFICA SI HAY QUE EXCLUIR LA BUSQUEDA DE LAS SUBOPCIONES DE UNA OPCION
			if (sCveOpcionExcluir != null){

				//VERIFICA SI LA OPCION ACTUAL NO ES LA OPCION QUE SE QUIERE EXCLUIR
				if (!sCveOpcionExcluir.equals(sCveOpcion)){
					//OBTIENE LAS SUBOPCIONES DE UNA OPCION
					LinkedList listaSubOpciones = getOpcionMenu(sIdAplicacion, " = '" + sCveOpcion + "'");
					//VERIFICA SI LA OPCION TIENE SUBOPCIONES
					if (listaSubOpciones != null) {
						//LA OPCION SI TIENE SUBOPCIONES

						//SE VUELVE A LLAMAR A ESTA FUNCION (RECURSIVIDAD) PARA CONSTRUIR LA NUEVA
						//RAMA DEL ARBOL DEL SUBMENU
						construyeMenu(listaFinalServlet, listaSubOpciones, sIdAplicacion, iNuevoNivel + 1, sCveOpcionExcluir);
					}
				}
			}
			else{
				//OBTIENE LAS SUBOPCIONES DE UNA OPCION
				LinkedList listaSubOpciones = getOpcionMenu(sIdAplicacion, " = '" + sCveOpcion + "'");
				//VERIFICA SI LA OPCION TIENE SUBOPCIONES
				if (listaSubOpciones != null) {
					//LA OPCION SI TIENE SUBOPCIONES

					//SE VUELVE A LLAMAR A ESTA FUNCION (RECURSIVIDAD) PARA CONSTRUIR LA NUEVA
					//RAMA DEL ARBOL DEL SUBMENU
					construyeMenu(listaFinalServlet, listaSubOpciones, sIdAplicacion, iNuevoNivel + 1, sCveOpcionExcluir);
				}
			}
		}
	}

	/**
	 * Se obtienen las opciones que forman parte de un menú
	 * @param sIdAplicacion Clave aplicación.
	 * @param sIdOpcionPadre Clave del menu padre.
	 * @return Lista de registros.
	 */
	public LinkedList getOpcionMenu(String sIdAplicacion, String sIdOpcionPadre) throws SQLException{
		//SE OBTIENEN LAS OPCIONES DE UN MENU
		sSql ="SELECT ME.CVE_OPCION, \n"+
			  "       ME.CVE_OPCION_PADRE, \n"+
			  "       ME.CVE_FUNCION, \n"+
			  "       ME.CVE_APLICACION, \n"+
			  "       ME.NOM_OPCION, \n"+
			  "       ME.NUM_POSICION, \n"+
			  "       FU.URL_FUNCION \n" +
			  " FROM  RS_CONF_MENU ME, \n"+
			  "       RS_CONF_FUNCION FU \n" +
			  " WHERE ME.CVE_APLICACION = '" + sIdAplicacion + "' \n" +
			  "       AND ME.CVE_OPCION_PADRE " + sIdOpcionPadre + " \n " +
			  "       AND ME.CVE_FUNCION = FU.CVE_FUNCION (+) \n" +
			  " ORDER BY ME.NUM_POSICION ";
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene un conjunto de registros tomando como base el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		LinkedList lista = null;
		String sCveOpcionExcluir = null;

		if (((String)parametros.getDefCampo("Filtro")).equals("OpcionesPadreExcluyendoRegistro")){
			sCveOpcionExcluir = (String)parametros.getDefCampo("CVE_OPCION");
		}

		//OBTENEMOS LAS OPCIONES DEL MENU PARA EL PRIMER NIVEL
		LinkedList listaOpciones = getOpcionMenu( (String) parametros.getDefCampo("CVE_APLICACION"), " is null ");

		//VERIFICA SI ENCONTRO OPCIONES DEL MENU
		if (listaOpciones != null) {
			//DEBIDO A QUE EL MENU NO ESTA VACIO, ENTONCES INICIALIZAMOS
			//LA LISTA DE REGISTROS QUE SE REGRESARA AL SERVLET
			lista = new LinkedList();
			construyeMenu(lista, listaOpciones, (String) parametros.getDefCampo("CVE_APLICACION"), 0, sCveOpcionExcluir);
		}
		return lista;
	}

	/**
	 * Obtiene un registro en base a la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql ="SELECT ME.CVE_OPCION, \n"+
			  "       ME.CVE_OPCION_PADRE, \n"+
			  "       ME.CVE_FUNCION, \n"+
			  "       ME.CVE_APLICACION, \n"+
			  "       ME.NOM_OPCION, \n"+
			  "       ME.NUM_POSICION \n"+
			  " FROM  RS_CONF_MENU ME, \n"+
			  "       RS_CONF_FUNCION FU \n" +
			  " WHERE ME.CVE_OPCION = '"+ (String)parametros.getDefCampo("CVE_OPCION") + "' \n" +
			  "       AND ME.CVE_FUNCION = FU.CVE_FUNCION (+)\n" ;
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

		// OBTIENE EL MAYOR NÚMERO Y LE SUMA UNO PARA
		// ASIGNARLE ORDEN A LA OPCIÓN QUE SE VA A DAR DE ALTA
		if (((String)registro.getDefCampo("CVE_OPCION_PADRE")).equals("null")) {
			sSql ="SELECT MAX(NUM_POSICION)+1 NUM_POSICION \n"+
				  "FROM RS_CONF_MENU \n"+
				  "WHERE CVE_APLICACION = '" + (String)registro.getDefCampo("CVE_APLICACION") + "' \n" +
				  "  AND CVE_OPCION_PADRE IS NULL ";
		}
		else {
			sSql ="SELECT MAX(NUM_POSICION)+1 NUM_POSICION \n"+
				  "FROM RS_CONF_MENU \n"+
				  "WHERE CVE_APLICACION = '" + (String)registro.getDefCampo("CVE_APLICACION") + "' \n" +
				  "  AND CVE_OPCION_PADRE = '" + (String)registro.getDefCampo("CVE_OPCION_PADRE") + "' \n";
		}
		ejecutaSql();
		if (rs.next()){
			if (rs.getString("NUM_POSICION")==null){
				registro.addDefCampo("NUM_POSICION","1");
			}
			else {
				registro.addDefCampo("NUM_POSICION",rs.getString("NUM_POSICION"));
			}
			sSql ="INSERT INTO RS_CONF_MENU ( \n"+
					  "CVE_OPCION, \n"+
					  "CVE_APLICACION, \n"+
					  "NOM_OPCION, \n"+
					  "CVE_OPCION_PADRE, \n"+
					  "CVE_FUNCION, \n"+
					  "NUM_POSICION) \n"+
				  " VALUES ( \n" +
						"'" + (String)registro.getDefCampo("CVE_OPCION") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_APLICACION") + "', \n" +
						"'" + (String)registro.getDefCampo("NOM_OPCION") + "', \n" ;

			if (((String)registro.getDefCampo("CVE_OPCION_PADRE")).equals("null")){
				sSql = sSql + " null, \n";
			}
			else{
				sSql = sSql + "'" + (String)registro.getDefCampo("CVE_OPCION_PADRE") + "', \n" ;
			}

			if (((String)registro.getDefCampo("CVE_FUNCION")).equals("null")){
				sSql = sSql + " null, \n";
			}
			else{
				sSql = sSql + "'" + (String)registro.getDefCampo("CVE_FUNCION") + "', \n" ;
			}

			sSql = sSql + (String)registro.getDefCampo("NUM_POSICION") + ") \n";

			//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		}
		else{
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

		//OBTIENE EL REGISTRO ORIGINAL
		Registro registroOriginal = (Registro)registro.getDefCampo("RegistroOriginal");
		String sNumOpcionOriginal = (String)registroOriginal.getDefCampo("NUM_POSICION");
		String sCveOpcionPadreOriginal = (String)registroOriginal.getDefCampo("CVE_OPCION_PADRE");

		String sCveOpcionPadre =(String)registro.getDefCampo("CVE_OPCION_PADRE");
		String sNumOpcion = "";
		//VERIFICA SI CAMBIO LA OPCION PADRE
		if (sCveOpcionPadre.equals(sCveOpcionPadreOriginal)){
			sNumOpcion = sNumOpcionOriginal;
		}
		else{
			// OBTIENE EL MAYOR NÚMERO Y LE SUMA UNO PARA
			// ASIGNARLE ORDEN A LA OPCIÓN QUE SE VA A DAR DE ALTA
			if (sCveOpcionPadre.equals("null")) {
				sSql ="SELECT MAX(NUM_POSICION)+1 NUM_POSICION \n"+
					  "FROM RS_CONF_MENU \n"+
					  "WHERE CVE_APLICACION = '" + (String)registro.getDefCampo("CVE_APLICACION") + "' \n" +
					  "  AND CVE_OPCION_PADRE IS NULL ";
			}
			else {
				sSql ="SELECT MAX(NUM_POSICION)+1 NUM_POSICION \n"+
					  "FROM RS_CONF_MENU \n"+
					  "WHERE CVE_APLICACION = '" + (String)registro.getDefCampo("CVE_APLICACION") + "' \n" +
					  "  AND CVE_OPCION_PADRE = '" + sCveOpcionPadre + "' \n";
			}
			ejecutaSql();
			rs.next();
			if (rs.getString("NUM_POSICION")==null){
				sNumOpcion = "1";
			}
			else{
				sNumOpcion = rs.getString("NUM_POSICION");
			}
		}

		sSql ="UPDATE RS_CONF_MENU SET \n" +
					"NOM_OPCION= '" + (String)registro.getDefCampo("NOM_OPCION") + "', \n " +
					"NUM_POSICION= " + sNumOpcion + ", \n " ;

		if (sCveOpcionPadre.equals("null")){
			sSql = sSql + " CVE_OPCION_PADRE = null, \n";
		}
		else{
			sSql = sSql + " CVE_OPCION_PADRE = '" + sCveOpcionPadre + "', \n" ;
		}

		if (((String)registro.getDefCampo("CVE_FUNCION")).equals("null")){
			sSql = sSql + " CVE_FUNCION = null \n";
		}
		else{
			sSql = sSql + "CVE_FUNCION = '" + (String)registro.getDefCampo("CVE_FUNCION") + "' \n" ;
		}
		sSql = sSql + " WHERE CVE_OPCION = '" + (String)registro.getDefCampo("CVE_OPCION") + "'";

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

		//VERIFICA SI HAY UNA OPCION DE MENU QUE DEPENDA DE LA QUE SE REQUIERE BORRAR
		sSql =  "SELECT CVE_OPCION FROM RS_CONF_MENU \n"+
				  "WHERE CVE_APLICACION= '" + (String)registro.getDefCampo("CVE_APLICACION") + "' \n"+
				   " AND CVE_OPCION_PADRE = '" + (String)registro.getDefCampo("CVE_OPCION") + "' ";
		ejecutaSql();
		if (rs.next()){
			resultadoCatalogo.mensaje.setClave("MENU_OPCION_RELACIONADA");
		}
		else{
			//BORRA LA FUNCION
			sSql = "DELETE FROM RS_CONF_MENU \n" +
					  " WHERE CVE_APLICACION='" + (String)registro.getDefCampo("CVE_APLICACION") + "' \n" +
						" AND CVE_OPCION = '" + (String)registro.getDefCampo("CVE_OPCION") + "' ";

			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		}

		return resultadoCatalogo;
	}
}