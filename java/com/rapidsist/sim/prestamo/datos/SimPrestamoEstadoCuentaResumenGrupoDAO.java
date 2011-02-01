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
			  "CVE_GPO_EMPRESA, \n"+	
			  "CVE_EMPRESA, \n"+	
			  "ID_PRESTAMO, \n"+	
			  "DESC_MOVIMIENTO DESCRIPCION, \n"+	
			  "TO_CHAR(IMP_DEBE_TOTAL,'999,999,999.99') IMPORTE, \n"+
			  "TO_CHAR(IMP_PAGO_TOTAL,'999,999,999.99') PAGADO, \n"+
			  "TO_CHAR(IMP_SALDO_TOTAL,'999,999,999.99') SALDO \n"+
			  "FROM \n"+	
			  "V_SIM_PRESTAMO_GPO_RES_EDO_CTA \n"+	
			  "WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			  "AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			  "AND ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
		    
		}else if (parametros.getDefCampo("CONSULTA").equals("SALDO_TOTAL")){
			
			sSql =	 "SELECT \n"+	 
			"CVE_GPO_EMPRESA, \n"+	
			"CVE_EMPRESA, \n"+	
			"ID_PRESTAMO, \n"+	
			"TO_CHAR(SUM(IMP_DEBE_TOTAL),'999,999,999.99') IMPORTE_TOTAL, \n"+
			"TO_CHAR(SUM(IMP_PAGO_TOTAL),'999,999,999.99') PAGO_TOTAL, \n"+
			"TO_CHAR(SUM(IMP_SALDO_TOTAL),'999,999,999.99') SALDO_TOTAL \n"+
			"FROM \n"+	
			"V_SIM_PRESTAMO_GPO_RES_EDO_CTA \n"+	
			"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
			"GROUP BY CVE_GPO_EMPRESA, CVE_EMPRESA, ID_PRESTAMO \n";
			
		}
		   
		ejecutaSql();
		return getConsultaLista();
	}
}