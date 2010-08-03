<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<Portal:Pagina funcion="SimComitePrestamo">

	<Portal:PaginaNombre titulo="Prestamos" subtitulo="Consulta de datos"/>
	
	<Portal:TablaForma nombre="Prestamos" funcion="SimComitePrestamo" operacion="AL" parametros='IdComite=${param.IdComite}&IdFolioActa=${param.IdFolioActa}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Clave del Préstamo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Grupo o Cliente'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPrestamos}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='Claves${registro.campos["ID_PRESTAMO"]}-${registro.campos["CVE_PRESTAMO"]}' />		
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["CVE_PRESTAMO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOMBRE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
</Portal:Pagina>	