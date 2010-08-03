/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para asignar el producto a un préstamo grupal.
 */
 
public class SimAsignaProductoDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		//Consulta los productos los asignados anteriormente al cliente.
		sSql =  "SELECT DISTINCT \n"+
			"PC.CVE_GPO_EMPRESA, \n"+
			"PC.CVE_EMPRESA, \n"+
			"PC.ID_PRODUCTO, \n"+
			"P.NOM_PRODUCTO, \n"+
			"P.APLICA_A, \n"+
			"P.ID_PERIODICIDAD, \n"+
			//"PC.VALOR_TASA, \n"+
			//"PC.PLAZO, \n"+
			//"PC.MONTO_MAXIMO, \n"+
			"P.CVE_METODO, \n"+
			"P.FECHA_INICIO_ACTIVACION, \n"+
			"P.FECHA_FIN_ACTIVACION \n"+
			"FROM \n"+
			"SIM_PRODUCTO_CICLO PC, \n"+
			"SIM_PRODUCTO P, \n"+
			"SIM_PRODUCTO_SUCURSAL PS \n"+
			"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
			"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
			"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
			"AND P.FECHA_BAJA_LOGICA IS NULL \n"+
			"AND P.APLICA_A = 'Grupo' \n"+
			"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
			"AND PS.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
			"AND PS.CVE_EMPRESA = P.CVE_EMPRESA \n"+
			"AND PS.ID_PRODUCTO = P.ID_PRODUCTO \n"+
			"ORDER BY PC.ID_PRODUCTO \n";
		
		ejecutaSql();
		return getConsultaLista();
	}
}
