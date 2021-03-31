<%-- Combobox --%>
.z-combobox-disabled, .z-combobox[disabled] {
	color: black !important; cursor: default !important; opacity: 1; -moz-opacity: 1; -khtml-opacity: 1; filter: alpha(opacity=100);
}

.z-combobox-disabled * {
	color: black !important; cursor: default !important;
}

.z-combobox-text-disabled {
	background-color: #ECEAE4 !important;
}
.z-comboitem {
	min-height:14px;
}
<%-- highlight focus form element --%>
input:focus, textarea:focus, .z-combobox-input:focus, z-datebox-input:focus {
	border: 1px solid #0000ff;
	background: #FFFFFF;
}

.z-textbox-readonly, .z-intbox-readonly, .z-longbox-readonly, .z-doublebox-readonly, 
.z-decimalbox-readonly, .z-datebox-readonly, .z-timebox-readonly, .editor-input-disd, 
.z-textbox[readonly], .z-intbox[readonly], .z-longbox[readonly], .z-doublebox[readonly], 
.z-decimalbox[readonly], .z-datebox[readonly], .z-timebox[readonly] {
	background-color: #F5F5F5;
}

.z-textbox[disabled], .z-intbox[disabled], .z-longbox[disabled], .z-doublebox[disabled], 
.z-decimalbox[disabled], .z-datebox[disabled], .z-timebox[disabled] {
	color: black !important;
	background-color: #F5F5F5 !important;
	cursor: default !important;
	opacity: 1 !important;
	border: 1px solid #cfcfcf !important;
}

.z-combobox-disabled *, 
.z-bandbox-disabled *, 
.z-datebox-disabled *, 
.z-timebox-disabled *, 
.z-spinner-disabled *, 
.z-doublespinner-disabled * {
    color: #AAAAAA !important;
    background: #F5F5F5 !important;
    cursor: default !important;
}

<%-- workaround for http://jira.idempiere.com/browse/IDEMPIERE-692 --%>
.z-combobox-popup {
	max-height: 200px;
}
