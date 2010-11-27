/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.comite.datos;

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
import java.sql.CallableStatement;
/**
 * Administra los accesos a la base de datos para comites.
 */
 
public class SimGenerarTablaAmortizacionDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

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
			   "C.CVE_GPO_EMPRESA, \n" +
			   "C.CVE_EMPRESA, \n" +
			   "C.ID_COMITE, \n"+
			   "C.NOM_COMITE, \n"+
			   "C.ID_SUCURSAL, \n"+
			   "S.NOM_SUCURSAL, \n"+
			   "C.FECHA \n" +	
			" FROM SIM_COMITE C, \n"+
			"      SIM_CAT_SUCURSAL S \n"+
			" WHERE C.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND C.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND S.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			" AND S.CVE_EMPRESA = C.CVE_EMPRESA \n"+
			" AND S.ID_SUCURSAL = C.ID_SUCURSAL \n";
			
		if (parametros.getDefCampo("ID_COMITE") != null) {
			sSql = sSql + " AND C.ID_COMITE = '" + (String) parametros.getDefCampo("ID_COMITE") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_COMITE") != null) {
			sSql = sSql + " AND UPPER(C.NOM_COMITE) LIKE'%" + ((String) parametros.getDefCampo("NOM_COMITE")).toUpperCase()  + "%' \n";
		}
		
		if (parametros.getDefCampo("ID_SUCURSAL") != null) {
			sSql = sSql + " AND C.ID_SUCURSAL = '" + (String) parametros.getDefCampo("ID_SUCURSAL") + "' \n";
		}
		
		if (parametros.getDefCampo("FECHA") != null) {
			sSql = sSql + " AND C.FECHA >= TO_DATE('" + (String) parametros.getDefCampo("FECHA") + "','DD/MM/YYYY') \n";
		}
		
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
			   "CVE_GPO_EMPRESA, \n" +
			   "CVE_EMPRESA, \n" +
			   "ID_COMITE, \n"+
			   "ID_SUCURSAL, \n"+
			   "NOM_COMITE, \n"+
			   "FECHA \n" +	
			" FROM SIM_COMITE \n"+
			" WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_COMITE = '" + (String)parametros.getDefCampo("ID_COMITE") + "' \n";
			
			
		
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
		String sTxrespuesta = "";

		String sCveGpoEmpresa = (String)registro.getDefCampo("CVE_GPO_EMPRESA");
		String sCveEmpresa = (String)registro.getDefCampo("CVE_EMPRESA");
		String sIdEmpresa = (String)registro.getDefCampo("ID_PRESTAMO");
		
		CallableStatement sto = conn.prepareCall("begin PKG_ADMINISTRADOR.pGeneraTablaAmortizacion(?,?,?,?); end;");
		sto.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
		sto.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
		sto.setString(3, (String)registro.getDefCampo("ID_PRESTAMO"));
		sto.registerOutParameter(4, java.sql.Types.VARCHAR);

		//EJECUTA EL PROCEDIMIENTO ALMACENADO
		sto.execute();
		sTxrespuesta  = sto.getString(4);
		sto.close();
		
		// SE AGREGA LA RESPUESTA
		System.out.println("sTxrespuesta"+sTxrespuesta);
		registro.addDefCampo("RESPUESTA" ,sTxrespuesta);
		resultadoCatalogo.Resultado = registro;
		
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
		String sTxrespuesta = "";

		CallableStatement sto = conn.prepareCall("begin pkg_administrador.pGeneraTablaAmortizacion(?,?,?,?); end;");
		sto.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
		sto.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
		sto.setString(3, (String)registro.getDefCampo("ID_PRESTAMO"));
		sto.registerOutParameter(4, java.sql.Types.VARCHAR);

		//EJECUTA EL PROCEDIMIENTO ALMACENADO
		sto.execute();
		sTxrespuesta  = sto.getString(4);
		sto.close();
		
		// SE AGREGA LA RESPUESTA
		registro.addDefCampo("RESPUESTA" ,sTxrespuesta);
		resultadoCatalogo.Resultado = registro;
		
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
		sSql = "DELETE FROM SIM_COMITE" +
				" WHERE ID_COMITE		='" + (String)registro.getDefCampo("ID_COMITE") + "' \n" +
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}