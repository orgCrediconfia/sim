/**
 * Sistema Central de Control.
 *
 */

package com.rapidsist.sim.prestamo.reportes;

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
 * Esta clase se encarga de administrar la operación consulta del Reporte de líneas de fondeo.
 * Esta clase es llamada por el servlet ProcesaReporteS.
 */

public class SimPrestamoReporteLineaFondeoREP implements ReporteControlIN {

	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();
		
		//OBTIENE LOS DATOS DEL USUARIO
		HttpSession session = request.getSession(true);
		Usuario usuario = (Usuario)session.getAttribute("Usuario");
		String sCveUsuario = usuario.sCveUsuario;

		String sSql =   "SELECT \n"+
						"C.CVE_PRESTAMO, \n"+
						"C.NOMBRE, \n"+
						"C.NUM_LINEA, \n"+
						"C.MONTO_AUTORIZADO + C.CARGO_INICIAL MONTO_PRESTADO \n"+
						" FROM V_CREDITO C, \n"+
						"SIM_USUARIO_ACCESO_SUCURSAL US \n"+
						"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+
						"AND C.CVE_EMPRESA = 'CREDICONFIA' \n"+
						"AND C.APLICA_A != 'INDIVIDUAL_GRUPO' \n"+
						"AND US.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
				        "AND US.CVE_EMPRESA = C.CVE_EMPRESA \n"+
				        "AND US.ID_SUCURSAL = C.ID_SUCURSAL \n"+
				        "AND US.CVE_USUARIO = '" + sCveUsuario + "' \n";
		
		if (request.getParameter("IdLinea") != null && !request.getParameter("IdLinea").equals("") && !request.getParameter("IdLinea").equals("null") ){
			sSql = sSql + " AND C.NUM_LINEA = '" + request.getParameter("IdLinea")+"'  \n";
		}	
		
		sSql = sSql + " ORDER BY C.NUM_LINEA, C.NOMBRE \n";
		
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/prestamo/SimPrestamoReporteLineaFondeo.jasper");
		return parametros;		
	}
}