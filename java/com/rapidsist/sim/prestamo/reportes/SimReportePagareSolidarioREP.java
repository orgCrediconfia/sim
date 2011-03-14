/**
 * Sistema Central de Control.
 *
 */

package com.rapidsist.sim.prestamo.reportes;

import com.rapidsist.portal.cliente.reportes.ReporteControlIN;

import java.util.Iterator;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import com.rapidsist.comun.util.Fecha2;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import com.rapidsist.portal.catalogos.CatalogoSL;
import javax.naming.Context;
import com.rapidsist.comun.bd.Registro;
import java.util.LinkedList;

/**
 * Esta clase se encarga de administrar la operaciï¿½n consulta del Reporte Anexo A
 * Esta clase es llamada por el servlet ProcesaReporteS.
 */

public class SimReportePagareSolidarioREP implements ReporteControlIN {

	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();

		//OBTIENE EL NOMBRE DE TODOS LOS INTEGRANTES DEL PRESTAMO GRUPAL
		parametrosCatalogo.addDefCampo("ID_GRUPO", request.getParameter("IdGrupo"));
		parametrosCatalogo.addDefCampo("CONSULTA", "ASIGNADOS");
		LinkedList lIntegrantes = catalogoSL.getRegistros("SimIntegrantesGrupo", parametrosCatalogo);
		Iterator iteratorIntegrantes = lIntegrantes.iterator();
		String sIntegrantes = "";
		
		int iCantidadIntegrantes = lIntegrantes.size();
		
		//ITEREA TODOS LOS INTEGRANTES Y LO DEJA EN UNA CADENA UNICA
		while(iteratorIntegrantes.hasNext()){
			Registro registro = (Registro)iteratorIntegrantes.next();
			sIntegrantes = sIntegrantes + " " + (String)registro.getDefCampo("NOM_COMPLETO") +", ";
			System.out.println(sIntegrantes);
		}
		
		//sIntegrantes = "Por este PagarÃ© los seÃ±or(es) firmantes, "+ sIntegrantes + "(el (los) â€œSuscriptor(es)â€�), se obliga(n) incondicional y solidariamente en este acto a pagar a";
		
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
						"C.PLAZO, \n"+
						"TO_CHAR(C.MONTO_AUTORIZADO + C.CARGO_INICIAL,'999,999,999.00') MONTO_AUTORIZADO, \n"+
						"CANTIDADES_LETRAS(C.MONTO_AUTORIZADO) MONTO_AUTORIZADO_LETRAS \n"+
						"FROM V_CREDITO C, \n"+
						"RS_CONF_EMPRESA E \n"+
						"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+ 
						"AND C.CVE_EMPRESA = 'CREDICONFIA'  \n"+
						"AND C.ID_PRESTAMO = '" + request.getParameter("IdPrestamoGrupo")+"' \n"+
						"AND C.APLICA_A = 'GRUPO' \n"+
						"AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
						"AND E.CVE_EMPRESA = C.CVE_EMPRESA \n";
		
		System.out.println("sSql"+sSql);
		
		parametros.put("Sql", sSql);
		parametros.put("NombresIntegrantes", sIntegrantes);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		
		
		if (iCantidadIntegrantes <= 7){
			parametros.put("NomReporte", "/Reportes/Sim/prestamo/SimReportePagareSolidarioNuevo.jasper");
			parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReportePagareSolidario_subreport0.jasper"));
			parametros.put("Subreporte2", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReportePagareSolidario2.jasper"));
			parametros.put("Subreporte3", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReportePagareSolidario1.jasper"));
		}else{
			parametros.put("NomReporte", "/Reportes/Sim/prestamo/SimReportePagareSolidarioNuevo.jasper");
			parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReportePagareSolidario_subreport0.jasper"));
			parametros.put("Subreporte2", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReportePagareSolidario2.jasper"));
			parametros.put("Subreporte3", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReportePagareSolidario1.jasper"));
			
		}

		return parametros;
	}
}
