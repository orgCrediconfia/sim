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
						"C.NOMBRE NOM_COMPLETO, \n"+
						"TO_CHAR(TO_DATE(NVL(C.FECHA_ENTREGA,C.FECHA_REAL)),'DD \"de\" MONTH \"de\" YYYY') FECHA, \n"+
						"T.ID_SUCURSAL, \n"+
						"C.NOM_SUCURSAL, \n"+
						"T.ID_CAJA, \n"+
						"C.DIRECCION_SUCURSAL DOMICILIO \n"+
						"FROM SIM_CAJA_TRANSACCION T, \n"+
						"SIM_SUCURSAL_CAJA S, \n"+
						"V_CREDITO C \n"+
						"WHERE T.CVE_GPO_EMPRESA = 'SIM' \n"+
						"AND T.CVE_EMPRESA = 'CREDICONFIA' \n"+
						"AND T.ID_TRANSACCION = '" + request.getParameter("IdTransaccion")+"' \n"+
						"AND S.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+ 
						"AND S.CVE_EMPRESA = T.CVE_EMPRESA \n"+
						"AND S.ID_SUCURSAL = T.ID_SUCURSAL \n"+
						"AND S.ID_SUCURSAL||'-'||S.ID_CAJA = '" + request.getParameter("IdCaja")+"' \n"+
						"AND C.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
						"AND C.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND C.ID_PRESTAMO (+)= T.ID_PRESTAMO \n"+
						"AND C.APLICA_A = 'INDIVIDUAL' \n";
		
		System.out.println("recibo individual"+sSql);
		
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