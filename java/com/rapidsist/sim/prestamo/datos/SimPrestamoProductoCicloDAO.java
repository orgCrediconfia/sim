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
import com.rapidsist.portal.catalogos.OperacionModificacion; 
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;
import com.rapidsist.sim.grupo.datos.SimGrupoIntegranteDAO;


/**
 * Administra los accesos a la base de datos para los productos asigandos a los préstamos.
 */
 
public class SimPrestamoProductoCicloDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro {

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
				"P.ID_CLIENTE, \n"+
				"C.NOM_COMPLETO, \n"+
				"P.ID_GRUPO, \n"+
				"G.NOM_GRUPO, \n"+
				"P.FECHA_SOLICITUD, \n"+
				"P.FECHA_ENTREGA, \n"+
				"P.ID_COMITE, \n"+
				"P.ID_ETAPA_PRESTAMO, \n"+
				"P.CVE_ASESOR_CREDITO, \n"+
				"NA.NOM_COMPLETO NOMBRE_ASESOR, \n"+
				"P.FECHA_ASESOR, \n"+
				"P.CVE_RECUPERADOR, \n"+
				"NR.NOM_COMPLETO NOMBRE_RECUPERADOR, \n"+
				"P.FECHA_RECUPERADOR \n"+
				"FROM \n"+
				"SIM_PRESTAMO P, \n"+
				"RS_GRAL_USUARIO UA, \n"+
				"RS_GRAL_PERSONA NA, \n"+
				"RS_GRAL_USUARIO UR, \n"+
				"RS_GRAL_PERSONA NR, \n"+
				"RS_GRAL_PERSONA C, \n"+
				"SIM_GRUPO G \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND UA.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND UA.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND UA.CVE_USUARIO = P.CVE_ASESOR_CREDITO \n"+
				"AND NA.CVE_GPO_EMPRESA = UA.CVE_GPO_EMPRESA \n"+
				"AND NA.CVE_EMPRESA = UA.CVE_EMPRESA \n"+
				"AND NA.ID_PERSONA = UA.ID_PERSONA \n"+
				"AND UR.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND UR.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND UR.CVE_USUARIO = P.CVE_ASESOR_CREDITO \n"+
				"AND NR.CVE_GPO_EMPRESA = UR.CVE_GPO_EMPRESA \n"+
				"AND NR.CVE_EMPRESA = UR.CVE_EMPRESA \n"+
				"AND NR.ID_PERSONA = UR.ID_PERSONA \n"+
				"AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND C.ID_PERSONA = P.ID_CLIENTE \n"+
				"AND G.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND G.ID_GRUPO = P.ID_GRUPO \n";
				
			
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
		
		sSql =  "SELECT DISTINCT \n"+
				"ID_ETAPA_PRESTAMO \n"+
				"FROM \n"+
				"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
		ejecutaSql();
		
		if (rs.next()){
			
			String sCvePrestamo = "";
		
			//OBTENEMOS EL SEQUENCE
			sSql = "SELECT SQ01_SIM_PRESTAMO.nextval as ID_PRESTAMO FROM DUAL";
			ejecutaSql();
			
			if (rs.next()){
				registro.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO"));
			}
			
			resultadoCatalogo.Resultado.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO"));
			
			sSql =  "SELECT \n" +
					"ID_PERSONA, \n" +
					"ID_SUCURSAL, \n" +
					"CVE_ASESOR_CREDITO \n" +
					"FROM RS_GRAL_PERSONA \n" +
					" WHERE ID_PERSONA		='" + (String)registro.getDefCampo("ID_CLIENTE") + "' \n" +
					" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			ejecutaSql();
			if (rs.next()){
				registro.addDefCampo("ID_PERSONA",rs.getString("ID_PERSONA")== null ? "": rs.getString("ID_PERSONA"));
				registro.addDefCampo("ID_SUCURSAL",rs.getString("ID_SUCURSAL")== null ? "": rs.getString("ID_SUCURSAL"));
				registro.addDefCampo("CVE_ASESOR_CREDITO",rs.getString("CVE_ASESOR_CREDITO")== null ? "": rs.getString("CVE_ASESOR_CREDITO"));
				
			}
			
			//OBTIENE CLAVE DEL PRÉSTAMO.
			sSql =  "SELECT REPLACE (CVE_PRESTAMO,' ','') CVE_PRESTAMO FROM ( \n"+
					"SELECT TO_CHAR('" + (String)registro.getDefCampo("ID_PRODUCTO") + "','00')||'000000'||TO_CHAR('" + (String)registro.getDefCampo("ID_CLIENTE") + "','00000000')||TO_CHAR('" + (String)registro.getDefCampo("NUM_CICLO") + "','00') AS CVE_PRESTAMO FROM DUAL \n"+
					") \n";
			ejecutaSql();
			if (rs.next()){
				sCvePrestamo = rs.getString("CVE_PRESTAMO");
			}
			
			
			
			sSql =  "INSERT INTO SIM_PRESTAMO ( "+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_PRESTAMO, \n" +
					"CVE_PRESTAMO, \n" +
					"FECHA_SOLICITUD, \n" +
					"ID_CLIENTE, \n" +
					"ID_SUCURSAL, \n" +
					"CVE_ASESOR_CREDITO, \n" +
					"FECHA_ASESOR_SIST, \n" +
					"ID_PRODUCTO, \n" +
					"NUM_CICLO, \n" +
					"CVE_METODO, \n" +
					"APLICA_A, \n" +
					"ID_PERIODICIDAD_PRODUCTO, \n" +
					"ID_FORMA_DISTRIBUCION, \n" +
					"PLAZO, \n" +
					"TIPO_TASA, \n" +
					"VALOR_TASA, \n" +
					"ID_PERIODICIDAD_TASA, \n" +
					"ID_TASA_REFERENCIA, \n" +
					"MONTO_MAXIMO, \n" +
					"MONTO_MINIMO, \n" +
					"PORC_FLUJO_CAJA, \n" +
					"B_ENTREGADO) \n" +
					" VALUES (" +
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
					" " + sCvePrestamo +", \n" +
					"SYSDATE, \n" +
					"'" + (String)registro.getDefCampo("ID_CLIENTE") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
					"SYSDATE, \n" +
					"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
					"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_METODO") + "', \n" +
					"'" + (String)registro.getDefCampo("APLICA_A") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_PRODUCTO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_FORMA_DISTRIBUCION") + "', \n" +
					"'" + (String)registro.getDefCampo("PLAZO") + "', \n" +
					"'" + (String)registro.getDefCampo("TIPO_TASA") + "', \n" +
					"'" + (String)registro.getDefCampo("VALOR_TASA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "', \n" +
					"'" + (String)registro.getDefCampo("MONTO_MAXIMO") + "', \n" +
					"'" + (String)registro.getDefCampo("MONTO_MINIMO") + "', \n" +
					"'" + (String)registro.getDefCampo("PORC_FLUJO_CAJA") + "', \n" +
					"'F') \n" ;
					
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			
			//INGRESA LA FECHA DE LA TASA DE REFERENCIA SI EL TIPO DE REFERENCIA ES SI INDEXADA
			if (registro.getDefCampo("TIPO_TASA").equals("Si indexada")){
				
				sSql = "UPDATE SIM_PRESTAMO SET "+
					"FECHA_TASA_REFERENCIA = (SELECT MAX(FECHA_PUBLICACION) FECHA_TASA_REFERENCIA \n"+
								"FROM ( \n"+
								"SELECT FECHA_PUBLICACION FROM SIM_CAT_TASA_REFER_DETALLE \n"+
								"WHERE ID_TASA_REFERENCIA = '" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "' \n" +
								"AND FECHA_PUBLICACION < TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') \n" +
								" ))\n"+
					"WHERE ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO")+ "' \n" +
					"AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA")+"' \n" ;
				
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				
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
				"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_CLIENTE") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "') \n" ;
			
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
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
					
			ejecutaSql();
			while (rs.next()){
				registro.addDefCampo("ID_CARGO_COMISION",rs.getString("ID_CARGO_COMISION")== null ? "": rs.getString("ID_CARGO_COMISION"));
				registro.addDefCampo("ID_FORMA_APLICACION",rs.getString("ID_FORMA_APLICACION")== null ? "": rs.getString("ID_FORMA_APLICACION"));
				registro.addDefCampo("CARGO_INICIAL",rs.getString("CARGO_INICIAL")== null ? "": rs.getString("CARGO_INICIAL"));
				registro.addDefCampo("PORCENTAJE_MONTO",rs.getString("PORCENTAJE_MONTO")== null ? "": rs.getString("PORCENTAJE_MONTO"));
				registro.addDefCampo("CANTIDAD_FIJA",rs.getString("CANTIDAD_FIJA")== null ? "": rs.getString("CANTIDAD_FIJA"));
				registro.addDefCampo("VALOR",rs.getString("VALOR")== null ? "": rs.getString("VALOR"));
				registro.addDefCampo("ID_UNIDAD",rs.getString("ID_UNIDAD")== null ? "": rs.getString("ID_UNIDAD"));
				registro.addDefCampo("ID_PERIODICIDAD",rs.getString("ID_PERIODICIDAD")== null ? "": rs.getString("ID_PERIODICIDAD"));
				
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
					"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_FORMA_APLICACION") + "', \n" +
					"'" + (String)registro.getDefCampo("CARGO_INICIAL") + "', \n" +
					"'" + (String)registro.getDefCampo("PORCENTAJE_MONTO") + "', \n" +
					"'" + (String)registro.getDefCampo("CANTIDAD_FIJA") + "', \n" +
					"'" + (String)registro.getDefCampo("VALOR") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_UNIDAD") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "') \n" ;
				
				PreparedStatement ps3 = this.conn.prepareStatement(sSql);
				ps3.execute();
				ResultSet rs3 = ps3.getResultSet();
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
			ejecutaSql();
			if (!rs.next()){
				//NO DIO UN CARGO INICIAL Y PARA QUE NO TRUENE EN EL SUPER MOTOR SE LO AGREGO CON CERO PESOS.
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
						"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
						"'9', \n" +
						"'1', \n" +
						"'0') \n" ;
						PreparedStatement ps24 = this.conn.prepareStatement(sSql);
						ps24.execute();
						ResultSet rs24 = ps24.getResultSet();
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
					
			ejecutaSql();
			while (rs.next()){
				registro.addDefCampo("ID_ACCESORIO",rs.getString("ID_ACCESORIO")== null ? "": rs.getString("ID_ACCESORIO"));
				registro.addDefCampo("ORDEN",rs.getString("ORDEN")== null ? "": rs.getString("ORDEN"));
						
				sSql = "INSERT INTO SIM_PRESTAMO_ACCESORIO ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_PRESTAMO, \n" +
					"ID_ACCESORIO, \n" +
					"ORDEN) \n" +
					" VALUES (" +
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_ACCESORIO") + "', \n" +
					"'" + (String)registro.getDefCampo("ORDEN") + "') \n" ;
				
				PreparedStatement ps4 = this.conn.prepareStatement(sSql);
				ps4.execute();
				ResultSet rs4 = ps4.getResultSet();	
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
			
			ejecutaSql();
			
			while (rs.next()){
				registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs.getString("ID_ACTIVIDAD_REQUISITO"));
				registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO")== null ? "": rs.getString("ID_ETAPA_PRESTAMO"));
				registro.addDefCampo("ORDEN_ETAPA",rs.getString("ORDEN_ETAPA")== null ? "": rs.getString("ORDEN_ETAPA"));
				
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
					"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
					"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "') \n" ;
				
				PreparedStatement ps5 = this.conn.prepareStatement(sSql);
				ps5.execute();
				ResultSet rs5 = ps5.getResultSet();
			}//Consulta las etapas-actividades del producto, para insertarlas en SIM_PRESTAMO_ETAPA.
			
			//Actualiza la etapa del crédito a la primer etapa del producto.
			sSql =  "SELECT DISTINCT \n"+
				"ID_ETAPA_PRESTAMO \n"+
				"FROM \n"+
				"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
				"AND ORDEN_ETAPA = '1' \n";
				
				ejecutaSql();
				if (rs.next()){
					registro.addDefCampo("ESTATUS_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO")== null ? "": rs.getString("ID_ETAPA_PRESTAMO"));
				
					sSql =  " UPDATE SIM_PRESTAMO SET "+
						" ID_ETAPA_PRESTAMO 		='" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "' \n" +
						" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
						" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
					
					//VERIFICA SI DIO DE ALTA EL REGISTRO
					PreparedStatement ps10 = this.conn.prepareStatement(sSql);
					ps10.execute();
					ResultSet rs10 = ps10.getResultSet();
				}//Actualiza la etapa del crédito a la primer etapa del producto.
				
			//Actualiza los atributos de fecha de registro y estatus de las actividades de la etapa 1.
			sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
				" FECHA_REGISTRO 		=SYSDATE, \n" +
				" ESTATUS	 		='Registrada' \n" +
				" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ESTATUS_PRESTAMO") + "' \n";
			
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			PreparedStatement ps11 = this.conn.prepareStatement(sSql);
			ps11.execute();
			ResultSet rs11 = ps11.getResultSet();
			//Actualiza los atributos de fecha de registro y estatus de las actividades de la etapa 1.
			
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
				"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
					
			ejecutaSql();
			
			while (rs.next()){
				registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs.getString("ID_ACTIVIDAD_REQUISITO"));
				registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO")== null ? "": rs.getString("ID_ETAPA_PRESTAMO"));
				registro.addDefCampo("ORDEN_ETAPA",rs.getString("ORDEN_ETAPA")== null ? "": rs.getString("ORDEN_ETAPA"));
				registro.addDefCampo("FECHA_REGISTRO",rs.getString("FECHA_REGISTRO")== null ? "": rs.getString("FECHA_REGISTRO"));
				registro.addDefCampo("ESTATUS",rs.getString("ESTATUS")== null ? "": rs.getString("ESTATUS"));
				
				sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
			
				PreparedStatement ps12 = this.conn.prepareStatement(sSql);
				ps12.execute();
				ResultSet rs12 = ps12.getResultSet();	
				if (rs12.next()){
					registro.addDefCampo("ID_HISTORICO",rs12.getString("ID_HISTORICO"));
					
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
						"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
						"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
						"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
						"'" + (String)registro.getDefCampo("ESTATUS") + "') \n" ;
					
					PreparedStatement ps13 = this.conn.prepareStatement(sSql);
					ps13.execute();
					ResultSet rs13 = ps13.getResultSet();
				}
			}//Actualiza la bitácora.
			
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
					
			ejecutaSql();
			
			while (rs.next()){
				registro.addDefCampo("ID_ARCHIVO_FLUJO",rs.getString("ID_ARCHIVO_FLUJO")== null ? "": rs.getString("ID_ARCHIVO_FLUJO"));
				registro.addDefCampo("NOM_ARCHIVO",rs.getString("NOM_ARCHIVO")== null ? "": rs.getString("NOM_ARCHIVO"));
				
				sSql = "INSERT INTO SIM_PRESTAMO_FLUJO_EFECTIVO ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_PRESTAMO, \n" +
					"ID_ARCHIVO_FLUJO, \n" +
					"NOM_ARCHIVO) \n"+
					" VALUES (" +
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_ARCHIVO_FLUJO") + "', \n" +
					"'" + (String)registro.getDefCampo("NOM_ARCHIVO") + "') \n" ;
			
				PreparedStatement ps15 = this.conn.prepareStatement(sSql);
				ps15.execute();
				ResultSet rs15 = ps15.getResultSet();
			}
			
			sSql =  "SELECT \n"+
				"CVE_GPO_EMPRESA, \n"+
				"CVE_EMPRESA, \n"+
				"ID_PRODUCTO, \n"+
				"CVE_TIPO_PERSONA \n"+
				"FROM \n"+
				"SIM_PRODUCTO_PARTICIPANTE \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
					
			ejecutaSql();
			
			while (rs.next()){
				registro.addDefCampo("CVE_TIPO_PERSONA",rs.getString("CVE_TIPO_PERSONA")== null ? "": rs.getString("CVE_TIPO_PERSONA"));
				
				sSql = "INSERT INTO SIM_PRESTAMO_PARTICIPANTE ( \n"+
					"CVE_GPO_EMPRESA, \n"+
					"CVE_EMPRESA, \n"+
					"ID_PRESTAMO, \n"+
					"CVE_TIPO_PERSONA) \n"+
					" VALUES (" +
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_TIPO_PERSONA") + "') \n" ;
				
				PreparedStatement ps16 = this.conn.prepareStatement(sSql);
				ps16.execute();
				ResultSet rs16 = ps16.getResultSet();
			}
			//Obtiene la cuenta vista (id_cuenta_referencia).
			sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
			
			PreparedStatement ps17 = this.conn.prepareStatement(sSql);
			ps17.execute();
			ResultSet rs17 = ps17.getResultSet();	
			if (rs17.next()){
				registro.addDefCampo("ID_CUENTA",rs17.getString("ID_CUENTA"));
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
					
				PreparedStatement ps18 = this.conn.prepareStatement(sSql);
				ps18.execute();
				ResultSet rs18 = ps18.getResultSet();
				//B) Agrega la cuenta vista (id_cuenta_referencia) al credito en la tabla SIM_PRESTAMO.
				sSql =  " UPDATE SIM_PRESTAMO SET "+
						" ID_CUENTA_REFERENCIA		='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
						" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
						" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
					
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				PreparedStatement ps19 = this.conn.prepareStatement(sSql);
				ps19.execute();
				ResultSet rs19 = ps19.getResultSet();
			}
			//Obtiene la cuenta crédito (id_cuenta).
			sSql = "SELECT SQ01_PFIN_CUENTA.nextval AS ID_CUENTA FROM DUAL";
				
			PreparedStatement ps20 = this.conn.prepareStatement(sSql);
			ps20.execute();
			ResultSet rs20 = ps20.getResultSet();	
			if (rs20.next()){
				registro.addDefCampo("ID_CUENTA",rs20.getString("ID_CUENTA"));
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
					"'" + (String)registro.getDefCampo("ID_CLIENTE") + "') \n" ;
				
				PreparedStatement ps21 = this.conn.prepareStatement(sSql);
				ps21.execute();
				ResultSet rs21 = ps21.getResultSet();
				//B) Agrega la cuenta crédito (id_cuenta) al credito en la tabla SIM_PRESTAMO.
				sSql =  " UPDATE SIM_PRESTAMO SET "+
					" ID_CUENTA			='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n"+
					" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
					" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				PreparedStatement ps22 = this.conn.prepareStatement(sSql);
				ps22.execute();
				ResultSet rs22 = ps22.getResultSet();
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
						
				ejecutaSql();
				while (rs.next()){
					registro.addDefCampo("ID_DOCUMENTO",rs.getString("ID_DOCUMENTO")== null ? "": rs.getString("ID_DOCUMENTO"));
							
					sSql = "INSERT INTO SIM_PRESTAMO_DOCUMENTACION ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_PRESTAMO, \n" +
						"ID_DOCUMENTO) \n" +
						" VALUES (" +
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_DOCUMENTO") + "') \n" ;
					
					PreparedStatement ps23 = this.conn.prepareStatement(sSql);
					ps23.execute();
					ResultSet rs23 = ps23.getResultSet();	
				}
				
				
				sSql = "SELECT \n"+
						"PC.CVE_GPO_EMPRESA, \n"+
						"PC.CVE_EMPRESA, \n"+
						"PC.ID_PRODUCTO, \n"+
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
				
				PreparedStatement ps24 = this.conn.prepareStatement(sSql);
				ps24.execute();
				ResultSet rs24 = ps24.getResultSet();	
				
				if(rs24.next()){
					registro.addDefCampo("ID_TIPO_RECARGO",rs24.getString("ID_TIPO_RECARGO")== null ? "": rs24.getString("ID_TIPO_RECARGO"));
					registro.addDefCampo("TIPO_TASA_RECARGO",rs24.getString("TIPO_TASA_RECARGO")== null ? "": rs24.getString("TIPO_TASA_RECARGO"));
					registro.addDefCampo("TASA_RECARGO",rs24.getString("TASA_RECARGO")== null ? "": rs24.getString("TASA_RECARGO"));
					registro.addDefCampo("ID_PERIODICIDAD_TASA_RECARGO",rs24.getString("ID_PERIODICIDAD_TASA_RECARGO")== null ? "": rs24.getString("ID_PERIODICIDAD_TASA_RECARGO"));
					registro.addDefCampo("ID_TASA_REFERENCIA_RECARGO",rs24.getString("ID_TASA_REFERENCIA_RECARGO")== null ? "": rs24.getString("ID_TASA_REFERENCIA_RECARGO"));
					registro.addDefCampo("FACTOR_TASA_RECARGO",rs24.getString("FACTOR_TASA_RECARGO")== null ? "": rs24.getString("FACTOR_TASA_RECARGO"));
					registro.addDefCampo("MONTO_FIJO_PERIODO",rs24.getString("MONTO_FIJO_PERIODO")== null ? "": rs24.getString("MONTO_FIJO_PERIODO"));
				
					
					sSql =  " UPDATE SIM_PRESTAMO SET "+
							" ID_TIPO_RECARGO				='" + (String)registro.getDefCampo("ID_TIPO_RECARGO") + "', \n"+
							" TIPO_TASA_RECARGO				='" + (String)registro.getDefCampo("TIPO_TASA_RECARGO") + "', \n"+
							" TASA_RECARGO					='" + (String)registro.getDefCampo("TASA_RECARGO") + "', \n"+
							" ID_PERIODICIDAD_TASA_RECARGO	='" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA_RECARGO") + "', \n"+
							" ID_TASA_REFERENCIA_RECARGO	='" + (String)registro.getDefCampo("ID_TASA_REFERENCIA_RECARGO") + "', \n"+
							" FACTOR_TASA_RECARGO			='" + (String)registro.getDefCampo("FACTOR_TASA_RECARGO") + "', \n"+
							" MONTO_FIJO_PERIODO			='" + (String)registro.getDefCampo("MONTO_FIJO_PERIODO") + "' \n"+
							" WHERE ID_PRESTAMO				='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
							" AND CVE_EMPRESA				='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA			='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						
				PreparedStatement ps25 = this.conn.prepareStatement(sSql);
				ps25.execute();
				ResultSet rs25 = ps25.getResultSet();	
				
				}	
					
						
				
		}else {
			resultadoCatalogo.mensaje.setClave("PRODUCTO_SIN_ACT_REQ");
		}
		
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
		
		sSql =  "UPDATE RS_GRAL_PERSONA SET "+
				"AP_PATERNO = '" + (String)registro.getDefCampo("AP_PATERNO") + "', \n" +
				"AP_MATERNO = '" + (String)registro.getDefCampo("AP_MATERNO") + "', \n" +
				"NOMBRE_1 = '" + (String)registro.getDefCampo("NOMBRE_1") + "', \n" +
				"NOMBRE_2 = '" + (String)registro.getDefCampo("NOMBRE_2") + "', \n" +
				"NOM_COMPLETO = '" + (String)registro.getDefCampo("NOM_COMPLETO") + "', \n" +
				"NOMBRE_ALTERNO = '" + (String)registro.getDefCampo("NOMBRE_ALTERNO") + "', \n" +
				"SEXO = '" + (String)registro.getDefCampo("SEXO") + "', \n" +
				"ESTADO_CIVIL = '" + (String)registro.getDefCampo("ESTADO_CIVIL") + "', \n" +
				"REGIMEN_MARITAL = '" + (String)registro.getDefCampo("REGIMEN_MARITAL") + "', \n" +
				"FECHA_NACIMIENTO = TO_DATE('" + (String)registro.getDefCampo("FECHA_NACIMIENTO")  + "','DD/MM/YYYY'), \n" +
				"IDENTIFICACION_OFICIAL	= '" + (String)registro.getDefCampo("IDENTIFICACION_OFICIAL") + "', \n" +
				"NUM_IDENTIFICACION_OFICIAL = '" + (String)registro.getDefCampo("NUM_IDENTIFICACION_OFICIAL") + "', \n" +
				"RFC = '" + (String)registro.getDefCampo("RFC") + "', \n" +
				"CURP = '" + (String)registro.getDefCampo("CURP") + "', \n" +
				"NUM_DEPENDIENTES_ECONOMICOS = '" + (String)registro.getDefCampo("NUM_DEPENDIENTES_ECONOMICOS") + "', \n" +
				"ID_ESCOLARIDAD	= '" + (String)registro.getDefCampo("ID_ESCOLARIDAD") + "', \n" +
				"LISTA_NEGRA = '" + (String)registro.getDefCampo("LISTA_NEGRA") + "', \n" +
				"B_PERSONA_FISICA = 'V' \n" +
			"WHERE ID_PERSONA = '" + (String)registro.getDefCampo("ID_PERSONA")+ "' \n" +
			"AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
			"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA")+"' \n" ;
		
		//VERIFICA SI NO SE ACTUALIZÓ EL REGISTRO
		if (ejecutaUpdate() == 0){
		resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
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