package com.rapidsist.sim.reportes.reportes;

import com.rapidsist.portal.cliente.reportes.ReporteControlIN;

import java.sql.SQLException;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import com.rapidsist.comun.util.Fecha2;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import com.rapidsist.portal.catalogos.CatalogoSL;
import javax.naming.Context;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;

import javax.servlet.http.HttpSession;
import com.rapidsist.portal.configuracion.Usuario;

/**
 * Realiza la consulta para obtener los datos que ser�n utilizados para generar el reporte de cat�logo de aplicaciones.
 */
public class SimReportesAmortizacionIndREP implements ReporteControlIN {

	/**
	 * Obtiene la consulta en la base de datos y par�metros que ser�n utilizados por el reporte.
	 * @param parametrosCatalogo Par�metros que se le env�an al m�todo .
	 * @param request Objeto que provee de informaci�n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo env�a como un par�metro a este m�todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON.
	 * @param contextoServidor Objeto que contiene informaci�n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param contextoServlet Objeto que contiene informaci�n acerca del entorno del servlet. 
	 * @return Map objeto que contiene los datos para generar el reporte.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public  Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();
		
		String sClavePrestamo = (String)request.getParameter("CvePrestamo");

		String sSql = "SELECT \n"+    
					 "V.CVE_GPO_EMPRESA, \n"+
					 "V.CVE_EMPRESA, \n"+
					 "V.ID_PRESTAMO, \n"+
					 "V.CVE_PRESTAMO, \n"+
					 "V.NUM_PAGO_AMORTIZACION, \n"+
					 "V.FECHA_AMORTIZACION, \n"+
					 "TO_NUMBER (V.IMP_SALDO_INICIAL, '999G999D0000') IMP_SALDO_INICIAL, \n"+
					 "V.TASA_INTERES, \n"+
					 "V.INTERES, \n"+
					 "V.IMP_CAPITAL_AMORT, \n"+
					 "V.IMP_PAGO, \n"+
					 "V.IMP_ACCESORIO, \n"+
					 "V.PAGO_TOTAL, \n"+
					 "TO_NUMBER (V.IMP_SALDO_FINAL, '999G999D0000') IMP_SALDO_FINAL \n"+
					 "FROM \n"+
					 "V_TABLA_AMORT_INDIVIDUAL V, \n"+
					 "SIM_PRESTAMO P \n"+
					 "WHERE V.CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					 "AND V.CVE_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "' \n"+
					 "AND V.CVE_PRESTAMO = '" + (String)request.getParameter("CvePrestamo") + "' \n"+
					 "AND P.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n"+
					 "AND P.CVE_EMPRESA = V.CVE_EMPRESA \n"+
					 "AND P.ID_PRESTAMO = V.ID_PRESTAMO \n"+
					 "AND P.ID_ETAPA_PRESTAMO != '16' \n"+
					 "AND P.ID_ETAPA_PRESTAMO != '8' \n"+
					 "ORDER BY  V.NUM_PAGO_AMORTIZACION \n";
							
	    String sTipoReporte = request.getParameter("TipoReporte");
		parametros.put("Sql", sSql);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReportesAmortizacionInd.jasper");
		parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/reportes/SimReportesAmortizacionInd1.jasper"));
		parametros.put("Subreporte2", contextoServlet.getRealPath("/Reportes/Sim/reportes/SimReportesAmortizacionInd2.jasper"));
		parametros.put("NombreReporte", "rep"+sClavePrestamo);
		                  
		return parametros;		
	}
}