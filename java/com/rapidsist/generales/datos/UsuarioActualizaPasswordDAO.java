/**
 * Sistema de administración de mensajería.
 * 
 * Copyright (c) 2005 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.generales.datos;

import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.CallableStatement;

/**
 * Procesa las productos
 */
public class UsuarioActualizaPasswordDAO extends Conexion2 implements OperacionModificacion {
	
	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo= new ResultadoCatalogo();
		
		sSql = "UPDATE RS_GRAL_USUARIO SET \n"+ 
				"PASSWORD = '" + (String)registro.getDefCampo("PASSWORD_NUEVO") + "'\n" +
				"WHERE CVE_USUARIO = '" + (String)registro.getDefCampo("CVE_USUARIO") + "'\n" +
				"AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "'\n" +
				"AND PASSWORD = '" + (String)registro.getDefCampo("PASSWORD_ACTUAL") + "'\n" ;	
		if (ejecutaUpdate() == 0){
			//NO ACTUALIZO EL REGISTRO
			resultadoCatalogo.mensaje.setTipo("Aviso");
			resultadoCatalogo.mensaje.setClave("Aviso");
			resultadoCatalogo.mensaje.setDescripcion("El password actual es incorrecto, no se realizó el cambio de password.");
		}
		else{
			//ACTUALIZO EL REGISTRO
			resultadoCatalogo.mensaje.setTipo("Aviso");
			resultadoCatalogo.mensaje.setClave("Aviso");
			resultadoCatalogo.mensaje.setDescripcion("La operación fue exitosa, se realizó el cambio de password.");
		}

		return resultadoCatalogo;
	}
}
