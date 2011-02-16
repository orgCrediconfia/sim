/**
 * Sistema de administraci�n de portales.
 *
 */

package com.rapidsist.portal.catalogos; 

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.util.Fecha2;
import com.rapidsist.portal.configuracion.Funcion;
import com.rapidsist.portal.configuracion.Mensaje;
import com.rapidsist.portal.configuracion.DefinicionTabla;
import com.rapidsist.portal.configuracion.ConfiguracionTabla;
import com.rapidsist.portal.configuracion.OperacionesComunes;
import javax.ejb.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

//WEBMACRO
import org.webmacro.*;
import org.webmacro.engine.*;

/**
 * Ejb que ejecuta las operaciones de alta, baja o cambio sobre los catalogos.
 *
 * @ejb.bean
 *     name="CatalogoSL"
 *     display-name="CatalogoSL"
 *     type="Stateless"
 *     jndi-name="CatalogoSL"
 *     local-jndi-name="CatalogoSLLocal"
 * @ejb.transaction
 *     type="Required"
 * @ejb.resource-ref
 *		res-ref-name="jdbc/PortalDs"
 *		res-type="javax.sql.DataSource"
 *		res-auth="Container"
 *		jndi-name="PortalDs"
 * @weblogic.resource-description
 *		res-ref-name="jdbc/PortalDs"
 *		jndi-name="PortalDs"
 * @jrun.resource-ref
 *		res-ref-name="jdbc/PortalDs"
 *		jndi-name="PortalDs"
 * @jboss.resource-ref
 *		res-ref-name="jdbc/PortalDs"
 *		jndi-name="java:/PortalDs"
 * @jonas.resource
 *		res-ref-name="jdbc/PortalDs"
 *		jndi-name="PortalDs"
 * @oc4j.bean
 * 		data-source="jdbc/PortalDs"
 */

public class CatalogoSLBean implements SessionBean{
    
	public static final int ALTA = 1;
	public static final int BAJA = 2;
	public static final int MODIFICACION = 3;
	public final int CONSULTA_REGISTRO = 4;
	public final int CONSULTA_TABLA = 5;
	public WebMacro wm = null;
	
	/**
	 * Contexto del EJB
	 */
	SessionContext sessionContext;

	/**
	 * Este m�todo es requerido por la especificaci�n J2EE, pero no tiene
	 * implementado ning�n c�digo.
	 */
	public void ejbCreate() {
		try{
			wm = new WM();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Este m�todo es requerido por la especificaci�n J2EE, pero no tiene
	 * implementado ning�n c�digo.
	 */
	public void ejbRemove(){
	}

	/**
	 * Este m�todo es requerido por la especificaci�n J2EE, pero no tiene
	 * implementado ning�n c�digo.
	 */
	public void ejbActivate(){
	}

	/**
	 * Este m�todo es requerido por la especificaci�n J2EE, pero no tiene
	 * implementado ning�n c�digo.
	 */
	public void ejbPassivate(){
	}

	/**
	 * Inicializa el contexto de la sesion para el EJB.
	 * @param sessionContext Contexto de la sesion que provee informaci�n al EJB sobre
	 * el contenedor.
	 */
	public void setSessionContext(SessionContext sessionContext){
		//System.out.println("*****************************************************************entro en catalogoSLBean");
		this.sessionContext = sessionContext;
	}

	/**
	 * @param conexion Objeto tipo Conexion2.
	 * @param sCveTabla Clave de la tabla.
	 * @param contexto Objeto tipo Context.
	 * @param registro Objeto registro.
	 * @return Obtiene el registro.
	 * @throws java.lang.Exception Se genero un error al obtener el registro.
	 */
	private Registro getRegistroAutomatico(Conexion2 conexion, String sCveTabla, Context contexto, Registro registro) throws Exception {
		Registro resultado = null;

		//SE GENERA AUTOMATICAMENTE LA CONSULTA DEL REGISTRO
		ConfiguracionTabla configuracionTabla = new ConfiguracionTabla();

		//ABRE LA CONEXION DE LA CLASE
		configuracionTabla.setConexion(conexion.getConexion());

		//OBTIENE LAS DEFINICIONES DE LA TABLA
		DefinicionTabla definicionTabla = configuracionTabla.getConfiguracion(sCveTabla, contexto);
		String sNomTabla = definicionTabla.getNomTabla();
		LinkedList listaCamposLlave = definicionTabla.getCamposLlavePrimaria();

		//OBTIENE LOS CAMPOS DE LA LLAVE PRIMARIA PARA CONSTRUIR LA CONDICION
		String sCamposLlave = null;
		if (listaCamposLlave != null) {
			Iterator listaCmpLlave = listaCamposLlave.iterator();
			int iNumCampo = 0;
			while (listaCmpLlave.hasNext()) {
				Registro registroLlave = (Registro) listaCmpLlave.next();
				String sCampoLlave = (String) registroLlave.getDefCampo("COLUMNA");
				String sTipo  = (String) registroLlave.getDefCampo("DATA_TYPE");
				if(sTipo != null){
					if(sTipo.equals("DATE")	|| sTipo.equals("NUMBER")){
						sTipo = "";	
					}
					else{
						sTipo = "'";	
					}
				}
				else{
					sTipo = "'";	
				}

				String sAgregaCampoCadena = sCampoLlave + " = " + sTipo + registro.getDefCampo(sCampoLlave) + sTipo;

				if (iNumCampo == 0) {
					sCamposLlave = "WHERE " + sAgregaCampoCadena;
				}
				else {
					sCamposLlave = sCamposLlave + " AND " + sAgregaCampoCadena;
				}
				iNumCampo++;
			}
		}//OBTIENE LOS CAMPOS DE LA LLAVE PRIMARIA PARA CONSTRUIR LA CONDICION

		//EJECUTA LA CONSULTA
		conexion.sSql = "SELECT * FROM "+sNomTabla+" "+sCamposLlave;
		conexion.ejecutaSql();
		return conexion.getConsultaRegistro();
	}
	
	/**
	 * @param conexion Objeto tipo Conexion2.
	 * @param sCveTabla Clave de la tabla
	 * @param contexto Objeto tipo Context.
	 * @param parametros Objeto registro.
	 * @return lista Lista de registros.
	 * @throws java.lang.Exception Se genero un error al obtener los registros.
	 */
	 
	private LinkedList getRegistrosAutomatico(Conexion2 conexion, String sCveTabla, Context contexto, Registro parametros) throws Exception {
		LinkedList lista = null;
		//SE GENERA AUTOMATICAMENTE LA CONSULTA DEL REGISTRO
		ConfiguracionTabla configuracionTabla = new ConfiguracionTabla();
		//ABRE LA CONEXION DE LA CLASE
		configuracionTabla.setConexion(conexion.getConexion());
		//OBTIENE LAS DEFINICIONES DE LA TABLA
		DefinicionTabla definicionTabla = configuracionTabla.getConfiguracion(sCveTabla, contexto);

		String sFiltro = (String)parametros.getDefCampo("Filtro");
		HashMap listaFiltros = definicionTabla.getFiltros();
		Registro registroFiltro = (Registro)listaFiltros.get(sFiltro);
		if (registroFiltro != null){

			//INICIALIZA WEBMACRO
			org.webmacro.Context contextTemplate = wm.getContext();
			ByteArrayOutputStream salida = new ByteArrayOutputStream();
			FastWriter fw = wm.getFastWriter (salida, "UTF8");

			//AGREGA LOS PARAMETROS QUE VIENEN EN EL OBJETO "PARAMETROS"
			//AL CONTEXTO DE WEBMACRO
			HashMap listaCampos = parametros.getCampos();
			Iterator nombresCampos = listaCampos.keySet().iterator();
			while (nombresCampos.hasNext()){
				String sNombreCampo = (String)nombresCampos.next();
				contextTemplate.put(sNombreCampo, parametros.getDefCampo(sNombreCampo));
			}

			//INSTANCIA EL TEMPLATE DEL FILTRO DE LA TABLA
			StringTemplate template = new StringTemplate(contextTemplate.getBroker(), (String)registroFiltro.getDefCampo("TX_SQL_FILTRO"));
			template.write(fw, contextTemplate);
			fw.flush();

			//EJECUTA LA CONSULTA
			conexion.sSql = salida.toString();
			if (((String)registroFiltro.getDefCampo("B_MUESTRA_CODIGO")).equals("V")){
				System.out.println("Macro ejecutada:\n" + salida.toString());
			}
			conexion.ejecutaSql();

			lista = conexion.getConsultaLista();
		}
		return lista;
	}

	/**
	 * Obtiene un registro de un cat�logo en base a la llave primaria.
	 * @param sCatalogo Nombre del cat�logo.
	 * @param registro Llave primaria.
	 * @return Objeto Registro resultado de la consulta.
	 * @throws LogicaNegocioException Se gener� un error al obtener el registro.
	 * @ejb.interface-method
	 */
	public Registro getRegistro(String sCatalogo, Registro registro) throws com.rapidsist.portal.catalogos.LogicaNegocioException {
		Registro resultado = new Registro();
		Conexion2 conexion = null;
		Conexion2 claseDao = null;
		
		//OBTIENE EL ITERATOR DE LOS CAMPOS DEL OBJETO REGISTRO
		HashMap camposRegistro = registro.Campos;
		Map mCamposRegistro = camposRegistro;
		Iterator iCamposRegistro = mCamposRegistro.keySet().iterator();

		while (iCamposRegistro.hasNext()) {
			String sValorCampoLlave = (String)iCamposRegistro.next();
			//VERIFICA SI EL OBJETO ES DE TIPO STRING
			if( registro.getDefCampo(sValorCampoLlave) instanceof String ){
				String sDescripcionCampoLlave = (String)registro.getDefCampo(sValorCampoLlave);
				//REEMPLAZA EL APOSTROFRE POR DOBLE APOSTROFE PARA CONSULTAR EL REGISTRO
				sDescripcionCampoLlave = sDescripcionCampoLlave.replaceAll("'","''");
				registro.addDefCampo(sValorCampoLlave,sDescripcionCampoLlave);				
			}
		}

		boolean bErrorGenerado = false;

		try {
			//OBTIENE EL NOMBRE DE LA CLASE DAO ASOCIADA A LA FUNCION
			Context contexto= new InitialContext();
			OperacionesComunes operacionesComunes = new OperacionesComunes();
			Funcion funcion = operacionesComunes.getFuncion(sCatalogo);

			//SE ABRE LA CONEXION A LA BASE DE DATOS
			conexion = new Conexion2();
			conexion.abreConexion("java:comp/env/jdbc/PortalDs");

			//VERIFICA SI ENCONTRO LA FUNCION
			if (funcion != null){

				//OBTIENE LA CLAVE DE LA TABLA ASIGNADA A LA FUNCION
				String sCveTabla = funcion.getCveTabla();

				//VERIFICA SI LA FUNCION TIENE DAO ASIGNADA
				if ((funcion.getClaseDao() != null) && (!funcion.getClaseDao().equals(""))){

					//SE INSTANCIA LA CLASE DAO
					Class cClase = Class.forName(funcion.getClaseDao());
					Object instanciaObj = cClase.newInstance();

					//VERIFICA SI LA CONSULTA DE REGISTROS SE DEBE IMPLEMENTAR DE LA DAO
					if (instanciaObj instanceof OperacionConsultaRegistro) {
						//SE PASA LA CONEXION A LA CLASE DAO
						Conexion2 claseDaoConsulta = (Conexion2) instanciaObj;
						claseDaoConsulta.setConexion(conexion.getConexion());
						resultado = ( (OperacionConsultaRegistro) instanciaObj).getRegistro(registro);
					}
					else {
						//VERIFICA SI SE TIENE LA CLAVE DE TABLA PARA IMPLEMENTAR LA CONSULTA
						//DINAMICAMENTE
						if ((sCveTabla != null) && (!sCveTabla.equals(""))){
							//SE IMPLEMENTA AUTOMATICAMENTE LA CONSULTA DE REGISTROS
							resultado = getRegistroAutomatico(conexion, sCveTabla, contexto, registro);
						}
					}//VERIFICA SI LA CONSULTA DE REGISTROS SE DEBE IMPLEMENTAR DE LA DAO
				}
				else{
					//VERIFICA SI SE TIENE LA CLAVE DE TABLA PARA IMPLEMENTAR LA CONSULTA
					//DINAMICAMENTE
					if ((sCveTabla != null) && (!sCveTabla.equals(""))){
						//SE IMPLEMENTA AUTOMATICAMENTE LA CONSULTA DE REGISTROS
						resultado = getRegistroAutomatico(conexion, sCveTabla, contexto, registro);
					}
				}
			}
			else{
				//GENERA MENSAJE DE ERROR
				System.out.println("La funcion: " +sCatalogo + ", no esta registrada en el cat�logo de funciones");
				bErrorGenerado = true;
			}//VERIFICA SI ENCONTRO LA FUNCION
		}
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			bErrorGenerado = true;
		}
		catch (IllegalAccessException ex) {
			ex.printStackTrace();
			bErrorGenerado = true;
		}
		catch (InstantiationException ex) {
			ex.printStackTrace();
			bErrorGenerado = true;
		}
		catch (SQLException ex) {
			sessionContext.setRollbackOnly();
			bErrorGenerado = true;
			System.out.println("Se genero una SQLException en CatalogoSLBean: " + ex.getMessage());
		}
		catch (Exception ex) {
			bErrorGenerado = true;
			ex.printStackTrace();
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if (claseDao != null){
				if (claseDao.isConectado()) {
					claseDao.cierraConexion();
				}
			}
			if (conexion != null){
				if (conexion.isConectado()) {
					conexion.cierraConexion();
				}
			}
		}
		//VERIFICA SI SE GENERO UNA EXCEPTION
		if (bErrorGenerado){
			//LE ENVIA A LA CAPA CLIENTE LA NOTIFICACION DE UNA EXCEPTION
			throw new LogicaNegocioException();
		}
		return resultado;
	}
	/**
	 * Obtiene registros de un cat�logo en base a la llave primaria.
	 * @param sCatalogo Nombre del cat�logo.
	 * @param parametros Par�metros de filtro de b�squeda.
	 * @return Objeto Registro resultado de la consulta.
	 * @throws LogicaNegocioException Se gener� un error al obtener los registros
	 * @ejb.interface-method
	 */
	public LinkedList getRegistros(String sCatalogo, Registro parametros) throws com.rapidsist.portal.catalogos.LogicaNegocioException {
		LinkedList lista = null;
		Conexion2 conexion = null;
		Conexion2 claseDao = null;
		
		//OBTIENE EL ITERATOR DE LOS CAMPOS DEL OBJETO REGISTRO
		HashMap camposRegistro = parametros.Campos;
		Map mCamposRegistro = camposRegistro;
		Iterator iCamposRegistro = mCamposRegistro.keySet().iterator();

		while (iCamposRegistro.hasNext()) {
			String sValorCampoLlave = (String)iCamposRegistro.next();
			//VERIFICA SI EL OBJETO ES DE TIPO STRING
			if( parametros.getDefCampo(sValorCampoLlave) instanceof String ){
				String sDescripcionCampoLlave = (String)parametros.getDefCampo(sValorCampoLlave);
				//REEMPLAZA EL APOSTROFRE POR DOBLE APOSTROFE PARA CONSULTAR EL REGISTRO
				sDescripcionCampoLlave = sDescripcionCampoLlave.replaceAll("'","''");
				parametros.addDefCampo(sValorCampoLlave,sDescripcionCampoLlave);				
			}
		}
		
		
		boolean bErrorGenerado = false;
		try {
			//OBTIENE EL NOMBRE DE LA CLASE DAO ASOCIADA A LA FUNCION
			Context contexto= new InitialContext();

			OperacionesComunes operacionesComunes = new OperacionesComunes();
			Funcion funcion = operacionesComunes.getFuncion(sCatalogo);
			//SE ABRE LA CONEXION A LA BASE DE DATOS
			conexion = new Conexion2();

			//ABRE CONEXION A BASE DE DATOS
			conexion.abreConexion("java:comp/env/jdbc/PortalDs");

			//VERIFICA SI ENCONTRO LA FUNCION
			if (funcion != null){

				//OBTIENE LA CLAVE DE LA TABLA ASIGNADA A LA FUNCION
				String sCveTabla = funcion.getCveTabla();

				//VERIFICA SI LA FUNCION TIENE DAO ASIGNADA
				if ((funcion.getClaseDao() != null) && (!funcion.getClaseDao().equals(""))){

					//SE INSTANCIA LA CLASE DAO
					Class cClase = Class.forName(funcion.getClaseDao());
					Object instanciaObj=cClase.newInstance();

					//VERIFICA SI LA CONSULTA DE REGISTROS SE DEBE IMPLEMENTAR DE LA DAO
					if (instanciaObj instanceof OperacionConsultaTabla){

						//SE ABRE LA CONEXION A LA BASE DE DATOS Y SE PASA A LA CLASE DAO
						claseDao = (Conexion2)instanciaObj;
						claseDao.setConexion(conexion.getConexion());

						OperacionConsultaTabla control = (OperacionConsultaTabla) claseDao;
						lista = control.getRegistros(parametros);
					}
					else{
						//VERIFICA SI SE TIENE LA CLAVE DE TABLA PARA IMPLEMENTAR LA CONSULTA
						//DINAMICAMENTE
						if ((sCveTabla != null) && (!sCveTabla.equals(""))){
							//SE IMPLEMENTA AUTOMATICAMENTE LA CONSULTA DE REGISTROS
							lista = getRegistrosAutomatico(conexion, sCveTabla, contexto, parametros);
						}
					}//VERIFICA SI LA CONSULTA DE REGISTROS SE DEBE IMPLEMENTAR DE LA DAO
				}
				else{
					//VERIFICA SI SE TIENE LA CLAVE DE TABLA PARA IMPLEMENTAR LA CONSULTA
					//DINAMICAMENTE
					if ((sCveTabla != null) && (!sCveTabla.equals(""))){
						//SE IMPLEMENTA AUTOMATICAMENTE LA CONSULTA DE REGISTROS
						lista = getRegistrosAutomatico(conexion, sCveTabla, contexto, parametros);
					}
				}//VERIFICA SI LA FUNCION TIENE DAO ASIGNADA
			}
			else{
				//GENERA MENSAJE DE ERROR
				System.out.println("La funcion: " +sCatalogo + ", no esta registrada en el cat�logo de funciones");
				bErrorGenerado = true;
			}//VERIFICA SI ENCONTRO LA FUNCION
		}
		catch (ClassNotFoundException ex) {
			bErrorGenerado = true;
			ex.printStackTrace();
		}
		catch (IllegalAccessException ex) {
			bErrorGenerado = true;
			ex.printStackTrace();
		}
		catch (InstantiationException ex) {
			bErrorGenerado = true;
			ex.printStackTrace();
		}
		catch (SQLException ex) {
			sessionContext.setRollbackOnly();
			bErrorGenerado = true;
			System.out.println("Se genero una SQLException en CatalogoSLBean: " + ex.getMessage());
		}
		catch (Exception ex) {
			bErrorGenerado = true;
			ex.printStackTrace();
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if (claseDao != null){
				if (claseDao.isConectado()) {
					claseDao.cierraConexion();
				}
			}
			if (conexion != null){
				if (conexion.isConectado()) {
					conexion.cierraConexion();
				}
			}
		}
		//VERIFICA SI SE GENERO UNA EXCEPTION
		if (bErrorGenerado){
			//LE ENVIA A LA CAPA CLIENTE LA NOTIFICACION DE UNA EXCEPTION
			throw new LogicaNegocioException();
		}
		return lista;
	}

	/**
	 * Genera las operaciones sobre la base de datos a partir de un archivo de configuracion
	 * en XML.
	 * @param sCatalogo Archivo de configuraci�n para la clase DAO.
	 * @param registro Parametros de entrada que provienen de la clase CON.
	 * @param iTipoOperacion Tipo de operacion a ejecutar.
	 * @return Resultado de la operacion.
	 * @throws LogicaNegocioException Se gener� un error en operacionCatalogo
	 * @ejb.interface-method
	 */
	public ResultadoCatalogo operacionCatalogo(String sCatalogo, Registro registro, int iTipoOperacion) throws com.rapidsist.portal.catalogos.LogicaNegocioException {
		ResultadoCatalogo resultadoCatalogo = null;
		/*
		boolean bResultado = true;
		boolean bCatalogoEncontrado = true;
		Conexion2 conexion = null;
		OperacionesComunes operacionesComunes = new OperacionesComunes();
		boolean bErrorGenerado = false;
		try {

			//OBTIENE EL NOMBRE DE LA CLASE DAO ASOCIADA A LA FUNCION
			Context contexto= new InitialContext();
			Funcion funcion = operacionesComunes.getFuncion(sCatalogo);

			//VERIFICA SI ENCONTRO LA FUNCION
			if (funcion != null){

				//VERIFICA QUE EL NOMBRE DE LA CLASE DAO NO SEA NULO
				if (funcion.getClaseDao() != null){

					//SE OBTIENE EL ESQUEMA DEL CATALOGO XML
					String sEsquemaCatalgo = funcion.getEsquemaCatalogo();
					//TRANSFORMA EL ARCHIVO XML DE ENTRADA A CLASES JAVA
					StringReader xmlReader = new StringReader(sEsquemaCatalgo);
					Catalogo catalgo = Catalogo.unmarshal(xmlReader);
					//SE ABRE LA CONEXION A LA BASE DE DATOS Y SE PASA A LA CLASE DAO
					conexion = new Conexion2();
					conexion.abreConexion("java:comp/env/jdbc/PortalDs");

					CatalogoXmlDAO catalogoXmlDAO = new CatalogoXmlDAO();
					catalogoXmlDAO.setConexion(conexion.getConexion());
					switch (iTipoOperacion){
						case CONSULTA_REGISTRO :
							resultadoCatalogo = catalogoXmlDAO.getConsultaRegistro(catalgo, registro);
							break;
						case CONSULTA_TABLA :
							resultadoCatalogo = catalogoXmlDAO.getConsultaTabla(catalgo, registro);
							break;
						case ALTA :
							resultadoCatalogo = catalogoXmlDAO.getActualizacion(catalgo, registro, iTipoOperacion);
							break;
						case BAJA :
							resultadoCatalogo = catalogoXmlDAO.getActualizacion(catalgo, registro, iTipoOperacion);
							break;
						case MODIFICACION :
							resultadoCatalogo = catalogoXmlDAO.getActualizacion(catalgo, registro, iTipoOperacion);
							break;
					}

					//VERIFICA SI SE GENERO ALGUN MENSAJE DURANTE LA OPERACI�N
					if (resultadoCatalogo.mensaje.getClave() != null){
						//VERIFICA SI EL MENSAJE NO SE GENERA AUTOMATICAMENTE
						if(resultadoCatalogo.mensaje.getDescripcion() == null){
							Mensaje mensaje = operacionesComunes.getMensaje(resultadoCatalogo.mensaje.getClave());
							resultadoCatalogo.mensaje = mensaje;
						}
					}

					//CIERRA LA CONEXION A LA BASE DE DATOS
					conexion.cierraConexion();
				}//VERIFICA SI NO ES NULO EL NOMBRE DE LA CLASE DAO
			}
			else{
				//GENERA MENSAJE DE ERROR
			}
		}
		catch (SQLException ex) {
			sessionContext.setRollbackOnly();
			bErrorGenerado = true;
			System.out.println("Se genero una SQLException en CatalogoSLBean: " + ex.getMessage());
			try{
				//ASIGNA EL MENSAJE DE ERROR
				Mensaje mensaje = operacionesComunes.getMensaje("CATALOGO_ERROR_XML_BD");
				resultadoCatalogo = new ResultadoCatalogo();
				resultadoCatalogo.mensaje = mensaje;
			}
			catch(Exception e){
				System.out.println("Se gener� un error al asignar el mensaje en el bloque catch(SQLException) de la clase CatalogoSLBean: " + e.getMessage());
			}
		}
		catch (Exception ex) {
			sessionContext.setRollbackOnly();
			bErrorGenerado = true;
			ex.printStackTrace();
			try{
				//ASIGNA EL MENSAJE DE ERROR
				Mensaje mensaje = operacionesComunes.getMensaje("CATALOGO_ERROR_XML");
				resultadoCatalogo = new ResultadoCatalogo();
				resultadoCatalogo.mensaje = mensaje;
			}
			catch(Exception e){
				System.out.println("Se gener� un error al asignar el mensaje en el bloque catch(Exception) de la clase CatalogoSLBean: " + e.getMessage());
			}
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if (conexion != null){
				if (conexion.isConectado()) {
					conexion.cierraConexion();
				}
			}
		}
		//VERIFICA SI SE GENERO UNA EXCEPTION
		if (bErrorGenerado){
			//LE ENVIA A LA CAPA CLIENTE LA NOTIFICACION DE UNA EXCEPTION
			throw new LogicaNegocioException();
		}
		*/
		return resultadoCatalogo;
	}

	/**
	 * @param conexion Objeto tipo Conexion2. 
	 * @param registro Objeto Registro.
	 * @param sCveTabla Clave de la tabla.
	 * @param contexto Objeto tipo Context.
	 * @throws java.lang.Exception Se gener� un error en la baja autom�tica.
	 */
	
	private ResultadoCatalogo bajaAutomatica(Conexion2 conexion, Registro registro, String sCveTabla, Context contexto) throws Exception {
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		ConfiguracionTabla configuracionTabla = new ConfiguracionTabla();
		configuracionTabla.setConexion(conexion.getConexion());
	
		//OBTIENE LAS DEFINICIONES DE LA TABLA
		DefinicionTabla definicionTabla = configuracionTabla.getConfiguracion(sCveTabla, contexto);
		String sNomTabla = definicionTabla.getNomTabla();
		String sBajaLogica = definicionTabla.getBajaLogica();
		LinkedList listaTablasDependientes = definicionTabla.getTablasDependientes();
		LinkedList listaCamposLlave = definicionTabla.getCamposLlavePrimaria();
		String sMensajeDependenciaTablas = "";
	
		//VERIFICA SI ES UNA LISTA DE REGISTROS
		LinkedList listaRegistros = new LinkedList();
		if (registro.getDefCampo("ListaRegistros") != null) {
			//OBTIENE LA LISTA DE REGISTROS
			listaRegistros = (LinkedList) registro.getDefCampo("ListaRegistros");
		}
		else {
			//ASIGNA EL REGISTRO A LA LISTA SI NO EXISTE UNA LISTA DE REGISTROS
			listaRegistros.addFirst(registro);
		}
		Iterator iListaRegistros = listaRegistros.iterator();
		String sCadenaBd = "";
		int iNumCampo = 0;
		int iCantidadRegistros=1;
		
		//VERIFICA SI SE EJECUTA UNA BAJA L�GICA
		if (sBajaLogica.equals("V")){
			//ITERA LOS REGISTROS PARA REALIZAR LA BAJA LOGICA
			while (iListaRegistros.hasNext()) {
				//OBTIENE EL REGISTRO PARA REALIZAR BAJA LOGICA
				registro = (Registro) iListaRegistros.next();
				//OBTIENE LOS CAMPOS DE LA LLAVE PRIMARIA PARA CONSTRUIR LA CONDICION
				String sCamposLlave = null;
				if (definicionTabla.getCamposLlavePrimaria() != null) {
					Iterator listaCmpLlave = definicionTabla.getCamposLlavePrimaria().iterator();
					iNumCampo = 0;
					while (listaCmpLlave.hasNext()) {
						Registro registroLlave = (Registro) listaCmpLlave.next();
						listaCmpLlave.next();
						String sCampoLlave = (String) registroLlave.getDefCampo("COLUMNA");
						String sAgregaCampoCadena = sCampoLlave + " = '" + (String) registro.getDefCampo(sCampoLlave) + "'";
						if (iNumCampo == 0) {
							sCamposLlave = "WHERE " + sAgregaCampoCadena;
						}
						else {
							sCamposLlave = sCamposLlave + " AND " + sAgregaCampoCadena;
						}
						iNumCampo++;
					}
				} //OBTIENE LOS CAMPOS DE LA LLAVE PRIMARIA PARA CONSTRUIR LA CONDICION

				//CONTRUYE LA SENTENCIA UPDATE PARA REALIZAR BAJA LOGICA DEL REGISTRO
				sCadenaBd = "UPDATE " + sNomTabla + " SET SITUACION ='IN' \n" + sCamposLlave;
				//EJECUTA LA BAJA LOGICA EN LA BD
				conexion.sSql = sCadenaBd;
				conexion.ejecutaUpdate();
			}//RECORRE LA LISTA DE REGISTROS QUE VIENEN DE LA CON PARA MODIFICARLOS EN LA BD
		}

		//SE EJECUTA UNA BAJA FISICA
		else{
			//ITERA LOS REGISTROS PARA DAR DE BAJA
			while (iListaRegistros.hasNext()) {
				//OBTIENE EL REGISTRO PARA DAR DE BAJA
				registro = (Registro)iListaRegistros.next();
				//VERIFICA SI LA TABLA TIENE TABLAS DEPENDIENTES
				if (listaTablasDependientes != null){
					//OBTIENE UN MENSAJE SI EL REGISTRO ESTA RELACIONADO CON TABLAS DEPENDIENTES
					sMensajeDependenciaTablas = configuracionTabla.getMensajeDependencia(listaTablasDependientes,listaCamposLlave,registro);
				}
	
				//VERIFICA SI EL REGISTRO ESTA RELACIONADA CON TABLAS DEPENDIENTES
				if (sMensajeDependenciaTablas.equals("")){
	
					if (listaCamposLlave != null){
						//OBTIENE LOS CAMPOS LLAVE PRIMARIA
						Iterator listaCmpLlave = listaCamposLlave.iterator();
	
						//CONTRUYE LA SENTENCIA DELETE PARA DAR DE BAJA
						while (listaCmpLlave.hasNext()){
							Registro registroLlave = (Registro)listaCmpLlave.next();
							String sCampoLlave = (String)registroLlave.getDefCampo("COLUMNA");
							String sTipo  = (String) registroLlave.getDefCampo("DATA_TYPE");
							if(sTipo != null){
								if(sTipo.equals("DATE")	|| sTipo.equals("NUMBER")){
									sTipo = "";	
								}
								else{
									sTipo = "'";	
								}
							}
							else{
								sTipo = "'";	
							}
							String sAgregaCampoCadena = sCampoLlave + " = " + sTipo + (String)registro.getDefCampo(sCampoLlave) + sTipo;
							if (iNumCampo == 0){
								sCadenaBd = "DELETE FROM " + sNomTabla +
									" WHERE " + sAgregaCampoCadena;
							}
							else {
								sCadenaBd = sCadenaBd + " AND " + sAgregaCampoCadena;
							}
							iNumCampo++;
						}
					}
					//BORRA EL REGISTRO AUTOMATICAMENTE
					conexion.sSql = sCadenaBd;
					conexion.ejecutaUpdate();
				}
				else {
					//EL REGISTRO SE ENCUENTRA RELACIONADO CON OTRAS TABLAS
					if(iCantidadRegistros==1){
						//DEFINE MENSAJE EN SI SOLO SE ENCONTRO UN REGISTRO EN LA LISTA Y ESTA RELACIONADO
						sMensajeDependenciaTablas = "El registro que intenta borrar tiene " + sMensajeDependenciaTablas + " asociados";
					}
					else{
						sMensajeDependenciaTablas = "Algunos registros que se intentaron borrar estan relacionados con componentes del sistema.";
					}
					//CREA EL MENSAJE DE LA OPERACION QUE SE GENERO AUTOMATICAMENTE
					resultadoCatalogo.mensaje.setClave("RegistroRelacionado");
					resultadoCatalogo.mensaje.setTipo("No procede la baja ");
					resultadoCatalogo.mensaje.setDescripcion(sMensajeDependenciaTablas);
	
				} //VERIFICA SI LA TABLA ESTA RELACIONADA CON OTRAS TABLAS
				iNumCampo = 0;
				iCantidadRegistros++;
			}//ITERA LOS REGISTROS PARA DAR DE BAJA
		}//END ELSE
	
	
		//CIERRA LA CONEXION DE LA CLASE ConfiguracionTabla
		configuracionTabla.cierraConexion();
		return resultadoCatalogo;
	}

	/**
	 * Ejecuta las operaciones de alta, baja y modificaci�n sobre la base de datos.
	 * en XML.
	 * @param sCatalogo Clave de la funci�n a ejecutar.
	 * @param registro Parametros de entrada que provienen de la clase CON.
	 * @param iTipoOperacion Tipo de operacion a ejecutar.
	 * @return Resultado de la operacion.
	 * @throws LogicaNegocioException Se gener� un error al ejecutar la modifiaci�n.
	 * @ejb.interface-method
	 */
	public ResultadoCatalogo modificacion(String sCatalogo, Registro registro, int iTipoOperacion) throws com.rapidsist.portal.catalogos.LogicaNegocioException {
		boolean bResultado = true;
		Conexion2 conexion = null;
		Conexion2 claseDao = null;
		boolean bErrorGenerado = false;
		OperacionesComunes operacionesComunes = new OperacionesComunes();
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		//OBTIENE EL ITERATOR DE LOS CAMPOS DEL OBJETO REGISTRO
		HashMap camposRegistro = registro.Campos;
		Map mCamposRegistro = camposRegistro;
		Iterator iCamposRegistro = mCamposRegistro.keySet().iterator();

		while (iCamposRegistro.hasNext()) {
			String sValorCampoLlave = (String)iCamposRegistro.next();
			//VERIFICA SI EL OBJETO ES DE TIPO STRING
			if( registro.getDefCampo(sValorCampoLlave) instanceof String ){
				String sDescripcionCampoLlave = (String)registro.getDefCampo(sValorCampoLlave);
				//REEMPLAZA EL APOSTROFRE POR DOBLE APOSTROFE PARA DAR DE ALTA O MODIFICAR EL REGISTRO
				sDescripcionCampoLlave = sDescripcionCampoLlave.replaceAll("'","''");
				registro.addDefCampo(sValorCampoLlave,sDescripcionCampoLlave);				
			}
		}

		try {
			//OBTIENE EL NOMBRE DE LA CLASE DAO ASOCIADA A LA FUNCION
			Context contexto= new InitialContext();
			Funcion funcion = operacionesComunes.getFuncion(sCatalogo);

			//SE ABRE LA CONEXION A LA BASE DE DATOS Y SE PASA A LA CLASE DAO
			conexion = new Conexion2();
			conexion.abreConexion("java:comp/env/jdbc/PortalDs");

			//VERIFICA SI ENCONTRO LA FUNCION
			if (funcion != null){

				//OBTIENE LA CLAVE DE LA TABLA ASIGNADA A LA FUNCION
				String sCveTabla = funcion.getCveTabla();

				//VERIFICA SI LA FUNCION TIENE ASIGNADA UNA CLASE DAO
				if ((funcion.getClaseDao() != null) && (!funcion.equals(""))){

					//SE INSTANCIA LA CLASE DAO
					Class cClase = Class.forName(funcion.getClaseDao());
					Object instanciaObj=cClase.newInstance();

					//VERIFICA SI ES UNA BAJA
					if (iTipoOperacion == CatalogoSLBean.BAJA){
						//VERIFICA SI SE GENERA AUTOMATICAMENTE LA OPERACION DE BAJA
						if (instanciaObj instanceof OperacionBaja){
							claseDao = (Conexion2)instanciaObj;
							claseDao.setConexion(conexion.getConexion());
							resultadoCatalogo = ((OperacionBaja)instanciaObj).baja(registro);
						}
						else{
							resultadoCatalogo = bajaAutomatica(conexion, registro, sCveTabla, contexto);
						}//VERIFICA SI SE GENERA AUTOMATICAMENTE LA OPERACION DE BAJA
					}
					else {
						//VERIFICA SI ES UNA ALTA
						if (iTipoOperacion == CatalogoSLBean.ALTA){
							boolean bDobleSubmit = false;
							//SE RECUPERA EL REGISTRO QUE FUE PREVIAMENTE DADO DE ALTA EN LA SESION
							Registro registroPrimerSubmit = (Registro)registro.getDefCampo("RegistroPrimerSubmit");
							if (registroPrimerSubmit != null){
								registro.eliminaCampo("RegistroPrimerSubmit");
							    /*
								System.out.println("Primer registro");
								Iterator listaNombresCampos = registro.getCampos().keySet().iterator();
								while (listaNombresCampos.hasNext()){
									String sNombreCampo = (String)listaNombresCampos.next();
									Object objetoLista = registro.getDefCampo(sNombreCampo);
									System.out.println("Campo: "+sNombreCampo + "Valor: "+objetoLista);
								}
								System.out.println("Segundo registro submit");
								Iterator listaNombresCamposSubmit = registroPrimerSubmit.getCampos().keySet().iterator();
								while (listaNombresCamposSubmit.hasNext()){
									String sNombreCampo = (String)listaNombresCamposSubmit.next();
									Object objetoLista = registro.getDefCampo(sNombreCampo);
									System.out.println("Campo: "+sNombreCampo + "Valor: "+objetoLista);
								}
								*/
								if (registro.igual(registroPrimerSubmit)){
									//VALIDA SI EL REGISTRO A INSERTAR TIENE UNA LISTA DE REGISTROS
									//SI TIENE UNA LISTA DE REGISTROS POR EL MOMENTO NO SE PUEDEN COMPARAR
									if (registro.getDefCampo("ListaRegistros") == null) {
										//SE HA GENERADO UN DOBLE SUBMIT
										bDobleSubmit = true;
									}
								}
							}
							//System.out.println("bDobleSubmit:"+ bDobleSubmit);
							//SI SE GENERO UN DOBLE SUBMIT NO DA DE ALTA EL REGISTRO
							if (!bDobleSubmit){
								//VERIFICA SI SE GENERA AUTOMATICAMENTE LA OPERACION DE ALTA
								if (instanciaObj instanceof OperacionAlta){
									claseDao = (Conexion2)instanciaObj;
									claseDao.setConexion(conexion.getConexion());
									resultadoCatalogo = ((OperacionAlta)instanciaObj).alta(registro);
								}
								else{
									//PARA POR AQUI CUANDO EXISTE UNA DAO PERO NO IMPLEMENTA LA INTERFACE DE ALTA
									resultadoCatalogo = altaAutomatica(conexion, registro, sCveTabla, contexto);
								}//VERIFICA SI SE GENERA AUTOMATICAMENTE LA OPERACION DE ALTA
								
							}
							//SE AGREGA AL RESULTADO EL OBJETO REGISTRO QUE FUE DADO DE ALTA
							if (resultadoCatalogo.Resultado == null){
								resultadoCatalogo.Resultado = new Registro();
							}
							resultadoCatalogo.Resultado.addDefCampo("RegistroPrimerSubmit", registro);
						}
						else{
							//REALIZA UNA MODIFICACION
							//VERIFICAMOS SI SE DEBE DESHABILITAR LA VERIFICACION DE RE-READ
							String sOperacionInfraestrucuta = (String)registro.getDefCampo("OperacionInfraestructura");
							if (sOperacionInfraestrucuta == null || !sOperacionInfraestrucuta.equals("DeshabilitaReRead")){

								//COMPONENTE RE-READ
								//OBTINE EL REGISTRO ORIGINAL
								Registro registroOriginal = (Registro) registro.getDefCampo("RegistroOriginal");
								//OBTIENE LA ULTIMA VERSION DE LA BASE DE DATOS DEL REGISTRO A MODIFICAR
								Registro registroOriginalCopia = new Registro();
								registroOriginal.copiaRegistro(registroOriginalCopia);

								Registro registroActual =  getRegistro(sCatalogo,registroOriginalCopia);

								// �SON IGUALES EL REGISTRO ORIGINAL ORIGINAL Y LA DE LA BASE DE DATOS?
								if (registroActual.igual(registroOriginal)) {
									//VERIFICA SI SE GENERA AUTOMATICAMENTE LA OPERACION DE MODIFICACION
									if (instanciaObj instanceof OperacionModificacion){
										claseDao = (Conexion2)instanciaObj;
										claseDao.setConexion(conexion.getConexion());
										resultadoCatalogo = ((OperacionModificacion)instanciaObj).modificacion(registro);
									}
									else{
										//SE GENERA AUTOMATICAMENTE LA OPERACION DE MODIFICACION
										resultadoCatalogo = modificacionAutomatica(conexion, registro, sCveTabla, contexto);
									}//VERIFICA SI SE GENERA AUTOMATICAMENTE LA OPERACION DE MODIFICACION
								}
								else {
									resultadoCatalogo.mensaje.setClave("CATALOGO_REG_MODIFICADO");
								}
							}
							else{
								//VERIFICA SI SE GENERA AUTOMATICAMENTE LA OPERACION DE MODIFICACION
								if (instanciaObj instanceof OperacionModificacion){
									claseDao = (Conexion2)instanciaObj;
									claseDao.setConexion(conexion.getConexion());
									resultadoCatalogo = ((OperacionModificacion)instanciaObj).modificacion(registro);
								}
								else{
									//SE GENERA AUTOMATICAMENTE LA OPERACION DE MODIFICACION
									resultadoCatalogo = modificacionAutomatica(conexion, registro, sCveTabla, contexto);
								}//VERIFICA SI SE GENERA AUTOMATICAMENTE LA OPERACION DE MODIFICACION
							}//MODIFICACION: VERIFICAMOS SI SE DEBE DESHABILITAR LA VERIFICACION DE RE-READ
						}//VERIFICA SI ES ALTA
					}//VERIFICA SI ES BAJA
				}
				else{
					//VERIFICA SI ESTA ASIGNADA LA TABLA A LA FUNCION
					if ((sCveTabla != null) && (!sCveTabla.equals(""))){
						//VERIFICA LA OPERACION A EJECUTAR SOBRE LA BASE DE DATOS
						if (iTipoOperacion == CatalogoSLBean.BAJA){
							resultadoCatalogo = bajaAutomatica(conexion, registro, sCveTabla, contexto);
						}
						else if (iTipoOperacion == CatalogoSLBean.ALTA) {
							
							boolean bDobleSubmit = false;
							//SE RECUPERA EL REGISTRO QUE FUE PREVIAMENTE DADO DE ALTA EN LA SESION
							Registro registroPrimerSubmit = (Registro)registro.getDefCampo("RegistroPrimerSubmit");
							if (registroPrimerSubmit != null){
								registro.eliminaCampo("RegistroPrimerSubmit");
								if (registro.igual(registroPrimerSubmit)){
									//VALIDA SI EL REGISTRO A INSERTAR TIENE UNA LISTA DE REGISTROS
									//SI TIENE UNA LISTA DE REGISTROS POR EL MOMENTO NO SE PUEDEN COMPARAR
									if (registro.getDefCampo("ListaRegistros") == null) {
										//SE HA GENERADO UN DOBLE SUBMIT
										bDobleSubmit = true;
									}								}
							}
							//SI SE GENERO UN DOBLE SUBMIT NO DA DE ALTA EL REGISTRO
							if (!bDobleSubmit){							
								resultadoCatalogo = altaAutomatica(conexion, registro, sCveTabla, contexto);
							}
							//SE AGREGA AL RESULTADO EL OBJETO REGISTRO QUE FUE DADO DE ALTA
							if (resultadoCatalogo.Resultado == null){
								resultadoCatalogo.Resultado = new Registro();
							}
							resultadoCatalogo.Resultado.addDefCampo("RegistroPrimerSubmit", registro);							
							
						}
						else if (iTipoOperacion == CatalogoSLBean.MODIFICACION) {
							//VERIFICAMOS SI SE DEBE DESHABILITAR LA VERIFICACION DE RE-READ
							String sOperacionInfraestrucuta = (String)registro.getDefCampo("OperacionInfraestructura");
							if (sOperacionInfraestrucuta == null || !sOperacionInfraestrucuta.equals("DeshabilitaReRead")){

								//COMPONENTE RE-READ
								//OBTINE EL REGISTRO ORIGINAL
								Registro registroOriginal = (Registro) registro.getDefCampo("RegistroOriginal");
								//OBTIENE LA ULTIMA VERSION DE LA BASE DE DATOS DEL REGISTRO A MODIFICAR
								Registro registroOriginalCopia = new Registro();
								registroOriginal.copiaRegistro(registroOriginalCopia);

								Registro registroActual =  getRegistro(sCatalogo,registroOriginalCopia);

								// �SON IGUALES EL REGISTRO ORIGINAL ORIGINAL Y LA DE LA BASE DE DATOS?
								if (registroActual.igual(registroOriginal)) {
									resultadoCatalogo = modificacionAutomatica(conexion, registro, sCveTabla, contexto);
								}
								else {
									resultadoCatalogo.mensaje.setClave("CATALOGO_REG_MODIFICADO");
								}
							}
						}//VERIFICA LA OPERACION A EJECUTAR SOBRE LA BASE DE DATOS
					}//VERIFICA SI ESTA ASIGNADA LA TABLA A LA FUNCION
				}//VERIFICA SI LA FUNCION TIENE ASIGNADA UNA CLASE DAO

				//VERIFICA SI SE GENERO ALGUN MENSAJE DURANTE LA OPERACI�N
				if (resultadoCatalogo.mensaje.getClave() != null){
					//VERIFICA SI EL MENSAJE NO SE GENERA AUTOMATICAMENTE
					if(resultadoCatalogo.mensaje.getDescripcion() == null){
						Mensaje mensaje = operacionesComunes.getMensaje(resultadoCatalogo.mensaje.getClave());
						resultadoCatalogo.mensaje = mensaje;
					}
				}
			}
			else{
				//GENERA MENSAJE DE ERROR
			}//VERIFICA SI ENCONTRO LA FUNCION
		}
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			bErrorGenerado = true;
		}
		catch (IllegalAccessException ex) {
			ex.printStackTrace();
			bErrorGenerado = true;
		}
		catch (InstantiationException ex) {
			ex.printStackTrace();
			bErrorGenerado = true;
		}
		catch (SQLException ex) {
			sessionContext.setRollbackOnly();
			bErrorGenerado = false;
			try{
				if(ex.getErrorCode()==1){
					//ASIGNA EL MENSAJE DE ERROR
					Mensaje mensaje = operacionesComunes.getMensaje("CATALOGO_REG_YA_EXISTE");
					resultadoCatalogo = new ResultadoCatalogo();
					resultadoCatalogo.mensaje = mensaje;
					
				}else{
					//ASIGNA EL MENSAJE DE ERROR
					Mensaje mensaje = operacionesComunes.getMensaje("CATALOGO_ERROR_DAO_BD");
					resultadoCatalogo = new ResultadoCatalogo();
					resultadoCatalogo.mensaje = mensaje;
				}
			}
			catch(Exception e){
			}
		}
		catch (Exception ex) {
			bErrorGenerado = true;
			ex.printStackTrace();
			try{
				//ASIGNA EL MENSAJE DE ERROR
				Mensaje mensaje = operacionesComunes.getMensaje("CATALOGO_ERROR_DAO");
				resultadoCatalogo = new ResultadoCatalogo();
				resultadoCatalogo.mensaje = mensaje;
			}
			catch(Exception e){
			}
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if (claseDao != null){
				if (claseDao.isConectado()) {
					claseDao.cierraConexion();
				}
			}
			if (conexion != null){
				if (conexion.isConectado()) {
					conexion.cierraConexion();
				}
			}
		}
		//VERIFICA SI SE GENERO UNA EXCEPTION
		if (bErrorGenerado){
			//LE ENVIA A LA CAPA CLIENTE LA NOTIFICACION DE UNA EXCEPTION
			throw new LogicaNegocioException();
		}
		return resultadoCatalogo;
	}

	/**
	 * @param conexion  Objeto tipo Conexion2.
	 * @param registro Objeto registro.
	 * @param sCveTabla Clave de la tabla.
	 * @param contexto Objeto tipo Context
	 * @throws java.lang.Exception Se gener� un error al hacer la modificaci�n autom�tica.
	 */
	private ResultadoCatalogo modificacionAutomatica(Conexion2 conexion, Registro registro, String sCveTabla, Context contexto) throws Exception {
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		ConfiguracionTabla configuracionTabla = new ConfiguracionTabla();
		configuracionTabla.setConexion(conexion.getConexion());
		boolean bError = false;

		//OBTIENE LAS DEFINICIONES DE LA TABLA
		DefinicionTabla definicionTabla = configuracionTabla.getConfiguracion(sCveTabla, contexto);

		//VERIFICA SI ENCONTRO LA DEFINICION DE LA TABLA
		if (definicionTabla != null){

			String sNomTabla = definicionTabla.getNomTabla();

			//VERIFICA SI ES UNA LISTA DE REGISTROS
			LinkedList listaRegistros = new LinkedList();
			if (registro.getDefCampo("ListaRegistros") != null) {
				//OBTIENE LA LISTA DE REGISTROS
				listaRegistros = (LinkedList) registro.getDefCampo("ListaRegistros");
			}
			else {
				//ASIGNA EL REGISTRO A LA LISTA SI NO EXISTE UNA LISTA DE REGISTROS
				listaRegistros.addFirst(registro);
			}
			Iterator iListaRegistros = listaRegistros.iterator();
			String sCadenaBd = "";
			int iNumCampo = 0;

			//RECORRE LA LISTA DE REGISTROS QUE VIENEN DE LA CON PARA MODIFICARLOS EN LA BD
			while (iListaRegistros.hasNext()) {
		
				//OBTIENE EL REGISTRO PARA DAR DE ALTA
				registro = (Registro) iListaRegistros.next();

				//OBTIENE LOS CAMPOS DE LA LLAVE PRIMARIA PARA CONSTRUIR LA CONDICION
				String sCamposLlave = null;
				if (definicionTabla.getCamposLlavePrimaria() != null) {
					Iterator listaCmpLlave = definicionTabla.getCamposLlavePrimaria().iterator();
					iNumCampo = 0;
					while (listaCmpLlave.hasNext()) {
						Registro registroLlave = (Registro) listaCmpLlave.next();
						String sCampoLlave = (String) registroLlave.getDefCampo("COLUMNA");
						String sAgregaCampoCadena = sCampoLlave + " = '" + (String) registro.getDefCampo(sCampoLlave) + "'";
						if (iNumCampo == 0) {
							sCamposLlave = "WHERE " + sAgregaCampoCadena;
						}
						else {
							sCamposLlave = sCamposLlave + " AND " + sAgregaCampoCadena;
						}
						iNumCampo++;
					}
				} //OBTIENE LOS CAMPOS DE LA LLAVE PRIMARIA PARA CONSTRUIR LA CONDICION

				LinkedList listaCamposSentencia = null;
				Iterator iListaCamposTabla = definicionTabla.getCamposTabla().iterator();
				//CONTRUYE LA SENTENCIA UPDATE PARA ACTUALIZAR EL REGISTRO
				iNumCampo = 0;
				while (iListaCamposTabla.hasNext()) {
					Registro registroCampo = (Registro) iListaCamposTabla.next();
					String sCampo = (String) registroCampo.getDefCampo("COLUMNA");
					Object valorCampo = (Object) registro.getDefCampo(sCampo);

					//VERIFICA SI ENCONTRO EL CAMPO
					if (valorCampo != null) {
						//OBTIENE EL VALOR DEL CAMPO
						if (listaCamposSentencia == null) {
							listaCamposSentencia = new LinkedList();
						}
						listaCamposSentencia.add(sCampo);
					}
					else{
						bError = true;
						resultadoCatalogo.mensaje.setClave("CAMPOS_INCOMPLETOS_CON");
						break;
					}//VERIFICA SI ENCONTRO EL CAMPO

					if (iNumCampo == 0) {
						sCadenaBd = "UPDATE " + sNomTabla + " SET \n" + sCampo + "= ? \n";
					}
					else {
						sCadenaBd = sCadenaBd + "," + sCampo + " = ? \n";
					}
					iNumCampo++;
				}

				//VERIFICA SI TODOS LOS CAMPOS SE ENCONTRARON
				if (!bError){
					sCadenaBd = sCadenaBd + sCamposLlave;
					ejecutaSentenciaBd(conexion, sCadenaBd, listaCamposSentencia, definicionTabla, registro, resultadoCatalogo);
				}
	
				else{
					//IGNORA LA LISTA DE REGISTROS QUE SE ENVIAN A LA BASE DE DATOS
					break;
				}//VERIFICA SI TODOS LOS CAMPOS SE ENCONTRARON
			}//RECORRE LA LISTA DE REGISTROS QUE VIENEN DE LA CON PARA MODIFICARLOS EN LA BD
		}
		else{
			resultadoCatalogo.mensaje.setClave("ERROR_DEFINICION_TABLA");
		}//VERIFICA SI ENCONTRO LA DEFINICION DE LA TABLA

		//OBTIENE LAS DEFINICIONES DE LA TABLA
		return resultadoCatalogo;
	}

	/**
	 * @param conexion Objeto tipo Conexion2.
	 * @param sSentenciaPreviaSql Sentencia previa.
	 * @param listaCamposSentencia Lista de campos de la sentencia.
	 * @param definicionTabla objeto DefinicionTabla.
	 * @param registro Objeto registro
	 * @param resultadoCatalogo Objeto donde se almacena el resultado de la operaci�n.
	 * @throws SQLException Se gener� un error al ejecutar la sentencia.
	 */
	 
	private void ejecutaSentenciaBd(Conexion2 conexion, String sSentenciaPreviaSql, LinkedList listaCamposSentencia, DefinicionTabla definicionTabla, Registro registro, ResultadoCatalogo resultadoCatalogo) throws SQLException {
		PreparedStatement sentencia = conexion.conn.prepareStatement(sSentenciaPreviaSql);
		Iterator iListaCamposSentencia = listaCamposSentencia.iterator();
		//AGREGA LOS CAMPOS AL PREPARED-STATEMENT
		int iNumCampo = 1;
		while (iListaCamposSentencia.hasNext()) {
			String sCampo = (String) iListaCamposSentencia.next();
			Registro campo = (Registro)definicionTabla.getMapaCamposTabla().get(sCampo);
			int iTipoCampo = ((Integer)campo.getDefCampo("TIPO_DATO")).intValue();
			Object valorCampo = registro.getDefCampo(sCampo);
			if ( iTipoCampo == java.sql.Types.TIMESTAMP){
				if (valorCampo instanceof String){
					Date datoFecha = Fecha2.toDate((String)valorCampo);
					java.sql.Timestamp tiempoFecha = new java.sql.Timestamp(datoFecha.getTime());
					sentencia.setTimestamp(iNumCampo, tiempoFecha);
				}
				else{
					//EL VALOR DEL CAMPO VIENE EN UN OBJETO DATE
					java.sql.Timestamp tiempoFecha = new java.sql.Timestamp( ((java.util.Date)valorCampo).getTime());
					sentencia.setTimestamp(iNumCampo, tiempoFecha);
				}
			}
			else if ( iTipoCampo == java.sql.Types.DATE ){
				if (valorCampo instanceof String){
					Date datoFecha = Fecha2.toDate((String)valorCampo);
					java.sql.Date datoFechaBd = new java.sql.Date(datoFecha.getTime());
					sentencia.setDate(iNumCampo, datoFechaBd);
				}
				else{
					//EL VALOR DEL CAMPO VIENE EN UN OBJETO DATE
					java.sql.Date tiempoFecha = new java.sql.Date( ((java.util.Date)valorCampo).getTime());
					sentencia.setDate(iNumCampo, tiempoFecha);
				}
			}
			else if ( iTipoCampo == java.sql.Types.LONGVARCHAR){
				StringReader cadena = new StringReader((String)valorCampo);
				sentencia.setCharacterStream(iNumCampo, cadena, ((String)valorCampo).length());
			}
			else if ( iTipoCampo == java.sql.Types.NUMERIC){
				sentencia.setFloat(iNumCampo,Float.parseFloat(valorCampo.toString()));
			}
			else{
				sentencia.setString(iNumCampo, (String)valorCampo);
			}
			iNumCampo++;
		}
		boolean bErrorSql = false;
		SQLException exceptionGenerada = null;
		try{
			//OBTIENE EL NUMERO DE REGISTROS MODIFICADOS. 1=EXITO
			int iRegistrosModificados = sentencia.executeUpdate();
			//VERIFICA SI SE EJECUTO LA MODIFICACION
			if (iRegistrosModificados < 1) {
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		}
		catch (SQLException e){
			bErrorSql = true;
			exceptionGenerada = e;
		}
		finally{
			sentencia.close();
			if (bErrorSql){
				throw exceptionGenerada;
			}
		}//FINALLY

	}
	
	/**
	 * @param conexion Objeto Conexion2. 
	 * @param registro Objeto Registro.
	 * @param sCveTabla Clave de la tabla.
	 * @param contexto Objeto tipo Context
	 * @throws java.lang.Exception Se gener� un error en la alta autom�tica. 
	 */

	private ResultadoCatalogo altaAutomatica(Conexion2 conexion, Registro registro, String sCveTabla, Context contexto) throws Exception {
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		ConfiguracionTabla configuracionTabla = new ConfiguracionTabla();
		configuracionTabla.setConexion(conexion.getConexion());
		String sSequence = "";
		String sSequenceLlave = "";
		//OBTIENE LAS DEFINICIONES DE LA TABLA
		DefinicionTabla definicionTabla = configuracionTabla.getConfiguracion(sCveTabla, contexto);
		
		//VERIFICA SI ENCONTRO LA DEFINICION DE LA TABLA
		if (definicionTabla != null){
			String sNomTabla = definicionTabla.getNomTabla();

			//VERIFICA SI ES UNA LISTA DE REGISTROS
			LinkedList listaRegistros = new LinkedList();
			if (registro.getDefCampo("ListaRegistros") != null) {
				//OBTIENE LA LISTA DE REGISTROS
				listaRegistros = (LinkedList) registro.getDefCampo("ListaRegistros");
			}
			else {
				//ASIGNA EL REGISTRO A LA LISTA SI NO EXISTE UNA LISTA DE REGISTROS
				listaRegistros.addFirst(registro);
			}
			Iterator iListaRegistros = listaRegistros.iterator();

			String sCadenaBd = "";

			//RECORRE LA LISTA DE REGISTROS QUE VIENEN DE LA CON PARA DAR DE ALTA EN LA BD
			while (iListaRegistros.hasNext()) {

				//OBTIENE EL REGISTRO PARA DAR DE ALTA
				registro = (Registro) iListaRegistros.next();
				//BUSCA SI EXISTE EL REGISTRO A DAR DE ALTA
				Registro registroAlta = null;
				registroAlta = getRegistroAutomatico(conexion, sCveTabla, contexto, registro);
				//VERIFICA SI ENCONTRO EL REGISTRO A DAR DE ALTA
				if (registroAlta == null) {
				
					Iterator iListaCamposTabla = definicionTabla.getCamposTabla().iterator();
					String sCadenaDatos = "";
					LinkedList listaCamposSentencia = null;
					int iNumCampo = 0;
					boolean bError = false;
	
					//CONTRUYE LA SENTENCIA INSERT PARA DAR DE ALTA
					while (iListaCamposTabla.hasNext()) {
						Registro registroCampo = (Registro) iListaCamposTabla.next();
						String sCampo = (String) registroCampo.getDefCampo("COLUMNA");
						String sValorCampo = "";					
	
						//OBTIENE LAS PROPIEDADES DE LOS CAMPOS
						LinkedList listaPropiedadesCampos = configuracionTabla.getPropiedadesCampos(sCveTabla);
						Iterator iListaPropiedadesCampos = listaPropiedadesCampos.iterator();
	
						while (iListaPropiedadesCampos.hasNext()){
							Registro registroPropiedadesCampos = (Registro) iListaPropiedadesCampos.next();
							if (sCampo.equals( (String) registroPropiedadesCampos.getDefCampo("NOM_CAMPO"))) {
								//VALIDA SI EL CAMPO ES AUTOINCREMENTABLE
								String sAutoIncrementable = (String) (registroPropiedadesCampos.getDefCampo("B_AUTO_INCREMENTABLE"));
								if (sAutoIncrementable.equals("V")){
									//OBTIENE EL VALOR DEL SEQUENCE								
									sValorCampo = getSequence(conexion, (String)registroPropiedadesCampos.getDefCampo("NOM_SECUENCIA"));
									sSequence = sValorCampo;
								}
								String sCampoLlave = (String) (registroPropiedadesCampos.getDefCampo("B_CAMPO_LLAVE"));
								if (sCampoLlave.equals("V")){
									sSequenceLlave = sValorCampo;
								}
							}
						}//OBTIENE LAS PROPIEDADES DE LOS CAMPOS
						
						
						//VERIFICA SI AL VALOR DEL CAMPO "NO" SE LE ASIGNO UNA SECUENCIA
						if (sValorCampo.equals("")) {
	
							//OBTIENE EL VALOR DEL CAMPO
							Object valorCampo = registro.getDefCampo(sCampo);
							//VERIFICA SI ENCONTRO EL CAMPO
							if (valorCampo != null){
								if (listaCamposSentencia == null) {
									listaCamposSentencia = new LinkedList();
								}
								listaCamposSentencia.add(sCampo);
							}
							else{
								bError = true;
								resultadoCatalogo.mensaje.setClave("CAMPOS_INCOMPLETOS_CON");
								System.out.println("CatalogosSLBean, altaAutomatica: no se encontr� el campo '" + sCampo + "' en el registro que viene de la CON");
								break;
							} //VERIFICA SI ENCONTRO EL CAMPO
							sValorCampo = " ? ";
						}//VERIFICA SI AL VALOR DEL CAMPO "NO" SE LE ASIGNO UNA SECUENCIA
	
						if (iNumCampo == 0){
							sCadenaBd = " INSERT INTO " + sNomTabla + " (" + sCampo;
							sCadenaDatos = " \n VALUES(" + sValorCampo;
						}
						else {
							sCadenaBd = sCadenaBd + ",\n" + sCampo;
							sCadenaDatos = sCadenaDatos + ",\n " + sValorCampo;
						}
						iNumCampo++;
					}//CONTRUYE LA SENTENCIA INSERT PARA DAR DE ALTA
	
					//VERIFICA SI TODOS LOS CAMPOS SE ENCONTRARON
					if (!bError){
						sCadenaBd = sCadenaBd + ") " + sCadenaDatos + ")";
						ejecutaSentenciaBd(conexion, sCadenaBd, listaCamposSentencia, definicionTabla, registro, resultadoCatalogo);
						// SI SE REALIZ� UN ALTA CON UN VALOR SEQUENCE EN LA LLAVE PRIMARIA ESTE SE ENV�A
						// DE REGRESO AL SERVLET PARA PODER GENERAR LA BIT�CORA DEL REGISTRO
						if(!sSequence.equals("")){
							resultadoCatalogo.Resultado = new Registro();
							resultadoCatalogo.Resultado.addDefCampo("SEQUENCE", sSequenceLlave);
						}
					}
					else{
						//IGNORA LA LISTA DE REGISTROS QUE SE ENVIAN A LA BASE DE DATOS
						break;
					}//VERIFICA SI TODOS LOS CAMPOS SE ENCONTRARON
					
				}
				else{
					//EL REGISTRO QUE SE DESEA INSERTAR YA EXISTE
					resultadoCatalogo.mensaje.setClave("CATALOGO_REG_YA_EXISTE");
				}//VERIFICA SI ENCONTRO EL REGISTRO A DAR DE ALTA
			}//RECORRE LA LISTA DE REGISTROS QUE VIENEN DE LA CON PARA DAR DE ALTA EN LA BD
		}
		else{
			resultadoCatalogo.mensaje.setClave("ERROR_DEFINICION_TABLA");
		}//VERIFICA SI ENCONTRO LA DEFINICION DE LA TABLA
		return resultadoCatalogo;
	}
	/**
	 * M�todo que obtiene el valor de un sequence para utilizarlo en una alta autom�tica.
	 * @param conexion Objeto Conexion2 para realizar la conexi�n al base de datos.
	 * @param SNomSequence Nombre del sequence.
	 * @return Objeto String con el valor del sequence.
	 * @throws SQLException Se gener� un error al obtener el valor del sequence.
	 */
	private String getSequence(Conexion2 conexion,String SNomSequence)throws SQLException {
		String sSequence = "";
		sSequence = " SELECT " + SNomSequence + ".nextval as SEQUENCE_NEXTVAL FROM DUAL ";
		PreparedStatement sentencia = conexion.conn.prepareStatement(sSequence);
		ResultSet res = sentencia.executeQuery(sSequence);
		if(res.next()){
			sSequence = res.getString("SEQUENCE_NEXTVAL");
		}	
		res.close();
		sentencia.close();
		return sSequence;
	}
}
