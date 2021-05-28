<%-- header --%>
.desktop-header-left {
	margin: 0;
	margin-left: 1px;
	margin-top: 1px;
	background-color: transparent !important; 
	border: none !important;
}

.desktop-header-right {
	margin: 0;
	margin-top: 1px;
	padding-right:1px;
	background-color: transparent !important; 
	border: none !important;
}

.desktop-header {
	background-color: #FFFFFF;
	width: 100%;
	height: 40px;
	border-bottom: 1px solid #C5C5C5 !important;
	padding-left: 4px;
}
.desktop-header.mobile {
	height: 36px;
	padding: 4px 4px;
}

.desktop-header-font {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10px;
}

.desktop-header-popup {
	width: 800px;
	border-radius: 3px;
	border: 1px solid #d5d5d5;
	border-right: 1px solid #d5d5d5;
	border-bottom-width: 1px;
	right: 1px;
}

.desktop-header-popup .desktop-header {
	border: none;
	height: 100% !important;
}

.desktop-header-username {
	padding-right: 6px;
}

.desktop-header-username:hover {
	color: blue;
	text-decoration: underline;
}

.desktop-user-panel {
	float: right;
}
.desktop-user-panel.mobile .desktop-header-font.desktop-header-username {
	font-weight: 600;
}

.desktop-layout {
	position: absolute; 
	border: none;
	background-color: #FFFFFF;
}

.desktop-tabbox {
	padding-top: 0px; 
	background-color: #FFFFFF;
}

.desktop-tabbox .z-tabs-content {
    width: 100%;
}

.desktop-tabbox .z-tab {
	height: 24px;
	#backgroup: #0093F9;
}

.z-tabs-content {
    display: table;
    width: 100%;
    border-collapse: separate;
    border-spacing: 0;
    border-bottom: 1px solid #CFCFCF;
    margin: 0;
    padding-left: 0;
    padding-top: 0;
    list-style-image: none;
    list-style-position: outside;
    list-style-type: none;
    zoom: 1;
    clear: both;
    min-height: 25px;
}

.z-tab {
	font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
    font-size: 12px;
    display: block;
    font-color: #FFFFFF;
    border: 1px solid #CFCFCF;
    border-width: 0px 1px 0px 1px;
    margin: 0;
    padding-top: 1px;
    line-height: 30px;
    #background: #0093F9;
    <!--F3F3F3-->
    text-align: center;
    position: relative;
    cursor: pointer;
    float: left;
    border-top-left-radius: 3px;
	border-top-right-radius: 3px;
}
.z-tab:hover {
	background: #7AC8FF;
	border-top-left-radius: 3px;
	border-top-right-radius: 3px;  
}

.z-tabbox-top > .z-tabs .z-tabs-content {
    white-space: nowrap;
    #background: #0093F9;
}

.z-tab-unselected {
	border-top-left-radius: 3px 3px;
	border-top-right-radius: 3px 3px;
}
@media screen and (max-width: 360px) {
	.desktop-tabbox .z-tab {
		max-width: 190px;
	}
}

.desktop-tabbox .z-tab-selected {
	height: 25px;
	border-top-left-radius: 3px;
	border-top-right-radius: 3px;
	background: #7AC8FF;
}

.desktop-tabbox .z-tab .z-tab-text {
	padding-top: 0px;
	padding-bottom: 0px;
}

.desktop-tabbox > .z-tabpanels {
	flex-grow: 1 1 0;
}

.z-tabpanel {
    border: 0px solid #CFCFCF;
    border-top: 0px;
    padding: 5px;
    zoom: 1;
}

.desktop-north, .desktop-center {
	border: none;
}

.desktop-center {
	padding-top: 1px;
	#background-color: #0093F9;
}

.desktop-tabpanel {
	margin: 0;
	padding: 0;
	border: 0;
	position: relative !important;
	background-color: #FFFFFF
}

.desktop-left-column {
	border: none;
	border-right: 0px solid #C5C5C5;
	padding-top: 2px; 
}

.desktop-right-column {
	width: 220px;
	border: none;
	border-left: 1px solid #C5C5C5;
	background-color: #FFFFFF;
	padding-top: 2px; 
}

.desktop-left-column + .z-west-splitter {
	border-top: none; 
	border-right: 0px solid #c5c5c5;
	#background: #0093F9;
}

.desktop-right-column + .z-east-splitter,  .desktop-right-column.z-east {
	border-top: none; 
	border-left: 1px solid #c5c5c5;
}

.desktop-left-column .z-west-body {
	border-right: none;
}

.desktop-right-column .z-east-body {
	border-left: none;
}

.desktop-layout > div > .z-west-collapsed, .desktop-layout > div > .z-east-collapsed {
	border-top: none;
}

.desktop-left-column .z-anchorlayout, .desktop-right-column .z-anchorlayout {
	overflow-x: hidden;
}

.z-anchorlayout { overflow:auto }
 
.z-anchorchildren { overflow:visible }

.desktop-hometab {
	margin-left: 0px !important;
	border-top-left-radius: 3px;
	border-top-right-radius: 3px;
}

.desktop-tabbox .z-toolbar-tabs-body {
	padding: 0px !important;
	margin: 0px !important;
}

.desktop-tabbox .z-toolbar-tabs-body .z-toolbarbutton {
	padding: 0px !important;
	/*border: 1px solid transparent !important;*/
	margin: 0px !important;
}

.desktop-tabbox .z-toolbar-tabs .z-toolbarbutton-hover {
	border: none !important;
	padding: 0px !important;
	margin: 0px !important;
}

.desktop-tabbox .z-toolbar-tabs .z-toolbarbutton-hover .z-toolbarbutton-content {
	background-image: none !important;
	background-color:#DDD !important;
	padding: 0px !important;
	margin: 0px !important;
	-webkit-box-shadow:inset 0px 0px 3px #CCC;
	-moz-box-shadow:inset 0px 0px 3px #CCC;	
	-o-box-shadow:inset 0px 0px 3px #CCC;	
	-ms-box-shadow:inset 0px 0px 3px #CCC;	
	box-shadow:inset 0px 0px 3px #CCC;
}

.z-tabs{
	border: none !important;
	margin: 0px !important;
	padding: 0px !important;
	background-image: none !important;
	background-color:#F9F9F9 !important;
	min-height: 25px;	
}

.desktop-menu-popup {
	z-index: 9999;
	background-color: #fff;
}

.desktop-menu-toolbar {
	background-color: #ffffff; 
	verticle-align: middle; 
	padding: 2px;
	border-top: 1px solid #c5c5c5;
}

.desktop-home-tabpanel {
	background-color: #FFFFFF;
	width: 100% !important;
}

.link {
	cursor:pointer;
	padding: 2px 2px 4px 4px;
	border: none !important;
}

.link.z-toolbarbutton:hover {
	border: none !important;
	background-image: none !important;
	text-decoration: underline;
}

.link.z-toolbarbutton:hover span {
	color: blue;
}

.desktop-home-tabpanel .z-panel-head {
	background-color: #FFFFFF;
}

<%-- window container --%>
.window-container-toolbar-btn.z-toolbarbutton .z-toolbarbutton-content img {
	width: 16px;
	height: 16px;
	padding: 3px 3px;
}
.window-container-toolbar-btn.z-toolbarbutton .z-toolbarbutton-content [class^="z-icon"] {
	width: 22px;
	height: 22px;
	padding: 3px 3px;
}
.window-container-toolbar > .z-toolbar-content,
.window-container-toolbar-btn.z-toolbarbutton, 
.window-container-toolbar-btn.z-toolbarbutton .z-toolbarbutton-content,
.window-container-toolbar-btn.z-toolbarbutton .z-toolbarbutton-content [class^="z-icon"] {
	display:inline-flex;
	align-items: center;
	justify-content: center; 
	border-bottom: 0px; 
}

.z-toolbar.z-toolbar-tabs {
    background-color: #F9F9F9;
    position: absolute;
    right: 0;
    top: 0;
    overflow: hidden;
    z-index: 1;
    min-height: 48px;
}

.user-panel-popup .z-popup-content {
	padding-left: 0px;
	padding-right: 0px;
}
.user-panel-popup .z-popup-content > .z-vlayout {
	overflow-x: auto;
	padding: 8px;
}

.z-panelchildren {
    position: relative;
    padding: 1px;
    overflow: hidden;
    zoom: 1;
}

.z-north-body, .z-south-body, .z-west-body, .z-center-body, .z-east-body {
    line-height: 16px;
    padding: 0px;
    color: rgba(0,0,0,0.9);
}