<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionPerfilConfiguracion">
	<Portal:PaginaNombre titulo="Funciones disponibles para el perfil" subtitulo="Asignaci&oacute;n de funciones para una perfil"/>
	
	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionPerfilConfiguracion' operacion='CT' filtro='FuncionesDisponibles'>
		<Portal:FormaSeparador nombre="Datos del perfil"/>	
		<Portal:FormaElemento etiqueta='Clave aplicaci&oacute;n' control='etiqueta' controlvalor='${param.CveAplicacion}' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Clave perfil' control='etiqueta' controlvalor='${param.CvePerfil}' controllongitud='50' controllongitudmax='80' editarinicializado='true' obligatorio='false' />
		<Portal:FormaBotones>
		</Portal:FormaBotones>		
	</Portal:Forma>

	<Portal:TablaForma nombre="Funciones disponibles para asignarlas al perfil" funcion="HerramientasConfiguracionPerfilConfiguracion" operacion="AL" parametros='&CveAplicacion=${param.CveAplicacion}&CvePerfil=${param.CvePerfil}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='50' valor='Seleccione'/>		
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='50' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["CVE_FUNCION"]}'>
					<input type="hidden" name="CveFuncion" value='<c:out value='${registro.campos["CVE_FUNCION"]}'/>'>		
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_FUNCION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_FUNCION"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' nombre='btnAlta' etiqueta='Alta de funciones' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>

</Portal:Pagina>