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
public class SimReporteInteresDevengadoGrupoXlsREP implements ReporteControlIN {

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
		
		String sSql = 	"SELECT\n"+
		  "FECHA, \n"+
		  "SUM(IMP_INTERES_DEV_X_DIA) AS IMP_INTERES_DEV_X_DIA,\n"+
		  "SUM(IMP_INTERES_PAGADO) AS IMP_INTERES_PAGADO\n"+
		"FROM\n"+
		  "(SELECT\n"+
		    "B.FECHA,\n"+
		    "A.ID_PRESTAMO,\n"+
		    "A.NUM_PAGO_AMORTIZACION,\n"+
		    "A.IMP_INTERES_DEV_X_DIA,\n"+
		    "0 AS IMP_INTERES_PAGADO,\n"+
		    "P.CVE_PRESTAMO,\n"+
		    "P.CVE_ASESOR_CREDITO,\n"+
		    "S.ID_SUCURSAL,\n"+
		    "R.ID_REGIONAL\n"+
		  "FROM\n"+
		    "SIM_TABLA_AMORTIZACION A,\n"+
		    "PFIN_FECHA B,\n"+
		    "SIM_PRESTAMO P,\n"+
		    "SIM_CAT_SUCURSAL S,\n"+
		    "SIM_CAT_REGIONAL R \n"+
		  "WHERE\n"+
		    "A.CVE_GPO_EMPRESA     = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "' AND\n"+
		    "A.CVE_EMPRESA         = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "' AND\n"+
		    "A.F_INI_AMORTIZACION  <= '31-MAR-2011' AND\n"+
		    "A.FECHA_AMORTIZACION  >= '01-ENE-2011' AND\n"+
		    "P.CVE_GPO_EMPRESA     = A.CVE_GPO_EMPRESA AND\n"+
		    "P.CVE_EMPRESA         = A.CVE_EMPRESA AND\n"+
		    "P.ID_PRESTAMO         = A.ID_PRESTAMO AND\n"+
		    "S.CVE_GPO_EMPRESA     = P.CVE_GPO_EMPRESA AND\n"+
		    "S.CVE_EMPRESA         = P.CVE_EMPRESA AND\n"+
		    "S.ID_SUCURSAL         = P.ID_SUCURSAL AND\n"+
		    "R.CVE_GPO_EMPRESA     = S.CVE_GPO_EMPRESA AND\n"+
		    "R.CVE_EMPRESA         = S.CVE_EMPRESA AND\n"+
		    "R.ID_REGIONAL         = S.ID_REGIONAL AND\n"+
		    "B.FECHA BETWEEN CASE WHEN F_INI_AMORTIZACION <= TO_DATE('01-ENE-2011') THEN TO_DATE('01-ENE-2011') ELSE F_INI_AMORTIZACION END AND\n"+ 
		                    "CASE WHEN FECHA_AMORTIZACION <= TO_DATE('31-MAR-2011') THEN FECHA_AMORTIZACION ELSE TO_DATE('31-MAR-2011') END\n"+
		  "UNION ALL\n"+
		  "SELECT \n"+
		    "A.F_OPERACION,\n"+
		    "A.ID_PRESTAMO,\n"+
		    "A.NUM_PAGO_AMORTIZACION,\n"+
		    "0 AS IMP_INTERES_DEV_X_DIA,\n"+
		    "B.IMP_CONCEPTO AS IMP_INTERES_PAGADO,\n"+
		    "P.CVE_PRESTAMO,\n"+
		    "P.CVE_ASESOR_CREDITO,\n"+
		    "S.ID_SUCURSAL,\n"+
		    "R.ID_REGIONAL\n"+
		  "FROM\n"+
		    "PFIN_MOVIMIENTO A,\n"+
		    "PFIN_MOVIMIENTO_DET B,\n"+
		    "SIM_PRESTAMO P,\n"+
		    "SIM_CAT_SUCURSAL S,\n"+
		    "SIM_CAT_REGIONAL R  \n"+
		  "WHERE\n"+
		    "A.CVE_GPO_EMPRESA     = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "' AND\n"+
		    "A.CVE_EMPRESA         = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "' AND\n"+ 
		    "A.F_OPERACION BETWEEN '01-ENE-2011' AND '31-MAR-2011' AND\n"+
		    "A.SIT_MOVIMIENTO      <> 'CA' AND\n"+
		    "A.CVE_OPERACION       = 'CRPAGOPRES' AND\n"+
		    "B.CVE_GPO_EMPRESA     = A.CVE_GPO_EMPRESA AND\n"+
		    "B.CVE_EMPRESA         = A.CVE_EMPRESA AND\n"+
		    "B.ID_MOVIMIENTO       = A.ID_MOVIMIENTO AND\n"+
		    "B.CVE_CONCEPTO IN ('INTERE', 'IVAINT') AND\n"+
		    "P.CVE_GPO_EMPRESA     = A.CVE_GPO_EMPRESA AND\n"+
		    "P.CVE_EMPRESA         = A.CVE_EMPRESA AND\n"+
		    "P.ID_PRESTAMO         = A.ID_PRESTAMO AND\n"+
		    "S.CVE_GPO_EMPRESA     = P.CVE_GPO_EMPRESA AND\n"+
		    "S.CVE_EMPRESA         = P.CVE_EMPRESA AND\n"+
		    "S.ID_SUCURSAL         = P.ID_SUCURSAL AND\n"+
		    "R.CVE_GPO_EMPRESA     = S.CVE_GPO_EMPRESA AND\n"+
		    "R.CVE_EMPRESA         = S.CVE_EMPRESA AND\n"+
		    "R.ID_REGIONAL         = S.ID_REGIONAL)\n"+
		"WHERE 1                   = 1\n";
		
		
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
		  
			sSql = sSql + "GROUP BY FECHA\n"+
			"ORDER BY FECHA\n";
		
		System.out.println(sSql);
	    String sTipoReporte = request.getParameter("TipoReporte");
		parametros.put("Sql", sSql);
		parametros.put("NomRegional", sIdRegional);
		parametros.put("NomSucursal", sIdSucursal);
		parametros.put("CveUsuario", sCveUsuario);
		parametros.put("CvePrestamo", sCvePrestamo);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteInteresDevengadoGrupoXls.jasper");
		parametros.put("NombreReporte", "Intereses devengados");
		                             
		
		return parametros;	
	}
}