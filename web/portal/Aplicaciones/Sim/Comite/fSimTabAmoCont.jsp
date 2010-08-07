<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimConsultaTablaAmortizacion">
	<Portal:PaginaNombre titulo="Aval o Fiador" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimConsultaTablaAmortizacion' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='IdPrestamo' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="consulta" nombre="Tabla de amortizaci&oacute;n">
		<Portal:TablaListaTitulos> 
		    <Portal:Columna tipovalor='texto' ancho='100' valor='Plazo'/>
		    <Portal:Columna tipovalor='texto' ancho='100' valor='Fecha'/>	
		    <Portal:Columna tipovalor='texto' ancho='100' valor='Saldo inicial'/>
		    <Portal:Columna tipovalor='texto' ancho='100' valor='Tasa'/>
		    <Portal:Columna tipovalor='texto' ancho='100' valor='Interés'/>	
		    <Portal:Columna tipovalor='texto' ancho='100' valor='Amortización de capital'/>		
		    <Portal:Columna tipovalor='texto' ancho='100' valor='Pago'/>	
		    <c:forEach var="registro" items="${requestScope.ListaTituloAccesorio}">
		    	<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_PERSONA"]}'/>
		    <c:forEach>
		    <Portal:Columna tipovalor='texto' ancho='100%' valor='Saldo final'/>
	</Portal:TablaLista>	
</Portal:Pagina>
