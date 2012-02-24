/**
 * Sistema Central de Control.
 *
 */

package com.rapidsist.sim.caja.reportes;

import com.rapidsist.portal.cliente.reportes.ReporteControlIN;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import com.rapidsist.comun.util.Fecha2;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import com.rapidsist.portal.catalogos.CatalogoSL;
import javax.naming.Context;
import com.rapidsist.comun.bd.Registro;
import javax.servlet.http.HttpSession;
import com.rapidsist.portal.configuracion.Usuario;

/**
 * Esta clase se encarga de administrar la operaci�n consulta del Reporte Anexo A
 * Esta clase es llamada por el servlet ProcesaReporteS.
 */

public class SimCajaDesembolsoGrupalREP implements ReporteControlIN {

	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();

		//OBTIENE LOS DATOS DEL USUARIO
		HttpSession session = request.getSession(true);
		Usuario usuario = (Usuario)session.getAttribute("Usuario");
		String sCveUsuario = usuario.sNomCompleto;
		parametros.put("CveUsuario", sCveUsuario);
		
		String sSql =   "SELECT \n"+
						"T.CVE_GPO_EMPRESA, \n"+
						"T.CVE_EMPRESA, \n"+
						"T.ID_MOVIMIENTO_OPERACION, \n"+
						"T.ID_CLIENTE, \n"+
						"T.ID_GRUPO, \n"+
						"G.NOM_GRUPO, \n"+
						"T.NUM_CICLO, \n"+ 
						"T.ID_PRESTAMO, \n"+
						"B.NOM_COMPLETO, \n"+
						"-T.MONTO MONTO, \n"+
						"CANTIDADES_LETRAS(MONTO) MONTO_AUTORIZADO_LETRAS, \n"+
						"NVL(P.FECHA_ENTREGA,P.FECHA_REAL) FECHA, \n"+
						"TO_CHAR(SYSDATE, 'HH:MI AM') AS HORA, \n"+
						"T.ID_SUCURSAL, \n"+
						"A.NOM_SUCURSAL, \n"+
						"T.ID_CAJA, \n"+
						"D.CALLE||' '||D.NUMERO_INT||' '||'COL.'||' '||D.NOM_ASENTAMIENTO||' '||'C.P.'||' '||D.CODIGO_POSTAL||','||' '||D.NOM_DELEGACION||','||' '||D.NOM_ESTADO DOMICILIO \n"+
						"FROM SIM_CAJA_TRANSACCION T, \n"+
						"SIM_PRESTAMO P, \n"+
						"SIM_SUCURSAL_CAJA S, \n"+
						"RS_GRAL_PERSONA B, \n"+
						"SIM_CAT_SUCURSAL A, \n"+
						"RS_GRAL_DOMICILIO D, \n"+
						"SIM_GRUPO G \n"+
						"WHERE T.CVE_GPO_EMPRESA = 'SIM' \n"+
						"AND T.CVE_EMPRESA = 'CREDICONFIA' \n"+
						"AND T.ID_TRANSACCION_GRUPO = '" + request.getParameter("IdTransaccionGrupo")+"' \n"+
						"AND T.CVE_MOVIMIENTO_CAJA = 'DESGPO' \n"+
						"AND S.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
						"AND S.CVE_EMPRESA = T.CVE_EMPRESA \n"+
						"AND S.ID_SUCURSAL = T.ID_SUCURSAL \n"+
						"AND S.ID_SUCURSAL||'-'||S.ID_CAJA = '" + request.getParameter("IdCaja")+"' \n"+
						"AND P.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND P.ID_PRESTAMO (+)= T.ID_PRESTAMO \n"+
						"AND B.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
						"AND B.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND B.ID_PERSONA (+)= T.ID_CLIENTE \n"+
						"AND A.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
						"AND A.CVE_EMPRESA = T.CVE_EMPRESA \n"+
						"AND A.ID_SUCURSAL = T.ID_SUCURSAL \n"+
						"AND D.CVE_GPO_EMPRESA (+)= A.CVE_GPO_EMPRESA \n"+
						"AND D.CVE_EMPRESA (+)= A.CVE_EMPRESA \n"+
						"AND D.ID_DOMICILIO (+)= A.ID_DIRECCION \n"+
						"AND G.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
						"AND G.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
						"AND G.ID_GRUPO (+)= T.ID_GRUPO \n";
		
		String sReimpresion = request.getParameter("Reimpresion");
		System.out.println(sSql);
		parametros.put("Sql", sSql);
		
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		
		parametros.put("FechaReporte", Fecha2.formatoTiempo24(new Date()));
		
		parametros.put("NomReporte", "/Reportes/Sim/caja/SimCajaDesembolsoGrupal.jasper");
	
		parametros.put("Reimpresion", sReimpresion);
		return parametros;		
	}
}