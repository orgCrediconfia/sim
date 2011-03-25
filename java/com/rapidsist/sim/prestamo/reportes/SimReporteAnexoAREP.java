/**
 * Sistema Central de Control.
 *
 */

package com.rapidsist.sim.prestamo.reportes;

import com.rapidsist.portal.cliente.reportes.ReporteControlIN;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import com.rapidsist.comun.util.Fecha2;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import com.rapidsist.portal.catalogos.CatalogoSL;
import javax.naming.Context;
import com.rapidsist.comun.bd.Registro;

/**
 * Esta clase se encarga de administrar la operaciï¿½n consulta del Reporte
 * Anexo A Esta clase es llamada por el servlet ProcesaReporteS.
 */

public class SimReporteAnexoAREP implements ReporteControlIN {

	public Map getParametros(Registro parametrosCatalogo,
			HttpServletRequest request, CatalogoSL catalogoSL,
			Context contextoServidor, ServletContext contextoServlet)
			throws Exception {
		Map parametros = new HashMap();
		String sIdPrestamo = request.getParameter("IdPrestamo");
		System.out.println("Id Prestamo:" + sIdPrestamo);
		parametrosCatalogo.addDefCampo("ID_PRESTAMO", sIdPrestamo);
		String sGarantiaDescripcion = "";
		String sGarantiaValorComercial = "";
		String sGarantiaNumeroFacturaEscritura = "";
		String sGarantiaFechaFacturaEscritura = "";
		// Se obtine una sola garantia del prestamo
		Registro registroGarantia = catalogoSL.getRegistro(
				"SimGarantiasPrestamo", parametrosCatalogo);
		// VERIFICA SI ENCONTRO LA GARANTIA PARA EL PRESTAMO
		if (registroGarantia != null) {
			sGarantiaDescripcion = (String) registroGarantia
					.getDefCampo("DESCRIPCION");
			sGarantiaValorComercial = (String) registroGarantia
					.getDefCampo("VALOR_COMERCIAL");
			sGarantiaNumeroFacturaEscritura = (String) registroGarantia
					.getDefCampo("NUMERO_FACTURA_ESCRITURA");
			sGarantiaFechaFacturaEscritura = (String) registroGarantia
					.getDefCampo("FECHA_FACTURA_ESCRITURA");
		}
		// System.out.println("sGarantiaDescripcion:"+sGarantiaDescripcion);
		// System.out.println("sGarantiaValorComercial:"+sGarantiaValorComercial);
		// System.out.println("sGarantiaNumeroFacturaEscritura:"+sGarantiaNumeroFacturaEscritura);
		// System.out.println("sGarantiaFechaFacturaEscritura:"+sGarantiaFechaFacturaEscritura);
		// Se obtiene un solo giro por persona
		Registro registroGiro = catalogoSL.getRegistro("SimGiroPrestamo",
				parametrosCatalogo);
		String sGiroPersona = (String) registroGiro.getDefCampo("GIRO");
		// SE OBTIENE LOS OBLIGADOS SOLIDARIOS Y EL GARANTE DEL PRESTAMO
		LinkedList lParticipantes = catalogoSL.getRegistros("SimParticipanteC",
				parametrosCatalogo);
		Iterator iteratorParticipantes = lParticipantes.iterator();
		String sObligadoUno = "";
		String sObligadoUnoDomicilio = "";
		String sObligadoDos = "";
		String sObligadoDosDomicilio = "";
		String sGarante = "";
		String sGaranteDomicilio = "";
		// ITEREA TODOS LOS PARTICIPANTES DEL CREDITO
		while (iteratorParticipantes.hasNext()) {
			Registro registro = (Registro) iteratorParticipantes.next();
			String sTipoPersona = (String) registro
					.getDefCampo("CVE_TIPO_PERSONA");
			if (sTipoPersona.equals("OBLIGADO")) {
				sObligadoUno = (String) registro.getDefCampo("NOM_COMPLETO");
				sObligadoUnoDomicilio = (String) registro
						.getDefCampo("DIRECCION");
			} else if (sTipoPersona.equals("OBLIGADO 2")) {
				sObligadoDos = (String) registro.getDefCampo("NOM_COMPLETO");
				sObligadoDosDomicilio = (String) registro
						.getDefCampo("DIRECCION");
			} else if (sTipoPersona.equals("GARANTE")) {
				sGarante = (String) registro.getDefCampo("NOM_COMPLETO");
				sGaranteDomicilio = (String) registro.getDefCampo("DIRECCION");
			}
		}
		String sSql = "SELECT \n"
				+ "C.CVE_GPO_EMPRESA, \n"
				+ "C.CVE_EMPRESA, \n"
				+ "C.ID_PRESTAMO, \n"
				+ "C.CVE_NOMBRE ID_CLIENTE, \n"
				+ "C.ID_PRODUCTO, \n"
				+ "C.NUM_CICLO, \n"
				+ "TO_CHAR(C.FECHA_ENTREGA, 'DD') ||' de '|| RTRIM(TO_CHAR(C.FECHA_ENTREGA, 'MONTH')) ||' de '||TO_CHAR(C.FECHA_ENTREGA, 'YYYY') FECHA_ENTREGA, \n"
				+ "C.NOMBRE NOM_COMPLETO, \n"
				+ "TO_CHAR(C.MONTO_AUTORIZADO + C.CARGO_INICIAL,'999,999,999.99') MONTO_AUTORIZADO, \n"
				+ "CANTIDADES_LETRAS(C.MONTO_AUTORIZADO) MONTO_AUTORIZADO_LETRAS, \n"
				+ "C.VALOR_TASA, \n"
				+ "C.VALOR_TASA * 12 AS TASA_ANUAL, \n"
				+ "C.PERIODICIDAD_PRODUCTO, \n"
				+ "C.CARGO_INICIAL CONSULTA_BURO, \n"
				+ "NVL(NVL(CA.CARGO_INICIAL,CA.PORCENTAJE_MONTO/100*C.MONTO_AUTORIZADO),0) COMISION_APERTURA, \n"
				+ "TO_CHAR(C.FECHA_FIN, 'DD') ||' de '|| RTRIM(TO_CHAR(C.FECHA_FIN, 'MONTH')) ||' de '||TO_CHAR(C.FECHA_FIN, 'YYYY') FECHA_FIN, \n"
				+ "C.PLAZO, \n"
				+ "C.ID_SUCURSAL, \n"
				+ "C.DIRECCION_SUCURSAL, \n"
				+ "C.MONTO_FIJO_PERIODO, \n"
				+ "CANTIDADES_LETRAS(C.MONTO_FIJO_PERIODO) MONTO_FIJO_PERIODO_LETRAS, \n"
				+ "PA.ID_PERSONA ID_AVAL, \n"
				+ "DECODE(PPA.NOM_COMPLETO,'',' ',PPA.NOM_COMPLETO) NOM_AVAL, \n"
				+ "PD.ID_PERSONA ID_DEPOSITARIO, \n"
				+ "DECODE(PPD.NOM_COMPLETO,'',' ',PPD.NOM_COMPLETO) NOM_DEPOSITARIO, \n"
				+ "PG.ID_PERSONA ID_GARANTE, \n"
				+ "DECODE(PPG.NOM_COMPLETO,'',' ',PPG.NOM_COMPLETO) NOM_GARANTE \n"
				+ "FROM \n" + "V_CREDITO C, \n"
				+ "SIM_PRESTAMO_CARGO_COMISION CA, \n"
				+ "SIM_PRESTAMO_PARTICIPANTE PA, \n"
				+ "RS_GRAL_PERSONA PPA, \n"
				+ "SIM_PRESTAMO_PARTICIPANTE PD, \n"
				+ "RS_GRAL_PERSONA PPD, \n"
				+ "SIM_PRESTAMO_PARTICIPANTE PG, \n" + "RS_GRAL_PERSONA PPG \n"
				+ "WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"
				+ "AND C.CVE_EMPRESA = 'CREDICONFIA' \n"
				+ "AND C.ID_PRESTAMO = '"
				+ request.getParameter("IdPrestamo")
				+ "' \n"
				+ "AND C.APLICA_A = 'INDIVIDUAL' \n"
				+ "AND CA.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"
				+ "AND CA.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"
				+ "AND CA.ID_PRESTAMO (+)= C.ID_PRESTAMO \n"
				+ "AND CA.ID_CARGO_COMISION (+)= '11' \n"
				+ "AND PA.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"
				+ "AND PA.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"
				+ "AND PA.ID_PRESTAMO (+)= C.ID_PRESTAMO \n"
				+ "AND PA.CVE_TIPO_PERSONA (+)= 'AVAL' \n"
				+ "AND PPA.CVE_GPO_EMPRESA (+)= PA.CVE_GPO_EMPRESA \n"
				+ "AND PPA.CVE_EMPRESA (+)= PA.CVE_EMPRESA \n"
				+ "AND PPA.ID_PERSONA (+)= PA.ID_PERSONA \n"
				+ "AND PD.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"
				+ "AND PD.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"
				+ "AND PD.ID_PRESTAMO (+)= C.ID_PRESTAMO \n"
				+ "AND PD.CVE_TIPO_PERSONA (+)= 'DEPOSIT' \n"
				+ "AND PPD.CVE_GPO_EMPRESA (+)= PD.CVE_GPO_EMPRESA \n"
				+ "AND PPD.CVE_EMPRESA (+)= PD.CVE_EMPRESA \n"
				+ "AND PPD.ID_PERSONA (+)= PD.ID_PERSONA \n"
				+ "AND PG.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"
				+ "AND PG.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"
				+ "AND PG.ID_PRESTAMO (+)= C.ID_PRESTAMO \n"
				+ "AND PG.CVE_TIPO_PERSONA (+)= 'GARANTE' \n"
				+ "AND PPG.CVE_GPO_EMPRESA (+)= PG.CVE_GPO_EMPRESA \n"
				+ "AND PPG.CVE_EMPRESA (+)= PG.CVE_EMPRESA \n"
				+ "AND PPG.ID_PERSONA (+)= PG.ID_PERSONA \n";
		System.out.println("sSql" + sSql);
		parametros.put("Sql", sSql);
		// Parametros para obtener una sola garantia de un prestamo
		parametros.put("GarantiaDescripcion", sGarantiaDescripcion);
		parametros.put("GarantiaValorComercial", sGarantiaValorComercial);
		parametros.put("GarantiaNumeroFacturaEscritura",
				sGarantiaNumeroFacturaEscritura);
		parametros.put("GarantiaFechaFacturaEscritura",
				sGarantiaFechaFacturaEscritura);
		// Parametros para obtener un giro por persona
		parametros.put("GiroPersona", sGiroPersona);
		// Parámetros para obtener los obligados solidarios y el garante
		parametros.put("ObligadoUno", sObligadoUno);
		parametros.put("ObligadoUnoDomicilio", sObligadoUnoDomicilio);
		parametros.put("ObligadoDos", sObligadoDos);
		parametros.put("ObligadoDosDomicilio", sObligadoDosDomicilio);
		parametros.put("GaranteUno", sGarante);
		parametros.put("GaranteDomicilio", sGaranteDomicilio);
		parametros.put("PathLogotipo", contextoServlet
				.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		parametros.put("FechaReporte", Fecha2
				.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte",
				"/Reportes/Sim/prestamo/SimReporteAnexoANuevo.jasper");
		parametros
				.put(
						"Subreporte1",
						contextoServlet
								.getRealPath("/Reportes/Sim/prestamo/SimReporteAnexoANuevo_Sub1.jasper"));
		parametros.put("Subreporte2", contextoServlet
				.getRealPath("/Reportes/Sim/prestamo/SimReporteAnexoA.jasper"));
		parametros
				.put(
						"Subreporte3",
						contextoServlet
								.getRealPath("/Reportes/Sim/prestamo/SimReporteAnexoA2.jasper"));
		return parametros;
	}
}
