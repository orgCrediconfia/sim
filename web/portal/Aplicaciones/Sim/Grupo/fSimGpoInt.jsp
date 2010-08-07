<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<Portal:Pagina funcion="SimGrupoIntegrante">

	<Portal:PaginaNombre titulo="Integrantes" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimGrupoIntegrante' operacion='CT' filtro='Todos' parametros='IdGrupo=${param.IdGrupo}&Asesor=${param.Asesor}&IdSucursal=${param.IdSucursal}'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdPersona' controllongitud='20' controllongitudmax='20'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomCompleto' controllongitud='35' controllongitudmax='35'/>
	</Portal:Forma>
	
	<Portal:TablaForma nombre="Consulta" funcion="SimGrupoIntegrante" operacion="AL" parametros='IdGrupo=${param.IdGrupo}&Asesor=${param.Asesor}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='350' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Tipo de Negocio'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Asesor'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<input type='hidden' name='IdCliente' value='<c:out value='${registro.campos["ID_PERSONA"]}'/>'>
				<input type='hidden' name='CveAsesor' value='<c:out value='${registro.campos["CVE_ASESOR_CREDITO"]}'/>'>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_PERSONA"]}-${registro.campos["CVE_ASESOR_CREDITO"]}' />		
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_PERSONA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='350' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_TIPO_NEGOCIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_ASESOR_CREDITO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
</Portal:Pagina>	