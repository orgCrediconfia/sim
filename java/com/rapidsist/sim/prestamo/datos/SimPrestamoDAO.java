/**
 * Sistema de administraci�n de portales.
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
 * 1. Hacer la consulta general del pr�stamos (grupales e individuales) atr�ves del m�todo getRegistros.
 * 2. Consultar el detalle de todos los pr�samos de forma grupal atrav�s del m�todo getRegistro.
 * 3. Dar de alta el cliente al que se le va a otorgar el cr�dito atrav�s del m�todo alta.
 */
 
public class SimPrestamoDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionConsultaRegistro, OperacionConsultaTabla  {
	

	/**
	 * Obtiene un conjunto de registros en base ael filtro de b�squeda.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT \n"+
				"P.ID_PRESTAMO, \n"+
				"P.CVE_PRESTAMO, \n"+
				"P.FECHA_ENTREGA, \n"+
				"P.FECHA_REAL, \n"+
				"P.ID_CLIENTE, \n"+
				"P.ID_GRUPO, \n"+
				"C.NOM_COMPLETO, \n"+
				"P.ID_PRODUCTO, \n"+
				"P.NUM_CICLO, \n"+
				"P.ID_ETAPA_PRESTAMO, \n"+
				"E.NOM_ESTATUS_PRESTAMO \n"+
			"FROM " +
				"(SELECT * FROM SIM_PRESTAMO ORDER BY ID_PRESTAMO) P, \n"+
				"RS_GRAL_PERSONA C, \n"+
				"SIM_CAT_ETAPA_PRESTAMO E, \n" +
				"SIM_USUARIO_ACCESO_SUCURSAL US \n"+
			"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND P.ID_PRODUCTO IS NOT NULL \n"+
			"AND C.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
			"AND C.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
			"AND C.ID_PERSONA (+)= P.ID_CLIENTE \n"+
			"AND E.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
			"AND E.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+ 
			"AND E.ID_ETAPA_PRESTAMO (+)= P.ID_ETAPA_PRESTAMO \n"+
			"AND US.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
            "AND US.CVE_EMPRESA = P.CVE_EMPRESA \n"+
            "AND US.ID_SUCURSAL = P.ID_SUCURSAL \n"+
            "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
			"AND P.ID_GRUPO IS NULL \n"+
			"AND ROWNUM <= 100 \n";
			
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + "AND P.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
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
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + " AND UPPER(C.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
		}
		if (parametros.getDefCampo("ID_ETAPA_PRESTAMO") != null) {
			sSql = sSql + "AND P.ID_ETAPA_PRESTAMO = '" + (String) parametros.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
		}
		
		sSql = sSql + "ORDER BY P.CVE_PRESTAMO \n";
		
		ejecutaSql();
		return getConsultaLista();
	}

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el registro
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
				"P.ID_CLIENTE ID_PERSONA, \n"+
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
				"P.ID_FORMA_DISTRIBUCION, \n"+
				"DECODE(P.ID_FORMA_DISTRIBUCION,'1','Saldos - Accesorios y capital','2','Saldos - Capital y accesorios') FORMA_APLICACION \n"+
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
	 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sNegocioGiro = "";
		String sCreditosSimultaneos = "";
		String sDeudaMinima = "";
		float fDeudaMinima = 0;
		int iDeuda = 0;
		String sSaldo = "";
		float fSaldo = 0;
		String sSaldoTotal = "";
		float fSaldoTotal = 0;
		
		//Validamos que el cliente tenga un negocio principal y este definido su giro.
		sSql = " SELECT COUNT(*) NEGOCIO_CLIENTE \n" +
				"FROM ( \n" +
					"SELECT \n" +
					"ID_NEGOCIO \n" +
					"FROM \n" +
					"SIM_CLIENTE_NEGOCIO \n" + 
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
					"AND ID_PERSONA = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" +
					"AND B_PRINCIPAL = 'V' \n" +
					"AND CVE_CLASE IS NOT NULL \n" +
				") \n" ;
		ejecutaSql();
		if (rs.next()){
			sNegocioGiro = rs.getString("NEGOCIO_CLIENTE");
		}
		
		if (sNegocioGiro.equals("1")){
			//Preguntamos por el par�metro Cr�dito Simult�neos.
			
			sSql = " SELECT \n" + 
				  " CREDITOS_SIMULTANEOS, \n" +
				  " IMP_DEUDA_MINIMA \n" +
				  "	FROM SIM_PARAMETRO_GLOBAL  \n" +
				  "	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				  "	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" ;
			ejecutaSql();
			if (rs.next()){
				sCreditosSimultaneos = rs.getString("CREDITOS_SIMULTANEOS");
				sDeudaMinima = rs.getString("IMP_DEUDA_MINIMA");
				fDeudaMinima = (Float.parseFloat(sDeudaMinima));
			}
			
			if (sCreditosSimultaneos.equals("V")){
				//No hace ninguna validaci�n.
				sSql = "Todavia no se da ning�n alta";
				resultadoCatalogo.Resultado.addDefCampo("ID_PERSONA", (String)registro.getDefCampo("ID_PERSONA"));
				
			}else {
				//No acepta cr�ditos simult�neos.
				//Consulta si no tiene un cr�dito vigente.
				//Busca todos los creditos del cliente.
				sSql =  "SELECT \n" +
						"P.ID_PRESTAMO \n" +
						"FROM SIM_PRESTAMO P \n" +
						"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
						"AND P.ID_CLIENTE = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" ;
				System.out.println("Busca todos los creditos del cliente");
				ejecutaSql();
				
				while (rs.next()){
					registro.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO")== null ? "": rs.getString("ID_PRESTAMO"));
					//Consulta el saldo a la fecha.
					sSql= "SELECT V.CVE_GPO_EMPRESA, \n" +
					  "    V.CVE_EMPRESA, \n"+
					  "    V.Id_Prestamo, \n"+
					  "    DECODE(SUM(V.IMP_SALDO_HOY),null,'NADA',SUM(V.IMP_SALDO_HOY)) IMP_SALDO_HOY \n" + 
					  "  From V_SIM_PRESTAMO_RES_EDO_CTA V, \n"+
					  "  SIM_PRESTAMO P \n"+
					  "  WHERE V.DESC_MOVIMIENTO IN ('Pago Tard�o','Pago Pago Tard�o','Seguro Deudor','Pago Seguro Deudor','Capital','Pago Capital','Inter�s', 'Inter�s Extra', 'Iva De Intereses', 'Iva Interes Extra', 'Pago Inter�s', 'Pago Inter�s Extra', 'Pago Iva De Intereses', 'Pago Iva Interes Extra') \n"+
					  "  AND V.ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
					  "  AND P.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n" +
				      "  AND P.CVE_EMPRESA = V.CVE_EMPRESA \n" +
				      "  AND P.ID_PRESTAMO = V.ID_PRESTAMO \n" +
				      "  AND (P.ID_ETAPA_PRESTAMO != '16' AND P.ID_ETAPA_PRESTAMO != '8') \n" +
					  "  GROUP BY V.CVE_GPO_EMPRESA, V.CVE_EMPRESA, V.Id_Prestamo \n";
					
					System.out.println("Consulta el saldo a la fecha");
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					ps1.execute();
					ResultSet rs1 = ps1.getResultSet();
					
					if (rs1.next()){
						sSaldo = rs1.getString("IMP_SALDO_HOY");
						fSaldo = (Float.parseFloat(sSaldo));
					}
					
					if (fSaldo >= 0){
						//Si no tiene saldo a la fecha consulta el saldo total.
						sSql =	"SELECT \n"+
								"A.CVE_GPO_EMPRESA, \n"+
								"A.CVE_EMPRESA, \n"+
								"A.Id_Prestamo, \n"+
								"DECODE(SUM(IMP_NETO),null,'NADA',SUM(IMP_NETO)) SALDO_TOTAL \n" + 
								"FROM  \n"+
								"V_SIM_TABLA_AMORT_CONCEPTO A, \n"+
								"PFIN_CAT_CONCEPTO B, \n"+
								"SIM_PRESTAMO P \n"+
								"WHERE A.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND A.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND A.ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
								"AND B.CVE_GPO_EMPRESA   = A.CVE_GPO_EMPRESA \n"+
								"AND B.CVE_EMPRESA       = A.CVE_EMPRESA \n"+
								"AND B.CVE_CONCEPTO      = A.CVE_CONCEPTO \n"+
								"AND A.IMP_ORIGINAL     <> 0 \n"+
								"AND P.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA \n"+
						        "AND P.CVE_EMPRESA = A.CVE_EMPRESA \n"+
						        "AND P.ID_PRESTAMO = A.ID_PRESTAMO \n"+
						        "AND (P.ID_ETAPA_PRESTAMO != '16' AND P.ID_ETAPA_PRESTAMO != '8') \n"+
						        "GROUP BY A.CVE_GPO_EMPRESA, A.CVE_EMPRESA, A.Id_Prestamo \n";
						
						PreparedStatement ps2 = this.conn.prepareStatement(sSql);
						ps2.execute();
						ResultSet rs2 = ps2.getResultSet();
						
						if (rs2.next()){
							sSaldoTotal = rs2.getString("SALDO_TOTAL");
							fSaldoTotal = (Float.parseFloat(sSaldoTotal));
							fSaldo = fSaldoTotal;
						}else{
							sSql =	"SELECT \n"+
									"CVE_GPO_EMPRESA, \n"+
									"CVE_EMPRESA, \n"+
									"Id_Prestamo, \n"+
									"FECHA_ENTREGA \n"+
									"FROM SIM_PRESTAMO \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
									"AND FECHA_ENTREGA IS NULL \n"+
									"AND (ID_ETAPA_PRESTAMO != '16' AND ID_ETAPA_PRESTAMO != '8') \n";
							PreparedStatement ps16 = this.conn.prepareStatement(sSql);
							ps16.execute();
							ResultSet rs16 = ps16.getResultSet();
							
							if (rs16.next()){
								iDeuda++;
							}
						}
					}
					fSaldo = fSaldo < 0 ? -fSaldo : fSaldo;
					System.out.println("fDeudaMinima"+fDeudaMinima);
					System.out.println("fSaldo"+fSaldo);
					//Compara si el saldo a la fecha o el saldo total es mayor o igual a la deuda m�nima.
					if (fSaldo >= fDeudaMinima){
						iDeuda++;
					}
				}
				
				if (iDeuda != 0){
					//El cliente ya tiene un cr�dito vigente por lo que no se puede dar de alta el cr�dito.
					resultadoCatalogo.mensaje.setClave("PRESTAMO_VIGENTE");
				}else {
					//Se puede dar de alta el cr�dito.
					sSql = "Todavia no se da ning�n alta";
					resultadoCatalogo.Resultado.addDefCampo("ID_PERSONA", (String)registro.getDefCampo("ID_PERSONA"));
				}
			}
		}else {
			//El cliente debe de tener declarado un negocio y su giro.
			resultadoCatalogo.mensaje.setClave("INTEGRANTE_NO_NEGOCIO_GIRO");
		}
				
		return resultadoCatalogo;
	}

		/**
		 * Borra un registro.
		 * @param registro Llave primaria.
		 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
		 * @throws SQLException Si se genera un error al accesar la base de datos.
		 */
		public ResultadoCatalogo baja(Registro registro) throws SQLException{
			ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
			//Pone el pr�stamo con estatus de cancelado.
			sSql =  " UPDATE SIM_PRESTAMO SET \n" +
					" ID_ETAPA_PRESTAMO = '16' \n" +
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