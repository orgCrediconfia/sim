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

public class SimCajaRecepcionCentralREP implements ReporteControlIN {

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
						"T.MONTO, \n"+
						"T.CVE_USUARIO_CAJERO, \n"+ 
						"T.USUARIO_ENTREGA,  \n"+
						"T.FECHA_TRANSACCION FECHA, \n"+ 
						"PC.NOM_COMPLETO NOM_USUARIO_CAJERO, \n"+
						"CANTIDADES_LETRAS(MONTO) MONTO_LETRAS, \n"+
						"T.ID_SUCURSAL, \n"+
						"A.NOM_SUCURSAL, \n"+
						"T.ID_CAJA \n"+
						"FROM SIM_CAJA_TRANSACCION T, \n"+
						"SIM_SUCURSAL_CAJA S, \n"+
						"RS_GRAL_USUARIO UC, \n"+
			            "RS_GRAL_PERSONA PC,  \n"+
						"SIM_CAT_SUCURSAL A \n"+
						"WHERE T.CVE_GPO_EMPRESA = 'SIM' \n"+
						"AND T.CVE_EMPRESA = 'CREDICONFIA' \n"+
						"AND T.ID_TRANSACCION = '" + request.getParameter("IdTransaccion")+"' \n"+ 
						"AND T.CVE_MOVIMIENTO_CAJA = 'RECCAJCEN' \n"+
						"AND S.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+ 
						"AND S.CVE_EMPRESA = T.CVE_EMPRESA \n"+
						"AND S.ID_SUCURSAL = T.ID_SUCURSAL \n"+
						"AND S.ID_SUCURSAL||'-'||S.ID_CAJA = '" + request.getParameter("IdCaja")+"' \n"+
						"AND UC.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+ 
						"AND UC.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND UC.CVE_USUARIO (+)= T.CVE_USUARIO_CAJERO \n"+ 
			            "AND PC.CVE_GPO_EMPRESA (+)= UC.CVE_GPO_EMPRESA \n"+
						"AND PC.CVE_EMPRESA (+)= UC.CVE_EMPRESA \n"+
						"AND PC.ID_PERSONA (+)= UC.ID_PERSONA \n"+
						"AND A.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+ 
						"AND A.CVE_EMPRESA = T.CVE_EMPRESA \n"+
						"AND A.ID_SUCURSAL = T.ID_SUCURSAL \n";
					
		String sReimpresion = request.getParameter("Reimpresion");
		              
		parametros.put("Sql", sSql);
		
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
	
		parametros.put("FechaReporte", Fecha2.formatoTiempo24(new Date()));
		
		parametros.put("NomReporte", "/Reportes/Sim/caja/SimCajaRecepcionCentral.jasper");
	
		parametros.put("Reimpresion", sReimpresion);
		return parametros;		
	}
}