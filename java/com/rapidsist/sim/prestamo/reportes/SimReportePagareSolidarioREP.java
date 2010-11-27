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
 * Esta clase se encarga de administrar la operación consulta del Reporte Anexo A
 * Esta clase es llamada por el servlet ProcesaReporteS.
 */

public class SimReportePagareSolidarioREP implements ReporteControlIN {

	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();

		String sSql = 	"SELECT \n"+
						"C.ID_PRESTAMO ID_PRESTAMO_GRUPO, \n"+
						"C.CVE_NOMBRE ID_GRUPO, \n"+
						"C.NUM_CICLO, \n"+
						"C.VALOR_TASA, \n"+
						"TO_CHAR(TO_DATE(C.FECHA_ENTREGA),'DD \"de\" MONTH \"de\" YYYY') FECHA_ENTREGA, \n"+
						"TO_CHAR(TO_DATE(C.FECHA_FIN),'DD \"de\" MONTH \"de\" YYYY') FECHA_FIN, \n"+
						"C.PERIODICIDAD_PRODUCTO NOM_PERIODICIDAD, \n"+
						"CANTIDADES_LETRAS(MONTO_FIJO_PERIODO) MONTO_FIJO_PERIODO_LETRAS, \n"+
						"E.TX_DESC_EMPRESA, \n"+
						"C.ID_SUCURSAL, \n"+
						"C.DIRECCION_SUCURSAL, \n"+
						"C.MONTO_AUTORIZADO, \n"+
						"CANTIDADES_LETRAS(C.MONTO_AUTORIZADO) MONTO_AUTORIZADO_LETRAS \n"+
						"FROM V_CREDITO C, \n"+
						"RS_CONF_EMPRESA E \n"+
						"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+ 
						"AND C.CVE_EMPRESA = 'CREDICONFIA'  \n"+
						"AND C.ID_PRESTAMO = '" + request.getParameter("IdPrestamoGrupo")+"' \n"+
						"AND C.APLICA_A = 'GRUPO' \n"+
						"AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
						"AND E.CVE_EMPRESA = C.CVE_EMPRESA \n";
		
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/prestamo/SimReportePagareSolidario.jasper");
		parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReportePagareSolidario1.jasper"));
		parametros.put("Subreporte2", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReportePagareSolidario2.jasper"));
		parametros.put("Subreporte3", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReportePagareSolidario3.jasper"));
		return parametros;		
	}
}