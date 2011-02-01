/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.parametroglobal.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para los parámetro globales del sistema.
 */
 
public class SimParametroGlobalDAO extends Conexion2 implements OperacionConsultaRegistro, OperacionModificacion {

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		
			sSql =  "SELECT \n"+
				"	PG.CVE_GPO_EMPRESA, \n"+
				"	PG.CVE_EMPRESA, \n"+
				"	PG.PORC_INT_FUNDADORES, \n"+
				"	PG.IMP_DEUDA_MINIMA, \n"+
				"	PG.IMP_VAR_PROPORCION, \n"+
				"	PG.CREDITOS_SIMULTANEOS, \n"+
				"	P.B_OPERA_SABADO, \n"+
				"	P.B_OPERA_DOMINGO, \n"+
				"	P.B_OPERA_DIA_FESTIVO \n"+
				"FROM SIM_PARAMETRO_GLOBAL PG, \n"+
				"	  PFIN_PARAMETRO P \n"+
				"WHERE PG.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND PG.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n" +
				"AND P.CVE_EMPRESA = PG.CVE_EMPRESA \n" ;
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql =  " UPDATE SIM_PARAMETRO_GLOBAL SET "+
			" PORC_INT_FUNDADORES	 = '" + (String)registro.getDefCampo("PORC_INT_FUNDADORES")  + "', \n" +
			" IMP_DEUDA_MINIMA	 	 = '" + (String)registro.getDefCampo("IMP_DEUDA_MINIMA")  + "', \n" +
			" CREDITOS_SIMULTANEOS	 = '" + (String)registro.getDefCampo("CREDITOS_SIMULTANEOS")  + "', \n" +
			" IMP_VAR_PROPORCION	 = '" + (String)registro.getDefCampo("IMP_VAR_PROPORCION")  + "' \n" +
			" WHERE CVE_EMPRESA    	 = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
			" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql =  " UPDATE PFIN_PARAMETRO SET "+
			" B_OPERA_SABADO	 = '" + (String)registro.getDefCampo("B_OPERA_SABADO")  + "', \n" +
			" B_OPERA_DOMINGO	 	 = '" + (String)registro.getDefCampo("B_OPERA_DOMINGO")  + "', \n" +
			" B_OPERA_DIA_FESTIVO	 = '" + (String)registro.getDefCampo("B_OPERA_DIA_FESTIVO")  + "' \n" +
			" WHERE CVE_EMPRESA    	 = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
			" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}