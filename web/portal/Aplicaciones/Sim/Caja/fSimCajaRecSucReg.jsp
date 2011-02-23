<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCajaRecepcionSucursal">
	<Portal:PaginaNombre titulo="Recepción de otra sucursal" subtitulo=""/>
	<Portal:Forma tipo='catalogo' funcion='SimCajaRecepcionSucursal' parametros='IdCaja=${param.IdCaja}'>
		<Portal:FormaSeparador nombre="Recepción de otra sucursal"/>
		<Portal:FormaElemento etiqueta='Monto' control='Texto' controlnombre='Cantidad' controlvalor='' controllongitud='24' controllongitudmax='24' editarinicializado='true' obligatorio='true' validadato='numerico' />
		<Portal:FormaElemento etiqueta='Usuario que entrega' control='Texto' controlnombre='UsuarioEntrega' controlvalor='' controllongitud='80' controllongitudmax='150' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Sucursal' control='selector' controlnombre='IdSucursalDestino' controlvalor='${requestScope.registro.campos["ID_SUCURSAL"]}' editarinicializado='true' obligatorio='true' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursal}'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Aceptar" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	<script>
		function fAceptar(){
			MM_openBrWindow('/portal/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR&Modulo=Caja&IdCaja='+document.frmRegistro.IdCaja.value+'&Ventana=Si','VentanaCp','scrollbars=yes,resizable=yes,width=500,height=300');
		}
	</script>
</Portal:Pagina>