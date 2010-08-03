<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoCatalogoOperacion">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Operaciones" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimPrestamoCatalogoConcepto' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveOperacion' controllongitud='10' controllongitudmax='10' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='DescCorta' controllongitud='20' controllongitudmax='20' editarinicializado='true'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta de operaciones" botontipo="url" url='/ProcesaCatalogo?Funcion=SimPrestamoCatalogoOperacion&OperacionCatalogo=IN&Filtro=Alta'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Nombre'/>	
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripci&oacute;n'/>	
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_OPERACION"]}' funcion='SimPrestamoCatalogoOperacion' operacion='CR' parametros='CveOperacion=${registro.campos["CVE_OPERACION"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["DESC_CORTA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["DESC_LARGA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>		
	</Portal:TablaLista>	
</Portal:Pagina>
