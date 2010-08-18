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
public class SimReportePrestamoEmpleadosREP implements ReporteControlIN {

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
							"C.CVE_GPO_EMPRESA,\n"+
							"C.CVE_EMPRESA,\n"+
							"C.ID_PRESTAMO,\n"+
							"C.CVE_PRESTAMO,\n"+
							"C.CVE_NOMBRE,\n"+
							"C.NOMBRE,\n"+
							"P.RFC,\n"+
							"DECODE(DC.CALLE||' '||DC.NUMERO_INT||' '||DC.NOM_ASENTAMIENTO||' '||DC.NOM_DELEGACION||' '||DC.NOM_ESTADO||' '||'C.P.'||' '||DC.CODIGO_POSTAL,'     C.P. ',' ',DC.CALLE||' '||DC.NUMERO_INT||' '||DC.NOM_ASENTAMIENTO||' '||DC.NOM_DELEGACION||' '||DC.NOM_ESTADO||' '||'C.P.'||' '||DC.CODIGO_POSTAL) DIRECCION_CLIENTE,\n"+
							"C.NOM_SUCURSAL,\n"+
							"TO_CHAR(NVL(C.MONTO_AUTORIZADO + C.CARGO_INICIAL,0),'999,999,999.99') PRESTAMO_SOLICITADO,\n"+
							"NVL(TRUNC(MONTHS_BETWEEN(SYSDATE,U.FECHA_INGRESO),2),0) ANTIGUEDAD\n"+
							"FROM\n"+
							"V_CREDITO C,\n"+
							"RS_GRAL_PERSONA P,\n"+
							"RS_GRAL_DOMICILIO DC,\n"+
							"RS_GRAL_USUARIO U\n"+
							"WHERE C.CVE_GPO_EMPRESA ='" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND C.CVE_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND C.CVE_PRESTAMO = '" + (String)request.getParameter("CvePrestamo") + "'\n"+
							"AND C.APLICA_A = 'INDIVIDUAL'\n"+
							"AND P.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA\n"+
							"AND P.CVE_EMPRESA = C.CVE_EMPRESA\n"+
							"AND P.ID_PERSONA = C.CVE_NOMBRE\n"+
							"AND DC.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA\n"+   
							"AND DC.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
							"AND DC.IDENTIFICADOR (+)= C.CVE_NOMBRE \n"+
							"AND DC.DOMICILIO_FISCAL (+)= 'V'\n"+ 
							"AND U.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA\n"+
							"AND U.CVE_EMPRESA (+)= P.CVE_EMPRESA\n"+
							"AND U.ID_PERSONA (+)= P.ID_PERSONA\n";
							
							
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReportePrestamoEmpleados.jasper");
		parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/reportes/SimReportePrestamoEmpleados1.jasper"));
		//parametros.put("Subreporte2", contextoServlet.getRealPath("/Reportes/Sim/reportes/SimReportePrestamoEmpleados2.jasper"));
		parametros.put("NombreReporte", "rep"+sClave);
		                             
		
		return parametros;		
	}
}