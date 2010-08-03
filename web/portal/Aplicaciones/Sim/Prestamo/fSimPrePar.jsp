<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoParticipante">
	<Portal:PaginaNombre titulo="Participantes" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoParticipante' parametros='IdPrestamo=${param.IdPrestamo}&CveTipoPersona=${param.CveTipoPersona}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/Personas/fSimCliCon.jsp?CveTipoPersona=${param.CveTipoPersona}' nombreliga='Asignar la persona' nomventana='VentanaAsignarPersona'/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Participante' control='etiqueta-controlreferencia' controlnombre='NomTipoPersona' controlvalor='${requestScope.registro.campos["NOM_TIPO_PERSONA"]}' />
		<Portal:FormaElemento etiqueta='Nombre' control='etiqueta-controlreferencia' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' />
		<input type="hidden" name="IdPersona" value='<c:out value='${requestScope.registro.campos["ID_PERSONA"]}'/>' />
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>	
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
		
	</Portal:Forma>
</Portal:Pagina>	