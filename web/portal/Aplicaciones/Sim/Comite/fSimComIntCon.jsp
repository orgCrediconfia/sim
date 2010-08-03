<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimComiteIntegrante">
	<Portal:PaginaNombre titulo="Integrantes" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimComiteIntegrante' operacion='CT' filtro='Todos' parametros='IdComite=${param.IdComite}'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='ClaveUsuario' controllongitud='20' controllongitudmax='20'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomCompleto' controllongitud='35' controllongitudmax='35'/>
	</Portal:Forma>
	
	<Portal:TablaForma nombre="Consulta de Integrantes" funcion="SimComiteIntegrante" operacion="AL" parametros='IdComite=${param.IdComite}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaIntegrante}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='Alta${registro.campos["CVE_USUARIO"]}' />		
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_USUARIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_COMPLETO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
	
	
</Portal:Pagina>
