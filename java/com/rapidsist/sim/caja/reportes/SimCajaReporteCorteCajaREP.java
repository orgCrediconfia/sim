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

		//OBTIENE LOS DATOS DEL USUARIO
		HttpSession session = request.getSession(true);
		Usuario usuario = (Usuario)session.getAttribute("Usuario");
		String sCveUsuario = usuario.sNomCompleto;
		parametros.put("CveUsuario", sCveUsuario);
		
		String sIdCaja = "";
		String sIdCajaSucursal = "";
		String sIdSucursal = "";
		
		sIdCajaSucursal = request.getParameter("IdCaja");
		
		sIdSucursal = sIdCajaSucursal.substring(0, sIdCajaSucursal.indexOf("-"));
		sIdCaja = sIdCajaSucursal.substring(sIdCajaSucursal.indexOf("-")+1, sIdCajaSucursal.length());
		
		String sSql =  "SELECT \n"+
		"C.CVE_GPO_EMPRESA, \n"+
		"C.CVE_EMPRESA, \n"+
		"C.CVE_MOVIMIENTO_CAJA, \n"+
		"M.NOM_MOVIMIENTO_CAJA, \n"+
		"C.ID_GRUPO, \n"+
		"G.NOM_GRUPO, \n"+
		"DECODE(C.ID_GRUPO,NULL,' ',C.ID_GRUPO||' - '||G.NOM_GRUPO) GRUPO, \n"+
		"C.ID_CLIENTE, \n"+
		"P.NOM_COMPLETO, \n"+
		"DECODE(C.ID_CLIENTE,NULL,' ',C.ID_CLIENTE||' - '||P.NOM_COMPLETO) CLIENTE, \n"+
		"C.NUM_CICLO, \n"+
		"C.FECHA_TRANSACCION FECHA, \n"+
		"TO_CHAR(C.MONTO,'999,999,999.99') MONTO \n"+
		"FROM SIM_CAJA_TRANSACCION C, \n"+
		"SIM_CAT_MOVIMIENTO_CAJA M, \n"+
		"SIM_GRUPO G, \n"+
		"RS_GRAL_PERSONA P \n"+
		"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+
		"AND C.CVE_EMPRESA = 'CREDICONFIA' \n"+
		"AND C.ID_SUCURSAL = '" + sIdSucursal + "' \n"+
		"AND C.ID_CAJA = '" + sIdCaja + "' \n"+
		"AND M.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
		"AND M.CVE_EMPRESA = C.CVE_EMPRESA \n"+
		"AND M.CVE_MOVIMIENTO_CAJA = C.CVE_MOVIMIENTO_CAJA \n"+
		"And G.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+
		"AND G.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
		"AND G.ID_GRUPO (+)= C.ID_GRUPO \n"+
		"AND P.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+
		"AND P.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
		"AND P.ID_PERSONA (+)= C.ID_CLIENTE \n"+
		"AND C.FECHA_TRANSACCION >= TO_DATE('" + request.getParameter("FechaInicial") + "','DD/MM/YYYY') \n"+
		"AND TO_DATE('" + request.getParameter("FechaFinal") + "','DD/MM/YYYY')+1 > C.FECHA_TRANSACCION \n"+
		"ORDER BY FECHA \n";
		
		parametros.put("Sql", sSql);
		
		parametros.put("IdSucursal", sIdSucursal);
		parametros.put("IdCaja", sIdCaja);
		parametros.put("FechaInicial", request.getParameter("FechaInicial"));
		parametros.put("FechaFinal", request.getParameter("FechaFinal"));
		parametros.put("FechaReporte", Fecha2.formatoTiempo24(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/caja/SimCajaReporteCorteCaja.jasper");
		parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/caja/SimCajaReporteCorteCajaInicial.jasper"));
		parametros.put("Subreporte2", contextoServlet.getRealPath("/Reportes/Sim/caja/SimCajaReporteCorteCajaFinal.jasper"));
		
		return parametros;		
	}
}
