// (Popup Window Core/Window Control)

var xc_gc=null;function xc_cq(){return(window.opener&&typeof(window.opener.closed)!="undefined"&&!window.opener.closed&&window.opener.xcCore&&window.opener.xcCore==2)?window.opener:null};function xc_dr(gx,dy){xc_gc=xc_cq();if(xc_gc){xc_gc.xc_dr(gx,dy)}};function xc_dq(gx,dm){xc_gc=xc_cq();if(xc_gc){xc_gc.xc_dq(gx,dm)}};function xc_bc(gx){xc_gc=xc_cq();if(xc_gc){xc_gc.xc_bc(gx)}};function xc_ed(gx,date){xc_gc=xc_cq();if(xc_gc){xc_gc.xc_ed(gx,date)}};function xc_es(gx){xc_gc=xc_cq();if(xc_gc){xc_gc.xc_es(gx)}};function xc_cv(gx){xc_gc=xc_cq();if(xc_gc){xc_gc.xc_cv(gx)}};function xc_et(gx,y){xc_gc=xc_cq();if(xc_gc){xc_gc.xc_et(gx,y)}};function xc_eq(gx,m){xc_gc=xc_cq();if(xc_gc){xc_gc.xc_eq(gx,m)}};function xc_ev(){xc_gc=xc_cq();if(xc_gc){xc_gc.xc_ev(this.gx)}};function xc_ek(){xc_gc=xc_cq();if(xc_gc){xc_gc.xc_ek(this.gx)}};function xc_er(gx){xc_gc=xc_cq();if(xc_gc){xc_gc.xc_er(gx)}};function xc_cj(id){return id==""?null:document.getElementById(id)};function xc_eu(e,gx,name){xc_gc=xc_cq();var l=xc_cj("xcPopup");if(l&&xc_gc){var ik=location.search.substring(1);var id=gx?gx:/^id=(xc\d+)\&/.test(ik)?(RegExp.$1):(window.name||"");document.gx=id;document.onmouseover=xc_ek;document.onmouseout=xc_ev;document.title=unescape(name||ik.substring(ik.indexOf("title=")+6));xc_gc.xc_cy(l,id)}};window.onload=xc_eu;