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
 * Esta clase se encarga de administrar la operación consulta del Reporte Anexo A
 * Esta clase es llamada por el servlet ProcesaReporteS.
 */

public class SimReportePagareREP implements ReporteControlIN {

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

		String sSql =   "SELECT \n"+
						"C.ID_PRESTAMO, \n"+
						"C.CVE_NOMBRE ID_CLIENTE, \n"+ 
						"C.NOMBRE NOM_COMPLETO, \n"+
						"C.NUM_CICLO, \n"+
						"C.ID_SUCURSAL, \n"+
						"TO_CHAR(C.FECHA_ENTREGA, 'DD') ||' de '|| RTRIM(TO_CHAR(C.FECHA_ENTREGA, 'MONTH')) ||' de '||TO_CHAR(C.FECHA_ENTREGA, 'YYYY') FECHA_ENTREGA, \n"+
						"C.DIRECCION_SUCURSAL,\n"+ 
						"C.PLAZO, \n"+
						"C.ID_PERIODICIDAD_PRODUCTO, \n"+ 
						"C.PERIODICIDAD_PRODUCTO NOM_PERIODICIDAD, \n"+
						"DC.CALLE||' '||DC.NUMERO_INT||' '||DC.NOM_ASENTAMIENTO||' '||DC.NOM_DELEGACION||' '||DC.NOM_ESTADO||' '||'C.P.'||' '||DC.CODIGO_POSTAL DIRECCION_CLIENTE, \n"+
						"TO_CHAR(C.MONTO_AUTORIZADO + CARGO_INICIAL,'999,999,999.99') MONTO_AUTORIZADO, \n"+ 
						"CANTIDADES_LETRAS(MONTO_AUTORIZADO + CARGO_INICIAL) MONTO_AUTORIZADO_LETRAS, \n"+ 
						"PA.ID_PERSONA ID_AVAL, \n"+
						"PPA.NOM_COMPLETO NOM_AVAL, \n"+
						"DA.CALLE||' '||DA.NUMERO_INT||' '||DA.NOM_ASENTAMIENTO||' '||DA.NOM_DELEGACION||' '||DA.NOM_ESTADO||' '||'C.P.'||' '||DA.CODIGO_POSTAL DIRECCION_AVAL \n"+ 
						"FROM V_CREDITO C, \n"+
						"SIM_PRESTAMO_PARTICIPANTE PA, \n"+ 
						"RS_GRAL_PERSONA PPA, \n"+
						"RS_GRAL_DOMICILIO DA, \n"+
						"RS_GRAL_DOMICILIO DC \n"+
						"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+
						"AND C.CVE_EMPRESA = 'CREDICONFIA' \n"+
						"AND C.ID_PRESTAMO = '" + request.getParameter("IdPrestamo")+"' \n"+
						"AND C.APLICA_A = 'INDIVIDUAL' \n"+
						"AND DC.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+ 
						"AND DC.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
						"AND DC.IDENTIFICADOR (+)= C.CVE_NOMBRE \n"+
						"AND DC.DOMICILIO_FISCAL (+)= 'V' \n"+
						"AND PA.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+  
						"AND PA.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
						"AND PA.ID_PRESTAMO (+)= C.ID_PRESTAMO \n"+
						"AND PA.CVE_TIPO_PERSONA (+)= 'AVAL' \n"+
						"AND PPA.CVE_GPO_EMPRESA (+)= PA.CVE_GPO_EMPRESA \n"+  
						"AND PPA.CVE_EMPRESA (+)= PA.CVE_EMPRESA \n"+
						"AND PPA.ID_PERSONA (+)= PA.ID_PERSONA \n"+
						"AND DA.CVE_GPO_EMPRESA (+)= PA.CVE_GPO_EMPRESA \n"+ 
						"AND DA.CVE_EMPRESA (+)= PA.CVE_EMPRESA \n"+
						"AND DA.IDENTIFICADOR (+)= PA.ID_PERSONA \n"+
						"AND DA.DOMICILIO_FISCAL (+)= 'V' \n";
					
		//Parámetros para obtener los obligados solidarios y el garante
		parametros.put("ObligadoUno", sObligadoUno);
		parametros.put("ObligadoUnoDomicilio", sObligadoUnoDomicilio);
		parametros.put("ObligadoDos", sObligadoDos);
		parametros.put("ObligadoDosDomicilio", sObligadoDosDomicilio);
		parametros.put("GaranteUno", sGarante);
		parametros.put("GaranteDomicilio", sGaranteDomicilio);
		
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/prestamo/SimReportePagare.jasper");
		return parametros;		
	}
}