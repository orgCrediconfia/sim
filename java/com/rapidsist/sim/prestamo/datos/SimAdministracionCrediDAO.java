/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para obtenr el identificador del pr�stamo en base a su clave.
 */
 
public class SimAdministracionCrediDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de b�squeda.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT\n"+
			"GC.VALOR,\n"+ 
			"GC.ID_PERIODICIDAD,\n"+
			"CP.NOM_PERIODICIDAD\n"+

		"FROM SIM_PRESTAMO_GPO_CARGO GC,\n"+
		    "SIM_CAT_PERIODICIDAD CP\n"+

		"WHERE GC.CVE_GPO_EMPRESA     = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
			"AND GC.CVE_EMPRESA       = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "'\n"+
			"AND CP.CVE_GPO_EMPRESA   = GC.CVE_GPO_EMPRESA\n"+
			"AND CP.CVE_EMPRESA       = GC.CVE_EMPRESA\n"+
			"AND GC.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "'\n"+
			"AND GC.ID_CARGO_COMISION = '" + (String)parametros.getDefCampo("ID_CARGO_COMISION") + "'\n"+
			"AND GC.ID_PERIODICIDAD   = CP.ID_PERIODICIDAD\n";
		ejecutaSql();
		return getConsultaLista();
	}
	

}