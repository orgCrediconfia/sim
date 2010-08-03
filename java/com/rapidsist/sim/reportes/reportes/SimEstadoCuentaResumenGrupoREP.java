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
public class SimEstadoCuentaResumenGrupoREP implements ReporteControlIN {

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

		String sClaveGrupo = request.getParameter("CvePrestamoGrupo");
		String sNombreGrupo =request.getParameter("NomGrupo");
		
		System.out.println("sClaveGrupo:"+sClaveGrupo);
		System.out.println("sNombreGrupo:"+sNombreGrupo);
		
		String sSql = "SELECT\n"+
							"V.CVE_GPO_EMPRESA,\n"+
							"V.CVE_EMPRESA,\n"+
							"V.ID_PRESTAMO,\n"+
							"V.DESC_MOVIMIENTO,\n"+
							"V.IMP_DEBE_TOTAL,\n"+
							"V.IMP_PAGO_TOTAL,\n"+
							"V.IMP_SALDO_TOTAL,\n"+
							"V.IMP_DEBE_HOY,\n"+
							"V.IMP_PAGO_HOY,\n"+
							"V.IMP_SALDO_HOY,\n"+
							"PG.ID_PRESTAMO_GRUPO,\n"+
							"PG.NUM_CICLO,\n"+
							"PG.CVE_PRESTAMO_GRUPO,\n"+
							"R.NOM_GRUPO,\n"+
							"P.NOM_PRODUCTO\n"+
							
							"FROM\n"+
							"V_SIM_PRESTAMO_GPO_RES_EDO_CTA V,\n"+
							"SIM_PRESTAMO_GRUPO PG,\n"+
							"SIM_GRUPO R,\n"+
							"SIM_PRODUCTO P\n"+
							
							"WHERE PG.CVE_GPO_EMPRESA   = V.CVE_GPO_EMPRESA\n"+
							"AND PG.CVE_EMPRESA          = V.CVE_EMPRESA\n"+
							"AND PG.ID_PRESTAMO_GRUPO          = V.ID_PRESTAMO\n"+
					
							"AND PG.CVE_GPO_EMPRESA     = R.CVE_GPO_EMPRESA\n"+
							"AND PG.CVE_EMPRESA         = R.CVE_EMPRESA\n"+
							"AND PG.ID_GRUPO            = R.ID_GRUPO\n"+
					
							"AND PG.CVE_GPO_EMPRESA     = P.CVE_GPO_EMPRESA\n"+
							"AND PG.CVE_EMPRESA         = P.CVE_EMPRESA\n"+
							"AND PG.ID_PRODUCTO         = P.ID_PRODUCTO\n";
						 
							if (sClaveGrupo != null && !sClaveGrupo.equals("") && !sClaveGrupo.equals("null") ){								
								sSql = sSql + "AND PG.CVE_PRESTAMO_GRUPO = '" + sClaveGrupo + "' \n";
							}
							if (sNombreGrupo != null && !sNombreGrupo.equals("") && !sNombreGrupo.equals("null") ){
								sSql = sSql + "AND R.NOM_GRUPO = '" + sNombreGrupo + "' \n";
							}		
		
							 sSql = sSql +
							 "ORDER BY V.ID_PRESTAMO\n";
							
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("Portales/Sam/img/Imagentsys1.gif"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimEstadoCuentaResumenGrupoXLS.jasper");
		                             
		
		return parametros;		
	}
}
