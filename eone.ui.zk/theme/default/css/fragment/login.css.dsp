.login-window {
	background-color: #E5E5E5;
}

.login-window .z-window-content {
	background-color: #E5E5E5;
}

.login-box-body {
	width: 660px;
	background-image: url(../images/login-box-bg.png);
	background-repeat: repeat-y;
	background-color: transparent;
	z-index: 1;
	padding: 0;
	margin: 0;
	text-align: center;
	padding-bottom: 100px;
}

.login-box-header {
	background-image: url(../images/login-box-header.png);
	background-color: transparent;
	z-index: 2;
	height: 54px;
	width: 660px;
}

.login-box-header-txt {
	color: white !important;
	font-weight: bold;
	position: relative;
	top: 30px;
}

.login-box-header-logo {
	padding-top: 20px;
	padding-bottom: 25px;
}

.login-box-footer {
	background-image: url(../images/login-box-footer.png);
	background-position: top right;
	background-attachment: scroll;
	background-repeat: repeat-y;
	z-index: 2;
	height: 110px;
	width: 660px;
}

.login-box-footer .confirm-panel {
	width: 600px !important;
}

.login-box-footer-pnl {
	width: 604px;
	margin-left: 10px;
	margin-right: 10px;
	padding-top: 40px !important;
}

.login-label {
	color: black;
	text-align: right;
	width: 40%;
}

.login-field {
	text-align: left;
	width: 55%;
}

.login-btn {
	padding: 4px 20px !important;
}

.login-east-panel, .login-west-panel {
	width: 350px;
	background-color: #FFFFFF;
	position: relative;
}

/*EONE*/
.login-div{
	width: 100%;
	height: 500px;
	background: #F0F0F0;
    color: #04609E;
    padding:5px;
    position: relative;	
}

.sologan-div{
	float: none;
    height: 70px;
    margin: 100px auto 15px;
    width: 420px;
}

.log-three-box{
	margin: 10px auto 15px;
	width: 250px;
	border-radius: 5px;
    color: #04609E;
    display: table;
    padding: 25px 0 10px 10px;
    margin: 10px auto 15px;
}

.role-text{
	color:#071a28;  
    font-family: Tahoma;
    font-size:13px;
    font-weight:nomarl;
}

.login-common{
	padding: 6px 0 6px 0px;
	width:100%;
	float:left;
}
.login-button{
	margin-left:91px;
	float:left;
	
}
.login-label-div{
	float: left;
    margin-left: 30px;
    margin-right: 12px;
    padding-top: 3px;
    padding-left: 7px;
}
.login-label-up-div{
	float: left;
    margin-left: 30px;
    margin-right: 20px;
    padding-left: 22px;
    padding-top: 3px;
}
.login-label-up-divp{
	float: left;	
    margin-left: 30px;
    margin-right: 21px;
    padding-left: 23px;
    padding-top: 3px;
}
.login-remember{
	margin-left:88px;
	float:left
}
.login-text-footer{
	color: #888888;    
    font-family: Tahoma;      
    line-height: 38px;    
    text-align: center;
    margin-left:20px;
    float:center;    
}

/*END EONE*/

.z-north-header, 
.z-south-header, 
.z-west-header, 
.z-center-header, 
.z-east-header {
    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
    font-size: 12px;
    font-weight: bold;
    font-style: normal;
    color: #555555;
    height: 32px;
    border-bottom: 1px solid #CFCFCF;
    padding: 4px 4px 3px;
    line-height: 24px;
    background: #FFFFFF;
    overflow: hidden;
    cursor: default;
    white-space: nowrap;
}

@media screen and (max-width: 659px) {
	.login-box-body, .login-box-header, .login-box-footer {
		background-image: none;
		width: 90%;
	}
	.login-box-footer .confirm-panel, .login-box-footer-pnl {
		width: 90% !important;
	}
	.login-box-header-txt {
		display: none;
	}
}
@media screen and (max-height: 600px) {
	.login-box-header-txt {
		display: none;
	}
	.login-box-body, .login-box-header, .login-box-footer {
		background-image: none;
	}
	.login-box-body {
		padding-bottom: 10px;
	}
	.login-box-header {
		height: 0px;
	}
}
@media screen and (max-width: 359px) {
	.login-window .z-center > .z-center-body .z-window.z-window-embedded > .z-window-content {
		padding: 0px
	}
}
