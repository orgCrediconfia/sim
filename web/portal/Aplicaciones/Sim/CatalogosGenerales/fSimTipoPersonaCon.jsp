<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoTipoPersona">
	<Portal:PaginaNombre titulo="Catálogo Tipo de Persona" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoTipoPersona' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Id Tipo Persona' control='Texto' controlnombre='IdTipoPersona' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomTipoPersona'  controllongitud='20' controllongitudmax='30' editarinicializado='true'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Id Tipo Persona'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_TIPO_PERSONA"]}' funcion='SimCatalogoTipoPersona' operacion='CR' parametros='IdTipoPersona=${registro.campos["CVE_TIPO_PERSONA"]}'/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_TIPO_PERSONA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
