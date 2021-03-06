.mandatory-decorator-text {
	text-decoration: none; font-size: xx-small; vertical-align: top; color:red;
}

.editor-box {
	display: inline-block;
	border: none; 
	padding: 0px; 
	margin: 0px; 
	background-color: transparent;
	position: relative;
}

.editor-input {
	box-sizing: border-box;
	-moz-box-sizing: border-box; /* Firefox */
	display: inline-block;
	/*padding-right: 22px;*/ 
	width: 100%;
	height: 21px;
}
.editor-input.mobile.z-decimalbox {
	padding-right: 5px;
}

.editor-input:focus {
	border: 1px solid #0000ff;
}

.editor-input-disd {
	padding-right: 5px !important;
}
	
.editor-button {
    padding: 0px;
    border-radius: 0px 4px 4px 0px;
    box-shadow: none;
    margin: 3px 0px;
    display: grid;
    background-color: white;
    background-image: none;
    width: 20px;
    height: 18px;
    min-height: 18px;
    border: none;
    position: absolute;
    right: 1px;
    top: 4px;
}

.editor-button :hover {
	-webkit-filter: contrast(1.5);
	filter: contrast(150%);
}

.editor-button img {
	vertical-align: middle;
	text-align: left;
	width: 18px;
	height: 18px;
	padding: 1px 1px;
}


.editor-box .grid-editor-input.z-textbox {
}

.grid-editor-button {
}

.grid-editor-button img {
}

.number-box {
	display: inline-block; 
	white-space:nowrap;
}

.number-box .grid-editor-input.z-decimalbox {
}


.datetime-box {
	display: inline-block;
    min-height: 24px;
    line-height: normal;
    white-space: nowrap;
    position: relative;
    width: 70%;
}

.z-datebox {
	display: inline-block;
    min-height: 24px;
    line-height: normal;
    white-space: nowrap;
    position: relative;
    width: 100%;
    height:24px;
}

span.grid-combobox-editor {
	width: 100% !important;
	position: relative;
}

.grid-combobox-editor input {
    width: 100% !important;
    padding-right: 20px;
    border: 1px solid #cfcfcf;
}

.grid-combobox-editor.z-combobox-disabled input {
	border-right: 1px solid #cfcfcf;
	padding-right: 5px;
}

.grid-combobox-editor {
	position: absolute;
	right: 1px;
	top: 1px;
}



.grid-combobox-editor input:focus {
	border-right: 0px;
}
	
.grid-combobox-editor input:focus + .z-combobox-button {
	border-left: 1px solid #0000ff;
}

<%-- payment rule --%>
.payment-rule-editor {
	display: inline-block;
	border: none; 
	padding: 0px; 
	margin: 0px; 
	background-color: transparent;
	position: relative;
}
.payment-rule-editor .z-combobox {
	width: 100%;
}
.payment-rule-editor .z-combobox-input {
	display: inline-block;
	padding-right: 44px; 
	width: 100%;
	height: 24px;
	border-right: 0px;
}
.payment-rule-editor .z-combobox-input:focus {
	border: 1px solid #0000ff;
}
.payment-rule-editor .z-combobox-input.editor-input-disd {
	padding-right: 22px !important;
}
.payment-rule-editor .z-combobox-button {
	position: absolute;
	right: 0px;
	top: 1px;
}
.payment-rule-editor .z-combobox .z-combobox-button-hover {
	background-color: #ddd;
	background-position: 0px 0px;
}
.payment-rule-editor .editor-button {
	right: 24px;
}

<%-- chart --%>
.chart-field {
	padding: 10px; 
	border: 1px solid lightgray !important;
}

.field-label {
	position: relative; 
	float: right;
}

.image-field {
	cursor: pointer;
	border: 1px solid #C5C5C5;
	height: 180px;
	width: 270px;
	border-radius: 5px;
	padding: 5px;
	margin: 25px;
}
.image-field.image-field-readonly {
	cursor: default;
	border: none;
}
.image-fit-contain {
	object-fit: contain;
}
.z-cell.image-field-cell {
	z-index: 1;
}

.html-field {
	cursor: pointer;
	border: 1px solid #C5C5C5;
	overflow: auto;
}

.dashboard-field-panel.z-panel, .dashboard-field-panel.z-panel > .z-panel-body,  .dashboard-field-panel.z-panel > .z-panel-body > .z-panelchildren  {
	overflow: visible;
}

.idempiere-mandatory, .idempiere-mandatory input, .idempiere-mandatory a {
    border-color:red;
}

.idempiere-label {
    color: #333;
}

.idempiere-mandatory-label{
   color: red!important;
}

.idempiere-zoomable-label {
    cursor: pointer; 
    text-decoration: underline;
}

.z-combobox-button, 
.z-bandbox-button, 
.z-spinner-button, 
.z-doublespinner-button {
    font-size: 14px;
    color: #636363;
    border: 0px solid #CFCFCF;
    display: inline-block;
    position: absolute;
    top: 1px;
    right: 1px;
    min-width: 20px;
    min-height: 20px;
    height: 20px;
    border-radius: 0 3px 3px 0;
    background: #FFFFFF;
    text-align: left;
    vertical-align: middle;
    overflow: hidden;
    cursor: pointer;
    padding-left: 7px;
    padding-top: 5px;
    margin: 5px 0px;
}

.z-datebox-button, 
.z-timebox-button
 {
    font-size: 14px;
    color: #636363;
    border: 0px solid #CFCFCF;
    display: inline-block;
    position: absolute;
    top: 1px;
    right: 1px;
    min-width: 20px;
    min-height: 20px;
    height: 20px;
    border-radius: 0 3px 3px 0;
    background: #FFFFFF;
    text-align: left;
    vertical-align: middle;
    overflow: hidden;
    cursor: pointer;
    padding-left: 7px;
    padding-top: 5px;
    margin: 1px 0px;
}

.z-combobox-input, 
 
.z-spinner-input, 
.z-doublespinner-input {
    font-family: Arial,Sans-serif;
    font-size: 12px;
    font-weight: normal;
    font-style: normal;
    color: #000000;
    width: 100%;
    height: 24px;
    border: 1px solid #CFCFCF;
    margin: 5px 0px;;
    padding: 4px 5px;
    padding-right: 29px;
    line-height: 14px;
    background: #FFFFFF;
}

.z-bandbox-input {
    font-family: Arial,Sans-serif;
    font-size: 12px;
    font-weight: normal;
    font-style: normal;
    color: #000000;
    width: 100%;
    height: 24px;
    border: 1px solid #CFCFCF;
    margin: 5px 3px;;
    padding: 4px 5px;
    padding-right: 29px;
    line-height: 14px;
    background: #FFFFFF;
}

.z-datebox-input, 
.z-timebox-input{
    font-family: Arial,Sans-serif;
    font-size: 12px;
    font-weight: normal;
    font-style: normal;
    color: #000000;
    width: 100%;
    height: 24px;
    border: 1px solid #CFCFCF;
    margin: 0;
    padding: 4px 5px;
    line-height: 14px;
    background: #FFFFFF;
}

.z-textbox {
    font-family: Arial,Sans-serif;
    font-size: 12px;
    font-weight: normal;
    font-style: normal;
    color: #000000;
    min-height: 24px;
    border: 1px solid #CFCFCF;
    margin: 0;
    padding: 4px 5px 3px;
    line-height: 14px;
    background: #FFFFFF;
    height: 24px;
}

.z-decimalbox, 
.z-intbox, 
.z-longbox, 
.z-doublebox {
    font-family: Arial,Sans-serif;
    font-size: 12px;
    font-weight: normal;
    font-style: normal;
    color: #000000;
    min-height: 24px;
    border: 1px solid #CFCFCF;
    margin: 0;
    padding: 4px 5px 3px;
    padding-right: 20px;
    line-height: 14px;
    background: #FFFFFF;
}