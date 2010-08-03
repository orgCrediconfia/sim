<%@ page language="java"%>
<html>
	<head>
		<title>Calendario</title>
	</head>
	<meta content="text/html; charset=iso-8859-1" http-equiv=Content-Type>
	<link rel="stylesheet" href='/portal/comun/est/EstiloCalendario.css' TYPE="text/css">
	<script src='/portal/comun/lib/bBibliotecas.js'></script>
	<script src='/portal/comun/lib/bCalendar.js'></script>
	<script src='/portal/comun/lib/bFechaForma.js'></script>

	<% 
	String sFecha = "";
	String sContenedor = "";
	String sCampo = "";
	
	if(request.getParameter("fecha")!=null){
			sFecha=request.getParameter("fecha");
	}
	if(request.getParameter("contenedor")!=null){
			sContenedor = request.getParameter("contenedor") + ".";
	}
	if(request.getParameter("campo")!=null){
			sCampo = request.getParameter("campo") + ".";
	}
	%>

	<script language="JavaScript">
		function SeleccionaFecha(sFecha){
			var padre = window.opener;
			padre.document.<%=sContenedor + sCampo%>value= sFecha;
			padre.document.<%=sContenedor + sCampo%>focus();
		}
	</script>

	<body leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>
  		<form name="frmCalendario">
			<input name='fecha' type='hidden' size='1' value='<%=sFecha%>' >
			<table>
				<tr>
					<td>
						<DIV id=PopUpCalendar 100 z-index: ; ></DIV>
						<DIV id=monthDays onClick="bCierra=true;SeleccionaFecha(document.frmCalendario.fecha.value)"></DIV>
					</td>
				</tr>
			</table>
  		</form>
  
		<script languaje="javascript">
	 		getCalendarFor(document.frmCalendario.fecha,0)
			var bCierra = false;
		</script>
	</body>
</html>