/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.Iterator;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el del catálogo de puesto.
 */
 
public class SimCatalogoPuestoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene las opciones de un menú. Esta función es recursiva si solo es llamada por getMenu()
	 * @param listaFinalServlet Es la lista de objetos que se regresan al servlet que hace llamada
	 * al EJB. Esta es una lista de objetos tipo Registro.
	 * @param listaOpciones Es la lista de opciones de menú encontradas para un nivel. Para cada
	 * objeto de esta lista se buscará si tiene a su vez hijos, conviertiendose de esta
	 * forma en un nivel más dentro de la estructura de un menú.
	 * @param sIdAplicacion Clave de la aplicación a la cual se busca sus opciones de menú
	 * @param iNivelActual Es el nivel dentro del arbol que se forma para cada menú. Es importante
	 * llevar el número de nivel para mostrar los registros en la consulta indentados
	 * de acuerdo a su nivel.
	 */
	public void construyeMenu(LinkedList listaFinalServlet, LinkedList listaOpciones, int iNivelActual, String sCveOpcionExcluir) throws SQLException{
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
			opcion.addDefCampo("NOM_OPCION_INDENTADO", sBloqueIndentado + (String)opcion.getDefCampo("NOM_PUESTO"));
			
			listaFinalServlet.add(opcion);

			//OBTIENE LA CLAVE DE LA OPCION DEL MENU
			String sCveOpcion = (String)opcion.getDefCampo("CVE_PUESTO");
			
			//VERIFICA SI HAY QUE EXCLUIR LA BUSQUEDA DE LAS SUBOPCIONES DE UNA OPCION
			if (sCveOpcionExcluir != null){
				
				//VERIFICA SI LA OPCION ACTUAL NO ES LA OPCION QUE SE QUIERE EXCLUIR
				if (!sCveOpcionExcluir.equals(sCveOpcion)){
					
					//OBTIENE LAS SUBOPCIONES DE UNA OPCION
					LinkedList listaSubOpciones = getOpcionMenu(" = '" + sCveOpcion + "'");
					
					//VERIFICA SI LA OPCION TIENE SUBOPCIONES
					if (listaSubOpciones != null) {
						
						//LA OPCION SI TIENE SUBOPCIONES

						//SE VUELVE A LLAMAR A ESTA FUNCION (RECURSIVIDAD) PARA CONSTRUIR LA NUEVA
						//RAMA DEL ARBOL DEL SUBMENU
						construyeMenu(listaFinalServlet, listaSubOpciones, iNuevoNivel + 1, sCveOpcionExcluir);
					}
				}
			}
			else{
			
				//OBTIENE LAS SUBOPCIONES DE UNA OPCION
				LinkedList listaSubOpciones = getOpcionMenu(" = '" + sCveOpcion + "'");
				//VERIFICA SI LA OPCION TIENE SUBOPCIONES
				if (listaSubOpciones != null) {
					
					//LA OPCION SI TIENE SUBOPCIONES

					//SE VUELVE A LLAMAR A ESTA FUNCION (RECURSIVIDAD) PARA CONSTRUIR LA NUEVA
					//RAMA DEL ARBOL DEL SUBMENU
					construyeMenu(listaFinalServlet, listaSubOpciones, iNuevoNivel + 1, sCveOpcionExcluir);
				}
			}
		}
	}

	/**
	 * Se obtienen las opciones que forman parte de un menú
	 * @param sIdOpcionPadre Clave del menu padre.
	 * @return Lista de registros.
	 */
	public LinkedList getOpcionMenu(String sIdOpcionPadre) throws SQLException{
		//SE OBTIENEN LAS OPCIONES DE UN MENU
		
		sSql = "SELECT \n"+
			   " CVE_PUESTO, \n"+
			   " NOM_PUESTO, \n"+
			   " CVE_PUESTO_PADRE, \n"+
			   " DESCRIPCION \n"+
			   " FROM SIM_CAT_PUESTO \n"+
			   " WHERE CVE_PUESTO_PADRE " + sIdOpcionPadre + " \n " ;
		
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		LinkedList lista = null;
		String sCveOpcionExcluir = null;

		//OBTENEMOS LAS OPCIONES DEL MENU PARA EL PRIMER NIVEL
		LinkedList listaOpciones = getOpcionMenu(" IS NULL ");

		//VERIFICA SI ENCONTRO OPCIONES DEL MENU
		if (listaOpciones != null) {
			//DEBIDO A QUE EL MENU NO ESTA VACIO, ENTONCES INICIALIZAMOS
			//LA LISTA DE REGISTROS QUE SE REGRESARA AL SERVLET
			lista = new LinkedList();
			construyeMenu(lista, listaOpciones, 0, sCveOpcionExcluir);
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
		sSql =  " SELECT \n"+
			" 	CVE_GPO_EMPRESA, \n"+
			" 	CVE_EMPRESA, \n"+
			" 	CVE_PUESTO, \n"+
			" 	NOM_PUESTO, \n"+
			" 	CVE_PUESTO_PADRE, \n"+
			" 	DESCRIPCION \n"+
			" FROM SIM_CAT_PUESTO\n"+
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_PUESTO = '" + (String)parametros.getDefCampo("CVE_PUESTO") + "' \n";
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
		String sIdPuesto = "";
		
		
			sSql =  "INSERT INTO SIM_CAT_PUESTO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"CVE_PUESTO, \n" +
				"NOM_PUESTO, \n" +
				"CVE_PUESTO_PADRE, \n" +
				"DESCRIPCION) \n" +
		           	"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_PUESTO") + "', \n" +
				"'" + (String)registro.getDefCampo("NOM_PUESTO") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_PUESTO_PADRE") + "', \n" +
				"'" + (String)registro.getDefCampo("DESCRIPCION") + "') \n" ;

			//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
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
		sSql =  " UPDATE SIM_CAT_PUESTO SET "+
			" NOM_PUESTO    	='" + (String)registro.getDefCampo("NOM_PUESTO")  + "', \n" +
			" DESCRIPCION    	='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" + 
			" CVE_PUESTO_PADRE 	='" + (String)registro.getDefCampo("CVE_PUESTO_PADRE")  + "' \n" + 
			" WHERE CVE_PUESTO    	='" + (String)registro.getDefCampo("CVE_PUESTO") + "' \n" +
			" AND CVE_EMPRESA   	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA   ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
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
		sSql =  " DELETE FROM SIM_CAT_PUESTO " +
			" WHERE CVE_PUESTO			='" + (String)registro.getDefCampo("CVE_PUESTO") + "' \n" +
			" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}