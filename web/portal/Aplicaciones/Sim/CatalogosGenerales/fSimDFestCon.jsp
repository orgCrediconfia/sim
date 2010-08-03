<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoDiaFestivo">
	<Portal:PaginaNombre titulo="Días Festivos" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoDiaFestivo' operacion='CT' filtro='Todos'>
		<Portal:Calendario2 etiqueta='Día Festivo' contenedor='frmRegistro' controlnombre='FDiaFestivo' esfechasis='false'/>
		<Portal:FormaElemento etiqueta='Descripción' control='Texto' controlnombre='DescDiaFestivo'  controllongitud='45' controllongitudmax='100' editarinicializado='true'/>
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Consulta de Días Festivos" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimDFestReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Día Festivo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripción'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["F_DIA_FESTIVO"]}' funcion='SimCatalogoDiaFestivo' operacion='CR' parametros='FDiaFestivo=${registro.campos["F_DIA_FESTIVO"]}'/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["DESC_DIA_FESTIVO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
