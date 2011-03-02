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
public class SimReportesResEdoCtaIndREP implements ReporteControlIN {

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

		String sCveGpoEmpresa = request.getParameter("CveGpoEmpresa");
		String sCveEmpresa = request.getParameter("CveEmpresa");
		String sClave = request.getParameter("CvePrestamo");
		
		System.out.println("sClave:"+sClave);
		
		String sSql = "SELECT\n"+
						"CVE_GPO_EMPRESA,\n"+
						"CVE_EMPRESA,\n"+
						"CVE_PRESTAMO,\n"+
						"ID_PRESTAMO,\n"+
						"NOMBRE,\n"+
						"NOM_SUCURSAL,\n"+
						"FECHA_ENTREGA,\n"+
						"FECHA_REAL,\n"+
						"PERIODICIDAD_PRODUCTO,\n"+
						"PLAZO,\n"+
						"VALOR_TASA,\n"+
						"PERIODICIDAD_TASA\n"+
						"FROM V_CREDITO\n"+
						"WHERE CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
						"AND CVE_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n"+
						"AND CVE_PRESTAMO = '" + (String)request.getParameter("CvePrestamo") + "'\n";
	
	    String sTipoReporte = request.getParameter("TipoReporte");
		parametros.put("Sql", sSql);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReportesResEdoCtaInd.jasper");
		parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/reportes/SimReportesResEdoCtaInd_subreport1.jasper"));
		parametros.put("Subreporte2", contextoServlet.getRealPath("/Reportes/Sim/reportes/SimReportesResEdoCtaInd_subreport2.jasper"));
		parametros.put("NombreReporte", "rep"+sClave);                             
		
		return parametros;		
	}
}
