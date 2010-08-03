<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoCargoComisionSeguroVida">
	<Portal:PaginaNombre titulo="Seguro de Vida" subtitulo="Consulta de datos"/>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/ProcesaCatalogo?Funcion=SimSeguroVidaBeneficiario&OperacionCatalogo=IN&Filtro=Alta&IdPrestamo=${param.IdPrestamo}&IdCargoComision=${param.IdCargoComision}&IdProducto=${param.IdProducto}">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Beneficiario'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Parentesco'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Porcentaje'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBeneficiario}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_BENEFICIARIO"]}' funcion='SimSeguroVidaBeneficiario' operacion='CR' parametros='IdBeneficiario=${registro.campos["ID_BENEFICIARIO"]}&IdPrestamo=${param.IdPrestamo}&IdCargoComision=${param.IdCargoComision}&IdProducto=${param.IdProducto}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_COMPLETO"]}'/>			
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_PARENTESCO"]}'/>			
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["PORCENTAJE"]}'/>	
			</Portal:TablaListaRenglon>	
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>	
