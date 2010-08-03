<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimGrupo" precarga="SimGrupoIntegrante">

	<Portal:PaginaNombre titulo="Grupo" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimGrupo' parametros='IdGrupo=${param.IdGrupo}&Asesor=${param.Asesor}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomGrupo' controlvalor='${requestScope.registro.campos["NOM_GRUPO"]}' controllongitud='30' controllongitudmax='30' editarinicializado='true' obligatorio='true'/>
		<Portal:Calendario2 etiqueta='Fecha de formaci&oacute;n' contenedor='frmRegistro' controlnombre='FechaFormacion' controlvalor='${requestScope.registro.campos["FECHA_FORMACION"]}'  esfechasis='true'/>
		<Portal:FormaElemento etiqueta='Sucursal' control='selector' controlnombre='IdSucursal' controlvalor='${requestScope.registro.campos["ID_SUCURSAL"]}' editarinicializado='false' obligatorio='true' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursal}'/>
		
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
			    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/Grupo/fSimGpoCooCon.jsp?IdGrupo=${param.IdGrupo}' nombreliga='Asignar coordinador' nomventana='VentanaCoordinador'/>
			</Portal:FormaElemento>
			<Portal:FormaElemento etiqueta='Coordinador' control='etiqueta-controlreferencia' controlnombre='NomCoordinador' controlvalor='${requestScope.registro.campos["NOMBRE_COORDINADOR"]}' />
			<input type="hidden" name="IdCoordinador" value='<c:out value='${requestScope.registro.campos["ID_COORDINADOR"]}'/>' />
		</c:if>
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
	
	<Portal:TablaForma maestrodetallefuncion="SimGrupoIntegrante" nombre="Integrantes del grupo" funcion="SimGrupoIntegrante" operacion="BA" parametros='IdGrupo=${registro.campos["ID_GRUPO"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='350' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Tipo de Negocio'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de alta'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Asesor'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaIntegrante}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_PERSONA"]}' />
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_PERSONA"]}'/>			
				<Portal:Columna tipovalor='texto' ancho='350' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_TIPO_NEGOCIO"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_ALTA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_ASESOR_CREDITO"]}'/>			
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='catalogo' etiqueta='Asigna Integrantes al Grupo' funcion='SimGrupoIntegrante' operacion='IN' parametros='&IdGrupo=${requestScope.registro.campos["ID_GRUPO"]}&IdSucursal=${requestScope.registro.campos["ID_SUCURSAL"]}&Asesor=${param.Asesor}' />
			<Portal:Boton tipo='submit' etiqueta='Eliminar' />
		</Portal:FormaBotones>					
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimGrupoIntegrante" nombre="Integrantes dados de baja" funcion="SimGrupoIntegrante" operacion="BA" >
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='350' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de alta'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Fecha de baja'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaIntegranteBaja}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_PERSONA"]}'/>			
				<Portal:Columna tipovalor='texto' ancho='350' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_ALTA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["FECHA_BAJA_LOGICA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
		</Portal:FormaBotones>
	</Portal:TablaForma>
	
</Portal:Pagina>	