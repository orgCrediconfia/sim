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
		
		String sSql = 	"SELECT\n"+
						  "ID_REGIONAL,\n"+
						  "ID_SUCURSAL,\n"+
						  "CVE_ASESOR_CREDITO,\n"+
						  "ID_PRESTAMO_GPO AS ID_PRESTAMO,\n"+
						  "CVE_PRESTAMO_GPO AS CVE_PRESTAMO,\n"+
						  "NOM_CLIENTE AS NOM_GRUPO,\n"+
						  "FECHA_DIA,\n"+
						  "IMP_INTERES_DEV_X_DIA,\n"+
						  "IMP_INTERES_DEV_PAGADO\n"+
						"FROM\n"+
						  "SIM_PRESTAMO_INT_DEV_GPO\n"+
						"WHERE CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
						  "AND CVE_EMPRESA     = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n"+
						  
						"UNION ALL\n"+
				
						"SELECT\n"+
						  "ID_REGIONAL,\n"+
						  "ID_SUCURSAL,\n"+
						  "CVE_ASESOR_CREDITO,\n"+
						  "ID_PRESTAMO,\n"+
						  "CVE_PRESTAMO,\n"+
						  "NOM_CLIENTE,\n"+
						  "FECHA_DIA,\n"+
						  "IMP_INTERES_DEV_X_DIA,\n"+
						  "IMP_INTERES_DEV_PAGADO\n"+
						"FROM SIM_PRESTAMO_INTERES_DEVENGADO\n"+
						"WHERE CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
						  "AND CVE_EMPRESA     = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n";
		
		if (!sIdSucursal.equals("null")){
			sSql = sSql + "AND ID_SUCURSAL = '" + (String)request.getParameter("IdSucursal") + "'\n";
		}
	
		if (!sIdRegional.equals("null")){
			sSql = sSql + "AND ID_REGIONAL = '" + (String)request.getParameter("IdRegional") + "'\n";
		}
		
		if (!sCveUsuario.equals("null")){
			sSql = sSql + "AND CVE_ASESOR_CREDITO = '" + (String)request.getParameter("CveUsuario") + "'\n";
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