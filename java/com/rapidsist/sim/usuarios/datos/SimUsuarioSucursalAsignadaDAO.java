/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.usuarios.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para las sucursales asignadas al usuario.
 */
 
public class SimUsuarioSucursalAsignadaDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
			"S.CVE_GPO_EMPRESA, \n"+
			"S.CVE_EMPRESA, \n"+
			"S.ID_SUCURSAL, \n"+ 
			"S.CVE_USUARIO, \n"+
			"CS.NOM_SUCURSAL \n"+
			"FROM SIM_USUARIO_SUCURSAL S, \n"+
			"     SIM_CAT_SUCURSAL CS \n"+
			"WHERE S.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND S.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND S.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
			"AND CS.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+
			"AND CS.CVE_EMPRESA = S.CVE_EMPRESA \n"+
			"AND CS.ID_SUCURSAL = S.ID_SUCURSAL \n";
			
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
		"US.CVE_GPO_EMPRESA, \n"+
		"US.CVE_EMPRESA, \n"+
		"US.CVE_USUARIO, \n"+	
		"US.ID_SUCURSAL, \n"+ 
		"CS.NOM_SUCURSAL \n"+
    "FROM  SIM_USUARIO_SUCURSAL US, \n"+
	"	   RS_GRAL_USUARIO GU, \n"+
	"	   SIM_CAT_SUCURSAL CS \n"+
	" WHERE US.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
	" AND US.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
	//" AND UR.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
	" AND US.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
	
	
		" AND GU.CVE_GPO_EMPRESA = US.CVE_GPO_EMPRESA \n"+
		" AND GU.CVE_EMPRESA = US.CVE_EMPRESA  \n"+
		" AND GU.CVE_USUARIO =  US.CVE_USUARIO \n";
		
		/*" AND UR.CVE_GPO_EMPRESA = CR.CVE_GPO_EMPRESA \n"+
		" AND UR.CVE_EMPRESA = CR.CVE_EMPRESA  \n"+
		" AND UR.ID_REGIONAL =  CR.ID_REGIONAL \n";*/
		
				 
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		
		sSql =  "INSERT INTO SIM_USUARIO_SUCURSAL ( \n"+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"CVE_USUARIO, \n" +
			"ID_SUCURSAL) \n" +
		        "VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "') \n" ;

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		return resultadoCatalogo;
	}

	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA LA FUNCION
		sSql = "DELETE FROM SIM_USUARIO_SUCURSAL " +
			" WHERE ID_SUCURSAL			='" + (String)registro.getDefCampo("ID_SUCURSAL") + "' \n" +
			" AND CVE_USUARIO			='" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
			" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}