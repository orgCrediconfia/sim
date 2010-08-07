<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoAccesorioOrdenCliente">
	<Portal:PaginaNombre titulo="Orden de Accesorios del Cliente" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:TablaForma nombre="Consulta" funcion="SimPrestamoAccesorioOrdenCliente" operacion="AL" parametros='IdPrestamo=${param.IdPrestamo}&IdCliente=${param.IdCliente}'>
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
				<Portal:FormaElemento etiqueta='' control='Texto-horizontal' controlnombre='Orden' controlvalor='${registro.campos["ORDEN"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Aceptar' />
		</Portal:FormaBotones>		
	</Portal:TablaForma>

</Portal:Pagina>	