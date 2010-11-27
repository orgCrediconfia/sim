/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para asignar el producto-ciclo a un préstamo.
 */
 
public class SimAsignaProductoCicloDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		String sCicloUltimo = "";
		String sFactor = "";
		int iFactor = 0;
		String sIdProducto = "";
	
		//Consulta los productos-ciclos asignados anteriormente al cliente.
		sSql =  "SELECT \n"+
			"P.CVE_GPO_EMPRESA, \n"+
			"P.CVE_EMPRESA, \n"+
			"P.ID_PRESTAMO, \n"+
			"P.ID_PRODUCTO, \n"+
			"P.NUM_CICLO, \n"+
			"P.ID_CLIENTE \n"+
			"FROM \n"+
			"SIM_PRESTAMO P \n"+
			"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND P.ID_CLIENTE = '" + (String)parametros.getDefCampo("ID_CLIENTE") + "' \n"+
			"AND P.ID_GRUPO IS NULL \n"+
			"AND P.ID_PRODUCTO IS NOT NULL \n";
		
			
		ejecutaSql();
		
		if (rs.next()){
			parametros.addDefCampo("ID_PRODUCTO",rs.getString("ID_PRODUCTO")== null ? "": rs.getString("ID_PRODUCTO"));
			
			
			//Obtiene el siguiente producto-ciclo.
			
			sSql = "SELECT DISTINCT \n"+
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
				"P.MONTO_MINIMO, \n"+
				"PC.PORC_FLUJO_CAJA, \n"+
				"P.FECHA_INICIO_ACTIVACION, \n"+
				"P.FECHA_FIN_ACTIVACION \n"+
				"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
				"SIM_PRODUCTO P, \n"+
				"SIM_PRODUCTO_SUCURSAL PS \n"+
				"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND PC.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				"AND PC.NUM_CICLO = (SELECT \n"+
				"MAX(P.NUM_CICLO)+1 NUM_CICLO_PROX \n"+
				"FROM \n"+
				"SIM_PRESTAMO P \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				"AND P.ID_CLIENTE = '" + (String)parametros.getDefCampo("ID_CLIENTE") + "' \n"+
				") \n"+
				"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
				"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
				"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
				"AND P.APLICA_A = 'Individual' \n"+
				"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
				"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
				"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
			"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n";
			ejecutaSql();
			if (rs.next()){
				//Los ciclos que se presentan son el el siguiente que corresponde (query above) y los ciclos 1 de los otros productos.
				sSql = sSql + "UNION \n"+
				      	"SELECT  DISTINCT \n"+
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
					"P.MONTO_MINIMO, \n"+
					"PC.PORC_FLUJO_CAJA, \n"+
					"P.FECHA_INICIO_ACTIVACION, \n"+
					"P.FECHA_FIN_ACTIVACION \n"+
					"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
					"SIM_PRODUCTO P, \n"+
					"SIM_PRODUCTO_SUCURSAL PS \n"+
					"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
					"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
					"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
					"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
					"AND PC.NUM_CICLO = '1'	\n"+
					"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
					"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
					"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
					"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
					"AND P.APLICA_A = 'Individual' \n"+
					"MINUS \n"+
					"SELECT DISTINCT \n"+
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
					"P.MONTO_MINIMO, \n"+
					"PC.PORC_FLUJO_CAJA, \n"+
					"P.FECHA_INICIO_ACTIVACION, \n"+
					"P.FECHA_FIN_ACTIVACION \n"+
					"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
					"SIM_PRODUCTO P, \n"+
					"SIM_PRODUCTO_SUCURSAL PS \n"+
					"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND PC.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
					"AND PC.NUM_CICLO = '1' \n"+
					"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
					"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
					"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
					"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
					"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
					"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
					"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
					"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
					"AND P.APLICA_A = 'Individual' \n";
				
					
			}else{
				//Obtiene el siguiente ciclo que corresponde aunque no este definido en el producto.
				
				sSql = "SELECT \n"+
						"(B.ULTIMO_CICLO - A.NUM_CICLO) FACTOR \n"+
						"FROM \n"+
						"(SELECT \n"+
						"MAX(NUM_CICLO) NUM_CICLO \n"+
						"FROM \n"+
						"SIM_PRODUCTO_CICLO \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "')A, \n"+
						"(SELECT \n"+
						"MAX(NUM_CICLO) ULTIMO_CICLO \n"+
						"FROM \n"+
						"SIM_PRESTAMO  \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
						"AND ID_CLIENTE = '" + (String)parametros.getDefCampo("ID_CLIENTE") + "')B \n";
				
				ejecutaSql();
				if (rs.next()){
					sFactor = rs.getString("FACTOR");
					iFactor = (Integer.parseInt(sFactor));
					iFactor++;
						
	
						//Obtiene el ultimo ciclo del producto y los ciclo 1 de los productos
						sSql = "SELECT DISTINCT \n"+
						"PC.CVE_GPO_EMPRESA, \n"+
						"PC.CVE_EMPRESA, \n"+
						"PC.ID_PRODUCTO, \n"+
						"P.NOM_PRODUCTO, \n"+
						"P.APLICA_A, \n"+
						"P.ID_PERIODICIDAD, \n"+
						"P.CVE_METODO, \n"+
						"PC.NUM_CICLO + '"+iFactor+"' NUM_CICLO, \n"+
						"PC.ID_FORMA_DISTRIBUCION, \n"+
						"PC.PLAZO, \n"+
						"PC.TIPO_TASA, \n"+
						"PC.VALOR_TASA, \n"+
						"PC.ID_PERIODICIDAD_TASA, \n"+
						"PC.ID_TASA_REFERENCIA, \n"+
						"PC.MONTO_MAXIMO, \n"+
						"P.MONTO_MINIMO, \n"+
						"PC.PORC_FLUJO_CAJA, \n"+
						"P.FECHA_INICIO_ACTIVACION, \n"+
						"P.FECHA_FIN_ACTIVACION \n"+
						"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
						"SIM_PRODUCTO P, \n"+
						"SIM_PRODUCTO_SUCURSAL PS \n"+
						"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND PC.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
						"AND PC.NUM_CICLO = (SELECT \n"+
						"MAX(NUM_CICLO) \n"+
						"FROM \n"+
						"SIM_PRODUCTO_CICLO \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
						") \n"+
						"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
						"AND P.APLICA_A = 'Individual' \n"+
						"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
						"UNION \n"+
						"SELECT DISTINCT \n"+
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
						"P.MONTO_MINIMO, \n"+
						"PC.PORC_FLUJO_CAJA, \n"+
						"P.FECHA_INICIO_ACTIVACION, \n"+
						"P.FECHA_FIN_ACTIVACION \n"+
						"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
						"SIM_PRODUCTO P, \n"+
						"SIM_PRODUCTO_SUCURSAL PS \n"+
						"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
						"AND PC.NUM_CICLO = '1'	\n"+
						"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
						"AND P.APLICA_A = 'Individual' \n"+
						"MINUS \n"+
						"SELECT DISTINCT \n"+
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
						"P.MONTO_MINIMO, \n"+
						"PC.PORC_FLUJO_CAJA, \n"+
						"P.FECHA_INICIO_ACTIVACION, \n"+
						"P.FECHA_FIN_ACTIVACION \n"+
						"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
						"SIM_PRODUCTO P, \n"+
						"SIM_PRODUCTO_SUCURSAL PS \n"+
						"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND PC.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
						"AND PC.NUM_CICLO = '1' \n"+
						"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
						"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
						"AND P.APLICA_A = 'Individual' \n";
					
				}
			
			}	
			
		} else {
		
			//Busca si hay una excepción.
			sSql =  "SELECT \n"+
					"P.CVE_GPO_EMPRESA, \n"+
					"P.CVE_EMPRESA, \n"+
					"P.ID_PRODUCTO, \n"+
					"P.NUM_CICLO, \n"+
					"P.ID_CLIENTE \n"+
					"FROM \n"+
					"SIM_PRESTAMO_EXCEPCION_CICLO P \n"+
					"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND P.ID_CLIENTE = '" + (String)parametros.getDefCampo("ID_CLIENTE") + "' \n";
					
				ejecutaSql();
				if (rs.next()){
					
					parametros.addDefCampo("ID_PRODUCTO",rs.getString("ID_PRODUCTO")== null ? "": rs.getString("ID_PRODUCTO"));
					parametros.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
					//Busca si existe el siguiente ciclo en en el producto.
					sSql = "SELECT DISTINCT \n"+
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
							"P.MONTO_MINIMO, \n"+
							"PC.PORC_FLUJO_CAJA, \n"+
							"P.FECHA_INICIO_ACTIVACION, \n"+
							"P.FECHA_FIN_ACTIVACION \n"+
							"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
							"SIM_PRODUCTO P, \n"+
							"SIM_PRODUCTO_SUCURSAL PS \n"+
							"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
							"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
							"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
							"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
							"AND PC.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+	
							"AND PC.NUM_CICLO = '" + (String)parametros.getDefCampo("NUM_CICLO") + "' + '1' \n"+	
							"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
							"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
							"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
							"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
							"AND P.APLICA_A = 'Individual' \n";
					ejecutaSql();
					if (!rs.next()){
						
						//Como no existe el siguiente se crea el siguiente ciclo al que llamaré "virtual".
						sSql = "SELECT \n"+
						"(B.ULTIMO_CICLO - A.NUM_CICLO) FACTOR \n"+
						"FROM \n"+
						"(SELECT \n"+
						"MAX(NUM_CICLO) NUM_CICLO \n"+
						"FROM \n"+
						"SIM_PRODUCTO_CICLO \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "')A, \n"+
						"(SELECT \n"+
						"NVL(MAX(NUM_CICLO),0) ULTIMO_CICLO \n"+
						"FROM \n"+
						"SIM_PRESTAMO  \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
						"AND ID_CLIENTE = '" + (String)parametros.getDefCampo("ID_CLIENTE") + "')B \n";
					
				ejecutaSql();
				if (rs.next()){
					sFactor = rs.getString("FACTOR");
					
					iFactor = (Integer.parseInt(sFactor));
					if (iFactor < 0){
						iFactor = 1;
					}else {
						iFactor++;
					}	
	
						//Obtiene el ultimo ciclo del producto y los ciclo 1 de los productos
						sSql = "SELECT DISTINCT \n"+
						"PC.CVE_GPO_EMPRESA, \n"+
						"PC.CVE_EMPRESA, \n"+
						"PC.ID_PRODUCTO, \n"+
						"P.NOM_PRODUCTO, \n"+
						"P.APLICA_A, \n"+
						"P.ID_PERIODICIDAD, \n"+
						"P.CVE_METODO, \n"+
						"PC.NUM_CICLO + '"+iFactor+"' NUM_CICLO, \n"+
						"PC.ID_FORMA_DISTRIBUCION, \n"+
						"PC.PLAZO, \n"+
						"PC.TIPO_TASA, \n"+
						"PC.VALOR_TASA, \n"+
						"PC.ID_PERIODICIDAD_TASA, \n"+
						"PC.ID_TASA_REFERENCIA, \n"+
						"PC.MONTO_MAXIMO, \n"+
						"P.MONTO_MINIMO, \n"+
						"PC.PORC_FLUJO_CAJA, \n"+
						"P.FECHA_INICIO_ACTIVACION, \n"+
						"P.FECHA_FIN_ACTIVACION \n"+
						"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
						"SIM_PRODUCTO P, \n"+
						"SIM_PRODUCTO_SUCURSAL PS \n"+
						"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND PC.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
						"AND PC.NUM_CICLO = (SELECT \n"+
						"MAX(NUM_CICLO) \n"+
						"FROM \n"+
						"SIM_PRODUCTO_CICLO \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
						") \n"+
						"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
						"AND P.APLICA_A = 'Individual' \n"+
						"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
						"UNION \n"+
						"SELECT DISTINCT \n"+
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
						"P.MONTO_MINIMO, \n"+
						"PC.PORC_FLUJO_CAJA, \n"+
						"P.FECHA_INICIO_ACTIVACION, \n"+
						"P.FECHA_FIN_ACTIVACION \n"+
						"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
						"SIM_PRODUCTO P, \n"+
						"SIM_PRODUCTO_SUCURSAL PS \n"+
						"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
						"AND PC.NUM_CICLO = '1'	\n"+
						"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
						"AND P.APLICA_A = 'Individual' \n"+
						"MINUS \n"+
						"SELECT DISTINCT \n"+
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
						"P.MONTO_MINIMO, \n"+
						"PC.PORC_FLUJO_CAJA, \n"+
						"P.FECHA_INICIO_ACTIVACION, \n"+
						"P.FECHA_FIN_ACTIVACION \n"+
						"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
						"SIM_PRODUCTO P, \n"+
						"SIM_PRODUCTO_SUCURSAL PS \n"+
						"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND PC.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
						"AND PC.NUM_CICLO = '1' \n"+
						"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
						"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
						"AND P.APLICA_A = 'Individual' \n";
					
				}
						
					}else {
						//Como si existe el siguiente se muestra en la lista junto con los productos-ciclo 1's.
						sSql = "SELECT DISTINCT \n"+
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
						"P.MONTO_MINIMO, \n"+
						"PC.PORC_FLUJO_CAJA, \n"+
						"P.FECHA_INICIO_ACTIVACION, \n"+
						"P.FECHA_FIN_ACTIVACION \n"+
						"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
						"SIM_PRODUCTO P, \n"+
						"SIM_PRODUCTO_SUCURSAL PS \n"+
						"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
						"AND PC.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+	
						"AND PC.NUM_CICLO = '" + (String)parametros.getDefCampo("NUM_CICLO") + "' + '1' \n"+	
						"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
						"AND P.APLICA_A = 'Individual' \n"+
						"UNION \n"+
						"SELECT DISTINCT \n"+
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
						"P.MONTO_MINIMO, \n"+
						"PC.PORC_FLUJO_CAJA, \n"+
						"P.FECHA_INICIO_ACTIVACION, \n"+
						"P.FECHA_FIN_ACTIVACION \n"+
						"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
						"SIM_PRODUCTO P, \n"+
						"SIM_PRODUCTO_SUCURSAL PS \n"+
						"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
						"AND PC.NUM_CICLO = '1'	\n"+
						"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
						"AND P.APLICA_A = 'Individual' \n"+
						"MINUS \n"+
						"SELECT DISTINCT \n"+
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
						"P.MONTO_MINIMO, \n"+
						"PC.PORC_FLUJO_CAJA, \n"+
						"P.FECHA_INICIO_ACTIVACION, \n"+
						"P.FECHA_FIN_ACTIVACION \n"+
						"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
						"SIM_PRODUCTO P, \n"+
						"SIM_PRODUCTO_SUCURSAL PS \n"+
						"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND PC.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
						"AND PC.NUM_CICLO = '1' \n"+
						"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
						"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
						"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
						"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
						"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
						"AND P.APLICA_A = 'Individual' \n";
					}
				}
				
				else {
			
			
			
			//Obtiene el ciclo 1 de todos los productos existentes tomando en cuenta las fechas de activación.
			sSql = "SELECT DISTINCT \n"+
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
				"P.MONTO_MINIMO, \n"+
				"PC.PORC_FLUJO_CAJA, \n"+
				"P.FECHA_INICIO_ACTIVACION, \n"+
				"P.FECHA_FIN_ACTIVACION \n"+
				"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
				"SIM_PRODUCTO P, \n"+
				"SIM_PRODUCTO_SUCURSAL PS \n"+
				"WHERE PC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND PC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
				"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
				"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
				"AND PC.NUM_CICLO = '1'	\n"+
				"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
				"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
				"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
				"AND PS.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND P.APLICA_A = 'Individual' \n";
				}	
		}
		
		ejecutaSql();
		return getConsultaLista();
	}
}
