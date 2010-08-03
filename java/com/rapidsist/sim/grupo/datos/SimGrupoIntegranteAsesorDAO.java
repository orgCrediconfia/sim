/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.grupo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para consultar el asesor del crédito grupal.
 */
 
public class SimGrupoIntegranteAsesorDAO extends Conexion2 implements OperacionConsultaRegistro, OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		//Consulta el asesor de crédito del grupo.

		sSql = " SELECT \n"+
			   " COUNT (*) NUM_ASESOR \n"+
			   " FROM SIM_GRUPO_INTEGRANTE GI, \n"+
			   "      RS_GRAL_PERSONA P \n"+
			   " WHERE  GI.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND GI.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND GI.ID_GRUPO = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
			   " AND P.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n"+
			   " AND P.CVE_EMPRESA = GI.CVE_EMPRESA \n"+
			   " AND P.ID_PERSONA = GI.ID_INTEGRANTE \n"+
			   " AND GI.FECHA_BAJA_LOGICA IS NULL \n";
			  
		ejecutaSql();
		return getConsultaLista();
	}
	

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
	
		if (parametros.getDefCampo("EXISTE_ASESOR").equals("PREGUNTA")){
			sSql =  " SELECT \n"+
				" COUNT (*) NUM_ASESOR \n"+
				" FROM SIM_GRUPO_INTEGRANTE GI, \n"+
				"      RS_GRAL_PERSONA P \n"+
				" WHERE  GI.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND GI.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND GI.ID_GRUPO = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
				" AND P.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n"+
				" AND P.CVE_EMPRESA = GI.CVE_EMPRESA \n"+
				" AND P.ID_PERSONA = GI.ID_INTEGRANTE \n"+
				" AND GI.FECHA_BAJA_LOGICA IS NULL \n";
				
		}else{
			//Consulta el asesor de crédito del grupo.
			sSql = " SELECT DISTINCT \n"+
				" DECODE(P.CVE_ASESOR_CREDITO,null,'ninguno',P.CVE_ASESOR_CREDITO) CVE_ASESOR_CREDITO \n"+
				" FROM SIM_GRUPO_INTEGRANTE GI, \n"+
				"      RS_GRAL_PERSONA P \n"+
				" WHERE  GI.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND GI.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND GI.ID_GRUPO = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
				" AND P.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n"+
				" AND P.CVE_EMPRESA = GI.CVE_EMPRESA \n"+
				" AND P.ID_PERSONA = GI.ID_INTEGRANTE \n"+
				" AND GI.FECHA_BAJA_LOGICA IS NULL \n";
				
		}
	
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}