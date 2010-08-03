/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.datos;

import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.comun.util.Fecha2;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de personalidad fiscal.
 */
 
public class TipoMenuDAO extends Conexion2 implements OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros tomando como base el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		LinkedList lista = new LinkedList();
		Registro registro = new Registro();
		registro.addDefCampo("TIPO_MENU", "Horizontal");
		registro.addDefCampo("NOM_TIPO_MENU", "Horizontal");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("TIPO_MENU", "Vertical");
		registro.addDefCampo("NOM_TIPO_MENU", "Vertical");
		lista.add(registro);
		return lista;
	}

}