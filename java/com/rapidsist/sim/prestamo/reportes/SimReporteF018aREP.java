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
						"TO_CHAR(C.MONTO_FIJO_PERIODO,'999,999.00') MONTO_FIJO_PERIODO, \n"+
						"CANTIDADES_LETRAS(MONTO_FIJO_PERIODO) MONTO_FIJO_PERIODO_LETRAS, \n"+ 
						"TO_CHAR(C.FECHA_ENTREGA, 'DD') ||' de '|| RTRIM(TO_CHAR(C.FECHA_ENTREGA, 'MONTH')) ||' de '||TO_CHAR(C.FECHA_ENTREGA, 'YYYY') FECHA_INICIO, \n"+
						"C.ID_SUCURSAL, \n"+
						"C.NOM_SUCURSAL, \n"+
						"C.DIRECCION_SUCURSAL, \n"+
						"TO_CHAR(C.MONTO_AUTORIZADO + C.CARGO_INICIAL,'999,999,999.00') MONTO_AUTORIZADO, \n"+
						"CANTIDADES_LETRAS(C.MONTO_AUTORIZADO + C.CARGO_INICIAL) MONTO_AUTORIZADO_LETRAS, \n"+
					    "GC.VALOR AS VALOR_ACCESORIO_NUEVO, \n"+
					    "CP.NOM_PERIODICIDAD AS PERIODICIDAD_ACCESORIO_NUEVO, \n"+
						"E.TX_DESC_EMPRESA \n"+
					"FROM V_CREDITO C, \n"+
						"RS_CONF_EMPRESA E, \n"+
					  "SIM_PRESTAMO_GPO_CARGO GC, \n"+
					  "SIM_CAT_PERIODICIDAD CP \n"+
					"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+ 
						"AND C.CVE_EMPRESA = 'CREDICONFIA' \n"+ 
						"AND C.ID_PRESTAMO = '" + request.getParameter("IdPrestamoGrupo")+"' \n"+ 
						"AND C.APLICA_A = 'GRUPO' \n"+
						"AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
						"AND E.CVE_EMPRESA = C.CVE_EMPRESA \n"+
					    "AND E.CVE_GPO_EMPRESA = GC.CVE_GPO_EMPRESA \n"+
						"AND E.CVE_EMPRESA = GC.CVE_EMPRESA \n"+
					    "AND GC.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
						"AND GC.CVE_EMPRESA = C.CVE_EMPRESA \n"+
					    "AND GC.ID_PRESTAMO_GRUPO = C.ID_PRESTAMO \n"+
					    "AND GC.ID_PRESTAMO_GRUPO = GC.ID_PRESTAMO_GRUPO \n"+
					    "AND GC.ID_CARGO_COMISION = 15 \n"+
					    "AND GC.ID_PERIODICIDAD = CP.ID_PERIODICIDAD \n"; 
		
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