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
public class SimReporteCaracteristicasREP implements ReporteControlIN {

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
				        "PG.NUM_CICLO, \n"+
				        "NVL(PG.FECHA_REAL, PG.FECHA_ENTREGA) FECHA_INICIO,\n"+
				        "MAX(T.FECHA_AMORTIZACION) FECHA_FIN,\n"+
				        "PG.DIA_SEMANA_PAGO,\n"+
				        "DECODE(PG.ID_PERIODICIDAD_PRODUCTO, '1','A','2','S','4','B','5','M','6','Q','7','C','8','S','16','B','17','T'),\n"+
				        "PG.CVE_METODO,\n"+
				        "PG.ID_PRODUCTO,\n"+
				        "'Grupal' AS TIPO_PRESTAMO,\n"+
				        "PG.FACTOR_TASA_RECARGO,\n"+
				        "PG.MONTO_FIJO_PERIODO * G.NUM_INTEGRANTES,\n"+
				        "S.ID_UNIDAD,\n"+
				        "U.VALOR\n"+
				        "FROM \n"+
				        "SIM_PRESTAMO_GRUPO PG,\n"+
				        "SIM_GRUPO G,\n"+
				        "RS_GRAL_PERSONA P,\n"+
				        "V_TABLA_AMORTIZACION_GRUPAL T,\n"+
				        "SIM_PRESTAMO_GPO_CARGO S,\n"+
				        "SIM_CAT_UNIDAD U\n"+
				        "WHERE PG.CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
				        "AND PG.CVE_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n"+
				        "AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA\n"+
				        "AND G.CVE_EMPRESA = PG.CVE_EMPRESA\n"+
				        "AND G.ID_GRUPO = PG.ID_GRUPO\n"+
				        "AND P.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA\n"+
				        "AND P.CVE_EMPRESA = G.CVE_EMPRESA\n"+
				        "AND P.ID_PERSONA = G.ID_COORDINADOR \n"+
				        "AND T.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA\n"+
				        "AND T.CVE_EMPRESA = PG.CVE_EMPRESA\n"+
				        "AND T.ID_PRESTAMO_GRUPO = PG.ID_PRESTAMO_GRUPO\n"+ 
				        "AND S.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA\n"+
				        "AND S.CVE_EMPRESA = PG.CVE_EMPRESA\n"+
				        "AND S.ID_PRESTAMO_GRUPO = PG.ID_PRESTAMO_GRUPO\n"+ 
				        "AND S.ID_CARGO_COMISION = '8'\n"+
				        "AND U.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA\n"+
				        "AND U.CVE_EMPRESA = S.CVE_EMPRESA\n"+
				        "AND U.ID_UNIDAD = S.ID_UNIDAD \n"+
				        "GROUP BY \n"+
				        "PG.ID_PRESTAMO_GRUPO,\n"+ 
				        "PG.ID_GRUPO, \n"+
				        "PG.NUM_CICLO, \n"+
				        "NVL(PG.FECHA_REAL, PG.FECHA_ENTREGA),\n"+ 
				        "PG.DIA_SEMANA_PAGO, \n"+
				        "DECODE(PG.ID_PERIODICIDAD_PRODUCTO, '1','A','2','S','4','B','5','M','6','Q','7','C','8','S','16','B','17','T'),\n"+ 
				        "PG.CVE_METODO, \n"+
				        "PG.ID_PRODUCTO, \n"+
				        "'Grupal', \n"+
				        "PG.FACTOR_TASA_RECARGO,\n"+ 
				        "PG.MONTO_FIJO_PERIODO * G.NUM_INTEGRANTES,\n"+ 
				        "S.ID_UNIDAD, \n"+
				        "U.VALOR\n"+
				        "UNION ALL\n"+
				        "SELECT \n"+
				        "P.ID_PRESTAMO,\n"+ 
				        "P.ID_CLIENTE, \n"+
				        "P.NUM_CICLO, \n"+
				        "NVL(P.FECHA_REAL, P.FECHA_ENTREGA) FECHA_INICIO,\n"+
				        "MAX(T.FECHA_AMORTIZACION) FECHA_FIN,\n"+
				        "P.DIA_SEMANA_PAGO,\n"+
				        "DECODE(P.ID_PERIODICIDAD_PRODUCTO, '1','A','2','S','4','B','5','M','6','Q','7','C','8','S','16','B','17','T'),\n"+
				        "P.CVE_METODO,\n"+
				        "P.ID_PRODUCTO,\n"+
				        "'Individual' AS TIPO_PRESTAMO,\n"+
				        "P.FACTOR_TASA_RECARGO,\n"+
				        "P.MONTO_FIJO_PERIODO,\n"+
				        "S.ID_UNIDAD,\n"+
				        "U.VALOR\n"+
				        "FROM \n"+
				        "SIM_PRESTAMO P,\n"+
				        "SIM_TABLA_AMORTIZACION T,\n"+
				        "SIM_PRESTAMO_CARGO_COMISION S,\n"+
				        "SIM_CAT_UNIDAD U\n"+
				        "WHERE P.CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
				        "AND P.CVE_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n"+
				        "AND P.ID_GRUPO IS NULL\n"+
				        "AND T.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA\n"+
				        "AND T.CVE_EMPRESA = P.CVE_EMPRESA\n"+
				        "AND T.ID_PRESTAMO = P.ID_PRESTAMO\n"+
				        "AND S.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA\n"+
				        "AND S.CVE_EMPRESA = P.CVE_EMPRESA\n"+
				        "AND S.ID_PRESTAMO = P.ID_PRESTAMO\n"+
				        "AND S.ID_CARGO_COMISION = '8'\n"+
				        "AND U.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA\n"+
				        "AND U.CVE_EMPRESA = S.CVE_EMPRESA\n"+
				        "AND U.ID_UNIDAD = S.ID_UNIDAD\n"+
				        "GROUP BY \n"+
				        "P.ID_PRESTAMO,\n"+ 
				        "P.ID_CLIENTE, \n"+
				        "P.NUM_CICLO, \n"+
				        "NVL(P.FECHA_REAL, P.FECHA_ENTREGA),\n"+ 
				        "P.DIA_SEMANA_PAGO, \n"+
				        "DECODE(P.ID_PERIODICIDAD_PRODUCTO, '1','A','2','S','4','B','5','M','6','Q','7','C','8','S','16','B','17','T'),\n"+ 
				        "P.CVE_METODO, \n"+
				        "P.ID_PRODUCTO, \n"+
				        "'Individual', \n"+
				        "P.FACTOR_TASA_RECARGO,\n"+ 
				        "P.MONTO_FIJO_PERIODO, \n"+
				        "S.ID_UNIDAD, \n"+
				        "U.VALOR \n";
						
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("Portales/Sam/img/Imagentsys1.gif"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteCaracteristicas.jasper");
		parametros.put("NombreReporte", "Reporte"+sClaveGrupo);
		
		return parametros;		
	}
}
