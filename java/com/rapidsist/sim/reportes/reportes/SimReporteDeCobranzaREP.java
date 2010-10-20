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
public class SimReporteDeCobranzaREP implements ReporteControlIN {

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

		//String sCveGpoEmpresa = request.getParameter("CveGpoEmpresa");
		//String sCveEmpresa = request.getParameter("CveEmpresa");
		String sClave = request.getParameter("CvePrestamoGpo");
		
		String sSql = 	"SELECT\n"+
							"FECHA_REPORTE,\n"+
							"ID_REGIONAL,\n"+
							"NOM_REGIONAL,\n"+
							"ID_SUCURSAL,\n"+
							"NOM_SUCURSAL,\n"+
							"CVE_ASESOR,\n"+
							"NOM_ASESOR,\n"+
							"CVE_PRESTAMO,\n"+
							"NUM_INTEGRANTES,\n"+
							"CATEGORIA,\n"+
							"NOM_GRUPO,\n"+
							"MONTO_PRESTADO,\n"+
							"DIAS_ANTIGUEDAD,\n"+
							"F_PROX_PAGO,\n"+
							"SALDO_LIQUIDADO_CUOTA,\n"+
							"ID_COORDINADOR_GRUPO,\n"+
							"TELEFONO_COORDINADOR,\n"+
							"CAPITAL_PROX_PAGO,\n"+
							"INTERESES_PROX_PAGO,\n"+
							"SEGURO_PROX_PAGO,\n"+
							"RECARGOS_PROX_PAGO,\n"+
							"TOTAL_PROX_PAGO,\n"+
							"INSOLUTO,\n"+
							"INTERESES,\n"+
							"SEGURO,\n"+
							"TOTAL_SALDO_LIQUIDAR\n"+
							"FROM\n"+
							"V_REPORTE_SEGUIMIENTO_COBRANZA\n";
		
		if (!request.getParameter("CvePrestamoGpo").equals("")) {
			sSql = sSql + " AND UPPER(CVE_PRESTAMO) LIKE '%" + ((String) request.getParameter("CvePrestamoGpo")).toUpperCase()  + "%' \n";
		}
							
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteDeCobranza.jasper");
		parametros.put("NombreReporte", "rep"+sClave);
		                             
		
		return parametros;		
	}
}