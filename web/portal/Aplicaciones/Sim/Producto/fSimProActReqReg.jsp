<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimProductoActividadRequisito">
	<Portal:PaginaNombre titulo="Actividades o requisitos" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimProductoActividadRequisito' parametros='IdProducto=${param.IdProducto}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<c:if test='${(requestScope.registro == null)}'>
			<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
			    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/Producto/fSimProActReqCon.jsp?IdProducto=${param.IdProducto}&AplicaA=${param.AplicaA}' nombreliga='Asignar actividad o requisito' nomventana='VentanaActividadRequisito'/>
			</Portal:FormaElemento>
		</c:if>
		<Portal:FormaElemento etiqueta='Actividad o requisito' control='etiqueta-controlreferencia' controlnombre='NomActividadRequisito' controlvalor='${requestScope.registro.campos["NOM_ACTIVIDAD_REQUISITO"]}' />
		<input type="hidden" name="IdActividadRequisito" value='<c:out value='${requestScope.registro.campos["ID_ACTIVIDAD_REQUISITO"]}'/>' />
		<Portal:FormaElemento etiqueta='Etapa de verificaci&oacute;n' control='selector' controlnombre='IdEstatusPrestamo' controlvalor='${requestScope.registro.campos["ID_ETAPA_PRESTAMO"]}' editarinicializado='false' obligatorio='false' campoclave="ID_ETAPA_PRESTAMO" campodescripcion="NOM_ESTATUS_PRESTAMO" datosselector='${requestScope.ListaEstatusPrestamo}'/>	
	
		<Portal:FormaBotones>
			<c:if test='${(requestScope.registro != null)}'>
				<input type="button" value="Regresar" onclick="javascript:history.go(-1);">
			</c:if>
			<c:if test='${(requestScope.registro == null)}'>
				<Portal:FormaBotonAltaModificacion/>
			</c:if>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
		
	</Portal:Forma>
</Portal:Pagina>	