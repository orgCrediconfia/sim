<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimConsultaTablaAmortizacion">
	<Portal:PaginaNombre titulo="Tabla de Amortizaci&oacute;n" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimConsultaTablaAmortizacion' operacion='CT' filtro='Todos' parametros='Consulta=Prestamos'>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${param.CvePrestamo}' controllongitud='19' controllongitudmax='18'/>
		<Portal:FormaElemento etiqueta='Cliente o grupo' control='Texto' controlnombre='Nombre' controlvalor='${param.NomCompleto}' controllongitud='30' controllongitudmax='100'/>
	</Portal:Forma>
	<Portal:TablaForma nombre="Consulta" funcion="SimConsultaTablaAmortizacion" operacion="AL">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='150' valor='Clave del préstamo'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre del grupo o cliente'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Producto'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Ciclo'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["CVE_PRESTAMO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOMBRE"]}' funcion='SimGenerarTablaAmortizacion' operacion='AL' parametros='CvePrestamo=${registro.campos["CVE_PRESTAMO"]}&Consulta=Tabla'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["ID_PRODUCTO"]} - ${registro.campos["NOM_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NUM_CICLO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>		
	</Portal:TablaForma>	
</Portal:Pagina>
