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

/**
 * Administra los accesos a la base de datos para el los documentos de un producto.
 */
 
public class SimProductoDocumentacionDisponibleDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  " SELECT \n"+
				" D.CVE_GPO_EMPRESA, \n" +
				" D.CVE_EMPRESA, \n" +
				" D.ID_DOCUMENTO, \n"+
				" D.NOM_DOCUMENTO, \n"+
				" D.ID_REPORTE, \n"+
				" R.APLICA_A, \n"+
				" R.NOM_REPORTE, \n"+
				" D.DESCRIPCION \n"+
			" FROM SIM_CAT_DOCUMENTO D, \n"+
			"      SIM_CAT_TIPO_DOCUMENTACION C, \n"+
			"      SIM_CAT_REPORTE R \n"+
			" WHERE D.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND D.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
			" AND D.ID_TIPO_DOCUMENTACION = '3' \n" +
			" AND C.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
			" AND C.CVE_EMPRESA = D.CVE_EMPRESA \n" +
			" AND C.ID_TIPO_DOCUMENTACION = D.ID_TIPO_DOCUMENTACION \n" +
			" AND R.CVE_GPO_EMPRESA (+)= D.CVE_GPO_EMPRESA \n" +
			" AND R.CVE_EMPRESA (+)= D.CVE_EMPRESA \n" +
			" AND R.ID_REPORTE (+)= D.ID_REPORTE \n" +
			" AND (R.APLICA_A = '" + (String)parametros.getDefCampo("APLICA_A") + "' OR R.APLICA_A IS NULL) \n" ;
		
			sSql = sSql + " MINUS \n"+
			" SELECT \n"+
				" PD.CVE_GPO_EMPRESA, \n" +
				" PD.CVE_EMPRESA, \n" +
				" PD.ID_DOCUMENTO, \n"+
				" DO.NOM_DOCUMENTO, \n"+
				" DO.ID_REPORTE, \n"+
				" R.APLICA_A, \n"+
				" R.NOM_REPORTE, \n"+
				" DO.DESCRIPCION \n"+
			" FROM SIM_PRODUCTO_DOCUMENTACION PD, \n"+
			"      SIM_CAT_DOCUMENTO DO, \n"+
			"      SIM_CAT_REPORTE R \n"+
			" WHERE PD.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND PD.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND PD.ID_PRODUCTO ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
			" AND DO.CVE_GPO_EMPRESA = PD.CVE_GPO_EMPRESA \n"+
			" AND DO.CVE_EMPRESA = PD.CVE_EMPRESA \n"+
			" AND DO.ID_DOCUMENTO = PD.ID_DOCUMENTO \n"+
			" AND R.CVE_GPO_EMPRESA (+)= DO.CVE_GPO_EMPRESA \n" +
			" AND R.CVE_EMPRESA (+)= DO.CVE_EMPRESA \n" +
			" AND R.ID_REPORTE (+)= DO.ID_REPORTE \n" +
			" AND (R.APLICA_A = '" + (String)parametros.getDefCampo("APLICA_A") + "' OR R.APLICA_A IS NULL) \n" +
			" ORDER BY NOM_DOCUMENTO \n";
			
		ejecutaSql();
		return getConsultaLista();
	}
}