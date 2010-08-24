package com.rapidsist.sim.prestamo.reportes;

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
public class SimPrestamoCatalogoOperacionREP implements ReporteControlIN {

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

		String sClaveOperacion = request.getParameter("CveOperacion");
		
		String sSql = 	"SELECT \n"+
						"O.CVE_GPO_EMPRESA,\n"+ 
						"O.CVE_EMPRESA, \n"+
						"O.CVE_OPERACION, \n"+
						"O.DESC_CORTA, \n"+
						"O.DESC_LARGA, \n"+
						"O.CVE_AFECTA_SALDO,\n"+ 
						"O.CVE_AFECTA_CREDITO,\n"+
						"OP.CVE_CONCEPTO, \n"+
						"C.DESC_LARGA DESC_LARGA_CONCEPTO,\n"+ 
						"O.DESC_LARGA DESC_LARGA_OPERACION, \n"+
						"OP.CVE_AFECTA \n"+
						"FROM PFIN_CAT_OPERACION_CONCEPTO OP,\n"+ 
						"PFIN_CAT_CONCEPTO C, \n"+
						"PFIN_CAT_OPERACION O \n"+
						"WHERE O.CVE_GPO_EMPRESA ='" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					    "AND O.CVE_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND O.CVE_OPERACION = '" + (String)request.getParameter("CveOperacion") + "'\n"+ 
						//"AND OP.CVE_CONCEPTO = 'INTERE'\n"; 
						"AND O.CVE_GPO_EMPRESA = OP.CVE_GPO_EMPRESA\n"+ 
						"AND O.CVE_EMPRESA = OP.CVE_EMPRESA \n"+
						"AND O.CVE_OPERACION = OP.CVE_OPERACION\n"+
						"AND C.CVE_GPO_EMPRESA = OP.CVE_GPO_EMPRESA\n"+ 
						"AND C.CVE_EMPRESA = OP.CVE_EMPRESA \n"+
						"AND C.CVE_CONCEPTO = OP.CVE_CONCEPTO\n";					
		                    
							
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/prestamo/SimPrestamoCatalogoOperacion.jasper");
		parametros.put("NombreReporte", "rep"+sClaveOperacion);
		                             
		
		return parametros;		
	}
}