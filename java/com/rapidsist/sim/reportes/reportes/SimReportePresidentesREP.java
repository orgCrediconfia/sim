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
public class SimReportePresidentesREP implements ReporteControlIN {

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
		String sClaveGrupo = request.getParameter("IdPrestamoGrupo");
		
		System.out.println("sClaveGrupo:"+sClaveGrupo);
		
        String sSql ="SELECT \n"+
						"PG.ID_PRESTAMO_GRUPO,\n"+ 
						"PG.ID_GRUPO, \n"+
						"G.NOM_GRUPO,\n"+
						"G.ID_COORDINADOR,\n"+
						"P.NOM_COMPLETO,\n"+
						"PG.NUM_CICLO, \n"+
						"NVL(PG.FECHA_REAL, PG.FECHA_ENTREGA) FECHA_INICIO,\n"+
						"MAX(T.FECHA_AMORTIZACION) FECHA_FIN\n"+
						"FROM\n"+ 
						"SIM_PRESTAMO_GRUPO PG,\n"+
						"SIM_GRUPO G,\n"+
						"RS_GRAL_PERSONA P,\n"+
						"V_TABLA_AMORTIZACION_GRUPAL T\n"+
						"WHERE PG.CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
						"AND PG.CVE_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n"+
						"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA\n"+
						"AND G.CVE_EMPRESA = PG.CVE_EMPRESA\n"+
						"AND G.ID_GRUPO = PG.ID_GRUPO\n"+
						"AND P.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA\n"+
						"AND P.CVE_EMPRESA = G.CVE_EMPRESA\n"+
						"AND P.ID_PERSONA = G.ID_COORDINADOR\n"+ 
						"AND T.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA\n"+
						"AND T.CVE_EMPRESA = PG.CVE_EMPRESA\n"+
						"AND T.ID_PRESTAMO_GRUPO = PG.ID_PRESTAMO_GRUPO\n"+ 
						"GROUP BY PG.ID_PRESTAMO_GRUPO, \n"+
						"PG.ID_GRUPO, \n"+
						"G.NOM_GRUPO, \n"+
						"G.ID_COORDINADOR, \n"+
						"P.NOM_COMPLETO, \n"+
						"PG.NUM_CICLO, \n"+
						"NVL(PG.FECHA_REAL, PG.FECHA_ENTREGA)\n";
        
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("Portales/Sam/img/Imagentsys1.gif"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReportePresidentes.jasper");
		parametros.put("NombreReporte", "Reporte"+sClaveGrupo);
		
		return parametros;		
	}
}
