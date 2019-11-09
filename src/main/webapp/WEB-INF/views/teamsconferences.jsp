<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Injury Tool</title>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <link rel="stylesheet" type="text/css" href="css/all.css">
    <script src="js/jquery.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/all.js"></script>
    <script src="js/common.js"></script>
    <script src="js/teamsconferences.js"></script>
    <script src="js/home.js"></script>
    
    <style>
        .table-first {width: 100%;}
        .table_show {display: inline-block;width: 100%;}
        .table_col {width: 16.66%;float: left;}
        .table_col_2 {display: inline-block;width: 83%;}
        header.padding-50 {padding: 30px 0 0;}
        .table_show table th select:focus {background-color: transparent;color: #ff0;box-shadow: none;}
        .table_responsive {max-width: 767px;}
        .table tbody tr:nth-child(2n+1) {background-color: #b4b4b94f !important;}
		.menus ul{margin: 0}
		.table_responsive .table {border-color: #5c6065; background: #343a40;}
		.table_responsive thead select#Line, .table_responsive thead select {
	    background: #343a40 url(images/br_down.png) no-repeat calc(100% - 10px);
	    padding-left: 0;height: 30px;padding: 0;}
   
   		.menus li {	width: 140px; }
		.menus li a { padding: 4px 0; display: inline-block; width: 100%; } 
		.tabler_heading select:focus{outline: none; box-shadow: none; background-color: #000; color:white;}
		.tabler_heading select{
		font-size: 12px;
		padding-left: 5px;
		height: 25px;
		width: auto;
		border-color: #cccccc;
		height: 28px;
		padding: 0 27px 0 11px;
		padding-left: 11px;
		-webkit-appearance: none;
		-moz-appearance: none;
		appearance: none;
		color: #fff;
		background: #000 url(images/br_down.png) no-repeat calc(100% - 10px);
		background-size: 12px;}   
		.tabler_heading select option{background: #000; color: #fff;}

		#minutesconf{
		background: #343a40  url(images/br_down.png) no-repeat calc(100% - 10px);
		padding-left: 0;
		height: 30px;
		padding: 0;
		color: white;
		border: none;
		color: #ff0;
		width: auto;
		min-width: 150px;
		font-weight: bold;
		font-size: 12px;}
		
		
    	.link { color: #FFFFFF; }
  
		
		#conftable tbody tr td:first-child, #conftable thead tr:first-child th:first-child {
    text-align: left;
    padding-left: 10px;
    font-family: 'OpenSans-SemiBold';}
    
    #teams option, #conferences option, #minutesteam option, #minutesteamconf option{ background: #343a40; color:white}
    </style>
<link rel="shortcut icon" href="#">
</head>
<body>
    <div class="wrapper_container">
        <!-- header -->

        <!-- header -->
        <header class="padding-50">

            <div class="container">
            
          <!--   <input type="button" id="testrequest" value="check"> -->
            
                <div class="row">

                    <!-- logo -->
                    <div class="col-12 col-sm-6 col-md-3 col-lg-2">

                        <div class="tabler_heading ">
                            <h3 class="c-white r-m">Injury Grid</h3>
							<select class="form-control d-inline-block font-18 font-14" id="sportsname">
								<option value="nfl">NFL</option>
								<option value="ncaaf">NCAAF</option>
								<option value="nba">NBA</option>
								<option value="ncaab">NCAAB</option>
								<option value="mlb">MLB</option>
								<option value="nhl">NHL</option>
							</select>
						</div>
                    </div>
                    <!-- end  -->

                    <!-- logo -->
                    <div class="col-12 col-sm-6 col-md-3 col-lg-10">
                        <div class="">
                            <div class="logo">
                                <a href="#"><img src="images/logo.png" class="img-fluid" alt="NBA"></a>
                            </div>
                        </div>
                    </div>

                    <!-- color picker -->

                </div>
            </div>
        </header>

        <!-- end header -->

        <!-- table -->
        <div class="table_wrapper">
            <div class="container">
                <div class="w-100 d-inline-block">
                    <div class="menus">
                        <ul>
                            <li id="byteams" class="d-inline-block active"><a href="javascript:changetab('alpha')">Alphabetical</a></li>
                            <li id="byconf" class="d-inline-block "><a href="javascript:changetab('conf')">Conferences</a></li>
                        </ul>
                    </div>
                    <div class="table_responsive" id="teamtable">
                        <div class="table_show">
                            <table class="table table-borderless mb-0">
                                <thead>
                                    <tr>
                                        <th>
                                            <select class="form-control d-inline-block font-18 font-14 " id="teams">
                                                <option>Abilene Christian </option>
                                                <option>Akron</option>
                                                <option>Alabama</option>
                                            </select>
                                        </th>
                                        <th>
                                            <select class="form-control d-inline-block font-18 font-14 " id="conferences">
                                                <option>Conf</option>
                                                <option>Abilene Christian </option>
                                                <option>Akron</option>
                                                <option>Alabama</option>
                                            </select>
                                        </th>
                                        <th>
                                        <span class="d-block">Minutes Lost</span>
                                            <select class="form-control d-inline-block font-18 font-14" id="minutesteam">
                                                <option>Per Game</option>
                                                 <option>Last Game</option>
                                                 <option>Last 3 Games</option>
                                                 <option>Last 5 Games</option>
                                                 <option>Last 10 Games</option>
                                                 <option>Last 20 Games</option>
                                                 <option>Season</option>
                                            </select>
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Abilene Christian</td>
                                        <td>MWC</td>
                                        <td>19</td>

                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!--  table 2 By Conference -->
                    <div class="table_responsive by_conferance" id="by_conferance">
                        <div class="table_show">
                            <table class="table table-borderless mb-0" id="conftable">
								<thead>
									<tr>
                                        <th></th>
                                        <th>
                                        <span class="d-block">Minutes Lost</span>
                                            <select class="form-control d-inline-block font-18 font-14" id="minutesteamconf">
                                                <option>Per Game</option>
                                                 <option>Last Game</option>
                                                 <option>Last 3 Games</option>
                                                 <option>Last 5 Games</option>
                                                 <option>Last 10 Games</option>
                                                 <option>Last 20 Games</option>
                                                 <option>Season</option>
                                            </select>
                                        </th>
                                    </tr>
								</thead>	
                                <tbody>
                                
                                    <tr>
                                        <td class="font-first" >Air Force</td>
                                        <td>14.1</td>
                                    </tr>
                                    <tr>
                                        <td>Akron</td>
                                        <td>14.1</td>
                                    </tr>
                                  <tr>
                                        <td class="font-first" >Air Force</td>
                                        <td>14.1</td>
                                    </tr>
                                    <tr>
                                        <td>Akron</td>
                                        <td>14.1</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

 </body>
</html>