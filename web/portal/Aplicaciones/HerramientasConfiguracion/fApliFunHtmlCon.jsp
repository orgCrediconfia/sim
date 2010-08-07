<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="HerramientasConfiguracionFuncionHtml">
	<Portal:PaginaNombre titulo="Editor de HTML" subtitulo="Funciones"/>
	
	<Portal:Forma tipo='busqueda' funcion='HerramientasConfiguracionFuncionHtml' operacion='CT' filtro='SinFiltro'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveHtml' controllongitud='30' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomHtml'  controllongitud='40' controllongitudmax='80' editarinicializado='true' obligatorio='false' />
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta c&oacute;digo HTML para funciones" botontipo="url" url='/ProcesaCatalogo?Funcion=HerramientasConfiguracionFuncionHtml&OperacionCatalogo=IN&Filtro=Alta'>
		<Portal:TablaListaTitulos> 
		<Portal:Columna tipovalor='texto' ancho='180' valor='Clave'/>
		<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>	
	</Portal:TablaListaTitulos>
		
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='180' valor=''>					
				<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_HTML"]}' funcion='HerramientasConfiguracionFuncionHtml' operacion='CR' parametros='CveHtml=${registro.campos["CVE_HTML"]}' parametrosregreso='\'${registro.campos["CVE_HTML"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_HTML"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	
</Portal:Pagina>
