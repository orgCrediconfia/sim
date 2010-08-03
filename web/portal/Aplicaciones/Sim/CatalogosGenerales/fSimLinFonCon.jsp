<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoLineaFondeo">
	<Portal:PaginaNombre titulo="Catálogo de Líneas de Fondeo" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoLineaFondeo' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='# Línea' control='Texto' controlnombre='NumLinea' controllongitud='20' controllongitudmax='20' editarinicializado='true'/>
		<Portal:Calendario2 etiqueta='Fecha de inicio' contenedor='frmRegistro' controlnombre='FechaInicio' esfechasis='false'/>
		<Portal:Calendario2 etiqueta='Fecha vigencia' contenedor='frmRegistro' controlnombre='FechaVigencia' esfechasis='false'/>
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Consulta de Líneas de Fondeo" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimLinFonReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='80' valor='# Línea'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Monto'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Tasa'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de inicio'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Fecha vigencia'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NUM_LINEA"]}' funcion='SimCatalogoLineaFondeo' operacion='CR' parametros='IdLinea=${registro.campos["ID_LINEA"]}'/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["MONTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["TASA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_INICIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_VIGENCIA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
