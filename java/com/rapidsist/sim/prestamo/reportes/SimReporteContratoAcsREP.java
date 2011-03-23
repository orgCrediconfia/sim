/**
 * Sistema Central de Control.
 *
 */

package com.rapidsist.sim.prestamo.reportes;

import com.rapidsist.portal.cliente.reportes.ReporteControlIN;

import java.util.Iterator;
import java.util.LinkedList;
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
 * Esta clase se encarga de administrar la operaciï¿½n consulta del Reporte Contrato ACS
 * Esta clase es llamada por el servlet ProcesaReporteS.
 */

public class SimReporteContratoAcsREP implements ReporteControlIN {

	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();
		
		String sIdPrestamo = request.getParameter("IdPrestamo");
		System.out.println("Id Prestamo:"+sIdPrestamo);
		
		parametrosCatalogo.addDefCampo("ID_PRESTAMO", sIdPrestamo);

		LinkedList lParticipantes = catalogoSL.getRegistros(
				"SimParticipanteC", parametrosCatalogo);
		Iterator iteratorParticipantes = lParticipantes.iterator();
		
		String sObligadoUno = "";
		String sObligadoUnoDomicilio = "";
		String sObligadoDos = "";
		String sObligadoDosDomicilio = "";
		String sGarante = "";
		String sGaranteDomicilio = "";
		// ITEREA TODOS LOS PARTICIPANTES DEL CREDITO
		while (iteratorParticipantes.hasNext()) {
			Registro registro = (Registro) iteratorParticipantes.next();
			String sTipoPersona = (String)registro.getDefCampo("CVE_TIPO_PERSONA");
			if (sTipoPersona.equals("OBLIGADO")){
				sObligadoUno = (String)registro.getDefCampo("NOM_COMPLETO");
				sObligadoUnoDomicilio = (String)registro.getDefCampo("DIRECCION");
			}else if(sTipoPersona.equals("OBLIGADO 2")){
				sObligadoDos = (String)registro.getDefCampo("NOM_COMPLETO");
				sObligadoDosDomicilio = (String)registro.getDefCampo("DIRECCION");
			}else if(sTipoPersona.equals("GARANTE")){
				sGarante = (String)registro.getDefCampo("NOM_COMPLETO");
				sGaranteDomicilio = (String)registro.getDefCampo("DIRECCION");
			}
		
		}
		
		//Parámetros para obtener los obligados solidarios y el garante
		parametros.put("ObligadoUno", sObligadoUno);
		parametros.put("ObligadoUnoDomicilio", sObligadoUnoDomicilio);
		parametros.put("ObligadoDos", sObligadoDos);
		parametros.put("ObligadoDosDomicilio", sObligadoDosDomicilio);
		parametros.put("GaranteUno", sGarante);
		parametros.put("GaranteDomicilio", sGaranteDomicilio);
		
		parametros.put("IdPrestamo", sIdPrestamo);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/prestamo/SimReporteContratoACS.jasper");
		parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReporteContratoACS1.jasper"));
		parametros.put("Subreporte2", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReporteContratoACS2.jasper"));
		parametros.put("Subreporte3", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReporteContratoACS3.jasper"));
		parametros.put("Subreporte4", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReporteContratoACS4.jasper"));
		parametros.put("Subreporte5", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReporteContratoACS5.jasper"));
		return parametros;		
	}
}