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
 * Realiza la consulta para obtener los datos que ser�n utilizados para generar
 * el reporte de los int. devengados en cr�ditos grupales.
 */
public class SimReportesEstadoCuentaCuentaREP implements ReporteControlIN {

	/**
	 * Obtiene la consulta en la base de datos y par�metros que ser�n utilizados
	 * por el reporte.
	 * 
	 * @param parametrosCatalogo
	 *            Par�metros que se le env�an al m�todo .
	 * @param request
	 *            Objeto que provee de informaci�n al servlet sobre el request
	 *            del cliente. El contenedor de servlets crea un objeto
	 *            HttpServletRequest y lo env�a como un par�metro a este m�todo.
	 * @param catalogoSL
	 *            Instancia del Ejb CatalogoSL que ejecuta en la base de datos
	 *            las operaciones especificadas en la clase CON.
	 * @param contextoServidor
	 *            Objeto que contiene informaci�n acerca del entorno del
	 *            servidor de aplicaciones.
	 * @param contextoServlet
	 *            Objeto que contiene informaci�n acerca del entorno del
	 *            servlet.
	 * @return Map objeto que contiene los datos para generar el reporte.
	 * @throws SQLException
	 *             Si se genera un error al accesar la base de datos.
	 */
	public Map getParametros(Registro parametrosCatalogo,
			HttpServletRequest request, CatalogoSL catalogoSL,
			Context contextoServidor, ServletContext contextoServlet)
			throws Exception {
		Map parametros = new HashMap();

		String sCvePrestamo = request.getParameter("CvePrestamo");
	
		String sSql = "SELECT\n" + "MO.ID_CUENTA,\n" + "SP.CVE_PRESTAMO,\n"
				+ "RP.NOM_COMPLETO,\n" + "MO.F_OPERACION,  \n"
				+ "CO.DESC_LARGA,\n" 
				+ "DECODE (CO.CVE_AFECTA_SALDO, 'D', MO.IMP_NETO * -1, MO.IMP_NETO) IMP_NETO\n" 
				+ "FROM\n"
				+ "PFIN_MOVIMIENTO MO,\n" + "PFIN_CAT_OPERACION CO,\n"
				+ "SIM_PRESTAMO SP,\n" + "RS_GRAL_PERSONA RP\n"
				+ "WHERE MO.CVE_GPO_EMPRESA      = '"
				+ parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"
				+ "AND MO.CVE_EMPRESA          = '"
				+ parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n"
				+ "AND CO.CVE_EMPRESA          = MO.CVE_EMPRESA\n"
				+ "AND CO.CVE_GPO_EMPRESA      = MO.CVE_GPO_EMPRESA\n"
				+ "AND CO.CVE_OPERACION        = MO.CVE_OPERACION\n"
				+ "AND SP.CVE_GPO_EMPRESA      = CO.CVE_GPO_EMPRESA\n"
				+ "AND SP.CVE_EMPRESA          = CO.CVE_EMPRESA\n"
				+ "AND SP.ID_CUENTA_REFERENCIA = MO.ID_CUENTA\n"
				+ "AND RP.CVE_GPO_EMPRESA      = SP.CVE_GPO_EMPRESA\n"
				+ "AND RP.CVE_EMPRESA          = SP.CVE_EMPRESA\n"
				+ "AND RP.ID_PERSONA           = SP.ID_CLIENTE\n";
		
		if (request.getParameter("CvePrestamo") != null && !request.getParameter("CvePrestamo").equals("") && !request.getParameter("CvePrestamo").equals("null") ){
			sSql = sSql + "AND SP.CVE_PRESTAMO = '" + request.getParameter("CvePrestamo")+"'  \n";
			parametros.put("CvePrestamo", request.getParameter("CvePrestamo"));
		}
		
		sSql = sSql + "ORDER BY MO.ID_CUENTA, MO.F_OPERACION\n";

		System.out.println(sSql);
		String sTipoReporte = request.getParameter("TipoReporte");
		parametros.put("Sql", sSql);
		parametros.put("CvePrestamo", sCvePrestamo);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteEstadoCuentaCuenta.jasper");
		parametros.put("NombreReporte", "Estado de Cuenta");

		return parametros;
	}
}