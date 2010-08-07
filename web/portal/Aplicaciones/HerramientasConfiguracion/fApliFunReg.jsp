<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionFuncion" precarga="HerramientasConfiguracionFuncionOperacion">
	<Portal:PaginaNombre titulo="Funciones" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionFuncion'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveFuncion' controlvalor='${requestScope.registro.campos["CVE_FUNCION"]}' controllongitud='50' controllongitudmax='80' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomFuncion' controlvalor='${requestScope.registro.campos["NOM_FUNCION"]}' controllongitud='50' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='TextoArea' controlnombre='TxDescFuncion' controlvalor='${requestScope.registro.campos["TX_DESC_FUNCION"]}' controllongitud='50' controllongitudmax='3' editarinicializado='true' obligatorio='true' controltexarealongmax='1000'/>
		<Portal:FormaElemento etiqueta='Url' control='Texto' controlnombre='Url' controlvalor='${requestScope.registro.campos["URL_FUNCION"]}' controllongitud='50' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Clase CON' control='Texto' controlnombre='NomClaseCon' controlvalor='${requestScope.registro.campos["NOM_CLASE_CON"]}' controllongitud='80' controllongitudmax='80' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Clase DAO' control='Texto' controlnombre='NomClaseDao' controlvalor='${requestScope.registro.campos["NOM_CLASE_DAO"]}' controllongitud='80' controllongitudmax='80' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Clase Reporte' control='Texto' controlnombre='NomClaseReporte' controlvalor='${requestScope.registro.campos["NOM_CLASE_REPORTE"]}' controllongitud='80' controllongitudmax='80' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Bloqueado' control='SelectorLogico' controlnombre='Bloqueado' controlvalor='${requestScope.registro.campos["B_BLOQUEADO"]}' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='WebService' control='SelectorLogico' controlnombre='Webservice' controlvalor='${requestScope.registro.campos["B_WEBSERVICE"]}' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Rastrea C&oacute;digo' control='SelectorLogico' controlnombre='bRastreaCodigo' controlvalor='${requestScope.registro.campos["B_RASTREA_CODIGO"]}' editarinicializado='true' obligatorio='false' />		
		<Portal:FormaElemento etiqueta='Bitacora' control='SelectorLogico' controlnombre='bBitacora' controlvalor='${requestScope.registro.campos["B_BITACORA"]}' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Paginaci&oacute;n' control='SelectorLogico' controlnombre='bPaginacion' controlvalor='${requestScope.registro.campos["B_PAGINACION"]}' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='M&eacute;todo Aplicaci&oacute;n Java' control='Texto' controlnombre='NomMetodoMenu' controlvalor='${requestScope.registro.campos["NOM_METODO_MENU"]}' controllongitud='80' controllongitudmax='150' editarinicializado='true' obligatorio='false' />
		
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
			<Portal:Url tipo='catalogo-ventana' url='/ProcesaCatalogo?Funcion=HerramientasConfiguracionTabla&OperacionCatalogo=CT&Filtro=Todos&CveGpoEmpresa=${param.CveGpoEmpresa}' nombreliga='Asginaci&oacute;n de la tabla a bitacorar' nomventana='VentanaCp'/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controlreferencia' controlnombre='CveTabla' controlvalor='${requestScope.registro.campos["CVE_TABLA"]}' />
		<Portal:FormaElemento etiqueta='Nombre' control='etiqueta-controlreferencia' controlnombre='NomTabla' controlvalor='${requestScope.registroTabla.campos["NOM_TABLA"]}' />
		
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
		
				
		<input type="hidden" name="CveTabla" value='<c:out value='${requestScope.registro.campos["CVE_TABLA"]}'/>' />
		<input type="hidden" name="NomTabla" value='<c:out value='${requestScope.registroTabla.campos["NOM_TABLA"]}'/>' />
	</Portal:Forma>
	
	
	<Portal:TablaForma maestrodetallefuncion="HerramientasConfiguracionFuncionOperacion" nombre="Operaciones por funci&oacute;n" funcion="HerramientasConfiguracionFuncionOperacion" operacion="BA" parametros='CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}&CveFuncion=${requestScope.registro.campos["CVE_FUNCION"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='Clave Operaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='Depurar C&oacute;digo'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.listaOperacion}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='BajaOperacion${registro.campos["CVE_OPERACION"]}'>
				<input type="hidden" name="CveOperacion" value='<c:out value='${registro.campos["CVE_OPERACION"]}'/>'>		
				</Portal:Columna>
				 
				<Portal:Columna tipovalor='texto' ancho='400' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_OPERACION"]}' funcion='HerramientasConfiguracionFuncionOperacion' operacion='CR' parametros='CveOperacion=${registro.campos["CVE_OPERACION"]}&CveFuncion=${requestScope.registro.campos["CVE_FUNCION"]}' clonarregistro='false' />
				</Portal:Columna>
			
				<Portal:Columna tipovalor='texto' ancho='50%' valor='${registro.campos["B_DEPURAR_CODIGO"]}'/>			
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
			<Portal:Boton tipo='catalogo' etiqueta='Agregar opciones' funcion='HerramientasConfiguracionFuncionOperacion' operacion='IN' filtro='OpcionesDisponibles' parametros='&CveGpoEmpresa=${param.CveGpoEmpresa}&CveFuncion=${requestScope.registro.campos["CVE_FUNCION"]}'/>
		</Portal:FormaBotones>					
	</Portal:TablaForma>
	
</Portal:Pagina>