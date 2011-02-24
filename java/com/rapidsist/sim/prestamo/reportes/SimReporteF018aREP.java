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
 * Esta clase se encarga de administrar la operaci�n consulta del Reporte Anexo A
 * Esta clase es llamada por el servlet ProcesaReporteS.
 */

public class SimReporteF018aREP implements ReporteControlIN {

	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();

		String sSql = 	"SELECT \n"+
						"C.ID_PRESTAMO ID_PRESTAMO_GRUPO, \n"+
						"C.CVE_NOMBRE ID_GRUPO, \n"+
						"C.NOMBRE NOM_GRUPO, \n"+
						"C.NUM_CICLO, \n"+
						//"'$'||''||TO_CHAR(C.MONTO_FIJO_PERIODO,'999,999.00') MONTO_FIJO_PERIODO, \n"+
						"MONTO_FIJO_PERIODO, \n"+
						"CANTIDADES_LETRAS(MONTO_FIJO_PERIODO) MONTO_FIJO_PERIODO_LETRAS, \n"+ 
						"TO_CHAR(TO_DATE(C.FECHA_ENTREGA),'DD \"de\" MONTH \"de\" YYYY') FECHA_INICIO, \n"+
						"C.ID_SUCURSAL, \n"+
						"C.NOM_SUCURSAL, \n"+
						"C.DIRECCION_SUCURSAL, \n"+ 
						//"'$'||''||TO_CHAR(C.MONTO_AUTORIZADO + CARGO_INICIAL,'999,999,999.00') MONTO_AUTORIZADO, \n"+
						"(MONTO_AUTORIZADO + C.CARGO_INICIAL) MONTO_AUTORIZADO, \n"+
						"CANTIDADES_LETRAS(MONTO_AUTORIZADO + CARGO_INICIAL) MONTO_AUTORIZADO_LETRAS, \n"+
						"E.TX_DESC_EMPRESA \n"+
						"FROM V_CREDITO C, \n"+
						"RS_CONF_EMPRESA E \n"+
						"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+ 
						"AND C.CVE_EMPRESA = 'CREDICONFIA'  \n"+
						"AND C.ID_PRESTAMO = '" + request.getParameter("IdPrestamoGrupo")+"' \n"+ 
						"AND C.APLICA_A = 'GRUPO' \n"+
						"AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
						"AND E.CVE_EMPRESA = C.CVE_EMPRESA \n";
		
		System.out.println(sSql);
						            

		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		//parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/prestamo/SimReporteF018A.jasper");
		parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReporteF018A1.jasper"));
		parametros.put("Subreporte2", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReporteF018A2.jasper"));
		return parametros;		
	}
}