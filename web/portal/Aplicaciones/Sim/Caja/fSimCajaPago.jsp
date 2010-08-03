<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaPago">
	<Portal:PaginaNombre titulo="Pago de amortizaci&oacute;n" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCajaPago'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='IdPrestamo' controlvalor='${param.IdPrestamo}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Clave del grupo' control='Texto' controlnombre='IdGrupo' controlvalor='${param.IdGrupo}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Clave del producto' control='Texto' controlnombre='IdProducto' controlvalor='${param.IdProducto}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='N&uacute;mero de Ciclo' control='Texto' controlnombre='NumCiclo' controlvalor='${param.NumCiclo}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true'/>
		<Portal:Calendario2 etiqueta='Fecha de aplicación' contenedor='frmRegistro' controlnombre='FechaMovimiento' controlvalor='${requestScope.registro.campos["FECHA_SOLICITUD"]}'  esfechasis='true'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Aceptar" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<script>
		function fAceptar(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaPago&OperacionCatalogo=IN&Filtro=Alta&IdCaja="+document.frmRegistro.IdCaja.value+"&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&IdGrupo="+document.frmRegistro.IdGrupo.value+"&IdProducto="+document.frmRegistro.IdProducto.value+"&NumCiclo="+document.frmRegistro.NumCiclo.value;
			document.frmRegistro.submit();
		}	
		
		document.frmRegistro.IdPrestamo.onblur = fDeshabilitaGpo;
		
		function fDeshabilitaGpo(){
			document.frmRegistro.IdGrupo.disabled = true;
			document.frmRegistro.IdGrupo.style.backgroundColor = '#CCCCCC';
			
			document.frmRegistro.IdProducto.disabled = true;;
			document.frmRegistro.IdProducto.style.backgroundColor = '#CCCCCC';
			
			document.frmRegistro.NumCiclo.disabled = true;;
			document.frmRegistro.NumCiclo.style.backgroundColor = '#CCCCCC';
		}
	</script>
	
</Portal:Pagina>
