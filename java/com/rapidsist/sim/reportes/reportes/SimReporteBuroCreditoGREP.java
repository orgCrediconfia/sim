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
public class SimReporteBuroCreditoGREP implements ReporteControlIN {

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
		String sClaveGrupo = request.getParameter("CvePrestamoGrupo");
		
		System.out.println("sClaveGrupo:"+sClaveGrupo);
		
        String sSql = "SELECT\n"+
				        "SP.ID_CLIENTE,\n"+
				        "SP.ID_PRESTAMO,\n"+
				        "SP.CVE_PRESTAMO,\n"+
				        "RGP.RFC,\n"+
				        "RGP.NOMBRE_1,\n"+
				        "RGP.NOMBRE_2,\n"+
				        "RGP.AP_PATERNO,\n"+
				        "RGP.AP_MATERNO,\n"+
				        "RGD.CALLE,\n"+
				        "RGD.NUMERO_EXT,\n"+
				        "RGD.NOM_ASENTAMIENTO,\n"+
				        "RGD.NOM_DELEGACION,\n"+
				        "RGD.CODIGO_POSTAL,\n"+
				        "RGD.NOM_CIUDAD,\n"+
				        "RGD.NOM_ESTADO\n"+
				      "FROM\n"+
				        "RS_GRAL_PERSONA RGP,\n"+
				        "RS_GRAL_DOMICILIO RGD,\n"+
				        "SIM_PRESTAMO SP\n"+
				      "WHERE RGP.CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
				        "AND RGP.CVE_EMPRESA     = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n"+
				        "AND RGP.ID_PERSONA      = SP.ID_CLIENTE\n"+
				        "AND RGP.ID_PERSONA      = RGD.IDENTIFICADOR\n"+
				        "AND RGD.IDENTIFICADOR   = SP.ID_CLIENTE\n"+				  
        				"AND SP.CVE_PRESTAMO = '" + (String)request.getParameter("CvePrestamoGrupo") + "'\n";
							
                            if (sClaveGrupo != null && !sClaveGrupo.equals("") && !sClaveGrupo.equals("null") ){								
								sSql = sSql + "AND SP.CVE_PRESTAMO = '" + sClaveGrupo + "' \n";
							}	
					
							 sSql = sSql;
							 
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("Portales/Sam/img/Imagentsys1.gif"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteBuroCredito.jasper");
		parametros.put("NombreReporte", "Reporte"+sClaveGrupo);
		
		return parametros;		
	}
}
