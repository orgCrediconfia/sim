<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoGarantia">
	<Portal:PaginaNombre titulo="Garant&iacute;as" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:TablaForma nombre="Garant&iacute;as" funcion="SimPrestamoGarantia" operacion="AL" parametros='IdPrestamo=${param.IdPrestamo}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Garantia'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_GARANTIA"]}' />		
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_GARANTIA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["DESCRIPCION"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<c:if test='${param.Lista == "si"}'>
				<Portal:Boton tipo='submit' etiqueta='Alta' />
			</c:if>
		</Portal:FormaBotones>				
	</Portal:TablaForma>
</Portal:Pagina>	