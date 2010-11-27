/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para el catálogo Dias Festivos.
 */
 
public class SimCatalogoDiaSemanaPagoDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		LinkedList lista = new LinkedList();
		Registro registro = new Registro();
		registro = new Registro();
		registro.addDefCampo("DIA_SEMANA_PAGO", "Lunes");
		registro.addDefCampo("NOM_DIA_SEMANA_PAGO", "Lunes");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("DIA_SEMANA_PAGO", "Martes");
		registro.addDefCampo("NOM_DIA_SEMANA_PAGO", "Martes");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("DIA_SEMANA_PAGO", "Miercoles");
		registro.addDefCampo("NOM_DIA_SEMANA_PAGO", "Miercoles");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("DIA_SEMANA_PAGO", "Jueves");
		registro.addDefCampo("NOM_DIA_SEMANA_PAGO", "Jueves");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("DIA_SEMANA_PAGO", "Viernes");
		registro.addDefCampo("NOM_DIA_SEMANA_PAGO", "Viernes");
		lista.add(registro);
		registro = new Registro();
		return lista;
	}
}