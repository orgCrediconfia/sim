<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaConsultaPagarCredito">
	<Portal:PaginaNombre titulo="Pago de amortizaci&oacute;n" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCajaConsultaPagarCredito'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='IdPrestamo' controlvalor='${registro.campos["CVE_PRESTAMO"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='IdGrupo' controlvalor='${registro.campos["NOMBRE"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Producto' control='Texto' controlnombre='IdProducto' controlvalor='${registro.campos["ID_PRODUCTO"]} - ${registro.campos["NOM_PRODUCTO"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='N&uacute;mero de Ciclo' control='Texto' controlnombre='NumCiclo' controlvalor='${registro.campos["NUM_CICLO"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Importe' control='Texto' controlnombre='Importe' controlvalor='${param.Importe}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true'/>
		<Portal:Calendario2 etiqueta='Fecha de aplicación' contenedor='frmRegistro' controlnombre='FechaMovimiento' controlvalor='${requestScope.registro.campos["FECHA_SOLICITUD"]}'  esfechasis='true'/>
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Aceptar" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<script>
		function fAceptar(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaPago&OperacionCatalogo=IN&Filtro=Alta&IdCaja="+document.frmRegistro.IdCaja.value+"&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&IdGrupo="+document.frmRegistro.IdGrupo.value+"&IdProducto="+document.frmRegistro.IdProducto.value+"&NumCiclo="+document.frmRegistro.NumCiclo.value;
			document.frmRegistro.submit();
		}	
	</script>
	
</Portal:Pagina>
