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
public class SimReportePrestamoEmpleados1REP implements ReporteControlIN {

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
		
				
		String sSql = 	"SELECT \n"+   
			        "C.ID_PRESTAMO,\n"+
			        "C.CVE_PRESTAMO,\n"+
			        "C.CVE_NOMBRE ID_CLIENTE,\n"+  
			        "C.NOMBRE NOM_COMPLETO, \n"+
			        "C.NUM_CICLO, \n"+
			        "TO_CHAR(TO_DATE(C.FECHA_FIN),'DD \"de\" MONTH \"de\" YYYY') FECHA_FIN,\n"+ 
			        "C.DIRECCION_SUCURSAL, \n"+
			        "C.PERIODICIDAD_PRODUCTO NOM_PERIODICIDAD,\n"+ 
			        "TO_CHAR(C.MONTO_AUTORIZADO + CARGO_INICIAL,'999,999,999.99') MONTO_AUTORIZADO,\n"+  
			        "CANTIDADES_LETRAS(MONTO_AUTORIZADO + CARGO_INICIAL) MONTO_AUTORIZADO_LETRAS,\n"+  
			        "C.VALOR_TASA,\n"+
			        "PA.ID_PERSONA ID_AVAL,\n"+ 
			        "PPA.NOM_COMPLETO NOM_AVAL\n"+
			        "FROM V_CREDITO C, \n"+
			        "SIM_PRESTAMO_PARTICIPANTE PA,\n"+  
			        "RS_GRAL_PERSONA PPA\n"+
			        "WHERE C.CVE_GPO_EMPRESA ='" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND C.CVE_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND C.CVE_PRESTAMO = '" + (String)request.getParameter("CvePrestamo") + "'\n"+
			        "AND C.APLICA_A = 'INDIVIDUAL'\n"+ 
			        "AND PA.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA\n"+   
			        "AND PA.CVE_EMPRESA (+)= C.CVE_EMPRESA\n"+ 
			        "AND PA.ID_PRESTAMO (+)= C.ID_PRESTAMO\n"+ 
			        "AND PA.CVE_TIPO_PERSONA (+)= 'AVAL'\n"+ 
			        "AND PPA.CVE_GPO_EMPRESA (+)= PA.CVE_GPO_EMPRESA\n"+   
			        "AND PPA.CVE_EMPRESA (+)= PA.CVE_EMPRESA\n"+ 
			        "AND PPA.ID_PERSONA (+)= PA.ID_PERSONA\n"; 
							
							
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReportePrestamoEmpleados3.jasper");
		//parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/reportes/SimReportePrestamoEmpleados1.jasper"));
		//parametros.put("Subreporte2", contextoServlet.getRealPath("/Reportes/Sim/reportes/SimReportePrestamoEmpleados2.jasper"));
		parametros.put("NombreReporte", "rep"+sClave);
		                             
		
		return parametros;		
	}
}