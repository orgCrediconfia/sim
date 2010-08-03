/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.OperacionBaja; 
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;

/**
 * Administra los accesos a la base de datos para:
 * 1. Hacer la consulta general del préstamos (grupales e individuales) atráves del método getRegistros.
 * 2. Consultar el detalle de todos los présamos de forma grupal através del método getRegistro.
 * 3. Dar de alta el cliente al que se le va a otorgar el crédito através del método alta.
 */
 
public class SimPrestamoDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionConsultaRegistro, OperacionConsultaTabla  {
	

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT \n"+
				"P.ID_PRESTAMO, \n"+
				"P.CVE_PRESTAMO, \n"+
				"P.FECHA_SOLICITUD, \n"+
				"P.FECHA_ENTREGA, \n"+
				"P.ID_CLIENTE, \n"+
				"P.ID_GRUPO, \n"+
				"C.NOM_COMPLETO, \n"+
				"G.NOM_GRUPO, \n"+
				"P.ID_PRODUCTO, \n"+
				"P.NUM_CICLO, \n"+
				"P.ID_ETAPA_PRESTAMO, \n"+
				"E.NOM_ESTATUS_PRESTAMO \n"+
			"FROM SIM_PRESTAMO P, \n"+
				"RS_GRAL_PERSONA C, \n"+
				"SIM_GRUPO G, \n" +
				"SIM_CAT_ETAPA_PRESTAMO E, \n" +
				"SIM_USUARIO_SUCURSAL US \n"+
			"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND P.ID_PRODUCTO IS NOT NULL \n"+
			"AND C.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
			"AND C.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
			"AND C.ID_PERSONA (+)= P.ID_CLIENTE \n"+
			"AND G.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
			"AND G.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+ 
			"AND G.ID_GRUPO (+)= P.ID_GRUPO \n"+
			"AND E.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
			"AND E.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+ 
			"AND E.ID_ETAPA_PRESTAMO (+)= P.ID_ETAPA_PRESTAMO \n"+
			"AND US.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
            "AND US.CVE_EMPRESA = P.CVE_EMPRESA \n"+
            "AND US.ID_SUCURSAL = P.ID_SUCURSAL \n"+
            "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
			"AND P.ID_GRUPO IS NULL \n";
			
		if (parametros.getDefCampo("ID_PRESTAMO") != null) {
			sSql = sSql + "AND P.ID_PRESTAMO = '" + (String) parametros.getDefCampo("ID_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("ID_PRODUCTO") != null) {
			sSql = sSql + "AND P.ID_PRODUCTO = '" + (String) parametros.getDefCampo("ID_PRODUCTO") + "' \n";
		}
		if (parametros.getDefCampo("NUM_CICLO") != null) {
			sSql = sSql + "AND P.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
		}
		if (parametros.getDefCampo("FECHA_SOLICITUD") != null) {
			sSql = sSql + "AND P.FECHA_SOLICITUD >= TO_DATE('" + (String) parametros.getDefCampo("FECHA_SOLICITUD") + "','DD/MM/YYYY') \n";
		}
		if (parametros.getDefCampo("FECHA_ENTREGA") != null) {
			sSql = sSql + "AND P.FECHA_ENTREGA <= TO_DATE('" + (String) parametros.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') \n";
		}
		if (parametros.getDefCampo("NOM_GRUPO") != null) {
			sSql = sSql + "AND UPPER(G.NOM_GRUPO) LIKE '%" + ((String) parametros.getDefCampo("NOM_GRUPO")).toUpperCase() + "%' \n";
		}
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + " AND UPPER(C.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
		}
		if (parametros.getDefCampo("ID_ETAPA_PRESTAMO") != null) {
			sSql = sSql + "AND P.ID_ETAPA_PRESTAMO = '" + (String) parametros.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
		}
		
		sSql = sSql + "ORDER BY P.ID_PRESTAMO \n";
		
		ejecutaSql();
		return getConsultaLista();
	}

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  "SELECT \n"+
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+
				"P.ID_PRESTAMO, \n"+
				"P.CVE_PRESTAMO, \n"+
				"P.ID_CLIENTE, \n"+
				"C.NOM_COMPLETO, \n"+
				"P.ID_GRUPO, \n"+
				"G.NOM_GRUPO, \n"+
				"P.ID_SUCURSAL, \n"+
				"S.TASA_IVA, \n"+
				"P.FECHA_SOLICITUD, \n"+
				"P.FECHA_ENTREGA, \n"+
				"P.FECHA_REAL, \n"+
				"P.ID_COMITE, \n"+
				"P.ID_ETAPA_PRESTAMO, \n"+
				"CE.NOM_ESTATUS_PRESTAMO, \n"+
				"P.CVE_ASESOR_CREDITO, \n"+
				"NA.NOM_COMPLETO NOMBRE_ASESOR, \n"+
				"P.FECHA_ASESOR, \n"+
				"P.CVE_RECUPERADOR, \n"+
				"NR.NOM_COMPLETO NOMBRE_RECUPERADOR, \n"+
				"P.FECHA_RECUPERADOR, \n"+
				"P.ID_PRODUCTO, \n"+
				"PR.NOM_PRODUCTO, \n"+
				"P.APLICA_A, \n"+
				"PR.B_GARANTIA, \n"+
				"P.NUM_CICLO, \n"+
				"P.CVE_METODO, \n"+
				"P.ID_PERIODICIDAD_PRODUCTO, \n"+
				"P.PLAZO, \n"+
				"P.TIPO_TASA, \n"+
				
				"				DECODE(P.ID_PERIODICIDAD_TASA,NULL,(SELECT ID_PERIODICIDAD \n" +
                "                        FROM SIM_CAT_TASA_REFERENCIA \n" +
                "                        WHERE ID_TASA_REFERENCIA = (SELECT ID_TASA_REFERENCIA FROM SIM_PRESTAMO WHERE ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "')),P.ID_PERIODICIDAD_TASA) ID_PERIODICIDAD_TASA, \n" +
				"DECODE(P.VALOR_TASA,NULL,(SELECT VALOR \n" +
				"                FROM SIM_CAT_TASA_REFER_DETALLE \n" +
				"                WHERE ID_TASA_REFERENCIA = (SELECT ID_TASA_REFERENCIA FROM SIM_PRESTAMO WHERE ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "') \n" +
				"AND FECHA_PUBLICACION = (SELECT FECHA_TASA_REFERENCIA FROM SIM_PRESTAMO WHERE ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "')),P.VALOR_TASA) VALOR_TASA, \n" +			
								
				"P.ID_TASA_REFERENCIA, \n"+
				"P.MONTO_MAXIMO, \n"+
				"PR.MONTO_MINIMO, \n"+
				"P.PORC_FLUJO_CAJA, \n"+
				"P.ID_FORMA_DISTRIBUCION \n"+
				"FROM \n"+
				"SIM_PRESTAMO P, \n"+
				"RS_GRAL_USUARIO UA, \n"+
				"RS_GRAL_PERSONA NA, \n"+
				"RS_GRAL_USUARIO UR, \n"+
				"RS_GRAL_PERSONA NR, \n"+
				"RS_GRAL_PERSONA C, \n"+
				"SIM_GRUPO G, \n"+
				"SIM_PRODUCTO PR, \n"+
				"SIM_CAT_SUCURSAL S, \n"+
				"SIM_CAT_ETAPA_PRESTAMO CE \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND UA.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND UA.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND UA.CVE_USUARIO (+)= P.CVE_ASESOR_CREDITO \n"+
				"AND NA.CVE_GPO_EMPRESA (+)= UA.CVE_GPO_EMPRESA \n"+
				"AND NA.CVE_EMPRESA (+)= UA.CVE_EMPRESA \n"+
				"AND NA.ID_PERSONA (+)= UA.ID_PERSONA \n"+
				"AND UR.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND UR.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND UR.CVE_USUARIO (+)= P.CVE_RECUPERADOR \n"+
				"AND NR.CVE_GPO_EMPRESA (+)= UR.CVE_GPO_EMPRESA \n"+
				"AND NR.CVE_EMPRESA (+)= UR.CVE_EMPRESA \n"+
				"AND NR.ID_PERSONA (+)= UR.ID_PERSONA \n"+
				"AND C.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND C.ID_PERSONA (+)= P.ID_CLIENTE \n"+
				"AND G.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND G.ID_GRUPO (+)= P.ID_GRUPO \n"+
				"AND PR.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND PR.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND PR.ID_PRODUCTO (+)= P.ID_PRODUCTO \n"+
				"AND S.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND S.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND S.ID_SUCURSAL (+)= P.ID_SUCURSAL \n"+
				"AND CE.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND CE.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND CE.ID_ETAPA_PRESTAMO (+)= P.ID_ETAPA_PRESTAMO \n";	
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdPrestamo = "";
		String sCreditosSimultaneos = "";
		String sDeudaMinima = "";
		int iDeudaMinima = 0;
		int iDeuda = 0;
		String sSaldo = "";
		float fSaldo = 0;
		
		//Preguntamos por el parámetro Crédito Simultáneos.
		
		sSql = " SELECT \n" + 
			  " CREDITOS_SIMULTANEOS, \n" +
			  " DEUDA_MINIMA \n" +
			  "	FROM SIM_PARAMETRO_GLOBAL  \n" +
			  "	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			  "	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" ;
		ejecutaSql();
		if (rs.next()){
			sCreditosSimultaneos = rs.getString("CREDITOS_SIMULTANEOS");
			sDeudaMinima = rs.getString("DEUDA_MINIMA");
			iDeudaMinima = (Integer.parseInt(sDeudaMinima));
		}
		
		if (sCreditosSimultaneos.equals("V")){
			//Realiza el crédito.
			
			//OBTENEMOS EL SEQUENCE
			sSql = "SELECT SQ01_SIM_PRESTAMO.nextval as ID_PRESTAMO FROM DUAL";
			ejecutaSql();
			
			if (rs.next()){
				sIdPrestamo = rs.getString("ID_PRESTAMO");
			}

			sSql =  "INSERT INTO SIM_PRESTAMO ( "+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRESTAMO, \n" +
				"FECHA_SOLICITUD, \n" +
				"ID_CLIENTE, \n" +
				"ID_GRUPO, \n" +
				"ID_SUCURSAL, \n" +
				"CVE_ASESOR_FUNDADOR, \n" +
				"CVE_ASESOR_CREDITO, \n" +
				"FECHA_ASESOR_SIST) \n" +
				" VALUES (" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				" " + sIdPrestamo +", \n" +
				"SYSDATE, \n" +
				"'" + (String)registro.getDefCampo("ID_PERSONA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
				"SYSDATE) \n" ;

			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			resultadoCatalogo.Resultado.addDefCampo("ID_PRESTAMO", sIdPrestamo);
			

		}else {
			
			//Consulta si no tiene un crédito vigente.
			
			//Busca todos los creditos del cliente.
			sSql =  "SELECT \n" +
					"P.ID_PRESTAMO \n" +
					"FROM SIM_PRESTAMO P, \n" +
					"SIM_CAT_ETAPA_PRESTAMO E \n" +
					"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
					"AND P.ID_CLIENTE = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" +
					"AND E.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n" +
					"AND E.CVE_EMPRESA = P.CVE_EMPRESA \n" +
					"AND E.ID_ETAPA_PRESTAMO = P.ID_ETAPA_PRESTAMO \n" +
					"AND E.B_ENTREGADO = 'V' \n" ;
			
			ejecutaSql();
			
			while (rs.next()){
				registro.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO")== null ? "": rs.getString("ID_PRESTAMO"));
				
				sSql = "    SELECT  ID_ORDEN_TIPO, ID_ORDEN, ID_PRESTAMO, FECHA_OPERACION, DESCRIPCION, NULL AS IMPORTE, SUM(IMP_DESGLOSE) IMP_DESGLOSE \n"+
			    "    FROM    ( \n"+
			        
			    "    SELECT  9999999999999999 AS ID_ORDEN_TIPO, 1 AS ID_ORDEN, ID_PRESTAMO, (SELECT  F_MEDIO  \n"+
			    "                          FROM    PFIN_PARAMETRO \n"+
			    "                          WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                              AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                              AND CVE_MEDIO       = 'SYSTEM') AS FECHA_OPERACION, \n"+
			    "            'SALDO A LA FECHA' AS DESCRIPCION, NULL AS IMPORTE, ROUND(SUM(IMP_NETO),2) AS IMP_DESGLOSE, CVE_CONCEPTO \n"+
			    "    FROM (         \n"+
			            
			    "        SELECT  1 AS ID_ORDEN_TIPO, 7 AS ID_ORDEN, ID_PRESTAMO, FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
			    "                ROUND(T.IMP_CAPITAL_AMORT * -1,2) AS IMP_NETO, INITCAP(C.DESC_LARGA) AS DESCRIPCION, C.CVE_CONCEPTO, 'I' CVE_AFECTA \n"+
			    "        FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+
			    "        WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			    "            AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "            AND T.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			    "            AND T.FECHA_AMORTIZACION <=(SELECT  F_MEDIO \n"+
			    "                                        FROM    PFIN_PARAMETRO  \n"+
			    "                                        WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                            AND CVE_MEDIO       = 'SYSTEM') \n"+
			    "            AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			    "            AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
			    "            AND C.CVE_CONCEPTO    = 'CAPITA' \n"+
			    "            AND ROUND(T.IMP_CAPITAL_AMORT,2) > 0 \n"+
			                
			    "        UNION ALL \n"+
			        
			    "        SELECT  1 AS ID_ORDEN_TIPO, 2 AS ID_ORDEN, ID_PRESTAMO, FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
			    "                ROUND(T.IMP_INTERES*-1,2) AS IMP_NETO, INITCAP(C.DESC_LARGA) AS DESCRIPCION, \n"+
			    "                C.CVE_CONCEPTO, 'I' CVE_AFECTA \n"+
			    "        FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+
			    "        WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			    "            AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "            AND T.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			    "            AND T.FECHA_AMORTIZACION <= (SELECT  F_MEDIO \n"+
			    "                                        FROM    PFIN_PARAMETRO \n"+
			    "                                        WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                            AND CVE_MEDIO       = 'SYSTEM') \n"+
			    "            AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			    "            AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
			    "            AND C.CVE_CONCEPTO    = 'INTERE' \n"+
			    "            AND ROUND(T.IMP_INTERES,2) > 0 \n"+
			        
			    "        UNION ALL \n"+
			
			    "        SELECT  1 AS ID_ORDEN_TIPO, 2 AS ID_ORDEN, ID_PRESTAMO, FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
			    "                ROUND(NVL(T.IMP_IVA_INTERES,0) * -1,2) AS IMP_NETO, INITCAP(C.DESC_LARGA) AS DESCRIPCION,  \n"+
			    "                C.CVE_CONCEPTO, 'I' CVE_AFECTA \n"+
			    "        FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+
			    "        WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			    "            AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "            AND T.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			    "            AND T.FECHA_AMORTIZACION <= (SELECT  F_MEDIO \n"+
			    "                                        FROM    PFIN_PARAMETRO \n"+
			    "                                        WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                            AND CVE_MEDIO       = 'SYSTEM') \n"+
			    "            AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			    "            AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
			    "            AND C.CVE_CONCEPTO    = 'IVAINT' \n"+
			    "            AND ROUND(NVL(T.IMP_IVA_INTERES,0),2) > 0 \n"+
			
			    "        UNION ALL \n"+
			        
			    "        SELECT  1 AS ID_ORDEN_TIPO, 3 AS ID_ORDEN, ID_PRESTAMO, FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
			    "                ROUND(T.IMP_INTERES_EXTRA * -1,2) AS IMP_NETO, INITCAP(C.DESC_LARGA) AS DESCRIPCION, \n"+
			    "                C.CVE_CONCEPTO, 'I' CVE_AFECTA \n"+
			    "        FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+
			    "        WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			    "            AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "            AND T.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			    "            AND T.FECHA_AMORTIZACION <=(SELECT  F_MEDIO \n"+
			    "                                        FROM    PFIN_PARAMETRO  \n"+
			    "                                        WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                            AND CVE_MEDIO       = 'SYSTEM') \n"+
			    "            AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			    "            AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
			    "            AND C.CVE_CONCEPTO    = 'INTEXT' \n"+
			    "            AND ROUND(T.IMP_INTERES_EXTRA,2) > 0 \n"+
			
			    "        UNION ALL \n"+
			
			            //Muestra el importe de IVA de interés extra a una fecha 
			    "        SELECT  1 AS ID_ORDEN_TIPO, 5 AS ID_ORDEN, ID_PRESTAMO, FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
			    "                ROUND(NVL(T.IMP_IVA_INTERES_EXTRA,0) * -1,2) AS IMP_DESGLOSE, INITCAP(C.DESC_LARGA) AS DESCRIPCION, \n"+
			    "                C.CVE_CONCEPTO, 'I' CVE_AFECTA \n"+
			    "        FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+
			    "        WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			    "            AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "            AND T.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			    "            AND T.FECHA_AMORTIZACION <=(SELECT  F_MEDIO \n"+
			    "                                        FROM    PFIN_PARAMETRO \n"+
			    "                                        WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                            AND CVE_MEDIO       = 'SYSTEM') \n"+
			    "            AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			    "            AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
			    "            AND C.CVE_CONCEPTO    = 'IVAINTEX' \n"+
			    "            AND ROUND(NVL(T.IMP_IVA_INTERES_EXTRA,0),2) > 0 \n"+
			
			    "        UNION ALL \n"+
			
			            //Obtiene los recargos por pago tardío en caso de que apliquen para el préstamo
			    "        SELECT  1 AS ID_ORDEN_TIPO, 6 AS ID_ORDEN, P.ID_PRESTAMO, T.FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
			    "                ROUND(NVL(PC.MONTO_FIJO_PERIODO * -1,0),2) AS IMP_DESGLOSE, INITCAP(C.DESC_LARGA) AS DESCRIPCION, \n"+
			    "                C.CVE_CONCEPTO, 'I' CVE_AFECTA \n"+
			    "        FROM    SIM_TABLA_AMORTIZACION T, SIM_PRESTAMO P, SIM_PRODUCTO_CICLO PC, PFIN_CAT_CONCEPTO C \n"+
			    "        WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			    "            AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "            AND T.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			    "            AND T.FECHA_AMORTIZACION < (SELECT  F_MEDIO \n"+
			    "                                        FROM    PFIN_PARAMETRO \n"+
			    "                                        WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                            AND CVE_MEDIO       = 'SYSTEM') \n"+
			    "            AND T.B_PAGO_PUNTUAL    = 'F' \n"+
			    "            AND T.CVE_GPO_EMPRESA   = P.CVE_GPO_EMPRESA \n"+
			    "            AND T.CVE_EMPRESA       = P.CVE_EMPRESA \n"+
			    "            AND T.ID_PRESTAMO       = P.ID_PRESTAMO \n"+
			    "            AND P.CVE_GPO_EMPRESA   = PC.CVE_GPO_EMPRESA \n"+
			    "            AND P.CVE_EMPRESA       = PC.CVE_EMPRESA \n"+
			    "            AND P.ID_PRODUCTO       = PC.ID_PRODUCTO \n"+
			    "            AND P.NUM_CICLO         = PC.NUM_CICLO \n"+
			    "            AND T.CVE_GPO_EMPRESA   = C.CVE_GPO_EMPRESA \n"+
			    "            AND T.CVE_EMPRESA       = C.CVE_EMPRESA \n"+
			    "            AND 'PAGOTARD'          = C.CVE_CONCEPTO \n"+
			    "            AND PC.ID_TIPO_RECARGO  IN (4,5) \n"+
			    "            AND ROUND(NVL(PC.MONTO_FIJO_PERIODO,0),2) > 0 \n"+
			        
			    "        UNION ALL \n"+
			        
			    "        SELECT  1 AS ID_ORDEN_TIPO, A.ID_ACCESORIO AS ID_ORDEN, T.ID_PRESTAMO, T.FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
			    "                ROUND(A.IMP_ACCESORIO * -1,2) AS IMP_NETO, INITCAP(C.NOM_ACCESORIO) AS DESCRIPCION, \n"+
			    "                DECODE(A.ID_ACCESORIO,6,'COMISVID',7,'COMISGM',8,'COMISDEU') AS CVE_CONCEPTO, \n"+
			    "                'I' CVE_AFECTA \n"+
			    "        FROM    SIM_TABLA_AMORTIZACION T, SIM_TABLA_AMORT_ACCESORIO A, SIM_CAT_ACCESORIO C \n"+
			    "        WHERE   T.CVE_GPO_EMPRESA       = 'SIM' \n"+
			    "            AND T.CVE_EMPRESA           = 'CREDICONFIA' \n"+
			    "            AND T.ID_PRESTAMO           = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			    "            AND T.FECHA_AMORTIZACION    <= (SELECT  F_MEDIO \n"+
			    "                                            FROM    PFIN_PARAMETRO \n"+
			    "                                            WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                                 AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                                 AND CVE_MEDIO       = 'SYSTEM') \n"+
			    "            AND T.CVE_GPO_EMPRESA       = A.CVE_GPO_EMPRESA \n"+
			    "            AND T.CVE_EMPRESA           = A.CVE_EMPRESA \n"+
			    "            AND T.ID_PRESTAMO           = A.ID_PRESTAMO \n"+
			    "            AND T.NUM_PAGO_AMORTIZACION = A.NUM_PAGO_AMORTIZACION \n"+
			    "            AND A.CVE_GPO_EMPRESA       = C.CVE_GPO_EMPRESA \n"+
			    "            AND A.CVE_EMPRESA           = C.CVE_EMPRESA \n"+
			    "            AND A.ID_ACCESORIO          = C.ID_ACCESORIO \n"+
			    "            AND ROUND(A.IMP_ACCESORIO,2)> 0 \n"+
			
			    "    UNION ALL \n"+
			        
			
			
			    "SELECT  ID_ORDEN_TIPO, ID_ORDEN, ID_PRESTAMO, FECHA_OPERACION, IMP_NETO, \n"+
			    "        DESCRIPCION, CVE_CONCEPTO, CVE_AFECTA \n"+
			    "FROM    (    \n"+
			
			    //Muestra el importe de Intereses Moratorios a la fecha 
			    "SELECT  1 AS ID_ORDEN_TIPO, 7 AS ID_ORDEN, A.ID_PRESTAMO, A.FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
			    "        ROUND(SUM(A.IMP_INT_MORATORIO) * -1,2) AS IMP_NETO, INITCAP(C.DESC_LARGA) AS DESCRIPCION, C.CVE_CONCEPTO, \n"+
			    "        'I' AS CVE_AFECTA \n"+
			    "FROM    (    \n"+
			                
			    "    SELECT  P.CVE_GPO_EMPRESA,P.CVE_EMPRESA,P.ID_PRESTAMO, F.F_LIQUIDACION, T.FECHA_AMORTIZACION, \n"+
			    "            T.NUM_PAGO_AMORTIZACION, T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION AS IMP_CAPITAL, \n"+
			                
			    "            CASE WHEN F.F_LIQUIDACION = T.FECHA_AMORTIZACION AND T.NUM_PAGO_AMORTIZACION = 1 THEN \n"+
			    "                0 \n"+
			    "            ELSE \n"+
			    "                CASE WHEN \n"+
			    "                    CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
			    "                        T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                    ELSE \n"+
			    "                        T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                    END > 0 THEN \n"+
			                        
			    "                    CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
			    "                        T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                    ELSE \n"+
			    "                        T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                    END \n"+
			    "                ELSE  \n"+
			    "                    0 \n"+
			    "                END \n"+
			    "            END AS IMP_CAPITAL_DEBE, M.IMP_MOVTOS,  \n"+
			                
			    "            PKG_CREDITO.dametasamoratoriadiaria(P.CVE_GPO_EMPRESA,P.CVE_EMPRESA,P.ID_PRESTAMO) TASA_MORATORIA, \n"+
			                
			    "            CASE WHEN F.F_LIQUIDACION = T.FECHA_AMORTIZACION AND T.NUM_PAGO_AMORTIZACION = 1 THEN \n"+
			    "                0 \n"+
			    "            ELSE \n"+
			    "                CASE WHEN \n"+
			    "                    CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
			    "                        T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                    ELSE \n"+
			    "                        T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                    END > 0 THEN \n"+
			                        
			    "                    CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
			    "                        T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                    ELSE \n"+
			    "                        T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                    END \n"+
			    "                ELSE  \n"+
			    "                    0 \n"+
			    "                END \n"+
			    "            END * PKG_CREDITO.dametasamoratoriadiaria(P.CVE_GPO_EMPRESA,P.CVE_EMPRESA,P.ID_PRESTAMO) AS IMP_INT_MORATORIO, \n"+
			    "            P.ID_PRODUCTO, P.NUM_CICLO \n"+
			                
			                
			                
			    "    FROM    PFIN_DIA_LIQUIDACION F, SIM_TABLA_AMORTIZACION T, SIM_PRESTAMO P, SIM_TABLA_AMORTIZACION T2, \n"+
			        
			    "            (   SELECT  F.CVE_GPO_EMPRESA, F.CVE_EMPRESA, F.F_LIQUIDACION, SUM( \n"+
			    "                        NVL(D.IMP_CONCEPTO *  \n"+
			    "                            DECODE(O.CVE_AFECTA_CREDITO,'I',-1,'D',1),0) \n"+
			    "                        )  \n"+
			    "                        IMP_MOVTOS \n"+
			    "                FROM    PFIN_DIA_LIQUIDACION F, SIM_TABLA_AMORTIZACION T, PFIN_MOVIMIENTO M, \n"+
			    "                        PFIN_MOVIMIENTO_DET D, PFIN_CAT_OPERACION O \n"+
			    "                WHERE   F.CVE_GPO_EMPRESA   = 'SIM' \n"+
			    "                    AND F.CVE_EMPRESA       = 'CREDICONFIA' \n"+
			    "                    AND F.CVE_LIQUIDACION   = 'NATUR' \n"+
			    "                    AND F.CVE_GPO_EMPRESA   = T.CVE_GPO_EMPRESA \n"+
			    "                    AND F.CVE_EMPRESA       = T.CVE_EMPRESA \n"+
			    "                    AND T.ID_PRESTAMO       = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			    "                    AND T.NUM_PAGO_AMORTIZACION   = 1 \n"+
			    "                    AND F.F_INFORMACION     BETWEEN T.FECHA_AMORTIZACION -1 \n"+
			    "                                            AND (SELECT  F_MEDIO \n"+
			    "                                                 FROM    PFIN_PARAMETRO \n"+
			    "                                                 WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                                     AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                                     AND CVE_MEDIO       = 'SYSTEM') \n"+
			    "                    AND F.CVE_GPO_EMPRESA   = M.CVE_GPO_EMPRESA(+) \n"+
			    "                    AND F.CVE_EMPRESA       = M.CVE_EMPRESA(+) \n"+
			    "                    AND " + (String)registro.getDefCampo("ID_PRESTAMO") + "                 = M.ID_PRESTAMO(+) \n"+
			    "                    AND F.F_INFORMACION     >= M.F_LIQUIDACION(+) \n"+
			    "                    AND M.SIT_MOVIMIENTO(+) <> 'CA' \n"+
			    "                    AND M.CVE_GPO_EMPRESA   = D.CVE_GPO_EMPRESA(+) \n"+
			    "                    AND M.CVE_EMPRESA       = D.CVE_EMPRESA(+) \n"+
			    "                    AND M.ID_MOVIMIENTO     = D.ID_MOVIMIENTO(+) \n"+
			    "                    AND M.CVE_OPERACION     = D.CVE_OPERACION(+) \n"+
			    "                    AND D.CVE_CONCEPTO(+)      = 'CAPITA' \n"+
			    "                    AND M.CVE_GPO_EMPRESA   = O.CVE_GPO_EMPRESA(+) \n"+
			    "                    AND M.CVE_EMPRESA       = O.CVE_EMPRESA(+) \n"+
			    "                    AND M.CVE_OPERACION     = O.CVE_OPERACION(+) \n"+
			    "                GROUP BY F.CVE_GPO_EMPRESA, F.CVE_EMPRESA, F.F_LIQUIDACION ) M \n"+
			
			    "    WHERE   F.CVE_GPO_EMPRESA   = 'SIM' \n"+
			    "        AND F.CVE_EMPRESA       = 'CREDICONFIA' \n"+
			    "        AND F.CVE_LIQUIDACION   = 'NATUR' \n"+
			    "        AND F.CVE_GPO_EMPRESA   = T.CVE_GPO_EMPRESA \n"+
			    "        AND F.CVE_EMPRESA       = T.CVE_EMPRESA \n"+
			    "        AND T.ID_PRESTAMO       = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			            
			    "        AND T.CVE_GPO_EMPRESA       = T2.CVE_GPO_EMPRESA(+) \n"+
			    "        AND T.CVE_EMPRESA           = T2.CVE_EMPRESA(+) \n"+
			    "        AND T.ID_PRESTAMO           = T2.ID_PRESTAMO(+) \n"+
			    "        AND T.NUM_PAGO_AMORTIZACION - 1 = T2.NUM_PAGO_AMORTIZACION(+) \n"+
			
			    "        AND T.CVE_GPO_EMPRESA   = P.CVE_GPO_EMPRESA \n"+
			    "        AND T.CVE_EMPRESA       = P.CVE_EMPRESA \n"+
			    "        AND T.ID_PRESTAMO       = P.ID_PRESTAMO \n"+
			    "        AND (SELECT  MAX(P.NUM_PAGO_AMORTIZACION) \n"+
			    //, P.FECHA_AMORTIZACION
			    "             FROM    SIM_TABLA_AMORTIZACION P \n"+
			    "             WHERE   P.CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                 AND P.CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                 AND P.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			    "                 AND P.FECHA_AMORTIZACION <= F.F_INFORMACION) = T.NUM_PAGO_AMORTIZACION \n"+
			    "        AND F.F_INFORMACION     BETWEEN (SELECT MIN(FECHA_AMORTIZACION) \n"+
			    "                                         FROM   SIM_TABLA_AMORTIZACION \n"+
			    "                                         WHERE  CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                            AND ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "') \n"+
			    "                                AND (SELECT  F_MEDIO  \n"+
			    "                                     FROM    PFIN_PARAMETRO  \n"+
			    "                                     WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                         AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                         AND CVE_MEDIO       = 'SYSTEM') \n"+
			                                             
			    "        AND F.CVE_GPO_EMPRESA   = M.CVE_GPO_EMPRESA \n"+
			    "        AND F.CVE_EMPRESA       = M.CVE_EMPRESA \n"+
			    "        AND F.F_INFORMACION     = M.F_LIQUIDACION + 1 \n"+
			            
			            
			    "        )   A, SIM_PRODUCTO_CICLO PC, PFIN_CAT_CONCEPTO C \n"+
			
			        
			    "    WHERE   A.CVE_GPO_EMPRESA   = PC.CVE_GPO_EMPRESA \n"+
			    "        AND A.CVE_EMPRESA       = PC.CVE_EMPRESA \n"+
			    "        AND A.ID_PRODUCTO       = PC.ID_PRODUCTO \n"+
			    "        AND A.NUM_CICLO         = PC.NUM_CICLO \n"+
			    "        AND A.CVE_GPO_EMPRESA   = C.CVE_GPO_EMPRESA \n"+
			    "        AND A.CVE_EMPRESA       = C.CVE_EMPRESA \n"+
			    "        AND C.CVE_CONCEPTO      = 'INTMORA' \n"+
			    "    GROUP BY A.ID_PRESTAMO, A.FECHA_AMORTIZACION, INITCAP(C.DESC_LARGA), C.CVE_CONCEPTO \n"+
			    "    HAVING ROUND(SUM(A.IMP_INT_MORATORIO),2) > 0 \n"+
			    ") \n"+
			
			    "UNION ALL \n"+
			
			    "SELECT  ID_ORDEN_TIPO, ID_ORDEN, ID_PRESTAMO, FECHA_OPERACION, IMP_NETO, \n"+
			    "        DESCRIPCION, CVE_CONCEPTO, CVE_AFECTA \n"+
			    "FROM    (    \n"+
			
			    //Muestra el importe de IVA de Intereses Moratorios a la fecha 
			    "SELECT  1 AS ID_ORDEN_TIPO, 8 AS ID_ORDEN, A.ID_PRESTAMO, A.FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
			    "        ROUND(SUM(A.IMP_IVA_INT_MORATORIO) * -1,2) AS IMP_NETO, INITCAP(C.DESC_LARGA) AS DESCRIPCION, C.CVE_CONCEPTO, \n"+
			    "        'I' AS CVE_AFECTA \n"+
			    "FROM    (    \n"+
			                
			    "    SELECT  P.CVE_GPO_EMPRESA,P.CVE_EMPRESA,P.ID_PRESTAMO, F.F_LIQUIDACION, T.FECHA_AMORTIZACION, \n"+
			    "            T.NUM_PAGO_AMORTIZACION, T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION AS IMP_CAPITAL, \n"+
			                
			    "            CASE WHEN F.F_LIQUIDACION = T.FECHA_AMORTIZACION AND T.NUM_PAGO_AMORTIZACION = 1 THEN \n"+
			    "                0 \n"+
			    "            ELSE \n"+
			    "                CASE WHEN \n"+
			    "                    CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
			    "                        T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                    ELSE \n"+
			    "                        T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                    END > 0 THEN \n"+
			                        
			    "                     CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
			    "                         T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                     ELSE \n"+
			    "                         T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                     END \n"+
			    "                 ELSE  \n"+
			    "                     0 \n"+
			    "                 END \n"+
			    "             END AS IMP_CAPITAL_DEBE, M.IMP_MOVTOS, \n"+
			                
			    "             PKG_CREDITO.dametasamoratoriadiaria(P.CVE_GPO_EMPRESA,P.CVE_EMPRESA,P.ID_PRESTAMO) TASA_MORATORIA, \n"+
			                 
			    "             CASE WHEN F.F_LIQUIDACION = T.FECHA_AMORTIZACION AND T.NUM_PAGO_AMORTIZACION = 1 THEN \n"+
			    "                 0 \n"+
			    "             ELSE \n"+
			    "                 CASE WHEN  \n"+
			    "                     CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
			    "                         T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                     ELSE \n"+
			    "                         T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                     END > 0 THEN \n"+
			    
			    "                    CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
			    "                        T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                    ELSE \n"+
			    "                        T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
			    "                    END \n"+
			    "                ELSE  \n"+
			    "                    0 \n"+
			    "                END \n"+
			    "            END * PKG_CREDITO.dametasamoratoriadiaria(P.CVE_GPO_EMPRESA,P.CVE_EMPRESA,P.ID_PRESTAMO) *  (TASA_IVA -1 ) AS IMP_IVA_INT_MORATORIO, \n"+
			    "            P.ID_PRODUCTO, P.NUM_CICLO \n"+
			                
			    "    FROM    PFIN_DIA_LIQUIDACION F, SIM_TABLA_AMORTIZACION T, SIM_PRESTAMO P, SIM_CAT_SUCURSAL S, SIM_TABLA_AMORTIZACION T2, \n"+
			        
			    "            (   SELECT  F.CVE_GPO_EMPRESA, F.CVE_EMPRESA, F.F_LIQUIDACION, SUM( \n"+
			    "                        NVL(D.IMP_CONCEPTO * \n"+
			    "                            DECODE(O.CVE_AFECTA_CREDITO,'I',-1,'D',1),0) \n"+
			    "                        ) \n"+
			    "                        IMP_MOVTOS, MAX(NUM_PAGO_AMORTIZACION) MAX_NUM_PAGO \n"+
			    "                FROM    PFIN_DIA_LIQUIDACION F, SIM_TABLA_AMORTIZACION T, PFIN_MOVIMIENTO M, \n"+
			    "                        PFIN_MOVIMIENTO_DET D, PFIN_CAT_OPERACION O \n"+
			    "                WHERE   F.CVE_GPO_EMPRESA   = 'SIM' \n"+
			    "                    AND F.CVE_EMPRESA       = 'CREDICONFIA' \n"+
			    "                    AND F.CVE_LIQUIDACION   = 'NATUR' \n"+
			    "                    AND F.CVE_GPO_EMPRESA   = T.CVE_GPO_EMPRESA \n"+
			    "                    AND F.CVE_EMPRESA       = T.CVE_EMPRESA \n"+
			    "                    AND T.ID_PRESTAMO       = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			    "                    AND T.NUM_PAGO_AMORTIZACION   = 1 \n"+
			    "                    AND F.F_INFORMACION     BETWEEN T.FECHA_AMORTIZACION -1 \n"+
			    "                                            AND (SELECT  F_MEDIO  \n"+
			    "                                                 FROM    PFIN_PARAMETRO \n"+
			    "                                                 WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                                     AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                                     AND CVE_MEDIO       = 'SYSTEM') \n"+
			    "                    AND F.CVE_GPO_EMPRESA   = M.CVE_GPO_EMPRESA(+) \n"+
			    "                    AND F.CVE_EMPRESA       = M.CVE_EMPRESA(+) \n"+
			    "                    AND " + (String)registro.getDefCampo("ID_PRESTAMO") + "    = M.ID_PRESTAMO(+) \n"+
			    "                    AND F.F_INFORMACION     >= M.F_LIQUIDACION(+) \n"+
			    "                    AND M.SIT_MOVIMIENTO(+) <> 'CA' \n"+
			    "                    AND M.CVE_GPO_EMPRESA   = D.CVE_GPO_EMPRESA(+) \n"+
			    "                    AND M.CVE_EMPRESA       = D.CVE_EMPRESA(+) \n"+
			    "                    AND M.ID_MOVIMIENTO     = D.ID_MOVIMIENTO(+) \n"+
			    "                    AND M.CVE_OPERACION     = D.CVE_OPERACION(+) \n"+
			    "                    AND D.CVE_CONCEPTO(+)      = 'CAPITA' \n"+
			    "                    AND M.CVE_GPO_EMPRESA   = O.CVE_GPO_EMPRESA(+) \n"+
			    "                    AND M.CVE_EMPRESA       = O.CVE_EMPRESA(+) \n"+
			    "                    AND M.CVE_OPERACION     = O.CVE_OPERACION(+) \n"+
			    "                GROUP BY F.CVE_GPO_EMPRESA, F.CVE_EMPRESA, F.F_LIQUIDACION ) M \n"+
			
			    "    WHERE   F.CVE_GPO_EMPRESA   = 'SIM' \n"+
			    "        AND F.CVE_EMPRESA       = 'CREDICONFIA' \n"+
			    "        AND F.CVE_LIQUIDACION   = 'NATUR' \n"+
			    "        AND F.CVE_GPO_EMPRESA   = T.CVE_GPO_EMPRESA \n"+
			    "        AND F.CVE_EMPRESA       = T.CVE_EMPRESA \n"+
			    "        AND T.ID_PRESTAMO       = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			            
			    "        AND T.CVE_GPO_EMPRESA       = T2.CVE_GPO_EMPRESA(+) \n"+
			    "        AND T.CVE_EMPRESA           = T2.CVE_EMPRESA(+) \n"+
			    "        AND T.ID_PRESTAMO           = T2.ID_PRESTAMO(+) \n"+
			    "        AND T.NUM_PAGO_AMORTIZACION - 1 = T2.NUM_PAGO_AMORTIZACION(+) \n"+
			
			    "        AND T.CVE_GPO_EMPRESA   = P.CVE_GPO_EMPRESA \n"+
			    "        AND T.CVE_EMPRESA       = P.CVE_EMPRESA \n"+
			    "        AND T.ID_PRESTAMO       = P.ID_PRESTAMO \n"+
			    "        AND P.CVE_GPO_EMPRESA   = S.CVE_GPO_EMPRESA \n"+
			    "        AND P.CVE_EMPRESA       = S.CVE_EMPRESA \n"+
			    "        AND P.ID_SUCURSAL       = S.ID_SUCURSAL \n"+
			    "        AND (SELECT  MAX(P.NUM_PAGO_AMORTIZACION) \n"+
			    //, P.FECHA_AMORTIZACION
			    "             FROM    SIM_TABLA_AMORTIZACION P \n"+
			    "             WHERE   P.CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                 AND P.CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                 AND P.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			    "                 AND P.FECHA_AMORTIZACION <= F.F_INFORMACION) = T.NUM_PAGO_AMORTIZACION \n"+
			    "        AND F.F_INFORMACION     BETWEEN (SELECT MIN(FECHA_AMORTIZACION) \n"+
			    "                                         FROM   SIM_TABLA_AMORTIZACION \n"+
			    "                                         WHERE  CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                            AND ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "') \n"+
			    "                                AND (SELECT  F_MEDIO  \n"+
			    "                                     FROM    PFIN_PARAMETRO \n"+
			    "                                     WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
			    "                                         AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			    "                                         AND CVE_MEDIO       = 'SYSTEM') \n"+
			                                             
			    "        AND F.CVE_GPO_EMPRESA   = M.CVE_GPO_EMPRESA \n"+
			    "        AND F.CVE_EMPRESA       = M.CVE_EMPRESA \n"+
			    "        AND F.F_INFORMACION     = M.F_LIQUIDACION +1)   A, SIM_PRODUCTO_CICLO PC, PFIN_CAT_CONCEPTO C \n"+
			
			        
			    "    WHERE   A.CVE_GPO_EMPRESA   = PC.CVE_GPO_EMPRESA \n"+
			    "        AND A.CVE_EMPRESA       = PC.CVE_EMPRESA \n"+
			    "        AND A.ID_PRODUCTO       = PC.ID_PRODUCTO \n"+
			    "        AND A.NUM_CICLO         = PC.NUM_CICLO \n"+
			    "        AND A.CVE_GPO_EMPRESA   = C.CVE_GPO_EMPRESA \n"+
			    "        AND A.CVE_EMPRESA       = C.CVE_EMPRESA \n"+
			    "        AND C.CVE_CONCEPTO      = 'IVAINTMO' \n"+
			    "    GROUP BY A.ID_PRESTAMO, A.FECHA_AMORTIZACION, INITCAP(C.DESC_LARGA), C.CVE_CONCEPTO \n"+
			    "    HAVING ROUND(SUM(A.IMP_IVA_INT_MORATORIO),2) > 0 \n"+
			    ") \n"+
			
			    "    UNION ALL \n"+
			        
			    "    SELECT  3 AS ID_ORDEN_TIPO, M.ID_MOVIMIENTO AS ID_ORDEN, M.ID_PRESTAMO, M.F_LIQUIDACION AS FECHA_OPERACION, \n"+
			    "            ROUND(D.IMP_CONCEPTO * DECODE(O.CVE_AFECTA_CREDITO,'I',-1,'D',1),2) AS IMP_NETO,  \n"+
			    "            INITCAP(C.DESC_LARGA) AS DESCRIPCION, C.CVE_CONCEPTO,  \n"+
			    "            O.CVE_AFECTA_CREDITO AS CVE_AFECTA \n"+
			    "    FROM    PFIN_MOVIMIENTO M, PFIN_MOVIMIENTO_DET D, PFIN_CAT_OPERACION O, PFIN_CAT_CONCEPTO C \n"+
			    "    WHERE   M.CVE_GPO_EMPRESA   = 'SIM' \n"+
			    "        AND M.CVE_EMPRESA       = 'CREDICONFIA' \n"+
			    "        AND M.ID_PRESTAMO       = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			    "        AND M.SIT_MOVIMIENTO    <> 'CA' \n"+
			    "        AND M.CVE_GPO_EMPRESA   = D.CVE_GPO_EMPRESA \n"+
			    "        AND M.CVE_EMPRESA       = D.CVE_EMPRESA \n"+
			    "        AND M.ID_MOVIMIENTO     = D.ID_MOVIMIENTO \n"+
			    "        AND M.CVE_OPERACION     = D.CVE_OPERACION \n"+
			    "        AND M.CVE_GPO_EMPRESA   = O.CVE_GPO_EMPRESA \n"+
			    "        AND M.CVE_EMPRESA       = O.CVE_EMPRESA \n"+
			    "        AND M.CVE_OPERACION     = O.CVE_OPERACION \n"+
			    "        AND D.CVE_GPO_EMPRESA   = C.CVE_GPO_EMPRESA \n"+
			    "        AND D.CVE_EMPRESA       = C.CVE_EMPRESA \n"+
			    "        AND D.CVE_CONCEPTO      = C.CVE_CONCEPTO  \n"+
			    "        AND ROUND(D.IMP_CONCEPTO,2) > 0 \n"+
			    "        )\n"+
			    "        GROUP BY ID_PRESTAMO, CVE_CONCEPTO \n"+
			    "    )GROUP BY ID_ORDEN_TIPO, ID_ORDEN, ID_PRESTAMO, FECHA_OPERACION, DESCRIPCION \n"+
			         
			    "    ORDER BY FECHA_OPERACION, ID_ORDEN_TIPO, ID_ORDEN \n";
				
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();
				
				if (rs1.next()){
					sSaldo = rs1.getString("IMP_DESGLOSE");
					fSaldo = (Float.parseFloat(sSaldo));
					
				}
				
				fSaldo = fSaldo < 0 ? -fSaldo : fSaldo;
				
				if (fSaldo >= iDeudaMinima){
					iDeuda++;
				}
			}
			
			if (iDeuda != 0){
				resultadoCatalogo.mensaje.setClave("PRESTAMO_VIGENTE");
			}else {
				
				//OBTENEMOS EL SEQUENCE
				sSql = "SELECT SQ01_SIM_PRESTAMO.nextval as ID_PRESTAMO FROM DUAL";
				ejecutaSql();
				
				if (rs.next()){
					sIdPrestamo = rs.getString("ID_PRESTAMO");
				}

				sSql =  "INSERT INTO SIM_PRESTAMO ( "+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_PRESTAMO, \n" +
					"FECHA_SOLICITUD, \n" +
					"ID_CLIENTE, \n" +
					"ID_GRUPO, \n" +
					"ID_SUCURSAL, \n" +
					"CVE_ASESOR_CREDITO, \n" +
					"FECHA_ASESOR_SIST) \n" +
					" VALUES (" +
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					" " + sIdPrestamo +", \n" +
					"SYSDATE, \n" +
					"'" + (String)registro.getDefCampo("ID_PERSONA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
					"SYSDATE) \n" ;

				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				resultadoCatalogo.Resultado.addDefCampo("ID_PRESTAMO", sIdPrestamo);
			}
			
		}
				
		return resultadoCatalogo;
	}

		/**
		 * Borra un registro.
		 * @param registro Llave primaria.
		 * @return Objeto que contiene el resultado de la ejecución de este método.
		 * @throws SQLException Si se genera un error al accesar la base de datos.
		 */
		public ResultadoCatalogo baja(Registro registro) throws SQLException{
			ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
			sSql =  " DELETE FROM SIM_PRESTAMO " +
				" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
				" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
	
		return resultadoCatalogo;
	}

}