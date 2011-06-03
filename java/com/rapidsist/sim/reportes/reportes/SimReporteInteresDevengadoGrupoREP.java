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
 * Realiza la consulta para obtener los datos que serï¿½n utilizados para generar el reporte de los int. devengados en créditos grupales.
 */
public class SimReporteInteresDevengadoGrupoREP implements ReporteControlIN {

	/**
	 * Obtiene la consulta en la base de datos y parï¿½metros que serï¿½n utilizados por el reporte.
	 * @param parametrosCatalogo Parï¿½metros que se le envï¿½an al mï¿½todo .
	 * @param request Objeto que provee de informaciï¿½n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON.
	 * @param contextoServidor Objeto que contiene informaciï¿½n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param contextoServlet Objeto que contiene informaciï¿½n acerca del entorno del servlet. 
	 * @return Map objeto que contiene los datos para generar el reporte.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public  Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();

		String sIdRegional = request.getParameter("IdRegional");
		String sIdSucursal = request.getParameter("IdSucursal");
		String sCveUsuario = request.getParameter("CveUsuario");
		String sCvePrestamo = request.getParameter("CvePrestamo");
		
		String sSql = 	"SELECT FECHA, SUM(IMP_INTERES_DEV_X_DIA) AS IMP_INTERES_DEV_X_DIA, SUM(IMP_INTERES_PAGADO) AS IMP_INTERES_PAGADO\n"+
		"FROM (\n"+
				"SELECT B.FECHA, A.ID_PRESTAMO, A.NUM_PAGO_AMORTIZACION, A.IMP_INTERES_DEV_X_DIA, 0 AS IMP_INTERES_PAGADO\n"+
				  "FROM SIM_TABLA_AMORTIZACION A, PFIN_FECHA B\n"+
				 "WHERE A.CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "' AND\n"+
				       "A.CVE_EMPRESA     = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "' AND\n"+
				       "A.F_INI_AMORTIZACION <= '31-03-2011' AND\n"+
				       "A.FECHA_AMORTIZACION >= '01-01-2011' AND\n"+
				       "B.FECHA BETWEEN CASE WHEN F_INI_AMORTIZACION <= TO_DATE('01-01-2011')\n"+ 
				                            "THEN TO_DATE('01-01-2011')\n"+
				                            "ELSE F_INI_AMORTIZACION \n"+
				                       "END AND\n"+
				                       "CASE WHEN FECHA_AMORTIZACION <= TO_DATE('31-03-2011')\n"+ 
				                            "THEN FECHA_AMORTIZACION \n"+
				                            "ELSE TO_DATE('31-03-2011') \n"+
				                       "END\n"+
				 "UNION ALL \n"+
				"SELECT A.F_OPERACION, A.ID_PRESTAMO, A.NUM_PAGO_AMORTIZACION, 0 AS IMP_INTERES_DEV_X_DIA,\n"+ 
				       "B.IMP_CONCEPTO AS IMP_INTERES_PAGADO\n"+
				  "FROM PFIN_MOVIMIENTO A, PFIN_MOVIMIENTO_DET B\n"+
				 "WHERE A.CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "' AND\n"+
				       "A.CVE_EMPRESA     = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "' AND\n"+
				       "A.F_OPERACION BETWEEN '01-01-2011' AND '31-03-2011' AND\n"+
				       "A.SIT_MOVIMIENTO <> 'CA' AND\n"+
				       "A.CVE_OPERACION = 'CRPAGOPRES' AND\n"+
				       "A.CVE_GPO_EMPRESA = B.CVE_GPO_EMPRESA AND\n"+
				       "A.CVE_EMPRESA = B.CVE_EMPRESA AND\n"+
				       "A.ID_MOVIMIENTO = B.ID_MOVIMIENTO AND\n"+
				       "B.CVE_CONCEPTO IN ('INTERE','IVAINT') )\n"+ 
				 "GROUP BY FECHA\n"+
				 "ORDER BY FECHA\n";
					
		if (!sIdSucursal.equals("null")){
			sSql = sSql + "AND ID_SUCURSAL = '" + (String)request.getParameter("IdSucursal") + "'\n";
		}
	
		if (!sIdRegional.equals("null")){
			sSql = sSql + "AND ID_REGIONAL = '" + (String)request.getParameter("IdRegional") + "'\n";
		}
		
		if (!sCveUsuario.equals("null")){
			sSql = sSql + "AND CVE_ASESOR_CREDITO = '" + (String)request.getParameter("CveUsuario") + "'\n";
		}
		
		if (!sCveUsuario.equals("null")){
			sSql = sSql + "AND CVE_PRESTAMO = '" + (String)request.getParameter("CvePrestamo") + "'\n";
		}
		
		System.out.println(sSql);
	    String sTipoReporte = request.getParameter("TipoReporte");
		parametros.put("Sql", sSql);
		
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteInteresDevengadoGrupo.jasper");
		parametros.put("NombreReporte", "Intereses devengados");
		                             
		
		return parametros;	
	}
}