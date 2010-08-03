<%@ page language="java" import="com.rapidsist.portal.configuracion.Usuario"%>
<%@ taglib uri="Portal" prefix="Portal" %>


<Portal:Pagina menu="false">
	<Portal:PaginaNombre titulo="Sistema Integral de Microfinanciamiento" subtitulo="Acceso al sistema"/>

	<Portal:Forma tipo='url' funcion='ninguna' url="j_security_check" agregaentorno="false">
		<Portal:FormaSeparador nombre="Datos para acceder al portal"/>
		<Portal:FormaElemento etiqueta='Usuario' control='Texto' controlnombre='usuario' controllongitud='60' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Password' control='etiqueta'>
			<input type="password" name="j_password" size="60" maxlength="50"/>
		</Portal:FormaElemento>
		<Portal:FormaBotones>
			<input type="button" value="Aceptar" onClick="javascript:ConcatenaCadena();">
		</Portal:FormaBotones>
	<Portal:FormaElemento etiqueta='Ninguna'  control='variableoculta'  controlnombre='j_username' controlvalor=''  controllongitud='50' controllongitudmax='200' editarinicializado='true' obligatorio='false' />
	<%Usuario usuario = (Usuario)session.getAttribute("Usuario");%>
	
	<Portal:FormaElemento etiqueta='Ninguna'  control='variableoculta'  controlnombre='CveGpoEmpresa' controlvalor='<%=usuario.sCveGpoEmpresa%>'  controllongitud='50' controllongitudmax='200' editarinicializado='true' obligatorio='false' />
	</Portal:Forma>
	<script languaje="javascript">
	    //DESACTIVA EL BOTON DE BACK
		if (history.forward(1)){location.replace(history.forward(1))} 
		function ConcatenaCadena(){
		 	sCadena = document.frmRegistro.CveGpoEmpresa.value + document.frmRegistro.usuario.value;
			document.frmRegistro.j_username.value = sCadena;
		 	document.frmRegistro.submit();
		}
		document.frmRegistro.usuario.focus();
	</script>
</Portal:Pagina>
