/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.bitacora.datos; 

import java.io.*;
import java.rmi.*;
import java.sql.*;
import java.util.*;
import javax.ejb.*;
import org.jdom.*;
import org.jdom.input.*;
import com.rapidsist.comun.bd.*;
import com.rapidsist.comun.util.*;
import com.rapidsist.portal.catalogos.*;
import com.rapidsist.portal.catalogos.LogicaNegocioException;

/**
 * Ejb que ejecuta las operaciones de registro y consulta de la bitacora.
 * 
 * @ejb.bean
 *     name="BitacoraSL"
 *     type="Stateless"
 *     jndi-name="BitacoraSL"
 *     local-jndi-name="BitacoraSLLocal"
 * @ejb.transaction type="Required"
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
public class BitacoraSLBean
	implements SessionBean {

	/**
	 * Contexto del EJB
	 */
	SessionContext sessionContext;

	/**
	 * Este método es requerido por la especificación J2EE, pero no tiene
	 * implementado ningún código.
	 */
	public void ejbCreate() {
	}

	/**
	 * Este método es requerido por la especificación J2EE, pero no tiene
	 * implementado ningún código.
	 */
	public void ejbRemove() {
	}

	/**
	 * Este método es requerido por la especificación J2EE, pero no tiene
	 * implementado ningún código.
	 */
	public void ejbActivate() {
	}

	/**
	 * Este método es requerido por la especificación J2EE, pero no tiene
	 * implementado ningún código.
	 */
	public void ejbPassivate() {
	}

	/**
	 * Inicializa el contexto de la sesion para el EJB.
	 * @param sessionContext Contexto de la sesion que provee información al EJB sobre
	 * el contenedor.
	 */
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	// SE INSTANCIA LA CLASE DAO
	private BitacoraDAO bitacora = new BitacoraDAO();
	// SE DECLARA LA LISTA QUE CONTENDRA LOS CAMPOS LLAVE DE UNA TABLA
	private LinkedList listaCamposLlave = null;
	// PROPIEDAD QUE ALMACENA EL REGISTRO ENCONTRADO EN LA TABLA RS_BITACORA
	private Registro registroBitacora = null;

	/**
	 * Registra la visita que hace un usuario a una funcion del sistema.
	 * @param sCveGpoEmpresa Clave del grupo de empresas.
	 * @param sCvePortal Clave del portal.
	 * @param sCveFuncion Es la clave de la función solicitada al sistema.
	 * @param sNomFuncion Nombre de la función.
	 * @param sCveUsuario Clave del usuario.
	 * @param sNomUsuario Nombre del usuario.
	 * @param sUrlServidor Url del servidor.
	 * @param sParametros Objeto registro.
	 * @param sDireccionIpUsuario Dirección ip del usuario.
	 * @throws LogicaNegocioException Se genero un error al registrar la función.
	 * @ejb.interface-method
	 */
	public void registraVisitaFuncion(String sCveGpoEmpresa, String sCvePortal, String sCveFuncion, String sNomFuncion, String sCveUsuario, String sNomUsuario, String sUrlServidor, String sParametros, String sDireccionIpUsuario)throws com.rapidsist.portal.catalogos.LogicaNegocioException {
		Conexion2 conexion = null;
		boolean bError = false;
		try{
			//SE ABRE LA CONEXION A LA BASE DE DATOS Y SE PASA A LA CLASE DAO
			conexion = new Conexion2();
			conexion.abreConexion("java:comp/env/jdbc/PortalDs");
			bitacora.setConexion(conexion.getConexion());
			bitacora.registraVisitaFuncion(sCveGpoEmpresa, sCvePortal, sCveFuncion, sNomFuncion, sCveUsuario, sNomUsuario, sUrlServidor, sParametros, sDireccionIpUsuario);
		}
		catch (SQLException ex) {
			System.out.println("Se genero una SQLException en BitacoraSLBean: " + ex.getMessage());
			ex.printStackTrace();
			bError = true;
		}
		catch (Exception e) {
			System.out.println("Se generó un Exception en BitacoraSLBean:: " + e.getMessage());
			e.printStackTrace();
			bError = true;
		}
		finally {
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if (conexion.isConectado()) {
				conexion.cierraConexion();
			}
		}
		if (bError){
			throw new LogicaNegocioException();
		}
	}
	
	/**
	 * Almacena en formato XML los movimientos realizados en los registros de las tablas
	 * relacionadas a una función.
	 * @param sCveFuncion Es la clave de la función.
	 * @param registro Objeto registro con los datos de los campos llave de cada tabla.
	 * @return <code> True </code> Si la actualización tuvo éxito.
	 * @throws LogicaNegocioException Se generó un error al registrar la bitácora.
	 * @ejb.interface-method
	 */
	public String registraBitacora(String sCveFuncion, com.rapidsist.comun.bd.Registro registro)throws com.rapidsist.portal.catalogos.LogicaNegocioException {
		Conexion2 conexion = null;
		String sNomTabla = null;
		listaCamposLlave = null;
		String sResultado = "";
		boolean bError = false;

		// SE INICIALIZA LA LISTA
		try {
			//SE ABRE LA CONEXION A LA BASE DE DATOS Y SE PASA A LA CLASE DAO
			conexion = new Conexion2();
			conexion.abreConexion("java:comp/env/jdbc/PortalDs");

			bitacora.setConexion(conexion.getConexion());
			// FUNCIONES A LLAMAR
			// VERIFICA SI LA FUNCION BITACORA, ESTO LO HACE OBTENIENDO UNA LISTA
			// Y SI LA LISTA ES DIFERENTE DE NULO SIGNIFICA QUE SI PUEDE BITACORAR
			// REGRESANDO EL NOMBRE DE LAS TABLAS QUE DEBE BITACORAR
			LinkedList lListaTablas = verificaFuncionBitacora(sCveFuncion);

			// SI LA LISTA DE TABLAS ES NULA, ENTONCES ESTA FUNCION NO BITACORA
			if (lListaTablas != null){

				Iterator iLista = lListaTablas.iterator();
				// PARA CADA TABLA SE DEBEN OBTENER SUS CAMPOS LLAVE
				while (iLista.hasNext()) {
					Registro registroTabla = (Registro) iLista.next();

					if (registroTabla.getDefCampo("SIN_TABLA") == null) {
						sNomTabla = (String) registroTabla.getDefCampo("CVE_TABLA");

						// 1. OBTIENE LOS CAMPOS LLAVE DE LA TABLA QUE SE VA A BITACORAR Y
						// 2. BUSCA EL REGISTRO EN LA TABLA QUE BITACORA.
						// SI ENCUENTRA EL REGISTRO LO ACTUALIZA CON LOS NUEVOS MOVIMIENTOS
						// EN CASO CONTRARIO CREA EL REGISTRO CON LOS NUEVOS MOVIMIENTOS

						registroBitacora = this.getRegistroBitacora(sNomTabla, registro);

						if (registroBitacora != null) {
							// VERIFICA SI SE ENVIARON CAMPOS VACÍOS Y NO SE PUDO OBTENER EL REGISTRO DE LA BITÁCORA
							if (registroBitacora.getDefCampo("CAMPO_VACIO") != null) {
								sResultado = "No se generó la bitácora, se ha enviado vacío o nulo el campo : " + registroBitacora.getDefCampo("CAMPO_VACIO") + ", verifique si se trata de una alta y un sequence.";
							}
							else {

								// BUSCA EL REGISTRO EN LA TABLA PARA OBTENER SUS CAMPOS
								// ARMA EL XML
								// ACTUALIZA EL REGISTRO EN LA TABLA QUE BITACORA AGREGANDO UN NUEVO TAG AL XML
								ResultadoCatalogo resultadoCatalogo = (ResultadoCatalogo)this.actualizaBitacora(sNomTabla, registro, false);

								if (resultadoCatalogo.mensaje.getClave() != null) {
									// SI SE GENERÓ UN ERROR SE OBTIENE EL MENSAJE
									sResultado = (String) resultadoCatalogo.mensaje.getDescripcion();
								}
							}
						}
						else {

							// BUSCA EL REGISTRO EN LA TABLA PARA OBTENER SUS CAMPOS Y FORMAR EL XML
							// ARMA EL XML
							// CREA EL REGISTRO CON LOS NUEVOS MOVIMIENTOS REGISTRADOS EN EL XML
							ResultadoCatalogo resultadoCatalogo = (ResultadoCatalogo)this.actualizaBitacora(sNomTabla, registro, true);

							if (resultadoCatalogo.mensaje.getClave() != null) {
								// SI SE GENERÓ UN ERROR SE OBTIENE EL MENSAJE
								sResultado = (String) resultadoCatalogo.mensaje.getDescripcion();
							}
						}
					}
					else{
						// SI LA LISTA CONTIENE EL CAMPO SIN_TABLA REGRESA EL CONTROL AL SERVLET Y REGRESA UN MENSAJE DE ERROR
						sResultado = " La bitácora NO se generó, la función " + sCveFuncion + " no tiene asignada una tabla a bitacorar. ";
						return sResultado;
 					}
				}
			}
			else {
				// SI LA LISTA ES NULA REGRESA EL CONTROL AL SERVLET
				return sResultado;
			}
		}
		catch (SQLException ex) {
			System.out.println("Se genero una SQLException en BitacoraSLBean: " + ex.getMessage());
			ex.printStackTrace();
			bError = true;
		}
		catch (Exception e) {
			System.out.println("Se generó un Exception en BitacoraSLBean:: " + e.getMessage());
			e.printStackTrace();
			bError = true;
		}
		finally {
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if (conexion.isConectado()) {
				conexion.cierraConexion();
			}
		}
		if (bError){
			throw new LogicaNegocioException();
		}
		return sResultado;
	}

	/**
	 * Realiza la consulta de la historia de los movimientos de un registro.
	 * @param sCveFuncion Es la clave de la función.
	 * @param registro Objeto registro con los datos de los campos llave de cada tabla.
	 * @return Objeto Registro con los datos ordenados listos para presentarse en la jsp.
	 * @throws SQLException Si se genera un error al consultar la bitácora.
	 * @ejb.interface-method
	 */
	public LinkedList consultaBitacora(String sCveFuncion, Registro registro) throws SQLException {

		// SE INICIALIZA LA CLASE QUE REALIZA LA CONEXIÓN A LA BASE DE DATOS
		Conexion2 conexion = new Conexion2();

		LinkedList listaRegistros = null;

		// SE INICIALIZA EL HASHTABLE QUE SE ENVIARÁ A LA JSP CON VALOR INICIAL DE NULL
		Registro regBitacora = null;

		// ALMACENA EL NOMBRE DE LA TABLA QUE SEVA A CONSULTAR
		String sNomTabla = "";

		try {
			//ABRE CONEXION A BASE DE DATOS
			conexion.abreConexion("java:comp/env/jdbc/PortalDs");
			bitacora.setConexion(conexion.getConexion());
			// FUNCIONES A LLAMAR
			// VERIFICA SI LA FUNCION BITACORA, ESTO LO HACE OBTENIENDO UNA LISTA
			// Y SI LA LISTA ES DIFERENTE DE NULO SIGNIFICA QUE SI PUEDE BITACORAR
			// REGRESANDO EL NOMBRE DE LAS TABLAS QUE DEBE BITACORAR
			LinkedList lListaTablas = verificaFuncionBitacora(sCveFuncion);
			// SI LA LISTA DE TABLAS ES NULA, ENTONCES ESTA FUNCION NO BITACORA
			if (lListaTablas != null) {
				Iterator iLista = lListaTablas.iterator();
				// SOLO SE OBTIENEN LOS CAMPOS LLAVE DE LA PRIMERA TABLA
				// SOLO SE ITERARÁ LA PRIMERA TABLA PARA LA CONSULTA DE BITÁCORA
				if (iLista.hasNext()) {
					regBitacora = (Registro) iLista.next();
					if (regBitacora.getDefCampo("SIN_TABLA") == null) {
						sNomTabla = (String) regBitacora.getDefCampo("CVE_TABLA");
						// 1. OBTIENE LOS CAMPOS LLAVE DE LA TABLA QUE SE VA A CONSULTAR Y
						// 2. BUSCA EL REGISTRO EN LA TABLA QUE BITACORA.
						registroBitacora = this.getRegistroBitacora(sNomTabla, registro);

						// SE INICIALIZAN LOS PARAMETROS PARA LOS ATRIBUTOS
						// DE FECHA Y NOMBRE DE LA TABLA QUE SE VA A CONSULTAR
						String sFecha;
						String sNomTablaRegXml;
						if (registroBitacora != null) {
							String sCadena = (String) registroBitacora.getDefCampo("XML_MOV_REG");
							sCadena = this.ValidaTextoAmp(sCadena);
							if (sCadena != null) {
								//isr CONTIENE EL CLOB LISTO PARA LEERSE
								ByteArrayInputStream io = new ByteArrayInputStream(sCadena.getBytes());
								InputStreamReader isr = new InputStreamReader(io, "iso-8859-1");

								//SE PARSEA EL CLOB
								SAXBuilder builder = new SAXBuilder();
								Document docBitacora = builder.build(isr);
								//OBTENEMOS EL NOMBRE DE LA TABLA DEL ATRIBUTO DEL REGISTRO
								sNomTablaRegXml = docBitacora.getRootElement().getAttribute("tabla").getValue();

								// SE OBTIENE LA LISTA DE REGISTROS
								List listaRegistrosXml = docBitacora.getRootElement().getChildren();

								// SE INICIALIZA EL REGISTRO QUE CONTENDRÁ LA LISTA DE NOMBRES DE COLUMNAS
								Registro registroColumnas = new Registro();

								// SE INICIALIZA EL LinkedList QUE CONTENDRÁ LA LISTA DE NOMBRES DE COLUMNAS
								LinkedList lNombresDeColumnas = new LinkedList();
								lNombresDeColumnas.add(0, "<b>ENCABEZADO</b>");
								int x = 1;

                                // CONTADOR DE REGISTROS
								Integer iCuentaRegistros = new Integer(0);

								// SE ITERA POR CADA REGISTRO QUE CONTENGA LA LISTA DE REGISTROS
								for (int j = 0; j < listaRegistrosXml.size(); j++) {
									// SE INCREMENTA EL CONTADOR
									iCuentaRegistros = new Integer(iCuentaRegistros.intValue() + 1);

									// SE RECUPERA EL SIGUIENTE ELEMENTO
									Element eRegistro = (Element) listaRegistrosXml.get(j);

									//SE  INICIALIZA EL REGISTRO QUE SE AÑADIRÁ AL HASHTABLE
									registroBitacora = new Registro();

									// SE AÑADEN LOS ATRIBUTOS DEL REGISTROXML AL REGISTRO
									registroBitacora.addDefCampo("<b>ENCABEZADO</b>",
										"<b>" + (String) ( (Attribute) eRegistro.getAttribute("encabezado")).getValue() + "<br>" + (String) ( (Attribute) eRegistro.getAttribute("fecha")).getValue() + "  " + (String) ( (Attribute) eRegistro.getAttribute("hora")).getValue() + "<br>por: " +
										(String) ( (Attribute) eRegistro.getAttribute("usuario")).getValue() + "</b>");

									// SE OBTIENE LA LISTA DE CAMPOS PARA CADA REGISTRO
									List listaCampos = eRegistro.getChildren();
									String sNombre = "";
									String sValor = "";
									for (int i = 0; i < listaCampos.size(); i++) {
										Element eCampo = (Element) listaCampos.get(i);
										sNombre = (String) ( (Attribute) eCampo.getAttribute("nombre")).getValue();
										sValor = ! ( (String) ( (Attribute) eCampo.getAttribute("valor")).getValue()).equals("null") ? (String) ( (Attribute) eCampo.getAttribute("valor")).getValue() : "";
										registroBitacora.addDefCampo(sNombre, sValor);
										if (registroColumnas.getDefCampo(sNombre) == null) {
											registroColumnas.addDefCampo(sNombre, sNombre);
											lNombresDeColumnas.add(x, sNombre);
											x++;
										}
									}

									// SE ASIGNA EL NUMERO CONSECUTIVO A CADA REGISTRO
									regBitacora.addDefCampo(iCuentaRegistros.toString(), registroBitacora);
								} //FOR: RECORRE REGISTROXML
								regBitacora.addDefCampo("0", lNombresDeColumnas);
								Registro parametros = new Registro();
								parametros.addDefCampo("TOTAL_REGISTROS", String.valueOf(iCuentaRegistros));
								parametros.addDefCampo("CVE_TABLA", sNomTabla);
								listaRegistros = new LinkedList();
								listaRegistros.add(parametros);
								listaRegistros.add(this.registroConsultaBitacora("1", regBitacora, iCuentaRegistros, sNomTabla));
							}
						}
						else {
							listaRegistros = new LinkedList();
							Registro mensaje = new Registro();
							mensaje.addDefCampo("MENSAJE", " -No se han registrado movimientos para este registro  ");
							listaRegistros.add(mensaje);
						}
					}
					else{
						listaRegistros = new LinkedList();
						Registro mensaje = new Registro();
						mensaje.addDefCampo("MENSAJE", " -No se ha definido tabla a bitacorar para la función " + sCveFuncion + ". ");
						listaRegistros.add(mensaje);

					}
				}
			}
			else{
				listaRegistros = new LinkedList();
				Registro mensaje = new Registro();
				mensaje.addDefCampo("MENSAJE", " -La función " + sCveFuncion + " no bitacora. ");
				listaRegistros.add(mensaje);
			}
		}
		catch (JDOMException e) {
			System.out.println("Error Jdom: " + e.getMessage());
			e.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if (conexion.isConectado()) {
				conexion.cierraConexion();
			}
		}
		return listaRegistros;
	}

	/**
	 * Construye la lista de registros que se van a desplegar en la JSP.
	 * @param sPagina Es el número de página que se va a desplegar.
	 * @param registro Objeto registro con los datos de los campos llave de cada tabla.
	 * @param iCuentaRegistros Es el número de registros que contiene el hashtable.
	 * @param sNomTabla Es el nombre de la tabla a consultar.
	 * @return Objeto LinkedList con los registros ordenados listos para presentarse en la jsp.
	 * @throws RemoteException Se generó un error al registrar la bitácora. 
	 */
	public LinkedList registroConsultaBitacora(String sPagina, Registro registro, Integer iCuentaRegistros, String sNomTabla) throws RemoteException{
		LinkedList lListaRegistros = null;
		try{
			LinkedList llistaColumnas = (LinkedList) registro.getDefCampo("0");
			lListaRegistros = new LinkedList();
			Iterator iterador = llistaColumnas.iterator();
			String sColumna = null;
			boolean bRenglonColor = false;
			while (iterador.hasNext()) {
				sColumna = (String) iterador.next();
				registroBitacora = new Registro();
				String sRenglonColor = "";
				if (bRenglonColor) {
					sRenglonColor = "class='RenglonTablaColor'";
					bRenglonColor = false;
				}
				else {
					sRenglonColor = "";
					bRenglonColor = true;
				}
				registroBitacora.addDefCampo("RenglonTablaColor", sRenglonColor);
				registroBitacora.addDefCampo("0", sColumna);

				// SE ITERA POR CADA REGISTRO QUE CONTENGA LA LISTA DE REGISTROS
				for (int j = 1; j <= iCuentaRegistros.intValue(); j++) {
					registroBitacora.addDefCampo(String.valueOf(j), (String) ( (Registro) registro.getDefCampo(String.valueOf(j))).getDefCampo(sColumna));
				}
				registroBitacora.addDefCampo("ETIQUETA", bitacora.getEtiquetaColumna(sNomTabla, sColumna));
				lListaRegistros.add(registroBitacora);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	return lListaRegistros;
}


	/**
	 * Este método verifica si la función puede bitacorar y regresa una
	 * lista con los nombres de las tablas si la función puede hacerlo
	 * en caso contrario regresa nulo.
	 * @param sCveFuncion Es la clave de la función.
	 * @return Objeto LinkedList con los nombres de las tablas.
	 * @throws SQLException Si se genera un error al verificar la función bitácora.
	 */
	private LinkedList verificaFuncionBitacora(String sCveFuncion) throws SQLException {
		LinkedList listaNomTablas = null;
		Registro registrofuncion = null;
		// SE OBTIENE EL REGISTRO DE LA FUNCION DESEADA DE LA TABLA RS_CONF_FUNCION
		Registro parametros = new Registro();
		parametros.addDefCampo("CVE_TABLA", "RS_CONF_FUNCION");
		parametros.addDefCampo("FILTRO", "CVE_FUNCION ='" + sCveFuncion + "'");
		// SE ASIGNA EL REGISTRO OBTENIDO DE LA DAO A UN OBJETO DEL MISMO TIPO
		registrofuncion = bitacora.getRegistro(parametros);

		// SI EL REGISTRO TIENE DATOS
		if (registrofuncion != null) {
			// VERIFICA SI LA FUNCION BITACORA
			if (registrofuncion.getDefCampo("B_BITACORA") != null) {
				if ( ( (String) registrofuncion.getDefCampo("B_BITACORA")).equals("V")){
					//VERIFICA SI BITACORA EN OTRAS TABLAS
					if((String)registrofuncion.getDefCampo("CVE_TABLA")!= null){
						listaNomTablas = new LinkedList();
						listaNomTablas.add(registrofuncion);
					}
					else{
						listaNomTablas = new LinkedList();
						Registro error = new Registro();
						error.addDefCampo("SIN_TABLA", "SI");
						listaNomTablas.add(error);
					}
				}
			}
		}
		return listaNomTablas;
	}

	/**
	 * Este método busca el registro que se va a bitacorar, para lograrlo el
	 * método obtiene los campos llave de la tabla que se va a bitacorar y
	 * después se busca el registro en la tabla que bitacora.
	 * @param sNomTabla Nombre de la tabla que se va a bitacorar
	 * @param registro Conjunto de datos que tienen los valores de los campo llave.
	 * @return Objeto de tipo Registro con el registro buscado y nulo en caso de no ser así.
	 * @throws SQLException Se genero un error al buscar el registro.
	 */
	private Registro getRegistroBitacora(String sNomTabla, Registro registro) throws SQLException {
		String sFiltro = "";
		String sValorColumnaLlave = "";
		String sCampoNulo = "";
		Registro registroLlave = null;
		Registro registroAuxiliar = new Registro();
		LinkedList listaCamposLlave = null;
		boolean bCamposNulos = false;

		// SE OBTIENEN LOS CAMPOS LLAVE DE LA TABLA DESEADA EN UNA LISTA
		listaCamposLlave = bitacora.getCamposLlaveTabla(sNomTabla);

		if(listaCamposLlave != null){
			// ITERA LOS VALORES DE listaCamposLlave PARA PODER FORMAR EL FILTRO
			Iterator ilistaCamposLlave = listaCamposLlave.iterator();
			while (ilistaCamposLlave.hasNext()) {
				// SE ASOCIAL EL SIGUIENTE ELEMENTO DE LA LISTA A UN REGISTRO
				registroAuxiliar = (Registro) ilistaCamposLlave.next();
				sValorColumnaLlave = (String) registroAuxiliar.getDefCampo("COLUMNA");

				// VERIFICA SI SE ENVIARON CAMPOS NULOS O VACÍOS
				// CUANDO SE DA DE ALTA UN REGISTRO QUE CONTIENE EL VALOR DE UN SEQUENCE EL CAMPO ES NULO, SI SE ENVIÓ
				// EL NÚMERO DE SEQUENCE SE REMPLAZA EL CAMPO, SI EL SEQUENCE ES NULO ENTONCES SE ENVÍA UN MENSAJE DE CAMPOS VACÍOS
				bCamposNulos = (((String) registro.getDefCampo(sValorColumnaLlave))== null || ((String) registro.getDefCampo(sValorColumnaLlave)).equals("")) ? ((String) registro.getDefCampo("SEQUENCE")) == null ? true : false : false;
				sCampoNulo = bCamposNulos ? sValorColumnaLlave : "";
                // CONCATENA A sFiltro LA COLUMNA QUE ES PARTE DE LA LLAVE CON EL VALOR DE registro
				sFiltro = sFiltro + (((String) registro.getDefCampo(sValorColumnaLlave))== null || ((String) registro.getDefCampo(sValorColumnaLlave)).equals("") ? (String) registro.getDefCampo("SEQUENCE") : (String) registro.getDefCampo(sValorColumnaLlave));
			}

			// SE OBTIENE EL REGISTRO BUSCADO EN LA TABLA DE BITACORA
			Registro parametrosbusqueda = new Registro();

            // SE AGREGAN LOS PARÁMETROS PARA LA BÚSQUEDA CVE_TABLA Y FILTRO
			parametrosbusqueda.addDefCampo("CVE_TABLA", "RS_BITACORA");

			// SE CONCATENA EL NOMBRE DE LA TABLA Y LOS CAMPOS LLAVE
			sFiltro = "CVE_BITACORA = '" + sNomTabla + sFiltro.toLowerCase() + "'";
			parametrosbusqueda.addDefCampo("FILTRO", sFiltro);
			registroLlave = bitacora.getRegistro(parametrosbusqueda);

			// SI HUBO CAMPOS NULOS O VACÍOS SE ENVÍA UN MENSAJE DE ERROR CON EL CAMPO QUE FALTÓ
			if(bCamposNulos){
				registroLlave = new Registro();
				registroLlave.addDefCampo("CAMPO_VACIO", sCampoNulo);
			}
		}
		return registroLlave;
	}

	/**
	 * Se obtienen los campos llave de la tabla en la que se registraron los
	 * movimientos para poder buscar el registro que fue modificado y obtener
	 * todos sus campos, se forma el xml con los valores de los campos que obtuvo y
	 * actualiza el registro en la tabla que bitacora.
	 * @param sNomTabla Nombre de la tabla que registro movimientos.
	 * @param registro Objeto con los valores del registro afectado en la tabla que se indica como parametro.
	 * @param bCreaBitacora <code>V</code>Indica crear el registro <code>F</code>Indica actualizar el registro
	 * @return <code>True</code> Si la bitácora se actualizó <code>False</code> Si la bitácora no se actualizó.
	 * @throws SQLException Si se genera un error al actualizar la bitácora.
	 */
	private ResultadoCatalogo actualizaBitacora(String sNomTabla, Registro registro, boolean bCreaBitacora) throws SQLException {

		boolean bResultado = false;
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		StringBuffer buf = new StringBuffer();
		String sValor = "";
		try {
			java.util.Date date = new java.util.Date();
			String sXml = "";
			String sMensajeEncabezado = "Modificado el ";
			// SE OBTIENE LOS CAMPOS LLAVE DE LA TABLA Y
			// SE OBTIENE EL REGISTRO DE LA TABLA AFECTADA
			Registro registroTabla = null;

			// SE VERIFICA EL TIPO DE OPERACIÓN PARA DEFINIR EL PROCESO A EJECUTAR
			// Y PARA DEFINIR EL MENSAJE DE ENCABEZADO DE EL REGISTRO
			if(((String)registro.getDefCampo("OPERACION")).equals("2")){
				 registroTabla = this.getRegistroLlaveTabla(sNomTabla, registro);
				 registro.addDefCampo("CVE_BITACORA", (String)registroTabla.getDefCampo("CVE_BITACORA"));
				 registroTabla = registro;
				 sMensajeEncabezado = "Dado de baja el ";
			}
			else{
				registroTabla = this.getRegistroLlaveTabla(sNomTabla, registro);
			}
			if(((String)registro.getDefCampo("OPERACION")).equals("1")){
				sMensajeEncabezado = "Dado de alta el ";
			}


			// SI SE ENCUENTRA EL REGISTRO EN LA TABLA QUE SE REALIZARON LOS CAMPOS ACTUALIZA EL REGISTRO
			if (registroTabla != null) {


				// SE OBTIENE LA ESTRUCTURA DE LA TABLA
				LinkedList listaCamposTabla = bitacora.getCamposTabla(sNomTabla);
				// SE ITERA LA LISTA DE LA ESTRUCTURA DE CAMPOS DE LA TABLA
				String sCampoEstructura = "";

				// PARA CUANDO SE CREA LA BITACORA
				if (bCreaBitacora) {

					// **** meter fecha, hora, usuario que registro
					buf.append("<registro-bitacora tabla=\"" + sNomTabla + "\">");
				}
				else {
					// OBTENER EL CLOB DE LA PROPIEDAD registroBitacora YA CONVERTIDO A STRING
					// QUITARLE AL STRING CONVERIDO LA CADENA "</registro-bitacora>"
					buf.append( ( (String) registroBitacora.getDefCampo("XML_MOV_REG")).substring(0, ( (String) registroBitacora.getDefCampo("XML_MOV_REG")).length() - 20));
				}

				// OBTENER EL ATRIBUTO INDICE
				String sIndice = bitacora.indice();
				registro.addDefCampo("INDICE", sIndice);


				// PARA CUANDO YA EXISTE EL REGISTRO Y SE DESEA ACTUALIZAR
				buf.append("   <registro encabezado=\"" + sMensajeEncabezado + "\" fecha=\"" + Fecha2.formatoSimple(date)+"\" hora=\""+ Fecha2.HoraMinutos(date)+"\" usuario=\""+registro.getDefCampo("CVE_USUARIO_BITACORA")+"\" indice=\""+registro.getDefCampo("INDICE")+"\">");
				Iterator ilistaCamposTabla = listaCamposTabla.iterator();
				Registro registroNomCamposTabla = new Registro();
				while (ilistaCamposTabla.hasNext()) {
					registroNomCamposTabla = (Registro) ilistaCamposTabla.next();
					sCampoEstructura = (String)registroNomCamposTabla.getDefCampo("COLUMNA");
					sValor = this.ValidaTextoAmp(((Object)registroTabla.getDefCampo(sCampoEstructura)) == null ? "" : ((Object)registroTabla.getDefCampo(sCampoEstructura)).toString());
					// SE AGREGAN LOS CAMPOS Y VALORES
					buf.append("      <campo nombre=\"" + sCampoEstructura + "\" valor=\"" + sValor + "\"/>");
				}

				// AGREGARLE A ESTA LA ULTIMA CADENA EL RESULTADO DE LAS LINEAS DE ABAJO, ES DECIR, EL NUEVO REGISTRO
				buf.append("   </registro>");
				buf.append("</registro-bitacora>");
				// SE GENERA EL ARCHIVO XML DEL REGISTRO OBTENIDO
				// SE CREA EL REGISTRO EN LA BITACORA
				Registro registroModificacion = new Registro();
				registroModificacion.addDefCampo("CVE_BITACORA", registroTabla.getDefCampo("CVE_BITACORA"));
				registroModificacion.addDefCampo("XML_MOV_REG", buf.toString());

				// PARA CUANDO SE CREA LA BITACORA
				if (bCreaBitacora) {
					resultadoCatalogo = bitacora.alta(registroModificacion);
				}
				else {
					// *** PARA CUANDO LO MODIFICA ***
					resultadoCatalogo = bitacora.modificacion(registroModificacion);
				}
				bResultado = true;
				// INSERTA EL INDICE DE LA BITACORA
				Registro registroIndice = new Registro();
				registroIndice.addDefCampo("CVE_BITACORA", registroTabla.getDefCampo("CVE_BITACORA"));
				registroIndice.addDefCampo("CVE_USUARIO" , registro.getDefCampo("CVE_USUARIO_BITACORA"));
				registroIndice.addDefCampo("CVE_GPO_EMPRESA" , registro.getDefCampo("CVE_GPO_EMPRESA"));
				registroIndice.addDefCampo("INDICE" , registro.getDefCampo("INDICE"));
				resultadoCatalogo = bitacora.insertaIndice(registroIndice);
			}
			else {
				System.out.println("NO SE ENCONTRO LA LLAVE DEL REGISTRO EN LA TABLA MODIFICADA POR FALTA DE DATOS EN EL OBJETO Registro Y NO SE >> CREA <<  EL REGISTRO EN LA BITACORA PARA LA TABLA " + sNomTabla +", VERIFIQUE SI SE TRATA DE UNA ALTA Y DE UN CAMPO TIPO SEQUENCE ");
				resultadoCatalogo.mensaje.setClave("FALTAN_CAMPOS");
				resultadoCatalogo.mensaje.setTipo("Error");
				resultadoCatalogo.mensaje.setDescripcion("No se encontró la llave del registro en la tabla modificada por falta de datos en el objeto Registro, no se generó la bitácora, verifique si se trata de una alta y de un campo sequence.");
				bResultado = false;
			}
		}
		catch (SQLException ex) {
			System.out.println("Se genero una SQLException en BitacoraSLBean: " + ex.getMessage());
		}
		finally {
		}
		return resultadoCatalogo;
	}

	/**
	 * Se obtienen los campos llave de la tabla en la que se registraron los
	 * movimientos para poder buscar el registro que fue modificado y obtener
	 * todos sus campos.
	 * @param sNomTabla Nombre de la tabla que registro movimientos.
	 * @param registro Registro con los datos que envia el Servlet.
	 * @return Objeto Registro con el registro que fue modificado y nulo si el registro no fue encontrado
	 * @throws SQLException Si se genera un error al obtener los campos.
	 */
	private Registro getRegistroLlaveTabla(String sNomTabla, Registro registro) throws SQLException {
		Registro registroLlave = null;
		Registro registroAuxiliar = new Registro();
		String sValorColumnaLlave = "";
		String sValorRegistro = "";
		String sValorRegistroFiltro = "";
		String sCveBitacora = sNomTabla;
		String sFiltro = "";
		String sValidaFecha = "'";
		boolean bFecha = false;
		//boolean bCampoVacio = false;


		// SE OBTIENE LOS CAMPOS LLAVE DE LA TABLA PARA PODER BUSCAR EL REGISTRO MODIFICADO
		listaCamposLlave = bitacora.getCamposLlaveTabla(sNomTabla);
		Iterator ilistaCamposLlave = (Iterator)this.listaCamposLlave.iterator();
		while (ilistaCamposLlave.hasNext()) {

			if (sFiltro.length() > 1) {
				sFiltro += "\n AND ";
			}
			// SE ASOCIAL EL SIGUIENTE ELEMENTO DE LA LISTA A UN REGISTRO
			registroAuxiliar = (Registro) ilistaCamposLlave.next();
			sValorColumnaLlave = (String) registroAuxiliar.getDefCampo("COLUMNA");

			// VERIFICA SI SE ENVIARON CAMPOS NULOS O VACÍOS
			// CUANDO SE DA DE ALTA UN REGISTRO QUE CONTIENE EL VALOR DE UN SEQUENCE EL CAMPO ES NULO, SI SE ENVIÓ
			// EL NÚMERO DE SEQUENCE SE REMPLAZA EL CAMPO, SI EL SEQUENCE ES NULO ENTONCES SE ENVÍA UN MENSAJE DE CAMPOS VACÍOS
			sValorRegistro = ( (String)(((String) registro.getDefCampo(sValorColumnaLlave)) == null || ((String) registro.getDefCampo(sValorColumnaLlave)).equals("") ? (String)registro.getDefCampo("SEQUENCE") == null ? "" : (String)registro.getDefCampo("SEQUENCE") : registro.getDefCampo(sValorColumnaLlave))).toLowerCase();
			sValorRegistroFiltro = ( (String)(((String) registro.getDefCampo(sValorColumnaLlave)) == null || ((String) registro.getDefCampo(sValorColumnaLlave)).equals("") ? (String)registro.getDefCampo("SEQUENCE") == null ? "" : (String)registro.getDefCampo("SEQUENCE") : registro.getDefCampo(sValorColumnaLlave)));
			if(sValorRegistroFiltro.indexOf("/")== 2){
				sValorRegistroFiltro = " TO_DATE('" + sValorRegistroFiltro + "','DD/MM/YYYY') ";
				bFecha = true;
			}
			// SE OBTIENE LA CLAVE DE LA BITACORA
			sCveBitacora += sValorRegistro;

			if(bFecha){
				sValidaFecha = "";
			}
			// CONCATENA A sFiltro LA COLUMNA QUE ES PARTE DE LA LLAVE CON EL VALOR DE registro
			sFiltro += sValorColumnaLlave + " = " + sValidaFecha + sValorRegistroFiltro + sValidaFecha;
			bFecha = false;
		}
		// SE OBTIENE EL REGISTRO BUSCADO EN LA TABLA EN LA QUE SE GENERARON LOS MOVIMIENTOS
		Registro parametrosbusqueda = new Registro();
		parametrosbusqueda.addDefCampo("CVE_TABLA", sNomTabla);
		parametrosbusqueda.addDefCampo("FILTRO", sFiltro);
		registroLlave = bitacora.getRegistro(parametrosbusqueda);
		if(registroLlave != null){
			registroLlave.addDefCampo("CVE_BITACORA", sCveBitacora);
		}

		// SI SE TRATA DE UNA BAJA SOLOSE ENVIA LA CLAVE DE BITACORA
		if(registroLlave == null && ((String)registro.getDefCampo("OPERACION")).equals("2")){
			registroLlave = new Registro();
			registroLlave.addDefCampo("CVE_BITACORA", sCveBitacora);
		}
		return registroLlave;
	}

	/**
	 * Método que obtiene el texto sustituto para caracters no válidos (ampersan).
	 * @param sCadena Es el string a validar.
	 * @return La cadena con el formato correcto.
	 */
	public String ValidaTextoAmp(String sCadena) {
		String sCadenaTemporal = sCadena;
		String sTemporal = "";
		String sResult = "";
		int iInicio = 0;
		int iFinal = 0;
		try{
			// VALIDA SI LA CADENA ES NULA
			if (sCadena != null){
				int longitudCadena = sCadena.length();
				if (sCadena.indexOf("&") != -1) {
					for (int i = 0; i < sCadena.length(); i++) {
						if (sCadena.charAt(i) == '&') {
							// SE REMPLAZA EL CARACTER & POR EL STRING &amp;
							sTemporal += sCadena.substring(iInicio, i) + "&amp;";
							iInicio = i + 1;
						}
					}
					sCadena = sTemporal + sCadena.substring(iInicio, sCadena.length());
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return sCadena;
	}



}