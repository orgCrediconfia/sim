<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCajaSalidaCajaDesembolso">
	<Portal:PaginaNombre titulo="Salida de caja por desembolso" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCajaSalidaCajaDesembolso' parametros='IdCaja=${param.IdCaja}&IdPrestamoGrupo=${param.IdPrestamoGrupo}&IdTransaccionGrupo=${param.IdTransaccionGrupo}&IdGrupo=${param.IdGrupo}&IdProducto=${param.IdProducto}&NumCiclo=${param.NumCiclo}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Integrante del grupo que recibe' control='selector' controlnombre='IdPersona' controlvalor='' editarinicializado='true' obligatorio='true' campoclave="ID_PERSONA" campodescripcion="NOM_COMPLETO" datosselector='${requestScope.ListaIntegrantes}'/>
		<Portal:FormaElemento etiqueta='Monto' control='etiqueta-controlreferencia' controlnombre='Monto' controlvalor='$ ${requestScope.registro.campos["MONTO"]}' editarinicializado='false'/>
		<input type="hidden" name="Importe" value='<c:out value='${requestScope.registro.campos["IMPORTE"]}'/>' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>