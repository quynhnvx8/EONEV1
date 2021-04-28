<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>
<%@ taglib uri="http://www.idempiere.org/dsp/web/util" prefix="u" %>
.z-grid tbody tr.grid-inactive-row td.z-cell {
	background-image: none !important;
	background-color: #DCDAD4 !important;
}

.z-grid tbody tr.grid-inactive-row td.row-indicator-selected {
	background-color: #DCDAD4 !important;
	background-image: url(${c:encodeURL('/theme/default/images/EditRecord16.png')}) !important;
	background-position: center;
	background-repeat: no-repeat;
	background-size: 16px 16px;  
	cursor: pointer;
}
.z-grid tbody tr.grid-inactive-row span.row-indicator-selected.z-icon-Edit,
.z-grid tbody tr.highlight span.row-indicator-selected.z-icon-Edit {
	font-family: FontAwesome;
	font-size: larger;
	color: #333; 
}

.z-grid tbody tr.highlight td.z-cell { 
	background-color: #FFFFFF !important;
	background-image: none !important;
}

.z-grid tbody tr.highlight td.row-indicator-selected {
	background-color: #FFFFCC !important;
	background-image: url(${c:encodeURL('/theme/default/images/EditRecord16.png')}) !important;
	background-position: center;
	background-repeat: no-repeat;
	background-size: 16px 16px;  
	cursor: pointer;
}

.z-grid tbody tr.highlight td.row-indicator {
	background-color: transparent !important;
	background-image: none !important; 
}

div.z-column-cnt, div.z-grid-header div.z-auxheader-cnt {
	padding: 4px 2px 3px;
}

.z-grid-header {
    width: 100%;
    background: #F0F0F0;
    position: relative;
    overflow: hidden;
}


<%-- text overflow for grid cell --%>
.z-cell > span.z-label {
	overflow: hidden;
	text-overflow: ellipsis;
	display: inline-block;
	width: 100%;
	vertical-align: middle;
}

.z-listcell > div.z-listcell-content {
	overflow: hidden;
	text-overflow: ellipsis;
	display: inline-block;
	width: 100%;
}

.z-column-content, .z-listheader-content, .z-listcell-content {
	padding: 2px 2px;
}

.z-column-content {
    color: #101D52;
}

.z-grid-body .z-cell {
	padding: 0px 0px;
}

.z-grid-form {
	padding: 0px 10px 10px 40px;
}

.z-cell {
	border-left: 1px solid #cfcfcf;
	border-bottom: 1px solid #cfcfcf;
	padding: 0px 10px 10px 40px;
}


.z-frozen-body {
    overflow: hidden;
    float: left;
    background: #F5F5F5;
}

.z-frozen-inner {
    overflow-x: scroll;
    overflow-y: hidden;
    background: #F5F5F5;
}
.z-frozen-inner:hover {
    overflow-x: scroll;
    overflow-y: hidden;
    background: #F5F5F5;
}

.z-row, .z-listitem .z-listcell, .z-listitem.z-listitem-selected>.z-listcell {
	border-right: 1px solid #cfcfcf;
	
}

.z-grid-emptybody td {
	text-align: left;
}

.z-grid-body {
	background-color: #FFF;
}

<%-- grid layout --%>
.grid-layout {
	border: none !important; 
	margin: 0 !important; 
	padding: 0 !important;
	background-color: transparent !important;
}
.grid-layout .z-row-inner, .grid-layout .z-cell {
	border: none !important;
	background-color: transparent !important;
}
.grid-layout tr.z-row-over>td.z-row-inner, .grid-layout tr.z-row-over>.z-cell {
	border: none !important;
}
.grid-layout tr.z-row-over>td.z-row-inner, .grid-layout tr.z-row-over>.z-cell {
	background-image: none !important;
}

.z-row:hover > .z-row-inner, .z-row:hover > .z-cell 
{
	background:none; !important
}

tbody.z-grid-empty-body td {
	text-align: left;
}

tbody.z-listbox-empty-body td {
	text-align: left;
}

div.z-listbox-body .z-listcell {
	padding: 2px;
}

.z-listbox-autopaging .z-listcell-cnt {
	height: 20px;
}
@media only screen 
  and (max-device-width: 700px) {
	.z-listcell > div.z-listcell-content {
		white-space: pre-line;
	}	
}
.z-column {
    padding: 0;
    background: #F0F0F0;
    position: relative;
    overflow: hidden;
    white-space: nowrap;
}

.z-columns-bar {
    background: #FEFEFE;
    background: #F0F0F0;
    /*border-left: 1px solid #CFCFCF;*/
    /*border-bottom: 1px solid #CFCFCF;*/
}

z-textbox, .z-decimalbox, .z-intbox, .z-longbox, .z-doublebox {
    font-family: Arial,Sans-serif;
    font-size: 12px;
    font-weight: normal;
    font-style: normal;
    color: #000000;
    min-height: 24px;
   
    border-radius: 0px;
    margin: 0;
    padding: 4px 5px 3px;
    line-height: 14px;
    background: #FFFFFF;
}
