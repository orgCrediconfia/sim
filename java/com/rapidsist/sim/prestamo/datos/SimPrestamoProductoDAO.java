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
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;
import com.rapidsist.sim.grupo.datos.SimGrupoIntegranteDAO;


/**
 * Administra los accesos a la base de datos para los préstamos grupales.
 */
 
public class SimPrestamoProductoDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		ResultadoCatalogo resultadoCatalogoAltaActividadRequisito = new ResultadoCatalogo();
		SimPrestamoAltaActividadRequisitoDAO simPrestamoAltaActividadRequisitoDAO = new SimPrestamoAltaActividadRequisitoDAO();
		
		simPrestamoAltaActividadRequisitoDAO.setConexion(this.getConexion());
		
		
		String sIdIntegrante = "";
		String sNumCiclo = "";
		
		//Verificamos si el producto tiene actividades o requisitos definidos.
		sSql =  "SELECT DISTINCT \n"+
			"ID_ETAPA_PRESTAMO \n"+
			"FROM \n"+
			"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
			"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
	
		ejecutaSql();
		
		if (rs.next()){
			/*
			//Obtenemos los datos básicos generales del prestamo.	
			sSql =  "SELECT \n" +
				"FECHA_SOLICITUD, \n" + 
				"FECHA_ENTREGA, \n" + 
				"ID_SUCURSAL, \n" + 
				"ID_COMITE, \n" + 
				"CVE_ASESOR_CREDITO, \n" + 
				"FECHA_ASESOR, \n" + 
				"ID_ETAPA_PRESTAMO \n" + 
				"FROM SIM_PRESTAMO \n"+
				"WHERE \n"+
				"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
				"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
				ejecutaSql();
			if (rs.next()){
				registro.addDefCampo("FECHA_SOLICITUD",rs.getString("FECHA_SOLICITUD")== null ? "": rs.getString("FECHA_SOLICITUD"));
				registro.addDefCampo("FECHA_ENTREGA",rs.getString("FECHA_ENTREGA")== null ? "": rs.getString("FECHA_ENTREGA"));
				registro.addDefCampo("ID_SUCURSAL",rs.getString("ID_SUCURSAL")== null ? "": rs.getString("ID_SUCURSAL"));
				registro.addDefCampo("ID_COMITE",rs.getString("ID_COMITE")== null ? "": rs.getString("ID_COMITE"));
				registro.addDefCampo("CVE_ASESOR_CREDITO",rs.getString("CVE_ASESOR_CREDITO")== null ? "": rs.getString("CVE_ASESOR_CREDITO"));
				registro.addDefCampo("FECHA_ASESOR",rs.getString("FECHA_ASESOR")== null ? "": rs.getString("FECHA_ASESOR"));
				registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO")== null ? "": rs.getString("ID_ETAPA_PRESTAMO"));
			}
			*/
			//Obtiene los integrantes del grupo.
			sSql =  "SELECT ID_INTEGRANTE \n" +
				"FROM SIM_GRUPO_INTEGRANTE \n"+
				"WHERE \n"+
				"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
				"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" ;
			ejecutaSql();			
			//Ingresa el productos-ciclo correspondiente de cada integrante del grupo.
			while (rs.next()){
				sIdIntegrante = rs.getString("ID_INTEGRANTE");
				registro.addDefCampo("ID_INTEGRANTE",sIdIntegrante);
				
				//Verifica si el integrante del grupo tiene algún préstamo de ese producto-ciclo.
				sSql =  "SELECT \n" +
					"ID_PRESTAMO, \n"+
					"ID_PRODUCTO, \n"+
					"NUM_CICLO \n"+
					"FROM SIM_PRESTAMO \n"+
					"WHERE \n"+
					"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
					"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
					"AND ID_CLIENTE = '" + sIdIntegrante + "' \n" ;
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ResultSet rs2 = ps2.getResultSet();
				
				if (rs2.next()){
					
					//Verifica si el producto tiene asignado el ciclo siguiente.
					sSql =  "SELECT \n" +
						"ID_PRODUCTO, \n"+
						"NUM_CICLO \n"+
						"FROM SIM_PRODUCTO_CICLO \n"+
						"WHERE \n"+
						"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
						"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
						"AND NUM_CICLO = (SELECT \n"+
								"MAX(P.NUM_CICLO)+1 NUM_CICLO_PROX \n"+
								"FROM \n"+
								"SIM_PRESTAMO P \n"+
								"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND P.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
								"AND P.ID_CLIENTE = '" + sIdIntegrante + "' \n"+
								") \n";
					PreparedStatement ps3 = this.conn.prepareStatement(sSql);
					ps3.execute();
					ResultSet rs3 = ps3.getResultSet();	
					if (rs3.next()){
					
						//Obtiene el producto-ciclo siguiente.
						
						sNumCiclo = rs3.getString("NUM_CICLO");
						
						sSql = "SELECT \n"+
							"PC.CVE_GPO_EMPRESA, \n"+
							"PC.CVE_EMPRESA, \n"+
							"PC.ID_PRODUCTO, \n"+
							"P.NOM_PRODUCTO, \n"+
							"P.APLICA_A, \n"+
							"P.ID_PERIODICIDAD, \n"+
							"P.CVE_METODO, \n"+
							"PC.NUM_CICLO, \n"+
							"PC.ID_FORMA_DISTRIBUCION, \n"+
							"PC.PLAZO, \n"+
							"PC.TIPO_TASA, \n"+
							"PC.VALOR_TASA, \n"+
							"PC.ID_PERIODICIDAD_TASA, \n"+
							"PC.ID_TASA_REFERENCIA, \n"+
							"PC.MONTO_MAXIMO, \n"+
							"PC.PORC_FLUJO_CAJA, \n"+
							"PC.ID_FORMA_DISTRIBUCION, \n"+
							"P.FECHA_INICIO_ACTIVACION, \n"+
							"P.FECHA_FIN_ACTIVACION \n"+
							"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
							"SIM_PRODUCTO P \n"+
							
							
							"WHERE PC.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND PC.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND PC.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
							"AND PC.NUM_CICLO = '" + sNumCiclo + "' \n"+
							"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
							"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
							"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n";
						PreparedStatement ps5 = this.conn.prepareStatement(sSql);
						ps5.execute();
						ResultSet rs5 = ps5.getResultSet();			
						
						if(rs5.next()){
							registro.addDefCampo("ID_PRODUCTO",rs5.getString("ID_PRODUCTO")== null ? "": rs5.getString("ID_PRODUCTO"));
							registro.addDefCampo("NUM_CICLO",rs5.getString("NUM_CICLO")== null ? "": rs5.getString("NUM_CICLO"));
							registro.addDefCampo("CVE_METODO",rs5.getString("CVE_METODO")== null ? "": rs5.getString("CVE_METODO"));
							registro.addDefCampo("APLICA_A",rs5.getString("APLICA_A")== null ? "": rs5.getString("APLICA_A"));
							registro.addDefCampo("ID_PERIODICIDAD_PRODUCTO",rs5.getString("ID_PERIODICIDAD")== null ? "": rs5.getString("ID_PERIODICIDAD"));
							registro.addDefCampo("PLAZO",rs5.getString("PLAZO")== null ? "": rs5.getString("PLAZO"));
							registro.addDefCampo("TIPO_TASA",rs5.getString("TIPO_TASA")== null ? "": rs5.getString("TIPO_TASA"));
							registro.addDefCampo("VALOR_TASA",rs5.getString("VALOR_TASA")== null ? "": rs5.getString("VALOR_TASA"));
							registro.addDefCampo("ID_PERIODICIDAD_TASA",rs5.getString("ID_PERIODICIDAD_TASA")== null ? "": rs5.getString("ID_PERIODICIDAD_TASA"));
							registro.addDefCampo("ID_TASA_REFERENCIA",rs5.getString("ID_TASA_REFERENCIA")== null ? "": rs5.getString("ID_TASA_REFERENCIA"));
							registro.addDefCampo("MONTO_MAXIMO",rs5.getString("MONTO_MAXIMO")== null ? "": rs5.getString("MONTO_MAXIMO"));
							registro.addDefCampo("PORC_FLUJO_CAJA",rs5.getString("PORC_FLUJO_CAJA")== null ? "": rs5.getString("PORC_FLUJO_CAJA"));
							registro.addDefCampo("ID_FORMA_DISTRIBUCION",rs5.getString("ID_FORMA_DISTRIBUCION")== null ? "": rs5.getString("ID_FORMA_DISTRIBUCION"));
						}
						
						
						String sIdPrestamo = "";
				
						//OBTENEMOS EL SEQUENCE
						sSql = "SELECT SQ01_SIM_PRESTAMO.nextval as ID_PRESTAMO FROM DUAL";
						PreparedStatement ps6 = this.conn.prepareStatement(sSql);
						ps6.execute();
						ResultSet rs6 = ps6.getResultSet();	
						
						if (rs6.next()){
							sIdPrestamo = rs6.getString("ID_PRESTAMO");
							registro.addDefCampo("PRESTAMO",sIdPrestamo);
						}
						
						sSql =  "INSERT INTO SIM_PRESTAMO ( "+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_PRESTAMO, \n" +
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
							
							"MONTO_MAXIMO, \n" +
							"PORC_FLUJO_CAJA, \n" +
							"ID_FORMA_DISTRIBUCION, \n" +
							"FECHA_SOLICITUD, \n" +
							"FECHA_ENTREGA, \n" +
							"FECHA_REAL, \n" +
							"ID_SUCURSAL, \n" +
							"ID_COMITE, \n" + 
							"CVE_ASESOR_FUNDADOR, \n" + 
							"CVE_ASESOR_CREDITO, \n" + 
							"FECHA_ASESOR, \n" + 
							"FECHA_ASESOR_SIST, \n" + 
							"ID_ETAPA_PRESTAMO, \n" +
							"B_ENTREGADO, \n" +
							"APLICA_A) \n" +
							
							" VALUES (" +
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							" " + sIdPrestamo +", \n" +
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
							
							"'" + (String)registro.getDefCampo("MONTO_MAXIMO") + "', \n" +
							"'" + (String)registro.getDefCampo("PORC_FLUJO_CAJA") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_FORMA_DISTRIBUCION") + "', \n" +
							"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_SOLICITUD") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
							"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
							//"TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY'), \n" +
							"TO_DATE('" + (String)registro.getDefCampo("FECHA_REAL") + "','DD/MM/YYYY'), \n" +
							"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_COMITE") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
							"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_ASESOR") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
							"SYSDATE, \n"+
							"'1', \n" +
							"'F', \n" +
							"'" + (String)registro.getDefCampo("APLICA_A") + "') \n" ;
							
						PreparedStatement ps7 = this.conn.prepareStatement(sSql);
						ps7.execute();
						ResultSet rs7 = ps7.getResultSet();
						
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
						
						PreparedStatement ps8 = this.conn.prepareStatement(sSql);
						ps8.execute();
						ResultSet rs8 = ps8.getResultSet();
						if(rs8.next()){	
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
							
							PreparedStatement ps9 = this.conn.prepareStatement(sSql);
							ps9.execute();
							ResultSet rs9 = ps9.getResultSet();
						}
						
						sSql =  "INSERT INTO SIM_CLIENTE_MONTO ( \n" +
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_PRESTAMO, \n" +
							"ID_CLIENTE, \n" +
							"ID_GRUPO, \n" +
							"ID_PRODUCTO, \n" +
							"NUM_CICLO) \n" +
							" VALUES (" +
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"'" + sIdPrestamo + "', \n" +
							"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
							"'" + (String)registro.getDefCampo("NUM_CICLO") + "') \n" ;
						
						PreparedStatement ps10 = this.conn.prepareStatement(sSql);
						ps10.execute();
						ResultSet rs10 = ps10.getResultSet();
					
						//Esto lo podemos quitar porque ya trae del registro el comite ¿no es así?
						sSql =  "SELECT \n"+
								"CVE_GPO_EMPRESA, \n"+
								"CVE_EMPRESA, \n"+
								"ID_COMITE \n"+
							"FROM SIM_PRESTAMO \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
						PreparedStatement ps11 = this.conn.prepareStatement(sSql);
						ps11.execute();
						ResultSet rs11 = ps11.getResultSet();
						if(rs11.next()){
							registro.addDefCampo("ID_COMITE",rs11.getString("ID_COMITE")== null ? "": rs11.getString("ID_COMITE"));
						}				
						
						sSql = "INSERT INTO SIM_COMITE_PRESTAMO ( \n"+
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_COMITE, \n" +
								"ID_PRESTAMO, \n" +
								"ID_CLIENTE) \n"+
								" VALUES (" +
								"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_COMITE") + "', \n" +
								"'" + sIdPrestamo + "', \n" +
								"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "') \n" ;
					
						PreparedStatement ps12 = this.conn.prepareStatement(sSql);
						ps12.execute();
						ResultSet rs12 = ps12.getResultSet();
						
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
								
						PreparedStatement ps13 = this.conn.prepareStatement(sSql);
						ps13.execute();
						ResultSet rs13 = ps13.getResultSet();
						while (rs13.next()){
							registro.addDefCampo("ID_CARGO_COMISION",rs13.getString("ID_CARGO_COMISION")== null ? "": rs13.getString("ID_CARGO_COMISION"));
							registro.addDefCampo("ID_FORMA_APLICACION",rs13.getString("ID_FORMA_APLICACION")== null ? "": rs13.getString("ID_FORMA_APLICACION"));
							registro.addDefCampo("CARGO_INICIAL",rs13.getString("CARGO_INICIAL")== null ? "": rs13.getString("CARGO_INICIAL"));
							registro.addDefCampo("PORCENTAJE_MONTO",rs13.getString("PORCENTAJE_MONTO")== null ? "": rs13.getString("PORCENTAJE_MONTO"));
							registro.addDefCampo("CANTIDAD_FIJA",rs13.getString("CANTIDAD_FIJA")== null ? "": rs13.getString("CANTIDAD_FIJA"));
							registro.addDefCampo("VALOR",rs13.getString("VALOR")== null ? "": rs13.getString("VALOR"));
							registro.addDefCampo("ID_UNIDAD",rs13.getString("ID_UNIDAD")== null ? "": rs13.getString("ID_UNIDAD"));
							registro.addDefCampo("ID_PERIODICIDAD",rs13.getString("ID_PERIODICIDAD")== null ? "": rs13.getString("ID_PERIODICIDAD"));
							
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
								
							PreparedStatement ps14 = this.conn.prepareStatement(sSql);
							ps14.execute();
							ResultSet rs14 = ps14.getResultSet();	
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
								"AND ID_FORMA_APLICACION = '1' \n";
					
						PreparedStatement ps15 = this.conn.prepareStatement(sSql);
						ps15.execute();
						ResultSet rs15 = ps15.getResultSet();	
						if (!rs15.next()){
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
							System.out.println("NO DIO UN CARGO INICIAL Y PARA QUE NO TRUENE SE LO AGREGO CON CERO PESOS"+sSql);
									PreparedStatement ps16 = this.conn.prepareStatement(sSql);
									ps16.execute();
									ResultSet rs16 = ps16.getResultSet();
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
								
						PreparedStatement ps17 = this.conn.prepareStatement(sSql);
						ps17.execute();
						ResultSet rs17 = ps17.getResultSet();	
						while (rs17.next()){
							registro.addDefCampo("ID_ACCESORIO",rs17.getString("ID_ACCESORIO")== null ? "": rs17.getString("ID_ACCESORIO"));
							registro.addDefCampo("ORDEN",rs17.getString("ORDEN")== null ? "": rs17.getString("ORDEN"));
									
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
						
							PreparedStatement ps18 = this.conn.prepareStatement(sSql);
							ps18.execute();
							ResultSet rs18 = ps18.getResultSet();			
						}					
						
						sSql =  "SELECT \n"+
							"CVE_GPO_EMPRESA, \n"+
							"CVE_EMPRESA, \n"+
							"ID_PRODUCTO, \n"+
							"ID_ACTIVIDAD_REQUISITO, \n"+
							"ID_ESTATUS_PRESTAMO, \n"+
							"ORDEN_ETAPA \n"+
							"FROM \n"+
							"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
								
						PreparedStatement ps19 = this.conn.prepareStatement(sSql);
						ps19.execute();
						ResultSet rs19 = ps19.getResultSet();	
						
						while (rs19.next()){
							registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs19.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs19.getString("ID_ACTIVIDAD_REQUISITO"));
							registro.addDefCampo("ID_ETAPA_PRESTAMO",rs19.getString("ID_ETAPA_PRESTAMO")== null ? "": rs19.getString("ID_ETAPA_PRESTAMO"));
							registro.addDefCampo("ORDEN_ETAPA",rs19.getString("ORDEN_ETAPA")== null ? "": rs19.getString("ORDEN_ETAPA"));
							
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
							
							PreparedStatement ps20 = this.conn.prepareStatement(sSql);
							ps20.execute();
							ResultSet rs18 = ps20.getResultSet();
						}
						
						sSql =  "SELECT DISTINCT \n"+
							"ID_ETAPA_PRESTAMO \n"+
							"FROM \n"+
							"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
							"AND ORDEN_ETAPA = '1' \n";
							
						PreparedStatement ps21 = this.conn.prepareStatement(sSql);
						ps21.execute();
						ResultSet rs21 = ps21.getResultSet();
						if (rs21.next()){
							registro.addDefCampo("ESTATUS_PRESTAMO",rs21.getString("ID_ETAPA_PRESTAMO")== null ? "": rs21.getString("ID_ETAPA_PRESTAMO"));
						}
						
						sSql =  " UPDATE SIM_PRESTAMO SET "+
							" ID_ETAPA_PRESTAMO 		='" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "' \n" +
							" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
							" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
					
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						PreparedStatement ps22 = this.conn.prepareStatement(sSql);
						ps22.execute();
						ResultSet rs22 = ps22.getResultSet();
							
						sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
							" FECHA_REGISTRO 		=SYSDATE, \n" +
							" ESTATUS	 		='Registrada' \n" +
							" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
							" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "' \n";
					
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						PreparedStatement ps23 = this.conn.prepareStatement(sSql);
						ps23.execute();
						ResultSet rs23 = ps23.getResultSet();
						
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
								
						PreparedStatement ps24 = this.conn.prepareStatement(sSql);
						ps24.execute();
						ResultSet rs24 = ps24.getResultSet();
						
						while (rs24.next()){
							registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs24.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs24.getString("ID_ACTIVIDAD_REQUISITO"));
							registro.addDefCampo("ID_ETAPA_PRESTAMO",rs24.getString("ID_ETAPA_PRESTAMO")== null ? "": rs24.getString("ID_ETAPA_PRESTAMO"));
							registro.addDefCampo("ORDEN_ETAPA",rs24.getString("ORDEN_ETAPA")== null ? "": rs24.getString("ORDEN_ETAPA"));
							registro.addDefCampo("FECHA_REGISTRO",rs24.getString("FECHA_REGISTRO")== null ? "": rs24.getString("FECHA_REGISTRO"));
							registro.addDefCampo("ESTATUS",rs24.getString("ESTATUS")== null ? "": rs24.getString("ESTATUS"));
							
							resultadoCatalogoAltaActividadRequisito = simPrestamoAltaActividadRequisitoDAO.alta(registro);
							
							
						}
						
						sSql =  " INSERT INTO SIM_PRESTAMO_ESTATUS_HISTORICO \n" +
								" (CVE_GPO_EMPRESA, \n" +
								" CVE_EMPRESA, \n" +
								" ID_PRESTAMO, \n" +
								" ID_ETAPA_PRESTAMO, \n" +
								" CVE_USUARIO, \n" +
								" FECHA) \n" +
								" VALUES \n" +
								"('" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								" '" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + sIdPrestamo + "', \n" +
								" '" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "', \n" +
								" '" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
								" SYSDATE) \n" ;
						
						PreparedStatement ps25 = this.conn.prepareStatement(sSql);
						ps25.execute();
						ResultSet rs25 = ps25.getResultSet();	
				
						sSql =  " DELETE FROM SIM_PRESTAMO "+
							" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
							" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
								
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						PreparedStatement ps26 = this.conn.prepareStatement(sSql);
						ps26.execute();
						ResultSet rs26 = ps26.getResultSet();		
						
						//Obtiene la cuenta crédito (id_cuenta).
						sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
						
						PreparedStatement ps27 = this.conn.prepareStatement(sSql);
						ps27.execute();
						ResultSet rs27 = ps27.getResultSet();	
						if (rs27.next()){
							registro.addDefCampo("ID_CUENTA",rs27.getString("ID_CUENTA"));
							//A) Agrega la cuenta crédito (id_cuenta) al integrante en la tabla PFIN_CUENTA.
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
							
							PreparedStatement ps28 = this.conn.prepareStatement(sSql);
							ps28.execute();
							ResultSet rs28 = ps28.getResultSet();
							//B) Agrega la cuenta crédito (id_cuenta) al credito en la tabla SIM_PRESTAMO.
							sSql =  " UPDATE SIM_PRESTAMO SET "+
								" ID_CUENTA			='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
								" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
							
							//VERIFICA SI DIO DE ALTA EL REGISTRO
							PreparedStatement ps29 = this.conn.prepareStatement(sSql);
							ps29.execute();
							ResultSet rs29 = ps29.getResultSet();
						}
						//Obtiene la cuenta vista (id_cuenta_referencia).
						sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
						
						PreparedStatement ps31 = this.conn.prepareStatement(sSql);
						ps31.execute();
						ResultSet rs31 = ps31.getResultSet();	
						if (rs31.next()){
							registro.addDefCampo("ID_CUENTA",rs31.getString("ID_CUENTA"));
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
						
							PreparedStatement ps32 = this.conn.prepareStatement(sSql);
							ps32.execute();
							ResultSet rs32 = ps32.getResultSet();
							//B) Agrega la cuenta vista (id_cuenta_referencia) al credito en la tabla SIM_PRESTAMO.
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
							
						PreparedStatement ps35 = this.conn.prepareStatement(sSql);
						ps35.execute();
						ResultSet rs35 = ps35.getResultSet();
						
						while (rs35.next()){
							registro.addDefCampo("ID_ARCHIVO_FLUJO",rs35.getString("ID_ARCHIVO_FLUJO")== null ? "": rs35.getString("ID_ARCHIVO_FLUJO"));
							registro.addDefCampo("NOM_ARCHIVO",rs35.getString("NOM_ARCHIVO")== null ? "": rs35.getString("NOM_ARCHIVO"));
							
							sSql = "INSERT INTO SIM_PRESTAMO_FLUJO_EFECTIVO ( \n"+
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
								
							PreparedStatement ps36 = this.conn.prepareStatement(sSql);
							ps36.execute();
							ResultSet rs36 = ps36.getResultSet();
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
									
							PreparedStatement ps37 = this.conn.prepareStatement(sSql);
							ps37.execute();
							ResultSet rs37 = ps37.getResultSet();
							while (rs37.next()){
								registro.addDefCampo("ID_DOCUMENTO",rs37.getString("ID_DOCUMENTO")== null ? "": rs37.getString("ID_DOCUMENTO"));
										
								sSql = "INSERT INTO SIM_PRESTAMO_DOCUMENTACION ( \n"+
									"CVE_GPO_EMPRESA, \n" +
									"CVE_EMPRESA, \n" +
									"ID_PRESTAMO, \n" +
									"ID_DOCUMENTO) \n" +
									" VALUES (" +
									"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
									"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
									"'" + sIdPrestamo + "', \n" +
									"'" + (String)registro.getDefCampo("ID_DOCUMENTO") + "') \n" ;
							
								PreparedStatement ps38 = this.conn.prepareStatement(sSql);
								ps38.execute();
								ResultSet rs38 = ps38.getResultSet();	
							}		
						
					
					}else{
						//como no tiene siguiente le asigan el ultimo conocido.
						
						sSql = "SELECT \n"+
							"PC.CVE_GPO_EMPRESA, \n"+
							"PC.CVE_EMPRESA, \n"+
							"PC.ID_PRODUCTO, \n"+
							"P.NOM_PRODUCTO, \n"+
							"P.APLICA_A, \n"+
							"P.ID_PERIODICIDAD, \n"+
							"P.CVE_METODO, \n"+
							"PC.NUM_CICLO, \n"+
							"PC.ID_FORMA_DISTRIBUCION, \n"+
							"PC.PLAZO, \n"+
							"PC.TIPO_TASA, \n"+
							"PC.VALOR_TASA, \n"+
							"PC.ID_PERIODICIDAD_TASA, \n"+
							"PC.ID_TASA_REFERENCIA, \n"+
							"PC.MONTO_MAXIMO, \n"+
							"PC.PORC_FLUJO_CAJA, \n"+
							"PC.ID_FORMA_DISTRIBUCION, \n"+
							"P.FECHA_INICIO_ACTIVACION, \n"+
							"P.FECHA_FIN_ACTIVACION \n"+
							"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
							"SIM_PRODUCTO P \n"+
							"WHERE PC.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND PC.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND PC.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
							"AND PC.NUM_CICLO = (SELECT \n"+
								"MAX(P.NUM_CICLO) NUM_CICLO_PROX \n"+
								"FROM \n"+
								"SIM_PRESTAMO P \n"+
								"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND P.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
								"AND P.ID_CLIENTE = '" + sIdIntegrante + "' \n"+
								") \n"+
							"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
							"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
							"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n";
						PreparedStatement ps5 = this.conn.prepareStatement(sSql);
						ps5.execute();
						ResultSet rs5 = ps5.getResultSet();			
						
						
						if(rs5.next()){
							registro.addDefCampo("ID_PRODUCTO",rs5.getString("ID_PRODUCTO")== null ? "": rs5.getString("ID_PRODUCTO"));
							registro.addDefCampo("NUM_CICLO",rs5.getString("NUM_CICLO")== null ? "": rs5.getString("NUM_CICLO"));
							registro.addDefCampo("CVE_METODO",rs5.getString("CVE_METODO")== null ? "": rs5.getString("CVE_METODO"));
							registro.addDefCampo("APLICA_A",rs5.getString("APLICA_A")== null ? "": rs5.getString("APLICA_A"));
							registro.addDefCampo("ID_PERIODICIDAD_PRODUCTO",rs5.getString("ID_PERIODICIDAD")== null ? "": rs5.getString("ID_PERIODICIDAD"));
							registro.addDefCampo("PLAZO",rs5.getString("PLAZO")== null ? "": rs5.getString("PLAZO"));
							registro.addDefCampo("TIPO_TASA",rs5.getString("TIPO_TASA")== null ? "": rs5.getString("TIPO_TASA"));
							registro.addDefCampo("VALOR_TASA",rs5.getString("VALOR_TASA")== null ? "": rs5.getString("VALOR_TASA"));
							registro.addDefCampo("ID_PERIODICIDAD_TASA",rs5.getString("ID_PERIODICIDAD_TASA")== null ? "": rs5.getString("ID_PERIODICIDAD_TASA"));
							registro.addDefCampo("ID_TASA_REFERENCIA",rs5.getString("ID_TASA_REFERENCIA")== null ? "": rs5.getString("ID_TASA_REFERENCIA"));
							registro.addDefCampo("MONTO_MAXIMO",rs5.getString("MONTO_MAXIMO")== null ? "": rs5.getString("MONTO_MAXIMO"));
							registro.addDefCampo("PORC_FLUJO_CAJA",rs5.getString("PORC_FLUJO_CAJA")== null ? "": rs5.getString("PORC_FLUJO_CAJA"));
							registro.addDefCampo("ID_FORMA_DISTRIBUCION",rs5.getString("ID_FORMA_DISTRIBUCION")== null ? "": rs5.getString("ID_FORMA_DISTRIBUCION"));
						}
						
						
						String sIdPrestamo = "";
				
						//OBTENEMOS EL SEQUENCE
						sSql = "SELECT SQ01_SIM_PRESTAMO.nextval as ID_PRESTAMO FROM DUAL";
						PreparedStatement ps6 = this.conn.prepareStatement(sSql);
						ps6.execute();
						ResultSet rs6 = ps6.getResultSet();	
						
						if (rs6.next()){
							sIdPrestamo = rs6.getString("ID_PRESTAMO");
							registro.addDefCampo("PRESTAMO",sIdPrestamo);
						}
						
						sSql =  "INSERT INTO SIM_PRESTAMO ( "+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_PRESTAMO, \n" +
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
							
							"MONTO_MAXIMO, \n" +
							"PORC_FLUJO_CAJA, \n" +
							"ID_FORMA_DISTRIBUCION, \n" +
							"FECHA_SOLICITUD, \n" +
							"FECHA_ENTREGA, \n" +
							"FECHA_REAL, \n" +
							"ID_SUCURSAL, \n" +
							"ID_COMITE, \n" + 
							"CVE_ASESOR_FUNDADOR, \n" + 
							"CVE_ASESOR_CREDITO, \n" + 
							"FECHA_ASESOR, \n" + 
							"FECHA_ASESOR_SIST, \n" + 
							"ID_ETAPA_PRESTAMO, \n" +
							"B_ENTREGADO, \n" +
							"APLICA_A) \n" +
							
							" VALUES (" +
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							" " + sIdPrestamo +", \n" +
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
							
							"'" + (String)registro.getDefCampo("MONTO_MAXIMO") + "', \n" +
							"'" + (String)registro.getDefCampo("PORC_FLUJO_CAJA") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_FORMA_DISTRIBUCION") + "', \n" +
							//"TO_DATE('" + (String)registro.getDefCampo("FECHA_SOLICITUD") + "','DD/MM/YYYY'), \n" +
							"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_SOLICITUD") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
							"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
							//"TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY'), \n" +
							"TO_DATE('" + (String)registro.getDefCampo("FECHA_REAL") + "','DD/MM/YYYY'), \n" +
							
							"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_COMITE") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
							"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_ASESOR") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
							"SYSDATE, \n"+
							"'1', \n" +
							"'F', \n" +
							"'" + (String)registro.getDefCampo("APLICA_A") + "') \n" ;
						
						PreparedStatement ps7 = this.conn.prepareStatement(sSql);
						ps7.execute();
						ResultSet rs7 = ps7.getResultSet();
						
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
						
						PreparedStatement ps8 = this.conn.prepareStatement(sSql);
						ps8.execute();
						ResultSet rs8 = ps8.getResultSet();
						if(rs8.next()){	
						
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
							
							PreparedStatement ps9 = this.conn.prepareStatement(sSql);
							ps9.execute();
							ResultSet rs9 = ps9.getResultSet();
						}
						
						sSql =  "INSERT INTO SIM_CLIENTE_MONTO ( \n" +
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_PRESTAMO, \n" +
								"ID_CLIENTE, \n" +
								"ID_GRUPO, \n" +
								"ID_PRODUCTO, \n" +
								"NUM_CICLO) \n" +
								" VALUES (" +
								"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + sIdPrestamo + "', \n" +
								"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
								"'" + (String)registro.getDefCampo("NUM_CICLO") + "') \n" ;
						
						PreparedStatement ps10 = this.conn.prepareStatement(sSql);
						ps10.execute();
						ResultSet rs10 = ps10.getResultSet();
						
						sSql =  "SELECT \n"+
								"CVE_GPO_EMPRESA, \n"+
								"CVE_EMPRESA, \n"+
								"ID_COMITE \n"+
							"FROM SIM_PRESTAMO \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
						PreparedStatement ps11 = this.conn.prepareStatement(sSql);
						ps11.execute();
						ResultSet rs11 = ps11.getResultSet();
						if(rs11.next()){
							registro.addDefCampo("ID_COMITE",rs11.getString("ID_COMITE")== null ? "": rs11.getString("ID_COMITE"));
						}				
						
						sSql = "INSERT INTO SIM_COMITE_PRESTAMO ( \n"+
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_COMITE, \n" +
								"ID_PRESTAMO, \n" +
								"ID_CLIENTE) \n"+
								" VALUES (" +
								"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("ID_COMITE") + "', \n" +
								"'" + sIdPrestamo + "', \n" +
								"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "') \n" ;
						
						PreparedStatement ps12 = this.conn.prepareStatement(sSql);
						ps12.execute();
						ResultSet rs12 = ps12.getResultSet();
						
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
								
						PreparedStatement ps13 = this.conn.prepareStatement(sSql);
						ps13.execute();
						ResultSet rs13 = ps13.getResultSet();
						while (rs13.next()){
							registro.addDefCampo("ID_CARGO_COMISION",rs13.getString("ID_CARGO_COMISION")== null ? "": rs13.getString("ID_CARGO_COMISION"));
							registro.addDefCampo("ID_FORMA_APLICACION",rs13.getString("ID_FORMA_APLICACION")== null ? "": rs13.getString("ID_FORMA_APLICACION"));
							registro.addDefCampo("CARGO_INICIAL",rs13.getString("CARGO_INICIAL")== null ? "": rs13.getString("CARGO_INICIAL"));
							registro.addDefCampo("PORCENTAJE_MONTO",rs13.getString("PORCENTAJE_MONTO")== null ? "": rs13.getString("PORCENTAJE_MONTO"));
							registro.addDefCampo("CANTIDAD_FIJA",rs13.getString("CANTIDAD_FIJA")== null ? "": rs13.getString("CANTIDAD_FIJA"));
							registro.addDefCampo("VALOR",rs13.getString("VALOR")== null ? "": rs13.getString("VALOR"));
							registro.addDefCampo("ID_UNIDAD",rs13.getString("ID_UNIDAD")== null ? "": rs13.getString("ID_UNIDAD"));
							registro.addDefCampo("ID_PERIODICIDAD",rs13.getString("ID_PERIODICIDAD")== null ? "": rs13.getString("ID_PERIODICIDAD"));
							
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
							
							PreparedStatement ps14 = this.conn.prepareStatement(sSql);
							ps14.execute();
							ResultSet rs14 = ps14.getResultSet();	
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
								"AND ID_FORMA_APLICACION = '1' \n";
					
						PreparedStatement ps15 = this.conn.prepareStatement(sSql);
						ps15.execute();
						ResultSet rs15 = ps15.getResultSet();	
						if (!rs15.next()){
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
							System.out.println("NO DIO UN CARGO INICIAL Y PARA QUE NO TRUENE SE LO AGREGO CON CERO PESOS"+sSql);
									PreparedStatement ps16 = this.conn.prepareStatement(sSql);
									ps16.execute();
									ResultSet rs16 = ps16.getResultSet();
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
							
					PreparedStatement ps17 = this.conn.prepareStatement(sSql);
					ps17.execute();
					ResultSet rs17 = ps17.getResultSet();
					while (rs17.next()){
						registro.addDefCampo("ID_DOCUMENTO",rs17.getString("ID_DOCUMENTO")== null ? "": rs17.getString("ID_DOCUMENTO"));
								
						sSql = "INSERT INTO SIM_PRESTAMO_DOCUMENTACION ( \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_PRESTAMO, \n" +
							"ID_DOCUMENTO) \n" +
							" VALUES (" +
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"'" + sIdPrestamo + "', \n" +
							"'" + (String)registro.getDefCampo("ID_DOCUMENTO") + "') \n" ;
					
						PreparedStatement ps18 = this.conn.prepareStatement(sSql);
						ps18.execute();
						ResultSet rs18 = ps18.getResultSet();	
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
							
						PreparedStatement ps19 = this.conn.prepareStatement(sSql);
						ps19.execute();
						ResultSet rs19 = ps19.getResultSet();	
						while (rs19.next()){
							registro.addDefCampo("ID_ACCESORIO",rs19.getString("ID_ACCESORIO")== null ? "": rs19.getString("ID_ACCESORIO"));
							registro.addDefCampo("ORDEN",rs19.getString("ORDEN")== null ? "": rs19.getString("ORDEN"));
									
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
						
							PreparedStatement ps20 = this.conn.prepareStatement(sSql);
							ps20.execute();
							ResultSet rs20 = ps20.getResultSet();			
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
								
						PreparedStatement ps21 = this.conn.prepareStatement(sSql);
						ps21.execute();
						ResultSet rs21 = ps21.getResultSet();	
						
						while (rs21.next()){
							registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs21.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs21.getString("ID_ACTIVIDAD_REQUISITO"));
							registro.addDefCampo("ID_ETAPA_PRESTAMO",rs21.getString("ID_ETAPA_PRESTAMO")== null ? "": rs21.getString("ID_ETAPA_PRESTAMO"));
							registro.addDefCampo("ORDEN_ETAPA",rs21.getString("ORDEN_ETAPA")== null ? "": rs21.getString("ORDEN_ETAPA"));
							
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
								
							PreparedStatement ps22 = this.conn.prepareStatement(sSql);
							ps22.execute();
							ResultSet rs22 = ps22.getResultSet();
						}
						
						sSql =  "SELECT DISTINCT \n"+
							"ID_ETAPA_PRESTAMO \n"+
							"FROM \n"+
							"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
							"AND ORDEN_ETAPA = '1' \n";
							
						PreparedStatement ps23 = this.conn.prepareStatement(sSql);
						ps23.execute();
						ResultSet rs23 = ps23.getResultSet();
						if (rs23.next()){
							registro.addDefCampo("ESTATUS_PRESTAMO",rs23.getString("ID_ETAPA_PRESTAMO")== null ? "": rs23.getString("ID_ETAPA_PRESTAMO"));
						}
						
						sSql =  " UPDATE SIM_PRESTAMO SET "+
							" ID_ETAPA_PRESTAMO 		='" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "' \n" +
							" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
							" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						PreparedStatement ps24 = this.conn.prepareStatement(sSql);
						ps24.execute();
						ResultSet rs24 = ps24.getResultSet();
							
						sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
							" FECHA_REGISTRO 		=SYSDATE, \n" +
							" ESTATUS	 		='Registrada' \n" +
							" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
							" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "' \n";
					
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						PreparedStatement ps25 = this.conn.prepareStatement(sSql);
						ps25.execute();
						ResultSet rs25 = ps25.getResultSet();
						
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
								
						PreparedStatement ps26 = this.conn.prepareStatement(sSql);
						ps26.execute();
						ResultSet rs26 = ps26.getResultSet();	
						
						while (rs26.next()){
							registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs26.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs26.getString("ID_ACTIVIDAD_REQUISITO"));
							registro.addDefCampo("ID_ETAPA_PRESTAMO",rs26.getString("ID_ETAPA_PRESTAMO")== null ? "": rs26.getString("ID_ETAPA_PRESTAMO"));
							registro.addDefCampo("ORDEN_ETAPA",rs26.getString("ORDEN_ETAPA")== null ? "": rs26.getString("ORDEN_ETAPA"));
							registro.addDefCampo("FECHA_REGISTRO",rs26.getString("FECHA_REGISTRO")== null ? "": rs26.getString("FECHA_REGISTRO"));
							registro.addDefCampo("ESTATUS",rs26.getString("ESTATUS")== null ? "": rs26.getString("ESTATUS"));
							
							resultadoCatalogoAltaActividadRequisito = simPrestamoAltaActividadRequisitoDAO.alta(registro);
							
						}
						
						sSql =  " INSERT INTO SIM_PRESTAMO_ESTATUS_HISTORICO \n" +
								" (CVE_GPO_EMPRESA, \n" +
								" CVE_EMPRESA, \n" +
								" ID_PRESTAMO, \n" +
								" ID_ETAPA_PRESTAMO, \n" +
								" CVE_USUARIO, \n" +
								" FECHA) \n" +
								" VALUES \n" +
								"('" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								" '" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + sIdPrestamo + "', \n" +
								" '" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "', \n" +
								" '" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
								" SYSDATE) \n" ;
						
						PreparedStatement ps27 = this.conn.prepareStatement(sSql);
						ps27.execute();
						ResultSet rs27 = ps27.getResultSet();	
				
						sSql =  " DELETE FROM SIM_PRESTAMO "+
							" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
							" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
							
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						PreparedStatement ps28 = this.conn.prepareStatement(sSql);
						ps28.execute();
						ResultSet rs28 = ps28.getResultSet();		
						//Obtiene la cuenta crédito (id_cuenta).
						sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
					
						PreparedStatement ps29 = this.conn.prepareStatement(sSql);
						ps29.execute();
						ResultSet rs29 = ps29.getResultSet();	
						if (rs29.next()){
							registro.addDefCampo("ID_CUENTA",rs29.getString("ID_CUENTA"));
							//A) Agrega la cuenta crédito (id_cuenta) al integrante en la tabla PFIN_CUENTA.
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
							
							PreparedStatement ps30 = this.conn.prepareStatement(sSql);
							ps30.execute();
							ResultSet rs30 = ps30.getResultSet();
							//B) Agrega la cuenta crédito (id_cuenta) al credito en la tabla SIM_PRESTAMO.
							sSql =  " UPDATE SIM_PRESTAMO SET "+
								" ID_CUENTA			='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
								" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
							
							//VERIFICA SI DIO DE ALTA EL REGISTRO
							PreparedStatement ps31 = this.conn.prepareStatement(sSql);
							ps31.execute();
							ResultSet rs31 = ps31.getResultSet();
						}
						//Obtiene la cuenta vista (id_cuenta_referencia).
						sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
						
						PreparedStatement ps33 = this.conn.prepareStatement(sSql);
						ps33.execute();
						ResultSet rs33 = ps33.getResultSet();	
						if (rs33.next()){
							registro.addDefCampo("ID_CUENTA",rs33.getString("ID_CUENTA"));
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
							
							PreparedStatement ps34 = this.conn.prepareStatement(sSql);
							ps34.execute();
							ResultSet rs34 = ps34.getResultSet();
							//B) Agrega la cuenta vista (id_cuenta_referencia) al credito en la tabla SIM_PRESTAMO.
							sSql =  " UPDATE SIM_PRESTAMO SET "+
								" ID_CUENTA_REFERENCIA 		='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
								" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
							
							//VERIFICA SI DIO DE ALTA EL REGISTRO
							PreparedStatement ps35 = this.conn.prepareStatement(sSql);
							ps35.execute();
							ResultSet rs35 = ps35.getResultSet();
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
							
						PreparedStatement ps37 = this.conn.prepareStatement(sSql);
						ps37.execute();
						ResultSet rs37 = ps37.getResultSet();
						
						while (rs37.next()){
							registro.addDefCampo("ID_ARCHIVO_FLUJO",rs37.getString("ID_ARCHIVO_FLUJO")== null ? "": rs37.getString("ID_ARCHIVO_FLUJO"));
							registro.addDefCampo("NOM_ARCHIVO",rs37.getString("NOM_ARCHIVO")== null ? "": rs37.getString("NOM_ARCHIVO"));
							
							sSql = "INSERT INTO SIM_PRESTAMO_FLUJO_EFECTIVO ( \n"+
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
								
							PreparedStatement ps38 = this.conn.prepareStatement(sSql);
							ps38.execute();
							ResultSet rs38 = ps38.getResultSet();
						}
						
						
					}
				}else{
					
					
					
					//Ingresa el producto-ciclo 1.
					
					//Obtiene las características de producto-ciclo 1.
					sSql = "SELECT \n"+
						"PC.CVE_GPO_EMPRESA, \n"+
						"PC.CVE_EMPRESA, \n"+
						"PC.ID_PRODUCTO, \n"+
						"P.NOM_PRODUCTO, \n"+
						"P.APLICA_A, \n"+
						"P.ID_PERIODICIDAD, \n"+
						"P.CVE_METODO, \n"+
						"PC.NUM_CICLO, \n"+
						"PC.ID_FORMA_DISTRIBUCION, \n"+
						"PC.PLAZO, \n"+
						"PC.TIPO_TASA, \n"+
						"PC.VALOR_TASA, \n"+
						"PC.ID_PERIODICIDAD_TASA, \n"+
						"PC.ID_TASA_REFERENCIA, \n"+
						"PC.MONTO_MAXIMO, \n"+
						"PC.PORC_FLUJO_CAJA, \n"+
						"PC.ID_FORMA_DISTRIBUCION, \n"+
						"P.FECHA_INICIO_ACTIVACION, \n"+
						"P.FECHA_FIN_ACTIVACION \n"+
						"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
						"SIM_PRODUCTO P \n"+
						"WHERE PC.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND PC.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND PC.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
						"AND PC.NUM_CICLO = '1' \n"+ 
						"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n";
					
					PreparedStatement ps3 = this.conn.prepareStatement(sSql);
					ps3.execute();
					ResultSet rs3 = ps3.getResultSet();
					if(rs3.next()){
						registro.addDefCampo("ID_PRODUCTO",rs3.getString("ID_PRODUCTO")== null ? "": rs3.getString("ID_PRODUCTO"));
						registro.addDefCampo("NUM_CICLO",rs3.getString("NUM_CICLO")== null ? "": rs3.getString("NUM_CICLO"));
						registro.addDefCampo("CVE_METODO",rs3.getString("CVE_METODO")== null ? "": rs3.getString("CVE_METODO"));
						registro.addDefCampo("APLICA_A",rs3.getString("APLICA_A")== null ? "": rs3.getString("APLICA_A"));
						registro.addDefCampo("ID_PERIODICIDAD_PRODUCTO",rs3.getString("ID_PERIODICIDAD")== null ? "": rs3.getString("ID_PERIODICIDAD"));
						registro.addDefCampo("PLAZO",rs3.getString("PLAZO")== null ? "": rs3.getString("PLAZO"));
						registro.addDefCampo("TIPO_TASA",rs3.getString("TIPO_TASA")== null ? "": rs3.getString("TIPO_TASA"));
						registro.addDefCampo("VALOR_TASA",rs3.getString("VALOR_TASA")== null ? "": rs3.getString("VALOR_TASA"));
						registro.addDefCampo("ID_PERIODICIDAD_TASA",rs3.getString("ID_PERIODICIDAD_TASA")== null ? "": rs3.getString("ID_PERIODICIDAD_TASA"));
						registro.addDefCampo("ID_TASA_REFERENCIA",rs3.getString("ID_TASA_REFERENCIA")== null ? "": rs3.getString("ID_TASA_REFERENCIA"));
						registro.addDefCampo("MONTO_MAXIMO",rs3.getString("MONTO_MAXIMO")== null ? "": rs3.getString("MONTO_MAXIMO"));
						registro.addDefCampo("PORC_FLUJO_CAJA",rs3.getString("PORC_FLUJO_CAJA")== null ? "": rs3.getString("PORC_FLUJO_CAJA"));
						registro.addDefCampo("ID_FORMA_DISTRIBUCION",rs3.getString("ID_FORMA_DISTRIBUCION")== null ? "": rs3.getString("ID_FORMA_DISTRIBUCION"));
					}
					
					String sIdPrestamo = "";
					//OBTENEMOS EL SEQUENCE
					sSql = "SELECT SQ01_SIM_PRESTAMO.nextval as ID_PRESTAMO FROM DUAL";
					PreparedStatement ps4 = this.conn.prepareStatement(sSql);
					ps4.execute();
					ResultSet rs4 = ps4.getResultSet();	
					
					if (rs4.next()){
						sIdPrestamo = rs4.getString("ID_PRESTAMO");
						registro.addDefCampo("PRESTAMO",sIdPrestamo);
					}
					
					sSql =  "INSERT INTO SIM_PRESTAMO ( "+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_PRESTAMO, \n" +
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
						
						"MONTO_MAXIMO, \n" +
						"PORC_FLUJO_CAJA, \n" +
						"ID_FORMA_DISTRIBUCION, \n" +
						"FECHA_SOLICITUD, \n" +
						"FECHA_ENTREGA, \n" +
						"ID_SUCURSAL, \n" +
						"ID_COMITE, \n" + 
						"CVE_ASESOR_FUNDADOR, \n" + 
						"CVE_ASESOR_CREDITO, \n" + 
						"FECHA_ASESOR, \n" + 
						"FECHA_ASESOR_SIST, \n" + 
						"ID_ETAPA_PRESTAMO, \n" +
						"B_ENTREGADO, \n" +
						"APLICA_A) \n" +
						
						" VALUES (" +
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						" " + sIdPrestamo +", \n" +
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
						
						"'" + (String)registro.getDefCampo("MONTO_MAXIMO") + "', \n" +
						"'" + (String)registro.getDefCampo("PORC_FLUJO_CAJA") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_FORMA_DISTRIBUCION") + "', \n" +
						"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_SOLICITUD") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
						"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
						//"TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY'), \n" +
						//"TO_DATE('" + (String)registro.getDefCampo("FECHA_REAL") + "','DD/MM/YYYY'), \n" +
						
						//"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
						"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_COMITE") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
						"TO_DATE('" + (String)registro.getDefCampo("FECHA_ASESOR") + "','DD/MM/YYYY'), \n" +
						"SYSDATE, \n"+
						"'1', \n" +
						"'F', \n" +
						"'" + (String)registro.getDefCampo("APLICA_A") + "') \n" ;
					
					PreparedStatement ps5 = this.conn.prepareStatement(sSql);
					ps5.execute();
					ResultSet rs5 = ps5.getResultSet();
					
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
					
					PreparedStatement ps6 = this.conn.prepareStatement(sSql);
					ps6.execute();
					ResultSet rs6 = ps6.getResultSet();
					if(rs6.next()){	
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
							
						PreparedStatement ps7 = this.conn.prepareStatement(sSql);
						ps7.execute();
						ResultSet rs7 = ps7.getResultSet();
					}
					
					sSql =  "INSERT INTO SIM_CLIENTE_MONTO ( \n" +
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_PRESTAMO, \n" +
							"ID_CLIENTE, \n" +
							"ID_GRUPO, \n" +
							"ID_PRODUCTO, \n" +
							"NUM_CICLO) \n" +
							" VALUES (" +
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"'" + sIdPrestamo + "', \n" +
							"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
							"'" + (String)registro.getDefCampo("NUM_CICLO") + "') \n" ;
					PreparedStatement ps8 = this.conn.prepareStatement(sSql);
					ps8.execute();
					ResultSet rs8 = ps8.getResultSet();
					
					sSql =  "SELECT \n"+
							"CVE_GPO_EMPRESA, \n"+
							"CVE_EMPRESA, \n"+
							"ID_COMITE \n"+
						"FROM SIM_PRESTAMO \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
					PreparedStatement ps9 = this.conn.prepareStatement(sSql);
					ps9.execute();
					ResultSet rs9 = ps9.getResultSet();
					if(rs9.next()){
						registro.addDefCampo("ID_COMITE",rs9.getString("ID_COMITE")== null ? "": rs9.getString("ID_COMITE"));
					}				
					
					sSql = "INSERT INTO SIM_COMITE_PRESTAMO ( \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_COMITE, \n" +
							"ID_PRESTAMO, \n" +
							"ID_CLIENTE) \n"+
							" VALUES (" +
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_COMITE") + "', \n" +
							"'" + sIdPrestamo + "', \n" +
							"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "') \n" ;
					PreparedStatement ps10 = this.conn.prepareStatement(sSql);
					ps10.execute();
					ResultSet rs10 = ps10.getResultSet();
					
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
							
					PreparedStatement ps11 = this.conn.prepareStatement(sSql);
					ps11.execute();
					ResultSet rs11 = ps11.getResultSet();
					while (rs11.next()){
						registro.addDefCampo("ID_CARGO_COMISION",rs11.getString("ID_CARGO_COMISION")== null ? "": rs11.getString("ID_CARGO_COMISION"));
						registro.addDefCampo("ID_FORMA_APLICACION",rs11.getString("ID_FORMA_APLICACION")== null ? "": rs11.getString("ID_FORMA_APLICACION"));
						registro.addDefCampo("CARGO_INICIAL",rs11.getString("CARGO_INICIAL")== null ? "": rs11.getString("CARGO_INICIAL"));
						registro.addDefCampo("PORCENTAJE_MONTO",rs11.getString("PORCENTAJE_MONTO")== null ? "": rs11.getString("PORCENTAJE_MONTO"));
						registro.addDefCampo("CANTIDAD_FIJA",rs11.getString("CANTIDAD_FIJA")== null ? "": rs11.getString("CANTIDAD_FIJA"));
						registro.addDefCampo("VALOR",rs11.getString("VALOR")== null ? "": rs11.getString("VALOR"));
						registro.addDefCampo("ID_UNIDAD",rs11.getString("ID_UNIDAD")== null ? "": rs11.getString("ID_UNIDAD"));
						registro.addDefCampo("ID_PERIODICIDAD",rs11.getString("ID_PERIODICIDAD")== null ? "": rs11.getString("ID_PERIODICIDAD"));
						
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
						
						PreparedStatement ps12 = this.conn.prepareStatement(sSql);
						ps12.execute();
						ResultSet rs12 = ps12.getResultSet();	
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
							"AND ID_FORMA_APLICACION = '1' \n";
						
						PreparedStatement ps13 = this.conn.prepareStatement(sSql);
						ps13.execute();
						ResultSet rs13 = ps13.getResultSet();	
					if (!rs13.next()){
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
						System.out.println("NO DIO UN CARGO INICIAL Y PARA QUE NO TRUENE SE LO AGREGO CON CERO PESOS"+sSql);
						PreparedStatement ps14 = this.conn.prepareStatement(sSql);
						ps14.execute();
						ResultSet rs14 = ps14.getResultSet();
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
							
					PreparedStatement ps15 = this.conn.prepareStatement(sSql);
					ps15.execute();
					ResultSet rs15 = ps15.getResultSet();	
					while (rs15.next()){
						registro.addDefCampo("ID_ACCESORIO",rs15.getString("ID_ACCESORIO")== null ? "": rs15.getString("ID_ACCESORIO"));
						registro.addDefCampo("ORDEN",rs15.getString("ORDEN")== null ? "": rs15.getString("ORDEN"));
								
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
							
						PreparedStatement ps16 = this.conn.prepareStatement(sSql);
						ps16.execute();
						ResultSet rs16 = ps16.getResultSet();			
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
							
					PreparedStatement ps18 = this.conn.prepareStatement(sSql);
					ps18.execute();
					ResultSet rs18 = ps18.getResultSet();
					
					while (rs18.next()){
						registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs18.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs18.getString("ID_ACTIVIDAD_REQUISITO"));
						registro.addDefCampo("ID_ETAPA_PRESTAMO",rs18.getString("ID_ETAPA_PRESTAMO")== null ? "": rs18.getString("ID_ETAPA_PRESTAMO"));
						registro.addDefCampo("ORDEN_ETAPA",rs18.getString("ORDEN_ETAPA")== null ? "": rs18.getString("ORDEN_ETAPA"));
						
						sSql = "INSERT INTO SIM_PRESTAMO_ACT_REQ ( \n"+
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
						
						PreparedStatement ps19 = this.conn.prepareStatement(sSql);
						ps19.execute();
						ResultSet rs19 = ps19.getResultSet();
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
							
					PreparedStatement ps20 = this.conn.prepareStatement(sSql);
					ps20.execute();
					ResultSet rs20 = ps20.getResultSet();
					
					while (rs20.next()){
						registro.addDefCampo("ID_ARCHIVO_FLUJO",rs20.getString("ID_ARCHIVO_FLUJO")== null ? "": rs20.getString("ID_ARCHIVO_FLUJO"));
						registro.addDefCampo("NOM_ARCHIVO",rs20.getString("NOM_ARCHIVO")== null ? "": rs20.getString("NOM_ARCHIVO"));
						
						sSql = "INSERT INTO SIM_PRESTAMO_FLUJO_EFECTIVO ( \n"+
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
							
						PreparedStatement ps21 = this.conn.prepareStatement(sSql);
						ps21.execute();
						ResultSet rs21 = ps21.getResultSet();
					}
					
					sSql =  "SELECT DISTINCT \n"+
						"ID_ETAPA_PRESTAMO \n"+
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
					}
					
					sSql =  " UPDATE SIM_PRESTAMO SET "+
						" ID_ETAPA_PRESTAMO 		='" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "' \n" +
						" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
						" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
					
					//VERIFICA SI DIO DE ALTA EL REGISTRO
					PreparedStatement ps23 = this.conn.prepareStatement(sSql);
					ps23.execute();
					ResultSet rs23 = ps23.getResultSet();	
					
					sSql =  " UPDATE SIM_PRESTAMO_ACT_REQ SET "+
						" FECHA_REGISTRO 		=SYSDATE, \n" +
						" ESTATUS	 		='Registrada' \n" +
						" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
						" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "' \n";
					
					//VERIFICA SI DIO DE ALTA EL REGISTRO
					PreparedStatement ps24 = this.conn.prepareStatement(sSql);
					ps24.execute();
					ResultSet rs24 = ps24.getResultSet();
					
					
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
							
					PreparedStatement ps25 = this.conn.prepareStatement(sSql);
					ps25.execute();
					ResultSet rs25 = ps25.getResultSet();
						
					while (rs25.next()){
						registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs25.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs25.getString("ID_ACTIVIDAD_REQUISITO"));
						registro.addDefCampo("ID_ETAPA_PRESTAMO",rs25.getString("ID_ETAPA_PRESTAMO")== null ? "": rs25.getString("ID_ETAPA_PRESTAMO"));
						registro.addDefCampo("ORDEN_ETAPA",rs25.getString("ORDEN_ETAPA")== null ? "": rs25.getString("ORDEN_ETAPA"));
						registro.addDefCampo("FECHA_REGISTRO",rs25.getString("FECHA_REGISTRO")== null ? "": rs25.getString("FECHA_REGISTRO"));
						registro.addDefCampo("ESTATUS",rs25.getString("ESTATUS")== null ? "": rs25.getString("ESTATUS"));
						
						resultadoCatalogoAltaActividadRequisito = simPrestamoAltaActividadRequisitoDAO.alta(registro); 
						
						
					}
					
					sSql =  " INSERT INTO SIM_PRESTAMO_ESTATUS_HISTORICO \n" +
							" (CVE_GPO_EMPRESA, \n" +
							" CVE_EMPRESA, \n" +
							" ID_PRESTAMO, \n" +
							" ID_ETAPA_PRESTAMO, \n" +
							" CVE_USUARIO, \n" +
							" FECHA) \n" +
							" VALUES \n" +
							"('" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							" '" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"'" + sIdPrestamo + "', \n" +
							" '" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "', \n" +
							" '" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
							"SYSDATE) \n" ;
					
					//ejecutaSql();
					PreparedStatement ps28 = this.conn.prepareStatement(sSql);
					ps28.execute();
					ResultSet rs28 = ps28.getResultSet();
				
					sSql =  " DELETE FROM SIM_PRESTAMO "+
						" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
						" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
							
					//VERIFICA SI DIO DE ALTA EL REGISTRO
					PreparedStatement ps29 = this.conn.prepareStatement(sSql);
					ps29.execute();
					ResultSet rs29 = ps29.getResultSet();	
					//Obtiene la cuenta vista (id_cuenta_referencia).
					sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
						
					PreparedStatement ps31 = this.conn.prepareStatement(sSql);
					ps31.execute();
					ResultSet rs31 = ps31.getResultSet();	
					if (rs31.next()){
						registro.addDefCampo("ID_CUENTA",rs31.getString("ID_CUENTA"));
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
							
						PreparedStatement ps32 = this.conn.prepareStatement(sSql);
						ps32.execute();
						ResultSet rs32 = ps32.getResultSet();
						//B) Agrega la cuenta vista (id_cuenta_referencia) al credito en la tabla SIM_PRESTAMO.
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
					
					//Obtiene la cuenta crédito (id_cuenta).
					sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
							
					PreparedStatement ps35 = this.conn.prepareStatement(sSql);
					ps35.execute();
					ResultSet rs35 = ps35.getResultSet();	
					if (rs35.next()){
						registro.addDefCampo("ID_CUENTA",rs35.getString("ID_CUENTA"));
						//A) Agrega la cuenta crédito (id_cuenta) al integrante en la tabla PFIN_CUENTA.
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
						
						PreparedStatement ps36 = this.conn.prepareStatement(sSql);
						ps36.execute();
						ResultSet rs36 = ps36.getResultSet();
						//B) Agrega la cuenta crédito (id_cuenta) al credito en la tabla SIM_PRESTAMO.
						sSql =  " UPDATE SIM_PRESTAMO SET "+
							" ID_CUENTA			='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
							" WHERE ID_PRESTAMO		='" + sIdPrestamo + "' \n" +
							" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						PreparedStatement ps37 = this.conn.prepareStatement(sSql);
						ps37.execute();
						ResultSet rs37 = ps37.getResultSet();
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
								
						PreparedStatement ps38 = this.conn.prepareStatement(sSql);
						ps38.execute();
						ResultSet rs38 = ps38.getResultSet();
						while (rs38.next()){
							registro.addDefCampo("ID_DOCUMENTO",rs38.getString("ID_DOCUMENTO")== null ? "": rs38.getString("ID_DOCUMENTO"));
									
							sSql = "INSERT INTO SIM_PRESTAMO_DOCUMENTACION ( \n"+
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_PRESTAMO, \n" +
								"ID_DOCUMENTO) \n" +
								" VALUES (" +
								"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + sIdPrestamo + "', \n" +
								"'" + (String)registro.getDefCampo("ID_DOCUMENTO") + "') \n" ;
						
							//ejecutaSql();
							PreparedStatement ps39 = this.conn.prepareStatement(sSql);
							ps39.execute();
							ResultSet rs39 = ps39.getResultSet();	
						}		
						
					
				}//Termina de ingresar el producto-ciclo 1.	
						
			}//Termina de ingresar el producto-ciclo correspondiente de cada integrante del grupo.	
			
		}else {
			//El producto no tiene actividades o requisitos definidos.
			resultadoCatalogo.mensaje.setClave("PRODUCTO_SIN_ACT_REQ");
		}
	
		simPrestamoAltaActividadRequisitoDAO.cierraConexion();
		
		return resultadoCatalogo;
	}
}