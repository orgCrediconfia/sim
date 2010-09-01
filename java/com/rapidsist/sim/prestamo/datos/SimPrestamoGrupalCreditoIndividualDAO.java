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
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;


/**
 * Administra los accesos a la base de datos para los préstamos grupales.
 */
 
public class SimPrestamoGrupalCreditoIndividualDAO extends Conexion2 implements OperacionAlta, OperacionModificacion {

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		if (registro.getDefCampo("ALTA").equals("CREDITO_GRUPAL")){
			
			sSql =	" SELECT \n"+
					" GI.CVE_GPO_EMPRESA, \n"+
					" GI.CVE_EMPRESA, \n"+
					" GI.ID_INTEGRANTE ID_PERSONA, \n"+
					" GI.FECHA_ALTA, \n"+
					" P.NOM_COMPLETO, \n"+
					" P.CVE_ASESOR_CREDITO, \n"+
					" NA.NOM_COMPLETO NOM_ASESOR_CREDITO, \n"+
					" DECODE(N.B_PRINCIPAL,'V','Principal','F','Secundario') B_PRINCIPAL, \n"+
					" TN.ID_TIPO_NEGOCIO, \n"+
					" TN.NOM_TIPO_NEGOCIO \n"+
					" FROM SIM_GRUPO_INTEGRANTE GI, \n"+
					"      RS_GRAL_PERSONA P, \n"+
					"      SIM_CLIENTE_NEGOCIO N, \n" +
					"      SIM_CAT_TIPO_NEGOCIO TN, \n" +
					"      RS_GRAL_USUARIO U, \n"+
					"      RS_GRAL_PERSONA NA \n"+
					" WHERE "+
					" GI.CVE_GPO_EMPRESA='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND GI.CVE_EMPRESA='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND GI.ID_GRUPO='" + (String)registro.getDefCampo("ID_GRUPO") + "' \n"+
					" AND P.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n"+
					" AND P.CVE_EMPRESA = GI.CVE_EMPRESA \n"+
					" AND P.ID_PERSONA = GI.ID_INTEGRANTE \n"+
					" AND GI.FECHA_BAJA_LOGICA IS NULL \n"+
					" AND N.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n" +
					" AND N.CVE_EMPRESA (+)= P.CVE_EMPRESA \n" +
					" AND N.ID_PERSONA (+)= P.ID_PERSONA \n" +
					" AND N.B_PRINCIPAL (+)= 'V' \n" +
					" AND TN.CVE_GPO_EMPRESA (+)= N.CVE_GPO_EMPRESA \n" +
					" AND TN.CVE_EMPRESA (+)= N.CVE_EMPRESA \n" +
					" AND TN.ID_TIPO_NEGOCIO (+)= N.ID_TIPO_NEGOCIO \n" +
					" AND U.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n" +
					" AND U.CVE_EMPRESA (+)= P.CVE_EMPRESA \n" +
					" AND U.CVE_USUARIO (+)= P.CVE_ASESOR_CREDITO \n" +
					" AND NA.CVE_GPO_EMPRESA (+)= U.CVE_GPO_EMPRESA \n"+
					" AND NA.CVE_EMPRESA (+)= U.CVE_EMPRESA \n"+
					" AND NA.ID_PERSONA (+)= U.ID_PERSONA \n"+
					" ORDER BY ID_PERSONA \n";
			ejecutaSql();
			if(rs.next()){
				registro.addDefCampo("CVE_ASESOR_CREDITO",rs.getString("CVE_ASESOR_CREDITO")== null ? "": rs.getString("CVE_ASESOR_CREDITO"));
			}
			
			sSql = "SELECT \n"+
					"PC.CVE_GPO_EMPRESA, \n"+
					"PC.CVE_EMPRESA, \n"+
					"PC.ID_PRODUCTO, \n"+
					"P.ID_PERIODICIDAD, \n"+
					"P.CVE_METODO, \n"+
					"PC.PLAZO, \n"+
					"PC.TIPO_TASA, \n"+
					"PC.VALOR_TASA, \n"+
					"PC.ID_PERIODICIDAD_TASA, \n"+
					"PC.ID_TASA_REFERENCIA, \n"+
					"P.CVE_METODO, \n"+
					"P.MONTO_MINIMO, \n"+
					"PC.MONTO_MAXIMO, \n"+
					"PC.ID_TIPO_RECARGO, \n"+
					"PC.TIPO_TASA_RECARGO, \n"+
					"PC.TASA_RECARGO, \n"+
					"PC.ID_PERIODICIDAD_TASA_RECARGO, \n"+
					"PC.ID_TASA_REFERENCIA_RECARGO, \n"+
					"PC.FACTOR_TASA_RECARGO, \n"+
					"PC.MONTO_FIJO_PERIODO \n"+
					"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
					"SIM_PRODUCTO P \n"+
					"WHERE PC.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND PC.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND PC.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
					"AND PC.NUM_CICLO = '" + (String)registro.getDefCampo("NUM_CICLO") + "' \n"+
					"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
					"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
					"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n";
				ejecutaSql();
				if(rs.next()){
					registro.addDefCampo("ID_PERIODICIDAD_PRODUCTO",rs.getString("ID_PERIODICIDAD")== null ? "": rs.getString("ID_PERIODICIDAD"));
					registro.addDefCampo("CVE_METODO",rs.getString("CVE_METODO")== null ? "": rs.getString("CVE_METODO"));
					registro.addDefCampo("PLAZO",rs.getString("PLAZO")== null ? "": rs.getString("PLAZO"));
					registro.addDefCampo("TIPO_TASA",rs.getString("TIPO_TASA")== null ? "": rs.getString("TIPO_TASA"));
					registro.addDefCampo("VALOR_TASA",rs.getString("VALOR_TASA")== null ? "": rs.getString("VALOR_TASA"));
					registro.addDefCampo("ID_PERIODICIDAD_TASA",rs.getString("ID_PERIODICIDAD_TASA")== null ? "": rs.getString("ID_PERIODICIDAD_TASA"));
					registro.addDefCampo("ID_TASA_REFERENCIA",rs.getString("ID_TASA_REFERENCIA")== null ? "": rs.getString("ID_TASA_REFERENCIA"));
					registro.addDefCampo("MONTO_MINIMO",rs.getString("MONTO_MINIMO")== null ? "": rs.getString("MONTO_MINIMO"));
					registro.addDefCampo("MONTO_MAXIMO",rs.getString("MONTO_MAXIMO")== null ? "": rs.getString("MONTO_MAXIMO"));
					registro.addDefCampo("ID_TIPO_RECARGO",rs.getString("ID_TIPO_RECARGO")== null ? "": rs.getString("ID_TIPO_RECARGO"));
					registro.addDefCampo("TIPO_TASA_RECARGO",rs.getString("TIPO_TASA_RECARGO")== null ? "": rs.getString("TIPO_TASA_RECARGO"));
					registro.addDefCampo("TASA_RECARGO",rs.getString("TASA_RECARGO")== null ? "": rs.getString("TASA_RECARGO"));
					registro.addDefCampo("ID_PERIODICIDAD_TASA_RECARGO",rs.getString("ID_PERIODICIDAD_TASA_RECARGO")== null ? "": rs.getString("ID_PERIODICIDAD_TASA_RECARGO"));
					registro.addDefCampo("ID_TASA_REFERENCIA_RECARGO",rs.getString("ID_TASA_REFERENCIA_RECARGO")== null ? "": rs.getString("ID_TASA_REFERENCIA_RECARGO"));
					registro.addDefCampo("FACTOR_TASA_RECARGO",rs.getString("FACTOR_TASA_RECARGO")== null ? "": rs.getString("FACTOR_TASA_RECARGO"));
					registro.addDefCampo("MONTO_FIJO_PERIODO",rs.getString("MONTO_FIJO_PERIODO")== null ? "": rs.getString("MONTO_FIJO_PERIODO"));
				
				String sIdPrestamoGrupal = "";
				String sCvePrestamoGrupal = "";
				
				//SE OBTIENE EL SEQUENCE
				   
				sSql = "SELECT SQ01_SIM_PRESTAMO_GRUPO.nextval as ID_PRESTAMO_GRUPO FROM DUAL";
				ejecutaSql();
				if (rs.next()){
					sIdPrestamoGrupal = rs.getString("ID_PRESTAMO_GRUPO");
				}
				
				//OBTIENE CLAVE DEL PRÉSTAMO GRUPAL.
				if (registro.getDefCampo("CICLO").equals("igual")){
					sSql =  "SELECT REPLACE (CVE_PRESTAMO_GRUPO,' ','') CVE_PRESTAMO_GRUPO FROM ( \n"+
							"SELECT TO_CHAR('" + (String)registro.getDefCampo("ID_PRODUCTO") + "','00')||TO_CHAR('" + (String)registro.getDefCampo("ID_GRUPO") + "','000000')||TO_CHAR('" + (String)registro.getDefCampo("NUM_CICLO") + "','00') AS CVE_PRESTAMO_GRUPO FROM DUAL \n"+
							") \n";
					ejecutaSql();
					if (rs.next()){
						sCvePrestamoGrupal = rs.getString("CVE_PRESTAMO_GRUPO");
					}
				}else {
					sSql =  "SELECT REPLACE (CVE_PRESTAMO_GRUPO,' ','') CVE_PRESTAMO_GRUPO FROM ( \n"+
							"SELECT TO_CHAR('" + (String)registro.getDefCampo("ID_PRODUCTO") + "','00')||TO_CHAR('" + (String)registro.getDefCampo("ID_GRUPO") + "','000000')||TO_CHAR('" + (String)registro.getDefCampo("CICLO") + "','00') AS CVE_PRESTAMO_GRUPO FROM DUAL \n"+
							") \n";
					ejecutaSql();
					if (rs.next()){
						sCvePrestamoGrupal = rs.getString("CVE_PRESTAMO_GRUPO");
					}
				}
				
				
				
				
				
				sSql =  "INSERT INTO SIM_PRESTAMO_GRUPO ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_PRESTAMO_GRUPO, \n" +
						"CVE_PRESTAMO_GRUPO, \n" +
						"ID_GRUPO, \n" +
						"ID_PRODUCTO, \n" +
						"NUM_CICLO, \n" +
						"PLAZO, \n"+
						"ID_PERIODICIDAD_PRODUCTO, \n"+
						"TIPO_TASA, \n"+
						"VALOR_TASA, \n"+
						"ID_PERIODICIDAD_TASA, \n"+
						"ID_TASA_REFERENCIA, \n"+
						"CVE_METODO, \n"+
						"MONTO_MINIMO, \n"+
						"MONTO_MAXIMO, \n"+
						"ID_TIPO_RECARGO, \n"+
						"TIPO_TASA_RECARGO, \n"+
						"TASA_RECARGO, \n"+
						"ID_PERIODICIDAD_TASA_RECARGO, \n"+
						"ID_TASA_REFERENCIA_RECARGO, \n"+
						"FACTOR_TASA_RECARGO, \n"+
						"MONTO_FIJO_PERIODO, \n"+
						"CVE_ASESOR_CREDITO, \n"+
						"CVE_ASESOR_FUNDADOR, \n"+
						"B_ENTREGADO) \n"+
				        "VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					sIdPrestamoGrupal + ", \n "+
					"'" + sCvePrestamoGrupal + "', \n" +
					"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" ;
					
					if (registro.getDefCampo("CICLO").equals("igual")){
					
						sSql = sSql + "'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" ;
					}else {
						sSql = sSql + "'" + (String)registro.getDefCampo("CICLO") + "', \n" ;
					}
					sSql = sSql + "'" + (String)registro.getDefCampo("PLAZO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_PRODUCTO") + "', \n" +
					"'" + (String)registro.getDefCampo("TIPO_TASA") + "', \n" +
					"'" + (String)registro.getDefCampo("VALOR_TASA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_METODO") + "', \n" +
					"'" + (String)registro.getDefCampo("MONTO_MINIMO") + "', \n" +
					"'" + (String)registro.getDefCampo("MONTO_MAXIMO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_TIPO_RECARGO") + "', \n" +
					"'" + (String)registro.getDefCampo("TIPO_TASA_RECARGO") + "', \n" + 
					"'" + (String)registro.getDefCampo("TASA_RECARGO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA_RECARGO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_TASA_REFERENCIA_RECARGO") + "', \n" +
					"'" + (String)registro.getDefCampo("FACTOR_TASA_RECARGO") + "', \n" +
					"'" + (String)registro.getDefCampo("MONTO_FIJO_PERIODO") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
					"'F') \n" ;
				
				//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				resultadoCatalogo.Resultado.addDefCampo("ID_PRESTAMO_GRUPO", sIdPrestamoGrupal);
				
				sSql =  "SELECT \n"+
						"CVE_GPO_EMPRESA, \n"+
						"CVE_EMPRESA, \n"+
						"ID_CARGO_COMISION, \n"+
						"ID_PRODUCTO, \n"+
						"ID_FORMA_APLICACION, \n"+
						"CARGO_INICIAL, \n"+
						"PORCENTAJE_MONTO, \n"+
						"CANTIDAD_FIJA, \n"+
						"VALOR, \n"+
						"ID_UNIDAD, \n"+
						"ID_PERIODICIDAD \n"+
						"FROM \n"+
						"SIM_PRODUCTO_CARGO_COMISION \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
							
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					ps1.execute();
					ResultSet rs1 = ps1.getResultSet();
					while (rs1.next()){
						registro.addDefCampo("ID_CARGO_COMISION",rs1.getString("ID_CARGO_COMISION")== null ? "": rs1.getString("ID_CARGO_COMISION"));
						registro.addDefCampo("ID_FORMA_APLICACION",rs1.getString("ID_FORMA_APLICACION")== null ? "": rs1.getString("ID_FORMA_APLICACION"));
						registro.addDefCampo("CARGO_INICIAL",rs1.getString("CARGO_INICIAL")== null ? "": rs1.getString("CARGO_INICIAL"));
						registro.addDefCampo("PORCENTAJE_MONTO",rs1.getString("PORCENTAJE_MONTO")== null ? "": rs1.getString("PORCENTAJE_MONTO"));
						registro.addDefCampo("CANTIDAD_FIJA",rs1.getString("CANTIDAD_FIJA")== null ? "": rs1.getString("CANTIDAD_FIJA"));
						registro.addDefCampo("VALOR",rs1.getString("VALOR")== null ? "": rs1.getString("VALOR"));
						registro.addDefCampo("ID_UNIDAD",rs1.getString("ID_UNIDAD")== null ? "": rs1.getString("ID_UNIDAD"));
						registro.addDefCampo("ID_PERIODICIDAD",rs1.getString("ID_PERIODICIDAD")== null ? "": rs1.getString("ID_PERIODICIDAD"));
						
						sSql = "INSERT INTO SIM_PRESTAMO_GPO_CARGO ( \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_PRESTAMO_GRUPO, \n" +
							"ID_CARGO_COMISION, \n" +
							"ID_FORMA_APLICACION, \n"+
							"CARGO_INICIAL, \n"+
							"PORCENTAJE_MONTO, \n"+
							"CANTIDAD_FIJA, \n"+
							"VALOR, \n"+
							"ID_UNIDAD, \n"+
							"ID_PERIODICIDAD) \n"+
							" VALUES (" +
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"'" + sIdPrestamoGrupal + "', \n" +
							"'" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_FORMA_APLICACION") + "', \n" +
							"'" + (String)registro.getDefCampo("CARGO_INICIAL") + "', \n" +
							"'" + (String)registro.getDefCampo("PORCENTAJE_MONTO") + "', \n" +
							"'" + (String)registro.getDefCampo("CANTIDAD_FIJA") + "', \n" +
							"'" + (String)registro.getDefCampo("VALOR") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_UNIDAD") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "') \n" ;
			
						PreparedStatement ps2 = this.conn.prepareStatement(sSql);
						ps2.execute();
						ResultSet rs2 = ps2.getResultSet();	
					}	
				}
		}else if (registro.getDefCampo("ALTA").equals("CREDITO_INDIVIDUAL")) {
		
			sSql =  "INSERT INTO SIM_GRUPO_FUNDADOR ( "+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_GRUPO, \n" +
					"ID_INTEGRANTE, \n" +
					"ID_PRESTAMO_GRUPO, \n" +
					"FECHA_SOLICITUD) \n" +
					" VALUES (" +
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_CLIENTE") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "', \n" +
					"SYSDATE) \n" ;
			
			PreparedStatement ps2 = this.conn.prepareStatement(sSql);
			ps2.execute();
			ResultSet rs2 = ps2.getResultSet();	
			
			sSql = "SELECT \n"+
					"PC.CVE_GPO_EMPRESA, \n"+
					"PC.CVE_EMPRESA, \n"+
					"PC.ID_PRODUCTO, \n"+
					"P.ID_PERIODICIDAD, \n"+
					"P.CVE_METODO, \n"+
					"P.APLICA_A, \n"+
					"PC.PLAZO, \n"+
					"PC.TIPO_TASA, \n"+
					"PC.VALOR_TASA, \n"+
					"PC.ID_PERIODICIDAD_TASA, \n"+
					"PC.ID_TASA_REFERENCIA, \n"+
					"P.MONTO_MINIMO, \n"+
					"PC.MONTO_MAXIMO, \n"+
					"PC.ID_TIPO_RECARGO, \n"+
					"PC.TIPO_TASA_RECARGO, \n"+
					"PC.TASA_RECARGO, \n"+
					"PC.ID_PERIODICIDAD_TASA_RECARGO, \n"+
					"PC.ID_TASA_REFERENCIA_RECARGO, \n"+
					"PC.FACTOR_TASA_RECARGO, \n"+
					"PC.MONTO_FIJO_PERIODO, \n"+
					"PC.ID_FORMA_DISTRIBUCION, \n"+
					"PC.PORC_FLUJO_CAJA \n"+
					"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
					"SIM_PRODUCTO P \n"+
					"WHERE PC.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND PC.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND PC.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
					"AND PC.NUM_CICLO = '" + (String)registro.getDefCampo("NUM_CICLO") + "' \n"+
					"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
					"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
					"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n";
			
			PreparedStatement ps3 = this.conn.prepareStatement(sSql);
			ps3.execute();
			ResultSet rs3 = ps3.getResultSet();	
			
			if(rs3.next()){
				registro.addDefCampo("ID_PERIODICIDAD_PRODUCTO",rs3.getString("ID_PERIODICIDAD")== null ? "": rs3.getString("ID_PERIODICIDAD"));
				registro.addDefCampo("CVE_METODO",rs3.getString("CVE_METODO")== null ? "": rs3.getString("CVE_METODO"));
				registro.addDefCampo("APLICA_A",rs3.getString("APLICA_A")== null ? "": rs3.getString("APLICA_A"));
				registro.addDefCampo("PLAZO",rs3.getString("PLAZO")== null ? "": rs3.getString("PLAZO"));
				registro.addDefCampo("TIPO_TASA",rs3.getString("TIPO_TASA")== null ? "": rs3.getString("TIPO_TASA"));
				registro.addDefCampo("VALOR_TASA",rs3.getString("VALOR_TASA")== null ? "": rs3.getString("VALOR_TASA"));
				registro.addDefCampo("ID_PERIODICIDAD_TASA",rs3.getString("ID_PERIODICIDAD_TASA")== null ? "": rs3.getString("ID_PERIODICIDAD_TASA"));
				registro.addDefCampo("ID_TASA_REFERENCIA",rs3.getString("ID_TASA_REFERENCIA")== null ? "": rs3.getString("ID_TASA_REFERENCIA"));
				registro.addDefCampo("MONTO_MINIMO",rs3.getString("MONTO_MINIMO")== null ? "": rs3.getString("MONTO_MINIMO"));
				registro.addDefCampo("MONTO_MAXIMO",rs3.getString("MONTO_MAXIMO")== null ? "": rs3.getString("MONTO_MAXIMO"));
				registro.addDefCampo("ID_TIPO_RECARGO",rs3.getString("ID_TIPO_RECARGO")== null ? "": rs3.getString("ID_TIPO_RECARGO"));
				registro.addDefCampo("TIPO_TASA_RECARGO",rs3.getString("TIPO_TASA_RECARGO")== null ? "": rs3.getString("TIPO_TASA_RECARGO"));
				registro.addDefCampo("TASA_RECARGO",rs3.getString("TASA_RECARGO")== null ? "": rs3.getString("TASA_RECARGO"));
				registro.addDefCampo("ID_PERIODICIDAD_TASA_RECARGO",rs3.getString("ID_PERIODICIDAD_TASA_RECARGO")== null ? "": rs3.getString("ID_PERIODICIDAD_TASA_RECARGO"));
				registro.addDefCampo("ID_TASA_REFERENCIA_RECARGO",rs3.getString("ID_TASA_REFERENCIA_RECARGO")== null ? "": rs3.getString("ID_TASA_REFERENCIA_RECARGO"));
				registro.addDefCampo("FACTOR_TASA_RECARGO",rs3.getString("FACTOR_TASA_RECARGO")== null ? "": rs3.getString("FACTOR_TASA_RECARGO"));
				registro.addDefCampo("MONTO_FIJO_PERIODO",rs3.getString("MONTO_FIJO_PERIODO")== null ? "": rs3.getString("MONTO_FIJO_PERIODO"));
				registro.addDefCampo("ID_FORMA_DISTRIBUCION",rs3.getString("ID_FORMA_DISTRIBUCION")== null ? "": rs3.getString("ID_FORMA_DISTRIBUCION"));
				registro.addDefCampo("PORC_FLUJO_CAJA",rs3.getString("PORC_FLUJO_CAJA")== null ? "": rs3.getString("PORC_FLUJO_CAJA"));
				
				String sIdPrestamo = "";
				String sCvePrestamo = "";
				
				//SE OBTIENE EL SEQUENCE DEL PRESTAMO
				sSql = "SELECT SQ01_SIM_PRESTAMO.nextval as ID_PRESTAMO FROM DUAL";
				ejecutaSql();
				if (rs.next()){
					sIdPrestamo = rs.getString("ID_PRESTAMO");
				}
				
				
				if (registro.getDefCampo("CICLO").equals("igual")){
				
					//OBTIENE CLAVE DEL PRÉSTAMO.
					sSql =  "SELECT REPLACE (CVE_PRESTAMO,' ','') CVE_PRESTAMO FROM ( \n"+
							"SELECT TO_CHAR('" + (String)registro.getDefCampo("ID_PRODUCTO") + "','00')||TO_CHAR('" + (String)registro.getDefCampo("ID_GRUPO") + "','000000')||TO_CHAR('" + (String)registro.getDefCampo("ID_CLIENTE") + "','00000000')||TO_CHAR('" + (String)registro.getDefCampo("NUM_CICLO") + "','00') AS CVE_PRESTAMO FROM DUAL \n"+
							") \n";
					ejecutaSql();
					if (rs.next()){
						sCvePrestamo = rs.getString("CVE_PRESTAMO");
					}
				} else {
					//OBTIENE CLAVE DEL PRÉSTAMO.
					sSql =  "SELECT REPLACE (CVE_PRESTAMO,' ','') CVE_PRESTAMO FROM ( \n"+
							"SELECT TO_CHAR('" + (String)registro.getDefCampo("ID_PRODUCTO") + "','00')||TO_CHAR('" + (String)registro.getDefCampo("ID_GRUPO") + "','000000')||TO_CHAR('" + (String)registro.getDefCampo("ID_CLIENTE") + "','00000000')||TO_CHAR('" + (String)registro.getDefCampo("CICLO") + "','00') AS CVE_PRESTAMO FROM DUAL \n"+
							") \n";
					ejecutaSql();
					if (rs.next()){
						sCvePrestamo = rs.getString("CVE_PRESTAMO");
					}
				}
				
				sSql =  "SELECT \n"+
						"G.ID_GRUPO, \n"+
						"G.ID_SUCURSAL \n"+
						"FROM SIM_GRUPO G \n"+ 
						"WHERE G.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND G.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND G.ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n";
				ejecutaSql();	
				if(rs.next()){
					registro.addDefCampo("ID_SUCURSAL",rs.getString("ID_SUCURSAL")== null ? "": rs.getString("ID_SUCURSAL"));
				}
				System.out.println("h");
				sSql =  "INSERT INTO SIM_PRESTAMO ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_PRESTAMO, \n" +
						"CVE_PRESTAMO, \n" +
						"ID_PRODUCTO, \n" +
						"ID_CLIENTE, \n" +
						"ID_GRUPO, \n" +
						"ID_SUCURSAL, \n" +
						"NUM_CICLO, \n" +
						"CVE_METODO, \n" +
						"ID_PERIODICIDAD_PRODUCTO, \n" +
						"PLAZO, \n" +
						"TIPO_TASA, \n" +
						"VALOR_TASA, \n" +
						"ID_PERIODICIDAD_TASA, \n" +
						"ID_TASA_REFERENCIA, \n" +
						"MONTO_MINIMO, \n" +
						"MONTO_MAXIMO, \n" +
						"PORC_FLUJO_CAJA, \n" +
						"ID_FORMA_DISTRIBUCION, \n" +
						"FECHA_SOLICITUD, \n" +
						"B_ENTREGADO, \n" +
						"APLICA_A, \n" +
						"ID_TIPO_RECARGO, \n"+
						"TIPO_TASA_RECARGO, \n"+
						"TASA_RECARGO, \n"+
						"ID_PERIODICIDAD_TASA_RECARGO, \n"+
						"ID_TASA_REFERENCIA_RECARGO, \n"+
						"FACTOR_TASA_RECARGO, \n"+
						"MONTO_FIJO_PERIODO) \n"+
				        "VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					sIdPrestamo + ", \n "+
					"'" + sCvePrestamo + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_CLIENTE") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" ;
				
					if (registro.getDefCampo("CICLO").equals("igual")){
					
						sSql = sSql + "'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" ;
					}else {
						sSql = sSql + "'" + (String)registro.getDefCampo("CICLO") + "', \n" ;
					}
					sSql = sSql + "'" + (String)registro.getDefCampo("CVE_METODO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_PRODUCTO") + "', \n" +
					"'" + (String)registro.getDefCampo("PLAZO") + "', \n" +
					"'" + (String)registro.getDefCampo("TIPO_TASA") + "', \n" +
					"'" + (String)registro.getDefCampo("VALOR_TASA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "', \n" +
					"'" + (String)registro.getDefCampo("MONTO_MINIMO") + "', \n" +
					"'" + (String)registro.getDefCampo("MONTO_MAXIMO") + "', \n" +
					"'" + (String)registro.getDefCampo("PORC_FLUJO_CAJA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_FORMA_DISTRIBUCION") + "', \n" +
					"SYSDATE, \n" +
					"'F', \n" +
					"'" + (String)registro.getDefCampo("APLICA_A") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_TIPO_RECARGO") + "', \n" +
					"'" + (String)registro.getDefCampo("TIPO_TASA_RECARGO") + "', \n" + 
					"'" + (String)registro.getDefCampo("TASA_RECARGO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA_RECARGO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_TASA_REFERENCIA_RECARGO") + "', \n" +
					"'" + (String)registro.getDefCampo("FACTOR_TASA_RECARGO") + "', \n" +
					"'" + (String)registro.getDefCampo("MONTO_FIJO_PERIODO") + "') \n" ;
				
				//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
				PreparedStatement ps4 = this.conn.prepareStatement(sSql);
				ps4.execute();
				ResultSet rs4 = ps4.getResultSet();	
				
				//INGRESA LA FECHA DE LA TASA DE REFERENCIA SI EL TIPO DE REFERENCIA ES SI INDEXADA
				String sIndexada = "Si indexada";
				
				sSql = "SELECT \n"+
					"CVE_GPO_EMPRESA, \n"+
					"CVE_EMPRESA, \n"+
					"TIPO_TASA \n"+
					"FROM SIM_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_PRESTAMO = '" + sIdPrestamo + "' \n"+
					"AND TIPO_TASA = '"+sIndexada+"' \n";
				
				PreparedStatement ps5 = this.conn.prepareStatement(sSql);
				ps5.execute();
				ResultSet rs5 = ps5.getResultSet();	
				if(rs5.next()){	
					sSql = "UPDATE SIM_PRESTAMO SET "+
						"FECHA_TASA_REFERENCIA = (SELECT MAX(FECHA_PUBLICACION) FECHA_TASA_REFERENCIA \n"+
									"FROM ( \n"+
									"SELECT FECHA_PUBLICACION FROM SIM_CAT_TASA_REFER_DETALLE \n"+
									"WHERE ID_TASA_REFERENCIA = '" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "' \n" +
									//"AND FECHA_PUBLICACION < TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','yyyy-MM-dd HH24:MI:SSXFF') \n" +
									"AND FECHA_PUBLICACION < SYSDATE \n"+
									" ))\n"+
						"WHERE ID_PRESTAMO = '" + sIdPrestamo + "' \n" +
						"AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA")+"' \n" ;
					
					PreparedStatement ps6 = this.conn.prepareStatement(sSql);
					ps6.execute();
					ResultSet rs6 = ps6.getResultSet();	
					
				}
				
				sSql =  "SELECT \n"+
						"CVE_GPO_EMPRESA, \n"+
						"CVE_EMPRESA, \n"+
						"ID_CARGO_COMISION, \n"+
						"ID_PRODUCTO, \n"+
						"ID_FORMA_APLICACION, \n"+
						"CARGO_INICIAL, \n"+
						"PORCENTAJE_MONTO, \n"+
						"CANTIDAD_FIJA, \n"+
						"VALOR, \n"+
						"ID_UNIDAD, \n"+
						"ID_PERIODICIDAD \n"+
						"FROM \n"+
						"SIM_PRODUCTO_CARGO_COMISION \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
				
				PreparedStatement ps7 = this.conn.prepareStatement(sSql);
				ps7.execute();
				ResultSet rs7 = ps7.getResultSet();	
					while (rs7.next()){
						registro.addDefCampo("ID_CARGO_COMISION",rs7.getString("ID_CARGO_COMISION")== null ? "": rs7.getString("ID_CARGO_COMISION"));
						registro.addDefCampo("ID_FORMA_APLICACION",rs7.getString("ID_FORMA_APLICACION")== null ? "": rs7.getString("ID_FORMA_APLICACION"));
						registro.addDefCampo("CARGO_INICIAL",rs7.getString("CARGO_INICIAL")== null ? "": rs7.getString("CARGO_INICIAL"));
						registro.addDefCampo("PORCENTAJE_MONTO",rs7.getString("PORCENTAJE_MONTO")== null ? "": rs7.getString("PORCENTAJE_MONTO"));
						registro.addDefCampo("CANTIDAD_FIJA",rs7.getString("CANTIDAD_FIJA")== null ? "": rs7.getString("CANTIDAD_FIJA"));
						registro.addDefCampo("VALOR",rs7.getString("VALOR")== null ? "": rs7.getString("VALOR"));
						registro.addDefCampo("ID_UNIDAD",rs7.getString("ID_UNIDAD")== null ? "": rs7.getString("ID_UNIDAD"));
						registro.addDefCampo("ID_PERIODICIDAD",rs7.getString("ID_PERIODICIDAD")== null ? "": rs7.getString("ID_PERIODICIDAD"));
						
						sSql = "INSERT INTO SIM_PRESTAMO_CARGO_COMISION ( \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_PRESTAMO, \n" +
							"ID_CARGO_COMISION, \n" +
							"ID_FORMA_APLICACION, \n"+
							"CARGO_INICIAL, \n"+
							"PORCENTAJE_MONTO, \n"+
							"CANTIDAD_FIJA, \n"+
							"VALOR, \n"+
							"ID_UNIDAD, \n"+
							"ID_PERIODICIDAD) \n"+
							" VALUES (" +
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"'" + sIdPrestamo + "', \n" +
							"'" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_FORMA_APLICACION") + "', \n" +
							"'" + (String)registro.getDefCampo("CARGO_INICIAL") + "', \n" +
							"'" + (String)registro.getDefCampo("PORCENTAJE_MONTO") + "', \n" +
							"'" + (String)registro.getDefCampo("CANTIDAD_FIJA") + "', \n" +
							"'" + (String)registro.getDefCampo("VALOR") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_UNIDAD") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "') \n" ;
						
						PreparedStatement ps8 = this.conn.prepareStatement(sSql);
						ps8.execute();
						ResultSet rs8 = ps8.getResultSet();	
					}	
					
					sSql =  "SELECT \n"+
							"CVE_GPO_EMPRESA, \n"+
							"CVE_EMPRESA, \n"+
							"ID_CARGO_COMISION, \n"+
							"ID_PRODUCTO, \n"+
							"ID_FORMA_APLICACION, \n"+
							"CARGO_INICIAL, \n"+
							"PORCENTAJE_MONTO, \n"+
							"CANTIDAD_FIJA, \n"+
							"VALOR, \n"+
							"ID_UNIDAD, \n"+
							"ID_PERIODICIDAD \n"+
							"FROM \n"+
							"SIM_PRODUCTO_CARGO_COMISION \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
							"AND ID_FORMA_APLICACION = '1' \n"+
							"AND ID_CARGO_COMISION = '9' \n";
					PreparedStatement ps8 = this.conn.prepareStatement(sSql);
					ps8.execute();
					ResultSet rs8 = ps8.getResultSet();
					
					if (!rs8.next()){
						sSql = "INSERT INTO SIM_PRESTAMO_CARGO_COMISION ( \n"+
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_PRESTAMO, \n" +
								"ID_CARGO_COMISION, \n" +
								"ID_FORMA_APLICACION, \n"+
								"CARGO_INICIAL) \n"+
								" VALUES (" +
								"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + sIdPrestamo + "', \n" +
								"'9', \n" +
								"'1', \n" +
								"'0') \n" ;
						
						PreparedStatement ps9 = this.conn.prepareStatement(sSql);
						ps9.execute();
						ResultSet rs9 = ps9.getResultSet();
					}
				
					
					sSql =  "SELECT \n"+
							"CVE_GPO_EMPRESA, \n"+
							"CVE_EMPRESA, \n"+
							"ID_PRODUCTO, \n"+
							"NUM_CICLO, \n"+
							"ID_ACCESORIO, \n"+
							"ORDEN \n"+
							"FROM \n"+
							"SIM_PRODUCTO_CICLO_ACCESORIO \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
							"AND NUM_CICLO = '" + (String)registro.getDefCampo("NUM_CICLO") + "' \n";
				
					PreparedStatement ps10 = this.conn.prepareStatement(sSql);
					ps10.execute();
					ResultSet rs10 = ps10.getResultSet();
						while (rs10.next()){
							registro.addDefCampo("ID_ACCESORIO",rs10.getString("ID_ACCESORIO")== null ? "": rs10.getString("ID_ACCESORIO"));
							registro.addDefCampo("ORDEN",rs10.getString("ORDEN")== null ? "": rs10.getString("ORDEN"));
									
							sSql = "INSERT INTO SIM_PRESTAMO_ACCESORIO ( \n"+
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_PRESTAMO, \n" +
								"ID_ACCESORIO, \n" +
								"ORDEN) \n" +
								" VALUES (" +
								"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + sIdPrestamo + "', \n" +
								"'" + (String)registro.getDefCampo("ID_ACCESORIO") + "', \n" +
								"'" + (String)registro.getDefCampo("ORDEN") + "') \n" ;
							
							PreparedStatement ps11 = this.conn.prepareStatement(sSql);
							ps11.execute();
							ResultSet rs11 = ps11.getResultSet();		
						}
						
						//Consulta las etapas-actividades del producto, para insertarlas en SIM_PRESTAMO_ETAPA.
						sSql =  "SELECT \n"+
								"CVE_GPO_EMPRESA, \n"+
								"CVE_EMPRESA, \n"+
								"ID_PRODUCTO, \n"+
								"ID_ACTIVIDAD_REQUISITO, \n"+
								"ID_ETAPA_PRESTAMO, \n"+
								"ORDEN_ETAPA \n"+
								"FROM \n"+
								"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
					
						PreparedStatement ps12 = this.conn.prepareStatement(sSql);
						ps12.execute();
						ResultSet rs12 = ps12.getResultSet();	
							
						while (rs12.next()){
							registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs12.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs12.getString("ID_ACTIVIDAD_REQUISITO"));
							registro.addDefCampo("ID_ETAPA_PRESTAMO",rs12.getString("ID_ETAPA_PRESTAMO")== null ? "": rs12.getString("ID_ETAPA_PRESTAMO"));
							registro.addDefCampo("ORDEN_ETAPA",rs12.getString("ORDEN_ETAPA")== null ? "": rs12.getString("ORDEN_ETAPA"));
							
							sSql = "INSERT INTO SIM_PRESTAMO_ETAPA ( \n"+
									"CVE_GPO_EMPRESA, \n" +
									"CVE_EMPRESA, \n" +
									"ID_PRESTAMO, \n" +
									"ID_ACTIVIDAD_REQUISITO, \n" +
									"ID_ETAPA_PRESTAMO, \n"+
									"ORDEN_ETAPA) \n"+
									" VALUES (" +
									"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
									"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
									"'" + sIdPrestamo + "', \n" +
									"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
									"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
									"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "') \n" ;
						
							PreparedStatement ps13 = this.conn.prepareStatement(sSql);
							ps13.execute();
							ResultSet rs13 = ps13.getResultSet();
									
						}//Consulta las etapas-actividades del producto, para insertarlas en SIM_PRESTAMO_ETAPA.
						
						
						
						
						
							//SE OBTIENE EL SEQUENCE DE LA CUENTA
							sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
							ejecutaSql();
							if (rs.next()){
								registro.addDefCampo("ID_CUENTA",rs.getString("ID_CUENTA"));
								
								sSql = "INSERT INTO PFIN_CUENTA ( \n"+
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_CUENTA, \n" +
								"SIT_CUENTA, \n" +
								"CVE_TIP_CUENTA, \n" +
								"B_RETIENE_IMPUESTO, \n"+
								"ID_TITULAR) \n"+
								" VALUES (" +
								"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_CUENTA") + "', \n" +
								"'AC', \n" +
								"'CREDITO', \n" +
								"'F', \n" +
								"'" + (String)registro.getDefCampo("ID_CLIENTE") + "') \n" ;
							
								if (ejecutaUpdate() == 0){
									resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
								}	
								
								sSql =  " UPDATE SIM_PRESTAMO SET "+
									" ID_CUENTA			='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
									" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
									" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
							
								//VERIFICA SI DIO DE ALTA EL REGISTRO
								if (ejecutaUpdate() == 0){
									resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
								}	
							}
							
							
							sSql =  "SELECT \n"+
									"CVE_GPO_EMPRESA, \n"+
									"CVE_EMPRESA, \n"+
									"ID_CUENTA \n"+
									"FROM \n"+
									"PFIN_CUENTA \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_TITULAR = '" + (String)registro.getDefCampo("ID_CLIENTE") + "' \n"+
									"AND CVE_TIP_CUENTA = 'VISTA' \n";
							
							PreparedStatement ps14 = this.conn.prepareStatement(sSql);
							ps14.execute();
							ResultSet rs14 = ps14.getResultSet();
						if (!rs14.next()){
							sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
							
							ejecutaSql();
							if (rs.next()){
								registro.addDefCampo("ID_CUENTA",rs.getString("ID_CUENTA"));
								
								sSql = "INSERT INTO PFIN_CUENTA ( \n"+
									"CVE_GPO_EMPRESA, \n" +
									"CVE_EMPRESA, \n" +
									"ID_CUENTA, \n" +
									"SIT_CUENTA, \n" +
									"CVE_TIP_CUENTA, \n" +
									"B_RETIENE_IMPUESTO, \n"+
									"ID_TITULAR) \n"+
									" VALUES (" +
									"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
									"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
									"'" + (String)registro.getDefCampo("ID_CUENTA") + "', \n" +
									"'AC', \n" +
									"'VISTA', \n" +
									"'V', \n" +
									"'" + (String)registro.getDefCampo("ID_CLIENTE") + "') \n" ;
								
								PreparedStatement ps15 = this.conn.prepareStatement(sSql);
								ps15.execute();
								ResultSet rs15 = ps15.getResultSet();
								
								sSql =  " UPDATE SIM_PRESTAMO SET "+
									" ID_CUENTA_REFERENCIA 		='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
									" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
									" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
								
								//VERIFICA SI DIO DE ALTA EL REGISTRO
								PreparedStatement ps16 = this.conn.prepareStatement(sSql);
								ps16.execute();
								ResultSet rs16 = ps16.getResultSet();
							
							}
						}else{
							registro.addDefCampo("ID_CUENTA",rs14.getString("ID_CUENTA")== null ? "": rs14.getString("ID_CUENTA"));
							
							sSql =  " UPDATE SIM_PRESTAMO SET "+
									" ID_CUENTA_REFERENCIA 		='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
									" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
									" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
							
								//VERIFICA SI DIO DE ALTA EL REGISTRO
								PreparedStatement ps17 = this.conn.prepareStatement(sSql);
								ps17.execute();
								ResultSet rs17 = ps17.getResultSet();
						
						}
					
						sSql =  "SELECT \n"+
								"CVE_GPO_EMPRESA, \n"+
								"CVE_EMPRESA, \n"+
								"ID_PRODUCTO, \n"+
								"ID_ARCHIVO_FLUJO, \n"+
								"NOM_ARCHIVO \n"+
								"FROM \n"+
								"SIM_PRODUCTO_FLUJO_EFECTIVO \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
					
						PreparedStatement ps18 = this.conn.prepareStatement(sSql);
						ps18.execute();
						ResultSet rs18 = ps18.getResultSet();
								
								
						while (rs18.next()){
							registro.addDefCampo("ID_ARCHIVO_FLUJO",rs18.getString("ID_ARCHIVO_FLUJO")== null ? "": rs18.getString("ID_ARCHIVO_FLUJO"));
							registro.addDefCampo("NOM_ARCHIVO",rs18.getString("NOM_ARCHIVO")== null ? "": rs18.getString("NOM_ARCHIVO"));
									
							sSql =  "INSERT INTO SIM_PRESTAMO_FLUJO_EFECTIVO ( \n"+
									"CVE_GPO_EMPRESA, \n" +
									"CVE_EMPRESA, \n" +
									"ID_PRESTAMO, \n" +
									"ID_ARCHIVO_FLUJO, \n" +
									"NOM_ARCHIVO) \n"+
									" VALUES (" +
									"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
									"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
									"'" + sIdPrestamo + "', \n" +
									"'" + (String)registro.getDefCampo("ID_ARCHIVO_FLUJO") + "', \n" +
									"'" + (String)registro.getDefCampo("NOM_ARCHIVO") + "') \n" ;
							
							PreparedStatement ps19 = this.conn.prepareStatement(sSql);
							ps19.execute();
							ResultSet rs19 = ps19.getResultSet();
						}
						
								
								
						sSql =  "SELECT \n"+
								"CVE_GPO_EMPRESA, \n"+
								"CVE_EMPRESA, \n"+
								"ID_PRODUCTO, \n"+
								"ID_DOCUMENTO \n"+
								"FROM \n"+
								"SIM_PRODUCTO_DOCUMENTACION \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
						
						PreparedStatement ps20 = this.conn.prepareStatement(sSql);
						ps20.execute();
						ResultSet rs20 = ps20.getResultSet();
						while (rs20.next()){
							registro.addDefCampo("ID_DOCUMENTO",rs20.getString("ID_DOCUMENTO")== null ? "": rs20.getString("ID_DOCUMENTO"));
								
							sSql =  "INSERT INTO SIM_PRESTAMO_DOCUMENTACION ( \n"+
									"CVE_GPO_EMPRESA, \n" +
									"CVE_EMPRESA, \n" +
									"ID_PRESTAMO, \n" +
									"ID_DOCUMENTO) \n" +
									" VALUES (" +
									"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
									"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
									"'" + sIdPrestamo + "', \n" +
									"'" + (String)registro.getDefCampo("ID_DOCUMENTO") + "') \n" ;
							
							PreparedStatement ps21 = this.conn.prepareStatement(sSql);
							ps21.execute();
							ResultSet rs21 = ps21.getResultSet();
						}	
						//Obtiene la primer etapa.
						sSql =  "SELECT DISTINCT \n"+
								"ID_ETAPA_PRESTAMO \n"+
								//"ID_ACTIVIDAD_REQUISITO \n"+
								"FROM \n"+
								"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
								"AND ORDEN_ETAPA = '1' \n";
						
						PreparedStatement ps22 = this.conn.prepareStatement(sSql);
						ps22.execute();
						ResultSet rs22 = ps22.getResultSet();
						if (rs22.next()){
							registro.addDefCampo("ESTATUS_PRESTAMO",rs22.getString("ID_ETAPA_PRESTAMO")== null ? "": rs22.getString("ID_ETAPA_PRESTAMO"));
							//registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs22.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs22.getString("ID_ACTIVIDAD_REQUISITO"));
						}
						//Actualiza la primer etapa.
						sSql =  " UPDATE SIM_PRESTAMO_GRUPO SET "+
						" ID_ETAPA_PRESTAMO 		='" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "' \n" +
						" WHERE ID_PRESTAMO_GRUPO		='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
						" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						PreparedStatement ps23 = this.conn.prepareStatement(sSql);
						ps23.execute();
						ResultSet rs23 = ps23.getResultSet();
						
						
						sSql =  "INSERT INTO SIM_PRESTAMO_GPO_DET ( "+
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_PRESTAMO_GRUPO, \n" +
								"ID_PRESTAMO, \n" +
								"ID_INTEGRANTE, \n" +
								"ID_ETAPA_PRESTAMO, \n" +
								"MONTO_SOLICITADO) \n" +
								" VALUES (" +
								"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "', \n" +
								"'" + sIdPrestamo + "', \n" +
								"'" + (String)registro.getDefCampo("ID_CLIENTE") + "', \n" +
								"'" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "', \n" +
								"'" + (String)registro.getDefCampo("MONTO_SOLICITADO") + "') \n" ;
				
						PreparedStatement ps24 = this.conn.prepareStatement(sSql);
						ps24.execute();
						ResultSet rs24 = ps24.getResultSet();
						
						//Actualiza la etapa del crédito a la primer etapa del producto.
						sSql =  " UPDATE SIM_PRESTAMO SET "+
						" ID_ETAPA_PRESTAMO 		='" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "' \n" +
						" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
						" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						PreparedStatement ps25 = this.conn.prepareStatement(sSql);
						ps25.execute();
						ResultSet rs25 = ps25.getResultSet();
						
						//Actualiza los atributos de fecha de registro y estatus de las actividades de la etapa 1.
						sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
								" FECHA_REGISTRO 		= SYSDATE, \n" +
								" ESTATUS 				= 'Registrada', \n" +
								" CVE_USUARIO 			='" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n" +
								" WHERE ID_PRESTAMO 	='" + sIdPrestamo + "' \n" +
								" AND ID_ETAPA_PRESTAMO ='" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "' \n" +
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						PreparedStatement ps26 = this.conn.prepareStatement(sSql);
						ps26.execute();
						ResultSet rs26 = ps26.getResultSet();
						
					
						//Actualiza la bitácora.
						sSql =  "SELECT \n"+
							"CVE_GPO_EMPRESA, \n"+
							"CVE_EMPRESA, \n"+
							"ID_PRESTAMO, \n"+
							"ID_ACTIVIDAD_REQUISITO, \n"+
							"ID_ETAPA_PRESTAMO, \n"+
							"FECHA_REGISTRO, \n"+
							"ORDEN_ETAPA, \n"+
							"ESTATUS \n"+
							"FROM \n"+
							"SIM_PRESTAMO_ETAPA \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRESTAMO = '" + sIdPrestamo + "' \n" ;
						
						PreparedStatement ps27 = this.conn.prepareStatement(sSql);
						ps27.execute();
						ResultSet rs27 = ps27.getResultSet();
						
						while (rs27.next()){
							registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs27.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs27.getString("ID_ACTIVIDAD_REQUISITO"));
							registro.addDefCampo("ID_ETAPA_PRESTAMO",rs27.getString("ID_ETAPA_PRESTAMO")== null ? "": rs27.getString("ID_ETAPA_PRESTAMO"));
							registro.addDefCampo("ORDEN_ETAPA",rs27.getString("ORDEN_ETAPA")== null ? "": rs27.getString("ORDEN_ETAPA"));
							registro.addDefCampo("FECHA_REGISTRO",rs27.getString("FECHA_REGISTRO")== null ? "": rs27.getString("FECHA_REGISTRO"));
							registro.addDefCampo("ESTATUS",rs27.getString("ESTATUS")== null ? "": rs27.getString("ESTATUS"));
							
							sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
						
							PreparedStatement ps28 = this.conn.prepareStatement(sSql);
							ps28.execute();
							ResultSet rs28 = ps28.getResultSet();	
							if (rs28.next()){
								
								registro.addDefCampo("ID_HISTORICO",rs28.getString("ID_HISTORICO"));
								
								sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
									"CVE_GPO_EMPRESA, \n" +
									"CVE_EMPRESA, \n" +
									"ID_HISTORICO, \n" +
									"ID_PRESTAMO, \n" +
									"ID_ACTIVIDAD_REQUISITO, \n" +
									"ID_ETAPA_PRESTAMO, \n"+
									"FECHA_REGISTRO, \n"+
									"ORDEN_ETAPA, \n"+
									"ESTATUS) \n" +
									" VALUES (" +
									"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
									"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
									"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
									"'" + sIdPrestamo + "', \n" +
									"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
									"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
									"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
									"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
									"'" + (String)registro.getDefCampo("ESTATUS") + "') \n" ;
								
								PreparedStatement ps29 = this.conn.prepareStatement(sSql);
								ps29.execute();
								ResultSet rs29 = ps29.getResultSet();
							}
						}//Actualiza la bitácora.
						
						
						
						sSql =  "INSERT INTO SIM_CLIENTE_MONTO ( \n" +
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_PRESTAMO, \n" +
								"ID_CLIENTE, \n" +
								"ID_PRODUCTO, \n" +
								"NUM_CICLO, \n" +
								"MONTO_SOLICITADO) \n" +
								" VALUES (" +
								"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + sIdPrestamo + "', \n" +
								"'" + (String)registro.getDefCampo("ID_CLIENTE") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" ;
						
								if (registro.getDefCampo("CICLO").equals("igual")){
								
									sSql = sSql + "'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" ;
								}else {
									sSql = sSql + "'" + (String)registro.getDefCampo("CICLO") + "', \n" ;
								}
								sSql = sSql + "'" + (String)registro.getDefCampo("MONTO_SOLICITADO") + "') \n" ;
								
						
						PreparedStatement ps30 = this.conn.prepareStatement(sSql);
						ps30.execute();
						ResultSet rs30= ps30.getResultSet();
					
			}
		}//Fin de credito individual.
		
		return resultadoCatalogo;
	}
	
	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
			ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		if (registro.getDefCampo("MOVIMIENTO").equals("MODIFICACION")) {
			//Ingresa los montos solicitados.
			sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET "+
					" MONTO_SOLICITADO		='" + (String)registro.getDefCampo("MONTO_SOLICITADO") + "' \n" +
					" WHERE ID_PRESTAMO_GRUPO  		='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
					" AND ID_PRESTAMO   		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
					" AND ID_INTEGRANTE   		='" + (String)registro.getDefCampo("ID_CLIENTE") + "' \n"+
					" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			
			//Valida si el usuario tiene la función de la etapa.
			sSql =  " SELECT \n"+
					"CVE_FUNCION, \n"+
					"CVE_APLICACION, \n"+
					"CVE_PERFIL \n"+
					"FROM RS_CONF_APLICACION_CONFIG \n"+
					"WHERE CVE_FUNCION = (SELECT \n"+
					"CVE_FUNCION \n"+
					"FROM \n"+
					"SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					") \n"+
					"AND CVE_APLICACION = 'Prestamo' \n"+
					"AND CVE_PERFIL = ( \n"+
					"SELECT \n"+
					"CVE_PERFIL \n"+ 
					"FROM RS_GRAL_USUARIO_PERFIL \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_USUARIO = '" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
					"AND CVE_APLICACION = 'Prestamo') \n";
			
			ejecutaSql();
			if(rs.next()){
			
				if (registro.getDefCampo("B_AVANZAR").equals("F")){
					//La etapa NO es avanzada individualmente, por lo que sólo se actualiza el comentario.
					sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
							" COMENTARIO 		='" + (String)registro.getDefCampo("COMENTARIO") + "' \n"+
							" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					ps1.execute();
					ResultSet rs1 = ps1.getResultSet();
					
					sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET "+
							" COMENTARIO 		='" + (String)registro.getDefCampo("COMENTARIO") + "' \n"+
							" WHERE ID_PRESTAMO_GRUPO		='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
							" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
							" AND ID_PRESTAMO	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
					PreparedStatement ps2 = this.conn.prepareStatement(sSql);
					ps2.execute();
					ResultSet rs2 = ps2.getResultSet();
					
					sSql =  " UPDATE SIM_PRESTAMO_ETAPA_HISTORICO SET "+
							" COMENTARIO 		='" + (String)registro.getDefCampo("COMENTARIO") + "' \n"+
							" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
						
					PreparedStatement ps3 = this.conn.prepareStatement(sSql);
					ps3.execute();
					ResultSet rs3 = ps3.getResultSet();
			
				}else {
					
					//Avanza la etapa.
					//Obtiene las actividades de la etapa que se completa.
					sSql =  "SELECT \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_ETAPA_PRESTAMO, \n"+
							"ID_ACTIVIDAD_REQUISITO, \n" +
							"FECHA_REGISTRO, \n" +
							"ORDEN_ETAPA \n" +
							"FROM \n" +
							"SIM_PRESTAMO_ETAPA \n" +
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
							"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
					
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					ps1.execute();
					ResultSet rs1 = ps1.getResultSet();
					
					while (rs1.next()){
						registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs1.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs1.getString("ID_ACTIVIDAD_REQUISITO"));
						registro.addDefCampo("ORDEN_ETAPA",rs1.getString("ORDEN_ETAPA")== null ? "": rs1.getString("ORDEN_ETAPA"));
						registro.addDefCampo("FECHA_REGISTRO",rs1.getString("FECHA_REGISTRO")== null ? "": rs1.getString("FECHA_REGISTRO"));
						//Guarda el historial de la etapa y las actividades
						sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
						PreparedStatement ps2 = this.conn.prepareStatement(sSql);
						ps2.execute();
						ResultSet rs2 = ps2.getResultSet();
						if (rs2.next()){
							registro.addDefCampo("ID_HISTORICO",rs2.getString("ID_HISTORICO"));
							
							sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_HISTORICO, \n" +
								"ID_PRESTAMO, \n" +
								"ID_ACTIVIDAD_REQUISITO, \n" +
								"ID_ETAPA_PRESTAMO, \n"+
								"FECHA_REALIZADA, \n"+
								"FECHA_REGISTRO, \n"+
								"ESTATUS, \n"+
								"CVE_USUARIO, \n"+
								"ORDEN_ETAPA) \n"+
								" VALUES (" +
								"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
								"SYSDATE, \n"+
								"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
								"'Completada', \n"+
								"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
								"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "') \n" ;
							
							if (ejecutaUpdate() == 0){
								resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
							}
						}
					}
					
					//Busca la siguiente etapa.
					sSql =  " SELECT DISTINCT ID_ETAPA_PRESTAMO ETAPA_PRESTAMO_NUEVA, \n"+
							" ORDEN_ETAPA \n"+
							"	  FROM SIM_PRESTAMO_ETAPA \n"+
							"	  WHERE ORDEN_ETAPA = (SELECT DISTINCT \n"+
							"	  ORDEN_ETAPA + 1 \n"+
							" 	  FROM SIM_PRESTAMO_ETAPA \n"+
							"	  WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"	  AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							"	  AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n"+
							"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
					
					ejecutaSql();
					if(rs.next()){
						registro.addDefCampo("ID_ETAPA_PRESTAMO_NUEVA",rs.getString("ETAPA_PRESTAMO_NUEVA"));
						registro.addDefCampo("ORDEN_ETAPA",rs.getString("ORDEN_ETAPA"));
						//Actualiza la etapa
						sSql =  " UPDATE SIM_PRESTAMO SET \n"+
								"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO_NUEVA") + "' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
							
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
						
						sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET \n"+
								"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO_NUEVA") + "', \n"+
								" COMENTARIO 		='' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
								
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
						
						sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
								" FECHA_REGISTRO 		=SYSDATE, \n" +
								" ESTATUS 	 			='Registrada', \n" +
								" COMENTARIO 	 		='' \n" +
								" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO_NUEVA") + "' \n";
						
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
						
						
						sSql =  "SELECT \n"+
								"CVE_GPO_EMPRESA, \n"+
								"CVE_EMPRESA, \n"+
								"ID_PRESTAMO, \n"+
								"ID_ACTIVIDAD_REQUISITO, \n"+
								"ID_ETAPA_PRESTAMO, \n"+
								"FECHA_REGISTRO, \n"+
								"FECHA_REALIZADA, \n"+
								"COMENTARIO, \n"+
								"ORDEN_ETAPA, \n"+
								"ESTATUS \n" +
								"FROM \n"+
								"SIM_PRESTAMO_ETAPA \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
								"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO_NUEVA") + "' \n";
							
							PreparedStatement ps2 = this.conn.prepareStatement(sSql);
							ps2.execute();
							ResultSet rs2 = ps2.getResultSet();
							
							while (rs2.next()){
								registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs2.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs2.getString("ID_ACTIVIDAD_REQUISITO"));
								registro.addDefCampo("ID_ETAPA_PRESTAMO",rs2.getString("ID_ETAPA_PRESTAMO")== null ? "": rs2.getString("ID_ETAPA_PRESTAMO"));
								registro.addDefCampo("ORDEN_ETAPA",rs2.getString("ORDEN_ETAPA")== null ? "": rs2.getString("ORDEN_ETAPA"));
								registro.addDefCampo("FECHA_REGISTRO",rs2.getString("FECHA_REGISTRO")== null ? "": rs2.getString("FECHA_REGISTRO"));
								registro.addDefCampo("COMENTARIO",rs2.getString("COMENTARIO")== null ? "": rs2.getString("COMENTARIO"));
								registro.addDefCampo("ESTATUS",rs2.getString("ESTATUS")== null ? "": rs2.getString("ESTATUS"));
							
								sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
								
								PreparedStatement ps3 = this.conn.prepareStatement(sSql);
								ps3.execute();
								ResultSet rs3 = ps3.getResultSet();	
								if (rs3.next()){
									registro.addDefCampo("ID_HISTORICO",rs3.getString("ID_HISTORICO"));
									//Actualiza la bitácora.
									sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
										"CVE_GPO_EMPRESA, \n" +
										"CVE_EMPRESA, \n" +
										"ID_HISTORICO, \n" +
										"ID_PRESTAMO, \n" +
										"ID_ACTIVIDAD_REQUISITO, \n" +
										"ID_ETAPA_PRESTAMO, \n"+
										"FECHA_REGISTRO, \n"+
										"ORDEN_ETAPA, \n"+
										"COMENTARIO, \n"+
										"ESTATUS) \n" +
										" VALUES (" +
										"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
										"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
										"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
										"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
										"'" + (String)registro.getDefCampo("COMENTARIO") + "', \n" +
										"'" + (String)registro.getDefCampo("ESTATUS") + "') \n" ;
								
									PreparedStatement ps4 = this.conn.prepareStatement(sSql);
									ps4.execute();
									ResultSet rs4 = ps4.getResultSet();
								}
							}
						
						
						
						
						
						sSql = "SELECT DISTINCT D.ID_ETAPA_PRESTAMO \n" +
						"		FROM SIM_PRESTAMO_GPO_DET D, \n" +
						"		SIM_PRESTAMO_ETAPA E \n" +
						"		WHERE D.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"		AND D.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
						"		AND D.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +    
						"		AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
						"		AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" + 
						"		AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
						"		AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO \n" +
						"		AND E.ORDEN_ETAPA = ( \n" +
						"		SELECT MIN(ORDEN_ETAPA) FROM \n" +
						"		(SELECT D.ID_ETAPA_PRESTAMO, E.ORDEN_ETAPA \n" +
						"		FROM SIM_PRESTAMO_GPO_DET D, \n" + 
						"		SIM_PRESTAMO_ETAPA E \n" +
						"		WHERE D.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"		AND D.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
						"		AND D.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +    
						"		AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
						"		AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" + 
						"		AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
						"		AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO)) \n";
						ejecutaSql();
						if(rs.next()){
							registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO"));
							//Actualiza la etapa
							sSql =  " UPDATE SIM_PRESTAMO_GRUPO SET \n"+
									"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
								
							if (ejecutaUpdate() == 0){
								resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
							}
						}
						
						
						
						
						
					}
				}//Avanza la etapa.
			}//Tiene permisos para manipular la etapa.
			
		}else if (registro.getDefCampo("MOVIMIENTO").equals("REGRESAR_ETAPA")) {
			//Verifica si tiene permisos para manipular la etapa.
			sSql =  " SELECT \n"+
					"CVE_FUNCION, \n"+
					"CVE_APLICACION, \n"+
					"CVE_PERFIL \n"+
					"FROM RS_CONF_APLICACION_CONFIG \n"+
					"WHERE CVE_FUNCION = (SELECT \n"+
					"CVE_FUNCION \n"+
					"FROM \n"+
					"SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					") \n"+
					"AND CVE_APLICACION = 'Prestamo' \n"+
					"AND CVE_PERFIL = ( \n"+
					"SELECT \n"+
					"CVE_PERFIL \n"+ 
					"FROM RS_GRAL_USUARIO_PERFIL \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_USUARIO = '" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
					"AND CVE_APLICACION = 'Prestamo') \n";
			ejecutaSql();
			if(rs.next()){
				
				//Retrocede etapa.
				//Obtiene las actividades de la etapa que se retroce para inicializarlas.
				sSql =  "SELECT \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_ETAPA_PRESTAMO, \n"+
						"ID_ACTIVIDAD_REQUISITO, \n" +
						"FECHA_REGISTRO, \n" +
						"ORDEN_ETAPA \n" +
						"FROM \n" +
						"SIM_PRESTAMO_ETAPA \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
						"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
				
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ResultSet rs2 = ps2.getResultSet();
				
				while (rs2.next()){
					registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs2.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs2.getString("ID_ACTIVIDAD_REQUISITO"));
					registro.addDefCampo("ORDEN_ETAPA",rs2.getString("ORDEN_ETAPA")== null ? "": rs2.getString("ORDEN_ETAPA"));
					registro.addDefCampo("FECHA_REGISTRO",rs2.getString("FECHA_REGISTRO")== null ? "": rs2.getString("FECHA_REGISTRO"));
					//Guarda el historial de la etapa y las actividades
					sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
					PreparedStatement ps3 = this.conn.prepareStatement(sSql);
					ps3.execute();
					ResultSet rs3 = ps3.getResultSet();
					if (rs3.next()){
						registro.addDefCampo("ID_HISTORICO",rs3.getString("ID_HISTORICO"));
						
						sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_HISTORICO, \n" +
							"ID_PRESTAMO, \n" +
							"ID_ACTIVIDAD_REQUISITO, \n" +
							"ID_ETAPA_PRESTAMO, \n"+
							"FECHA_REALIZADA, \n"+
							"FECHA_REGISTRO, \n"+
							"ESTATUS, \n"+
							"CVE_USUARIO, \n"+
							"ORDEN_ETAPA) \n"+
							" VALUES (" +
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
							"'', \n"+
							"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
							"'Regresar etapa', \n"+
							"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
							"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "') \n" ;
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
					}
				}
					
				//Obtiene la etapa anterior.
				sSql =  " SELECT DISTINCT ID_ETAPA_PRESTAMO, ORDEN_ETAPA \n"+
					"	  FROM SIM_PRESTAMO_ETAPA \n"+
					"	  WHERE ORDEN_ETAPA = (SELECT DISTINCT \n"+
					"	  ORDEN_ETAPA - 1 \n"+
					" 	  FROM SIM_PRESTAMO_ETAPA \n"+
					"	  WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"	  AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
					"	  AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n"+
					" 	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
					
					ejecutaSql();
					if(rs.next()){
						//Retrocede a la etapa anterior.
						registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO"));
						registro.addDefCampo("ORDEN_ETAPA",rs.getString("ORDEN_ETAPA"));
						
						sSql =  " UPDATE SIM_PRESTAMO SET \n"+
							"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
							
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
						
						sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET \n"+
								"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
								
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
						
						sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
								" FECHA_REGISTRO 		=SYSDATE, \n" +
								" ESTATUS 	 		='Registrada', \n" +
								" COMENTARIO 	 		='' \n" +
								" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
						
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
						
						sSql =  "SELECT \n"+
								"CVE_GPO_EMPRESA, \n"+
								"CVE_EMPRESA, \n"+
								"ID_PRESTAMO, \n"+
								"ID_ACTIVIDAD_REQUISITO, \n"+
								"ID_ETAPA_PRESTAMO, \n"+
								"FECHA_REGISTRO, \n"+
								"FECHA_REALIZADA, \n"+
								"COMENTARIO, \n"+
								"ORDEN_ETAPA, \n"+
								"ESTATUS \n" +
								"FROM \n"+
								"SIM_PRESTAMO_ETAPA \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
								"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
						
							PreparedStatement ps10 = this.conn.prepareStatement(sSql);
							ps10.execute();
							ResultSet rs10 = ps10.getResultSet();
							
							while (rs10.next()){
								registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs10.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs10.getString("ID_ACTIVIDAD_REQUISITO"));
								registro.addDefCampo("ID_ETAPA_PRESTAMO",rs10.getString("ID_ETAPA_PRESTAMO")== null ? "": rs10.getString("ID_ETAPA_PRESTAMO"));
								registro.addDefCampo("ORDEN_ETAPA",rs10.getString("ORDEN_ETAPA")== null ? "": rs10.getString("ORDEN_ETAPA"));
								registro.addDefCampo("FECHA_REGISTRO",rs10.getString("FECHA_REGISTRO")== null ? "": rs10.getString("FECHA_REGISTRO"));
								registro.addDefCampo("COMENTARIO",rs10.getString("COMENTARIO")== null ? "": rs10.getString("COMENTARIO"));
								registro.addDefCampo("ESTATUS",rs10.getString("ESTATUS")== null ? "": rs10.getString("ESTATUS"));
							
								sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
								
								PreparedStatement ps11 = this.conn.prepareStatement(sSql);
								ps11.execute();
								ResultSet rs11 = ps11.getResultSet();	
								if (rs11.next()){
									registro.addDefCampo("ID_HISTORICO",rs11.getString("ID_HISTORICO"));
									//Actualiza la bitácora.
									sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
										"CVE_GPO_EMPRESA, \n" +
										"CVE_EMPRESA, \n" +
										"ID_HISTORICO, \n" +
										"ID_PRESTAMO, \n" +
										"ID_ACTIVIDAD_REQUISITO, \n" +
										"ID_ETAPA_PRESTAMO, \n"+
										"FECHA_REGISTRO, \n"+
										"ORDEN_ETAPA, \n"+
										"COMENTARIO, \n"+
										"ESTATUS) \n" +
										" VALUES (" +
										"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
										"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
										"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
										"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
										"'" + (String)registro.getDefCampo("COMENTARIO") + "', \n" +
										"'" + (String)registro.getDefCampo("ESTATUS") + "') \n" ;
									
									PreparedStatement ps12 = this.conn.prepareStatement(sSql);
									ps12.execute();
									ResultSet rs12 = ps12.getResultSet();
								}
							}
						
						
						
						sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET "+
								" COMENTARIO 		='' \n"+
								" WHERE ID_PRESTAMO_GRUPO		='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
								" AND ID_PRESTAMO	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
					}
				}	
			}else if (registro.getDefCampo("MOVIMIENTO").equals("ACTUALIZA_A_ETAPA_ANTERIOR")) {
				
				sSql = "		SELECT DISTINCT D.ID_ETAPA_PRESTAMO \n" +
						"		FROM SIM_PRESTAMO_GPO_DET D, \n" +
						"		SIM_PRESTAMO_ETAPA E \n" +
						"		WHERE D.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"		AND D.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
						"		AND D.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +    
						"		AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
						"		AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" + 
						"		AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
						"		AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO \n" +
						"		AND E.ORDEN_ETAPA = ( \n" +
						"		SELECT MIN(ORDEN_ETAPA) FROM \n" +
						"		(SELECT D.ID_ETAPA_PRESTAMO, E.ORDEN_ETAPA \n" +
						"		FROM SIM_PRESTAMO_GPO_DET D, \n" + 
						"		SIM_PRESTAMO_ETAPA E \n" +
						"		WHERE D.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"		AND D.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
						"		AND D.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +    
						"		AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
						"		AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" + 
						"		AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
						"		AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO)) \n";
			
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();
				if(rs1.next()){
					registro.addDefCampo("ID_ETAPA_PRESTAMO",rs1.getString("ID_ETAPA_PRESTAMO")== null ? "": rs1.getString("ID_ETAPA_PRESTAMO"));
					
					
					sSql =  " UPDATE SIM_PRESTAMO_GRUPO SET \n"+
							"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
					
					PreparedStatement ps2 = this.conn.prepareStatement(sSql);
					ps2.execute();
					ResultSet rs2 = ps2.getResultSet();
				}
				
				sSql =  "SELECT \n"+
						"B_LINEA_FONDEO \n" +
						"FROM \n" +
						"SIM_CAT_ETAPA_PRESTAMO \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
				PreparedStatement ps3 = this.conn.prepareStatement(sSql);
				ps3.execute();
				ResultSet rs3 = ps3.getResultSet();
				if(rs3.next()){
					String sBLineaFondeo = rs3.getString("B_LINEA_FONDEO");
					
					if (sBLineaFondeo.equals("V")){
						
						sSql =  "SELECT \n"+
								"NUM_LINEA \n" +
								"FROM \n" +
								"SIM_PRESTAMO_GRUPO \n" +
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
						PreparedStatement ps4 = this.conn.prepareStatement(sSql);
						ps4.execute();
						ResultSet rs4 = ps4.getResultSet();
						if(rs4.next()){
							registro.addDefCampo("NUM_LINEA",rs4.getString("NUM_LINEA")== null ? "": rs4.getString("NUM_LINEA"));
						}
						
						sSql =  "SELECT \n"+
								"MONTO_DISPONIBLE \n" +
								"FROM \n" +
								"SIM_CAT_FONDEADOR_LINEA \n" +
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND NUM_LINEA = '" + (String)registro.getDefCampo("NUM_LINEA") + "' \n";
						PreparedStatement ps5 = this.conn.prepareStatement(sSql);
						ps5.execute();
						ResultSet rs5 = ps5.getResultSet();
						if(rs5.next()){
							registro.addDefCampo("MONTO_DISPONIBLE",rs5.getString("MONTO_DISPONIBLE")== null ? "": rs5.getString("MONTO_DISPONIBLE"));
						}
						
						sSql =  "SELECT \n"+
								"A.MONTO_AUTORIZADO + B.CARGO_INICIAL IMPORTE_PRESTADO \n"+
								"FROM SIM_PRESTAMO_GRUPO P, \n"+
								"SIM_GRUPO N, \n"+ 
								"(SELECT \n"+ 
								"CVE_GPO_EMPRESA, \n"+
								"CVE_EMPRESA, \n"+
								"ID_PRESTAMO_GRUPO, \n"+
								"SUM(MONTO_AUTORIZADO) MONTO_AUTORIZADO \n"+
								"FROM SIM_PRESTAMO_GPO_DET \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO_GRUPO ='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
								"GROUP BY ID_PRESTAMO_GRUPO, \n"+
								"CVE_GPO_EMPRESA, \n"+ 
								"CVE_EMPRESA) A, \n"+
								"(SELECT  \n"+
								"P.CVE_GPO_EMPRESA, \n"+
								"P.CVE_EMPRESA, \n"+
								"P.ID_PRESTAMO_GRUPO, \n"+
								"SUM(C.CARGO_INICIAL) CARGO_INICIAL \n"+
								"FROM SIM_PRESTAMO_GPO_DET P, \n"+ 
								"SIM_PRESTAMO_GPO_CARGO C \n"+
								"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND P.ID_PRESTAMO_GRUPO ='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
								"AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
								"AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+
								"AND C.ID_PRESTAMO_GRUPO = P.ID_PRESTAMO_GRUPO \n"+
								"AND C.ID_FORMA_APLICACION = 1 \n"+
								"GROUP BY  \n"+
								"P.CVE_GPO_EMPRESA, \n"+
								"P.CVE_EMPRESA, \n"+
								"P.ID_PRESTAMO_GRUPO)B \n"+
								"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND P.ID_PRESTAMO_GRUPO ='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
								"AND N.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
								"AND N.CVE_EMPRESA = P.CVE_EMPRESA \n"+
								"AND N.ID_GRUPO = P.ID_GRUPO \n"+
								"AND A.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
								"AND A.CVE_EMPRESA = P.CVE_EMPRESA \n"+
								"AND A.ID_PRESTAMO_GRUPO = P.ID_PRESTAMO_GRUPO \n"+
								"AND B.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
								"AND B.CVE_EMPRESA = P.CVE_EMPRESA \n"+
								"AND B.ID_PRESTAMO_GRUPO = P.ID_PRESTAMO_GRUPO \n";
						PreparedStatement ps6 = this.conn.prepareStatement(sSql);
						ps6.execute();
						ResultSet rs6 = ps6.getResultSet();
						if(rs6.next()){
							registro.addDefCampo("IMPORTE_PRESTADO",rs6.getString("IMPORTE_PRESTADO")== null ? "": rs6.getString("IMPORTE_PRESTADO"));
						}
						
						sSql =  " UPDATE SIM_CAT_FONDEADOR_LINEA SET "+
								" MONTO_DISPONIBLE 		='" + (String)registro.getDefCampo("MONTO_DISPONIBLE") + "' + '" + (String)registro.getDefCampo("IMPORTE_PRESTADO") + "' \n"+
								" WHERE NUM_LINEA		='" + (String)registro.getDefCampo("NUM_LINEA") + "' \n"+
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						PreparedStatement ps7 = this.conn.prepareStatement(sSql);
						ps7.execute();
						ResultSet rs7 = ps7.getResultSet();
						
						sSql =  " UPDATE SIM_PRESTAMO_GRUPO SET \n"+
								"NUM_LINEA = '' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
						
						PreparedStatement ps8 = this.conn.prepareStatement(sSql);
						ps8.execute();
						ResultSet rs8 = ps8.getResultSet();
						
						sSql =  "SELECT \n"+
								"ID_PRESTAMO \n" +
								"FROM \n" +
								"SIM_PRESTAMO_GPO_DET \n" +
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
						PreparedStatement ps9 = this.conn.prepareStatement(sSql);
						ps9.execute();
						ResultSet rs9 = ps9.getResultSet();
						while(rs9.next()){
							registro.addDefCampo("ID_PRESTAMO",rs9.getString("ID_PRESTAMO")== null ? "": rs9.getString("ID_PRESTAMO"));
							
						
							sSql =  " UPDATE SIM_PRESTAMO SET \n"+
									"NUM_LINEA = '' \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
							PreparedStatement ps10 = this.conn.prepareStatement(sSql);
							ps10.execute();
							ResultSet rs10 = ps10.getResultSet();
						}
					}	
				}	
				
				sSql =  "SELECT \n"+
						"B_AUTORIZAR_COMITE \n" +
						"FROM \n" +
						"SIM_CAT_ETAPA_PRESTAMO \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
				PreparedStatement ps11 = this.conn.prepareStatement(sSql);
				ps11.execute();
				ResultSet rs11 = ps11.getResultSet();
				if(rs11.next()){
						String sBAutorizarComite = rs11.getString("B_AUTORIZAR_COMITE");
					
					if (sBAutorizarComite.equals("V")){
						sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET \n"+
								"MONTO_AUTORIZADO = '' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
						
						PreparedStatement ps12 = this.conn.prepareStatement(sSql);
						ps12.execute();
						ResultSet rs12 = ps12.getResultSet();
						
						sSql =  " UPDATE SIM_PRESTAMO_GRUPO SET \n"+
								"FECHA_ENTREGA = '', \n"+
								"FECHA_REAL = '', \n"+
								"DIA_SEMANA_PAGO = '' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
						
						PreparedStatement ps13 = this.conn.prepareStatement(sSql);
						ps13.execute();
						ResultSet rs13 = ps13.getResultSet();
						
						sSql =  "SELECT \n"+
								"ID_PRESTAMO \n" +
								"FROM \n" +
								"SIM_PRESTAMO_GPO_DET \n" +
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
						PreparedStatement ps14 = this.conn.prepareStatement(sSql);
						ps14.execute();
						ResultSet rs14 = ps14.getResultSet();
						while(rs14.next()){
							registro.addDefCampo("ID_PRESTAMO",rs14.getString("ID_PRESTAMO")== null ? "": rs14.getString("ID_PRESTAMO"));
							
						
							sSql =  " UPDATE SIM_CLIENTE_MONTO SET \n"+
									"MONTO_AUTORIZADO = '' \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
							PreparedStatement ps15 = this.conn.prepareStatement(sSql);
							ps15.execute();
							ResultSet rs15 = ps15.getResultSet();
							
							sSql =  " UPDATE SIM_PRESTAMO SET \n"+
									"FECHA_ENTREGA = '', \n"+
									"FECHA_REAL = '', \n"+
									"DIA_SEMANA_PAGO = '' \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
							
							PreparedStatement ps16 = this.conn.prepareStatement(sSql);
							ps16.execute();
							ResultSet rs16 = ps16.getResultSet();
						}
						
					}
				}
			}else if (registro.getDefCampo("MOVIMIENTO").equals("AVANZAR_ETAPA_GRUPAL")) {
				sSql =  " SELECT \n"+
						"CVE_FUNCION, \n"+
						"CVE_APLICACION, \n"+
						"CVE_PERFIL \n"+
						"FROM RS_CONF_APLICACION_CONFIG \n"+
						"WHERE CVE_FUNCION = (SELECT \n"+
						"CVE_FUNCION \n"+
						"FROM \n"+
						"SIM_CAT_ETAPA_PRESTAMO \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
						") \n"+
						"AND CVE_APLICACION = 'Prestamo' \n"+
						"AND CVE_PERFIL = ( \n"+
						"SELECT \n"+
						"CVE_PERFIL \n"+ 
						"FROM RS_GRAL_USUARIO_PERFIL \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_USUARIO = '" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
						"AND CVE_APLICACION = 'Prestamo') \n";
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();
				if(rs1.next()){
				
					//Avanza la etapa.
					//Obtiene las actividades de la etapa que se completa.
					sSql =  "SELECT \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_ETAPA_PRESTAMO, \n"+
							"ID_ACTIVIDAD_REQUISITO, \n" +
							"FECHA_REGISTRO, \n" +
							"ORDEN_ETAPA \n" +
							"FROM \n" +
							"SIM_PRESTAMO_ETAPA \n" +
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
							"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
					
					PreparedStatement ps2 = this.conn.prepareStatement(sSql);
					ps2.execute();
					ResultSet rs2 = ps2.getResultSet();
					
					while (rs2.next()){
						registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs2.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs2.getString("ID_ACTIVIDAD_REQUISITO"));
						registro.addDefCampo("ORDEN_ETAPA",rs2.getString("ORDEN_ETAPA")== null ? "": rs2.getString("ORDEN_ETAPA"));
						registro.addDefCampo("FECHA_REGISTRO",rs2.getString("FECHA_REGISTRO")== null ? "": rs2.getString("FECHA_REGISTRO"));
						//Guarda el historial de la etapa y las actividades
						sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
						PreparedStatement ps3 = this.conn.prepareStatement(sSql);
						ps3.execute();
						ResultSet rs3 = ps3.getResultSet();
						if (rs3.next()){
							registro.addDefCampo("ID_HISTORICO",rs3.getString("ID_HISTORICO"));
							
							sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_HISTORICO, \n" +
								"ID_PRESTAMO, \n" +
								"ID_ACTIVIDAD_REQUISITO, \n" +
								"ID_ETAPA_PRESTAMO, \n"+
								"FECHA_REALIZADA, \n"+
								"FECHA_REGISTRO, \n"+
								"ESTATUS, \n"+
								"CVE_USUARIO, \n"+
								"ORDEN_ETAPA) \n"+
								" VALUES (" +
								"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
								"SYSDATE, \n"+
								"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
								"'Completada', \n"+
								"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
								"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "') \n" ;
						
							if (ejecutaUpdate() == 0){
								resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
							}
						}
					}
				
					//Obtiene la etapa posterior.
					sSql =  " SELECT DISTINCT ID_ETAPA_PRESTAMO, ORDEN_ETAPA \n"+
						"	  FROM SIM_PRESTAMO_ETAPA \n"+
						"	  WHERE ORDEN_ETAPA = (SELECT DISTINCT \n"+
						"	  ORDEN_ETAPA + 1 \n"+
						" 	  FROM SIM_PRESTAMO_ETAPA \n"+
						"	  WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"	  AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
						"	  AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n"+
						" 	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
						
						ejecutaSql();
						if(rs.next()){
							//Avanza a la etapa posterior.
							registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO"));
							registro.addDefCampo("ORDEN_ETAPA",rs.getString("ORDEN_ETAPA"));
							
							sSql =  " UPDATE SIM_PRESTAMO SET \n"+
								"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
								
							if (ejecutaUpdate() == 0){
								resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
							}
							
							sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET \n"+
									"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
									"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
									
							if (ejecutaUpdate() == 0){
								resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
							}
							
							sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
									" FECHA_REGISTRO 		=SYSDATE, \n" +
									" ESTATUS 	 		='Registrada', \n" +
									" COMENTARIO 	 		='' \n" +
									" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
									" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
							
							if (ejecutaUpdate() == 0){
								resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
							}
							
							sSql =  "SELECT \n"+
									"CVE_GPO_EMPRESA, \n"+
									"CVE_EMPRESA, \n"+
									"ID_PRESTAMO, \n"+
									"ID_ACTIVIDAD_REQUISITO, \n"+
									"ID_ETAPA_PRESTAMO, \n"+
									"FECHA_REGISTRO, \n"+
									"FECHA_REALIZADA, \n"+
									"COMENTARIO, \n"+
									"ORDEN_ETAPA, \n"+
									"ESTATUS \n" +
									"FROM \n"+
									"SIM_PRESTAMO_ETAPA \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
									"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
							
								PreparedStatement ps10 = this.conn.prepareStatement(sSql);
								ps10.execute();
								ResultSet rs10 = ps10.getResultSet();
								
								while (rs10.next()){
									registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs10.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs10.getString("ID_ACTIVIDAD_REQUISITO"));
									registro.addDefCampo("ID_ETAPA_PRESTAMO",rs10.getString("ID_ETAPA_PRESTAMO")== null ? "": rs10.getString("ID_ETAPA_PRESTAMO"));
									registro.addDefCampo("ORDEN_ETAPA",rs10.getString("ORDEN_ETAPA")== null ? "": rs10.getString("ORDEN_ETAPA"));
									registro.addDefCampo("FECHA_REGISTRO",rs10.getString("FECHA_REGISTRO")== null ? "": rs10.getString("FECHA_REGISTRO"));
									registro.addDefCampo("COMENTARIO",rs10.getString("COMENTARIO")== null ? "": rs10.getString("COMENTARIO"));
									registro.addDefCampo("ESTATUS",rs10.getString("ESTATUS")== null ? "": rs10.getString("ESTATUS"));
								
									sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
									
									PreparedStatement ps11 = this.conn.prepareStatement(sSql);
									ps11.execute();
									ResultSet rs11 = ps11.getResultSet();	
									if (rs11.next()){
										registro.addDefCampo("ID_HISTORICO",rs11.getString("ID_HISTORICO"));
										//Actualiza la bitácora.
										sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
											"CVE_GPO_EMPRESA, \n" +
											"CVE_EMPRESA, \n" +
											"ID_HISTORICO, \n" +
											"ID_PRESTAMO, \n" +
											"ID_ACTIVIDAD_REQUISITO, \n" +
											"ID_ETAPA_PRESTAMO, \n"+
											"FECHA_REGISTRO, \n"+
											"ORDEN_ETAPA, \n"+
											"COMENTARIO, \n"+
											"ESTATUS) \n" +
											" VALUES (" +
											"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
											"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
											"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
											"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
											"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
											"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
											"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
											"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
											"'" + (String)registro.getDefCampo("COMENTARIO") + "', \n" +
											"'" + (String)registro.getDefCampo("ESTATUS") + "') \n" ;
										
										PreparedStatement ps12 = this.conn.prepareStatement(sSql);
										ps12.execute();
										ResultSet rs12 = ps12.getResultSet();
									}
								}
							
							
							
							sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET "+
									" COMENTARIO 		='' \n"+
									" WHERE ID_PRESTAMO_GRUPO		='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
									" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
									" AND ID_PRESTAMO	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
							if (ejecutaUpdate() == 0){
								resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
							}
						}
					
				}
			}else if (registro.getDefCampo("MOVIMIENTO").equals("ACTUALIZA_A_ETAPA_POSTERIOR")) {
				
				sSql = "		SELECT DISTINCT D.ID_ETAPA_PRESTAMO \n" +
						"		FROM SIM_PRESTAMO_GPO_DET D, \n" +
						"		SIM_PRESTAMO_ETAPA E \n" +
						"		WHERE D.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"		AND D.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
						"		AND D.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +    
						"		AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
						"		AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" + 
						"		AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
						"		AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO \n" +
						"		AND E.ORDEN_ETAPA = ( \n" +
						"		SELECT MIN(ORDEN_ETAPA) FROM \n" +
						"		(SELECT D.ID_ETAPA_PRESTAMO, E.ORDEN_ETAPA \n" +
						"		FROM SIM_PRESTAMO_GPO_DET D, \n" + 
						"		SIM_PRESTAMO_ETAPA E \n" +
						"		WHERE D.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"		AND D.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
						"		AND D.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +    
						"		AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
						"		AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" + 
						"		AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
						"		AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO)) \n";
			
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();
				if(rs1.next()){
					registro.addDefCampo("ID_ETAPA_PRESTAMO",rs1.getString("ID_ETAPA_PRESTAMO")== null ? "": rs1.getString("ID_ETAPA_PRESTAMO"));
					
					
					sSql =  " UPDATE SIM_PRESTAMO_GRUPO SET \n"+
							"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
					
					PreparedStatement ps2 = this.conn.prepareStatement(sSql);
					ps2.execute();
					ResultSet rs2 = ps2.getResultSet();
				}
				
				
			}else if (registro.getDefCampo("MOVIMIENTO").equals("RECONFORMAR_PRESTAMO")) {
				
				String sNumIntegrantes = "";
				String sMinIntegrantes = "";
				String sMaxIntegrantes = "";
				String sMaximoRiego = "";
				String sMaximoAmbulante = "";
				String sMaximoCatalogo = "";
				String sIdIntegrantes = "";
				int iMaxRiesgoProp = 0;
				int iMaxAmbulanteProp = 0;
				int iMaxCatalogoProp = 0;
				String sCreditosSimultaneos = "";
				String sDeudaMinima = "";
				int iDeudaMinima = 0;
				int iDeuda = 0;
				String sSaldo = "";
				float fSaldo = 0;
				String sMinIntFundadores = "";
				float fMinIntFundadores = 0;
				float fIntegrante = 0;
				String sNumCiclo = "";
				
				//Preguntamos por el parámetro % mínimo de fundadores del grupo.
				sSql = "SELECT A.INT_FUNDADORES / 100 * B.PORC_INT_FUNDADORES MIN_INT_FUNDADORES \n" +
						"FROM \n" +
						"(SELECT COUNT(*) INT_FUNDADORES \n" +
						"FROM SIM_GRUPO_FUNDADOR \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
						"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
						"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "') A, \n" +
						"(SELECT  \n" +
						"PORC_INT_FUNDADORES  \n" +
						"FROM SIM_PARAMETRO_GLOBAL  \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					    "AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "')B \n" ;
					   	
				ejecutaSql();
				
				if (rs.next()){
					sMinIntFundadores = rs.getString("MIN_INT_FUNDADORES");
					fMinIntFundadores = (Float.parseFloat(sMinIntFundadores));
					
				}
				
				//Obtenemos los integrantes que actualmente integran el grupo
				sSql = " SELECT \n" + 
					  " ID_INTEGRANTE \n" +
					  "	FROM SIM_GRUPO_INTEGRANTE  \n" +
					  "	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					  "	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
					  "	AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
					  " AND FECHA_BAJA_LOGICA IS NULL \n";
				ejecutaSql();
				System.out.println("Obtenemos los integrantes que actualmente integran el grupo"+sSql);
				while (rs.next()){
					registro.addDefCampo("ID_INTEGRANTE",rs.getString("ID_INTEGRANTE")== null ? "": rs.getString("ID_INTEGRANTE"));
					
					sSql = "SELECT \n"+
							"ID_INTEGRANTE \n"+
							"FROM SIM_GRUPO_FUNDADOR \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
							"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
							"AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n" +
							"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					ps1.execute();
					ResultSet rs1 = ps1.getResultSet();
					
					if (rs1.next()){
						fIntegrante++;
						System.out.println("Cuenta cuantos integrantes fundadores hay"+sSql);
					}
				}
				
				if (fIntegrante < fMinIntFundadores){
					resultadoCatalogo.mensaje.setClave("NO_MINIMO_FUNDADORES");
				}
				else {
				
					//Preguntamos por el parámetro Crédito Simultáneos y deuda mínima.
					
					sSql = " SELECT \n" + 
						  " CREDITOS_SIMULTANEOS, \n" +
						  " DEUDA_MINIMA \n" +
						  "	FROM SIM_PARAMETRO_GLOBAL  \n" +
						  "	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						  "	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" ;
					System.out.println("Preguntamos por el parámetro Crédito Simultáneos y deuda mínima"+sSql);
					ejecutaSql();
					if (rs.next()){
						sCreditosSimultaneos = rs.getString("CREDITOS_SIMULTANEOS");
						sDeudaMinima = rs.getString("DEUDA_MINIMA");
						iDeudaMinima = (Integer.parseInt(sDeudaMinima));
					}
					
					if (sCreditosSimultaneos.equals("V")){
					
						//Cuenta los integrantes del grupo.
						sSql =  "SELECT COUNT(*) NUM_INTEGRANTES \n" +
							"FROM SIM_GRUPO_INTEGRANTE  \n"+
							"WHERE \n"+
							"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
							"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
							"AND FECHA_BAJA_LOGICA IS NULL \n" ;
						ejecutaSql();
						if (rs.next()){
							sNumIntegrantes = rs.getString("NUM_INTEGRANTES");
						}
						
						//Obtiene de los parámetros globales del grupo el mínimo de integrantes que debe tener un grupo.
						sSql =  "SELECT \n"+
							"	MIN(NUM_INTEGRANTE) MINIMO_INTEGRANTES \n"+
							"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
						ejecutaSql();
						
						if (rs.next()){
							sMinIntegrantes = rs.getString("MINIMO_INTEGRANTES");
						}
						
						//Obtiene de los parámetros globales del grupo el máximo de integrantes que debe tener un grupo.
						sSql =  "SELECT \n"+
							"	MAX(NUM_INTEGRANTE) MAXIMO_INTEGRANTES \n"+
							"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
						ejecutaSql();
						if (rs.next()){
							sMaxIntegrantes = rs.getString("MAXIMO_INTEGRANTES");
						}
						
						int iNumIntegrantes =Integer.parseInt(sNumIntegrantes.trim());
						int iMinIntegrantes =Integer.parseInt(sMinIntegrantes.trim());
						int iMaxIntegrantes =Integer.parseInt(sMaxIntegrantes.trim());
						
						//Compara si el número de candidatos al grupo se encuentra dentro de los parámetros globales del grupo.
						if (iNumIntegrantes >= iMinIntegrantes){
							
							if (iNumIntegrantes <= iMaxIntegrantes){
								
								//Obtiene de los parámetros globales del grupo el máximo de integrantes que pueden estar en riesgo.
								sSql =  "SELECT \n"+
									"	MAXIMO_RIESGO \n"+
									"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
									"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
								ejecutaSql();
								
								if (rs.next()){
									sMaximoRiego = rs.getString("MAXIMO_RIESGO");
								}
								
								int iMaximoRiego =Integer.parseInt(sMaximoRiego.trim());
								
								//Obtiene de los parámetros globales del grupo el máximo de integrantes que pueden tener negocios ambulantes.
								sSql =  "SELECT \n"+
									"	MAX_AMBULANTE \n"+
									"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
									"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
								ejecutaSql();
								
								if (rs.next()){
									sMaximoAmbulante = rs.getString("MAX_AMBULANTE");
								}
								
								int iMaximoAmbulante =Integer.parseInt(sMaximoAmbulante.trim());
								
								//Obtiene de los parámetros globales del grupo el máximo de integrantes que pueden tener negocios de venta por catálogo.
								sSql =  "SELECT \n"+
									"	MAX_CATALOGO \n"+
									"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
									"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
								ejecutaSql();
								
								if (rs.next()){
									sMaximoCatalogo = rs.getString("MAX_CATALOGO");
								}
								
								int iMaximoCatalogo =Integer.parseInt(sMaximoCatalogo.trim());
								
								//Obtiene los integrantes del grupo.
								sSql =  "SELECT ID_INTEGRANTE \n" +
									"FROM SIM_GRUPO_INTEGRANTE  \n"+
									"WHERE \n"+
									"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
									"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
									"AND FECHA_BAJA_LOGICA IS NULL \n" ;
								ejecutaSql();			
								
								while (rs.next()){
									
									sIdIntegrantes = rs.getString("ID_INTEGRANTE");
									//Busca si el tipo de negocio del candidato es ambulante o de venta por catálogo.
									sSql =  "SELECT NOM_NEGOCIO \n" +
										"FROM SIM_CLIENTE_NEGOCIO  \n"+
										"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
										"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
										"AND B_PRINCIPAL = 'V' \n" +
										"AND (ID_TIPO_NEGOCIO = '3' OR ID_TIPO_NEGOCIO = '4') \n" +
										"AND ID_PERSONA = '" + sIdIntegrantes + "' \n";
									
									PreparedStatement ps1 = this.conn.prepareStatement(sSql);
									ps1.execute();
									ResultSet rs1 = ps1.getResultSet();
						
									if (rs1.next()){
										iMaxRiesgoProp ++;
									}
									rs1.close();
									ps1.close();	
								}
								
								//Comprueba si el número de candidatos en riesgo se encuentra dentro de los parámetros globales del grupo.
								if (iMaxRiesgoProp <= iMaximoRiego){
									
									//Obtiene los candidatos propuestos a formar el grupo.
									sSql =  "SELECT ID_INTEGRANTE \n" +
										"FROM SIM_GRUPO_INTEGRANTE  \n"+
										"WHERE \n"+
										"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
										"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
										"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
										"AND FECHA_BAJA_LOGICA IS NULL \n" ;
									ejecutaSql();			
									
									System.out.println("4"+sSql);
									while (rs.next()){
										
										sIdIntegrantes = rs.getString("ID_INTEGRANTE");
										//Busca si el tipo de negocio del candidato es ambulante.
										sSql =  "SELECT NOM_NEGOCIO \n" +
											"FROM SIM_CLIENTE_NEGOCIO  \n"+
											"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
											"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
											"AND B_PRINCIPAL = 'V' \n" +
											"AND ID_TIPO_NEGOCIO = '3' \n" +
											"AND ID_PERSONA = '" + sIdIntegrantes + "' \n";
										
										PreparedStatement ps1 = this.conn.prepareStatement(sSql);
										ps1.execute();
										ResultSet rs1 = ps1.getResultSet();
							
										if (rs1.next()){
											iMaxAmbulanteProp ++;
										}
										rs1.close();
										ps1.close();
									}	
										
									//Comprueba si el número de integrantes con negocios tipo ambulantes es el permitido.
									if (iMaxAmbulanteProp <= iMaximoAmbulante){
										
										//Obtiene los candidatos propuestos a formar el grupo.
										sSql =  "SELECT ID_INTEGRANTE \n" +
											"FROM SIM_GRUPO_INTEGRANTE  \n"+
											"WHERE \n"+
											"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
											"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
											"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
											"AND FECHA_BAJA_LOGICA IS NULL \n" ;
										ejecutaSql();			
										System.out.println("6"+sSql);
										
										while (rs.next()){
											
											sIdIntegrantes = rs.getString("ID_INTEGRANTE");
											//Busca si el tipo de negocio del candidato es venta por catálogo.
											sSql =  "SELECT NOM_NEGOCIO \n" +
												"FROM SIM_CLIENTE_NEGOCIO  \n"+
												"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
												"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
												"AND B_PRINCIPAL = 'V' \n" +
												"AND ID_TIPO_NEGOCIO = '4' \n" +
												"AND ID_PERSONA = '" + sIdIntegrantes + "' \n";
											
											PreparedStatement ps1 = this.conn.prepareStatement(sSql);
											ps1.execute();
											ResultSet rs1 = ps1.getResultSet();
								
											if (rs1.next()){
												iMaxCatalogoProp ++;
											}
											rs1.close();
											ps1.close();
										}	
										
										//Comprueba si el número de integrantes con negocios tipo venta por catálogo es el permitido.
										if (iMaxCatalogoProp <= iMaximoCatalogo){
											
											//El grupo cumple con los parametro globales por lo tanto se reconformará el grupo.
											sSql =  "   SELECT \n" + 
													"   ID_INTEGRANTE \n" +
													"	FROM SIM_GRUPO_INTEGRANTE  \n" +
													"	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
													"	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
													"	AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
													"   AND FECHA_BAJA_LOGICA IS NULL \n";
											PreparedStatement ps15 = this.conn.prepareStatement(sSql);
											ps15.execute();
											ResultSet rs15 = ps15.getResultSet();
											
									while (rs15.next()){
										registro.addDefCampo("ID_INTEGRANTE",rs15.getString("ID_INTEGRANTE")== null ? "": rs15.getString("ID_INTEGRANTE"));
										
										//Verifica si es un integrante nuevo.
										sSql =  " SELECT ID_INTEGRANTE \n"+
												" FROM SIM_PRESTAMO_GPO_DET \n"+
												" WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
												" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
												" AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
												" AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n" ;
										PreparedStatement ps16 = this.conn.prepareStatement(sSql);
										ps16.execute();
										ResultSet rs16 = ps16.getResultSet();
										
										if(!rs16.next()){
											//Da de alta el crédito
											sSql =  "INSERT INTO SIM_PRESTAMO_GPO_DET ( "+
													"CVE_GPO_EMPRESA, \n" +
													"CVE_EMPRESA, \n" +
													"ID_PRESTAMO_GRUPO, \n" +
													"ID_INTEGRANTE) \n" +
													" VALUES (" +
													"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
													"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
													"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "', \n" +
													"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "') \n" ;
											
											PreparedStatement ps17 = this.conn.prepareStatement(sSql);
											ps17.execute();
											ResultSet rs17 = ps17.getResultSet();	
											
											sSql = "SELECT \n"+
													"PG.CVE_GPO_EMPRESA, \n"+
													"PG.CVE_EMPRESA, \n"+
													"PG.ID_PRODUCTO, \n"+
													"PG.ID_GRUPO, \n"+
													"PG.NUM_CICLO, \n"+
													"PG.ID_PERIODICIDAD_PRODUCTO, \n"+
													"PG.CVE_METODO, \n"+
													"P.APLICA_A, \n"+
													"G.ID_SUCURSAL,\n"+
													"PG.PLAZO, \n"+
													"PG.TIPO_TASA, \n"+
													"PG.VALOR_TASA, \n"+
													"PG.ID_PERIODICIDAD_TASA, \n"+
													"P.MONTO_MINIMO, \n"+
													"PG.MONTO_MAXIMO, \n"+
													"PG.ID_TIPO_RECARGO, \n"+
													"PG.TIPO_TASA_RECARGO, \n"+
													"PG.TASA_RECARGO, \n"+
													"PG.ID_PERIODICIDAD_TASA_RECARGO, \n"+
													"PG.ID_TASA_REFERENCIA_RECARGO, \n"+
													"PG.FACTOR_TASA_RECARGO, \n"+
													"PG.MONTO_FIJO_PERIODO, \n"+
													"PC.ID_FORMA_DISTRIBUCION, \n"+
													"PC.PORC_FLUJO_CAJA \n"+
													"FROM SIM_PRESTAMO_GRUPO PG, \n"+ 
													"SIM_PRODUCTO P, \n"+
													"SIM_PRODUCTO_CICLO PC, \n"+
													"SIM_GRUPO G \n"+
													"WHERE PG.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
													"AND PG.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
													"AND PG.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
													"AND P.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
													"AND P.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
													"AND P.ID_PRODUCTO = PG.ID_PRODUCTO \n"+
													"AND PC.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
													"AND PC.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
													"AND PC.ID_PRODUCTO = PG.ID_PRODUCTO \n"+
													"AND PC.NUM_CICLO = PG.NUM_CICLO \n"+
													"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
													"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
													"AND G.ID_GRUPO = PG.ID_GRUPO \n";
											PreparedStatement ps18 = this.conn.prepareStatement(sSql);
											ps18.execute();
											ResultSet rs18 = ps18.getResultSet();	
											
											if(rs18.next()){
												registro.addDefCampo("ID_PERIODICIDAD_PRODUCTO",rs18.getString("ID_PERIODICIDAD_PRODUCTO")== null ? "": rs18.getString("ID_PERIODICIDAD_PRODUCTO"));
												registro.addDefCampo("ID_GRUPO",rs18.getString("ID_GRUPO")== null ? "": rs18.getString("ID_GRUPO"));
												registro.addDefCampo("ID_PRODUCTO",rs18.getString("ID_PRODUCTO")== null ? "": rs18.getString("ID_PRODUCTO"));
												registro.addDefCampo("ID_SUCURSAL",rs18.getString("ID_SUCURSAL")== null ? "": rs18.getString("ID_SUCURSAL"));
												registro.addDefCampo("NUM_CICLO",rs18.getString("NUM_CICLO")== null ? "": rs18.getString("NUM_CICLO"));
												registro.addDefCampo("CVE_METODO",rs18.getString("CVE_METODO")== null ? "": rs18.getString("CVE_METODO"));
												registro.addDefCampo("APLICA_A",rs18.getString("APLICA_A")== null ? "": rs18.getString("APLICA_A"));
												registro.addDefCampo("PLAZO",rs18.getString("PLAZO")== null ? "": rs18.getString("PLAZO"));
												registro.addDefCampo("TIPO_TASA",rs18.getString("TIPO_TASA")== null ? "": rs18.getString("TIPO_TASA"));
												registro.addDefCampo("VALOR_TASA",rs18.getString("VALOR_TASA")== null ? "": rs18.getString("VALOR_TASA"));
												registro.addDefCampo("ID_PERIODICIDAD_TASA",rs18.getString("ID_PERIODICIDAD_TASA")== null ? "": rs18.getString("ID_PERIODICIDAD_TASA"));
												registro.addDefCampo("MONTO_MINIMO",rs18.getString("MONTO_MINIMO")== null ? "": rs18.getString("MONTO_MINIMO"));
												registro.addDefCampo("MONTO_MAXIMO",rs18.getString("MONTO_MAXIMO")== null ? "": rs18.getString("MONTO_MAXIMO"));
												registro.addDefCampo("ID_TIPO_RECARGO",rs18.getString("ID_TIPO_RECARGO")== null ? "": rs18.getString("ID_TIPO_RECARGO"));
												registro.addDefCampo("TIPO_TASA_RECARGO",rs18.getString("TIPO_TASA_RECARGO")== null ? "": rs18.getString("TIPO_TASA_RECARGO"));
												registro.addDefCampo("TASA_RECARGO",rs18.getString("TASA_RECARGO")== null ? "": rs18.getString("TASA_RECARGO"));
												registro.addDefCampo("ID_PERIODICIDAD_TASA_RECARGO",rs18.getString("ID_PERIODICIDAD_TASA_RECARGO")== null ? "": rs18.getString("ID_PERIODICIDAD_TASA_RECARGO"));
												registro.addDefCampo("ID_TASA_REFERENCIA_RECARGO",rs18.getString("ID_TASA_REFERENCIA_RECARGO")== null ? "": rs18.getString("ID_TASA_REFERENCIA_RECARGO"));
												registro.addDefCampo("FACTOR_TASA_RECARGO",rs18.getString("FACTOR_TASA_RECARGO")== null ? "": rs18.getString("FACTOR_TASA_RECARGO"));
												registro.addDefCampo("MONTO_FIJO_PERIODO",rs18.getString("MONTO_FIJO_PERIODO")== null ? "": rs18.getString("MONTO_FIJO_PERIODO"));
												registro.addDefCampo("ID_FORMA_DISTRIBUCION",rs18.getString("ID_FORMA_DISTRIBUCION")== null ? "": rs18.getString("ID_FORMA_DISTRIBUCION"));
												registro.addDefCampo("PORC_FLUJO_CAJA",rs18.getString("PORC_FLUJO_CAJA")== null ? "": rs18.getString("PORC_FLUJO_CAJA"));
												
												String sIdPrestamo = "";
												
												//SE OBTIENE EL SEQUENCE DEL PRESTAMO
												sSql = "SELECT SQ01_SIM_PRESTAMO.nextval as ID_PRESTAMO FROM DUAL";
												ejecutaSql();
												if (rs.next()){
													sIdPrestamo = rs.getString("ID_PRESTAMO");
												}
												
												String sCvePrestamo = "";
												
												//OBTIENE CLAVE DEL PRÉSTAMO.
												sSql =  "SELECT REPLACE (CVE_PRESTAMO,' ','') CVE_PRESTAMO FROM ( \n"+
														"SELECT TO_CHAR('" + (String)registro.getDefCampo("ID_PRODUCTO") + "','00')||TO_CHAR('" + (String)registro.getDefCampo("ID_GRUPO") + "','000000')||TO_CHAR('" + (String)registro.getDefCampo("ID_INTEGRANTE") + "','00000000')||TO_CHAR('" + (String)registro.getDefCampo("NUM_CICLO") + "','00') AS CVE_PRESTAMO FROM DUAL \n"+
														") \n";
												ejecutaSql();
												if (rs.next()){
													sCvePrestamo = rs.getString("CVE_PRESTAMO");
												}
												
												sSql =  "INSERT INTO SIM_PRESTAMO ( \n"+
														"CVE_GPO_EMPRESA, \n" +
														"CVE_EMPRESA, \n" +
														"ID_PRESTAMO, \n" +
														"CVE_PRESTAMO, \n" +
														"ID_PRODUCTO, \n" +
														"ID_CLIENTE, \n" +
														"ID_GRUPO, \n" +
														"NUM_CICLO, \n" +
														"CVE_METODO, \n" +
														"ID_SUCURSAL, \n" +
														"ID_PERIODICIDAD_PRODUCTO, \n" +
														"PLAZO, \n" +
														"TIPO_TASA, \n" +
														"VALOR_TASA, \n" +
														"ID_PERIODICIDAD_TASA, \n" +
														"MONTO_MINIMO, \n" +
														"MONTO_MAXIMO, \n" +
														"PORC_FLUJO_CAJA, \n" +
														"ID_FORMA_DISTRIBUCION, \n" +
														"FECHA_SOLICITUD, \n" +
														"B_ENTREGADO, \n" +
														"APLICA_A) \n" +
												        "VALUES ( \n"+
													"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
													"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
													sIdPrestamo + ", \n "+
													"'" + sCvePrestamo + "', \n" +
													"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
													"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "', \n" +
													"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
													"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
													"'" + (String)registro.getDefCampo("CVE_METODO") + "', \n" +
													"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
													"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_PRODUCTO") + "', \n" +
													"'" + (String)registro.getDefCampo("PLAZO") + "', \n" +
													"'" + (String)registro.getDefCampo("TIPO_TASA") + "', \n" +
													"'" + (String)registro.getDefCampo("VALOR_TASA") + "', \n" +
													"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA") + "', \n" +
													"'" + (String)registro.getDefCampo("MONTO_MINIMO") + "', \n" +
													"'" + (String)registro.getDefCampo("MONTO_MAXIMO") + "', \n" +
													"'" + (String)registro.getDefCampo("PORC_FLUJO_CAJA") + "', \n" +
													"'" + (String)registro.getDefCampo("ID_FORMA_DISTRIBUCION") + "', \n" +
													"SYSDATE, \n" +
													"'F', \n" +
													"'" + (String)registro.getDefCampo("APLICA_A") + "') \n" ;
												
												//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
												PreparedStatement ps19 = this.conn.prepareStatement(sSql);
												ps19.execute();
												ResultSet rs19 = ps19.getResultSet();	
										
												//INGRESA LA FECHA DE LA TASA DE REFERENCIA SI EL TIPO DE REFERENCIA ES SI INDEXADA
												String sIndexada = "Si indexada";
												
												sSql = "SELECT \n"+
													"CVE_GPO_EMPRESA, \n"+
													"CVE_EMPRESA, \n"+
													"TIPO_TASA \n"+
													"FROM SIM_PRESTAMO \n"+
													"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
													"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
													"AND ID_PRESTAMO = '" + sIdPrestamo + "' \n"+
													"AND TIPO_TASA = '"+sIndexada+"' \n";
												
												PreparedStatement ps20 = this.conn.prepareStatement(sSql);
												ps20.execute();
												ResultSet rs20 = ps20.getResultSet();	
												if(rs20.next()){	
													sSql = "UPDATE SIM_PRESTAMO SET "+
														"FECHA_TASA_REFERENCIA = (SELECT MAX(FECHA_PUBLICACION) FECHA_TASA_REFERENCIA \n"+
																	"FROM ( \n"+
																	"SELECT FECHA_PUBLICACION FROM SIM_CAT_TASA_REFER_DETALLE \n"+
																	"WHERE ID_TASA_REFERENCIA = '" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "' \n" +
																	"AND FECHA_PUBLICACION < TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','yyyy-MM-dd HH24:MI:SSXFF') \n" +
																	" ))\n"+
														"WHERE ID_PRESTAMO = '" + sIdPrestamo + "' \n" +
														"AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
														"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA")+"' \n" ;
													
													PreparedStatement ps21 = this.conn.prepareStatement(sSql);
													ps21.execute();
													ResultSet rs21 = ps21.getResultSet();	
													
												}
										
												sSql =  "SELECT \n"+
														"CVE_GPO_EMPRESA, \n"+
														"CVE_EMPRESA, \n"+
														"ID_CARGO_COMISION, \n"+
														"ID_FORMA_APLICACION, \n"+
														"CARGO_INICIAL, \n"+
														"PORCENTAJE_MONTO, \n"+
														"CANTIDAD_FIJA, \n"+
														"VALOR, \n"+
														"ID_UNIDAD, \n"+
														"ID_PERIODICIDAD \n"+
														"FROM \n"+
														"SIM_PRESTAMO_GPO_CARGO \n"+
														"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
														"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
												
												PreparedStatement ps22 = this.conn.prepareStatement(sSql);
												ps22.execute();
												ResultSet rs22 = ps22.getResultSet();	
												
												while (rs22.next()){
													registro.addDefCampo("ID_CARGO_COMISION",rs22.getString("ID_CARGO_COMISION")== null ? "": rs22.getString("ID_CARGO_COMISION"));
													registro.addDefCampo("ID_FORMA_APLICACION",rs22.getString("ID_FORMA_APLICACION")== null ? "": rs22.getString("ID_FORMA_APLICACION"));
													registro.addDefCampo("CARGO_INICIAL",rs22.getString("CARGO_INICIAL")== null ? "": rs22.getString("CARGO_INICIAL"));
													registro.addDefCampo("PORCENTAJE_MONTO",rs22.getString("PORCENTAJE_MONTO")== null ? "": rs22.getString("PORCENTAJE_MONTO"));
													registro.addDefCampo("CANTIDAD_FIJA",rs22.getString("CANTIDAD_FIJA")== null ? "": rs22.getString("CANTIDAD_FIJA"));
													registro.addDefCampo("VALOR",rs22.getString("VALOR")== null ? "": rs22.getString("VALOR"));
													registro.addDefCampo("ID_UNIDAD",rs22.getString("ID_UNIDAD")== null ? "": rs22.getString("ID_UNIDAD"));
													registro.addDefCampo("ID_PERIODICIDAD",rs22.getString("ID_PERIODICIDAD")== null ? "": rs22.getString("ID_PERIODICIDAD"));
													
														sSql = "INSERT INTO SIM_PRESTAMO_CARGO_COMISION ( \n"+
															"CVE_GPO_EMPRESA, \n" +
															"CVE_EMPRESA, \n" +
															"ID_PRESTAMO, \n" +
															"ID_CARGO_COMISION, \n" +
															"ID_FORMA_APLICACION, \n"+
															"CARGO_INICIAL, \n"+
															"PORCENTAJE_MONTO, \n"+
															"CANTIDAD_FIJA, \n"+
															"VALOR, \n"+
															"ID_UNIDAD, \n"+
															"ID_PERIODICIDAD) \n"+
															" VALUES (" +
															"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
															"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
															"'" + sIdPrestamo + "', \n" +
															"'" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "', \n" +
															"'" + (String)registro.getDefCampo("ID_FORMA_APLICACION") + "', \n" +
															"'" + (String)registro.getDefCampo("CARGO_INICIAL") + "', \n" +
															"'" + (String)registro.getDefCampo("PORCENTAJE_MONTO") + "', \n" +
															"'" + (String)registro.getDefCampo("CANTIDAD_FIJA") + "', \n" +
															"'" + (String)registro.getDefCampo("VALOR") + "', \n" +
															"'" + (String)registro.getDefCampo("ID_UNIDAD") + "', \n" +
															"'" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "') \n" ;
														
														PreparedStatement ps23 = this.conn.prepareStatement(sSql);
														ps23.execute();
														ResultSet rs23 = ps23.getResultSet();	
													}	
											
													
													sSql =  "SELECT \n"+
															"CVE_GPO_EMPRESA, \n"+
															"CVE_EMPRESA, \n"+
															"ID_PRODUCTO, \n"+
															"NUM_CICLO, \n"+
															"ID_ACCESORIO, \n"+
															"ORDEN \n"+
															"FROM \n"+
															"SIM_PRODUCTO_CICLO_ACCESORIO \n"+
															"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
															"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
															"AND NUM_CICLO = '" + (String)registro.getDefCampo("NUM_CICLO") + "' \n";
												
													PreparedStatement ps26 = this.conn.prepareStatement(sSql);
													ps26.execute();
													ResultSet rs26 = ps26.getResultSet();
													
													while (rs26.next()){
														registro.addDefCampo("ID_ACCESORIO",rs26.getString("ID_ACCESORIO")== null ? "": rs26.getString("ID_ACCESORIO"));
														registro.addDefCampo("ORDEN",rs26.getString("ORDEN")== null ? "": rs26.getString("ORDEN"));
																
														sSql = "INSERT INTO SIM_PRESTAMO_ACCESORIO ( \n"+
															"CVE_GPO_EMPRESA, \n" +
															"CVE_EMPRESA, \n" +
															"ID_PRESTAMO, \n" +
															"ID_ACCESORIO, \n" +
															"ORDEN) \n" +
															" VALUES (" +
															"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
															"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
															"'" + sIdPrestamo + "', \n" +
															"'" + (String)registro.getDefCampo("ID_ACCESORIO") + "', \n" +
															"'" + (String)registro.getDefCampo("ORDEN") + "') \n" ;
														
														PreparedStatement ps27 = this.conn.prepareStatement(sSql);
														ps27.execute();
														ResultSet rs27 = ps27.getResultSet();		
													}
												
													sSql =  "SELECT \n"+
															"CVE_GPO_EMPRESA, \n"+
															"CVE_EMPRESA, \n"+
															"ID_PRODUCTO, \n"+
															"ID_ACTIVIDAD_REQUISITO, \n"+
															"ID_ETAPA_PRESTAMO, \n"+
															"ORDEN_ETAPA \n"+
															"FROM \n"+
															"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
															"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
															"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
													
													PreparedStatement ps28 = this.conn.prepareStatement(sSql);
													ps28.execute();
													ResultSet rs28 = ps28.getResultSet();	
														
													while (rs28.next()){
														registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs28.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs28.getString("ID_ACTIVIDAD_REQUISITO"));
														registro.addDefCampo("ID_ETAPA_PRESTAMO",rs28.getString("ID_ETAPA_PRESTAMO")== null ? "": rs28.getString("ID_ETAPA_PRESTAMO"));
														registro.addDefCampo("ORDEN_ETAPA",rs28.getString("ORDEN_ETAPA")== null ? "": rs28.getString("ORDEN_ETAPA"));
														
														sSql = "INSERT INTO SIM_PRESTAMO_ETAPA ( \n"+
																"CVE_GPO_EMPRESA, \n" +
																"CVE_EMPRESA, \n" +
																"ID_PRESTAMO, \n" +
																"ID_ACTIVIDAD_REQUISITO, \n" +
																"ID_ETAPA_PRESTAMO, \n"+
																"ORDEN_ETAPA) \n"+
																" VALUES (" +
																"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
																"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
																"'" + sIdPrestamo + "', \n" +
																"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
																"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
																"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "') \n" ;
														
																PreparedStatement ps29 = this.conn.prepareStatement(sSql);
																ps29.execute();
																ResultSet rs29 = ps29.getResultSet();
																
													}
												
													//SE OBTIENE EL SEQUENCE DE LA CUENTA
													sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
													ejecutaSql();
													if (rs.next()){
														registro.addDefCampo("ID_CUENTA",rs.getString("ID_CUENTA"));
														
														sSql = "INSERT INTO PFIN_CUENTA ( \n"+
														"CVE_GPO_EMPRESA, \n" +
														"CVE_EMPRESA, \n" +
														"ID_CUENTA, \n" +
														"SIT_CUENTA, \n" +
														"CVE_TIP_CUENTA, \n" +
														"B_RETIENE_IMPUESTO, \n"+
														"ID_TITULAR) \n"+
														" VALUES (" +
														"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
														"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
														"'" + (String)registro.getDefCampo("ID_CUENTA") + "', \n" +
														"'AC', \n" +
														"'CREDITO', \n" +
														"'F', \n" +
														"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "') \n" ;
														
														if (ejecutaUpdate() == 0){
															resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
														}	
														
														sSql =  " UPDATE SIM_PRESTAMO SET "+
															" ID_CUENTA			='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
															" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
															" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
														
														//VERIFICA SI DIO DE ALTA EL REGISTRO
														if (ejecutaUpdate() == 0){
															resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
														}	
													}
													
													
													sSql =  "SELECT \n"+
															"CVE_GPO_EMPRESA, \n"+
															"CVE_EMPRESA, \n"+
															"ID_CUENTA \n"+
															"FROM \n"+
															"PFIN_CUENTA \n"+
															"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
															"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															"AND ID_TITULAR = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n"+
															"AND CVE_TIP_CUENTA = 'VISTA' \n";
													
													PreparedStatement ps30 = this.conn.prepareStatement(sSql);
													ps30.execute();
													ResultSet rs30 = ps30.getResultSet();
												if (!rs30.next()){
													sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
													
													ejecutaSql();
													if (rs.next()){
														registro.addDefCampo("ID_CUENTA",rs.getString("ID_CUENTA"));
														
														sSql = "INSERT INTO PFIN_CUENTA ( \n"+
															"CVE_GPO_EMPRESA, \n" +
															"CVE_EMPRESA, \n" +
															"ID_CUENTA, \n" +
															"SIT_CUENTA, \n" +
															"CVE_TIP_CUENTA, \n" +
															"B_RETIENE_IMPUESTO, \n"+
															"ID_TITULAR) \n"+
															" VALUES (" +
															"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
															"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
															"'" + (String)registro.getDefCampo("ID_CUENTA") + "', \n" +
															"'AC', \n" +
															"'VISTA', \n" +
															"'V', \n" +
															"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "') \n" ;
														
														PreparedStatement ps31 = this.conn.prepareStatement(sSql);
														ps31.execute();
														ResultSet rs31 = ps31.getResultSet();
														
														sSql =  " UPDATE SIM_PRESTAMO SET "+
															" ID_CUENTA_REFERENCIA 		='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
															" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
															" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
														System.out.println("27"+sSql);
														//VERIFICA SI DIO DE ALTA EL REGISTRO
														PreparedStatement ps32 = this.conn.prepareStatement(sSql);
														ps32.execute();
														ResultSet rs32 = ps32.getResultSet();
														
													}
												}else{
													registro.addDefCampo("ID_CUENTA",rs30.getString("ID_CUENTA")== null ? "": rs30.getString("ID_CUENTA"));
													
													sSql =  " UPDATE SIM_PRESTAMO SET "+
															" ID_CUENTA_REFERENCIA 		='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
															" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
															" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
													
														//VERIFICA SI DIO DE ALTA EL REGISTRO
														PreparedStatement ps33 = this.conn.prepareStatement(sSql);
														ps33.execute();
														ResultSet rs33 = ps33.getResultSet();
												
												}
											
												sSql =  "SELECT \n"+
														"CVE_GPO_EMPRESA, \n"+
														"CVE_EMPRESA, \n"+
														"ID_PRODUCTO, \n"+
														"ID_ARCHIVO_FLUJO, \n"+
														"NOM_ARCHIVO \n"+
														"FROM \n"+
														"SIM_PRODUCTO_FLUJO_EFECTIVO \n"+
														"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
														"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
												
												PreparedStatement ps34 = this.conn.prepareStatement(sSql);
												ps34.execute();
												ResultSet rs34 = ps34.getResultSet();
														
														
												while (rs34.next()){
													registro.addDefCampo("ID_ARCHIVO_FLUJO",rs34.getString("ID_ARCHIVO_FLUJO")== null ? "": rs34.getString("ID_ARCHIVO_FLUJO"));
													registro.addDefCampo("NOM_ARCHIVO",rs34.getString("NOM_ARCHIVO")== null ? "": rs34.getString("NOM_ARCHIVO"));
													
													sSql =  "INSERT INTO SIM_PRESTAMO_FLUJO_EFECTIVO ( \n"+
															"CVE_GPO_EMPRESA, \n" +
															"CVE_EMPRESA, \n" +
															"ID_PRESTAMO, \n" +
															"ID_ARCHIVO_FLUJO, \n" +
															"NOM_ARCHIVO) \n"+
															" VALUES (" +
															"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
															"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
															"'" + sIdPrestamo + "', \n" +
															"'" + (String)registro.getDefCampo("ID_ARCHIVO_FLUJO") + "', \n" +
															"'" + (String)registro.getDefCampo("NOM_ARCHIVO") + "') \n" ;
													
															PreparedStatement ps35 = this.conn.prepareStatement(sSql);
															ps35.execute();
															ResultSet rs35 = ps35.getResultSet();
												}
													
												sSql =  "SELECT \n"+
														"CVE_GPO_EMPRESA, \n"+
														"CVE_EMPRESA, \n"+
														"ID_PRODUCTO, \n"+
														"ID_DOCUMENTO \n"+
														"FROM \n"+
														"SIM_PRODUCTO_DOCUMENTACION \n"+
														"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
														"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
												
												PreparedStatement ps36 = this.conn.prepareStatement(sSql);
												ps36.execute();
												ResultSet rs36 = ps36.getResultSet();
												while (rs36.next()){
													registro.addDefCampo("ID_DOCUMENTO",rs36.getString("ID_DOCUMENTO")== null ? "": rs36.getString("ID_DOCUMENTO"));
																
														sSql =  "INSERT INTO SIM_PRESTAMO_DOCUMENTACION ( \n"+
																"CVE_GPO_EMPRESA, \n" +
																"CVE_EMPRESA, \n" +
																"ID_PRESTAMO, \n" +
																"ID_DOCUMENTO) \n" +
																" VALUES (" +
																"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
																"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
																"'" + sIdPrestamo + "', \n" +
																"'" + (String)registro.getDefCampo("ID_DOCUMENTO") + "') \n" ;
													
														PreparedStatement ps37 = this.conn.prepareStatement(sSql);
														ps37.execute();
														ResultSet rs37 = ps37.getResultSet();
												}	
												//Regresa a la 
												sSql =  "SELECT DISTINCT \n"+
														"ID_ETAPA_PRESTAMO \n"+
														"FROM \n"+
														"SIM_PRESTAMO_ETAPA \n"+
														"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
														"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														"AND ID_PRESTAMO = '" + sIdPrestamo + "' \n"+
														"AND ORDEN_ETAPA = '1' \n";
												
												PreparedStatement ps38 = this.conn.prepareStatement(sSql);
												ps38.execute();
												ResultSet rs38 = ps38.getResultSet();
												if (rs38.next()){
													registro.addDefCampo("ID_ETAPA_PRESTAMO",rs38.getString("ID_ETAPA_PRESTAMO")== null ? "": rs38.getString("ID_ETAPA_PRESTAMO"));
												}
												
												sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET "+
														" ID_ETAPA_PRESTAMO 		='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
														" ID_PRESTAMO 		='" + sIdPrestamo + "' \n" +
														" WHERE ID_PRESTAMO_GRUPO		='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
														" AND ID_INTEGRANTE		='" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n"+
														" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
												
												//VERIFICA SI DIO DE ALTA EL REGISTRO
												PreparedStatement ps39 = this.conn.prepareStatement(sSql);
												ps39.execute();
												ResultSet rs39 = ps39.getResultSet();
												
												sSql =  " UPDATE SIM_PRESTAMO SET "+
														" ID_ETAPA_PRESTAMO 		='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
														" WHERE ID_PRESTAMO 		='" + sIdPrestamo + "' \n" +
														" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
											
												//VERIFICA SI DIO DE ALTA EL REGISTRO
												PreparedStatement ps40 = this.conn.prepareStatement(sSql);
												ps40.execute();
												ResultSet rs40 = ps40.getResultSet();	
												
												sSql =  " UPDATE SIM_PRESTAMO_GRUPO SET "+
														" ID_ETAPA_PRESTAMO 		='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
														" WHERE ID_PRESTAMO_GRUPO		='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
														" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
												
												//VERIFICA SI DIO DE ALTA EL REGISTRO
												PreparedStatement ps41 = this.conn.prepareStatement(sSql);
												ps41.execute();
												ResultSet rs41 = ps41.getResultSet();
										
										
												
												sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
												
												PreparedStatement ps42 = this.conn.prepareStatement(sSql);
												ps42.execute();
												ResultSet rs42 = ps42.getResultSet();
												if (rs42.next()){
													
													registro.addDefCampo("ID_HISTORICO",rs42.getString("ID_HISTORICO"));
													
													sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
														"CVE_GPO_EMPRESA, \n" +
														"CVE_EMPRESA, \n" +
														"ID_HISTORICO, \n" +
														"ID_PRESTAMO, \n" +
														//"ID_ACTIVIDAD_REQUISITO, \n" +
														"ID_ETAPA_PRESTAMO, \n"+
														"FECHA_REGISTRO)\n"+
														" VALUES (" +
														"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
														"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
														"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
														"'" + sIdPrestamo + "', \n" +
														//"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
														"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
														"SYSDATE) \n";
													
													PreparedStatement ps43 = this.conn.prepareStatement(sSql);
													ps43.execute();
													ResultSet rs43 = ps43.getResultSet();
												}
											}
										}
									}
									//Cancela el préstamos a integrantes que fueron dados de baja en el grupo.
									sSql =  " SELECT ID_INTEGRANTE \n"+
											" FROM SIM_PRESTAMO_GPO_DET \n"+
											" WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
											" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
											" AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
									
									PreparedStatement ps41 = this.conn.prepareStatement(sSql);
									ps41.execute();
									ResultSet rs41 = ps41.getResultSet();	
									while (rs41.next()){
										registro.addDefCampo("ID_INTEGRANTE",rs41.getString("ID_INTEGRANTE")== null ? "": rs41.getString("ID_INTEGRANTE"));
										
										sSql =  " SELECT ID_PRESTAMO \n"+
												" FROM SIM_PRESTAMO_GPO_DET \n"+
												" WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
												" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
												" AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
												" AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n" ;
										ejecutaSql();
										if (rs.next()){
											registro.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO"));
										}
										
										sSql =  "   SELECT \n" + 
												"   ID_INTEGRANTE \n" +
												"	FROM SIM_GRUPO_INTEGRANTE  \n" +
												"	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
												"	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
												"	AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n" +
												"	AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
												"   AND FECHA_BAJA_LOGICA IS NULL \n";
										PreparedStatement ps42 = this.conn.prepareStatement(sSql);
										ps42.execute();
										ResultSet rs42 = ps42.getResultSet();
										
										if(!rs42.next()){
											
											sSql =  "   SELECT ID_PRESTAMO \n" + 
													"	FROM SIM_PRESTAMO_GPO_DET  \n" +
													" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
													" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
													" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
											ejecutaSql();
											if (rs.next()){
												registro.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO"));
												System.out.println("41"+sSql);
												
												sSql =  " UPDATE SIM_PRESTAMO SET "+
														" ID_ETAPA_PRESTAMO 		='18' \n" +
														" WHERE ID_PRESTAMO 	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
														" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
														
												//VERIFICA SI DIO DE ALTA EL REGISTRO
												if (ejecutaUpdate() == 0){
													resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
												}
												
												sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET "+
														" ID_ETAPA_PRESTAMO 		='18' \n" +
														" WHERE ID_PRESTAMO 	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
														" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
												
												//VERIFICA SI DIO DE ALTA EL REGISTRO
												if (ejecutaUpdate() == 0){
													resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
												}
												
											}
											
											
										}
									}
											
										
										}else{
											//El número de integrantes con negocios tipo venta por catálogo no es el permitido.
											System.out.println("**NO CUMPLE** con los tipo de negocio por catalogo permitido");
											resultadoCatalogo.mensaje.setClave("INTEGRANTES_CATALOGO_ERROR");
										}
											
									}else{
										//El número de integrantes con negocios tipo ambulantes no es el permitido.
										System.out.println("**NO CUMPLE** con los tipo de negocio ambulante permitido");
										resultadoCatalogo.mensaje.setClave("INTEGRANTES_AMBULANTE_ERROR");
									}
									
								} else {
									//El número de candidatos en riesgo no encuentra dentro de los parámetros globales del grupo.
									System.out.println("**NO CUMPLE** con los integrantes en riesgo permitidos");
									resultadoCatalogo.mensaje.setClave("INTEGRANTES_MAX_RIESGO_ERROR");
								}
							}else {
								//El número de integrantes para formar el grupo no se encuentra dentro de los parámetros globales del grupo.
								System.out.println("***NO*** Cumple con los integrantes maximos");
								resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
							}
						}else {
							//El número de integrantes para formar el grupo no se encuentra dentro de los parámetros globales del grupo.
							System.out.println("***NO*** Cumple con los integrantes mínimos");
							resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
						}
						
					}else{
						
						sSql =  "SELECT \n"+
								"ID_INTEGRANTE \n"+
								"FROM SIM_GRUPO_INTEGRANTE \n"+
								"WHERE CVE_GPO_EMPRESA='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
								"AND CVE_EMPRESA='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_GRUPO='" + (String)registro.getDefCampo("ID_GRUPO") + "' \n"+
								"AND FECHA_BAJA_LOGICA IS NULL \n" ;
						
						PreparedStatement ps1 = this.conn.prepareStatement(sSql);
						ps1.execute();
						ResultSet rs1 = ps1.getResultSet();
						
						while (rs1.next()){
							
							registro.addDefCampo("ID_PERSONA",rs1.getString("ID_INTEGRANTE")== null ? "": rs1.getString("ID_INTEGRANTE"));
							
							//Busca todos los creditos del cliente.
							sSql =  "SELECT \n" +
									"ID_PRESTAMO \n" +
									"FROM SIM_PRESTAMO \n" +
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
									"AND ID_CLIENTE = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" ;
							
							PreparedStatement ps2 = this.conn.prepareStatement(sSql);
							ps2.execute();
							ResultSet rs2 = ps2.getResultSet();
							
							while (rs2.next()){
								registro.addDefCampo("ID_PRESTAMO",rs2.getString("ID_PRESTAMO")== null ? "": rs2.getString("ID_PRESTAMO"));
								
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
								
								PreparedStatement ps3 = this.conn.prepareStatement(sSql);
								ps3.execute();
								ResultSet rs3 = ps3.getResultSet();
								
								if (rs3.next()){
									sSaldo = rs3.getString("IMP_DESGLOSE");
									fSaldo = (Float.parseFloat(sSaldo));
								
								}
								
								fSaldo = fSaldo < 0 ? -fSaldo : fSaldo;
								
								if (fSaldo >= iDeudaMinima){
									iDeuda++;
								}
							}
						}
						if (iDeuda != 0){
							resultadoCatalogo.mensaje.setClave("PRESTAMO_VIGENTE_GRUPO");
						}else {
							//Cuenta los integrantes del grupo.
							sSql =  "SELECT COUNT(*) NUM_INTEGRANTES \n" +
								"FROM SIM_GRUPO_INTEGRANTE  \n"+
								"WHERE \n"+
								"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
								"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
								"AND FECHA_BAJA_LOGICA IS NULL \n" ;
							PreparedStatement ps4 = this.conn.prepareStatement(sSql);
							ps4.execute();
							ResultSet rs4 = ps4.getResultSet();
							if (rs4.next()){
								sNumIntegrantes = rs4.getString("NUM_INTEGRANTES");
							}
							
							//Obtiene de los parámetros globales del grupo el mínimo de integrantes que debe tener un grupo.
							sSql =  "SELECT \n"+
								"	MIN(NUM_INTEGRANTE) MINIMO_INTEGRANTES \n"+
								"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
							PreparedStatement ps5 = this.conn.prepareStatement(sSql);
							ps5.execute();
							ResultSet rs5 = ps5.getResultSet();
							
							if (rs5.next()){
								sMinIntegrantes = rs5.getString("MINIMO_INTEGRANTES");
							}
							
							//Obtiene de los parámetros globales del grupo el máximo de integrantes que debe tener un grupo.
							sSql =  "SELECT \n"+
								"	MAX(NUM_INTEGRANTE) MAXIMO_INTEGRANTES \n"+
								"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
							PreparedStatement ps6 = this.conn.prepareStatement(sSql);
							ps6.execute();
							ResultSet rs6 = ps6.getResultSet();
							if (rs6.next()){
								sMaxIntegrantes = rs6.getString("MAXIMO_INTEGRANTES");
							}
							
							int iNumIntegrantes =Integer.parseInt(sNumIntegrantes.trim());
							int iMinIntegrantes =Integer.parseInt(sMinIntegrantes.trim());
							int iMaxIntegrantes =Integer.parseInt(sMaxIntegrantes.trim());
							
							//Compara si el número de candidatos al grupo se encuentra dentro de los parámetros globales del grupo.
							if (iNumIntegrantes >= iMinIntegrantes){
								
								if (iNumIntegrantes <= iMaxIntegrantes){
									
									//Obtiene de los parámetros globales del grupo el máximo de integrantes que pueden estar en riesgo.
									sSql =  "SELECT \n"+
										"	MAXIMO_RIESGO \n"+
										"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
										"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
									PreparedStatement ps7 = this.conn.prepareStatement(sSql);
									ps7.execute();
									ResultSet rs7 = ps7.getResultSet();
									
									if (rs7.next()){
										sMaximoRiego = rs7.getString("MAXIMO_RIESGO");
									}
									
									int iMaximoRiego =Integer.parseInt(sMaximoRiego.trim());
									
									//Obtiene de los parámetros globales del grupo el máximo de integrantes que pueden tener negocios ambulantes.
									sSql =  "SELECT \n"+
										"	MAX_AMBULANTE \n"+
										"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
										"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
									PreparedStatement ps8 = this.conn.prepareStatement(sSql);
									ps8.execute();
									ResultSet rs8 = ps8.getResultSet();
									
									if (rs8.next()){
										sMaximoAmbulante = rs8.getString("MAX_AMBULANTE");
									}
									
									int iMaximoAmbulante =Integer.parseInt(sMaximoAmbulante.trim());
									
									//Obtiene de los parámetros globales del grupo el máximo de integrantes que pueden tener negocios de venta por catálogo.
									sSql =  "SELECT \n"+
										"	MAX_CATALOGO \n"+
										"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
										"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
									PreparedStatement ps9 = this.conn.prepareStatement(sSql);
									ps9.execute();
									ResultSet rs9 = ps9.getResultSet();
									
									if (rs9.next()){
										sMaximoCatalogo = rs9.getString("MAX_CATALOGO");
									}
									
									int iMaximoCatalogo =Integer.parseInt(sMaximoCatalogo.trim());
									
									//Obtiene los candidatos propuestos a formar el grupo.
									sSql =  "SELECT ID_INTEGRANTE \n" +
										"FROM SIM_GRUPO_INTEGRANTE  \n"+
										"WHERE \n"+
										"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
										"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
										"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
										"AND FECHA_BAJA_LOGICA IS NULL \n" ;
									PreparedStatement ps10 = this.conn.prepareStatement(sSql);
									ps10.execute();
									ResultSet rs10 = ps10.getResultSet();		
										
									while (rs10.next()){
										
										sIdIntegrantes = rs10.getString("ID_INTEGRANTE");
										//Busca si el tipo de negocio del candidato es ambulante o de venta por catálogo.
										sSql =  "SELECT NOM_NEGOCIO \n" +
											"FROM SIM_CLIENTE_NEGOCIO  \n"+
											"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
											"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
											"AND B_PRINCIPAL = 'V' \n" +
											"AND (ID_TIPO_NEGOCIO = '3' OR ID_TIPO_NEGOCIO = '4') \n" +
											"AND ID_PERSONA = '" + sIdIntegrantes + "' \n";
										
										PreparedStatement ps11 = this.conn.prepareStatement(sSql);
										ps11.execute();
										ResultSet rs11 = ps11.getResultSet();
							
										if (rs11.next()){
											iMaxRiesgoProp ++;
										}
										rs11.close();
										ps11.close();	
									}
									
									//Comprueba si el número de candidatos en riesgo se encuentra dentro de los parámetros globales del grupo.
									if (iMaxRiesgoProp <= iMaximoRiego){
										
										//Obtiene los candidatos propuestos a formar el grupo.
										sSql =  "SELECT ID_INTEGRANTE \n" +
											"FROM SIM_GRUPO_INTEGRANTE  \n"+
											"WHERE \n"+
											"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
											"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
											"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" ;
										ejecutaSql();			
										
										
										while (rs.next()){
											
											sIdIntegrantes = rs.getString("ID_INTEGRANTE");
											//Busca si el tipo de negocio del candidato es ambulante.
											sSql =  "SELECT NOM_NEGOCIO \n" +
												"FROM SIM_CLIENTE_NEGOCIO  \n"+
												"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
												"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
												"AND B_PRINCIPAL = 'V' \n" +
												"AND ID_TIPO_NEGOCIO = '3' \n" +
												"AND ID_PERSONA = '" + sIdIntegrantes + "' \n";
											
											PreparedStatement ps12 = this.conn.prepareStatement(sSql);
											ps12.execute();
											ResultSet rs12 = ps12.getResultSet();
								
											if (rs12.next()){
												iMaxAmbulanteProp ++;
											}
											rs12.close();
											ps12.close();
										}	
											
										//Comprueba si el número de integrantes con negocios tipo ambulantes es el permitido.
										if (iMaxAmbulanteProp <= iMaximoAmbulante){
											
											//Obtiene los candidatos propuestos a formar el grupo.
											sSql =  "SELECT ID_INTEGRANTE \n" +
												"FROM SIM_GRUPO_INTEGRANTE  \n"+
												"WHERE \n"+
												"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
												"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
												"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" ;
											PreparedStatement ps13 = this.conn.prepareStatement(sSql);
											ps13.execute();
											ResultSet rs13 = ps13.getResultSet();		
											
											
											while (rs13.next()){
												
												sIdIntegrantes = rs13.getString("ID_INTEGRANTE");
												//Busca si el tipo de negocio del candidato es venta por catálogo.
												sSql =  "SELECT NOM_NEGOCIO \n" +
													"FROM SIM_CLIENTE_NEGOCIO  \n"+
													"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
													"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
													"AND B_PRINCIPAL = 'V' \n" +
													"AND ID_TIPO_NEGOCIO = '4' \n" +
													"AND ID_PERSONA = '" + sIdIntegrantes + "' \n";
												
												PreparedStatement ps14 = this.conn.prepareStatement(sSql);
												ps14.execute();
												ResultSet rs14 = ps14.getResultSet();
									
												if (rs14.next()){
													iMaxCatalogoProp ++;
												}
												rs14.close();
												ps14.close();
											}	
											
											//Comprueba si el número de integrantes con negocios tipo venta por catálogo es el permitido.
											if (iMaxCatalogoProp <= iMaximoCatalogo){
												
											

												sSql =  "   SELECT \n" + 
												"   ID_INTEGRANTE \n" +
												"	FROM SIM_GRUPO_INTEGRANTE  \n" +
												"	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
												"	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
												"	AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
												"   AND FECHA_BAJA_LOGICA IS NULL \n";
										PreparedStatement ps15 = this.conn.prepareStatement(sSql);
										ps15.execute();
										ResultSet rs15 = ps15.getResultSet();
										
										while (rs15.next()){
											registro.addDefCampo("ID_INTEGRANTE",rs15.getString("ID_INTEGRANTE")== null ? "": rs15.getString("ID_INTEGRANTE"));
											
											sSql =  " SELECT ID_INTEGRANTE \n"+
													" FROM SIM_PRESTAMO_GPO_DET \n"+
													" WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
													" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
													" AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
													" AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n" ;
											PreparedStatement ps16 = this.conn.prepareStatement(sSql);
											ps16.execute();
											ResultSet rs16 = ps16.getResultSet();
											
											if(!rs16.next()){
												//Da de alta el crédito
												sSql =  "INSERT INTO SIM_PRESTAMO_GPO_DET ( "+
														"CVE_GPO_EMPRESA, \n" +
														"CVE_EMPRESA, \n" +
														"ID_PRESTAMO_GRUPO, \n" +
														"ID_INTEGRANTE) \n" +
														" VALUES (" +
														"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
														"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
														"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "', \n" +
														"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "') \n" ;
												
												PreparedStatement ps17 = this.conn.prepareStatement(sSql);
												ps17.execute();
												ResultSet rs17 = ps17.getResultSet();	
												
												sSql = "SELECT \n"+
														"PG.CVE_GPO_EMPRESA, \n"+
														"PG.CVE_EMPRESA, \n"+
														"PG.ID_PRODUCTO, \n"+
														"PG.ID_GRUPO, \n"+
														"PG.NUM_CICLO, \n"+
														"PG.ID_PERIODICIDAD_PRODUCTO, \n"+
														"PG.CVE_METODO, \n"+
														"P.APLICA_A, \n"+
														"PG.PLAZO, \n"+
														"PG.TIPO_TASA, \n"+
														"PG.VALOR_TASA, \n"+
														"PG.ID_PERIODICIDAD_TASA, \n"+
														"P.MONTO_MINIMO, \n"+
														"PG.MONTO_MAXIMO, \n"+
														"PG.ID_TIPO_RECARGO, \n"+
														"PG.TIPO_TASA_RECARGO, \n"+
														"PG.TASA_RECARGO, \n"+
														"PG.ID_PERIODICIDAD_TASA_RECARGO, \n"+
														"PG.ID_TASA_REFERENCIA_RECARGO, \n"+
														"PG.FACTOR_TASA_RECARGO, \n"+
														"PG.MONTO_FIJO_PERIODO, \n"+
														"PC.ID_FORMA_DISTRIBUCION, \n"+
														"PC.PORC_FLUJO_CAJA \n"+
														"FROM SIM_PRESTAMO_GRUPO PG, \n"+ 
														"SIM_PRODUCTO P, \n"+
														"SIM_PRODUCTO_CICLO PC \n"+
														"WHERE PG.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
														"AND PG.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														"AND PG.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
														"AND P.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
														"AND P.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
														"AND P.ID_PRODUCTO = PG.ID_PRODUCTO \n"+
														"AND PC.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
														"AND PC.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
														"AND PC.ID_PRODUCTO = PG.ID_PRODUCTO \n"+
														"AND PC.NUM_CICLO = PG.NUM_CICLO \n";
												PreparedStatement ps18 = this.conn.prepareStatement(sSql);
												ps18.execute();
												ResultSet rs18 = ps18.getResultSet();	
											
												if(rs18.next()){
													registro.addDefCampo("ID_PERIODICIDAD_PRODUCTO",rs18.getString("ID_PERIODICIDAD_PRODUCTO")== null ? "": rs18.getString("ID_PERIODICIDAD_PRODUCTO"));
													registro.addDefCampo("ID_GRUPO",rs18.getString("ID_GRUPO")== null ? "": rs18.getString("ID_GRUPO"));
													registro.addDefCampo("ID_PRODUCTO",rs18.getString("ID_PRODUCTO")== null ? "": rs18.getString("ID_PRODUCTO"));
													registro.addDefCampo("NUM_CICLO",rs18.getString("NUM_CICLO")== null ? "": rs18.getString("NUM_CICLO"));
													registro.addDefCampo("CVE_METODO",rs18.getString("CVE_METODO")== null ? "": rs18.getString("CVE_METODO"));
													registro.addDefCampo("APLICA_A",rs18.getString("APLICA_A")== null ? "": rs18.getString("APLICA_A"));
													registro.addDefCampo("PLAZO",rs18.getString("PLAZO")== null ? "": rs18.getString("PLAZO"));
													registro.addDefCampo("TIPO_TASA",rs18.getString("TIPO_TASA")== null ? "": rs18.getString("TIPO_TASA"));
													registro.addDefCampo("VALOR_TASA",rs18.getString("VALOR_TASA")== null ? "": rs18.getString("VALOR_TASA"));
													registro.addDefCampo("ID_PERIODICIDAD_TASA",rs18.getString("ID_PERIODICIDAD_TASA")== null ? "": rs18.getString("ID_PERIODICIDAD_TASA"));
													registro.addDefCampo("MONTO_MINIMO",rs18.getString("MONTO_MINIMO")== null ? "": rs18.getString("MONTO_MINIMO"));
													registro.addDefCampo("MONTO_MAXIMO",rs18.getString("MONTO_MAXIMO")== null ? "": rs18.getString("MONTO_MAXIMO"));
													registro.addDefCampo("ID_TIPO_RECARGO",rs18.getString("ID_TIPO_RECARGO")== null ? "": rs18.getString("ID_TIPO_RECARGO"));
													registro.addDefCampo("TIPO_TASA_RECARGO",rs18.getString("TIPO_TASA_RECARGO")== null ? "": rs18.getString("TIPO_TASA_RECARGO"));
													registro.addDefCampo("TASA_RECARGO",rs18.getString("TASA_RECARGO")== null ? "": rs18.getString("TASA_RECARGO"));
													registro.addDefCampo("ID_PERIODICIDAD_TASA_RECARGO",rs18.getString("ID_PERIODICIDAD_TASA_RECARGO")== null ? "": rs18.getString("ID_PERIODICIDAD_TASA_RECARGO"));
													registro.addDefCampo("ID_TASA_REFERENCIA_RECARGO",rs18.getString("ID_TASA_REFERENCIA_RECARGO")== null ? "": rs18.getString("ID_TASA_REFERENCIA_RECARGO"));
													registro.addDefCampo("FACTOR_TASA_RECARGO",rs18.getString("FACTOR_TASA_RECARGO")== null ? "": rs18.getString("FACTOR_TASA_RECARGO"));
													registro.addDefCampo("MONTO_FIJO_PERIODO",rs18.getString("MONTO_FIJO_PERIODO")== null ? "": rs18.getString("MONTO_FIJO_PERIODO"));
													registro.addDefCampo("ID_FORMA_DISTRIBUCION",rs18.getString("ID_FORMA_DISTRIBUCION")== null ? "": rs18.getString("ID_FORMA_DISTRIBUCION"));
													registro.addDefCampo("PORC_FLUJO_CAJA",rs18.getString("PORC_FLUJO_CAJA")== null ? "": rs18.getString("PORC_FLUJO_CAJA"));
													
													String sIdPrestamo = "";
													
													//SE OBTIENE EL SEQUENCE DEL PRESTAMO
													sSql = "SELECT SQ01_SIM_PRESTAMO.nextval as ID_PRESTAMO FROM DUAL";
													ejecutaSql();
													if (rs.next()){
														sIdPrestamo = rs.getString("ID_PRESTAMO");
													}
													
													String sCvePrestamo = "";
													
													//OBTIENE CLAVE DEL PRÉSTAMO.
													sSql =  "SELECT REPLACE (CVE_PRESTAMO,' ','') CVE_PRESTAMO FROM ( \n"+
															"SELECT TO_CHAR('" + (String)registro.getDefCampo("ID_PRODUCTO") + "','00')||TO_CHAR('" + (String)registro.getDefCampo("ID_GRUPO") + "','000000')||TO_CHAR('" + (String)registro.getDefCampo("ID_INTEGRANTE") + "','00000000')||TO_CHAR('" + (String)registro.getDefCampo("NUM_CICLO") + "','00') AS CVE_PRESTAMO FROM DUAL \n"+
															") \n";
													ejecutaSql();
													if (rs.next()){
														sCvePrestamo = rs.getString("CVE_PRESTAMO");
													}
													
													sSql =  "INSERT INTO SIM_PRESTAMO ( \n"+
															"CVE_GPO_EMPRESA, \n" +
															"CVE_EMPRESA, \n" +
															"ID_PRESTAMO, \n" +
															"CVE_PRESTAMO, \n" +
															"ID_PRODUCTO, \n" +
															"ID_CLIENTE, \n" +
															"ID_GRUPO, \n" +
															"NUM_CICLO, \n" +
															"CVE_METODO, \n" +
															"ID_PERIODICIDAD_PRODUCTO, \n" +
															"PLAZO, \n" +
															"TIPO_TASA, \n" +
															"VALOR_TASA, \n" +
															"ID_PERIODICIDAD_TASA, \n" +
															"MONTO_MINIMO, \n" +
															"MONTO_MAXIMO, \n" +
															"PORC_FLUJO_CAJA, \n" +
															"ID_FORMA_DISTRIBUCION, \n" +
															"FECHA_SOLICITUD, \n" +
															"B_ENTREGADO, \n" +
															"APLICA_A) \n" +
													        "VALUES ( \n"+
														"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
														"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
														sIdPrestamo + ", \n "+
														"'" + sCvePrestamo + "', \n" +
														"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
														"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "', \n" +
														"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
														"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
														"'" + (String)registro.getDefCampo("CVE_METODO") + "', \n" +
														"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_PRODUCTO") + "', \n" +
														"'" + (String)registro.getDefCampo("PLAZO") + "', \n" +
														"'" + (String)registro.getDefCampo("TIPO_TASA") + "', \n" +
														"'" + (String)registro.getDefCampo("VALOR_TASA") + "', \n" +
														"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA") + "', \n" +
														"'" + (String)registro.getDefCampo("MONTO_MINIMO") + "', \n" +
														"'" + (String)registro.getDefCampo("MONTO_MAXIMO") + "', \n" +
														"'" + (String)registro.getDefCampo("PORC_FLUJO_CAJA") + "', \n" +
														"'" + (String)registro.getDefCampo("ID_FORMA_DISTRIBUCION") + "', \n" +
														"SYSDATE, \n" +
														"'F', \n" +
														"'" + (String)registro.getDefCampo("APLICA_A") + "') \n" ;
													
													//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
													PreparedStatement ps19 = this.conn.prepareStatement(sSql);
													ps19.execute();
													ResultSet rs19 = ps19.getResultSet();	
											
													//INGRESA LA FECHA DE LA TASA DE REFERENCIA SI EL TIPO DE REFERENCIA ES SI INDEXADA
													String sIndexada = "Si indexada";
													
													sSql = "SELECT \n"+
														"CVE_GPO_EMPRESA, \n"+
														"CVE_EMPRESA, \n"+
														"TIPO_TASA \n"+
														"FROM SIM_PRESTAMO \n"+
														"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
														"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														"AND ID_PRESTAMO = '" + sIdPrestamo + "' \n"+
														"AND TIPO_TASA = '"+sIndexada+"' \n";
													
													PreparedStatement ps20 = this.conn.prepareStatement(sSql);
													ps20.execute();
													ResultSet rs20 = ps20.getResultSet();	
													if(rs20.next()){	
														sSql = "UPDATE SIM_PRESTAMO SET "+
															"FECHA_TASA_REFERENCIA = (SELECT MAX(FECHA_PUBLICACION) FECHA_TASA_REFERENCIA \n"+
																		"FROM ( \n"+
																		"SELECT FECHA_PUBLICACION FROM SIM_CAT_TASA_REFER_DETALLE \n"+
																		"WHERE ID_TASA_REFERENCIA = '" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "' \n" +
																		"AND FECHA_PUBLICACION < TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','yyyy-MM-dd HH24:MI:SSXFF') \n" +
																		" ))\n"+
															"WHERE ID_PRESTAMO = '" + sIdPrestamo + "' \n" +
															"AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
															"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA")+"' \n" ;
													
														PreparedStatement ps21 = this.conn.prepareStatement(sSql);
														ps21.execute();
														ResultSet rs21 = ps21.getResultSet();	
														
													}
											
													sSql =  "SELECT \n"+
															"CVE_GPO_EMPRESA, \n"+
															"CVE_EMPRESA, \n"+
															"ID_CARGO_COMISION, \n"+
															"ID_FORMA_APLICACION, \n"+
															"CARGO_INICIAL, \n"+
															"PORCENTAJE_MONTO, \n"+
															"CANTIDAD_FIJA, \n"+
															"VALOR, \n"+
															"ID_UNIDAD, \n"+
															"ID_PERIODICIDAD \n"+
															"FROM \n"+
															"SIM_PRESTAMO_GPO_CARGO \n"+
															"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
															"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
													
													PreparedStatement ps22 = this.conn.prepareStatement(sSql);
													ps22.execute();
													ResultSet rs22 = ps22.getResultSet();	
													
													while (rs22.next()){
														registro.addDefCampo("ID_CARGO_COMISION",rs22.getString("ID_CARGO_COMISION")== null ? "": rs22.getString("ID_CARGO_COMISION"));
														registro.addDefCampo("ID_FORMA_APLICACION",rs22.getString("ID_FORMA_APLICACION")== null ? "": rs22.getString("ID_FORMA_APLICACION"));
														registro.addDefCampo("CARGO_INICIAL",rs22.getString("CARGO_INICIAL")== null ? "": rs22.getString("CARGO_INICIAL"));
														registro.addDefCampo("PORCENTAJE_MONTO",rs22.getString("PORCENTAJE_MONTO")== null ? "": rs22.getString("PORCENTAJE_MONTO"));
														registro.addDefCampo("CANTIDAD_FIJA",rs22.getString("CANTIDAD_FIJA")== null ? "": rs22.getString("CANTIDAD_FIJA"));
														registro.addDefCampo("VALOR",rs22.getString("VALOR")== null ? "": rs22.getString("VALOR"));
														registro.addDefCampo("ID_UNIDAD",rs22.getString("ID_UNIDAD")== null ? "": rs22.getString("ID_UNIDAD"));
														registro.addDefCampo("ID_PERIODICIDAD",rs22.getString("ID_PERIODICIDAD")== null ? "": rs22.getString("ID_PERIODICIDAD"));
														
															sSql = "INSERT INTO SIM_PRESTAMO_CARGO_COMISION ( \n"+
																"CVE_GPO_EMPRESA, \n" +
																"CVE_EMPRESA, \n" +
																"ID_PRESTAMO, \n" +
																"ID_CARGO_COMISION, \n" +
																"ID_FORMA_APLICACION, \n"+
																"CARGO_INICIAL, \n"+
																"PORCENTAJE_MONTO, \n"+
																"CANTIDAD_FIJA, \n"+
																"VALOR, \n"+
																"ID_UNIDAD, \n"+
																"ID_PERIODICIDAD) \n"+
																" VALUES (" +
																"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
																"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
																"'" + sIdPrestamo + "', \n" +
																"'" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "', \n" +
																"'" + (String)registro.getDefCampo("ID_FORMA_APLICACION") + "', \n" +
																"'" + (String)registro.getDefCampo("CARGO_INICIAL") + "', \n" +
																"'" + (String)registro.getDefCampo("PORCENTAJE_MONTO") + "', \n" +
																"'" + (String)registro.getDefCampo("CANTIDAD_FIJA") + "', \n" +
																"'" + (String)registro.getDefCampo("VALOR") + "', \n" +
																"'" + (String)registro.getDefCampo("ID_UNIDAD") + "', \n" +
																"'" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "') \n" ;
															
															PreparedStatement ps23 = this.conn.prepareStatement(sSql);
															ps23.execute();
															ResultSet rs23 = ps23.getResultSet();	
														}	
												
														
												
														sSql =  "SELECT \n"+
																"CVE_GPO_EMPRESA, \n"+
																"CVE_EMPRESA, \n"+
																"ID_PRODUCTO, \n"+
																"NUM_CICLO, \n"+
																"ID_ACCESORIO, \n"+
																"ORDEN \n"+
																"FROM \n"+
																"SIM_PRODUCTO_CICLO_ACCESORIO \n"+
																"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
																"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
																"AND NUM_CICLO = '" + (String)registro.getDefCampo("NUM_CICLO") + "' \n";
														
														PreparedStatement ps26 = this.conn.prepareStatement(sSql);
														ps26.execute();
														ResultSet rs26 = ps26.getResultSet();
														
														while (rs26.next()){
															registro.addDefCampo("ID_ACCESORIO",rs26.getString("ID_ACCESORIO")== null ? "": rs26.getString("ID_ACCESORIO"));
															registro.addDefCampo("ORDEN",rs26.getString("ORDEN")== null ? "": rs26.getString("ORDEN"));
																	
															sSql = "INSERT INTO SIM_PRESTAMO_ACCESORIO ( \n"+
																"CVE_GPO_EMPRESA, \n" +
																"CVE_EMPRESA, \n" +
																"ID_PRESTAMO, \n" +
																"ID_ACCESORIO, \n" +
																"ORDEN) \n" +
																" VALUES (" +
																"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
																"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
																"'" + sIdPrestamo + "', \n" +
																"'" + (String)registro.getDefCampo("ID_ACCESORIO") + "', \n" +
																"'" + (String)registro.getDefCampo("ORDEN") + "') \n" ;
															
															PreparedStatement ps27 = this.conn.prepareStatement(sSql);
															ps27.execute();
															ResultSet rs27 = ps27.getResultSet();		
														}
													
														sSql =  "SELECT \n"+
																"CVE_GPO_EMPRESA, \n"+
																"CVE_EMPRESA, \n"+
																"ID_PRODUCTO, \n"+
																"ID_ACTIVIDAD_REQUISITO, \n"+
																"ID_ETAPA_PRESTAMO, \n"+
																"ORDEN_ETAPA \n"+
																"FROM \n"+
																"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
																"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
																"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
													
														PreparedStatement ps28 = this.conn.prepareStatement(sSql);
														ps28.execute();
														ResultSet rs28 = ps28.getResultSet();	
															
														while (rs28.next()){
															registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs28.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs28.getString("ID_ACTIVIDAD_REQUISITO"));
															registro.addDefCampo("ID_ETAPA_PRESTAMO",rs28.getString("ID_ETAPA_PRESTAMO")== null ? "": rs28.getString("ID_ETAPA_PRESTAMO"));
															registro.addDefCampo("ORDEN_ETAPA",rs28.getString("ORDEN_ETAPA")== null ? "": rs28.getString("ORDEN_ETAPA"));
															
															sSql = "INSERT INTO SIM_PRESTAMO_ETAPA ( \n"+
																	"CVE_GPO_EMPRESA, \n" +
																	"CVE_EMPRESA, \n" +
																	"ID_PRESTAMO, \n" +
																	"ID_ACTIVIDAD_REQUISITO, \n" +
																	"ID_ETAPA_PRESTAMO, \n"+
																	"ORDEN_ETAPA) \n"+
																	" VALUES (" +
																	"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
																	"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
																	"'" + sIdPrestamo + "', \n" +
																	"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
																	"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
																	"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "') \n" ;
															
																	PreparedStatement ps29 = this.conn.prepareStatement(sSql);
																	ps29.execute();
																	ResultSet rs29 = ps29.getResultSet();
																	
														}
													
														//SE OBTIENE EL SEQUENCE DE LA CUENTA
														sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
														ejecutaSql();
														if (rs.next()){
															registro.addDefCampo("ID_CUENTA",rs.getString("ID_CUENTA"));
															
															sSql = "INSERT INTO PFIN_CUENTA ( \n"+
															"CVE_GPO_EMPRESA, \n" +
															"CVE_EMPRESA, \n" +
															"ID_CUENTA, \n" +
															"SIT_CUENTA, \n" +
															"CVE_TIP_CUENTA, \n" +
															"B_RETIENE_IMPUESTO, \n"+
															"ID_TITULAR) \n"+
															" VALUES (" +
															"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
															"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
															"'" + (String)registro.getDefCampo("ID_CUENTA") + "', \n" +
															"'AC', \n" +
															"'CREDITO', \n" +
															"'F', \n" +
															"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "') \n" ;
															
															if (ejecutaUpdate() == 0){
																resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
															}	
															
															sSql =  " UPDATE SIM_PRESTAMO SET "+
																" ID_CUENTA			='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
																" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
																" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
															
															//VERIFICA SI DIO DE ALTA EL REGISTRO
															if (ejecutaUpdate() == 0){
																resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
															}	
														}
														
														
														sSql =  "SELECT \n"+
																"CVE_GPO_EMPRESA, \n"+
																"CVE_EMPRESA, \n"+
																"ID_CUENTA \n"+
																"FROM \n"+
																"PFIN_CUENTA \n"+
																"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
																"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																"AND ID_TITULAR = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n"+
																"AND CVE_TIP_CUENTA = 'VISTA' \n";
														
														PreparedStatement ps30 = this.conn.prepareStatement(sSql);
														ps30.execute();
														ResultSet rs30 = ps30.getResultSet();
													if (!rs30.next()){
														sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
														
														ejecutaSql();
														if (rs.next()){
															registro.addDefCampo("ID_CUENTA",rs.getString("ID_CUENTA"));
															
															sSql = "INSERT INTO PFIN_CUENTA ( \n"+
																"CVE_GPO_EMPRESA, \n" +
																"CVE_EMPRESA, \n" +
																"ID_CUENTA, \n" +
																"SIT_CUENTA, \n" +
																"CVE_TIP_CUENTA, \n" +
																"B_RETIENE_IMPUESTO, \n"+
																"ID_TITULAR) \n"+
																" VALUES (" +
																"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
																"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
																"'" + (String)registro.getDefCampo("ID_CUENTA") + "', \n" +
																"'AC', \n" +
																"'VISTA', \n" +
																"'V', \n" +
																"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "') \n" ;
															
															PreparedStatement ps31 = this.conn.prepareStatement(sSql);
															ps31.execute();
															ResultSet rs31 = ps31.getResultSet();
															
															sSql =  " UPDATE SIM_PRESTAMO SET "+
																" ID_CUENTA_REFERENCIA 		='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
																" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
																" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
														
															//VERIFICA SI DIO DE ALTA EL REGISTRO
															PreparedStatement ps32 = this.conn.prepareStatement(sSql);
															ps32.execute();
															ResultSet rs32 = ps32.getResultSet();
															
														}
													}else{
														registro.addDefCampo("ID_CUENTA",rs30.getString("ID_CUENTA")== null ? "": rs30.getString("ID_CUENTA"));
														
														sSql =  " UPDATE SIM_PRESTAMO SET "+
																" ID_CUENTA_REFERENCIA 		='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
																" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
																" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
														
															//VERIFICA SI DIO DE ALTA EL REGISTRO
															PreparedStatement ps33 = this.conn.prepareStatement(sSql);
															ps33.execute();
															ResultSet rs33 = ps33.getResultSet();
													
													}
												
													sSql =  "SELECT \n"+
															"CVE_GPO_EMPRESA, \n"+
															"CVE_EMPRESA, \n"+
															"ID_PRODUCTO, \n"+
															"ID_ARCHIVO_FLUJO, \n"+
															"NOM_ARCHIVO \n"+
															"FROM \n"+
															"SIM_PRODUCTO_FLUJO_EFECTIVO \n"+
															"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
															"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
													
													PreparedStatement ps34 = this.conn.prepareStatement(sSql);
													ps34.execute();
													ResultSet rs34 = ps34.getResultSet();
															
															
													while (rs34.next()){
														registro.addDefCampo("ID_ARCHIVO_FLUJO",rs34.getString("ID_ARCHIVO_FLUJO")== null ? "": rs34.getString("ID_ARCHIVO_FLUJO"));
														registro.addDefCampo("NOM_ARCHIVO",rs34.getString("NOM_ARCHIVO")== null ? "": rs34.getString("NOM_ARCHIVO"));
														
														sSql =  "INSERT INTO SIM_PRESTAMO_FLUJO_EFECTIVO ( \n"+
																"CVE_GPO_EMPRESA, \n" +
																"CVE_EMPRESA, \n" +
																"ID_PRESTAMO, \n" +
																"ID_ARCHIVO_FLUJO, \n" +
																"NOM_ARCHIVO) \n"+
																" VALUES (" +
																"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
																"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
																"'" + sIdPrestamo + "', \n" +
																"'" + (String)registro.getDefCampo("ID_ARCHIVO_FLUJO") + "', \n" +
																"'" + (String)registro.getDefCampo("NOM_ARCHIVO") + "') \n" ;
														
																PreparedStatement ps35 = this.conn.prepareStatement(sSql);
																ps35.execute();
																ResultSet rs35 = ps35.getResultSet();
													}
														
													sSql =  "SELECT \n"+
															"CVE_GPO_EMPRESA, \n"+
															"CVE_EMPRESA, \n"+
															"ID_PRODUCTO, \n"+
															"ID_DOCUMENTO \n"+
															"FROM \n"+
															"SIM_PRODUCTO_DOCUMENTACION \n"+
															"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
															"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
													
													PreparedStatement ps36 = this.conn.prepareStatement(sSql);
													ps36.execute();
													ResultSet rs36 = ps36.getResultSet();
													while (rs36.next()){
														registro.addDefCampo("ID_DOCUMENTO",rs36.getString("ID_DOCUMENTO")== null ? "": rs36.getString("ID_DOCUMENTO"));
																	
															sSql =  "INSERT INTO SIM_PRESTAMO_DOCUMENTACION ( \n"+
																	"CVE_GPO_EMPRESA, \n" +
																	"CVE_EMPRESA, \n" +
																	"ID_PRESTAMO, \n" +
																	"ID_DOCUMENTO) \n" +
																	" VALUES (" +
																	"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
																	"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
																	"'" + sIdPrestamo + "', \n" +
																	"'" + (String)registro.getDefCampo("ID_DOCUMENTO") + "') \n" ;
															
															PreparedStatement ps37 = this.conn.prepareStatement(sSql);
															ps37.execute();
															ResultSet rs37 = ps37.getResultSet();
													}	
														
													sSql =  "SELECT  \n"+
															"ID_ETAPA_PRESTAMO \n"+
															"FROM \n"+
															"SIM_PRESTAMO_GRUPO \n"+
															"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
															"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
													
													PreparedStatement ps38 = this.conn.prepareStatement(sSql);
													ps38.execute();
													ResultSet rs38 = ps38.getResultSet();
													if (rs38.next()){
														registro.addDefCampo("ID_ETAPA_PRESTAMO",rs38.getString("ID_ETAPA_PRESTAMO")== null ? "": rs38.getString("ID_ETAPA_PRESTAMO"));
													}
													
													sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET "+
															" ID_ETAPA_PRESTAMO 		='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
															" ID_PRESTAMO 		='" + sIdPrestamo + "' \n" +
															" WHERE ID_PRESTAMO_GRUPO		='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
															" AND ID_INTEGRANTE		='" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n"+
															" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
												
													//VERIFICA SI DIO DE ALTA EL REGISTRO
													PreparedStatement ps39 = this.conn.prepareStatement(sSql);
													ps39.execute();
													ResultSet rs39 = ps39.getResultSet();
													
													sSql =  " UPDATE SIM_PRESTAMO SET "+
															" ID_ETAPA_PRESTAMO 		='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
															" WHERE ID_PRESTAMO 		='" + sIdPrestamo + "' \n" +
															" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
												
													//VERIFICA SI DIO DE ALTA EL REGISTRO
													PreparedStatement ps40 = this.conn.prepareStatement(sSql);
													ps40.execute();
													ResultSet rs40 = ps40.getResultSet();
													
													sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
													PreparedStatement ps41 = this.conn.prepareStatement(sSql);
													ps41.execute();
													ResultSet rs41 = ps41.getResultSet();
													if (rs41.next()){
														registro.addDefCampo("ID_HISTORICO",rs.getString("ID_HISTORICO"));
														
														sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
															"CVE_GPO_EMPRESA, \n" +
															"CVE_EMPRESA, \n" +
															"ID_HISTORICO, \n" +
															"ID_PRESTAMO, \n" +
															//"ID_ACTIVIDAD_REQUISITO, \n" +
															"ID_ETAPA_PRESTAMO, \n"+
															"FECHA_REGISTRO)\n"+
															" VALUES (" +
															"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
															"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
															"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
															"'" + sIdPrestamo + "', \n" +
															//"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
															"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
															"SYSDATE) \n";
															
														PreparedStatement ps42 = this.conn.prepareStatement(sSql);
														ps42.execute();
														ResultSet rs42 = ps42.getResultSet();
													}
												}
											}
										}
										
										sSql =  " SELECT ID_INTEGRANTE \n"+
												" FROM SIM_PRESTAMO_GPO_DET \n"+
												" WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
												" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
												" AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
									
										PreparedStatement ps41 = this.conn.prepareStatement(sSql);
										ps41.execute();
										ResultSet rs41 = ps41.getResultSet();	
										while (rs41.next()){
											registro.addDefCampo("ID_INTEGRANTE",rs41.getString("ID_INTEGRANTE")== null ? "": rs41.getString("ID_INTEGRANTE"));
											
											sSql =  " SELECT ID_PRESTAMO \n"+
													" FROM SIM_PRESTAMO_GPO_DET \n"+
													" WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
													" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
													" AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
													" AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n" ;
											ejecutaSql();
											if (rs.next()){
												registro.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO"));
											}
											
											sSql =  "   SELECT \n" + 
													"   ID_INTEGRANTE \n" +
													"	FROM SIM_GRUPO_INTEGRANTE  \n" +
													"	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
													"	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
													"	AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n" +
													"	AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
													"   AND FECHA_BAJA_LOGICA IS NULL \n";
											PreparedStatement ps42 = this.conn.prepareStatement(sSql);
											ps42.execute();
											ResultSet rs42 = ps42.getResultSet();
											
											
											if(!rs42.next()){
												
												sSql =  "   SELECT ID_PRESTAMO \n" + 
														"	FROM SIM_PRESTAMO_GPO_DET  \n" +
														" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
														" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
												PreparedStatement ps43 = this.conn.prepareStatement(sSql);
												ps43.execute();
												ResultSet rs43 = ps43.getResultSet();
												if (rs43.next()){
													registro.addDefCampo("ID_PRESTAMO",rs43.getString("ID_PRESTAMO"));
													
													
													sSql =  " UPDATE SIM_PRESTAMO SET "+
															" ID_ETAPA_PRESTAMO 		='16' \n" +
															" WHERE ID_PRESTAMO 	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
															" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
													
													//VERIFICA SI DIO DE ALTA EL REGISTRO
													if (ejecutaUpdate() == 0){
														resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
													}
													
													sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET "+
															" ID_ETAPA_PRESTAMO 		='16' \n" +
															" WHERE ID_PRESTAMO 	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
															" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
													
													//VERIFICA SI DIO DE ALTA EL REGISTRO
													if (ejecutaUpdate() == 0){
														resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
													}
													
												}
												
												
											}
										}
											
												
											}else{
												//El número de integrantes con negocios tipo venta por catálogo no es el permitido.
												System.out.println("**NO CUMPLE** con los tipo de negocio por catalogo permitido");
												resultadoCatalogo.mensaje.setClave("INTEGRANTES_CATALOGO_ERROR");
											}
												
										}else{
											//El número de integrantes con negocios tipo ambulantes no es el permitido.
											System.out.println("**NO CUMPLE** con los tipo de negocio ambulante permitido");
											resultadoCatalogo.mensaje.setClave("INTEGRANTES_AMBULANTE_ERROR");
										}
										
									} else {
										//El número de candidatos en riesgo no encuentra dentro de los parámetros globales del grupo.
										System.out.println("**NO CUMPLE** con los integrantes en riesgo permitidos");
										resultadoCatalogo.mensaje.setClave("INTEGRANTES_MAX_RIESGO_ERROR");
									}
								}else {
									//El número de integrantes para formar el grupo no se encuentra dentro de los parámetros globales del grupo.
									System.out.println("***NO*** Cumple con los integrantes maximos");
									resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
								}
							}else {
								//El número de integrantes para formar el grupo no se encuentra dentro de los parámetros globales del grupo.
								System.out.println("***NO*** Cumple con los integrantes mínimos");
								resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
							}
							
						}
					}
				}
				
			}else if (registro.getDefCampo("MOVIMIENTO").equals("CANCELAR_PRESTAMO")) {
				
				sSql =  " SELECT \n"+
						"CVE_FUNCION, \n"+
						"CVE_APLICACION, \n"+
						"CVE_PERFIL \n"+
						"FROM RS_CONF_APLICACION_CONFIG \n"+
						"WHERE CVE_FUNCION = (SELECT \n"+
						"CVE_FUNCION \n"+
						"FROM \n"+
						"SIM_CAT_ETAPA_PRESTAMO \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_ETAPA_PRESTAMO = '16' \n" +
						") \n"+
						"AND CVE_APLICACION = 'Prestamo' \n"+
						"AND CVE_PERFIL = ( \n"+
						"SELECT \n"+
						"CVE_PERFIL \n"+ 
						"FROM RS_GRAL_USUARIO_PERFIL \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_USUARIO = '" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
						"AND CVE_APLICACION = 'Prestamo') \n";
				
				ejecutaSql();
				if(rs.next()){
				
				
					sSql =  " UPDATE SIM_PRESTAMO_GRUPO SET "+
							" ID_ETAPA_PRESTAMO 		='16' \n" +
							" WHERE ID_PRESTAMO_GRUPO 	='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
							" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				
					if (ejecutaUpdate() == 0){
						resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
					}
					
					sSql =  " SELECT \n"+
							" ID_PRESTAMO \n"+
							" FROM \n"+
							" SIM_PRESTAMO_GPO_DET \n"+
							" WHERE ID_PRESTAMO_GRUPO 	='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
							" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
					
					ejecutaSql();
					while (rs.next()){
						registro.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO"));
						
						sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET "+
								" ID_ETAPA_PRESTAMO 		='16' \n" +
								" WHERE ID_PRESTAMO 	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
								" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						
						PreparedStatement ps1 = this.conn.prepareStatement(sSql);
						ps1.execute();
						ResultSet rs1 = ps1.getResultSet();
						
						sSql =  " UPDATE SIM_PRESTAMO SET "+
								" ID_ETAPA_PRESTAMO 		='16' \n" +
								" WHERE ID_PRESTAMO 	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
								" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						
						PreparedStatement ps2 = this.conn.prepareStatement(sSql);
						ps2.execute();
						ResultSet rs2 = ps2.getResultSet();
					}
				}
			}
			
		return resultadoCatalogo;
	}
}