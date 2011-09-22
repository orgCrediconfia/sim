<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaCatalogoMovimientoCuenta">
	<Portal:PaginaNombre titulo="Catálogo de Movimiento de la Cuenta" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCajaCatalogoMovimientoCuenta' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave del Movimiento' control='Texto' controlnombre='CveOperacion' controllongitud='10' controllongitudmax='10' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre del Movimiento' control='Texto' controlnombre='DescLarga'  controllongitud='45' controllongitudmax='60' editarinicializado='true'/>
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Catálogo de Movimiento de la Cuenta" botontipo="url" url="/Aplicaciones/Sim/Caja/fSimCajaCatMovtoCueReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Clave del Movimiento'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre del Movimiento'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_OPERACION"]}' funcion='SimCajaCatalogoMovimientoCuenta' operacion='CR' parametros='CveOperacion=${registro.campos["CVE_OPERACION"]}'/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["DESC_LARGA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
