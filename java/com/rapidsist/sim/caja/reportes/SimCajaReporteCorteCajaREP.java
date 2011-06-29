/**
 * Sistema Central de Control.
 *
 */

package com.rapidsist.sim.caja.reportes;

import com.rapidsist.portal.cliente.reportes.ReporteControlIN;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import com.rapidsist.comun.util.Fecha2;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import com.rapidsist.portal.catalogos.CatalogoSL;
import javax.naming.Context;
import com.rapidsist.comun.bd.Registro;
import javax.servlet.http.HttpSession;
import com.rapidsist.portal.configuracion.Usuario;

/**
 * Esta clase se encarga de administrar la operación consulta del Reporte Anexo A
 * Esta clase es llamada por el servlet ProcesaReporteS.
 */

public class SimCajaReporteCorteCajaREP implements ReporteControlIN {

	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();

		parametrosCatalogo.addDefCampo("ID_REGIONAL",request.getParameter("IdRegional"));
		parametrosCatalogo.addDefCampo("ID_SUCURSAL",request.getParameter("IdSucursal"));
		parametrosCatalogo.addDefCampo("FECHA_INICIAL",request.getParameter("FechaInicial"));
		parametrosCatalogo.addDefCampo("FECHA_FINAL",request.getParameter("FechaFinal"));
		
		Registro SaldoInicial = new Registro();
		parametrosCatalogo.addDefCampo("SALDO","INICIAL");
		SaldoInicial = catalogoSL.getRegistro("SimCajaCorte", parametrosCatalogo);
		String sSaldoInicial = (String)SaldoInicial.getDefCampo("SALDO_INICIAL");
		
		Registro SaldoFinal = new Registro();
		parametrosCatalogo.addDefCampo("SALDO","FINAL");
		SaldoFinal = catalogoSL.getRegistro("SimCajaCorte", parametrosCatalogo);
		String sSaldoFinal = (String)SaldoFinal.getDefCampo("SALDO_FINAL");
	
		String sSql =  "SELECT \n"+
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
						"					WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+
						"					AND C.CVE_EMPRESA = 'CREDICONFIA' \n"+
						"					AND S.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+
						"					AND S.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
						"					AND S.ID_SUCURSAL (+)= C.ID_SUCURSAL \n"+
						"					AND C.FECHA_TRANSACCION >= TO_DATE('" + request.getParameter("FechaInicial") + "','DD/MM/YYYY') \n";
				
						if (!request.getParameter("IdRegional").equals("null")) {
							sSql = sSql + " AND S.ID_REGIONAL = '" + request.getParameter("IdRegional") + "' \n";
						}
						
						if (!request.getParameter("IdSucursal").equals("null")) {
							sSql = sSql + " AND S.ID_SUCURSAL = '" + request.getParameter("IdSucursal") + "' \n";
						}
						
						sSql = sSql + "GROUP BY C.CVE_GPO_EMPRESA, C.CVE_EMPRESA \n"+

										")A \n"+
										"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+
										"AND C.CVE_EMPRESA = 'CREDICONFIA' \n"+
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
										"AND C.FECHA_TRANSACCION >= TO_DATE('" + request.getParameter("FechaInicial") + "','DD/MM/YYYY') \n"+
										"AND TO_DATE('" + request.getParameter("FechaFinal") + "','DD/MM/YYYY')+1 > C.FECHA_TRANSACCION \n";
		
						if (!request.getParameter("IdRegional").equals("null")) {
							sSql = sSql + " AND S.ID_REGIONAL = '" + request.getParameter("IdRegional") + "' \n";
						}
						
						if (!request.getParameter("IdSucursal").equals("null")) {
							sSql = sSql + " AND S.ID_SUCURSAL = '" + request.getParameter("IdSucursal") + "' \n";
						}
						
						sSql = sSql + "ORDER BY FECHA \n";
		
		parametros.put("Sql", sSql);
		parametros.put("FechaInicial", request.getParameter("FechaInicial"));
		parametros.put("FechaFinal", request.getParameter("FechaFinal"));
		parametros.put("SaldoInicial", sSaldoInicial);
		parametros.put("SaldoFinal", sSaldoFinal);
		parametros.put("FechaReporte", Fecha2.formatoTiempo24(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/caja/SimCajaReporteCorteCaja.jasper");
		
		return parametros;		
	}
}
