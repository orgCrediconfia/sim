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
 * Realiza la consulta para obtener los datos que ser�n utilizados para generar el reporte de saldos y antiguedad de capital vencido.
 */
public class SimReporteAntiguedadREP implements ReporteControlIN {

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

		String sIdRegional = request.getParameter("IdRegional");
		String sIdSucursal = request.getParameter("IdSucursal");
		String sCveUsuario = request.getParameter("CveUsuario");
		
		String sSql = 	"SELECT  \n"+
		
		"V.CVE_GPO_EMPRESA, \n"+ 
		"V.CVE_EMPRESA, \n"+ 
		"ID_PRESTAMO, \n"+ 
		"CVE_PRESTAMO, \n"+ 
		"F_MEDIO, \n"+ 
		"APLICA_A, \n"+ 
		"CVE_NOMBRE, \n"+ 
		"NOMBRE, \n"+ 
		"NUM_INTEGRANTES, \n"+ 
		"ID_SUCURSAL, \n"+ 
		"NOM_SUCURSAL, \n"+
		"ID_REGIONAL, \n"+ 
		"NOM_REGIONAL, \n"+
		"CVE_ASESOR_CREDITO, \n"+
		"ID_PERSONA, \n"+ 
		"NOM_COMPLETO_ASESOR, \n"+
		"ID_ETAPA_PRESTAMO, \n"+ 
		"MONTO_AUTORIZADO, \n"+ 
		"CARGO_INICIAL, \n"+ 
		"NVL(MONTO_AUTORIZADO,0) + NVL(CARGO_INICIAL,0) MONTO_PRESTADO, \n"+ 
		"NUM_DIAS_ANTIGUEDAD NUM_DIAS_ATRASO_ACTUAL, \n"+ 
		"NUM_DIAS_ATRASO_MAX, \n"+
		"CVE_CATEGORIA_ATRASO, \n"+
		"F_PROX_PAGO, \n"+ 
		"F_ULT_PAGO_REALIZADO, \n"+
		"F_ULT_AMORTIZACION, \n"+ 
		"VENCIDO_INTERES, \n"+
		"VENCIDO_CAPITAL, \n"+
		"VENCIDO_SEGURO, \n"+
		"VENCIDO_RECARGO, \n"+
		"NVL(VENCIDO_INTERES,0) + NVL(VENCIDO_CAPITAL,0) + NVL(VENCIDO_SEGURO,0) + NVL(VENCIDO_RECARGO,0) VENCIDO_TOTAL, \n"+
		"SALDO_INTERES, \n"+
		"SALDO_CAPITAL, \n"+
		"SALDO_SEGURO, \n"+
		"SALDO_RECARGO, \n"+
		"NVL(SALDO_INTERES,0) + NVL(SALDO_CAPITAL,0) + NVL(SALDO_SEGURO,0) + NVL(SALDO_RECARGO,0) SALDO_TOTAL, \n"+
		"TRUNC((NVL(SALDO_INTERES,0) + NVL(SALDO_CAPITAL,0) + NVL(SALDO_SEGURO,0) + NVL(SALDO_RECARGO,0)) / CUOTA,2) SALDO_CUOTA \n"+
		"FROM V_ANTIGUEDADES V, \n"+
		"PFIN_PARAMETRO P \n"+
		"WHERE V.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
		"AND V.CVE_EMPRESA = P.CVE_EMPRESA \n";
	 
		
		if (!sIdSucursal.equals("")){
			sSql = sSql + "AND NOM_SUCURSAL = '" + (String)request.getParameter("IdSucursal") + "'\n";
		}
	
		if (!sIdRegional.equals("")){
			sSql = sSql + "AND NOM_REGIONAL = '" + (String)request.getParameter("IdRegional") + "'\n";
		}
		
		if (!sCveUsuario.equals("")){
			sSql = sSql + "AND NOM_COMPLETO_ASESOR = '" + (String)request.getParameter("CveUsuario") + "'\n";
		}
							
	    String sTipoReporte = request.getParameter("TipoReporte");
		parametros.put("Sql", sSql);
		parametros.put("NomRegional", sIdRegional);
		parametros.put("NomSucursal", sIdSucursal);
		parametros.put("CveUsuario", sCveUsuario);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteAntiguedad.jasper");
		parametros.put("NombreReporte", "Antiguedades");
		                             
		
		return parametros;		
	}
}