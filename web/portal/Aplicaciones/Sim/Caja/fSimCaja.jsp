<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCaja">
	<Portal:PaginaNombre titulo="Caja" subtitulo=""/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCaja'>
		<Portal:FormaSeparador nombre="Elección de una caja"/>
		<Portal:FormaElemento etiqueta='Caja' control='selector' controlnombre='IdCaja' controlvalor='${requestScope.registro.campos["ID_CAJA"]}' editarinicializado='true' obligatorio='true' campoclave="ID_CAJA" campodescripcion="NOM_CAJA" datosselector='${requestScope.ListaCajas}'/>
		<Portal:FormaBotones>
			<input type="button" name="btnOperaciones" value="Aceptar" onClick="MM_goToURL('parent','/portal/ProcesaCatalogo?Funcion=SimCajaMovimiento&OperacionCatalogo=IN&Filtro=Alta&IdCaja='+document.frmRegistro.IdCaja.value)" >
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>
