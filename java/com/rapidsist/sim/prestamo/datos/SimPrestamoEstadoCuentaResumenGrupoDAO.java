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
 * Administra los accesos a la base de datos para la consulta del resumen del estado de cuenta de los créditos.
 */
 
public class SimPrestamoEstadoCuentaResumenGrupoDAO extends Conexion2 implements OperacionConsultaTabla  {
	

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		String sConsulta = "";
		String sNumIntegrantes = "";
		int iNumIntegrantes = 0;
	
		if (parametros.getDefCampo("CONSULTA").equals("RESUMEN")){
			sSql =	 "SELECT \n"+	
			"A.cve_concepto, \n"+
			"sum(imp_neto) SALDO, \n"+ 
			"sum(imp_original) + sum(imp_extraordinario) IMPORTE, \n"+ 
			"sum(imp_pagado) PAGADO, \n"+
			"'Total De ' || INITCAP(B.DESC_CORTA) DESCRIPCION \n"+
			"FROM \n"+
			"SIM_PRESTAMO_GPO_DET G, \n"+
			"V_SIM_TABLA_AMORT_CONCEPTO A, \n"+
			"PFIN_CAT_CONCEPTO B \n"+
			"WHERE G.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND G.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND G.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
			"AND A.CVE_GPO_EMPRESA   = G.CVE_GPO_EMPRESA \n"+
			"AND A.CVE_EMPRESA       = G.CVE_EMPRESA \n"+
			"AND A.ID_PRESTAMO       = G.ID_PRESTAMO \n"+
			"AND B.CVE_GPO_EMPRESA   = A.CVE_GPO_EMPRESA \n"+
			"AND B.CVE_EMPRESA       = A.CVE_EMPRESA \n"+
			"AND B.CVE_CONCEPTO      = A.CVE_CONCEPTO \n"+
			"AND A.IMP_ORIGINAL     <> 0 \n"+
			"GROUP BY A.cve_concepto, 'Total De ' || INITCAP(B.DESC_CORTA) \n"+
			"Order by cve_concepto \n";
			
		}else if (parametros.getDefCampo("CONSULTA").equals("SALDO_TOTAL")){
			
			sSql =	"SELECT \n"+	
					"  TO_CHAR(SUM(SALDO),'999,999,999.99') SALDO_TOTAL, \n"+
					"  TO_CHAR(SUM(IMPORTE),'999,999,999.99') IMPORTE_TOTAL, \n"+
					"  TO_CHAR(SUM(PAGADO),'999,999,999.99') PAGO_TOTAL \n"+
					"FROM ( \n"+
					"  SELECT  \n"+
					"    sum(imp_neto) SALDO, \n"+
					"    sum(imp_original) + sum(imp_extraordinario) IMPORTE, \n"+ 
					"    sum(imp_pagado) PAGADO \n"+
					"  FROM  \n"+
					"  SIM_PRESTAMO_GPO_DET G, \n"+
					"  V_SIM_TABLA_AMORT_CONCEPTO A, \n"+
					"  PFIN_CAT_CONCEPTO B \n"+
					  "WHERE G.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					 "AND G.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					 "AND G.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					 "AND A.CVE_GPO_EMPRESA   = G.CVE_GPO_EMPRESA \n"+
					 "AND A.CVE_EMPRESA       = G.CVE_EMPRESA \n"+
					 "AND A.ID_PRESTAMO       = G.ID_PRESTAMO \n"+
					 "AND B.CVE_GPO_EMPRESA   = A.CVE_GPO_EMPRESA \n"+
					 "AND B.CVE_EMPRESA       = A.CVE_EMPRESA \n"+
					 "AND B.CVE_CONCEPTO      = A.CVE_CONCEPTO \n"+
					 "AND A.IMP_ORIGINAL     <> 0 \n"+
					") \n";
			
		}
		   
		ejecutaSql();
		return getConsultaLista();
	}
}