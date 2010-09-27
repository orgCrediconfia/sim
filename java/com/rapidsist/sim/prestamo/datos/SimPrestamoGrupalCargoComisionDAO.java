/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para los cargos y comisiones del préstamo.
 */
 
public class SimPrestamoGrupalCargoComisionDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		sSql =  " SELECT \n"+
				" 	PG.CVE_GPO_EMPRESA, \n"+
				" 	PG.CVE_EMPRESA, \n"+
				" 	PG.ID_CARGO_COMISION, \n"+
				" 	PG.ID_PRESTAMO_GRUPO, \n"+
				" 	C.NOM_ACCESORIO, \n"+
				" 	PG.ID_FORMA_APLICACION, \n"+
				" 	F.NOM_FORMA_APLICACION, \n"+
				" 	PG.CARGO_INICIAL, \n"+
				" 	PG.PORCENTAJE_MONTO, \n"+
				" 	PG.CANTIDAD_FIJA, \n"+
				" 	PG.VALOR, \n"+
				" 	PG.ID_UNIDAD, \n"+
				" 	U.NOM_UNIDAD, \n"+
				" 	PG.ID_PERIODICIDAD, \n"+
				" 	P.NOM_PERIODICIDAD \n"+
				" FROM SIM_PRESTAMO_GPO_CARGO PG, \n"+
				"      SIM_CAT_ACCESORIO C, \n"+
				"      SIM_CAT_FORMA_APLICACION F, \n"+
				"      SIM_CAT_UNIDAD U, \n"+
				"      SIM_CAT_PERIODICIDAD P \n"+
				" WHERE PG.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND PG.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND PG.ID_PRESTAMO_GRUPO ='" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
				" AND C.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n" +
				" AND C.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				" AND C.ID_ACCESORIO = PG.ID_CARGO_COMISION \n"+
				" AND F.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n" +
				" AND F.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				" AND F.ID_FORMA_APLICACION = PG.ID_FORMA_APLICACION \n"+
				" AND U.CVE_GPO_EMPRESA (+)= PG.CVE_GPO_EMPRESA \n" +
				" AND U.CVE_EMPRESA (+)= PG.CVE_EMPRESA \n"+
				" AND U.ID_UNIDAD (+)= PG.ID_UNIDAD \n"+
				" AND P.CVE_GPO_EMPRESA (+)= PG.CVE_GPO_EMPRESA \n" +
				" AND P.CVE_EMPRESA (+)= PG.CVE_EMPRESA \n"+
				" AND P.ID_PERIODICIDAD (+)= PG.ID_UNIDAD \n";
		
		sSql = sSql + " ORDER BY ID_CARGO_COMISION \n";
		
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
			" 	PAR.CVE_GPO_EMPRESA, \n"+
			" 	PAR.CVE_EMPRESA, \n"+
			" 	PAR.ID_ACTIVIDAD_REQUISITO, \n"+
			" 	PAR.ID_PRESTAMO, \n"+
			" 	PAR.FECHA_REGISTRO, \n"+
			" 	PAR.FECHA_REALIZADA, \n"+
			" 	PAR.ESTATUS, \n"+
			" 	PAR.COMENTARIO, \n"+
			" 	C.NOM_ACTIVIDAD_REQUISITO, \n"+
			" 	PR.ID_ETAPA_PRESTAMO \n"+
			" FROM SIM_PRESTAMO_ETAPA PAR, \n"+
			"      SIM_CAT_ACTIVIDAD_REQUISITO C, \n"+
			"      SIM_PRODUCTO_ETAPA_PRESTAMO PR \n"+
			" WHERE PAR.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND PAR.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND PAR.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND C.CVE_GPO_EMPRESA = PAR.CVE_GPO_EMPRESA \n" +
			" AND C.CVE_EMPRESA = PAR.CVE_EMPRESA \n"+
			" AND C.ID_ACTIVIDAD_REQUISITO = PAR.ID_ACTIVIDAD_REQUISITO \n"+
			" AND PR.CVE_GPO_EMPRESA = PAR.CVE_GPO_EMPRESA \n" +
			" AND PR.CVE_EMPRESA = PAR.CVE_EMPRESA \n"+
			" AND PR.ID_ACTIVIDAD_REQUISITO = PAR.ID_ACTIVIDAD_REQUISITO \n";
			//" AND PR.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n";
			
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
		
		String sCargoC = new String();
		String sForma = new String();
		String sCargoI = new String();
		String sPorcentaje = new String();
		String sCantidad = new String();
		String sValorT = new String();
		String sUnidad = new String();
		String sPeriodicidad = new String();
		
		String[] sIdCargoComision = (String[]) registro.getDefCampo("DAO_ID_CARGO_COMISION");
		String[] sIdFormaAplicacion = (String[]) registro.getDefCampo("DAO_ID_FORMA_APLICACION");
		String[] sCargoInicial = (String[]) registro.getDefCampo("DAO_CARGO_INICIAL");
		String[] sPorcentajeMonto = (String[]) registro.getDefCampo("DAO_PORCENTAJE_MONTO");
		String[] sCantidadFija = (String[]) registro.getDefCampo("DAO_CANTIDAD_FIJA");
		String[] sValor = (String[]) registro.getDefCampo("DAO_VALOR");
		String[] sIdUnidad = (String[]) registro.getDefCampo("DAO_ID_UNIDAD");
		String[] sIdPeriodicidad = (String[]) registro.getDefCampo("DAO_ID_PERIODICIDAD");
		
		if (sIdCargoComision != null) {
			for (int iNumParametro = 0; iNumParametro < sIdCargoComision.length; iNumParametro++) {
				sCargoC = sIdCargoComision[iNumParametro];
				sForma = sIdFormaAplicacion[iNumParametro];
				sCargoI = sCargoInicial[iNumParametro];
				sPorcentaje = sPorcentajeMonto[iNumParametro];
				sCantidad = sCantidadFija[iNumParametro];
				sValorT = sValor[iNumParametro];
				sUnidad= sIdUnidad[iNumParametro];
				sPeriodicidad = sIdPeriodicidad[iNumParametro];
				registro.addDefCampo("ID_CARGO_COMISION",sCargoC == null ? "" : sCargoC);
				registro.addDefCampo("ID_FORMA_APLICACION",sForma == null ? "" : sForma);
				registro.addDefCampo("CARGO_INICIAL",sCargoI == null ? "" : sCargoI);
				registro.addDefCampo("PORCENTAJE_MONTO",sPorcentaje == null ? "" : sPorcentaje);
				registro.addDefCampo("CANTIDAD_FIJA",sCantidad == null ? "" : sCantidad);
				registro.addDefCampo("VALOR",sValorT == null ? "" : sValorT);
				
				if (sUnidad.equals("null")){
					registro.addDefCampo("ID_UNIDAD","");
				}else {
					registro.addDefCampo("ID_UNIDAD",sUnidad);
				}
				
				if (sPeriodicidad.equals("null")){
					registro.addDefCampo("ID_PERIODICIDAD","");
				}else {
					registro.addDefCampo("ID_PERIODICIDAD",sPeriodicidad);
				}	
				
				sSql =  " UPDATE SIM_PRESTAMO_GPO_CARGO SET "+
						" ID_FORMA_APLICACION		='" + (String)registro.getDefCampo("ID_FORMA_APLICACION") + "', \n" +
						" CARGO_INICIAL			='" + (String)registro.getDefCampo("CARGO_INICIAL") + "', \n" +
						" PORCENTAJE_MONTO		='" + (String)registro.getDefCampo("PORCENTAJE_MONTO") + "', \n" +
						" CANTIDAD_FIJA			='" + (String)registro.getDefCampo("CANTIDAD_FIJA") + "', \n" +
						" VALOR				='" + (String)registro.getDefCampo("VALOR") + "', \n" +
						" ID_UNIDAD			='" + (String)registro.getDefCampo("ID_UNIDAD") + "', \n" +
						" ID_PERIODICIDAD		='" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "' \n" +
						" WHERE ID_PRESTAMO_GRUPO 		='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
						" AND ID_CARGO_COMISION   	='" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "' \n"+
						" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();	
			}
				
			sSql = "SELECT ID_PRESTAMO \n" +
					"FROM SIM_PRESTAMO_GPO_DET \n" +
					" WHERE ID_PRESTAMO_GRUPO 		='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
					" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			PreparedStatement ps2 = this.conn.prepareStatement(sSql);
			ps2.execute();
			ResultSet rs2 = ps2.getResultSet();	
			
			
			while (rs2.next()){
				registro.addDefCampo("ID_PRESTAMO",rs2.getString("ID_PRESTAMO"));
					for (int iNumParametro = 0; iNumParametro < sIdCargoComision.length; iNumParametro++) {
						sCargoC = sIdCargoComision[iNumParametro];
						sForma = sIdFormaAplicacion[iNumParametro];
						sCargoI = sCargoInicial[iNumParametro];
						sPorcentaje = sPorcentajeMonto[iNumParametro];
						sCantidad = sCantidadFija[iNumParametro];
						sValorT = sValor[iNumParametro];
						sUnidad= sIdUnidad[iNumParametro];
						sPeriodicidad = sIdPeriodicidad[iNumParametro];
						registro.addDefCampo("ID_CARGO_COMISION",sCargoC == null ? "" : sCargoC);
						registro.addDefCampo("ID_FORMA_APLICACION",sForma == null ? "" : sForma);
						registro.addDefCampo("CARGO_INICIAL",sCargoI == null ? "" : sCargoI);
						registro.addDefCampo("PORCENTAJE_MONTO",sPorcentaje == null ? "" : sPorcentaje);
						registro.addDefCampo("CANTIDAD_FIJA",sCantidad == null ? "" : sCantidad);
						registro.addDefCampo("VALOR",sValorT == null ? "" : sValorT);
						
						if (sUnidad.equals("null")){
							registro.addDefCampo("ID_UNIDAD","");
						}else {
							registro.addDefCampo("ID_UNIDAD",sUnidad);
						}
						
						if (sPeriodicidad.equals("null")){
							registro.addDefCampo("ID_PERIODICIDAD","");
						}else {
							registro.addDefCampo("ID_PERIODICIDAD",sPeriodicidad);
						}	
						
						sSql =  " UPDATE SIM_PRESTAMO_CARGO_COMISION SET "+
								" ID_FORMA_APLICACION		='" + (String)registro.getDefCampo("ID_FORMA_APLICACION") + "', \n" +
								" CARGO_INICIAL			='" + (String)registro.getDefCampo("CARGO_INICIAL") + "', \n" +
								" PORCENTAJE_MONTO		='" + (String)registro.getDefCampo("PORCENTAJE_MONTO") + "', \n" +
								" CANTIDAD_FIJA			='" + (String)registro.getDefCampo("CANTIDAD_FIJA") + "', \n" +
								" VALOR				='" + (String)registro.getDefCampo("VALOR") + "', \n" +
								" ID_UNIDAD			='" + (String)registro.getDefCampo("ID_UNIDAD") + "', \n" +
								" ID_PERIODICIDAD		='" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "' \n" +
								" WHERE ID_PRESTAMO 		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								" AND ID_CARGO_COMISION   	='" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "' \n"+
								" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						PreparedStatement ps3 = this.conn.prepareStatement(sSql);
						ps3.execute();
						ResultSet rs3 = ps3.getResultSet();	
					}
				}
		
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
		sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
			" FECHA_REGISTRO		=TO_DATE('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','DD/MM/YYYY'), \n" +
			" FECHA_REALIZADA		=TO_DATE('" + (String)registro.getDefCampo("FECHA_REALIZADA") + "','DD/MM/YYYY'), \n" +
			" ESTATUS			='" + (String)registro.getDefCampo("ESTATUS") + "', \n" +
			" COMENTARIO			='" + (String)registro.getDefCampo("COMENTARIO") + "' \n" +
			" WHERE ID_ACTIVIDAD_REQUISITO  ='" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n" +
			" AND ID_PRESTAMO   		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
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
		sSql =  " DELETE FROM SIM_PRESTAMO_ETAPA " +
			" WHERE ID_ACTIVIDAD_REQUISITO  ='" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n" +
			" AND ID_PRESTAMO   		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}