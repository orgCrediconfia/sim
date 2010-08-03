<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoCambioAsesor">
	<Portal:PaginaNombre titulo="Préstamos del asesor" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='' funcion='' operacion='' filtro=''>
		
	</Portal:Forma>
	<Portal:TablaForma nombre="Consulta" funcion="SimPrestamoCambioAsesor" operacion="MO">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='80' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Asesor'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPrestamo}">		
			<Portal:TablaListaRenglon>
				<input type='hidden' name='IdPrestamo' value='<c:out value='${registro.campos["ID_PRESTAMO"]}'/>'>
				<input type='hidden' name='Aplica' value='<c:out value='${registro.campos["APLICA_A"]}'/>'>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["CVE_PRESTAMO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOMBRE"]}'/>
				<Portal:FormaElemento etiqueta='' control='selector-horizontal' controlnombre='CveAsesorCredito' controlvalor='${registro.campos["CVE_ASESOR_CREDITO"]}' editarinicializado='true' obligatorio='false' campoclave="CVE_USUARIO" campodescripcion="NOM_COMPLETO" datosselector='${requestScope.ListaAsesor}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Aceptar' />
		</Portal:FormaBotones>		
	</Portal:TablaForma>
	
</Portal:Pagina>	
