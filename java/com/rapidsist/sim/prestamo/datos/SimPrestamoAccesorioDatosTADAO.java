/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para las actividades o requisitos del préstamo.
 */
 
public class SimPrestamoAccesorioDatosTADAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		sSql =  " SELECT \n"+
			" 	CC.CVE_GPO_EMPRESA, \n"+
			" 	CC.CVE_EMPRESA, \n"+
			" 	A.NOM_ACCESORIO, \n"+
			"	FA.NOM_FORMA_APLICACION, \n"+
			" 	CC.VALOR, \n"+
			" 	U.NOM_UNIDAD, \n"+
			" 	P.NOM_PERIODICIDAD \n"+
			" FROM SIM_PRESTAMO_CARGO_COMISION CC, \n"+
			"      SIM_CAT_ACCESORIO A, \n"+
			"      SIM_CAT_FORMA_APLICACION FA, \n"+
			"      SIM_CAT_UNIDAD U, \n"+
			"      SIM_CAT_PERIODICIDAD P \n"+
			" WHERE CC.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CC.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CC.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND A.CVE_GPO_EMPRESA = CC.CVE_GPO_EMPRESA \n" +
			" AND A.CVE_EMPRESA = CC.CVE_EMPRESA \n"+
			" AND A.ID_ACCESORIO = CC.ID_CARGO_COMISION \n"+
			" AND FA.CVE_GPO_EMPRESA = CC.CVE_GPO_EMPRESA \n"+ 
			" AND FA.CVE_EMPRESA = CC.CVE_EMPRESA \n"+
			" AND FA.ID_FORMA_APLICACION = CC.ID_FORMA_APLICACION \n"+
			" AND U.CVE_GPO_EMPRESA (+)= CC.CVE_GPO_EMPRESA \n"+
			" AND U.CVE_EMPRESA (+)= CC.CVE_EMPRESA \n"+
			" AND U.ID_UNIDAD (+)= CC.ID_UNIDAD \n"+
			" AND P.CVE_GPO_EMPRESA (+)= CC.CVE_GPO_EMPRESA \n"+
			" AND P.CVE_EMPRESA (+)= CC.CVE_EMPRESA \n"+
			" AND P.ID_PERIODICIDAD (+)= CC.ID_PERIODICIDAD \n"+
			" AND CC.VALOR IS NOT NULL \n"+
			" ORDER BY CC.ID_CARGO_COMISION \n";
		
		ejecutaSql();
		return getConsultaLista();
	}
}