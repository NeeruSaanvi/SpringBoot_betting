<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>NBA</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/all.css">
<link rel="stylesheet" type="text/css" href="css/jquery-ui.css">
<script src="js/jquery.min.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/all.js"></script>
<script src="js/home.js"></script>
<style>
h1 {
  font-weight: bold;
  color: #0070c0;
  font-size: 56px;
}
</style>
</head>
<body class="bg_white">
<div class="wrapper_container"> 
  <!-- header -->
  <div class="home">

    
    <!-- end header -->
    
    <div class="table_wrapper">
      <div class="container">
        <div class="center_blog">
          <div class="w-100 d-inline-block text-center c-white">
            <div class="logo"> <a href="#"><img src="images/logo_black.png" class="img-fluid" alt="NBA"></a> </div>
            <h1 class="c-white">Injury Grid</h1>
            <div class="inner">
              <div class="flex_content">
                <ul class="sales_list">
                  <li><a href="#" onclick="callTeamConfrence('nfl')">NFL</a></li>
                  <li><a href="#" onclick="callTeamConfrence('nba')">NBA</a></li>
                  <li><a href="#" onclick="callTeamConfrence('ncaaf')">College Football</a></li>
                  <li><a href="#" onclick="callTeamConfrence('ncaab')">College Basketball</a></li>
                  <li><a href="#" onclick="callTeamConfrence('mlb')">MLB</a></li>
                  <li><a href="#" onclick="callTeamConfrence('nhl')">NHL</a></li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript">
        $(document).ready(function() {

            $("#part_1").hide();
            $("#btn_1").click(function() {
                $("#part_2").hide();
                $("#part_1  ").show("slide", {
                    direction: 'right'
                }, 1000);
            });
            $("#btn_2").click(function() {
                $("#part_1").hide();
                $("#part_2").show("slide", {
                    direction: 'left'
                }, 1000);
            });

            $("#tab_1").hide();
            $("#btn_3").click(function() {
                $("#tab_2").hide();
                $("#tab_1").show("slide", {
                    direction: 'right'
                }, 1000);
            });
            $("#btn_4").click(function() {
                $("#tab_1").hide();
                $("#tab_2").show("slide", {
                    direction: 'left'
                }, 1000);
            });

        });
    </script>
</body>
</html>