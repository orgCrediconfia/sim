<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimProductoCicloAccesorio">
	<Portal:PaginaNombre titulo="Accesorios" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:TablaForma nombre="Consulta" funcion="SimProductoCicloAccesorio" operacion="AL" parametros='IdProducto=${param.IdProducto}&NumCiclo=${param.NumCiclo}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Accesorio'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Orden'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaAccesorio}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_ACCESORIO"]}'/>
				<input type='hidden' name='IdAccesorio' value='<c:out value='${registro.campos["ID_ACCESORIO"]}'/>'>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_ACCESORIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor=''>		
					<input type='text' name='Orden' size='2' maxlength='3' value='<c:out value='${registro.campos["ORDEN"]}'/>'>
				</Portal:Columna>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Aceptar' />
		</Portal:FormaBotones>		
	</Portal:TablaForma>

</Portal:Pagina>	