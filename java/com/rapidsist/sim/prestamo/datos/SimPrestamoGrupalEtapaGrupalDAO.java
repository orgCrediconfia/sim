/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para consultar la etapa del crédito grupal.
 */
 
public class SimPrestamoGrupalEtapaGrupalDAO extends Conexion2 implements OperacionConsultaRegistro {

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  " SELECT \n"+
				" 	NOM_ESTATUS_PRESTAMO ETAPA_GRUPAL \n"+
				" FROM SIM_CAT_ETAPA_PRESTAMO \n"+
				" WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND ID_ETAPA_PRESTAMO = ( \n"+
				"		SELECT DISTINCT D.ID_ETAPA_PRESTAMO \n" +
				"		FROM SIM_PRESTAMO_GPO_DET D, \n" +
				"		SIM_PRESTAMO_ETAPA E \n" +
				"		WHERE D.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"		AND D.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"		AND D.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +    
				"		AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
				"		AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" + 
				"		AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
				"		AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO \n" +
				"		AND E.ORDEN_ETAPA = ( \n" +
				"		SELECT MIN(ORDEN_ETAPA) FROM \n" +
				"		(SELECT D.ID_ETAPA_PRESTAMO, E.ORDEN_ETAPA \n" +
				"		FROM SIM_PRESTAMO_GPO_DET D, \n" + 
				"		SIM_PRESTAMO_ETAPA E \n" +
				"		WHERE D.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"		AND D.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"		AND D.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +    
				"		AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
				"		AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" + 
				"		AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
				"		AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO))) \n";
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}
