/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.configuracion;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.DefinicionTabla;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.HashMap;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import javax.naming.*;

/**
 * Clase que obtiene las propiedades de una tabla
 */

public class ConfiguracionTabla extends Conexion2{

	/**
	 * Obtiene las propiedades de una tabla
	 * @param sCveTabla Clave de la tabla
	 * @param contexto Clave de la tabla
	 * @return Propiedades de la tabla
	 * @throws SQLException Se generó un error al obtener la configuración del tabla.
	 */
	public DefinicionTabla getConfiguracion(String sCveTabla, Context contexto) throws SQLException{
		DefinicionTabla definicionTabla = new DefinicionTabla();
		//VERIFICA SI ENCUENTRA EL OBJETO CON LA DEFINICION EN EL JNDI
		try {
			definicionTabla = (DefinicionTabla)contexto.lookup("Tabla_" + sCveTabla);
		}
		catch (NamingException exLookup) {
			String sNomTabla = "";
			String sDescTabla = "";
			String sBajaLogica = "";
			LinkedList listaTablasDependientes = null;
			LinkedList listaTablasBitacora = null;
			LinkedList listaCamposLlaveTabla = null;
			LinkedList listaCamposTabla = null;

			//OBTIENE EL NOMBRE Y DESCRIPCION DE LA TABLA
			Registro registroRsConfTabla = new Registro();
			registroRsConfTabla.addDefCampo("CVE_TABLA",sCveTabla);
			registroRsConfTabla = getRegistroRsConfTabla(registroRsConfTabla);
			sNomTabla = (String)registroRsConfTabla.getDefCampo("NOM_TABLA");
			sDescTabla = (String)registroRsConfTabla.getDefCampo("DESC_TABLA");
			sBajaLogica = (String)registroRsConfTabla.getDefCampo("B_BAJA_LOGICA");

			//OBTIENE LAS TABLAS DEPENDIENTES DE LA TABLA
			listaTablasDependientes = getTablasDependientes(sCveTabla);
			//OBTIENE LAS TABLAS DE BITACORA DE LA TABLA RS_CONF_TABLA_BITACORA
			listaTablasBitacora = getTablasBitacora(sCveTabla);

			//OBTIENE LA LLAVE PRIMARIA DE LA TABLA
			listaCamposLlaveTabla = getCamposLlaveTabla(sNomTabla);
			//OBTIENE LAS COLUMNAS DE LA TABLA
			Registro camposTabla = getCamposTabla(sNomTabla);
			listaCamposTabla = (LinkedList)camposTabla.getDefCampo("Lista");
			definicionTabla.setMapaCamposTabla((HashMap)camposTabla.getDefCampo("Mapa"));
			//OBTIENE LOS FILTROS DE BUSQUEDA
			definicionTabla.setFiltros(getFiltros(sNomTabla));

			//INICIALIZA OBJETO
			definicionTabla.setNomTabla(sNomTabla);
			definicionTabla.setDescTabla(sDescTabla);
			definicionTabla.setBajaLogica(sBajaLogica);
			definicionTabla.setTablasDependientes(listaTablasDependientes);
			definicionTabla.setTablasBitacora(listaTablasBitacora);
			definicionTabla.setCamposLlavePrimaria(listaCamposLlaveTabla);
			definicionTabla.setCamposTabla(listaCamposTabla);

			//ALMACENA OBJETO EN JNDI
			try {
				contexto = new InitialContext();
				contexto.rebind("Tabla_" + sCveTabla, definicionTabla);
			}
			catch (NamingException exRebind) {
				exRebind.printStackTrace();
			}
		}

		//REGRESA EL OBJETO
		return definicionTabla;
	}

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Se genero un error al obtener el registro.
	 */
	private Registro getRegistroRsConfTabla(Registro parametros) throws SQLException{
		//REALIZA UNA CONSULTA DINÁMICA PARA OBTENER UN REGISTRO DE UNA TABLA
		// ESPECÍFICA
		sSql = "SELECT * FROM RS_CONF_TABLA \n "+
			   " WHERE CVE_TABLA = '"+(String)parametros.getDefCampo("CVE_TABLA")+"' \n";
		ejecutaSql();
		Registro resultado = getConsultaRegistro();
		return resultado;
	}

	/**
	 * Obtiene la lista de los campos de una tabla específica.
	 * @param sCveTabla Clave de la tabla.
	 * @return Los campos del registro.
	 * @throws SQLException Arroja una SQLException si se genera un error al obtener la lista.
	 */
	private LinkedList getTablasDependientes(String sCveTabla) throws SQLException{
		sSql = " SELECT * \n" +
			   " FROM RS_CONF_TABLA_DEPENDIENTE \n" +
			   " WHERE CVE_TABLA = '" + sCveTabla + "' \n" ;
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene la lista de los campos de una tabla específica.
	 * @param sCveTabla Clave de la tabla.
	 * @return Los campos del registro.
	 * @throws SQLException Arroja una SQLException si se genera un error al obtener la lista.
	 */
	private LinkedList getTablasBitacora(String sCveTabla) throws SQLException{
		sSql = " SELECT * \n" +
			   " FROM RS_CONF_TABLA_BITACORA \n" +
			   " WHERE CVE_TABLA = '" + sCveTabla + "' \n" ;
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene la lista de los campos llave de una tabla específica.
	 * @param sNomTabla Nombre de la tabla.
	 * @return Los campos del registro.
	 * @throws SQLException Arroja una SQLException si se genera un error obtener la lista.
	 */
	private LinkedList getCamposLlaveTabla(String sNomTabla) throws SQLException{
		
		// SE RELACIONÓ LA CONSUTA CON LA TABLA COLS PARA OBTENER EL TIPO DE DATO DE 
		// LA COLUMNA Y DETERMINAR SI SE AGREGA APÓSTROFO O NO EN LAS CONSULTAS.
		sSql = " SELECT COL.COLUMN_NAME COLUMNA, COLU.DATA_TYPE \n" +
			   " FROM  \n" +
			   " ALL_CONSTRAINTS CONS, \n" +
			   " ALL_CONS_COLUMNS COL, \n" + 
			   " COLS COLU \n" +
			   " WHERE   COL.TABLE_NAME       = '" + sNomTabla + "' \n" +
			   "     AND COL.TABLE_NAME       = CONS.TABLE_NAME      \n" +
			   "     AND COL.CONSTRAINT_NAME  = CONS.CONSTRAINT_NAME  \n" +
			   "     AND COL.OWNER            = CONS.OWNER             \n" +
			   "     AND CONS.CONSTRAINT_TYPE = 'P'                     \n" +
			   "     AND COL.TABLE_NAME       = COLU.TABLE_NAME(+)       \n" +
			   "     AND COL.COLUMN_NAME      = COLU.COLUMN_NAME(+)       \n"+
			   "     ORDER BY COL.POSITION "; 

		ejecutaSql();
		return this.getConsultaLista();
	}


	/**
	 * Obtiene la lista de los campos de una tabla específica.
	 * @param sNomTabla Nombre de la tabla.
	 * @return Los campos del registro.
	 * @throws SQLException Arroja una SQLException si se genera un error al accesar
	 * la base de datos.
	 */
	private Registro getCamposTabla(String sNomTabla) throws SQLException{
		Registro camposTabla = new Registro();
		LinkedList listaColumnas = new LinkedList();
		HashMap mapaListaColumnas = new HashMap();

		sSql = "SELECT * FROM " + sNomTabla + " WHERE ROWNUM = 0 ";
		ejecutaSql();

		ResultSetMetaData data = rs.getMetaData();
		for(int i = 1; i<= data.getColumnCount(); i++){
			Registro registro = new Registro();
			registro.addDefCampo("COLUMNA", data.getColumnName(i));
			registro.addDefCampo("TIPO_DATO", new Integer(data.getColumnType(i)));
			registro.addDefCampo("TIPO_NULL", new Integer(data.isNullable(i)));
			listaColumnas.add(registro);
			mapaListaColumnas.put(data.getColumnName(i), registro);
		}
		camposTabla.addDefCampo("Lista", listaColumnas);
		camposTabla.addDefCampo("Mapa", mapaListaColumnas);
		return camposTabla;
	}

	/**
	 * Obtiene un mensaje si el registro esta relacionado con tablas dependientes
	 * @param listaTablasDependientes Lista de las tablas dependientes
	 * @param listaCamposLlave Lista de los campos llaves primaria
	 * @param registro Datos de los campos llaves primaria
	 * @return Mensaje de las tablas con las que el registro esta relacionado
	 * @throws SQLException Arroja una SQLException si se genera un error al accesar
	 * la base de datos.
	 */
	public String getMensajeDependencia(LinkedList listaTablasDependientes, LinkedList listaCamposLlave, Registro registro) throws SQLException{
		String sMensajeDependenciaTablas = "";
		Iterator iListaTablasDependientes = listaTablasDependientes.iterator();

		//BUSCA REGISTROS HIJOS EN LAS TABLAS DEPENDIENTES
		//ITERA LA LISTA DE TABLAS DEPENDIENTES
		while (iListaTablasDependientes.hasNext()){
			//OBTIENE TABLA DEPENDIENTE
			Registro registroTablaDependiente = (Registro)iListaTablasDependientes.next();
			//OBTIENE LA CLAVE DE LA TABLA DEPENDIENTE
			String sCveTablaDependiente = (String)registroTablaDependiente.getDefCampo("CVE_TABLA_DEPENDIENTE");
			//OBTIENE LA CLAVE DE LA TABLA PADRE
			String sCveTablaPadre = (String)registroTablaDependiente.getDefCampo("CVE_TABLA");
			registroTablaDependiente.addDefCampo("CVE_TABLA",sCveTablaDependiente);

			//OBTIENE LOS DATOS DE LA TABLA DEPENDIENTE
			Registro registroDatosTablaDependiente = this.getRegistroRsConfTabla(registroTablaDependiente);
			String sNomTablaDependiente = (String)registroDatosTablaDependiente.getDefCampo("NOM_TABLA");
			String sDescTablaDependiente = (String)registroDatosTablaDependiente.getDefCampo("DESC_TABLA");
			String sBCamposDiferentes = (String)registroTablaDependiente.getDefCampo("B_CAMPOS_DIFERENTES");
			
			//LINEA TEMPORAL PARA CORREGIR LA MODIFICACION DEL OBJETO EN EL JNDI
			registroTablaDependiente.addDefCampo("CVE_TABLA",sCveTablaPadre);

			//VERIFICA SI EXISTEN CAMPOS DIFERENTES EN LA TABLA PADRE Y LA TABLA DEPENDIENTE
			if(sBCamposDiferentes.equals("V")){
				//OBTIENE LA LISTA DE CAMPOS LLAVE DE LA TABLA PADRE QUE SON DIFERENTES A LA TABLA HIJO
				LinkedList listaCamposLlaveDiferentes = this.getListaCamposLlaveDiferentes(sCveTablaPadre, sCveTablaDependiente);
				//OBTIENE LA LISTA DE TODOS LOS CAMPOS LLAVE DE LA TABLA PADRE
				Iterator ilistaCamposLlave = listaCamposLlave.iterator();
				int iNumCampo = 0;
				String sCadenaSql = null;
				//ITERA TODOS LOS CAMPOS DE LA TABLA PADRE
				while (ilistaCamposLlave.hasNext()){
					Registro registroLlave = (Registro)ilistaCamposLlave.next();
					String sCampoLlave = (String)registroLlave.getDefCampo("COLUMNA");
					Iterator iListaCamposLlaveDiferentes = listaCamposLlaveDiferentes.iterator();
					//ITERA LOS CAMPOS DE LA TABLA PADRE QUE SON DIFERENTES A LA TABLA HIJO
					while(iListaCamposLlaveDiferentes.hasNext()){
						String sAgregaCampoCadena = null;
						Registro registroCampoLlaveDiferente = (Registro)iListaCamposLlaveDiferentes.next();
						String sCampoLlaveDiferente = (String)registroCampoLlaveDiferente.getDefCampo("CAMPO_LLAVE");
						//VERIFICA SI EL CAMPO DE LA TABLA PADRE ES DIFERENTE EN LA TABLA DEPENDIENTE
						if(sCampoLlave.equals(sCampoLlaveDiferente)){
							LinkedList listaCamposLlaveDiferenteDetalle = this.getListaCamposLlaveDiferentesDetalle(sCveTablaPadre, sCveTablaDependiente, sCampoLlaveDiferente);
							Iterator iListaCamposLlaveDiferenteDetalle = listaCamposLlaveDiferenteDetalle.iterator();
							//ITERA LOS CAMPOS DE LA TABLA DEPENDIENTE QUE SON REFERENCIA DEL CAMPO LLAVE DE LA TABLA PADRE
							int iNumCampoReferencia = 0;
							while(iListaCamposLlaveDiferenteDetalle.hasNext()){
								String sCampoReferencia = null;
								Registro registroCampoReferencia = (Registro)iListaCamposLlaveDiferenteDetalle.next();
								sCampoReferencia = (String)registroCampoReferencia.getDefCampo("CAMPO_REFERENCIA");
								sAgregaCampoCadena = sCampoReferencia + " = '" + (String)registro.getDefCampo(sCampoLlave) + "'";
								//VERIFICA SI ES EL PRIMER CAMPO PARA ARMAR LA CONDICION
								if (iNumCampo == 0) {
									sCadenaSql = " WHERE " + sAgregaCampoCadena;
								}
								else {
									if(iNumCampoReferencia == 0){
										sCadenaSql = sCadenaSql + " AND ( " + sAgregaCampoCadena;
									}
									else{
										//EL CAMPO LLAVE DE LA TABLA PADRE ESTA DEFINIDA MAS DE UNA VEZ EN LA TABLA DEPENDIENTE
										sCadenaSql = sCadenaSql + " OR " + sAgregaCampoCadena;
									}
								}
							iNumCampoReferencia++;
							}//ITERA LOS CAMPOS DE LA TABLA DEPENDIENTE QUE SON REFERENCIA DEL CAMPO LLAVE DE LA TABLA PADRE
							sCadenaSql = sCadenaSql + " ) ";
						}
						else{
							//EL CAMPO DE LA TABLA PADRE ES IGUAL EN LA TABLA DEPENDIENTE 
							sAgregaCampoCadena = sCampoLlave + " = '" + (String)registro.getDefCampo(sCampoLlave) + "'";
							if (iNumCampo == 0) {
								sCadenaSql = " WHERE " + sAgregaCampoCadena;
							}
							else {
                                if(!sBCamposDiferentes.equals("V")){
                                    sCadenaSql = sCadenaSql + " AND " + sAgregaCampoCadena;
                                }
							}
						}//VERIFICA SI EL CAMPO DE LA TABLA PADRE ES DIFERENTE EN LA TABLA DEPENDIENTE
					}//ITERA LOS CAMPOS DE LA TABLA PADRE QUE SON DIFERENTES A LA TABLA HIJO
					iNumCampo++;
				}//ITERA TODOS LOS CAMPOS DE LA TABLA PADRE

				//EJECUTA LA CONSULTA
				sSql = "SELECT * FROM " + sNomTablaDependiente + sCadenaSql;
				//VERIFICA SI ENCONTRO REGISTROS HIJOS
				ejecutaSql();
				if (rs.next()) {
					//OBTIENE LA DESCRIPCION DE LA TABLA CON LA QUE ESTA RELACIONADO EL REGISTRO
					sMensajeDependenciaTablas = sMensajeDependenciaTablas + " " + sDescTablaDependiente;
				}
			}
			else{
				//NO EXISTEN CAMPOS DIFERENTES EN LA TABLA PADRE Y LA TABLA DEPENDIENTE
				//OBTIENE LOS CAMPOS DE LA LLAVE PRIMARIA PARA ARMAR LA CONDICION
				String sCamposLlave = null;
				if (listaCamposLlave != null){
					Iterator ilistaCamposLlave = listaCamposLlave.iterator();
					int iNumCampo = 0;
					while (ilistaCamposLlave.hasNext()){
						Registro registroLlave = (Registro)ilistaCamposLlave.next();
						String sCampoLlave = (String)registroLlave.getDefCampo("COLUMNA");
						String sAgregaCampoCadena = sCampoLlave + " = '" + (String)registro.getDefCampo(sCampoLlave) + "'";
						if (iNumCampo == 0){
							sCamposLlave = 	" WHERE " + sAgregaCampoCadena;
						}
						else{
							sCamposLlave = sCamposLlave + " AND " + sAgregaCampoCadena;
						}
						iNumCampo++;
					}
				}
				//EJECUTA LA CONSULTA
				sSql = "SELECT * FROM " + sNomTablaDependiente + sCamposLlave;
				//VERIFICA SI ENCONTRO REGISTROS HIJOS
				ejecutaSql();
				if (rs.next()) {
					//OBTIENE LA DESCRIPCION DE LA TABLA CON LA QUE ESTA RELACIONADO EL REGISTRO
					sMensajeDependenciaTablas = sMensajeDependenciaTablas + " " + sDescTablaDependiente;
				}
			}//VERIFICA SI EXISTEN CAMPOS DIFERENTES EN LA TABLA PADRE Y LA TABLA DEPENDIENTE
		}//ITERA LA LISTA DE TABLAS DEPENDIENTES
		return sMensajeDependenciaTablas;
	}

	/**
	 * Obtiene la lista de las propiedades de los campos de una tabla específica.
	 * @param sCveTabla Clave de la tabla.
	 * @return Los campos del registro.
	 * @throws SQLException Arroja una SQLException si se genera un error al accesar
	 * la base de datos.
	 */
	public LinkedList getPropiedadesCampos(String sCveTabla) throws SQLException{
		sSql = " SELECT * \n" +
			   " FROM RS_CONF_TABLA_CAMPO \n" +
			   " WHERE CVE_TABLA = '" + sCveTabla + "' \n" ;
		ejecutaSql();
		return this.getConsultaLista();
	}
	
	/**
	 * Obtiene el campo llave de una tabla específica
	 * @param sCveTabla Clave de la tabla.
	 * @return campo del registro.
	 * @throws SQLException Arroja una SQLException si se genera un error al accesar
	 * la base de datos.
	 */
	 public String  getCampoSequence(String sCveTabla) throws SQLException{ 
		 String sNomSecuencia = null;
		 sSql = " SELECT  NOM_SECUENCIA \n" +
			   " FROM RS_CONF_TABLA_CAMPO \n" +
			   " WHERE CVE_TABLA = '" + sCveTabla + "' \n" +
			   " AND B_CAMPO_LLAVE = 'V'  " ;
		ejecutaSql();
		if (rs.next()){
			sNomSecuencia = rs.getString("NOM_SECUENCIA");
	   }
		return sNomSecuencia;
	}
	

	/**
	 * Obtiene la lista de campos llave de una tabla padre que son
	 * diferentes a la tabla dependiente.
	 * @param sCveTabla Clave de la tabla padre.
	 * @param sCveTabladependiente Clave de la tabla dependiente.
	 * @return La lista de campos.
	 * @throws SQLException Arroja una SQLException si se genera un error al accesar
	 * la base de datos.
	 */
	private LinkedList getListaCamposLlaveDiferentes(String sCveTabla, String sCveTabladependiente) throws SQLException{
		sSql = " SELECT DISTINCT CAMPO_LLAVE \n" +
			   " FROM RS_CONF_TABLA_DEP_CAMPO_REF \n" +
			   " WHERE CVE_TABLA = '" + sCveTabla + "' \n" +
			   " AND CVE_TABLA_DEPENDIENTE = '" + sCveTabladependiente + "'";
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene la lista de campos llave de una tabla dependiente.
	 * @param sCveTabla Clave de la tabla.
	 * @param sCveTabladependiente Clave de la tabla dependiente.
	 * @param sCampoLlave Campo llave.
	 * @return La lista de campos.
	 * @throws SQLException Arroja una SQLException si se genera un error al accesar
	 * la base de datos.
	 */
	private LinkedList getListaCamposLlaveDiferentesDetalle(String sCveTabla, String sCveTabladependiente, String sCampoLlave) throws SQLException{
		sSql = " SELECT CAMPO_REFERENCIA \n" +
			   " FROM RS_CONF_TABLA_DEP_CAMPO_REF \n" +
			   " WHERE CVE_TABLA = '" + sCveTabla + "' \n" +
			   " AND CVE_TABLA_DEPENDIENTE = '" + sCveTabladependiente + "'"+
			   " AND CAMPO_LLAVE = '" + sCampoLlave + "'";
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene la lista de campos llave de una tabla dependiente.
	 * @param sCveTabla Clave de la tabla.
	 * @return lista.
	 * @throws SQLException Arroja una SQLException si se genera un error al accesar
	 * la base de datos.
	 */
	public HashMap getFiltros (String sCveTabla) throws SQLException{
		HashMap listaFiltros = null;
		boolean bExito = false;
		sSql = " SELECT * \n" +
				 " FROM RS_CONF_TABLA_FILTRO \n" +
				 " WHERE CVE_TABLA = '" + sCveTabla + "' \n" ;
		ejecutaSql();
		while (rs.next()){
			if (bExito == false){
			   bExito = true;
			   listaFiltros = new HashMap();
			}
			Registro registro = new Registro();
			registro.addDefCampo("CVE_FILTRO", rs.getString("CVE_FILTRO"));
			registro.addDefCampo("CVE_TABLA", rs.getString("CVE_TABLA"));
			registro.addDefCampo("TX_SQL_FILTRO", rs.getString("TX_SQL_FILTRO"));
			registro.addDefCampo("B_MUESTRA_CODIGO", rs.getString("B_MUESTRA_CODIGO"));
			listaFiltros.put(rs.getString("CVE_FILTRO"), registro);
		}
		return listaFiltros;
	}
}