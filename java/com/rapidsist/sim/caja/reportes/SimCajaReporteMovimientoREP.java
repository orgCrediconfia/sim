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

public class SimCajaReporteMovimientoREP implements ReporteControlIN {

	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();

		//OBTIENE LOS DATOS DEL USUARIO
		HttpSession session = request.getSession(true);
		Usuario usuario = (Usuario)session.getAttribute("Usuario");
		String sCveUsuario = usuario.sNomCompleto;
		parametros.put("CveUsuario", sCveUsuario);
		
		String sSql =   "SELECT \n"+
						"T.CVE_GPO_EMPRESA, \n"+
						"T.CVE_EMPRESA, \n"+
						"T.ID_TRANSACCION, \n"+
						"T.CVE_MOVIMIENTO_CAJA, \n"+
						"M.NOM_MOVIMIENTO_CAJA, \n"+
						"R.ID_REGIONAL, \n"+
						"R.NOM_REGIONAL, \n"+
						"T.ID_SUCURSAL, \n"+
						"S.NOM_SUCURSAL, \n"+
						"T.ID_CAJA, \n"+
						"T.ID_PRESTAMO, \n"+
						"T.ID_PRESTAMO_GRUPO, \n"+
						"T.ID_PRODUCTO, \n"+
						"O.NOM_PRODUCTO, \n"+
						"T.NUM_CICLO, \n"+
						"T.ID_GRUPO, \n"+
						"G.NOM_GRUPO, \n"+
						"T.ID_CLIENTE, \n"+
						"P.NOM_COMPLETO, \n"+
						"T.MONTO, \n"+
						"T.FECHA_CANCELACION, \n"+
						"T.CVE_USUARIO_CAJERO, \n"+
						"T.FECHA_APLICACION, \n"+
						"T.FECHA_REGISTRO \n"+  
						"FROM \n"+
						"SIM_CAJA_TRANSACCION T, \n"+
						"RS_GRAL_PERSONA P, \n"+
						"SIM_GRUPO G, \n"+ 
						"SIM_PRODUCTO O, \n"+
						"SIM_CAT_MOVIMIENTO_CAJA M, \n"+
						"SIM_REGIONAL_SUCURSAL RS, \n"+
						"SIM_CAT_REGIONAL R, \n"+
						"SIM_CAT_SUCURSAL S \n"+
						"WHERE T.CVE_GPO_EMPRESA = 'SIM' \n"+
						"AND T.CVE_EMPRESA = 'CREDICONFIA' \n"+
						"AND P.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND P.ID_PERSONA (+)= T.ID_CLIENTE \n"+
						"AND G.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
						"AND G.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND G.ID_GRUPO (+)= T.ID_GRUPO \n"+
						"AND O.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
						"AND O.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND O.ID_PRODUCTO (+)= T.ID_PRODUCTO \n"+
						"AND M.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
						"AND M.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND M.CVE_MOVIMIENTO_CAJA (+)= T.CVE_MOVIMIENTO_CAJA \n"+
						"AND RS.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
						"AND RS.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND RS.ID_SUCURSAL (+)= T.ID_SUCURSAL \n"+
						"AND R.CVE_GPO_EMPRESA (+)= RS.CVE_GPO_EMPRESA \n"+
						"AND R.CVE_EMPRESA (+)= RS.CVE_EMPRESA \n"+
						"AND R.ID_REGIONAL (+)= RS.ID_REGIONAL \n"+
						"AND S.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
						"AND S.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND S.ID_SUCURSAL (+)= T.ID_SUCURSAL \n";
		
		if (!request.getParameter("IdRegional").equals("null")){
			sSql = sSql + "AND R.ID_REGIONAL = '" + request.getParameter("IdRegional") + "'\n";
			parametros.put("IdRegional", request.getParameter("IdRegional"));
		}
		if (!request.getParameter("IdSucursal").equals("null")){
			sSql = sSql + "AND T.ID_SUCURSAL = '" + request.getParameter("IdSucursal") + "'\n";	
			parametros.put("IdSucursal", request.getParameter("IdSucursal"));
		}
		if (!request.getParameter("IdCaja").equals("null")){
			
			
			String sIdCaja = "";
			String sIdCajaSucursal = "";
			String sIdSucursal = "";
			
			sIdCajaSucursal = request.getParameter("IdCaja");
			
			sIdSucursal = sIdCajaSucursal.substring(0, sIdCajaSucursal.indexOf("-"));
			sIdCaja = sIdCajaSucursal.substring(sIdCajaSucursal.indexOf("-")+1, sIdCajaSucursal.length());
			
			
			
			sSql = sSql + "AND T.ID_CAJA = '" + sIdCaja + "'\n";	
			parametros.put("IdCaja", sIdCaja);
		}
		if (!request.getParameter("FechaIni").equals("")){
			sSql = sSql + "AND T.FECHA_REGISTRO >= TO_DATE('" + request.getParameter("FechaIni") + "','dd/mm/yyyy') \n";
			parametros.put("FechaIni", request.getParameter("FechaIni"));
		}
		if (!request.getParameter("FechaFin").equals("")){
			sSql = sSql + "AND T.FECHA_REGISTRO < 1 + TO_DATE('" + request.getParameter("FechaFin") + "','dd/mm/yyyy') \n";
			parametros.put("FechaFin", request.getParameter("FechaFin"));
		}
		
		parametros.put("Sql", sSql);
		
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
	
		parametros.put("FechaReporte", Fecha2.formatoTiempo24(new Date()));

		parametros.put("NomReporte", "/Reportes/Sim/caja/SimCajaReporteMovimiento.jasper");
		return parametros;		
	}
}