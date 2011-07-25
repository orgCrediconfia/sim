<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCajaIngresoCajaDesembolsar">
	<Portal:PaginaNombre titulo="Ingreso en caja para desembolsar" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCajaIngresoCajaDesembolsar' parametros='IdCaja=${param.IdCaja}&IdPrestamoGrupo=${param.IdPrestamoGrupo}&IdTransaccionGrupo=${param.IdTransaccionGrupo}&IdGrupo=${param.IdGrupo}&IdProducto=${param.IdProducto}&NumCiclo=${param.NumCiclo}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Integrante del grupo que entrega' control='selector' controlnombre='IdPersona' controlvalor='' editarinicializado='true' obligatorio='true' campoclave="ID_PERSONA" campodescripcion="NOM_COMPLETO" datosselector='${requestScope.ListaIntegrantes}'/>
		<Portal:FormaElemento etiqueta='Monto' control='Texto' controlnombre='Monto' controlvalor='' controllongitud='6' controllongitudmax='10'/>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>