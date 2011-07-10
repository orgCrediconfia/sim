/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;


/**
 * Administra los accesos a la base de datos para consultar los créditos de un cliente.
 */
 
public class SimPrestamoCreditosPersonalesDAO extends Conexion2 implements OperacionConsultaTabla {
	

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT \n"+
				"V.CVE_GPO_EMPRESA, \n"+
				"V.CVE_EMPRESA, \n"+
				"V.ID_PRESTAMO, \n"+
				"V.CVE_PRESTAMO, \n"+
				"V.CVE_NOMBRE, \n"+
				"V.ID_PRODUCTO, \n"+
				"V.NUM_CICLO, \n"+
				"V.FECHA_ENTREGA, \n"+
				"V.FECHA_REAL, \n"+
				"V.NOMBRE, \n"+
				"V.ID_ETAPA_PRESTAMO, \n"+
				"V.NOMBRE, \n"+
				"V.ID_ETAPA_PRESTAMO, \n"+
				"V.CVE_ASESOR_CREDITO, \n"+
				"V.ID_SUCURSAL, \n"+
				"V.ID_ETAPA_PRESTAMO, \n"+
				"E.NOM_ESTATUS_PRESTAMO \n"+
			"FROM \n"+
			"(SELECT * FROM V_CREDITO ORDER BY CVE_PRESTAMO) V, \n"+
			"     SIM_CAT_ETAPA_PRESTAMO E \n"+
			"WHERE V.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND V.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND APLICA_A != 'GRUPO' \n"+
			"AND E.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n"+
			"AND E.CVE_EMPRESA = V.CVE_EMPRESA \n"+
			"AND E.ID_ETAPA_PRESTAMO = V.ID_ETAPA_PRESTAMO \n"+
			"AND ROWNUM <= 100 \n";
		
		if (parametros.getDefCampo("CVE_NOMBRE") != null) {
			sSql = sSql + "AND CVE_NOMBRE = '" + (String) parametros.getDefCampo("CVE_NOMBRE") + "' \n";
		}
		if (parametros.getDefCampo("NOMBRE") != null) {
			sSql = sSql + "AND UPPER(NOMBRE) LIKE '%" + ((String) parametros.getDefCampo("NOMBRE")).toUpperCase() + "%' \n";
		}
		
		sSql = sSql + "ORDER BY CVE_PRESTAMO \n";
		
		ejecutaSql();
		return getConsultaLista();
	}
}