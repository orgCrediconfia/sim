/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para obtenr el identificador del
 * pr�stamo en base a su clave.
 */

public class SimParticipanteCDAO extends Conexion2 implements
		OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de b�squeda.
	 * 
	 * @param parametros
	 *            Par�metros que se le env�an a la consulta para obtener el
	 *            conjunto de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException
	 *             Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException {

		sSql = "SELECT GP.NOM_COMPLETO,\n"
				+ "PP.CVE_TIPO_PERSONA,\n"
				+ "GP.ID_PERSONA,\n"
				+ "GD.CALLE\n"
				+ "||', '\n"
				+ "||GD.NUMERO_EXT\n"
				+ "||', '\n"
				+ "||GD.NUMERO_INT\n"
				+ "||', '\n"
				+ "||GD.NOM_ASENTAMIENTO\n"
				+ "||', '\n"
				+ "||GD.NOM_DELEGACION\n"
				+ "||', '\n"
				+ "||GD.NOM_CIUDAD\n"
				+ "||', '\n"
				+ "||GD.NOM_ESTADO\n"
				+ "||'. CP: '\n"
				+ "||GD.CODIGO_POSTAL AS DIRECCION\n"
				+ "FROM  SIM_PRESTAMO_PARTICIPANTE PP,\n"
				+ "RS_GRAL_PERSONA GP,\n"
				+ "RS_GRAL_DOMICILIO GD\n"
				+ "WHERE PP.CVE_GPO_EMPRESA        = '"
				+ (String) parametros.getDefCampo("CVE_GPO_EMPRESA")
				+ "'\n"
				+ "AND PP.CVE_EMPRESA            = '"
				+ (String) parametros.getDefCampo("CVE_EMPRESA")
				+ "'\n"
				+ "AND PP.ID_PRESTAMO            = '"
				+ (String) parametros.getDefCampo("ID_PRESTAMO")
				+ "'\n"
				+ "AND (PP.CVE_TIPO_PERSONA      = 'OBLIGADO' OR PP.CVE_TIPO_PERSONA = 'OBLIGADO 2' OR PP.CVE_TIPO_PERSONA = 'GARANTE')\n"
				+ "AND GP.CVE_GPO_EMPRESA        = PP.CVE_GPO_EMPRESA\n"
				+ "AND GP.CVE_EMPRESA            = PP.CVE_EMPRESA\n"
				+ "AND GP.ID_PERSONA             = PP.ID_PERSONA\n"
				+ "AND GD.CVE_GPO_EMPRESA(+)        = GP.CVE_GPO_EMPRESA\n"
				+ "AND GD.CVE_EMPRESA(+)            = GP.CVE_EMPRESA\n"
				+ "AND GD.IDENTIFICADOR(+)          = GP.ID_PERSONA\n"
				+ "AND GD.CVE_TIPO_IDENTIFICADOR(+) = 'CLIENTE'\n";

		ejecutaSql();
		return getConsultaLista();
	}

}