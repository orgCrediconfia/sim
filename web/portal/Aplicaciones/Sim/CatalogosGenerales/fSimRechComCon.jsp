<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoRechazoComite">
	
	<Portal:PaginaNombre titulo="Cat&aacute;logo de razones de rechazo del comit&eacute;" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoRechazoComite' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdRechazo' controllongitud='22' controllongitudmax='20'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomRechazo' controllongitud='22' controllongitudmax='20'/>
				</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimRechComReg.jsp?OperacionCatalogo=AL">
		
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		
		
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_RECHAZO"]}' funcion='SimCatalogoRechazoComite' operacion='CR'parametros='IdRechazo=${registro.campos["ID_RECHAZO"]}'/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_RECHAZO"]}'/>
		
			</Portal:TablaListaRenglon>
			
		</c:forEach>
	</Portal:TablaLista>
	
	
</Portal:Pagina>
