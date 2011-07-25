/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.producto.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para los cargo o comisiones de un producto.
 */
 
public class SimProductoCargoComisionDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
				"PC.CVE_GPO_EMPRESA, \n" +
				"PC.CVE_EMPRESA, \n" +
				"PC.ID_CARGO_COMISION, \n"+
				"A.NOM_ACCESORIO, \n"+
				"PC.ID_PRODUCTO, \n"+
				"PC.ID_FORMA_APLICACION, \n" +
				"F.NOM_FORMA_APLICACION, \n" +
				"PC.CARGO_INICIAL, \n" +
				"PC.PORCENTAJE_MONTO, \n"+
				"PC.CANTIDAD_FIJA, \n" +
				"PC.VALOR, \n" +
				"PC.ID_UNIDAD, \n"+
				"U.NOM_UNIDAD, \n"+
				"PC.ID_PERIODICIDAD, \n" +
				"P.NOM_PERIODICIDAD \n"+
			"FROM SIM_PRODUCTO_CARGO_COMISION PC, \n"+
			"     SIM_CAT_ACCESORIO A, \n"+
			"     SIM_CAT_FORMA_APLICACION F, \n"+
			"	  SIM_CAT_UNIDAD U, \n"+
			"	  SIM_CAT_PERIODICIDAD P \n"+
			" WHERE PC.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND PC.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND PC.ID_PRODUCTO ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
			" AND A.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
			" AND A.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
			" AND A.ID_ACCESORIO = PC.ID_CARGO_COMISION \n"+
			" AND F.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
			" AND F.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
			" AND F.ID_FORMA_APLICACION = PC.ID_FORMA_APLICACION \n"+
			" AND U.CVE_GPO_EMPRESA (+)= PC.CVE_GPO_EMPRESA \n"+
			" AND U.CVE_EMPRESA (+)= PC.CVE_EMPRESA \n"+
			" AND U.ID_UNIDAD (+)= PC.ID_UNIDAD \n"+
			" AND P.CVE_GPO_EMPRESA (+)= PC.CVE_GPO_EMPRESA \n"+
			" AND P.CVE_EMPRESA (+)= PC.CVE_EMPRESA \n"+
			" AND P.ID_PERIODICIDAD (+)= PC.ID_PERIODICIDAD \n";
		
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  "SELECT \n"+
				"PC.CVE_GPO_EMPRESA, \n" +
				"PC.CVE_EMPRESA, \n" +
				"PC.ID_CARGO_COMISION, \n"+
				"A.NOM_ACCESORIO, \n"+
				"PC.ID_PRODUCTO, \n"+
				"PC.ID_FORMA_APLICACION, \n" +
				"F.NOM_FORMA_APLICACION, \n" +
				"PC.CARGO_INICIAL, \n" +
				"PC.PORCENTAJE_MONTO, \n"+
				"PC.CANTIDAD_FIJA, \n" +
				"PC.VALOR, \n" +
				"PC.ID_UNIDAD, \n"+
				"PC.ID_PERIODICIDAD \n" +
			"FROM SIM_PRODUCTO_CARGO_COMISION PC, \n"+
			"     SIM_CAT_ACCESORIO A, \n"+
			"     SIM_CAT_FORMA_APLICACION F \n"+
			" WHERE PC.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND PC.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND PC.ID_PRODUCTO ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
			" AND PC.ID_CARGO_COMISION ='" + (String)parametros.getDefCampo("ID_CARGO_COMISION") + "' \n"+
			" AND A.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
			" AND A.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
			" AND A.ID_ACCESORIO = PC.ID_CARGO_COMISION \n"+
			" AND F.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
			" AND F.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
			" AND F.ID_FORMA_APLICACION = PC.ID_FORMA_APLICACION \n";	
				
			   
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		ResultadoCatalogo resultadoCatalogoProductoAccesorioCiclo = new  ResultadoCatalogo();
		SimProductoAccesorioCicloDAO ProductoAccesorioCicloDAO = new SimProductoAccesorioCicloDAO();
		
		String sNumCiclo = "";
		String sAccesorio = "";
		
		sSql =  "INSERT INTO SIM_PRODUCTO_CARGO_COMISION ( \n"+
			"	CVE_GPO_EMPRESA, \n" +
			"	CVE_EMPRESA, \n" +
			"	ID_CARGO_COMISION, \n"+
			"	ID_PRODUCTO, \n"+
			"	ID_FORMA_APLICACION, \n" +
			"	CARGO_INICIAL, \n" +
			"	PORCENTAJE_MONTO, \n"+
			"	CANTIDAD_FIJA, \n" +
			"	VALOR, \n" +
			"	ID_UNIDAD, \n"+
			"	ID_PERIODICIDAD) \n" +
			"VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
			"'" + (String)registro.getDefCampo("FORMA_APLICACION") + "', \n" +
			"'" + (String)registro.getDefCampo("CARGO_INICIAL") + "', \n" +
			"'" + (String)registro.getDefCampo("PORCENTAJE_MONTO") + "', \n" +
			"'" + (String)registro.getDefCampo("CANTIDAD_FIJA") + "', \n" +
			"'" + (String)registro.getDefCampo("VALOR") + "', \n" +
			"'" + (String)registro.getDefCampo("UNIDAD") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "') \n" ;
				
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql =  "SELECT  \n"+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"ID_ACCESORIO, \n" +
			"B_ACCESORIO \n"+
			"FROM SIM_CAT_ACCESORIO \n" +
			"WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND ID_ACCESORIO ='" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "' \n";
				
		ejecutaSql();
		if (rs.next()){
					
			sAccesorio = rs.getString("B_ACCESORIO");
					
				if (sAccesorio.equals("V")){
					
					sSql =  "SELECT  \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_PRODUCTO, \n"+
						"NUM_CICLO \n" +
						"FROM SIM_PRODUCTO_CICLO \n" +
						"WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRODUCTO ='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
		
					ejecutaSql();		
						
					ProductoAccesorioCicloDAO.setConexion(this.getConexion());
					
					while (rs.next()){
						sNumCiclo = rs.getString("NUM_CICLO");
						registro.addDefCampo("NUM_CICLO",sNumCiclo);
						resultadoCatalogoProductoAccesorioCiclo = ProductoAccesorioCicloDAO.alta(registro);
					}			
				}
		}
		
		ProductoAccesorioCicloDAO.cierraConexion();
		return resultadoCatalogo;
	}
	
	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new 	ResultadoCatalogo();
		sSql =  " UPDATE SIM_PRODUCTO_CARGO_COMISION SET "+
			" ID_FORMA_APLICACION     		= '" + (String)registro.getDefCampo("FORMA_APLICACION")  + "', \n" +
			" CARGO_INICIAL     		= '" + (String)registro.getDefCampo("CARGO_INICIAL")  + "', \n" +
			" PORCENTAJE_MONTO              = '" + (String)registro.getDefCampo("PORCENTAJE_MONTO")  + "', \n" +
			" CANTIDAD_FIJA     		= '" + (String)registro.getDefCampo("CANTIDAD_FIJA")  + "', \n" +
			" VALOR     			= '" + (String)registro.getDefCampo("VALOR")  + "', \n" +
			" ID_UNIDAD                     = '" + (String)registro.getDefCampo("UNIDAD")  + "', \n" +
			" ID_PERIODICIDAD     		= '" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "' \n" +
			" WHERE ID_PRODUCTO      	= '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
			" AND ID_CARGO_COMISION   	= '" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "' \n" +
			" AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
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
		//BORRA EL REGISTRO
		sSql =  " DELETE FROM SIM_PRODUCTO_CARGO_COMISION" +
			" WHERE ID_PRODUCTO		='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
			" AND ID_CARGO_COMISION		='" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "' \n"+
			" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		if (!registro.getDefCampo("ID_CARGO_COMISION").equals("9") & !registro.getDefCampo("ID_CARGO_COMISION").equals("10")){
			sSql =  " DELETE FROM SIM_PRODUCTO_CICLO_ACCESORIO" +
				" WHERE ID_PRODUCTO		='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
				" AND ID_ACCESORIO		='" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "' \n"+
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		}

		return resultadoCatalogo;
	}

}