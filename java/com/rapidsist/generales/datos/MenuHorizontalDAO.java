/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.generales.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de menú horizontal.
 */
public class MenuHorizontalDAO extends Conexion2 implements OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		LinkedList lista = new LinkedList();
		
		String sIdGrupo = (String)parametros.getDefCampo("ID_GRUPO");
		String sIdAplicacion = (String)parametros.getDefCampo("ID_APLICACION");
		String sIdOpcionPadre = (String)parametros.getDefCampo("ID_OPCION_PADRE");
		
		lista = getOpcionMenu(sIdGrupo,sIdAplicacion,sIdOpcionPadre);		
		return lista;
	}

	
	/**
	 * Se obtienen las opciones que forman parte de un menú.
	 * @param sIdGrupo Clave grupo.
	 * @param sIdAplicacion Clave aplicación.
	 * @param sIdOpcionPadre Clave opción padre.
	 * @return lista de opciones que forman parte de un menú.
	 */
	 
	 /* 
	 Esté método fue tomado del proyecto Agsa para implementar en menú horizontal
	 */
	public LinkedList getOpcionMenu(String sIdGrupo, String sIdAplicacion, String sIdOpcionPadre){
		LinkedList listaOpciones = null;
		boolean bInicializa=true;
		try{
			//SE OBTIENEN LAS OPCIONES DE UN MENU
			sSql ="SELECT ME.ID_OPCION, ME.NOM_OPCION, ME.URL, ME.ID_OPCION_PADRE, ME.NUM_POSICION " +
					" FROM  MENU ME " +
					" WHERE ME.ID_APLICACION = " + sIdAplicacion +
						" AND ME.ID_OPCION_PADRE " + sIdOpcionPadre +
						//" AND ME.ID_FUNCION = FU.ID_FUNCION " +
						//" AND ME.ID_FUNCION = GF.ID_FUNCION " +
						//" AND GF.ID_GRUPO = " + sIdGrupo +
						//" AND ME.SITUACION = 'AC' " +
						//" AND ME.B_BLOQUEADO = 'F' " +
						//" AND FU.SITUACION = 'AC' " +
						//" AND FU.B_BLOQUEADO = 'F' " +
						//" AND GF.SITUACION = 'AC' " +
						//" AND GF.B_BLOQUEADO = 'F' " +
					" ORDER BY ME.NUM_POSICION ";
			ejecutaSql();
			while (rs.next()){
				//VERIFICA SI ES NECESARIO INICIALIZAR LA LISTA
				if (bInicializa){
					bInicializa = false;
					listaOpciones = new LinkedList();
				}
				String sUrl = (rs.getString("URL") == null) ? "" : rs.getString("URL");
				
				Registro registroOpcion = new Registro();
				registroOpcion.addDefCampo("ID_OPCION",rs.getString("ID_OPCION"));
				//registroOpcion.addDefCampo("ID_FUNCION",rs.getString("ID_FUNCION"));
				registroOpcion.addDefCampo("NOM_OPCION",rs.getString("NOM_OPCION"));
				registroOpcion.addDefCampo("URL",sUrl);
				listaOpciones.add(registroOpcion);
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return listaOpciones;
	}
	


}