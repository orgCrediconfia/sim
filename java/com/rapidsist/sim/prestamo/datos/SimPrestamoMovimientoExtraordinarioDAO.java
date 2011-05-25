/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.sql.SQLException;
import com.rapidsist.prestamo.MovimientosExtraordinarios;



/**
 * Administra los accesos a la base de datos para generar las tablas de amortizaciòn.
 */
 
public class SimPrestamoMovimientoExtraordinarioDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta, OperacionConsultaRegistro {


	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql = "SELECT \n"+
				"V.CVE_GPO_EMPRESA, \n"+
				"V.CVE_EMPRESA, \n"+
				"V.ID_PRESTAMO, \n"+
				"V.CVE_PRESTAMO, \n"+ 
				"V.APLICA_A, \n"+ 
				"V.NOMBRE, \n"+
				"V.NOM_PRODUCTO, \n"+
				"V.NUM_CICLO, \n"+
				"V.ID_ETAPA_PRESTAMO, \n"+
				"E.NOM_ESTATUS_PRESTAMO \n"+
				"FROM V_CREDITO V, \n"+
				"SIM_CAT_ETAPA_PRESTAMO E, \n"+
				"SIM_USUARIO_ACCESO_SUCURSAL US  \n"+
				"WHERE V.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND V.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND E.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n"+
				"AND E.CVE_EMPRESA = V.CVE_EMPRESA \n"+
				"AND E.ID_ETAPA_PRESTAMO = V.ID_ETAPA_PRESTAMO \n"+
				"AND US.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n"+
				"AND US.CVE_EMPRESA = V.CVE_EMPRESA \n"+
				"AND US.ID_SUCURSAL = V.ID_SUCURSAL \n"+
				"AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
				"AND V.APLICA_A != 'INDIVIDUAL_GRUPO' \n";
				
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + " AND V.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + " AND UPPER(V.NOMBRE) LIKE'%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase()  + "%' \n";
		}
		
		sSql = sSql + " ORDER BY V.CVE_PRESTAMO \n";
			
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
		sSql = "SELECT \n"+
				"V.CVE_GPO_EMPRESA, \n"+
				"V.CVE_EMPRESA, \n"+
				"V.ID_PRESTAMO, \n"+
				"V.CVE_PRESTAMO, \n"+ 
				"V.APLICA_A, \n"+ 
				"V.NOMBRE, \n"+
				"V.NOM_PRODUCTO, \n"+
				"V.NUM_CICLO, \n"+
				"V.ID_ETAPA_PRESTAMO, \n"+
				"E.NOM_ESTATUS_PRESTAMO \n"+
				"FROM V_CREDITO V, \n"+
				"SIM_CAT_ETAPA_PRESTAMO E, \n"+
				"SIM_USUARIO_ACCESO_SUCURSAL US  \n"+
				"WHERE V.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND V.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND V.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND E.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n"+
				"AND E.CVE_EMPRESA = V.CVE_EMPRESA \n"+
				"AND E.ID_ETAPA_PRESTAMO = V.ID_ETAPA_PRESTAMO \n"+
				"AND US.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n"+
				"AND US.CVE_EMPRESA = V.CVE_EMPRESA \n"+
				"AND US.ID_SUCURSAL = V.ID_SUCURSAL \n"+
				"AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
				"AND V.APLICA_A != 'INDIVIDUAL_GRUPO' \n";
			   
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
		resultadoCatalogo.Resultado = new Registro();
		
		Object[] MovtosExtraObjetos = (Object[])registro.getDefCampo("MovExtraObjetos");
		
		MovimientosExtraordinarios movto = new MovimientosExtraordinarios();
		try{
			movto.doMovimientosExtraordinarios((String)registro.getDefCampo("CVE_GPO_EMPRESA"), (String)registro.getDefCampo("CVE_EMPRESA"), (String)registro.getDefCampo("CVE_OPERACION"), (String)registro.getDefCampo("CVE_USUARIO"), MovtosExtraObjetos, conn);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			System.out.println(":'(");
		}
		
		return resultadoCatalogo;
	}
}