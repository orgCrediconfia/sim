<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoConsultaMontoAutorizado">
	<Portal:PaginaNombre titulo="Montos Autorizados" subtitulo="Consulta de datos"/>

	<Portal:TablaLista tipo="consulta" nombre="Montos autorizados">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre integrante'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Monto autorizado'/>	
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaMontoAutorizado}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO_AUTORIZADO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<c:forEach var="registro" items="${requestScope.ListaMontoAutorizadoTotal}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='250' valor='Total'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["TOTAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>	
