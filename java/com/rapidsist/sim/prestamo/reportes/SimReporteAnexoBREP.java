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

public class SimReporteAnexoBREP implements ReporteControlIN {
	
	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet) throws Exception {
		Map parametros = new HashMap();
		
		String sSql =	"SELECT\n" +
		"A.NOM_COMPLETO AS NOM_GARANTE,\n" +
		"B.NOM_COMPLETO AS SOLIDARIO,\n" +
		"C.NOM_COMPLETO AS SOLIDARIO_2,\n" +
		"VC.DIRECCION_SUCURSAL,\n" +
		"TO_CHAR(VC.FECHA_ENTREGA, 'DD') ||' de '|| RTRIM(TO_CHAR(VC.FECHA_ENTREGA, 'MONTH')) ||' de '||TO_CHAR(VC.FECHA_ENTREGA, 'YYYY') FECHA_ENTREGA\n" +
		"FROM\n" +
		"(SELECT \n" +
		  "GP.NOM_COMPLETO,\n" +
		  "PP.CVE_TIPO_PERSONA\n" +
		"FROM\n" +
		  "RS_GRAL_PERSONA GP,\n" +
		  "SIM_PRESTAMO_PARTICIPANTE PP\n" +
		"WHERE PP.CVE_GPO_EMPRESA         = 'SIM'\n" +
			"AND PP.CVE_EMPRESA             = 'CREDICONFIA'\n" +
		  "AND PP.ID_PRESTAMO             = '" + request.getParameter("IdPrestamo") + "'\n" +
		  "AND GP.CVE_GPO_EMPRESA         = PP.CVE_GPO_EMPRESA\n" +
		  "AND GP.CVE_EMPRESA             = GP.CVE_EMPRESA\n" +
		  "AND GP.ID_PERSONA              = PP.ID_PERSONA\n" +
		  "AND PP.CVE_TIPO_PERSONA        = 'GARANTE')A,\n" +
		"(SELECT \n" +
		  "GP.NOM_COMPLETO,\n" +
		  "PP.CVE_TIPO_PERSONA\n" +
		"FROM\n" +
		  "RS_GRAL_PERSONA GP,\n" +
		  "SIM_PRESTAMO_PARTICIPANTE PP\n" +
		"WHERE PP.CVE_GPO_EMPRESA         = 'SIM'\n" +
			"AND PP.CVE_EMPRESA             = 'CREDICONFIA'\n" +
		  "AND PP.ID_PRESTAMO             = '" + request.getParameter("IdPrestamo") + "'\n" +
		  "AND GP.CVE_GPO_EMPRESA         = PP.CVE_GPO_EMPRESA\n" +
		  "AND GP.CVE_EMPRESA             = GP.CVE_EMPRESA\n" +
		  "AND GP.ID_PERSONA              = PP.ID_PERSONA\n" +
		  "AND PP.CVE_TIPO_PERSONA        = 'OBLIGADO') B,\n" +
		"(SELECT \n" +
		  "GP.NOM_COMPLETO,\n" +
		  "PP.CVE_TIPO_PERSONA,\n" +
		  "PP.ID_PRESTAMO\n" +
		"FROM\n" +
		  "RS_GRAL_PERSONA GP,\n" +
		  "SIM_PRESTAMO_PARTICIPANTE PP\n" +
		"WHERE PP.CVE_GPO_EMPRESA         = 'SIM'\n" +
		  "AND PP.CVE_EMPRESA             = 'CREDICONFIA'\n" +
		  "AND PP.ID_PRESTAMO             = '" + request.getParameter("IdPrestamo") + "'" +
		  "AND GP.CVE_GPO_EMPRESA         = PP.CVE_GPO_EMPRESA\n" +
		  "AND GP.CVE_EMPRESA             = GP.CVE_EMPRESA\n" +
		  "AND GP.ID_PERSONA              = PP.ID_PERSONA\n" +
		  "AND PP.CVE_TIPO_PERSONA        = 'OBLIGADO 2') C,\n" +
		"V_CREDITO VC\n" +
		"WHERE C.ID_PRESTAMO       = VC.ID_PRESTAMO\n";
		
		System.out.println("sSql" + sSql);
		
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte","/Reportes/Sim/prestamo/SimReporteAnexoB.jasper");
		return parametros;
	
	}
}
