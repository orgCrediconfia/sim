/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para hacer el corte de Caja.
 */
 
public class SimCajaCorteDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro {
	
	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		sSql =  "SELECT \n"+
				"C.CVE_GPO_EMPRESA, \n"+
				"C.CVE_EMPRESA, \n"+
				"S.ID_REGIONAL, \n"+
				"R.NOM_REGIONAL, \n"+
				"C.ID_SUCURSAL, \n"+ 
				"S.NOM_SUCURSAL, \n"+
				"C.ID_CAJA, \n"+
				"C.FECHA_TRANSACCION FECHA, \n"+
				"C.CVE_MOVIMIENTO_CAJA, \n"+ 
				"M.NOM_MOVIMIENTO_CAJA, \n"+
				"C.ID_GRUPO, \n"+
				"G.NOM_GRUPO, \n"+
				"DECODE(C.ID_GRUPO,NULL,' ',C.ID_GRUPO||' - '||G.NOM_GRUPO) GRUPO, \n"+
				"C.ID_CLIENTE, \n"+
				"P.NOM_COMPLETO, \n"+
				"DECODE(C.ID_CLIENTE,NULL,' ',C.ID_CLIENTE||' - '||P.NOM_COMPLETO) CLIENTE, \n"+
				"C.NUM_CICLO, \n"+
				"C.ID_TRANSACCION, \n"+
				"TO_CHAR(C.MONTO,'999,999,999.99') MONTO, \n"+
				"TO_CHAR(sum(C.MONTO) over (order by C.FECHA_TRANSACCION ROWS UNBOUNDED PRECEDING) + A.SALDO_INICIAL,'999,999,999.99') MONTO_ACUMULADO \n"+
				"FROM SIM_CAJA_TRANSACCION C, \n"+
				"SIM_CAT_MOVIMIENTO_CAJA M, \n"+
				"SIM_GRUPO G, \n"+
				"RS_GRAL_PERSONA P, \n"+
				"SIM_CAT_SUCURSAL S, \n"+
				"SIM_CAT_REGIONAL R, \n"+
				"(SELECT \n"+
				"          C.CVE_GPO_EMPRESA, \n"+
				"          C.CVE_EMPRESA, \n"+
				"          NVL(SUM(C.MONTO),0) SALDO_INICIAL \n"+ 
				"					FROM SIM_CAJA_TRANSACCION C, \n"+
				"					SIM_CAT_SUCURSAL S \n"+
				"					WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"					AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"					AND S.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+
				"					AND S.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
				"					AND S.ID_SUCURSAL (+)= C.ID_SUCURSAL \n"+
				"					AND C.FECHA_TRANSACCION >= TO_DATE('" + (String)parametros.getDefCampo("FECHA_INICIAL") + "','DD/MM/YYYY') \n";
		
		if (!parametros.getDefCampo("ID_REGIONAL").equals("null")) {
			sSql = sSql + " AND S.ID_REGIONAL = '" + (String) parametros.getDefCampo("ID_REGIONAL") + "' \n";
		}
		
		if (!parametros.getDefCampo("ID_SUCURSAL").equals("null")) {
			sSql = sSql + " AND S.ID_SUCURSAL = '" + (String) parametros.getDefCampo("ID_SUCURSAL") + "' \n";
		}

		sSql = sSql + "GROUP BY C.CVE_GPO_EMPRESA, C.CVE_EMPRESA \n"+
		
						")A \n"+
						"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND M.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
						"AND M.CVE_EMPRESA = C.CVE_EMPRESA \n"+
						"AND M.CVE_MOVIMIENTO_CAJA = C.CVE_MOVIMIENTO_CAJA \n"+
						"And G.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+
						"AND G.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
						"AND G.ID_GRUPO (+)= C.ID_GRUPO \n"+
						"AND P.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
						"AND P.ID_PERSONA (+)= C.ID_CLIENTE \n"+
						"AND S.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+
						"AND S.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
						"AND S.ID_SUCURSAL (+)= C.ID_SUCURSAL \n"+
						"AND R.CVE_GPO_EMPRESA (+)= S.CVE_GPO_EMPRESA \n"+ 
						"AND R.CVE_EMPRESA (+)= S.CVE_EMPRESA \n"+
						"AND R.ID_REGIONAL (+)= S.ID_REGIONAL \n"+
						"AND A.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+
						"AND A.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
						"AND C.FECHA_TRANSACCION >= TO_DATE('" + (String)parametros.getDefCampo("FECHA_INICIAL") + "','DD/MM/YYYY') \n"+
						"AND TO_DATE('" + (String)parametros.getDefCampo("FECHA_FINAL") + "','DD/MM/YYYY')+1 > C.FECHA_TRANSACCION \n";
						
		if (!parametros.getDefCampo("ID_REGIONAL").equals("null")) {
			sSql = sSql + " AND S.ID_REGIONAL = '" + (String) parametros.getDefCampo("ID_REGIONAL") + "' \n";
		}
		
		if (!parametros.getDefCampo("ID_SUCURSAL").equals("null")) {
			sSql = sSql + " AND S.ID_SUCURSAL = '" + (String) parametros.getDefCampo("ID_SUCURSAL") + "' \n";
		}

		sSql = sSql + "ORDER BY FECHA \n";
			
		System.out.println("movimientos"+sSql);
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
		
		if (parametros.getDefCampo("SALDO").equals("INICIAL")){
			sSql =  "SELECT \n"+
					"TO_CHAR(SALDO_INICIAL,'999,999,999.99') SALDO_INICIAL \n"+
					"FROM ( \n"+
					"SELECT \n"+
					"NVL(SUM(C.MONTO),0.00) SALDO_INICIAL \n"+
					"FROM SIM_CAJA_TRANSACCION C, \n"+
					"SIM_CAT_SUCURSAL S \n"+
					"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND S.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+
					"AND S.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
					"AND S.ID_SUCURSAL (+)= C.ID_SUCURSAL \n"+
					"AND C.FECHA_TRANSACCION >= TO_DATE('" + (String)parametros.getDefCampo("FECHA_INICIAL") + "','DD/MM/YYYY') \n";
					
			if (!parametros.getDefCampo("ID_REGIONAL").equals("null")) {
				sSql = sSql + " AND S.ID_REGIONAL = '" + (String) parametros.getDefCampo("ID_REGIONAL") + "' \n";
			}
			
			if (!parametros.getDefCampo("ID_SUCURSAL").equals("null")) {
				sSql = sSql + " AND S.ID_SUCURSAL = '" + (String) parametros.getDefCampo("ID_SUCURSAL") + "' \n";
			}

			sSql = sSql + " ) \n";
			
		} else if (parametros.getDefCampo("SALDO").equals("FINAL")){
			sSql =  "SELECT TO_CHAR(SALDO_INICIAL + SALDO_FINAL,'999,999,999.99') AS SALDO_FINAL \n"+
					"FROM ( \n"+
				 	"SELECT \n"+
					" SALDO_INICIAL \n"+
					"FROM ( \n"+
					"SELECT \n"+
					"NVL(SUM(C.MONTO),0.00) SALDO_INICIAL \n"+
					"FROM SIM_CAJA_TRANSACCION C, \n"+
					"SIM_CAT_SUCURSAL S \n"+
					"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND S.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+
					"AND S.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
					"AND S.ID_SUCURSAL (+)= C.ID_SUCURSAL \n"+
					"AND C.FECHA_TRANSACCION >= TO_DATE('" + (String)parametros.getDefCampo("FECHA_INICIAL") + "','DD/MM/YYYY') \n";
					
					if (!parametros.getDefCampo("ID_REGIONAL").equals("null")) {
						sSql = sSql + " AND S.ID_REGIONAL = '" + (String) parametros.getDefCampo("ID_REGIONAL") + "' \n";
					}
					
					if (!parametros.getDefCampo("ID_SUCURSAL").equals("null")) {
						sSql = sSql + " AND S.ID_SUCURSAL = '" + (String) parametros.getDefCampo("ID_SUCURSAL") + "' \n";
					}

					sSql = sSql + " ))A, \n"+
					" ( \n"+
					"SELECT \n"+
					"SALDO_FINAL \n"+
					"FROM ( \n"+
					"SELECT \n"+
					"NVL(SUM(C.MONTO),0.00) SALDO_FINAL \n"+
					"FROM SIM_CAJA_TRANSACCION C, \n"+
					"SIM_CAT_SUCURSAL S \n"+
					"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND S.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+
					"AND S.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
					"AND S.ID_SUCURSAL (+)= C.ID_SUCURSAL \n"+
					"AND C.FECHA_TRANSACCION >= TO_DATE('" + (String)parametros.getDefCampo("FECHA_INICIAL") + "','DD/MM/YYYY') \n"+
					"AND TO_DATE('" + (String)parametros.getDefCampo("FECHA_FINAL") + "','DD/MM/YYYY')+1 > C.FECHA_TRANSACCION \n";
					
					if (!parametros.getDefCampo("ID_REGIONAL").equals("null")) {
						sSql = sSql + " AND S.ID_REGIONAL = '" + (String) parametros.getDefCampo("ID_REGIONAL") + "' \n";
					}
					
					if (!parametros.getDefCampo("ID_SUCURSAL").equals("null")) {
						sSql = sSql + " AND S.ID_SUCURSAL = '" + (String) parametros.getDefCampo("ID_SUCURSAL") + "' \n";
					}

					sSql = sSql + " ))B \n";
			
		}
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}