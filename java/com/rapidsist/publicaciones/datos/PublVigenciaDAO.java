/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;

import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.comun.util.Fecha2;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el cat�logo de personalidad fiscal.
 */
 
public class PublVigenciaDAO extends Conexion2 implements OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		LinkedList lista = new LinkedList();
		Registro registro = new Registro();
		registro.addDefCampo("Clave", "1");
		registro.addDefCampo("Descripcion", "Vigentes");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "2");
		registro.addDefCampo("Descripcion", "Hist�ricas");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "3");
		registro.addDefCampo("Descripcion", "Vigentes e Hist�ricas");
		lista.add(registro);
		return lista;
	}

}