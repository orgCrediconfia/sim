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
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;


/**
 * Administra los accesos a la base de datos para los pr�stamos grupales.
 */
 
public class SimPrestamoGrupalCreditoIndividualDAO extends Conexion2 implements OperacionAlta, OperacionModificacion {

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
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
				   
				sSql = "SELECT SQ01_SIM_PRESTAMO.nextval as ID_PRESTAMO_GRUPO FROM DUAL";
				ejecutaSql();
				if (rs.next()){
					sIdPrestamoGrupal = rs.getString("ID_PRESTAMO_GRUPO");
				}
				
				//OBTIENE CLAVE DEL PR�STAMO GRUPAL.
				if (registro.getDefCampo("CICLO").equals("igual")){
					sSql =  "SELECT REPLACE (CVE_PRESTAMO_GRUPO,' ','') CVE_PRESTAMO_GRUPO FROM ( \n"+
							"SELECT TO_CHAR('" + (String)registro.getDefCampo("ID_PRODUCTO") + "','00')||TO_CHAR('" + (String)registro.getDefCampo("ID_GRUPO") + "','000000')||'00000000'||TO_CHAR('" + (String)registro.getDefCampo("NUM_CICLO") + "','00') AS CVE_PRESTAMO_GRUPO FROM DUAL \n"+
							") \n";
					ejecutaSql();
					if (rs.next()){
						sCvePrestamoGrupal = rs.getString("CVE_PRESTAMO_GRUPO");
					}
				}else {
					sSql =  "SELECT REPLACE (CVE_PRESTAMO_GRUPO,' ','') CVE_PRESTAMO_GRUPO FROM ( \n"+
							"SELECT TO_CHAR('" + (String)registro.getDefCampo("ID_PRODUCTO") + "','00')||TO_CHAR('" + (String)registro.getDefCampo("ID_GRUPO") + "','000000')||'00000000'||TO_CHAR('" + (String)registro.getDefCampo("CICLO") + "','00') AS CVE_PRESTAMO_GRUPO FROM DUAL \n"+
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
		
			sSql =  "SELECT \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_GRUPO, \n" +
					"ID_INTEGRANTE \n" +
					"FROM SIM_GRUPO_FUNDADOR \n" +
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n"+
					"AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_CLIENTE") + "' \n";
			ejecutaSql();
			if (!rs.next()){
				sSql =  "INSERT INTO SIM_GRUPO_FUNDADOR ( "+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_GRUPO, \n" +
						"ID_INTEGRANTE) \n" +
						" VALUES (" +
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_CLIENTE") + "') \n" ;
				
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ResultSet rs2 = ps2.getResultSet();
			}
			
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
			System.out.println("B");
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
				
					//OBTIENE CLAVE DEL PR�STAMO.
					sSql =  "SELECT REPLACE (CVE_PRESTAMO,' ','') CVE_PRESTAMO FROM ( \n"+
							"SELECT TO_CHAR('" + (String)registro.getDefCampo("ID_PRODUCTO") + "','00')||TO_CHAR('" + (String)registro.getDefCampo("ID_GRUPO") + "','000000')||TO_CHAR('" + (String)registro.getDefCampo("ID_CLIENTE") + "','00000000')||TO_CHAR('" + (String)registro.getDefCampo("NUM_CICLO") + "','00') AS CVE_PRESTAMO FROM DUAL \n"+
							") \n";
					ejecutaSql();
					if (rs.next()){
						sCvePrestamo = rs.getString("CVE_PRESTAMO");
					}
				} else {
					//OBTIENE CLAVE DEL PR�STAMO.
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
						
						//Obtiene la cuenta cr�dito (id_cuenta).
							sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
							ejecutaSql();
							if (rs.next()){
								registro.addDefCampo("ID_CUENTA",rs.getString("ID_CUENTA"));
								//A) Agrega la cuenta cr�dito (id_cuenta) al integrante en la tabla PFIN_CUENTA.
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
								//B) Agrega la cuenta cr�dito (id_cuenta) al credito en la tabla SIM_PRESTAMO.
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
							//Obtiene la cuenta vista (id_cuenta_referencia).
							sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
							
							ejecutaSql();
							if (rs.next()){
								registro.addDefCampo("ID_CUENTA",rs.getString("ID_CUENTA"));
								//A) Agrega la cuenta vista (id_cuenta_referencia) al integrante en la tabla PFIN_CUENTA.
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
								//B) Agrega la cuenta vista (id_cuenta_referencia) al credito en la tabla SIM_PRESTAMO.
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
						
						//Actualiza la etapa del cr�dito a la primer etapa del producto.
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
						
					
						//Actualiza la bit�cora.
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
						}//Actualiza la bit�cora.
						
						
						
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
	 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
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
			
			//Valida si el usuario tiene la funci�n de la etapa.
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
					//La etapa NO es avanzada individualmente, por lo que s�lo se actualiza el comentario.
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
					
					//SI EL CREDITO TIENE UNA ETAPA DIFERENTE A AUTORIZAR POR COMITE, ASIGNACI�N DE L�NEAS DE FONDEO O DESEMBOLSO, AVANZA A LA SIG. ETAPA.
					sSql =  "SELECT \n"+
					  		"ID_ETAPA_PRESTAMO \n"+
					  		"FROM( \n"+
					  				"SELECT \n"+
									"ID_ETAPA_PRESTAMO, \n"+
									"B_AUTORIZAR_COMITE, \n"+
									"B_LINEA_FONDEO, \n"+
									"B_DESEMBOLSO \n"+
									"FROM \n"+
									"SIM_CAT_ETAPA_PRESTAMO \n"+
									"WHERE CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					                "AND B_AUTORIZAR_COMITE = 'V' OR B_LINEA_FONDEO = 'V' OR B_DESEMBOLSO = 'V' \n"+
					             ") \n"+
					          "WHERE ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
					ejecutaSql();						
					if (!rs.next()){
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
										//Actualiza la bit�cora.
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
				System.out.println("Verifica si tiene permisos para manipular la etapa");
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
				System.out.println("Obtiene las actividades de la etapa que se retroce para inicializarlas.");
				while (rs2.next()){
					registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs2.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs2.getString("ID_ACTIVIDAD_REQUISITO"));
					registro.addDefCampo("ORDEN_ETAPA",rs2.getString("ORDEN_ETAPA")== null ? "": rs2.getString("ORDEN_ETAPA"));
					registro.addDefCampo("FECHA_REGISTRO",rs2.getString("FECHA_REGISTRO")== null ? "": rs2.getString("FECHA_REGISTRO"));
					//Guarda el historial de la etapa y las actividades
					sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
					System.out.println("Guarda el historial de la etapa y las actividades");
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
				System.out.println("Obtiene la etapa anterior.");
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
						System.out.println("Retrocede a la etapa anterior.");
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
						
						sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET \n"+
								"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
						System.out.println("1");
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
						System.out.println("2");
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
						System.out.println("3");
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
								System.out.println("4");
								sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
								
								PreparedStatement ps11 = this.conn.prepareStatement(sSql);
								ps11.execute();
								ResultSet rs11 = ps11.getResultSet();	
								if (rs11.next()){
									registro.addDefCampo("ID_HISTORICO",rs11.getString("ID_HISTORICO"));
									//Actualiza la bit�cora.
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
									System.out.println("5");
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
						System.out.println("6");
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
				
				String[] sIdCliente = (String[]) registro.getDefCampo("DAO_ID_CLIENTE");
				String[] sIdPrestamo = (String[]) registro.getDefCampo("DAO_ID_PRESTAMO");
				String[] sIdEtapa = (String[]) registro.getDefCampo("DAO_ID_ETAPA");
				
				String sCliente = new String();
				String sPrestamo = new String();
				String sEtapa = new String();
				
				for (int iNumParametro = 0; iNumParametro < sIdCliente.length; iNumParametro++) {
					sCliente = sIdCliente[iNumParametro];
					sPrestamo = sIdPrestamo[iNumParametro];
					sEtapa = sIdEtapa[iNumParametro];
					
					registro.addDefCampo("ID_CLIENTE",sCliente);
					registro.addDefCampo("ID_PRESTAMO",sPrestamo);
					registro.addDefCampo("ID_ETAPA_PRESTAMO",sEtapa);
					
					//SI EL CREDITO TIENE UNA ETAPA DIFERENTE A AUTORIZAR POR COMITE, ASIGNACI�N DE L�NEAS DE FONDEO O DESEMBOLSO, AVANZA A LA SIG. ETAPA.
					sSql =  "SELECT \n"+
					  		"ID_ETAPA_PRESTAMO \n"+
					  		"FROM( \n"+
					  				"SELECT \n"+
									"ID_ETAPA_PRESTAMO, \n"+
									"B_AUTORIZAR_COMITE, \n"+
									"B_LINEA_FONDEO, \n"+
									"B_DESEMBOLSO \n"+
									"FROM \n"+
									"SIM_CAT_ETAPA_PRESTAMO \n"+
									"WHERE CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					                "AND B_AUTORIZAR_COMITE = 'V' OR B_LINEA_FONDEO = 'V' OR B_DESEMBOLSO = 'V' \n"+
					             ") \n"+
					          "WHERE ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
					ejecutaSql();						
					if (!rs.next()){
					
					
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
											//Actualiza la bit�cora.
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
				float fDeudaMinima = 0;
				boolean bReconforma = true;
				
				//Preguntamos por el par�metro % m�nimo de fundadores del grupo.
				sSql = "SELECT A.INT_FUNDADORES / 100 * B.PORC_INT_FUNDADORES MIN_INT_FUNDADORES \n" +
						"FROM \n" +
						"(SELECT COUNT(*) INT_FUNDADORES \n" +
						"FROM SIM_GRUPO_FUNDADOR \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
						"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "') A, \n" +
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
				while (rs.next()){
					registro.addDefCampo("ID_INTEGRANTE",rs.getString("ID_INTEGRANTE")== null ? "": rs.getString("ID_INTEGRANTE"));
					//Consulta si es un integrante fundador.
					sSql = "SELECT \n"+
							"ID_INTEGRANTE \n"+
							"FROM SIM_GRUPO_FUNDADOR \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
							"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
							"AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n" ;
					
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					ps1.execute();
					ResultSet rs1 = ps1.getResultSet();
					//Si lo es incrementa fIntegrante.
					if (rs1.next()){
						fIntegrante++;
					}
				}
				//Valida si el grupo tiene los minimos fundadores del grupo
				if (fIntegrante < fMinIntFundadores){
					resultadoCatalogo.mensaje.setClave("NO_MINIMO_FUNDADORES");
				}
				else {
					//Obtenemos los par�metros Cr�dito Simult�neos y deuda m�nima.
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
						iDeudaMinima = (Integer.parseInt(sDeudaMinima));
						fDeudaMinima = (Float.parseFloat(sDeudaMinima));
					}
					if (sCreditosSimultaneos.equals("F")){
						//No se permite dar de alta cr�ditos simult�neos.
						//Consulta el saldo actual del grupo.
						sSql= "SELECT V.CVE_GPO_EMPRESA, \n" +
							   "V.CVE_EMPRESA, \n" +
							   "V.Id_Prestamo, \n" +
							   "SUM(V.IMP_SALDO_HOY) IMP_SALDO_HOY \n" + 
							   "From V_SIM_PRESTAMO_GPO_RES_EDO_CTA V, \n" +
							   "SIM_PRESTAMO_GRUPO P \n" +
							   "WHERE V.DESC_MOVIMIENTO IN ('Pago Tard�o','Pago Pago Tard�o','Seguro Deudor','Pago Seguro Deudor','Capital','Pago Capital','Inter�s', 'Inter�s Extra', 'Iva De Intereses', 'Iva Interes Extra', 'Pago Inter�s', 'Pago Inter�s Extra', 'Pago Iva De Intereses', 'Pago Iva Interes Extra') \n" + 
					           "AND V.ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
					           "AND P.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n" +
					           "AND P.CVE_EMPRESA = V.CVE_EMPRESA \n" +
					           "AND P.ID_PRESTAMO_GRUPO = V.ID_PRESTAMO \n" +
					           "AND (P.ID_ETAPA_PRESTAMO != '16' AND P.ID_ETAPA_PRESTAMO != '8') \n" +
							   "GROUP BY V.CVE_GPO_EMPRESA, V.CVE_EMPRESA, V.Id_Prestamo \n" ;
						
						System.out.println("SimPrestamoGrupoCreditoIndividual saldo actual"+sSql);
						System.out.println("fDeudaMinima"+fDeudaMinima);
						ejecutaSql();	
						if (rs.next()){
							sSaldo = rs.getString("IMP_SALDO_HOY");
							fSaldo = (Float.parseFloat(sSaldo));
						}
						if (fDeudaMinima < fSaldo){
							resultadoCatalogo.mensaje.setClave("PRESTAMO_VIGENTE_GRUPO");
							bReconforma = false;
						}
					}
					
					
					if (bReconforma){
						
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
						
						//Obtiene de los par�metros globales del grupo el m�nimo de integrantes que debe tener un grupo.
						sSql =  "SELECT \n"+
							"	MIN(NUM_INTEGRANTE) MINIMO_INTEGRANTES \n"+
							"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
						ejecutaSql();
						
						if (rs.next()){
							sMinIntegrantes = rs.getString("MINIMO_INTEGRANTES");
						}
						
						//Obtiene de los par�metros globales del grupo el m�ximo de integrantes que debe tener un grupo.
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
						
						//Compara si el n�mero de candidatos al grupo se encuentra dentro de los par�metros globales del grupo.
						if (iNumIntegrantes >= iMinIntegrantes){
							
							if (iNumIntegrantes <= iMaxIntegrantes){
								
								//Obtiene de los par�metros globales del grupo el m�ximo de integrantes que pueden estar en riesgo.
								sSql =  "SELECT \n"+
									"	MAXIMO_RIESGO \n"+
									"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
									"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
								ejecutaSql();
								
								if (rs.next()){
									sMaximoRiego = rs.getString("MAXIMO_RIESGO");
								}
								
								int iMaximoRiego =Integer.parseInt(sMaximoRiego.trim());
								
								//Obtiene de los par�metros globales del grupo el m�ximo de integrantes que pueden tener negocios ambulantes.
								sSql =  "SELECT \n"+
									"	MAX_AMBULANTE \n"+
									"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
									"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
								ejecutaSql();
								
								if (rs.next()){
									sMaximoAmbulante = rs.getString("MAX_AMBULANTE");
								}
								
								int iMaximoAmbulante =Integer.parseInt(sMaximoAmbulante.trim());
								
								//Obtiene de los par�metros globales del grupo el m�ximo de integrantes que pueden tener negocios de venta por cat�logo.
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
									//Busca si el tipo de negocio del candidato es ambulante o de venta por cat�logo.
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
								
								//Comprueba si el n�mero de candidatos en riesgo se encuentra dentro de los par�metros globales del grupo.
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
										
									//Comprueba si el n�mero de integrantes con negocios tipo ambulantes es el permitido.
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
										
										while (rs.next()){
											
											sIdIntegrantes = rs.getString("ID_INTEGRANTE");
											//Busca si el tipo de negocio del candidato es venta por cat�logo.
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
										
										//Comprueba si el n�mero de integrantes con negocios tipo venta por cat�logo es el permitido.
										if (iMaxCatalogoProp <= iMaximoCatalogo){
											
											//EL GRUPO CUMPLE CON LOS PAR�METROS GLOBALES DE UN GRUPO POR LO TANTO SE RECONFORMAR� EL GRUPO.
											//Obtiene los integrantes del grupo vigentes.
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
											//Da de alta el cr�dito
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
												
												//OBTIENE CLAVE DEL PR�STAMO.
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
										
												sSql =  "INSERT INTO SIM_CLIENTE_MONTO ( \n" +
														"CVE_GPO_EMPRESA, \n" +
														"CVE_EMPRESA, \n" +
														"ID_PRESTAMO, \n" +
														"ID_CLIENTE, \n" +
														"ID_PRODUCTO, \n" +
														"NUM_CICLO) \n" +
														" VALUES (" +
														"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
														"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
														"'" + sIdPrestamo + "', \n" +
														"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "', \n" +
														"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
														"'" + (String)registro.getDefCampo("NUM_CICLO") + "') \n" ;
						
												
												PreparedStatement ps50 = this.conn.prepareStatement(sSql);
												ps50.execute();
												ResultSet rs50= ps50.getResultSet();
												
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
												
													//Obtiene la cuenta cr�dito (id_cuenta).
													sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
													ejecutaSql();
													if (rs.next()){
														registro.addDefCampo("ID_CUENTA",rs.getString("ID_CUENTA"));
														//A) Agrega la cuenta cr�dito (id_cuenta) al integrante en la tabla PFIN_CUENTA.
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
														//B) Agrega la cuenta cr�dito (id_cuenta) al credito en la tabla SIM_PRESTAMO.
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
													
													
													//Obtiene la cuenta vista (id_cuenta_referencia).
													sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
													
													ejecutaSql();
													if (rs.next()){
														registro.addDefCampo("ID_CUENTA",rs.getString("ID_CUENTA"));
														//A) Agrega la cuenta vista (id_cuenta_referencia) al integrante en la tabla PFIN_CUENTA.
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
														//B) Agrega la cuenta vista (id_cuenta_referencia) al credito en la tabla SIM_PRESTAMO.
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
									//Cancela el pr�stamos a integrantes que fueron dados de baja en el grupo.
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
												
												sSql =  " UPDATE SIM_PRESTAMO SET "+
														" ID_ETAPA_PRESTAMO 		='31' \n" +
														" WHERE ID_PRESTAMO 	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
														" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
														
												//VERIFICA SI DIO DE ALTA EL REGISTRO
												if (ejecutaUpdate() == 0){
													resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
												}
												
												sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET "+
														" ID_ETAPA_PRESTAMO 		='31' \n" +
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
											//El n�mero de integrantes con negocios tipo venta por cat�logo no es el permitido.
											System.out.println("**NO CUMPLE** con los tipo de negocio por catalogo permitido");
											resultadoCatalogo.mensaje.setClave("INTEGRANTES_CATALOGO_ERROR");
										}
											
									}else{
										//El n�mero de integrantes con negocios tipo ambulantes no es el permitido.
										System.out.println("**NO CUMPLE** con los tipo de negocio ambulante permitido");
										resultadoCatalogo.mensaje.setClave("INTEGRANTES_AMBULANTE_ERROR");
									}
									
								} else {
									//El n�mero de candidatos en riesgo no encuentra dentro de los par�metros globales del grupo.
									System.out.println("**NO CUMPLE** con los integrantes en riesgo permitidos");
									resultadoCatalogo.mensaje.setClave("INTEGRANTES_MAX_RIESGO_ERROR");
								}
							}else {
								//El n�mero de integrantes para formar el grupo no se encuentra dentro de los par�metros globales del grupo.
								System.out.println("***NO*** Cumple con los integrantes maximos");
								resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
							}
						}else {
							//El n�mero de integrantes para formar el grupo no se encuentra dentro de los par�metros globales del grupo.
							System.out.println("***NO*** Cumple con los integrantes m�nimos");
							resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
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