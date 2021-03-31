.z-button {
  display: inline-block;
  margin: 0px;
  padding: 0px 10px;
  font-size: 12px;
  line-height: 20px;
  text-align: center;
  vertical-align: middle;
  cursor: pointer;
  background-color: #f5f5f5;
  border: 1px solid #cccccc;
  
}

.z-paging-button {
    font-family: Arial,Sans-serif;
    font-size: 12px;
    font-weight: normal;
    font-style: normal;
    color: #2184BA;
    display: inline-block;
    min-width: 24px;
    height: 24px;
    border: 1px solid #CFCFCF;
    -webkit-border-radius: 3px;
    -moz-border-radius: 3px;
    -o-border-radius: 3px;
    -ms-border-radius: 3px;
    border-radius: 3px;
    margin-right: 6px;
    padding: 4px 0px;
    line-height: 14px;
    background: #F9F9F9;
    background: -moz-linear-gradient(top, #FEFEFE 0%, #EEEEEE 100%);
    background: -o-linear-gradient(top, #FEFEFE 0%, #EEEEEE 100%);
    background: -ms-linear-gradient(top, #FEFEFE 0%, #EEEEEE 100%);
    text-align: center;
    vertical-align: top;
    cursor: pointer;
    text-decoration: none;
    white-space: nowrap;
}

.z-button {
	margin: 0px !important;
	background: #F9F9F9;
}

.z-button-hover,
.z-button-focus,
.z-button-click,
.z-button-disabled {
  background: #e6e6e6;
}

.z-button-click {
  background-color: #cccccc;
}

.z-button-hover,
.z-button-focus {
  color: #333333;
  text-decoration: none;
  
  
}

.z-button-focus {
  outline: 5px auto -webkit-focus-ring-color;
}

.z-button-click {
  background-image: none;
  outline: 0;
  -webkit-box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.15), 0 1px 2px rgba(0, 0, 0, 0.05);
     -moz-box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.15), 0 1px 2px rgba(0, 0, 0, 0.05);
          box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.15), 0 1px 2px rgba(0, 0, 0, 0.05);
}

.z-button-disabled {
  cursor: default;
  background-image: none;
  opacity: 0.65;
  filter: alpha(opacity=65);
  -webkit-box-shadow: none;
     -moz-box-shadow: none;
          box-shadow: none;
}

.z-button.btn-small {
	padding: 1px 5px;
}
.z-button.btn-medium {
	padding: 2px 10px;
}

.z-button-disabled {
	color: black; cursor: default; opacity: .6; -moz-opacity: .6; -khtml-opacity: .6; filter: alpha(opacity=60);
}

.img-btn{
	text-align: center;
	cursor: pointer; 
	/*width:24px;*/ 
	height:24px;
}

.img-btn img {
	height: 22px;
	width: 22px;
	background-color: transparent;
}

.txt-btn img, .small-img-btn img {
	height: 16px;
	width: 16px;
	background-color: transparent;
	vertical-align: middle;
	display: inline-block;
}

.btn-sorttab {
	box-shadow: 0px 0px 4px #bbb;
	color: #555;
	border: solid 1px #bbb;
	text-shadow: 0px 1px 2px #888;
}

.z-button [class^="z-icon-"],
.z-button-os [class^="z-icon-"]{
	font-size: larger;
	color: #333;	
	padding-left: 2px;
	padding-right: 2px;
}
.z-button.xlarge-toolbarbutton [class^="z-icon-"] {
	font-size: 24px;
}
.z-button.large-toolbarbutton [class^="z-icon-"] {
	font-size: 20px;
}
.z-button.medium-toolbarbutton [class^="z-icon-"] {
	font-size: 16px;
}
.z-button.small-toolbarbutton [class^="z-icon-"] {
	font-size: 12px;
}
.z-button {
	vertical-align: middle;
	text-align: center;
}
.btn-ok.z-button [class^="z-icon-"]:before {
	color: green;	
}
.btn-cancel.z-button [class^="z-icon-"]:before {
	color: red;	
}