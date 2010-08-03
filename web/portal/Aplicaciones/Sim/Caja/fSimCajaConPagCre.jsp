<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaConsultaPagarCredito">
	<Portal:PaginaNombre titulo="Pago de amortizaci&oacute;n" subtitulo=""/>
	
	<Portal:Forma tipo='busqueda' funcion='SimCajaConsultaPagarCredito' operacion='CT' filtro='Todos' parametros='IdCaja=${param.IdCaja}&AplicaA=${param.AplicaA}'>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${param.CvePrestamo}' controllongitud='19' controllongitudmax='18'/>
		<Portal:FormaElemento etiqueta='Cliente o grupo' control='Texto' controlnombre='Nombre' controlvalor='${param.Nombre}' controllongitud='30' controllongitudmax='100'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="consulta" nombre="Créditos">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave del pr&eacute;stamo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>	
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PRESTAMO"]}' funcion='SimCajaConsultaPagarCredito' operacion='CR' parametros='IdCaja=${param.IdCaja}&AplicaA=${registro.campos["APLICA_A"]}&IdPrestamo=${registro.campos["ID_PRESTAMO"]}&IdTransaccion=null&TxRespuesta=0&TxPregunta=0&FechaMovimiento=null&Respuesta=null'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOMBRE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>		
	</Portal:TablaLista>	
		
</Portal:Pagina>
