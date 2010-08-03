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

public class SimCajaPagoIndividualREP implements ReporteControlIN {

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
						"T.ID_CLIENTE, \n"+
						"T.NUM_CICLO, \n"+ 
						"T.ID_PRESTAMO, \n"+
						"CANTIDADES_LETRAS(" + request.getParameter("Importe")+") PAGO_LETRAS, \n"+
						"B.NOM_COMPLETO, \n"+
						"NVL(P.FECHA_ENTREGA,P.FECHA_REAL) FECHA, \n"+
						"T.ID_SUCURSAL, \n"+
						"A.NOM_SUCURSAL, \n"+
						"T.ID_CAJA, \n"+
						"TA.NUM_PAGO_AMORTIZACION, \n"+
						"D.CALLE||' '||D.NUMERO_INT||' '||'COL.'||' '||D.NOM_ASENTAMIENTO||' '||'C.P.'||' '||D.CODIGO_POSTAL||','||' '||D.NOM_DELEGACION||','||' '||D.NOM_ESTADO DOMICILIO \n"+
						"FROM SIM_CAJA_TRANSACCION T, \n"+
						"SIM_PRESTAMO P, \n"+
						"SIM_SUCURSAL_CAJA S, \n"+
						"RS_GRAL_PERSONA B, \n"+
						"SIM_CAT_SUCURSAL A, \n"+
						"RS_GRAL_DOMICILIO D, \n"+
						"SIM_TABLA_AMORTIZACION TA \n"+
						"WHERE T.CVE_GPO_EMPRESA = 'SIM' \n"+
						"AND T.CVE_EMPRESA = 'CREDICONFIA' \n"+
						"AND T.ID_TRANSACCION = '" + request.getParameter("IdTransaccion")+"' \n"+
						"AND S.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
						"AND S.CVE_EMPRESA = T.CVE_EMPRESA \n"+
						"AND S.ID_SUCURSAL = T.ID_SUCURSAL \n"+
						"AND S.ID_SUCURSAL||'-'||S.ID_CAJA = '" + request.getParameter("IdCaja")+"' \n"+
						"AND P.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND P.ID_PRESTAMO (+)= T.ID_PRESTAMO \n"+
						"AND B.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
						"AND B.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND B.ID_PERSONA (+)= T.ID_CLIENTE \n"+
						"AND A.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
						"AND A.CVE_EMPRESA = T.CVE_EMPRESA \n"+
						"AND A.ID_SUCURSAL = T.ID_SUCURSAL \n"+
						"AND D.CVE_GPO_EMPRESA (+)= A.CVE_GPO_EMPRESA \n"+
						"AND D.CVE_EMPRESA (+)= A.CVE_EMPRESA \n"+
						"AND D.ID_DOMICILIO (+)= A.ID_DIRECCION \n"+
						"AND TA.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
						"AND TA.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						"AND TA.ID_PRESTAMO = P.ID_PRESTAMO \n"+
						"AND TA.NUM_PAGO_AMORTIZACION = (SELECT \n"+
														"MAX(NUM_PAGO_AMORTIZACION) \n"+
														"FROM  \n"+
														"SIM_TABLA_AMORTIZACION \n"+
														"WHERE ID_PRESTAMO = '" + request.getParameter("IdPrestamo")+"' \n"+
														"AND FECHA_AMORTIZACION <= (SELECT  F_MEDIO \n"+ 
																					" FROM    PFIN_PARAMETRO \n"+
																			 		" WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
																			 		" AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
																			 		" AND CVE_MEDIO       = 'SYSTEM')) \n";
		
		
		
		String sReimpresion = request.getParameter("Reimpresion");
		
		parametros.put("Pago", request.getParameter("Importe"));
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		parametros.put("FechaReporte", Fecha2.formatoTiempo24(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/caja/SimCajaPagoIndividual.jasper");
		parametros.put("Reimpresion", sReimpresion);
		return parametros;		
	}
}