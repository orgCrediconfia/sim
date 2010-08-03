/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

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

/**
 * Administra los accesos a la base de datos para el catálogo de actividades o requisitos.
 */
 
public class SimCatalogoEtapaActividadDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  " SELECT \n"+
			" 	EA.CVE_GPO_EMPRESA, \n"+
			" 	EA.CVE_EMPRESA, \n"+
			" 	EA.ID_ACTIVIDAD_REQUISITO, \n"+
			" 	EA.ID_ETAPA_PRESTAMO, \n"+
			" 	CAR.NOM_ACTIVIDAD_REQUISITO, \n"+
			" 	CAR.DESCRIPCION, \n"+
			" 	CAR.APLICA_A, \n"+
			" 	CAR.TIPO, \n"+
			" 	CAR.APLICA_CICLO, \n"+
			" 	CEP.NOM_ESTATUS_PRESTAMO \n"+
			" FROM SIM_ESTATUS_ACTIVIDAD EA, \n"+
			"      SIM_CAT_ACTIVIDAD_REQUISITO CAR, \n"+
			"      SIM_CAT_ETAPA_PRESTAMO CEP \n"+
			" WHERE EA.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND EA.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND EA.ID_ETAPA_PRESTAMO ='" + (String)parametros.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
			" AND CAR.CVE_GPO_EMPRESA = EA.CVE_GPO_EMPRESA \n"+
			" AND CAR.CVE_EMPRESA = EA.CVE_EMPRESA \n"+
			" AND CAR.ID_ACTIVIDAD_REQUISITO = EA.ID_ACTIVIDAD_REQUISITO \n"+
			" AND CEP.CVE_GPO_EMPRESA = EA.CVE_GPO_EMPRESA \n"+
			" AND CEP.CVE_EMPRESA = EA.CVE_EMPRESA \n"+
			" AND CEP.ID_ETAPA_PRESTAMO = EA.ID_ETAPA_PRESTAMO \n";
			   
		sSql = sSql + " ORDER BY EA.ID_ACTIVIDAD_REQUISITO \n";
				
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
		sSql =  " SELECT \n"+
			" 	CVE_GPO_EMPRESA, \n"+
			" 	CVE_EMPRESA, \n"+
			" 	ID_ACTIVIDAD_REQUISITO, \n"+
			" 	NOM_ACTIVIDAD_REQUISITO, \n"+
			" 	DESCRIPCION, \n"+
			" 	APLICA_A, \n"+
			" 	TIPO, \n"+
			" 	APLICA_CICLO \n"+
			" FROM SIM_CAT_ACTIVIDAD_REQUISITO \n"+
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_ACTIVIDAD_REQUISITO = '" + (String)parametros.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n";
			   
			   
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
	
		sSql =  "INSERT INTO SIM_ESTATUS_ACTIVIDAD ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_ETAPA_PRESTAMO, \n"+
				"ID_ACTIVIDAD_REQUISITO) \n"+
			"VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "') \n" ;
			
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
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
		ResultadoCatalogo resultadoCatalogo = new 	ResultadoCatalogo();
		sSql =  " UPDATE SIM_CAT_ACTIVIDAD_REQUISITO SET "+
			" NOM_ACTIVIDAD_REQUISITO     	='" + (String)registro.getDefCampo("NOM_ACTIVIDAD_REQUISITO")  + "', \n" +
			" DESCRIPCION     		='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
			" APLICA_A     			='" + (String)registro.getDefCampo("APLICA_A")  + "', \n" +
			" TIPO     			='" + (String)registro.getDefCampo("TIPO")  + "', \n" +
			" APLICA_CICLO  		='" + (String)registro.getDefCampo("APLICA_CICLO")  + "' \n" +
			" WHERE ID_ACTIVIDAD_REQUISITO  ='" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n" +
			" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
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
		//BORRA LA FUNCION
		sSql =  " DELETE FROM SIM_ESTATUS_ACTIVIDAD " +
	 	   	" WHERE ID_ACTIVIDAD_REQUISITO  ='" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n" +
	 	   	" AND ID_ETAPA_PRESTAMO  ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
		  	" AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
		   	" AND CVE_EMPRESA  ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}