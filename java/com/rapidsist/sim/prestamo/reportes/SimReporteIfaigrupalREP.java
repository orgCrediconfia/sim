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

/**
 * Esta clase se encarga de administrar la operación consulta del Reporte Anexo
 * A Esta clase es llamada por el servlet ProcesaReporteS.
 */

public class SimReporteIfaigrupalREP implements ReporteControlIN {

	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet) throws Exception {
		Map parametros = new HashMap();

		String sSql = "SELECT\n" +
					"TO_CHAR(SYSDATE, 'DD') ||' de '|| RTRIM(TO_CHAR(SYSDATE, 'MONTH')) ||' del '||TO_CHAR(SYSDATE, 'YYYY') FECHA_HOY, \n"+
					"TO_CHAR(V.FECHA_ENTREGA, 'DD') ||' de '|| RTRIM(TO_CHAR(V.FECHA_ENTREGA, 'MONTH')) ||' del '||TO_CHAR(V.FECHA_ENTREGA, 'YYYY') FECHA_ENTREGA, \n"+
					"P.NOM_COMPLETO \n"+ 
					"FROM \n"+ 
					"SIM_PRESTAMO_GPO_DET G, \n"+
					"V_CREDITO V, \n"+
					"RS_GRAL_PERSONA P \n"+ 
					"WHERE G.ID_PRESTAMO_GRUPO = '" + request.getParameter("IdPrestamoGrupo") + "'\n"+
					"AND P.CVE_GPO_EMPRESA     =  G.CVE_GPO_EMPRESA \n"+
					"AND P.CVE_EMPRESA 		   =  G.CVE_EMPRESA \n"+
					"AND P.ID_PERSONA 		   =  G.ID_INTEGRANTE \n"+
					"AND V.CVE_GPO_EMPRESA     = P.CVE_GPO_EMPRESA \n"+
					"AND V.CVE_EMPRESA         = P.CVE_EMPRESA \n"+
					"AND V.ID_PRESTAMO         = G.ID_PRESTAMO_GRUPO \n";

		System.out.println("sSql" + sSql);

		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte","/Reportes/Sim/prestamo/ConsentimientoIFAI.jasper");
		return parametros;

	}

}
