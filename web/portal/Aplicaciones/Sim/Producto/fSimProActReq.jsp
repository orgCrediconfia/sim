<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimProductoActividadRequisito" precarga="SimCatalogoActividadRequisito">
	<Portal:PaginaNombre titulo="Actividades o requisitos" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimProductoActividadRequisito' parametros='IdProducto=${param.IdProducto}'>
		<table border='0' cellspacing='2' cellpadding='3'>
		<Portal:FormaElemento etiqueta='Orden de la etapa' control='Texto' controlnombre='OrdenEtapa' controlvalor='${requestScope.registro.campos["ORDEN_ETAPA"]}' controllongitud='2' controllongitudmax='2' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Etapa de verificaci&oacute;n' control='selector' controlnombre='IdEstatusPrestamo' controlvalor='${requestScope.registro.campos["ID_ETAPA_PRESTAMO"]}' editarinicializado='false' obligatorio='false' campoclave="ID_ETAPA_PRESTAMO" campodescripcion="NOM_ESTATUS_PRESTAMO" datosselector='${requestScope.ListaEstatusPrestamo}'/>	
	</Portal:Forma>	
	
	<Portal:TablaForma nombre="Actividades y requisitos" funcion="SimProductoActividadRequisito" operacion="AL" parametros='IdProducto=${param.IdProducto}'>
		
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
			<input type='hidden' name='Orden' value=''>
			<input type='hidden' name='Etapa' value=''>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaActividadRequisito}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_ACTIVIDAD_REQUISITO"]}' />		
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_ACTIVIDAD_REQUISITO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_ACTIVIDAD_REQUISITO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>	
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>	
	</Portal:TablaForma>
	
	
	<script>

	document.frmRegistro.OrdenEtapa.onblur = fOrdenEtapa;
		function fOrdenEtapa(){
			sOrden = document.frmRegistro.OrdenEtapa.value;
			document.frmTablaForma.Orden.value = sOrden;
		}
		
	document.frmRegistro.IdEstatusPrestamo.onblur = fIdEstatusPrestamo;
		function fIdEstatusPrestamo(){
			sEtapa = document.frmRegistro.IdEstatusPrestamo.value;
			document.frmTablaForma.Etapa.value = sEtapa;
		}
	
	</script>
	
</Portal:Pagina>

	