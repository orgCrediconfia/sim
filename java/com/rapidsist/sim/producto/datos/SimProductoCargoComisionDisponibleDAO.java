/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.producto.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para los cargo o comisiones de un producto.
 */
 
public class SimProductoCargoComisionDisponibleDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql = "SELECT \n"+
			   " CVE_GPO_EMPRESA, \n"+
			   " CVE_EMPRESA, \n"+
			   " ID_ACCESORIO, \n"+
			   " CVE_TIPO_ACCESORIO, \n"+
			   " NOM_ACCESORIO, \n"+
			   " DECODE(BENEFICIARIO,'V','Sí','No') BENEFICIARIO \n"+
			   " FROM SIM_CAT_ACCESORIO \n"+
			   " WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND CVE_TIPO_ACCESORIO ='CARGO_COMISION' \n"+
			"MINUS \n"+		
			"SELECT \n"+
				"PC.CVE_GPO_EMPRESA, \n" +
				"PC.CVE_EMPRESA, \n" +
				"PC.ID_CARGO_COMISION ID_ACCESORIO, \n"+
				"A.CVE_TIPO_ACCESORIO, \n"+
				"A.NOM_ACCESORIO, \n"+
				"DECODE(A.BENEFICIARIO,'V','Sí','No') BENEFICIARIO \n"+
			"FROM SIM_PRODUCTO_CARGO_COMISION PC, \n"+
			"     SIM_CAT_ACCESORIO A, \n"+
			"     SIM_CAT_FORMA_APLICACION F \n"+
			" WHERE PC.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND PC.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND PC.ID_PRODUCTO ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
			" AND A.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
			" AND A.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
			" AND A.ID_ACCESORIO = PC.ID_CARGO_COMISION \n"+
			" AND F.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
			" AND F.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
			" AND F.ID_FORMA_APLICACION = PC.ID_FORMA_APLICACION \n";	
			
		ejecutaSql();
		return getConsultaLista();
	}
}