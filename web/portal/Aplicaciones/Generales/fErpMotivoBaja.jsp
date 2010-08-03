<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina>
	<Portal:PaginaNombre titulo="Motivo de baja" subtitulo="Inactivar"/>
	
	<Portal:Forma tipo='catalogo' funcion='ErpClientesCliente'>
		<Portal:FormaSeparador nombre="Datos generales"/>			

		<Portal:Calendario etiqueta='Fecha de baja' contenedor='frmRegistro' controlnombre='FBaja' controlvalor='${requestScope.registro.campos["F_BAJA"]}' esfechasis='true'/>
		<Portal:FormaElemento etiqueta='Motivo baja' control='Texto' controlnombre='TxMotivoBaja' controlvalor='${requestScope.registro.campos["TX_MOTIVO_BAJA"]}' controllongitud='50' controllongitudmax='250' editarinicializado='true' obligatorio='false' />
	
		<Portal:FormaBotones>
			<input type="button" name="Inactivar" value="Aceptar" onclick='javascript:RegresaDatos();'>
		</Portal:FormaBotones>

	</Portal:Forma>
	
	<script>
		function RegresaDatos(){
			var FBaja = document.frmRegistro.FBaja.value
			var TxMotivoBaja = document.frmRegistro.TxMotivoBaja.value			
			var padre = window.opener;
			padre.document.frmRegistro.FBaja.value = FBaja;
			padre.document.frmRegistro.TxMotivoBaja.value = TxMotivoBaja;
			padre.document.frmRegistro.Situacion.selectedIndex = 1;
			padre.document.frmRegistro.submit();
			window.close();
		}
	</script>
	
</Portal:Pagina>